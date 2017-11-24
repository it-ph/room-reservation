package com.personiv.model;

import java.util.Date;

import lombok.Data;

@Data
public class Event {
	private Long id;
	private Long repeatId;
	private String title;
	private Date start;
	private Date end;
	private Date createdAt;
	private Date udpatedAt;
	private User createdBy;
	private Room room;
}

