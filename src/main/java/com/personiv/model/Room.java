package com.personiv.model;

import java.util.Date;

import lombok.Data;

@Data
public class Room {
	private Long id;
	private String room;
	private int capacity;
	private String color_code;
	private Date createdAt;
	private Date updatedAt;
}
