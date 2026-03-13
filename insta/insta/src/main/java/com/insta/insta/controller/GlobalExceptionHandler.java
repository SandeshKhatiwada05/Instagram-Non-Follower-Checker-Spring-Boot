package com.insta.insta.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({MaxUploadSizeExceededException.class, MultipartException.class})
    public String handleMultipart(Exception ex, Model model) {
        log.error("Multipart exception: {}", ex.getMessage());
        model.addAttribute("error", "Upload error: " + (ex.getMessage() != null ? ex.getMessage() : "Invalid upload"));
        model.addAttribute("notFollowingBack", Collections.emptyList());
        return "index";
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParam(MissingServletRequestParameterException ex, Model model) {
        log.error("Missing request parameter: {}", ex.getParameterName());
        model.addAttribute("error", "Missing upload field: " + ex.getParameterName());
        model.addAttribute("notFollowingBack", Collections.emptyList());
        return "index";
    }

    @ExceptionHandler(Exception.class)
    public String handleAny(Exception ex, Model model) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        model.addAttribute("error", "Unexpected error: " + (ex.getMessage() != null ? ex.getMessage() : "see server logs"));
        model.addAttribute("notFollowingBack", Collections.emptyList());
        return "index";
    }
}

