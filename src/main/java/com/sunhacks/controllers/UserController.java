package com.sunhacks.controllers;

import com.sunhacks.models.User;
import com.sunhacks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository applicationUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder1;


    @PostMapping("/sign-up")
    public boolean signUp(@RequestBody User user) {
        user.setPassWord(bCryptPasswordEncoder1.encode(user.getPassword()));
        applicationUserRepository.save(user);
        return true;
    }


}