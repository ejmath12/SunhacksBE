package com.sunhacks.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.constraints.NotNull;


@Document(collection = "events")
public class Event {
	@Id
	private Key id;
	private String eventName;
	private String eventPlace;
	private String eventLatitude, eventLongitude;
	private String eventLink;
	private int eventRating = 0;
	private Category category = Category.NULL;

			;
	private long eventStartTime;
	private String eventStartTimeInString;
	private int eventDuration;

	private long travellingTime;
	private String travellingTimeInString;

	public Key getId() {
		return id;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}


	public String getEventPlace() {
		return eventPlace;
	}



	public void setEventPlace(String eventPlace) {
		this.eventPlace = eventPlace;
	}



	public String getEventLatitude() {
		return eventLatitude;
	}



	public void setEventLatitude(String eventLatitude) {
		this.eventLatitude = eventLatitude;
	}



	public String getEventLongitude() {
		return eventLongitude;
	}



	public void setEventLongitude(String eventLongitude) {
		this.eventLongitude = eventLongitude;
	}



	public String getEventLink() {
		return eventLink;
	}



	public void setEventLink(String eventLink) {
		this.eventLink = eventLink;
	}



	public int getEventRating() {
		return eventRating;
	}



	public void setEventRating(int eventRating) {
		this.eventRating = eventRating;
	}



	public long getEventStartTime() {
		return eventStartTime;
	}



	public void setEventStartTime(long eventStartTime) {
		this.eventStartTime = eventStartTime;
	}




	public long getTravellingTime() {
		return travellingTime;
	}



	public void setTravellingTime(long travellingTime) {
		this.travellingTime = travellingTime;
	}



	public int getEventDuration() {
		return eventDuration;
	}



	public void setEventDuration(int eventDuration) {
		this.eventDuration = eventDuration;
	}



	public String getEventStartTimeInString() {
		return eventStartTimeInString;
	}



	public void setEventStartTimeInString(String eventStartTimeInString) {
		this.eventStartTimeInString = eventStartTimeInString;
	}



	public String getTravellingTimeInString() {
		return travellingTimeInString;
	}



	public void setTravellingTimeInString(String travellingTimeInString) {
		this.travellingTimeInString = travellingTimeInString;
	}



}

enum Category
{
	NULL, ARTSNTHEATRE, SPORTS, MUSIC, FILM, ;
}
