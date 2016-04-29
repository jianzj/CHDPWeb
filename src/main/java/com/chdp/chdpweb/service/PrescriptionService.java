package com.chdp.chdpweb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.chdp.chdpweb.bean.Prescription;
import com.chdp.chdpweb.dao.PrescriptionDao;

@Repository
public class PrescriptionService {

	@Autowired
	private PrescriptionDao prsDao;

	public Prescription getPrescription(int id){
		try{
			return prsDao.getPrescriptionByID(id);
		} catch (Exception e){
			return null;
		}
	}
	
	public Prescription getLastestPrs(){
		try{
			return prsDao.getLastestPrescription();
		} catch (Exception e){
			return null;
		}
	}
	
	public boolean createPrescription(Prescription prs){
		try{
			prsDao.createPrescription(prs);
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
	public Prescription getPrescriptionByUuid(String uuid){
		try{
			return prsDao.getPrescriptionByUUID(uuid);
		} catch (Exception e){
			return null;
		}
	}
	
	public List<Prescription> listPrsWithProcess(int process){
		try{
			return prsDao.getPrescriptionsByProcess(process);
		} catch (Exception e){
			return new ArrayList<Prescription>();
		}
	}
	
	public List<Prescription> listPrsWithParams(int process, String hospitalName){
		try{
			return prsDao.getPrescriptionsByParams(process, hospitalName);
		} catch (Exception e){
			return new ArrayList<Prescription>();
		}
	}
	
	public boolean updatePrescriptionProcess(Prescription prs){
		try{
			prsDao.updatePrescriptionProcess(prs);
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
	public boolean deletePrescription(int prsId){
		try{
			prsDao.deletePrescription(prsId);
			return true;
		} catch (Exception e){
			return false;
		}
	}

	public boolean validPrescriptionHospitalInfo(Prescription prs){
		try{
			int count = prsDao.countPrescriptionWithHospitalInfo(prs);
			if (count > 0){
				return true;
			}
			return false;
		} catch (Exception e){
			return false;
		}
	}
	
	public double formatPricewithPrs(Prescription prs, String price){
		try{
			double newPrice = Double.parseDouble(price);
			return newPrice;
		} catch (Exception e){
			return prs.getPrice();
		}
	}
	
	public boolean updatePrsInReceive(Prescription prs){
		try{
			prsDao.updatePrescriptionByPhase1(prs);
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
	public boolean equalTwoPrescription(Prescription prs1, Prescription prs2){
		try{
			if (prs1.getHospital_name().equals(prs2.getHospital_name()) &&
					prs1.getOuter_id().equals(prs2.getOuter_id()) &&
					prs1.getPatient_name().equals(prs2.getPatient_name()) &&
					prs1.getPacket_num() == prs2.getPacket_num() &&
					prs1.getPrice() == prs2.getPrice() &&
					prs1.getSex() == prs2.getSex()){
				return true;
			}else{
				return false;
			}
		} catch (Exception e){
			return false;
		}
	}
	
	public boolean equalHospitalInfo(Prescription prs1, Prescription prs2){
		try{
			if (prs1.getHospital_name().equals(prs2.getHospital_name()) && prs1.getOuter_id().equals(prs2.getOuter_id())){
				return true;
			}
			return false;
		} catch (Exception e){
			return false;
		}
	}
}
