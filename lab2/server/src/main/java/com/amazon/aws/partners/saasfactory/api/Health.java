package com.amazon.aws.partners.saasfactory.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Health {

    @GetMapping("/health.html")
    public ResponseEntity health() {
        return ResponseEntity.ok().build();
    }
}
