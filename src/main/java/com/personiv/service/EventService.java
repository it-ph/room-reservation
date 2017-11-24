package com.personiv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personiv.dao.EventDao;
import com.personiv.model.Event;

@Service
public class EventService {
	
	@Autowired
	private EventDao evtDao;
	
	public List<Event> getEvents(){
		return evtDao.getEvents();
	}
	
	public Event getEvent(Long id) {
		return evtDao.getEvent(id);
	}

	public void updateEvent(Event event) {
		evtDao.updateEvent(event);
		
	}
	
	public List<Event> getConflicts(Event event){
		return evtDao.getConflicts(event);
	}
	
	public List<Event> getNewEventsConflicts(List<Event> events){
		return evtDao.getNewEventsConflicts(events);
	}
	public void addEvent(Event event) {
		evtDao.addEvent(event);
	}
	
	public void addEvents(List<Event> events) {
		evtDao.addEvents(events);
	}

	public List<Event> getConflicts(List<Event> events) {
		return evtDao.getConflicts(events);
	}

	public void updateEvents(List<Event> events) {
		evtDao.updateEvents(events);
	}
}
