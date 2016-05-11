package com.chdp.chdpweb.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

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
import com.chdp.chdpweb.printer.PrintHelper;
import com.github.pagehelper.PageHelper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

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

	@Autowired
	private DataSourceTransactionManager transactionManager;

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

	public boolean add(Prescription prs) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);

		try {
			prsDao.createPrescription(prs);

			User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute("user");

			Process newProcess = new Process();
			newProcess.setProcess_type(Constants.RECEIVE);
			newProcess.setUser_id(currentUser.getId());
			newProcess.setPrescription_id(prs.getId());
			newProcess.setBegin(Utils.getCurrentDateAndTime());
			newProcess.setPrevious_process_id(0);
			processDao.createProcess(newProcess);

			prs.setProcess_id(newProcess.getId());
			prsDao.updatePrescriptionProcess(prs);

			transactionManager.commit(status);
			return true;
		} catch (Exception e) {
			transactionManager.rollback(status);
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

	public Prescription getPrescriptionByPourMachineUuid(String uuid) {
		try {
			return prsDao.getPrescriptionByPourMachineUuid(uuid);
		} catch (Exception e) {
			return null;
		}
	}

	public List<Prescription> listPrsWithProcessUnfinished() {
		try {
			List<Prescription> prsList = prsDao.getPrescriptionsUnfinished();
			return prsList;
			// return this.updatePrsListwithUsername(prsList);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	public List<Prescription> listPrsWithProcessUnfinished(int pageNum) {
		PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
		try {
			List<Prescription> prsList = prsDao.getPrescriptionsUnfinished();
			return prsList;
			// return this.updatePrsListwithUsername(prsList);
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

	public List<Integer> listInProgressHospitalwithProcess(int process) {
		try {
			return prsDao.listInProgressHospitalwithProcess(process);
		} catch (Exception e) {
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
			// return this.updatePrsListwithUsername(prsList);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	// No user_name included 为了出库列表
	public List<Prescription> listShipPrescription(int process, int hospitalId) {
		try {
			return prsDao.listShipPrescription(process, hospitalId);
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
			// return this.updatePrsListwithUsername(prsList);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	// 获取所有与指定医院相关，且订单处于某一特定阶段的处方列表
	public List<Prescription> listPrsWithParamsAndTime(int process, int hospitalId, int pageNum, String start,
			String end) {
		PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
		try {
			return prsDao.getPrescriptionsByParamswithTime(process, hospitalId, start, end);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	// 获取所有与指定医院相关，且订单处于某一特定阶段的处方列表
	public List<Prescription> listPrsWithParamsAndTime(int process, int hospitalId, String start, String end) {
		try {
			return prsDao.getPrescriptionsByParamswithTime(process, hospitalId, start, end);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	public List<Prescription> listPrsWithHospital(int hospitalId) {
		try {
			List<Prescription> prsList = prsDao.getPrescriptionByHospitalId(hospitalId);
			return prsList;
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	public List<Prescription> listPrsWithHospital(int hospitalId, int pageNum) {
		PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
		try {
			List<Prescription> prsList = prsDao.getPrescriptionByHospitalId(hospitalId);
			return prsList;
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	public List<Prescription> listPrsWithParams(int process, int hospitalId) {
		try {
			List<Prescription> prsList = prsDao.getPrescriptionsByParams(process, hospitalId);
			return prsList;
			// return this.updatePrsListwithUsername(prsList);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	public List<Prescription> listPrsWithParams(int process, int hospitalId, int pageNum) {
		PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
		try {
			List<Prescription> prsList = prsDao.getPrescriptionsByParams(process, hospitalId);
			return prsList;
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

	public int countPrsNumForHospital(int hospitalId, int process, String start, String end) {
		try {
			return prsDao.countPrsNumForHospital(hospitalId, process, start, end);
		} catch (Exception e) {
			return 0;
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

	public List<Prescription> listPrsByUser(int userAuth, int userId, String start, String end) {
		try {
			User user = userDao.getUserById(userId);
			List<Prescription> prsList = new ArrayList<Prescription>();
			if (userAuth == 0) {
				List<Integer> prsIdList = prsDao.listDealPrsIdByUser_NoShip(userId, start, end);
				if ((user.getAuthority() & 1) != 0) {
					List<Integer> tempPrsIdList = prsDao.listDealPrsIdByUserr_Ship(userId, start, end);
					prsIdList = Utils.mergeTwoPrsIdList(prsIdList, tempPrsIdList);
				}
				Iterator<Integer> itr = prsIdList.iterator();
				Prescription prs = null;
				while (itr.hasNext()) {
					int tempId = itr.next();
					prs = prsDao.getPrescriptionByID(tempId);
					prs.setHospital_name(hospitalDao.getHospitalwithID(prs.getHospital_id()).getName());
					prsList.add(prs);
				}
			} else {
				List<Integer> prsIdList = prsDao.listDealPrsIdByUser_NoShip(userId, start, end);
				if (userAuth == 1) {
					prsIdList = prsDao.listDealPrsIdByUserr_Ship(userId, start, end);
				} else {
					int process_type = Utils.getProcessTypebyUserAuth(userAuth);
					prsIdList = prsDao.listDealPrsIdByUserAndProcess_NoShip(userId, process_type, start, end);
				}
				Iterator<Integer> itr = prsIdList.iterator();
				Prescription prs = null;
				while (itr.hasNext()) {
					int tempId = itr.next();
					prs = prsDao.getPrescriptionByID(tempId);
					prs.setHospital_name(hospitalDao.getHospitalwithID(prs.getHospital_id()).getName());
					prsList.add(prs);
				}
			}

			return prsList;
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	// 接方打印及流转，带事务控制
	public boolean printReceiveList(int hospitalId) {
		List<Prescription> prsList = null;
		if (hospitalId == 0) {
			prsList = listPrsWithProcessNoUser(Constants.RECEIVE);
		} else {
			prsList = listPrsWithProHospitalNoUser(Constants.RECEIVE, hospitalId);
		}

		if (prsList == null)
			return false;

		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			PrintHelper.startAndSetup();
			for (Prescription prs : prsList) {
				User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
				Process currentProcess = processDao.getProcessesById(prs.getProcess_id());

				Process newProcess = new Process();
				newProcess.setProcess_type(Constants.CHECK);
				newProcess.setUser_id(currentUser.getId());
				newProcess.setPrescription_id(prs.getId());
				newProcess.setBegin(Utils.getCurrentDateAndTime());
				newProcess.setPrevious_process_id(prs.getProcess_id());
				processDao.createProcess(newProcess);

				currentProcess.setFinish(Utils.getCurrentDateAndTime());
				processDao.refreshProcessTime(currentProcess);

				prs.setProcess(Constants.CHECK);
				prs.setProcess_id(newProcess.getId());
				prsDao.updatePrescriptionProcess(prs);

				PrintHelper.printPrescription(prs.getPatient_name(), prs.getOuter_id(), prs.getPacket_num(),
						prs.getSex(), prs.getHospital_name(), prs.getUuid(), prs.getCreate_time());
			}
		} catch (Exception e) {
			transactionManager.rollback(status);
			return false;
		} finally {
			PrintHelper.close();
		}

		transactionManager.commit(status);
		return true;
	}

	// 出库单生成逻辑，带事务控制
	public boolean printShipListXls(int hospitalId) {
		if (hospitalId == 0)
			return false;

		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);

		User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute("user");

		String uuid = Utils.generateUuid();
		Order newOrder = new Order();
		newOrder.setUuid(uuid);
		newOrder.setHospital_id(hospitalId);
		newOrder.setCreate_time(Utils.getCurrentDateAndTime());
		newOrder.setCreate_user_id(currentUser.getId());
		newOrder.setStatus(Constants.ORDER_BEGIN);

		try {
			orderDao.createFullOrder(newOrder);

			List<Prescription> prsList = listShipPrescription(Constants.SHIP, hospitalId);

			for (Prescription prs : prsList) {
				prs.setProcess_id(newOrder.getId());
				if (!updatePrescriptionProcess(prs)) {
					transactionManager.rollback(status);
					return false;
				}
			}

			if (!generatePrsListXls(hospitalId, uuid, prsList)) {
				transactionManager.rollback(status);
				return false;
			}
		} catch (Exception e) {
			transactionManager.rollback(status);
			return false;
		}

		transactionManager.commit(status);
		return true;
	}

	// 生成Excel出库单
	private boolean generatePrsListXls(int hospitalId, String orderUuid, List<Prescription> prsList) {
		try {
			Resource tempResource = new ClassPathResource("shipTemplate.xls");

			FileInputStream fis = new FileInputStream(tempResource.getFile());
			HSSFWorkbook templateWb = new HSSFWorkbook(fis);
			HSSFSheet templateSt = templateWb.getSheetAt(0);

			HSSFRow noRow = templateSt.getRow(0);
			HSSFRow titleRow = templateSt.getRow(2);
			HSSFRow itemRow = templateSt.getRow(3);

			Hospital hospital = hospitalDao.getHospitalwithID(hospitalId);
			titleRow.getCell(1).setCellValue(hospital.getName());

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String currentTime = df.format(new Date());
			titleRow.getCell(5).setCellValue(currentTime);

			noRow.getCell(0).setCellValue("No." + orderUuid);

			templateSt.shiftRows(4, templateSt.getLastRowNum(), prsList.size());
			int index = 4;
			double totalPrice = 0;
			int totalNum = 0;

			for (Prescription printItem : prsList) {
				HSSFRow insertRow = templateSt.createRow(index);
				insertRow.setHeightInPoints(25);

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
			lastRow.getCell(1).setCellValue(prsList.size());
			lastRow.getCell(3).setCellValue(totalNum + "帖");
			lastRow.getCell(4).setCellValue(totalPrice);

			lastRow = templateSt.getRow(templateSt.getLastRowNum());
			lastRow.setHeightInPoints(25);

			Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			hints.put(EncodeHintType.MARGIN, 1);
			BitMatrix bitMatrix = new MultiFormatWriter().encode(orderUuid, BarcodeFormat.QR_CODE, 95, 95, hints);

			BufferedImage image = new BufferedImage(bitMatrix.getWidth(), bitMatrix.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			for (int x = 0; x < bitMatrix.getWidth(); x++) {
				for (int y = 0; y < bitMatrix.getHeight(); y++) {
					image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
				}
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpeg", baos);

			HSSFClientAnchor anchor = new HSSFClientAnchor();
			anchor.setCol1(5);
			anchor.setRow1(0);
			Drawing drawing = templateSt.createDrawingPatriarch();
			Picture picture = drawing.createPicture(anchor,
					templateWb.addPicture(baos.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
			picture.resize();

			String newPath = Constants.TEMPPATH + hospital.getName() + "-" + orderUuid + ".xls";
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

	/** ------------------------------------------------------------------ **/
	// 根据出库单ID获取所属的处方List
	public List<Prescription> getPrsListByOrderId(int orderId, String start, String end) {
		try {
			Order order = orderDao.getOrderById(orderId);
			return prsDao.getPrsListByOrderId(order.getId(), order.getHospital_id(), start, end);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	// 根据出库单ID获取所属的处方List, 用于分页
	public List<Prescription> getPrsListByOrderId(int orderId, String start, String end, int pageNum) {
		PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
		try {
			Order order = orderDao.getOrderById(orderId);
			return prsDao.getPrsListByOrderId(order.getId(), order.getHospital_id(), start, end);
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	// 根据用户Id查询此用户已经完成的所有处方数量
	public List<Prescription> getPrsListByUserId(int userId, String start, String end) {
		try {
			List<Prescription> prsList = prsDao.getPrsListFromProcessByUserId(userId, start, end);
			List<Prescription> prsByOrderList = prsDao.getPrsListFromOrderByUserId(userId, start, end);
			for (Prescription prs : prsByOrderList) {
				if (prsList.contains(prs)) {
					prsList.add(prs);
				}
			}
			return prsList;
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	// 根据用户Id查询此用户已经完成的所有处方数量
	public List<Prescription> getPrsListByUserId(int userId, String start, String end, int pageNum) {
		PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
		try {
			List<Prescription> prsList = prsDao.getPrsListFromProcessByUserId(userId, start, end);
			List<Prescription> prsByOrderList = prsDao.getPrsListFromOrderByUserId(userId, start, end);
			for (Prescription prs : prsByOrderList) {
				if (prsList.contains(prs)) {
					prsList.add(prs);
				}
			}
			return prsList;
		} catch (Exception e) {
			return new ArrayList<Prescription>();
		}
	}

	// 根据用户Id统计用户在一个时间段内处理处方的情况
	public List<User> getUserListForPrsSummary(int userAuth, String start, String end, int pageNum) {
		PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
		try {
			List<User> userList = null;
			if (userAuth == 0) {
				userList = userDao.getUserList();
			} else {
				userList = userDao.getUserListWithAuth(userAuth);
			}

			for (User user : userList) {
				int fivePacketNum = 0;
				int sevenPacketNum = 0;
				int tenPacketNum = 0;
				int fourteenPacketNum = 0;
				int otherPacketNum = 0;

				List<Prescription> prsList = this.getPrsListByUserId(user.getId(), start, end);
				user.setDone_prs_num(prsList.size());
				for (Prescription prs : prsList) {
					int packet_num = prs.getPacket_num();
					if (packet_num == 5) {
						fivePacketNum += 1;
					} else if (packet_num == 7) {
						sevenPacketNum += 1;
					} else if (packet_num == 10) {
						tenPacketNum += 1;
					} else if (packet_num == 14) {
						fourteenPacketNum += 1;
					} else {
						otherPacketNum += 1;
					}
				}
				user.setPrs_five_packet_num(fivePacketNum);
				user.setPrs_seven_packet_num(sevenPacketNum);
				user.setPrs_ten_packet_num(tenPacketNum);
				user.setPrs_fourteen_packet_num(fourteenPacketNum);
				user.setPrs_other_packet_num(otherPacketNum);
				Integer errorNum = prsDao.getErrorProcessByUserId(user.getId(), start, end);
				if (errorNum != null) {
					user.setError_num(errorNum);
				} else {
					user.setError_num(0);
				}
			}
			return userList;
		} catch (Exception e) {
			return new ArrayList<User>();
		}
	}

}
