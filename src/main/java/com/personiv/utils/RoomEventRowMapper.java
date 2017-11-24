package com.personiv.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.personiv.model.Event;
import com.personiv.model.Room;
import com.personiv.model.RoomEvent;
import com.personiv.model.User;

public class RoomEventRowMapper implements RowMapper<RoomEvent>{

	@Override
	public RoomEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
		RoomEvent re = new RoomEvent();
		
		re.setId(rs.getLong("id"));
		
		User u = new User();
		u.setId(rs.getLong("userId"));		
		u.setUsername(rs.getString("username"));		
		u.setCreatedAt(rs.getTimestamp("userCreated"));
		u.setUpdatedAt(rs.getTimestamp("userUpdated"));
		
		re.setCreatedBy(u);
		
		Room room = new Room();
		
		room.setId(rs.getLong("roomId"));
		room.setRoom(rs.getString("room"));
		room.setCapacity(rs.getInt("capacity"));
		room.setColor_code(rs.getString("color"));
		room.setCreatedAt(rs.getTimestamp("roomCreated"));
		room.setUpdatedAt(rs.getTimestamp("roomUpdated"));
		
		re.setRoom(room);
		
		Event event = new Event();
		event.setId(rs.getLong("eventId"));
		event.setTitle(rs.getString("title"));
		event.setStart(rs.getTimestamp("start"));
		event.setEnd(rs.getTimestamp("end"));
		event.setCreatedAt(rs.getTimestamp("eventCreated"));
		event.setUdpatedAt(rs.getTimestamp("eventUpdated"));
		event.setRepeatId(rs.getLong("repeatId"));

		re.setEvent(event);
		return re;
		
	}

}
