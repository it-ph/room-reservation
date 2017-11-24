package com.personiv.model;

import java.util.List;

import lombok.Data;

@Data
public class RoomEvent {
	private Long id;
	private User createdBy;
	private Room room;
	private Event event;
	private List<Equipment> equipments;
}
