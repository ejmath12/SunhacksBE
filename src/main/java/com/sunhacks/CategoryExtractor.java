package com.sunhacks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunhacks.models.Constants;
import com.sunhacks.models.EventClassifier;
import com.sunhacks.repository.EventClassifierRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class CategoryExtractor implements CommandLineRunner {
    @Autowired
    private Environment env;


    @Autowired
    private EventClassifierRepo repository;

    public static void main(String args[]) {
        SpringApplication.run(CategoryExtractor.class, args);
    }
    @Override
    public void run(String... args) throws IOException {
        String api = env.getProperty("application.urls.categories") + env.getProperty("application.keys.discovery");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(api, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        JsonNode genres = root.path(Constants.segment).path(Constants.embedded).path(Constants.genres);
        List<EventClassifier> eventClassifiers = new ArrayList<>();
        int i =0;
        if(genres.isArray()) {
            for (final JsonNode objNode : genres) {
                EventClassifier e = new EventClassifier();
                e.setGenreName(objNode.get(Constants.name).asText());
                e.setGenreKey(objNode.get(Constants.id).asText());
                e.setIntegerMapper(i++);
                eventClassifiers.add(e);
            }
        }
        repository.save(eventClassifiers);
    }
}
