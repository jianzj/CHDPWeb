package com.chdp.chdpweb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.Hospital;
import com.chdp.chdpweb.dao.HospitalDao;
import com.github.pagehelper.PageHelper;

@Repository
public class HospitalService {

	@Autowired
	private HospitalDao hospitalDao;
	
	public List<Hospital> getHospitalList(int pageNum){
		PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
		try{
			return hospitalDao.getHospitalList();
		} catch (Exception e){
			return new ArrayList<Hospital>();
		}
	}
	
	public boolean addHospital(Hospital hospital){
		try{
			hospitalDao.createHospital(hospital);
			return true;
		}catch (Exception e){
			return false;
		}
	}

	public boolean isHospitalInUse(int hospitalId){
		try{
			if(hospitalDao.countPrescriptionsWithHospital(hospitalId) > 0){
				return true;
			}
			
			return false;
		} catch (Exception e){
			return false;
		}
	}
	
	public boolean deleteHospital(int hospitalId){
		try{
			hospitalDao.deleteHospital(hospitalId);
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
	public boolean doesHospitalExist(Hospital hospital){
		try{
			if (hospitalDao.getHospitalIdwithName(hospital.getName()) > 0){
				return true;
			}
			return false;
		} catch (Exception e){
			return false;
		}
	}
	
	public int getHospitalIdByName(String hospitalName){
		try{
			return hospitalDao.getHospitalIdwithName(hospitalName);
		} catch (Exception e){
			return -1;
		}
	}
}
