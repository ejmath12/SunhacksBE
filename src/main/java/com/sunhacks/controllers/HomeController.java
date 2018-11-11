package com.sunhacks.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunhacks.models.Events;
import com.sunhacks.repository.EventRepository;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
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
//        e.setEvent_strt_time(1232312);
        repository.save(e);
        StringBuilder sb = new StringBuilder("");
        for(Events events:repository.findAll()) {
            sb.append(events.toString());
        }
        return sb.toString();
    }
    
  @RequestMapping(value = "/events")
  public List<Events> getEvents() throws JsonProcessingException, IOException, ParseException
  {
	  	RestTemplate restTemplate = new RestTemplate();
    	ObjectMapper mapper = new ObjectMapper();
    	
    	String fooResourceUrl = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=MUoKA8DyO4d1TsiK8TDreOQG1tIOHbHD";
    	ResponseEntity<String> response
    	  = restTemplate.getForEntity(fooResourceUrl, String.class);
    	JsonNode root = mapper.readTree(response.getBody());
    	JsonNode name = root.path("_embedded").path("events");
    	List<Events> ret = new ArrayList<>();
    	if (name.isArray()) {
    	    for (final JsonNode objNode : name) {
    	    	if(objNode.path("sales").path("public").get("startDateTime") == null) {
    	    		continue;
    	    	}
    	    	Events event = new Events();
    	    	event.setName(objNode.get("name").toString());
//    	    	event.setDescription();
//    	    	JsonNode venues = objNode.path("_embedded").path("venues").;
    	    	
    	    	event.setLatitude(objNode.path("_embedded").path("venues").get(0).path("location").get("latitude").toString());
    	    	event.setLongitude(objNode.path("_embedded").path("venues").get(0).path("location").get("longitude").toString());
//    	    	event.setDescription("Price - " + "min : " + objNode.path("priceRanges").get("min").toString() + " max : " + objNode.path("priceRanges").get("max").toString());
//    	    	System.out.println(objNode.get("priceRanges") == null);
    	    	if(objNode.get("priceRanges") == null) {
    	    		event.setDescription(" ");
    	    	}else {
//    	    		System.out.println("Price - " + "min : " + objNode.path("priceRanges").get(0).get("min").toString() + " max : " + objNode.path("priceRanges").get(0).get("max").toString());
    	    		event.setDescription("Price - " + "min : " + objNode.path("priceRanges").get(0).get("min").toString() + " max : " + objNode.path("priceRanges").get(0).get("max").toString());
    	    	}
    	    	
    	    	event.setPlace(objNode.path("_embedded").path("venues").get(0).get("name").toString());
//    	    	System.out.println(objNode.path("sales").path("public").toString());
    	    	
    	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Date dt = sdf.parse(objNode.path("sales").path("public").get("startDateTime").asText());
                long epoch = dt.getTime();
                event.setEvent_strt_time(epoch/ 1000);
    	         
//    	    	ret.add(objNode.get("name").toString());
    	    	ret.add(event);
//    	        System.out.println(objNode.get("name"));
    	    }
    	}
//        Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
    	
    	return generate_feasiable_event(ret,"36.7783", "119.4179");
  }
  
  public List<Events> generate_feasiable_event(List<Events> event_list, String origin_latitude, String origin_longitude) throws JsonProcessingException, IOException //,long user_strt_time
	{
		String requests="https://maps.googleapis.com/maps/api/distancematrix/json?origins="+origin_latitude+","+origin_longitude;
		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper mapper = new ObjectMapper();
		
		
		requests+="&destinations=";
		
		for(int i=0;i<event_list.size();i++)
		{
			requests+=event_list.get(i).getLatitude()+","+event_list.get(i).getLongitude()+"|";
		}
		requests=requests.substring(0,requests.length());
		requests+="&key=AIzaSyAq9QsLNB4AcqvPmLgVhR22CIAznd2Y3uM";
		
		System.out.println(requests);
		
		ResponseEntity<String> response= restTemplate.getForEntity(requests, String.class);
		
		JsonNode root = mapper.readTree(response.getBody());
	  	JsonNode destinations = root.path("rows").path("events").path("elements"); 	
	  	
	  	List<Events> events_fea_list=new ArrayList<Events>();
	  	
	  	int i=0;
	  	for (final JsonNode objNode : destinations)
	  	{
	  		long time_taken = Long.parseLong(objNode.get("duration").toString());
	  		long timestamp = System.currentTimeMillis() / 1000;
	  		
	  		if (time_taken+timestamp>event_list.get(i).getEvent_strt_time())// && event_list.get(i).getEvent_strt_time()>user_strt_time)
	  		{
	  			events_fea_list.add(event_list.get(i));
	  		}
	  		i++;
	  	}		
		return events_fea_list;
	}
}
