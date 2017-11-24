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

import com.personiv.model.Reservation;

@Repository
@Transactional(readOnly = false)
public class ReservationDao extends JdbcDaoSupport{

	private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }
    
    public List<Reservation> getReservations(){
    	String sql ="SELECT * FROM reservations";
    	return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Reservation>(Reservation.class));
    	
    }
    
    public List<Reservation> getReservationById(Long id){
    	String sql ="SELECT * FROM reservations WHERE id =?";
    	return jdbcTemplate.query(sql,new Object[] {id}, new BeanPropertyRowMapper<Reservation>(Reservation.class));
    	
    }
    public void addReservation(Reservation res) {
    	String sql = "INSERT INTO reservations(title,remarks,start,end) VALUES(?,?,?,?)";
    	jdbcTemplate.update(sql,new Object[] {res.getTitle(),res.getRemarks(),res.getStart(),res.getEnd()});
    }
    
    public List<Reservation> getConflicts(Reservation res){
    	String sql ="SELECT * from reservations r WHERE (? <= r.end AND ? >= r.start) AND r.id != ?";
    	
    	return jdbcTemplate.query(sql, new Object[] {res.getStart(),res.getEnd(),res.getId()},new BeanPropertyRowMapper<Reservation>(Reservation.class));
    }
}
