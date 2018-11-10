package com.sunhacks.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunhacks.models.Events;
import com.sunhacks.repository.EventRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
    
  @RequestMapping(value = "/events")
  public String getEvents() throws JsonProcessingException, IOException
  {
	  	RestTemplate restTemplate = new RestTemplate();
    	ObjectMapper mapper = new ObjectMapper();
    	
    	String fooResourceUrl = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=MUoKA8DyO4d1TsiK8TDreOQG1tIOHbHD";
    	ResponseEntity<String> response
    	  = restTemplate.getForEntity(fooResourceUrl, String.class);
    	JsonNode root = mapper.readTree(response.getBody());
    	JsonNode name = root.path("_embedded").path("events");
    	List<String> ret = new ArrayList<>();
    	if (name.isArray()) {
    	    for (final JsonNode objNode : name) {
//    	    	Events event = new Events();
//    	    	event.setName(objNode.get("name").toString());
//    	    	event.setDescription();
    	    	ret.add(objNode.get("name").toString());
//    	        System.out.println(objNode.get("name"));
    	    }
    	}
    	
    	
    	
//        Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
        return ret.toString();
  }
}
