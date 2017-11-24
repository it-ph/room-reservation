package com.personiv.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.personiv.model.Role;
import com.personiv.model.User;


@Repository
@Transactional(readOnly = false)
public class UserDao extends JdbcDaoSupport{
	
	private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }
   
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    
	public User getUser(String username, String password) {
		String sql = "Call _proc_getUserAccount(?,?)";
		User user = jdbcTemplate.queryForObject(sql,new Object[] {username,password}, new BeanPropertyRowMapper<User>(User.class));
		
		String sql2 = "Call _proc_getUserRoles(?)";
		List<Role> roles = jdbcTemplate.query(sql2,new Object[] {user.getId()}, new BeanPropertyRowMapper<Role>(Role.class));
		
		Set<Role> r = new HashSet<>();
		
		for(Role rl: roles) {
			r.add(rl);
		}
		
		user.setRoles(r);
		
		return user;
	}


	public User findUserById(Integer id) {
		String sql = "Call findUserById(?);";
		System.out.println(sql);
		User user = jdbcTemplate.queryForObject(sql,new Object[] {id}, new BeanPropertyRowMapper<User>(User.class));
		
		if(user != null) {
			List<Role> roles = getRoles(user.getId());
			if(!roles.isEmpty()) {
				Set<Role> roles2 = new HashSet<>();
				for(Role r: roles) {
					roles2.add(r);
				}
				user.setRoles(roles2);
			}
		}
		return user;
	}
	public List<User>getUsers(){
		String query ="Call _proc_getUsers()";
		List<User> users =jdbcTemplate.query(query,new BeanPropertyRowMapper<User>(User.class));
		
		for(User u: users) {
			String sql2 = "Call _proc_getUserRoles(?)";
			List<Role> roles = jdbcTemplate.query(sql2,new Object[] {u.getId()}, new BeanPropertyRowMapper<Role>(Role.class));
			
			Set<Role> r = new HashSet<>();
			
			for(Role rl: roles) {
				r.add(rl);
			}
			
			u.setRoles(r);
		}
		return users;
	}
	
	


	public User getUserByUsername(String username) {
		String sql = "Call _proc_getUserByUsername(?)";
		User user = jdbcTemplate.queryForObject(sql,new Object[] {username}, new BeanPropertyRowMapper<User>(User.class));	
		
		if(user != null) {
			
			String sql2 = "Call _proc_getUserRoles(?)";
			List<Role> roles = jdbcTemplate.query(sql2,new Object[] {user.getId()}, new BeanPropertyRowMapper<Role>(Role.class));
			
			Set<Role> r = new HashSet<>();
			
			for(Role rl: roles) {
				r.add(rl);
			}
			
			user.setRoles(r);
		}
		
		return user;
	}


	public List<Role> getRoles(Long id) {
		String query ="Call _proc_getUserRoles(?)";
		List<Role> roles =jdbcTemplate.query(query,new Object[] {id},new BeanPropertyRowMapper<Role>(Role.class));
		return roles;
	}


	public List<User> getNonAdminUsers() {
		String query ="Call _proc_getNonAdminUsers()";
		
		List<User> users = jdbcTemplate.query(query,new BeanPropertyRowMapper<User>(User.class));
		
		for(User u: users) {
			String sql2 = "Call _proc_getUserRoles(?)";
			List<Role> roles = jdbcTemplate.query(sql2,new Object[] {u.getId()}, new BeanPropertyRowMapper<Role>(Role.class));
			
			Set<Role> r = new HashSet<>();
			
			for(Role rl: roles) {
				r.add(rl);
			}
			
			u.setRoles(r);
		}
		
		return users;
	}


	public void addNonAdminUser(User user) {
		String query = "INSERT INTO users(firstName,lastName,email,password,accountNonLocked,accountNonExpired,enabled) VALUES(?,?,?,?,?,?,?)";
		
		String query2 ="INSERT INTO userroles(userId,role) VALUES(?,?)";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
	 	jdbcTemplate.update(
		    	new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
							
						ps.setString(1,user.getFirstName());
						ps.setString(2,user.getLastName());
						ps.setString(3,user.getUsername());
						ps.setString(4,passwordEncoder().encode(user.getPassword()));
						ps.setBoolean(5,true);
						ps.setBoolean(6,true);
						ps.setBoolean(7,true);
					 return ps;
					}
		    		
		    	},
		    	keyHolder);
	 	
	 	Long id = keyHolder.getKey().longValue();
	 	
	 	jdbcTemplate.update(query2,new Object[] {id,"USER"});
	}

	public void resetPassword(User user) {
		String query = "call _proc_resetPassword(?,?)";
		
		jdbcTemplate.update(query,new Object[] {user.getId(),passwordEncoder().encode("!welcome10")});
		
	}


	
}
