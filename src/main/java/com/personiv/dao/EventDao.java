package com.personiv.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.personiv.model.Event;
import com.personiv.utils.EventRowMapper;


@Repository
@Transactional(readOnly = false)
public class EventDao extends JdbcDaoSupport{
	
	private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }
    
    public List<Event> getEvents(){
    	String sql = "call _proc_getEvents()";
    	return jdbcTemplate.query(sql, new EventRowMapper());
    }
    
    public Event getEvent(Long id) {
    	String sql = "call _proc_getEventById(?)";
    	return jdbcTemplate.queryForObject(sql,new Object[] {id},new EventRowMapper());
    }

	public void updateEvent(Event event) {
		String sql = "UPDATE events SET start = ?, end= ?,roomId =? ,updatedAt = CURRENT_TIMESTAMP WHERE  id = ?";
		jdbcTemplate.update(sql,new Object[] {event.getStart(),event.getEnd(),event.getRoom().getId(),event.getId()});
	}

	public List<Event> getConflicts(Event event) {
		
		List<Event> conflicts = new ArrayList<>();
		
		if(event.getId() != null) {
			String sql ="call _proc_getConflicts(?,?,?,?)";
			conflicts = jdbcTemplate.query(sql,new Object[] {event.getStart(),event.getEnd(),event.getId(),event.getRoom().getId()}, new EventRowMapper());	 
		}else {
			String sql ="call _proc_getTimeConflicts(?,?,?)";
			conflicts = jdbcTemplate.query(sql,new Object[] {event.getStart(),event.getEnd(),event.getRoom().getId()}, new EventRowMapper());	
		}
	 	return conflicts;   
	}
	

	
	public void addEvent(Event event) {
		String sql = "INSERT INTO events(start,end,title,createdBy,roomId) VALUES(?,?,?,?,?)";
		jdbcTemplate.update(sql,new Object[] {event.getStart(), event.getEnd(), event.getTitle(),event.getCreatedBy().getId(), event.getRoom().getId()});
		
	}

	public List<Event> getNewEventsConflicts(List<Event> events) {
		return null;
	}

	public List<Event> getConflicts(List<Event> events) {
		
		String query ="SELECT r.id roomId, r.room, r.capacity, r.color_code color,r.createdAt roomCreated, r.updatedAt roomUpdated, " + 
					  "		  e.id, e.title, e.start, e.end, e.createdAt eventCreated, e.updatedAt eventUpdated, e.repeatId, " + 
					  "	      u.id userId, u.username, u.createdAt userCreated, u.updatedAt userUpdated " + 
					  "  FROM events e " + 
					  "  JOIN rooms r ON e.roomId = r.id " + 
					  "  JOIN users u ON e.createdBy = u.id "+
					  " WHERE ";
		
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		for(int i=0; i <events.size(); i++) {
			
			Event e = events.get(i);
			
			String filter ="(('"+formatter.format(e.getStart())+"' <= e.end AND '"+ formatter.format(e.getEnd())+"'  >= e.start) "
			               +((e.getRepeatId() != null)?" AND (e.repeatId != "+ e.getRepeatId() +" OR e.repeatId IS NULL) ":" ")
			               +" AND e.id !="+ e.getId()+" "
			               +" AND e.roomId = "+e.getRoom().getId()+")";
		
			
			query += filter+"";
			query += (i == (events.size()-1))? "" :" OR ";
		}
		
		System.out.println(query);
		
		return jdbcTemplate.query(query, new EventRowMapper());
	}

	public void addEvents(List<Event> events) {
		
		String sql ="INSERT INTO events(title,roomId,start,end,createdBy) VALUES(?,?,?,?,?)";
		
		
		
		jdbcTemplate.batchUpdate(sql,new BatchPreparedStatementSetter() {

			@Override
			public int getBatchSize() {
				return events.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				
				Event e = events.get(i);
				
				ps.setString(1, e.getTitle());
				ps.setLong(2, e.getRoom().getId());
				ps.setTimestamp(3, new Timestamp(e.getStart().getTime()));
				ps.setTimestamp(4, new Timestamp(e.getEnd().getTime()));
				ps.setLong(5, e.getCreatedBy().getId());
			}
			
		});
	}

	public void updateEvents(List<Event> events) {
		
		String sql ="UPDATE events SET title =?, roomId =?, start =?, end = ?, createdBy = ? WHERE id = ?";
		
		for(Event e: events) {
			System.out.println(e.getStart()+" ,"+ e.getEnd());
		}
		
		jdbcTemplate.batchUpdate(sql,new BatchPreparedStatementSetter() {

			@Override
			public int getBatchSize() {
				return events.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				
				Event e = events.get(i);
				
				ps.setString(1, e.getTitle());
				ps.setLong(2, e.getRoom().getId());
				ps.setTimestamp(3, new Timestamp(e.getStart().getTime()));
				ps.setTimestamp(4, new Timestamp(e.getEnd().getTime()));
				ps.setLong(5, e.getCreatedBy().getId());
				ps.setLong(6, e.getId());
				
				System.out.println(e);
			}
			
		});
		
	}
}
