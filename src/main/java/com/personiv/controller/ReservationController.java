package com.personiv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.personiv.model.Reservation;
import com.personiv.service.ReservationService;

@RestController
public class ReservationController {
	
	@Autowired
	private ReservationService reservationService;
	
	@RequestMapping(path= "/reservations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Reservation> getReservations(){
		return reservationService.getReservations();
	}
	@RequestMapping(path= "/reservations/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Reservation> getReservationById(@PathVariable("id") Long id){
		return reservationService.getReservationById(id);
	}
	
	
	@RequestMapping(path= "/reservations/getConflicts", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<Reservation> getConflicts(@RequestBody  Reservation res){
		
		return reservationService.getConflicts(res);
	}
}
