package com.personiv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personiv.dao.EquipmentDao;
import com.personiv.model.Equipment;

@Service
public class EquipmentService {

	@Autowired
	private EquipmentDao equipDao;
	
	public List<Equipment> getEquipments(){
		return equipDao.getEquipments();
	}
	
	public Equipment getEquipment(Long id) {
		return equipDao.getEquipment(id);
	}
}
