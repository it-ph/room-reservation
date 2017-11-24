	package com.personiv.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Reservation {
	private Long id;
	private String title;
	private String remarks;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date start;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date end;
	private boolean allDay;
	private Long repeatId;
}
