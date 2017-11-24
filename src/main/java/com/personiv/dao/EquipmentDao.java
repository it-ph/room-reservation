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

import com.personiv.model.Equipment;

@Repository
@Transactional(readOnly = false)
public class EquipmentDao extends JdbcDaoSupport{
	
	private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }
    
    public List<Equipment> getEquipments(){
    	String sql ="SELECT * FROM equipments";
    	return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Equipment>(Equipment.class));
    }
    
    public Equipment getEquipment(Long id) {

    	String sql ="SELECT * FROM equipments WHERE id = ?";
    	return jdbcTemplate.queryForObject(sql,new Object[] {id}, new BeanPropertyRowMapper<Equipment>(Equipment.class));
    }
}
