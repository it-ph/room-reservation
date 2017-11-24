package com.personiv.model;

import java.util.Date;
import java.util.Set;

import lombok.Data;

@Data
public class User {
	private Long id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private Date createdAt;
	private Date updatedAt;
	private Set<Role> roles;
	private boolean accountNonLocked;
	private boolean accountNonExpired;
	private boolean credentialsNonExpired;
	private boolean enabled;
}
