package com.personiv.model;

import java.util.Date;

import lombok.Data;

@Data
public class Equipment {
	private Long id;
	private String equipment;
	private Date createdAt;
	private Date updatedAt;
}
