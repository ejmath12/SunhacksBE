package com.sunhacks.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping("/welcome")
    public String home() {
        return "home.html";
    }

    @RequestMapping("/sign-up")
    public String signUp() {
        return "signUp.html";
    }

    @RequestMapping("/")
    public String index() {
        return "index.html";
    }
}
