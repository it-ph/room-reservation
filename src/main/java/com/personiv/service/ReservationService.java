package com.personiv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personiv.dao.ReservationDao;
import com.personiv.model.Reservation;

@Service
public class ReservationService {
	
	@Autowired
	private ReservationDao reservationDao;
	
	public List<Reservation> getReservations(){
		return reservationDao.getReservations();
	}

	public List<Reservation> getConflicts(Reservation res) {
		return reservationDao.getConflicts(res);
	}

	public List<Reservation> getReservationById(Long id) {
		// TODO Auto-generated method stub
		return reservationDao.getReservationById(id);
	}
}
