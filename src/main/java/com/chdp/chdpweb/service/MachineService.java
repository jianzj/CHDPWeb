package com.chdp.chdpweb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.Machine;
import com.chdp.chdpweb.dao.MachineDao;
import com.github.pagehelper.PageHelper;

@Repository
public class MachineService {

	@Autowired
	private MachineDao machineDao;
	
	public List<Machine> getMachineList(int pageNum){
		PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
		try{
			return machineDao.getMachines();
		} catch (Exception e){
			return new ArrayList<Machine>();
		}
	}
	
	public boolean addMachine(Machine machine){
		try{
			machineDao.createMachine(machine);
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
}
