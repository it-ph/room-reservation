package com.personiv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.personiv.model.Room;
import com.personiv.service.RoomService;

@RestController
public class RoomController {
	 //private final Log logger = LogFactory.getLog(this.getClass());
	 
	 @Autowired
	 private RoomService roomService;
	 
	 @RequestMapping(path = {"/rooms","/rooms/"},method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	 public List<Room> getRooms(){
		 return roomService.getRooms();
	 }
	 
	 @RequestMapping(path = "/rooms/{id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	 public Room getRoom(@PathVariable Long id){
		
		 
		 try {	  
			return roomService.getRoom(id);
		 }catch(Exception e) {return null;}
	 }
}
