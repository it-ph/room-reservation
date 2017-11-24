package com.personiv.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.personiv.model.Event;
import com.personiv.model.Room;
import com.personiv.model.User;

public class EventRowMapper implements RowMapper<Event>{

	@Override
	public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
		

		Event event = new Event();
		
		User u = new User();
		u.setId(rs.getLong("userId"));		
		u.setUsername(rs.getString("username"));		
		u.setCreatedAt(rs.getTimestamp("userCreated"));
		u.setUpdatedAt(rs.getTimestamp("userUpdated"));
		
		Room room = new Room();
		
		room.setId(rs.getLong("roomId"));
		room.setRoom(rs.getString("room"));
		room.setCapacity(rs.getInt("capacity"));
		room.setColor_code(rs.getString("color"));
		room.setCreatedAt(rs.getTimestamp("roomCreated"));
		room.setUpdatedAt(rs.getTimestamp("roomUpdated"));
		
		event.setId(rs.getLong("id"));
		event.setRepeatId(rs.getLong("repeatId"));
		event.setTitle(rs.getString("title"));
		event.setStart(rs.getTimestamp("start"));
		event.setEnd(rs.getTimestamp("end"));
		event.setCreatedAt(rs.getTimestamp("eventCreated"));
		event.setUdpatedAt(rs.getTimestamp("eventUpdated"));
		event.setRepeatId(rs.getLong("repeatId"));
		
		event.setRoom(room);
		event.setCreatedBy(u);
		
		return event;
	}

}
