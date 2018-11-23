package com.sunhacks.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.sunhacks.models.Constants;
import com.sunhacks.models.Event;
import com.sunhacks.models.EventShort;
import com.sunhacks.models.Key;
import com.sunhacks.repository.EventRepository;

import ch.qos.logback.core.net.SyslogOutputStream;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/events")
public class HomeController {

	@Autowired
	Environment env;

	@Autowired
	private EventRepository repository;

	@RequestMapping(value = "/saveEvent", method = RequestMethod.POST,
			consumes = "application/json", produces = "application/json")
	public String index(@RequestBody String request) throws IOException {
		System.out.println(request);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(request);
		Event e = new Event();
		String userId = "abc123";
		String eventName = root.get(Constants.name).textValue();
		e.setId(new Key(userId, eventName));
		e.setEventName(eventName);
		repository.save(e);
		return "{}";
	}

	@RequestMapping(value = "/historyEvents", method = RequestMethod.POST,
			consumes = "application/json", produces = "application/json")
	public String getHistoryEvents() {
		ObjectMapper mapper = new ObjectMapper();
		List<Event> list = repository.findAll();
		String jsonInString = "";
		try {
			jsonInString = mapper.writeValueAsString(list);
		} catch (JsonProcessingException j) {
			jsonInString = "";
		}
		return jsonInString;
	}

	@RequestMapping(value = "/saveRatings", method = RequestMethod.POST,
			consumes = "application/json", produces = "application/json")
	public boolean saveRatings(@RequestBody String request) throws IOException {
		//System.out.println("Request" + request);
		ObjectMapper mapper = new ObjectMapper();
		List<EventShort> list2 = mapper.readValue(request,
				TypeFactory.defaultInstance().constructCollectionType(List.class, EventShort.class));
		for(EventShort e: list2) {
			if(!(e.getEventRating() == null ||  e.getEventRating().equals(""))){
				Event event = new Event();
				String userId = "abc123";
				event.setId(new Key(userId, e.getEventName()));
				event.setEventName(e.getEventName());
				event.setEventRating(Integer.parseInt(e.getEventRating()));
				repository.save(event);
			}

		}
		return true;

	}

	@RequestMapping(value = "/getEvents", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public String getEvents(@RequestBody String request) throws JsonProcessingException, IOException, ParseException {

		//System.out.println("POST Request" + request);

//		boolean rightNow = true; // default should be false
		String requestLocation = "Texas"; // default value is "default"
		String requestLatitude = null;
		String requestLongitude = null;
		String searchRadius = Constants.defaultSearchRadius;
//		long requestDateTime = -1;
		int noOfDays = 10; // default value is -1"43.874668","-81.484383"
		int noOfHours = 0;

		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper mapper = new ObjectMapper();

		JsonNode root = mapper.readTree(request);

		requestLocation = root.get(0).get(Constants.value).asText();
		noOfDays = root.get(1).get(Constants.value).asInt();
		noOfHours = root.get(2).get(Constants.value).asInt();


		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.simpleDateFormat);
		String startDate = null;
		String endDate = null;

		long systemTime = System.currentTimeMillis();

		if(requestLocation != "") {
			System.out.println(requestLocation);
			if(noOfDays != 0) {
				Timestamp timestamp = new Timestamp(systemTime);
				startDate = simpleDateFormat.format(timestamp);
				timestamp = new Timestamp(systemTime + noOfDays * 24 * 3600 * 1000 - noOfHours * 3600 * 1000);
				endDate = simpleDateFormat.format(timestamp);
			}else {
				Timestamp timestamp = new Timestamp(systemTime);
				startDate = simpleDateFormat.format(timestamp);
				timestamp = new Timestamp(systemTime + noOfHours * 3600 * 1000);
				endDate = simpleDateFormat.format(timestamp);
			}

			String geoCodeAPIRequest=env.getProperty("application.urls.geocode")+requestLocation+"&key="
					+ env.getProperty("application.keys.geocode");
			ResponseEntity<String> response = restTemplate.getForEntity(geoCodeAPIRequest, String.class);
			JsonNode geocode_root = mapper.readTree(response.getBody());
			requestLatitude = geocode_root.path(Constants.results).get(0).path(Constants.geometry).path(Constants.location).get("lat").asText();
			requestLongitude = geocode_root.path(Constants.results).get(0).path(Constants.geometry).path(Constants.location).get("lng").asText();
			System.out.println(requestLatitude + " " + requestLongitude);
		}
		else {
			if(noOfDays != 0) {
				Timestamp timestamp = new Timestamp(systemTime);
				startDate = simpleDateFormat.format(timestamp);
				timestamp = new Timestamp(systemTime + noOfDays * 24 * 3600 * 1000 - noOfHours * 3600 * 1000);
				endDate = simpleDateFormat.format(timestamp);
			}else {
				Timestamp timestamp = new Timestamp(systemTime);
				startDate = simpleDateFormat.format(timestamp);
				timestamp = new Timestamp(systemTime + noOfHours * 3600 * 1000);
				endDate = simpleDateFormat.format(timestamp);
			}

			requestLatitude = root.get(3).get(Constants.value).asText();
			requestLongitude = root.get(4).get(Constants.value).asText();
		}
		String discoveryApi = env.getProperty("application.urls.discovery") + requestLatitude + "," + requestLongitude + "&radius=" + searchRadius
				+ "&startDateTime=" + startDate + "&endDateTime=" + endDate +
				"&apikey=" + env.getProperty("application.keys.discovery");
		System.out.println(discoveryApi);
		ResponseEntity<String> response = restTemplate.getForEntity(discoveryApi, String.class);
		root = mapper.readTree(response.getBody());
		JsonNode name = root.path(Constants.embedded).path(Constants.events);

		List<Event> eventList = new ArrayList<>();

		if (name.isArray()) {
			for (final JsonNode objNode : name) {

				if (objNode.path(Constants.dates).path(Constants.start).get(Constants.dateTime) == null) {
					System.out.println("dateTime null");
					continue;
				}

				Event event = new Event();
				String userId = "abc123";
				String eventName = objNode.get(Constants.name).asText();
				event.setId(new Key(userId, eventName));
				event.setEventName(eventName);
				event.setEventPlace(objNode.path(Constants.embedded).path("venues").get(0).get(Constants.name).asText());
				event.setEventLatitude(
						objNode.path(Constants.embedded).path("venues").get(0).path(Constants.location).get("latitude").asText());
				event.setEventLongitude(
						objNode.path(Constants.embedded).path("venues").get(0).path(Constants.location).get("longitude").asText());
				event.setEventLink(objNode.get("url").asText());

				SimpleDateFormat sdf = new SimpleDateFormat(Constants.simpleDateFormat);
				Date dt = sdf.parse(objNode.path(Constants.dates).path(Constants.start).get(Constants.dateTime).asText());
				long epoch = dt.getTime();
				event.setEventStartTime(epoch / 1000);
				event.setEventDuration((int) ((Math.random() * 240) + 180) * 60);

				eventList.add(event);
			}
		}


		List<Event> feasibleEvents;
		feasibleEvents = generateFeasibleEvents(eventList, requestLocation==""?requestLatitude+","+requestLongitude:requestLocation, noOfHours, noOfDays);
		String jsonInString = "";
		try {
			jsonInString = mapper.writeValueAsString(feasibleEvents);
		} catch (JsonProcessingException j) {
			jsonInString = "";
		}
		return jsonInString;
	}

	private List<Event> generateFeasibleEvents(List<Event> eventList, String requestLocation, int noOfHours,
											   int noOfDays) throws JsonProcessingException, IOException {

		String requests = env.getProperty("application.urls.distance") + requestLocation;

		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper mapper = new ObjectMapper();

		requests += "&destinations=";

		for (int i = 0; i < eventList.size(); i++) {
			requests += Float.parseFloat(eventList.get(i).getEventLatitude()) + ","
					+ Float.parseFloat(eventList.get(i).getEventLongitude()) + "|";
		}
		requests = requests.substring(0, requests.length() - 1);
		requests += "&key=" + env.getProperty("application.keys.geocode");

		System.out.println(requests);

		ResponseEntity<String> response = restTemplate.getForEntity(requests, String.class);

		JsonNode root = mapper.readTree(response.getBody());
		if(root.path("rows").size() == 0) {
			System.out.println(Collections.emptyList());
			return Collections.emptyList();
		}
		JsonNode destinations = root.path("rows").get(0).path("elements");

		List<Event> feasibleEvents = new ArrayList<Event>();

		int i = 0;
		for (final JsonNode objNode : destinations) {

			long travelTime = Long.parseLong(objNode.path("duration").get(Constants.value).asText());
			long timestamp = System.currentTimeMillis() / 1000;

			System.out.println((travelTime) + " " + eventList.get(i).getEventStartTime());

			if ((timestamp + travelTime < eventList.get(i).getEventStartTime())
					&& (eventList.get(i).getEventDuration() + 2 * eventList.get(i).getTravellingTime() < (noOfHours * 3600))
					&& (eventList.get(i).getEventStartTime() + noOfHours * 3600 < timestamp + noOfDays * 24 * 3600)) {
				eventList.get(i).setTravellingTime(travelTime);

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.simpleDateFormat2);

				Timestamp timestamp2 = new Timestamp(eventList.get(i).getEventStartTime() * 1000);
				eventList.get(i).setEventStartTimeInString(simpleDateFormat.format(timestamp2));
				eventList.get(i).setTravellingTimeInString(objNode.path("duration").get("text").asText());
				feasibleEvents.add(eventList.get(i));
			}

			i++;
		}

//		return feasibleEvents.subList(0, Math.min(feasibleEvents.size(), 5));
		return feasibleEvents;
	}

}
