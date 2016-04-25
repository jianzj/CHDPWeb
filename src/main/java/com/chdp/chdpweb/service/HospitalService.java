package com.chdp.chdpweb.service;

import com.chdp.chdpweb.bean.Hospital;
import com.chdp.chdpweb.dao.HospitalDao;

public class HospitalService {
	
	private HospitalDao hospitalDao;
	
	public Hospital getHospital(String name){
		return hospitalDao.getHospital(name);
	}
	
}
