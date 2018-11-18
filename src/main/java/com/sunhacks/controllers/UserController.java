package com.sunhacks.controllers;

import com.sunhacks.models.User;
import com.sunhacks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository applicationUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @PostMapping("/sign-up")
    public String signUp(@RequestBody User user) {
        user.setPassWord(bCryptPasswordEncoder.encode(user.getPassWord()));
        applicationUserRepository.save(user);
        return "success";
    }
}