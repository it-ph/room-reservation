package com.personiv.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.personiv.model.Equipment;
import com.personiv.model.RoomEvent;
import com.personiv.utils.RoomEventRowMapper;

@Repository
@Transactional(readOnly = false)
public class RoomEventDao extends JdbcDaoSupport{
	
	private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }
    
    public List<RoomEvent> getRoomEvents(){
    	
    	String sql ="call _proc_getRoomEvents";
    	List<RoomEvent> roomEvents = jdbcTemplate.query(sql, new RoomEventRowMapper());
    	
    	String sql2 = "SELECT e.* FROM room_events_equipments re JOIN equipments e ON re.equipmentId = e.id WHERE re.id = ?";
    	
    	for(RoomEvent re: roomEvents) {
    		
    		List<Equipment> roomEquipments = jdbcTemplate.query(sql2, new Object[] {re.getId()},new BeanPropertyRowMapper<Equipment>(Equipment.class));
    		re.setEquipments(roomEquipments);
    	}
    	
    	return roomEvents;
    }

	public RoomEvent getRoomEvent(Long id) {
		
		String sql ="call _proc_getRoomEventById(?)";
		RoomEvent re = null;		
		try {
			re = jdbcTemplate.queryForObject(sql,new Object[] {id}, new RoomEventRowMapper());
  
			String sql2 = "SELECT e.* FROM room_events_equipments re JOIN equipments e ON re.equipmentId = e.id WHERE re.id = ?";
    	
    		List<Equipment> roomEquipments = jdbcTemplate.query(sql2, new Object[] {re.getId()},new BeanPropertyRowMapper<Equipment>(Equipment.class));
    		re.setEquipments(roomEquipments);
		
		}catch(Exception e) {}
    	
    	return re;
	}

	public List<RoomEvent> getRoomEventsByRoom(Long roomId) {
		
	   	String sql ="call _proc_getRoomEventsByRoom(?)";
    	List<RoomEvent> roomEvents = jdbcTemplate.query(sql,new Object[] {roomId}, new RoomEventRowMapper());
    	
    	String sql2 = "SELECT e.* FROM room_events_equipments re JOIN equipments e ON re.equipmentId = e.id WHERE re.id = ?";
    	
    	for(RoomEvent re: roomEvents) {
    		
    		List<Equipment> roomEquipments = jdbcTemplate.query(sql2, new Object[] {re.getId()},new BeanPropertyRowMapper<Equipment>(Equipment.class));
    		re.setEquipments(roomEquipments);
    	}
    	
    	return roomEvents;
	}
}
