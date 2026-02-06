package com.zone._blog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestBackend {

    @GetMapping("${app.api.v1}/test-backend")
    public ResponseEntity<String> testBackend() {
        return ResponseEntity.ok("Hello i am working");
    }

}
