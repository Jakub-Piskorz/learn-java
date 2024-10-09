package com.example.controller;

import com.example.config.GlobalVariables;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("dev")
public class HomeController {
    private final GlobalVariables props;

    public HomeController(GlobalVariables props) {
        this.props = props;
    }

    @GetMapping("/")
    public GlobalVariables home() {
        return props;
    }

    @GetMapping("/secured")
    public String secured() {
        return "Hello secured!";
    }
}
