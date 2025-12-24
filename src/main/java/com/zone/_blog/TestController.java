package com.zone._blog; 

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class TestController {
    @GetMapping("/test")
    public String getTest() {
        return "Hello World!";
    }
}
