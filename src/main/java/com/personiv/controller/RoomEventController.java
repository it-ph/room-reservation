package com.personiv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.personiv.model.RoomEvent;
import com.personiv.service.RoomEventService;

@RestController
public class RoomEventController {

	@Autowired
	private RoomEventService roomEvtService;
	
	@RequestMapping(path = {"/room-events","/room-events/"},method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<RoomEvent> getRoomEvents(){
		return roomEvtService.getRoomEvents();
	}
	
	@RequestMapping(path = "/room-events/{id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public RoomEvent getRoomEvent(@PathVariable Long id){
		return roomEvtService.getRoomEvent(id);
	}
	
	@RequestMapping(path = "/room-events/room/{roomId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public List<RoomEvent> getRoomEventByRoom(@PathVariable Long roomId){
		return roomEvtService.getRoomEventById(roomId);
	}
}
