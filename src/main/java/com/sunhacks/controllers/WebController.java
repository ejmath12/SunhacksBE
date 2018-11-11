package com.sunhacks.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
    @RequestMapping("/homepage")
    public String home() {
        return "home.html";
    }
}
