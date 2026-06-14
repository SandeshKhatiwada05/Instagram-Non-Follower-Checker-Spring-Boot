package com.mainframe.instagram.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mainframe.instagram.model.AnalysisResult;
import com.mainframe.instagram.model.InstagramUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class InstagramService {

    private static final Logger log = LoggerFactory.getLogger(InstagramService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Filter static non-username endpoints out of safety
    private static final Set<String> SKIP = Set.of(
            "accounts", "explore", "reels", "stories", "p", "tv", "reel", "about",
            "legal", "privacy", "help", "press", "api", "blog", "jobs", "terms", "directory",
            "_u", "_n", "_i"
    );

    public AnalysisResult analyze(MultipartFile followersFile, MultipartFile followingFile) {
        log.info("=== ANALYZE START (JSON ONLY) ===");
        log.info("followersFile: name={}, size={}", followersFile.getOriginalFilename(), followersFile.getSize());
        log.info("followingFile: name={}, size={}", followingFile.getOriginalFilename(), followingFile.getSize());

        AnalysisResult result = new AnalysisResult();
        try {
            Set<String> followerUsernames = new HashSet<>();
            List<InstagramUser> followingUsers = new ArrayList<>();

            log.info("--- Parsing FOLLOWERS JSON ---");
            parseFile(followersFile.getBytes(), "followers_1", followerUsernames, null);

            log.info("--- Parsing FOLLOWING JSON ---");
            parseFile(followingFile.getBytes(), "relationships_following", null, followingUsers);

            log.info("[RESULT] followers parsed: {}", followerUsernames.size());
            log.info("[RESULT] following parsed: {}", followingUsers.size());

            // Process intersection to identify non-followers
            List<InstagramUser> nonFollowers = new ArrayList<>();
            for (InstagramUser user : followingUsers) {
                String unameLower = user.getUsername().toLowerCase();
                if (!followerUsernames.contains(unameLower)) {
                    nonFollowers.add(user);
                }
            }

            nonFollowers.sort(Comparator.comparing(u -> u.getUsername().toLowerCase()));

            result.setFollowersCount(followerUsernames.size());
            result.setFollowingCount(followingUsers.size());
            result.setNonFollowers(nonFollowers);
            result.setNonFollowersCount(nonFollowers.size());

            log.info("=== ANALYZE COMPLETE === nonFollowersCount={}", result.getNonFollowersCount());

        } catch (IOException e) {
            log.error("IOException during analysis", e);
            result.setError("Failed to parse JSON files: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during analysis", e);
            result.setError("Unexpected error: " + e.getMessage());
        }
        return result;
    }

    private void parseFile(byte[] bytes, String jsonKey,
                           Set<String> usernameSet, List<InstagramUser> userList) throws IOException {

        String content = new String(bytes, StandardCharsets.UTF_8).trim();

        // Safety checkpoint ensuring the file uploaded is structurally a JSON resource
        if (content.startsWith("[") || content.startsWith("{")) {
            JsonNode root = objectMapper.readTree(content);
            parseJson(root, jsonKey, usernameSet, userList);
        } else {
            log.error("Invalid file format uploaded. Must be a JSON export.");
            throw new IOException("Invalid format. Expected JSON content, instead found standard textual context.");
        }
    }

    private void parseJson(JsonNode root, String possibleKey,
                           Set<String> usernameSet, List<InstagramUser> userList) {

        JsonNode arrayNode = null;

        if (root.isArray()) {
            arrayNode = root;
        } else if (root.has(possibleKey) && root.get(possibleKey).isArray()) {
            arrayNode = root.get(possibleKey);
        } else {
            Iterator<Map.Entry<String, JsonNode>> fields = root.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                if (entry.getValue().isArray()) {
                    arrayNode = entry.getValue();
                    break;
                }
            }
        }

        if (arrayNode == null) {
            log.error("No valid array structure detected in JSON payload.");
            return;
        }

        int processed = 0;
        int skipped = 0;

        for (int i = 0; i < arrayNode.size(); i++) {
            JsonNode item = arrayNode.get(i);
            JsonNode sld = item.get("string_list_data");

            if (sld == null || !sld.isArray() || sld.isEmpty()) {
                skipped++;
                continue;
            }

            JsonNode entry = sld.get(0);

            // FIX: If "value" isn't present inside string_list_data, fall back to the outer object's "title"
            String username = "";
            if (entry.has("value")) {
                username = entry.get("value").asText("").trim();
            } else if (item.has("title")) {
                username = item.get("title").asText("").trim();
            }

            String href = entry.has("href") ? entry.get("href").asText("").trim() : "";

            if (username.isEmpty() || username.length() < 2 || SKIP.contains(username.toLowerCase())) {
                skipped++;
                continue;
            }

            String usernameLower = username.toLowerCase();

            // Clean up href if it contains the internal Meta tracking slug '_u/'
            String url;
            if (!href.isEmpty()) {
                url = href.replace("instagram.com/_u/", "instagram.com/");
            } else {
                url = "https://www.instagram.com/" + username;
            }

            if (usernameSet != null) usernameSet.add(usernameLower);
            if (userList != null) userList.add(new InstagramUser(username, username, url));
            processed++;
        }

        log.info("JSON Parse Success: {} accounts logged, {} structures skipped.", processed, skipped);
    }
}