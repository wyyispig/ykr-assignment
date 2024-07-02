package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ask")
public class ApiController {

    @Autowired
    private ApiService apiService;

    @GetMapping
    public String askQuestion(@RequestParam String question) {
        System.out.println("Received question: " + question);
        try {
            return apiService.askQuestion(question);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }
}




