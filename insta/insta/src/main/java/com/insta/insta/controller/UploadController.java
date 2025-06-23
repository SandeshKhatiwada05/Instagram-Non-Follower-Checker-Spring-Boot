package com.insta.insta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.fasterxml.jackson.databind.*;

@Controller
public class UploadController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/upload")
    public String handleUpload(
            @RequestParam("followersFile") MultipartFile followersFile,
            @RequestParam("followingFile") MultipartFile followingFile,
            Model model) {

        try {
            ObjectMapper mapper = new ObjectMapper();

            List<String> followers = new ArrayList<>();
            List<Map<String, Object>> followersJson = mapper.readValue(new InputStreamReader(followersFile.getInputStream(), StandardCharsets.UTF_8), List.class);
            for (Map<String, Object> obj : followersJson) {
                List<Map<String, Object>> strList = (List<Map<String, Object>>) obj.get("string_list_data");
                for (Map<String, Object> entry : strList) {
                    followers.add((String) entry.get("value"));
                }
            }

            Set<String> following = new HashSet<>();
            Map<String, Object> followingJson = mapper.readValue(new InputStreamReader(followingFile.getInputStream(), StandardCharsets.UTF_8), Map.class);
            List<Map<String, Object>> rels = (List<Map<String, Object>>) followingJson.get("relationships_following");
            for (Map<String, Object> obj : rels) {
                List<Map<String, Object>> strList = (List<Map<String, Object>>) obj.get("string_list_data");
                for (Map<String, Object> entry : strList) {
                    following.add((String) entry.get("value"));
                }
            }

            List<String> notFollowingBack = new ArrayList<>();
            for (String user : following) {
                if (!followers.contains(user)) {
                    notFollowingBack.add(user);
                }
            }

            model.addAttribute("notFollowingBack", notFollowingBack);

        } catch (Exception e) {
            model.addAttribute("error", "Error processing files: " + e.getMessage());
        }

        return "index";
    }
}