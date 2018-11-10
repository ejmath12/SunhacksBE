package com.sunhacks.controllers;

import com.sunhacks.models.Events;
import com.sunhacks.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @Autowired
    private EventRepository repository;

    @RequestMapping("/")
    public String index() {
        Events e = new Events();
        e.setName("Sunhacks");
        e.setDescription("Hacktathon");
        e.setEvent_strt_time(1232312);
        repository.save(e);
        StringBuilder sb = new StringBuilder("");
        for(Events events:repository.findAll()) {
            sb.append(events.toString());
        }
        return sb.toString();
    }
}
