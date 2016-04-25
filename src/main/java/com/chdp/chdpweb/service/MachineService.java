package com.chdp.chdpweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.chdp.chdpweb.dao.MachineDao;

@Repository
public class MachineService {

	@Autowired
	private MachineDao machineDao;
}
