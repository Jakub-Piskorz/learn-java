package com.example.controller;

import com.example.config.GlobalVariables;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Profile("dev")
public class HomeController {
    private final GlobalVariables env;

    public HomeController(GlobalVariables env) {
        this.env = env;
    }

    @GetMapping("/")
    public String home(Principal principal) {
        return "Hello " + principal.getName();
    }

    @GetMapping("/test")
    public String secured() {
        return "Hello" + env.dbUsername();
    }
}
