package com.personiv.utils;

import java.util.List;

import com.personiv.model.Event;

public class EventQueryBuilder {
	
	private Event event;

	public EventQueryBuilder(Event event) {
		this.event = event;
	}
	
	public String getConlictsQuery(List<Event> events) {
		String query ="";
		return query;
	}
}
