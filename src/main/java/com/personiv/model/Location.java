package com.personiv.model;

import java.util.Date;

import lombok.Data;

@Data
public class Location {
	private Long id;
	private String location;
	private Date createdAt;
	private Date updatedAt;
}
