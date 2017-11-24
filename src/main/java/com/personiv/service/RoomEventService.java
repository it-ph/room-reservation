package com.personiv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personiv.dao.RoomEventDao;
import com.personiv.model.RoomEvent;

@Service
public class RoomEventService {
	
	@Autowired
	private RoomEventDao roomEvtDao;
	
	public List<RoomEvent> getRoomEvents(){
		return roomEvtDao.getRoomEvents();
	}
	
	public RoomEvent getRoomEvent(Long id) {
		return roomEvtDao.getRoomEvent(id);
	}

	public List<RoomEvent> getRoomEventById(Long roomId) {
		return roomEvtDao.getRoomEventsByRoom(roomId);
	}
}
