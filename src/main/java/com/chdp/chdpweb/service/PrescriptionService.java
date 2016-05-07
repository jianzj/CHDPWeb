package com.chdp.chdpweb.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.Hospital;
import com.chdp.chdpweb.bean.Order;
import com.chdp.chdpweb.bean.Prescription;
import com.chdp.chdpweb.bean.Process;
import com.chdp.chdpweb.bean.User;
import com.chdp.chdpweb.common.Utils;
import com.chdp.chdpweb.dao.HospitalDao;
import com.chdp.chdpweb.dao.OrderDao;
import com.chdp.chdpweb.dao.PrescriptionDao;
import com.chdp.chdpweb.dao.ProcessDao;
import com.chdp.chdpweb.dao.UserDao;
import com.github.pagehelper.PageHelper;

@Repository
public class PrescriptionService {

	@Autowired
	private PrescriptionDao prsDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ProcessDao processDao;
	@Autowired
	private HospitalDao hospitalDao;
	
	public Prescription getPrescription(int id) {
		try {
			Prescription prs = prsDao.getPrswithIdNoUser(id);
			if (prs.getProcess() == Constants.FINISH) {
				Order order = orderDao.getOrderById(prs.getProcess_id());
				prs.setUser_name(userDao.getUserById(order.getOutbound_user_id()).getName());
			} else if (prs.getProcess() == Constants.SHIP) {
				if (prs.getProcess_id() == -1) {
					// Process pro =
					// processDao.getProcesswithPrsIdandProcess(prs.getId(),
					// Constants.PACKAGE);
					// prs.setUser_name(userDao.getUserById(pro.getUser_id()).getName());
					prs.setUser_name("尚未打印出库单!");
				} else {
					Order order = orderDao.getOrderById(prs.getProcess_id());
					prs.setUser_name(userDao.getUserById(order.getCreate_user_id()).getName());
				}
			} else {
				Process proc = processDao.getProcessesById(prs.getProcess_id());
				prs.setUser_name(userDao.getUserById(proc.getUser_id()).getName());
			}
			return prs;
		} catch (Exception e) {
			return null;
		}
	}

	public Prescription getPrsNoUser(int id) {
		try {
			return prsDao.getPrswithIdNoUser(id);
		} catch (Exception e) {
			return null;
		}
	}

	public Prescription getLastestPrs() {
		try {
			return prsDao.getLastestPrescription();
		} catch (Exception e) {
			return null;
		}
	}

	public boolean createPrescription(Prescription prs) {
		try {
			prsDao.createPrescription(prs);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Prescription getPrescriptionByUuid(String uuid) {
		try {
			return prsDao.getPrescriptionByUUID(uuid);
		} catch (Exception e) {
			return null;
		}
	}

	public List<Prescription> listPrsWithProcessUnfinished() {
		try {
			List<Prescription> prsList = prsDao.getPrescriptionsUnfinished();
			return prsList;
			//return this.updatePrsListwithUsername(prsList);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	public List<Prescription> listPrsWithProcessUnfinished(int pageNum) {
		PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
		try {
			List<Prescription> prsList = prsDao.getPrescriptionsUnfinished();
			return prsList;
			//return this.updatePrsListwithUsername(prsList);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	// No user_name included
	public List<Prescription> listPrsWithProcessNoUser_Ship(int process) {
		try {
			return prsDao.getPrsListWithProcess_Ship(process);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}
	
	// No user_name included
	public List<Prescription> listPrsWithProcessNoUser(int process) {
		try {
			return prsDao.getPrsListWithProcess(process);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}
	
	public List<Integer> listInProgressHospitalwithProcess(int process){
		try{
			return prsDao.listInProgressHospitalwithProcess(process);
		} catch (Exception e){
			return new ArrayList<Integer>();
		}
	}

	public List<Prescription> listPrsWithProcessAndTime(int process, int pageNum, String start, String end) {
		PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
		try {
			return prsDao.getPrescriptionsByProcessAndTime(process, start, end);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	public List<Prescription> listPrsWithProcess(int process) {
		try {
			List<Prescription> prsList = prsDao.getPrescriptionsByProcess(process);
			return prsList;
			//return this.updatePrsListwithUsername(prsList);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	// No user_name included 为了出库列表
	public List<Prescription> listPrsWithProHospitalNoUser_Ship(int process, int hospitalId) {
		try {
			return prsDao.getPrsListWithProAndHospital_Ship(process, hospitalId);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}
	
	// No user_name included
	public List<Prescription> listPrsWithProHospitalNoUser(int process, int hospitalId) {
		try {
			return prsDao.getPrsListWithProAndHospital(process, hospitalId);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	public List<Prescription> listPrsWithProcess(int process, int pageNum) {
		PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
		try {
			List<Prescription> prsList = prsDao.getPrescriptionsByProcess(process);
			return prsList;
			//return this.updatePrsListwithUsername(prsList);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	public List<Prescription> listPrsWithParamsAndTime(int process, int hospitalId, int pageNum, String start,
			String end) {
		PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
		try {
			return prsDao.getPrescriptionsByParamswithTime(process, hospitalId, start, end);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	public List<Prescription> listPrsWithHospital(int hospitalId) {
		try {
			List<Prescription> prsList = prsDao.getPrescriptionByHospitalName(hospitalId);
			return prsList;
			//return this.updatePrsListwithUsername(prsList);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	public List<Prescription> listPrsWithHospital(int hospitalId, int pageNum) {
		PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
		try {
			List<Prescription> prsList = prsDao.getPrescriptionByHospitalName(hospitalId);
			return prsList;
			//return this.updatePrsListwithUsername(prsList);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	public List<Prescription> listPrsWithParams(int process, int hospitalId) {
		try {
			List<Prescription> prsList = prsDao.getPrescriptionsByParams(process, hospitalId);
			return prsList;
			//return this.updatePrsListwithUsername(prsList);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	public List<Prescription> listPrsWithParams(int process, int hospitalId, int pageNum) {
		PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
		try {
			List<Prescription> prsList = prsDao.getPrescriptionsByParams(process, hospitalId);
			return prsList;
			//return this.updatePrsListwithUsername(prsList);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	public boolean updatePrescriptionProcess(Prescription prs) {
		try {
			prsDao.updatePrescriptionProcess(prs);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean deletePrescription(int prsId) {
		try {
			prsDao.deletePrescription(prsId);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean validPrescriptionHospitalInfo(Prescription prs) {
		try {
			int count = prsDao.countPrescriptionWithHospitalInfo(prs);
			if (count > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public double formatPricewithPrs(Prescription prs, String price) {
		try {
			double newPrice = Double.parseDouble(price);
			return newPrice;
		} catch (Exception e) {
			return prs.getPrice();
		}
	}

	public boolean updatePrsBasicInfo(Prescription prs) {
		try {
			prsDao.updatePrescriptionByPhase1(prs);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean updatePrsInReceive(Prescription prs) {
		try {
			prsDao.updatePrescriptionByPhase1(prs);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean equalTwoPrescription(Prescription prs1, Prescription prs2) {
		try {
			if (prs1.getHospital_name().equals(prs2.getHospital_name()) && prs1.getOuter_id().equals(prs2.getOuter_id())
					&& prs1.getPatient_name().equals(prs2.getPatient_name())
					&& prs1.getPacket_num() == prs2.getPacket_num() && prs1.getPrice() == prs2.getPrice()
					&& prs1.getSex() == prs2.getSex()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public boolean equalHospitalInfo(Prescription prs1, Prescription prs2) {
		try {
			if (prs1.getHospital_name().equals(prs2.getHospital_name())
					&& prs1.getOuter_id().equals(prs2.getOuter_id())) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public int countPrsNumForHospital(int hospitalId, int process, String start, String end){
		try{
			return prsDao.countPrsNumForHospital(hospitalId, process, start, end);
		} catch (Exception e){
			return 0;
		}
	}

	public boolean printReceiveLabel(String uuid) {

		return true;
	}

	public boolean printPackageLabel(String uuid) {

		return true;
	}

	public boolean printSinglePackage(String uuid) {
		return true;
	}

	public boolean printSinglePrs(String uuid) {
		return true;
	}

	public boolean generatePrsListXls(int hospitalId, User user, List<Prescription> prs) {
		try {
			String templatePath = Constants.TEMPLATEPATH + "/template.xls";

			File tempFile = new File(templatePath);
			if (!tempFile.exists()) {
				return false;
			}

			FileInputStream fis = new FileInputStream(templatePath);
			HSSFWorkbook templateWb = new HSSFWorkbook(fis);
			HSSFSheet templateSt = templateWb.getSheetAt(0);

			HSSFRow titleRow = templateSt.getRow(1);
			HSSFRow itemRow = templateSt.getRow(2);

			Hospital hospital = hospitalDao.getHospitalwithID(hospitalId);
			titleRow.getCell(1).setCellValue(hospital.getName());

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String currentTime = df.format(new Date());
			titleRow.getCell(5).setCellValue(currentTime);

			int prsNum = prs.size();
			templateSt.shiftRows(3, templateSt.getLastRowNum(), prsNum);
			int index = 3;
			double totalPrice = 0;
			int totalNum = 0;

			Iterator<Prescription> itr = prs.iterator();
			Prescription printItem = null;
			while (itr.hasNext()) {
				printItem = itr.next();
				HSSFRow insertRow = templateSt.createRow(index);

				insertRow.createCell(0).setCellValue(printItem.getUuid());
				insertRow.getCell(0).setCellStyle(itemRow.getCell(0).getCellStyle());

				insertRow.createCell(1).setCellValue(printItem.getOuter_id());
				insertRow.getCell(1).setCellStyle(itemRow.getCell(1).getCellStyle());

				insertRow.createCell(2).setCellValue(printItem.getPatient_name());
				insertRow.getCell(2).setCellStyle(itemRow.getCell(2).getCellStyle());

				insertRow.createCell(3).setCellValue(printItem.getPacket_num());
				insertRow.getCell(3).setCellStyle(itemRow.getCell(3).getCellStyle());

				insertRow.createCell(4).setCellValue(printItem.getPrice());
				insertRow.getCell(4).setCellStyle(itemRow.getCell(4).getCellStyle());

				insertRow.createCell(5).setCellValue("");
				insertRow.getCell(5).setCellStyle(itemRow.getCell(5).getCellStyle());

				totalPrice += printItem.getPrice();
				totalNum += printItem.getPacket_num();
				index += 1;
			}

			HSSFRow lastRow = templateSt.getRow(templateSt.getLastRowNum() - 1);
			lastRow.getCell(1).setCellValue(prs.size());
			lastRow.getCell(3).setCellValue(totalNum + "帖");
			lastRow.getCell(4).setCellValue(totalPrice);

			// 生成UUID，形如20160502213800386, 当前日期+三位随机数
			int randomNum = (int) (Math.random() * 900) + 100;
			df = new SimpleDateFormat("yyyyMMdd-HHmmss");
			currentTime = df.format(new Date());
			String uuid = currentTime + String.valueOf(randomNum);

			String newPath = hospital.getName() + "-" + uuid + ".xls";
			File newShipList = new File(newPath);
			try {
				newShipList.createNewFile();
			} catch (Exception e) {
				templateWb.close();
				fis.close();
				return false;
			}

			FileOutputStream fileOut = new FileOutputStream(newShipList);
			templateWb.write(fileOut);
			fileOut.flush();
			fileOut.close();
			fis.close();
			templateWb.close();

			return true;
		} catch (Exception e) {

			return false;
		}
	}

	public boolean updatePrsProcess(Prescription prs) {
		try {
			prsDao.updatePrescriptionProcess(prs);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean markPrsFinished(Prescription prs) {
		try {
			prsDao.markPrsFinished(prs);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 用户维度
	public int countErrorProByUser(int userId, String start, String end) {
		try {
			return prsDao.countProcsErrorByUser(userId, start, end);
		} catch (Exception e) {
			return 0;
		}
	}

	public List<Prescription> listPrswithUser(int userId, int processType, String start, String end) {
		try {
			return prsDao.listPrsByUser(userId, processType, start, end);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	public List<Prescription> updatePrsListwithUsername(List<Prescription> prsList) {
		try {
			List<Prescription> finalPrsList = new ArrayList<Prescription>();
			Iterator<Prescription> itr = prsList.iterator();
			Prescription prs = null;
			while (itr.hasNext()) {
				prs = itr.next();
				if (prs.getProcess() == Constants.SHIP) {
					if (prs.getProcess_id() == -1) {
						prs.setUser_name("尚未打印出库单!");
					} else {
						Order order = orderDao.getOrderById(prs.getProcess_id());
						prs.setUser_name(userDao.getUserById(order.getCreate_user_id()).getName());
					}
				} else {
					Process proc = processDao.getProcessesById(prs.getProcess_id());
					prs.setUser_name(userDao.getUserById(proc.getUser_id()).getName());
				}
				finalPrsList.add(prs);
			}
			return finalPrsList;
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	// 此方法主要用来取得最近的process的id
	public int getLastestProcIdwithPrsandProcess(int prsId, int process_type) {
		try {
			Process pro = processDao.getProcesswithPrsIdandProcess(prsId, Constants.PACKAGE);
			return pro.getId();
		} catch (Exception e) {
			return -1;
		}
	}
	
	//获得用户维度的处方列表
	public List<User> getUserListForPrsSummary(int userAuth, String start, String end){
		try{
			List<User> userList = null;
			if (userAuth == 0){
				userList = userDao.getUserList();
				//userList = userDao.getUserListWithoutAdmin();
			}else{
				userList = userDao.getUserListWithAuth(userAuth);
			}
			List<User> finalUserList = new ArrayList<User>();
			Iterator<User> itr = userList.iterator();
			User user = null;
			while (itr.hasNext()){
				user = itr.next();
				int prsNum = 0;
				int errorNum = 0;
				if (userAuth == 0){
					List<Integer> tempPrsIdList = prsDao.listDealPrsIdByUser_NoShip(user.getId(), start, end);
					if ((user.getAuthority() & 1) != 0){
						List<Integer> tempPrsIdList02 = prsDao.listDealPrsIdByUserr_Ship(user.getId(), start, end);
						tempPrsIdList = Utils.mergeTwoPrsIdList(tempPrsIdList, tempPrsIdList02);
					}
					prsNum = tempPrsIdList.size();
				}else{
					int process_type = Utils.getProcessTypebyUserAuth(userAuth);
					if (process_type == Constants.SHIP){
						prsNum = prsDao.countPrsDealByUser_Ship(user.getId(), start, end);
					}else{
						prsNum = prsDao.countPrsDealByUserAndProcess_NoShip(user.getId(), process_type, start, end);
					}
				}
				if (userAuth == 0){
					Integer temp = prsDao.countProcsErrorByUser(user.getId(), start, end);
					if (temp == null){
						errorNum = 0;
					}else{
						errorNum = temp;
					}
				}else{
					//int process_type = Utils.getProcessTypebyUserAuth(userAuth);
					//Integer temp = prsDao.countProcsErrorByUse_Process(process_type,user.getId(), start, end);
					Integer temp = prsDao.countProcsErrorByUser(user.getId(), start, end);
					if (temp == null){
						errorNum = 0;
					}else{
						errorNum = temp;
					}
				}
				user.setDone_prs_num(prsNum);
				user.setError_num(errorNum);
				user.setPosition(user.getAuthority_str());
				finalUserList.add(user);
			}
			
			return finalUserList;
		}catch (Exception e){
			return new ArrayList<User>();
		}
	}

	public List<Prescription> listPrsByUser(int userAuth, int userId, String start, String end){
		try{
			User user = userDao.getUserById(userId);
			List<Prescription> prsList = new ArrayList<Prescription>();
			if (userAuth == 0){
				List<Integer> prsIdList = prsDao.listDealPrsIdByUser_NoShip(userId, start, end);
				if ((user.getAuthority() & 1) != 0){
					List<Integer> tempPrsIdList = prsDao.listDealPrsIdByUserr_Ship(userId, start, end);
					prsIdList = Utils.mergeTwoPrsIdList(prsIdList, tempPrsIdList);
				}
				Iterator<Integer> itr = prsIdList.iterator();
				Prescription prs = null;
				while (itr.hasNext()){
					int tempId = itr.next();
					prs = prsDao.getPrescriptionByID(tempId);
					prs.setHospital_name(hospitalDao.getHospitalwithID(prs.getHospital_id()).getName());
					prsList.add(prs);
				}
			}else{
				List<Integer> prsIdList = prsDao.listDealPrsIdByUser_NoShip(userId, start, end);
				if (userAuth == 1){
					prsIdList = prsDao.listDealPrsIdByUserr_Ship(userId, start, end);
				}else{
					int process_type = Utils.getProcessTypebyUserAuth(userAuth);
					prsIdList = prsDao.listDealPrsIdByUserAndProcess_NoShip(userId, process_type, start, end);
				}
				Iterator<Integer> itr = prsIdList.iterator();
				Prescription prs = null;
				while (itr.hasNext()){
					int tempId = itr.next();
					prs = prsDao.getPrescriptionByID(tempId);
					prs.setHospital_name(hospitalDao.getHospitalwithID(prs.getHospital_id()).getName());
					prsList.add(prs);
				}
			}
			
			return prsList;
		} catch (Exception e){
			return new ArrayList<Prescription>();
		}
	}
}
