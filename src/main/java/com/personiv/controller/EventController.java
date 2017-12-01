package com.personiv.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.personiv.model.Event;
import com.personiv.service.EventService;
import com.personiv.service.RoomEventService;
import com.sun.mail.iap.Response;

@RestController
public class EventController {

	@Autowired
	private EventService evtService;
	

	@RequestMapping(path = {"/events","/events/"},method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Event> getEvents(){
		return evtService.getEvents();
	}
	
	@RequestMapping(path = "/events/{id}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Event getEvent(@PathVariable Long id){
		return evtService.getEvent(id);
	}
	
	
//	@RequestMapping(path = "/events",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> updateEvent(@RequestBody Event event){
//		
//		List<Event> conflicts = evtService.getConflicts(event);
//		
//		if(conflicts.isEmpty()) {
//			evtService.updateEvent(event);
//		}else {
//			return ResponseEntity.status(422).body(conflicts);
//		}
//		
//		return ResponseEntity.ok(event);
//	}
	
	@RequestMapping(path = "/events",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateEvents(@RequestBody List<Event> events){
		
		List<Event> conflicts = evtService.getConflicts(events);
		
		if(conflicts.isEmpty()) {
			evtService.updateEvents(events);
//			for(Event e: events) {
//				System.out.println(e.getStart()+" , "+e.getEnd());
//			}
		}else {
			return ResponseEntity.status(422).body(conflicts);
		}
		
	
		
		return ResponseEntity.ok(events);
	}
	
	@RequestMapping(path = "/events",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addEvent(@RequestBody Event event){
		
		List<Event> conflicts = evtService.getConflicts(event);
		
		if(conflicts.isEmpty()) {
			
			evtService.updateEvent(event);
			
			
		}else {
			return ResponseEntity.status(422).body(conflicts);
		}
		
		return ResponseEntity.ok(event);
	}
	
	@RequestMapping(path = "/events/newEvents",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addEvents(@RequestBody List<Event> events){
		
		
		List<Event> conflicts = evtService.getNewEventsConflicts(events);
		
		if(conflicts.isEmpty()) {
			evtService.addEvents(events);
			return ResponseEntity.ok(events);
			
		}else {
			return ResponseEntity.status(422).body(conflicts);
		}
		
		
	}
	
	
	@RequestMapping(path = "/events/getConflicts",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Event> getConflicst(@RequestBody List<Event> events){
			
		return evtService.getConflicts(events);
	}
}
