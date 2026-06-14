package com.mainframe.instagram.controller;

import com.mainframe.instagram.model.AnalysisResult;
import com.mainframe.instagram.service.InstagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class InstagramController {

    @Autowired
    private InstagramService instagramService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/analyze")
    @ResponseBody
    public ResponseEntity<AnalysisResult> analyze(
            @RequestParam("followers") MultipartFile followersFile,
            @RequestParam("following") MultipartFile followingFile) {

        if (followersFile.isEmpty() || followingFile.isEmpty()) {
            AnalysisResult err = new AnalysisResult();
            err.setError("Both files are required.");
            return ResponseEntity.badRequest().body(err);
        }

        AnalysisResult result = instagramService.analyze(followersFile, followingFile);
        if (result.getError() != null) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }
}
