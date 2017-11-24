package com.personiv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.personiv.model.Equipment;
import com.personiv.service.EquipmentService;

@RestController
public class EquipmentController {
	
	@Autowired
	private EquipmentService equipService;
	
	@RequestMapping(path = {"/equipments","/equipments/"},method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Equipment> getEquipments(){
		return equipService.getEquipments();
	}
	
	@RequestMapping(path = "/equipments/{id}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Equipment getEquipment(Long id){
		return equipService.getEquipment(id);
	}
}
