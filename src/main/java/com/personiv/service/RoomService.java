package com.personiv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personiv.dao.RoomDao;
import com.personiv.model.Room;

@Service
public class RoomService {

	@Autowired
	private RoomDao roomDao;

	public List<Room> getRooms(){
		return roomDao.getRooms();
	}
	
	public Room getRoom(Long id) {
		return roomDao.getRoom(id);
		
	}
}
