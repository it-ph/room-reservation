package com.personiv.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.personiv.model.Room;

@Repository
@Transactional(readOnly = false)
public class RoomDao extends JdbcDaoSupport{
	private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }
    
    public List<Room> getRooms(){
    	String sql ="SELECT * FROM rooms";
    	return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Room>(Room.class));
    }
    public Room getRoom(Long id) {
    	String sql ="SELECT * FROM rooms WHERE id =?";
    	return jdbcTemplate.queryForObject(sql,new Object[] {id}, new BeanPropertyRowMapper<Room>(Room.class));
    }
}
