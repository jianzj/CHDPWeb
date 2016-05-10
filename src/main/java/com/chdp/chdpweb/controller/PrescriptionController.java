package com.chdp.chdpweb.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.Hospital;
import com.chdp.chdpweb.bean.Order;
import com.chdp.chdpweb.bean.Prescription;
import com.chdp.chdpweb.bean.User;
import com.chdp.chdpweb.common.Utils;
import com.chdp.chdpweb.printer.PrintHelper;
import com.chdp.chdpweb.service.HospitalService;
import com.chdp.chdpweb.service.OrderService;
import com.chdp.chdpweb.service.PrescriptionService;
import com.chdp.chdpweb.service.ProcessService;
import com.chdp.chdpweb.service.UserService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/prescription")
public class PrescriptionController {

	@Autowired
	private PrescriptionService prsService;
	@Autowired
	private HospitalService hospitalService;
	@Autowired
	private ProcessService proService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;

	@RequiresRoles(value = { "ADMIN", "RECEIVE" }, logical = Logical.OR)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request) {
		request.setAttribute("nav", "出库流程列表");

		Prescription lastestPrs = prsService.getLastestPrs();
		if (lastestPrs != null) {
			request.setAttribute("lastestPrs", lastestPrs);
		}

		List<Hospital> hospitalList = hospitalService.getHospitalList();
		request.setAttribute("hospitalList", hospitalList);

		return "prescription/addPrescription";
	}

	@RequiresRoles(value = { "ADMIN", "RECEIVE" }, logical = Logical.OR)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(HttpServletRequest request, Prescription prs) {
		request.setAttribute("nav", "出库流程列表");

		List<Hospital> hospitalList = hospitalService.getHospitalList();
		request.setAttribute("hospitalList", hospitalList);

		if (prs.getPacket_num() == -1) {
			prs.setPacket_num(request.getParameter("packet_num_other") == null ? 0
					: Integer.parseInt(request.getParameter("packet_num_other")));
		}

		String price = request.getParameter("prs_price");
		double priceNum = 0;
		try {
			priceNum = Double.parseDouble(price);
		} catch (Exception e) {
			request.setAttribute("errorMsg", "您输入的价格格式不正确，请重新输入！");

			request.setAttribute("prsAdd", prs);
			return "prescription/addPrescription";
		}
		prs.setPrice(priceNum);

		if (prsService.validPrescriptionHospitalInfo(prs)) {
			request.setAttribute("errorMsg", "与此处方相同医院名称，相同订单编号的处方已经存在，请检查输入！");
		} else {
			prs.setCreate_time(Utils.getCurrentDateAndTime());
			prs.setUuid(Utils.generateUuid());
			prs.setProcess(Constants.RECEIVE);

			if (prsService.add(prs)) {
				request.setAttribute("successMsg", "添加处方成功！");
			} else {
				request.setAttribute("errorMsg", "添加处方失败，请稍后重试！");
			}
		}

		request.setAttribute("prsAdd", prs);
		return "prescription/addPrescription";
	}

	@RequiresRoles(value = { "ADMIN", "RECEIVE", "PACKAGE", "SHIP" }, logical = Logical.OR)
	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public String modify(HttpServletRequest request) {
		String from = request.getParameter("from");
		String prsIdStr = request.getParameter("prsId");

		if (from == null) {
			from = "RECEIVE";
		}

		List<Hospital> hospitalList = hospitalService.getHospitalList();
		request.setAttribute("hospitalList", hospitalList);
		// request.setAttribute("hospital", "ALL");
		List<Prescription> prsList = null;

		if (prsIdStr == null) {
			request.setAttribute("errorMsg", "未知处方ID，无法修改！");
			if (from.equals("RECEIVE")) {
				prsList = prsService.listPrsWithProcessNoUser(Constants.RECEIVE);
				request.setAttribute("receiveList", prsList);
				return "process/receiveList";
			} else if (from.equals("PACKAGE")) {
				prsList = prsService.listPrsWithProcessNoUser(Constants.PACKAGE);
				request.setAttribute("packageList", prsList);
				return "process/packageList";
			} else if (from.equals("SHIP")) {
				prsList = prsService.listPrsWithProcessNoUser(Constants.SHIP);
				request.setAttribute("shipList", prsList);
				return "process/shipList";
			} else if (from.equals("CURRENT")) {
				prsList = prsService.listPrsWithProcessUnfinished(1);
				// request.setAttribute("hospital", "ALL");
				request.setAttribute("process", 0);
				request.setAttribute("currentPrsList", prsList);
				PageInfo<Prescription> page = new PageInfo<Prescription>(prsList);
				request.setAttribute("page", page);
				return "prescription/currentPrsList";
			}
		}

		int prsId = Integer.parseInt(prsIdStr);

		User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
		if (from.equals("RECEIVE")
				&& ((currentUser.getAuthority() & 1024) == 0 && (currentUser.getAuthority() & 512) == 0)) {
			request.setAttribute("errorMsg", "您暂无此操作权限！");
			prsList = prsService.listPrsWithProcessNoUser(Constants.RECEIVE);
			request.setAttribute("receiveList", prsList);
			return "process/receiveList";
		} else if (from.equals("PACKAGE")
				&& ((currentUser.getAuthority() & 1024) == 0 && (currentUser.getAuthority() & 2) == 0)) {
			request.setAttribute("errorMsg", "您暂无此操作权限！");
			prsList = prsService.listPrsWithProcessNoUser(Constants.PACKAGE);
			request.setAttribute("packageList", prsList);
			return "process/packageList";
		} else if (from.equals("SHIP")
				&& ((currentUser.getAuthority() & 1024) == 0 && (currentUser.getAuthority() & 1) == 0)) {
			request.setAttribute("errorMsg", "您暂无此操作权限！");
			prsList = prsService.listPrsWithProcessNoUser(Constants.SHIP);
			request.setAttribute("shipList", prsList);
			return "process/shipList";
		} else if (from.equals("CURRENT") && ((currentUser.getAuthority() & 1024) == 0)) {
			request.setAttribute("errorMsg", "您暂无此操作权限！");
			prsList = prsService.listPrsWithProcessUnfinished(1);
			// request.setAttribute("hospital", "ALL");
			request.setAttribute("process", 0);
			request.setAttribute("currentPrsList", prsList);
			PageInfo<Prescription> page = new PageInfo<Prescription>(prsList);
			request.setAttribute("page", page);
			return "prescription/currentPrsList";
		}

		Prescription prs = prsService.getPrsNoUser(prsId);
		request.setAttribute("prsId", prsId);
		request.setAttribute("prsModify", prs);
		request.setAttribute("from", from);
		return "prescription/modifyPrs";
	}

	@RequiresRoles(value = { "ADMIN", "RECEIVE", "PACKAGE", "SHIP" }, logical = Logical.OR)
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modifyPost(HttpServletRequest request, Prescription prs) {

		String prsIdStr = request.getParameter("prsId");
		String from = request.getParameter("from");

		if (from == null) {
			from = "RECEIVE";
		}

		List<Hospital> hospitalList = hospitalService.getHospitalList();
		request.setAttribute("hospitalList", hospitalList);
		// request.setAttribute("hospital", "ALL");
		List<Prescription> prsList = null;

		if (prsIdStr == null) {
			request.setAttribute("errorMsg", "未知处方ID，无法修改！");
			if (from.equals("RECEIVE")) {
				prsList = prsService.listPrsWithProcessNoUser(Constants.RECEIVE);
				request.setAttribute("receiveList", prsList);
				return "process/receiveList";
			} else if (from.equals("PACKAGE")) {
				prsList = prsService.listPrsWithProcessNoUser(Constants.PACKAGE);
				request.setAttribute("packageList", prsList);
				return "process/packageList";
			} else if (from.equals("SHIP")) {
				prsList = prsService.listPrsWithProcessNoUser(Constants.SHIP);
				request.setAttribute("shipList", prsList);
				return "process/shipList";
			} else if (from.equals("CURRENT")) {
				prsList = prsService.listPrsWithProcessUnfinished(1);
				request.setAttribute("process", 0);
				request.setAttribute("currentPrsList", prsList);
				PageInfo<Prescription> page = new PageInfo<Prescription>(prsList);
				request.setAttribute("page", page);
				return "prescription/currentPrsList";
			}
		}

		int prsId = Integer.parseInt(prsIdStr);

		User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
		if (from.equals("RECEIVE")
				&& ((currentUser.getAuthority() & 1024) == 0 && (currentUser.getAuthority() & 512) == 0)) {
			request.setAttribute("errorMsg", "您暂无此操作权限！");
			prsList = prsService.listPrsWithProcessNoUser(Constants.RECEIVE);
			request.setAttribute("receiveList", prsList);
			return "process/receiveList";
		} else if (from.equals("PACKAGE")
				&& ((currentUser.getAuthority() & 1024) == 0 && (currentUser.getAuthority() & 2) == 0)) {
			request.setAttribute("errorMsg", "您暂无此操作权限！");
			prsList = prsService.listPrsWithProcessNoUser(Constants.PACKAGE);
			request.setAttribute("packageList", prsList);
			return "process/packageList";
		} else if (from.equals("SHIP")
				&& ((currentUser.getAuthority() & 1024) == 0 && (currentUser.getAuthority() & 1) == 0)) {
			request.setAttribute("errorMsg", "您暂无此操作权限！");
			prsList = prsService.listPrsWithProcessNoUser(Constants.SHIP);
			request.setAttribute("shipList", prsList);
			return "process/shipList";
		} else if (from.equals("CURRENT") && ((currentUser.getAuthority() & 1024) == 0)) {
			request.setAttribute("errorMsg", "您暂无此操作权限！");
			prsList = prsService.listPrsWithProcessUnfinished(1);
			request.setAttribute("process", 0);
			request.setAttribute("currentPrsList", prsList);
			PageInfo<Prescription> page = new PageInfo<Prescription>(prsList);
			request.setAttribute("page", page);
			return "prescription/currentPrsList";
		}

		Prescription currentPrs = prsService.getPrsNoUser(prsId);

		if (prs.getPacket_num() == -1) {
			prs.setPacket_num(request.getParameter("packet_num_other") == null ? 0
					: Integer.parseInt(request.getParameter("packet_num_other")));
		}

		String price = request.getParameter("price_num");
		double priceNum = 0;
		try {
			priceNum = Double.parseDouble(price);
		} catch (Exception e) {
			request.setAttribute("errorMsg", "您输入的价格格式不正确，请重新输入！");
			request.setAttribute("prsModify", prs);
			request.setAttribute("from", from);
			request.setAttribute("prsId", prsId);
			return "prescription/modifyPrs";
		}

		prs.setPrice(priceNum);
		if (prsService.equalTwoPrescription(currentPrs, prs)) {
			request.setAttribute("errorMsg", "您并未作出任何修改");
			request.setAttribute("prsModify", prs);
			request.setAttribute("from", from);
			request.setAttribute("prsId", prsId);
			return "prescription/modifyPrs";
		} else {
			if ((!prsService.equalHospitalInfo(currentPrs, prs)) && prsService.validPrescriptionHospitalInfo(prs)) {
				request.setAttribute("errorMsg", "您输入的医院名称已经存在，请校验后重新输入！");
				request.setAttribute("prsModify", prs);
				request.setAttribute("from", from);
				request.setAttribute("prsId", prsId);
				return "prescription/modifyPrs";
			} else {
				prs.setId(prsId);
				prs.setHospital_id(hospitalService.getHospitalIdByName(prs.getHospital_name()));
				if (prsService.updatePrsInReceive(prs)) {
					request.setAttribute("successMsg", "修改处方成功！");
				} else {
					request.setAttribute("errorMsg", "处方修改失败，请稍后重试！");
				}
			}
		}

		if (from.equals("RECEIVE")) {
			prsList = prsService.listPrsWithProcessNoUser(Constants.RECEIVE);
			request.setAttribute("receiveList", prsList);
			return "process/receiveList";
		} else if (from.equals("PACKAGE")) {
			prsList = prsService.listPrsWithProcessNoUser(Constants.PACKAGE);
			request.setAttribute("packageList", prsList);
			return "process/packageList";
		} else if (from.equals("SHIP")) {
			prsList = prsService.listPrsWithProcessNoUser(Constants.SHIP);
			request.setAttribute("shipList", prsList);
			return "process/shipList";
		} else if (from.equals("CURRENT")) {
			prsList = prsService.listPrsWithProcessUnfinished(1);
			request.setAttribute("process", 0);
			request.setAttribute("currentPrsList", prsList);
			PageInfo<Prescription> page = new PageInfo<Prescription>(prsList);
			request.setAttribute("page", page);
			return "prescription/currentPrsList";
		}
		return "process/receiveList";
	}

	@RequiresRoles(value = { "ADMIN", "RECEIVE" }, logical = Logical.OR)
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request) {
		String prsIdStr = request.getParameter("prsId");
		String hospitalIdStr = request.getParameter("hospitalId");

		if (prsIdStr == null) {
			request.setAttribute("errorMsg", "未知处方ID，无法删除！");
		} else {
			int prsId = Integer.parseInt(prsIdStr);
			Prescription prs = prsService.getPrsNoUser(prsId);
			if (prs != null) {
				if (prs.getProcess() != Constants.RECEIVE) {
					request.setAttribute("errorMsg", "处方已经进入流转阶段，无法删除！");
				} else {
					if (prsService.deletePrescription(prs.getId())) {
						proService.deleteProcess(prs.getProcess_id());
						request.setAttribute("successMsg", "删除处方成功！");
					} else {
						request.setAttribute("errorMsg", "删除处方出错，请稍后重试！");
					}
				}
			} else {
				request.setAttribute("errorMsg", "处方不存在，无法删除！");
			}
		}
		
		return InternalResourceViewResolver.FORWARD_URL_PREFIX + "process/receiveList?hospitalId=" + hospitalIdStr;
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/currentList", method = RequestMethod.GET)
	public String getCurrentList(HttpServletRequest request,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "hospitalId", defaultValue = "0") int hospitalId,
			@RequestParam(name = "process", defaultValue = "0") int process) {

		request.setAttribute("nav", "当前处方列表");

		List<Prescription> prsList = null;

		if (hospitalId == 0 && process == 0) {
			prsList = prsService.listPrsWithProcessUnfinished(pageNum);
		} else if (hospitalId == 0) {
			prsList = prsService.listPrsWithProcess(process, pageNum);
		} else if (process == 0) {
			prsList = prsService.listPrsWithHospital(hospitalId, pageNum);
		} else {
			prsList = prsService.listPrsWithParams(process, hospitalId, pageNum);
		}

		for (Prescription prsItem : prsList) {
			prsItem.setPhase_name(proService.getPhaseNamewithProcess(prsItem));
		}

		request.setAttribute("hospitalId", hospitalId);
		request.setAttribute("process", process);

		List<Hospital> hospitalList = hospitalService.getHospitalList();
		request.setAttribute("hospitalList", hospitalList);
		request.setAttribute("currentPrsList", prsList);
		PageInfo<Prescription> page = new PageInfo<Prescription>(prsList);
		request.setAttribute("page", page);

		return "prescription/currentPrsList";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/historyList", method = RequestMethod.GET)
	public String getHistoryList(HttpServletRequest request,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId) {

		request.setAttribute("nav", "历史处方列表");

		String start = request.getParameter("startTime");
		String end = request.getParameter("endTime");
		if (start == null || start.equals("")) {
			start = Utils.getOneMonthAgoTime();
		}
		if (end == null || end.equals("")) {
			end = Utils.getCurrentTime();
		}

		request.setAttribute("hospitalId", hospitalId);
		request.setAttribute("startTime", start);
		request.setAttribute("endTime", end);
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		request.setAttribute("hospitalList", hospitalList);

		start = Utils.formatStartTime(start);
		end = Utils.formatEndTime(end);

		if (!Utils.validStartEndTime(start, end)) {
			request.setAttribute("errorMsg", "您输入的时间间隔有误，请重新输入!");
		} else {
			List<Prescription> prsList = null;
			if (hospitalId == 0) {
				prsList = prsService.listPrsWithProcessAndTime(Constants.FINISH, pageNum, start, end);
			} else {
				prsList = prsService.listPrsWithParamsAndTime(Constants.FINISH, hospitalId, pageNum, start, end);
			}
			request.setAttribute("historyPrsList", prsList);
			PageInfo<Prescription> page = new PageInfo<Prescription>(prsList);
			request.setAttribute("page", page);
		}

		return "prescription/historyPrsList";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/hospitalDimensionList", method = RequestMethod.GET)
	public String listHospitalDimension(HttpServletRequest request,
			@RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId) {

		String start = request.getParameter("startTime");
		String end = request.getParameter("endTime");

		if (start == null || start.equals("")) {
			start = Utils.getOneMonthAgoTime();
		}
		if (end == null || end.equals("")) {
			end = Utils.getCurrentTime();
		}

		request.setAttribute("hospitalId", hospitalId);
		request.setAttribute("startTime", start);
		request.setAttribute("endTime", end);
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		request.setAttribute("hospitalList", hospitalList);

		start = Utils.formatStartTime(start);
		end = Utils.formatEndTime(end);

		if (!Utils.validStartEndTime(start, end)) {
			request.setAttribute("errorMsg", "您输入的时间间隔有误，请重新输入!");
		} else {
			List<Hospital> returnHospitalList = hospitalService.getHospitalList();
			List<Prescription> prsList = new ArrayList<Prescription>();
			for (Hospital hospitalItem : returnHospitalList) {
				prsList = prsService.listPrsWithParamsAndTime(Constants.FINISH, hospitalItem.getId(), start, end);
				int packetNum = 0;
				int totalPrice = 0;
				hospitalItem.setFinishedPrsNum(prsList.size());
				for (Prescription item : prsList) {
					packetNum += item.getPacket_num();
					totalPrice += item.getPrice();
				}
				hospitalItem.setTotalPacketNum(packetNum);
				hospitalItem.setTotalPrice(totalPrice);
			}

			Collections.sort(returnHospitalList);
			request.setAttribute("displayHospitalList", returnHospitalList);
		}

		return "prescription/hospitalDimension";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/orderDimensionList", method = RequestMethod.GET)
	public String listOrderDimension(HttpServletRequest request,
			@RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum) {

		request.setAttribute("nav", "出货单维度统计");

		String start = request.getParameter("startTime");
		String end = request.getParameter("endTime");

		request.setAttribute("hospitalId", hospitalId);
		request.setAttribute("startTime", start);
		request.setAttribute("endTime", end);

		if (start == null || start.equals("")) {
			start = Utils.getMinTime();
		}
		if (end == null || end.equals("")) {
			end = Utils.getMinTime();
		}

		start = Utils.formatStartTime(start);
		end = Utils.formatEndTime(end);

		List<Hospital> hospitalList = hospitalService.getHospitalList();

		List<Order> orderList = orderService.listOrderFinished(hospitalId, start, end, pageNum);
		List<Order> finalOrderList = new ArrayList<Order>();

		Iterator<Order> itr = orderList.iterator();
		Order item = null;
		while (itr.hasNext()) {
			item = itr.next();
			int prsNum = orderService.countPrsNumInOrder(item.getId());
			item.setPrs_num(prsNum);
			item.setCreate_user_name(userService.getUserById(item.getCreate_user_id()).getName());
			item.setOutbound_user_name(userService.getUserById(item.getOutbound_user_id()).getName());
			finalOrderList.add(item);
		}

		Collections.sort(finalOrderList);
		request.setAttribute("hospitalList", hospitalList);
		PageInfo<Order> page = new PageInfo<Order>(finalOrderList);
		request.setAttribute("page", page);

		request.setAttribute("displayOrderList", finalOrderList);
		return "prescription/orderDimension";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/userDimensionList", method = RequestMethod.GET)
	public String listUserDimension(HttpServletRequest request,
			@RequestParam(value = "userAuth", defaultValue = "0") int userAuth) {

		String start = request.getParameter("startTime");
		String end = request.getParameter("endTime");

		if (start == null || start.equals("")) {
			start = Utils.getOneMonthAgoTime();
		}
		if (end == null || end.equals("")) {
			end = Utils.getCurrentTime();
		}

		request.setAttribute("userAuth", userAuth);
		request.setAttribute("startTime", start);
		request.setAttribute("endTime", end);

		start = Utils.formatStartTime(start);
		end = Utils.formatEndTime(end);

		if (!Utils.validStartEndTime(start, end)) {
			request.setAttribute("errorMsg", "您输入的时间间隔有误，请重新输入!");
		} else {
			List<User> userList = prsService.getUserListForPrsSummary(userAuth, start, end);
			Collections.sort(userList);
			request.setAttribute("finalUserList", userList);
		}

		return "prescription/userDimension";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/dimensionPrsList", method = RequestMethod.GET)
	public String dimensionPrsList(HttpServletRequest request,
			@RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "from", defaultValue = "HOSPITAL") String from,
			@RequestParam(name = "userId", defaultValue = "0") int userId,
			@RequestParam(value = "orderId", defaultValue = "0") int orderId,
			@RequestParam(name = "userAuth", defaultValue = "0") int userAuth) {

		request.setAttribute("nav", "处方汇总列表");

		String start = request.getParameter("startTime");
		String end = request.getParameter("endTime");

		if (start == null || start.equals("")) {
			start = Utils.getOneMonthAgoTime();
		}
		if (end == null || end.equals("")) {
			end = Utils.getCurrentTime();
		}

		if (from.equals("USER") && userId == 0) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/userDimensionList";
		}

		request.setAttribute("startTime", start);
		request.setAttribute("endTime", end);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("hospitalId", hospitalId);
		request.setAttribute("userId", userId);
		request.setAttribute("from", from);
		request.setAttribute("orderId", orderId);
		request.setAttribute("userAuth", userAuth);

		start = Utils.formatStartTime(start);
		end = Utils.formatEndTime(end);

		if (!Utils.validStartEndTime(start, end)) {
			if (from.equals("HOSPITAL")) {
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/hospitalDimensionList";
			} else if (from.equals("USER")) {
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/userDimensionList";
			}
		}

		List<Prescription> prsList = null;

		if (from.equals("HOSPITAL")) {
			prsList = prsService.listPrsWithParamsAndTime(Constants.FINISH, hospitalId, pageNum, start, end);
		} else if (from.equals("USER")) {
			prsList = prsService.listPrsByUser(userAuth, userId, start, end);
		} else {
			prsList = prsService.listPrsWithParamsAndTime(Constants.FINISH, hospitalId, pageNum, start, end);
		}

		PageInfo<Prescription> page = new PageInfo<Prescription>(prsList);
		request.setAttribute("page", page);
		request.setAttribute("finishPrsList", prsList);

		return "prescription/dimensionPrs";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/printSingleLabel")
	public String printSingleLabelPackage(HttpServletRequest request,
			@RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId,
			@RequestParam(value = "process", defaultValue = "0") int process,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "printType", defaultValue = "PACKAGE") String printType,
			@RequestParam(value = "from", defaultValue = "CURRENT") String from,
			@RequestParam(value = "prsId", defaultValue = "0") int prsId) {

		request.setAttribute("hospitalId", hospitalId);
		request.setAttribute("process", process);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("printType", printType);
		request.setAttribute("from", from);

		if (from.equals("CURRENT")) {
			request.setAttribute("nav", "当前处方列表");
		}

		if (prsId == 0) {
			request.setAttribute("errorMsg", "未知处方无法打印！");
		} else {
			Prescription prs = prsService.getPrsNoUser(prsId);
			PrintHelper.startAndSetup();
			PrintHelper.printPrescription(prs.getPatient_name(), prs.getOuter_id(), prs.getPacket_num(), prs.getSex(),
					prs.getHospital_name(), prs.getUuid(), prs.getCreate_time());
			PrintHelper.close();
			request.setAttribute("successMsg", "打印成功！");
		}

		List<Prescription> prsList = null;

		if (hospitalId == 0 && process == 0) {
			prsList = prsService.listPrsWithProcessUnfinished(pageNum);
		} else if (hospitalId == 0) {
			prsList = prsService.listPrsWithProcess(process, pageNum);
		} else if (process == 0) {
			prsList = prsService.listPrsWithHospital(hospitalId, pageNum);
		} else {
			prsList = prsService.listPrsWithParams(process, hospitalId, pageNum);
		}

		List<Hospital> hospitalList = hospitalService.getHospitalList();
		request.setAttribute("hospitalList", hospitalList);

		request.setAttribute("currentPrsList", prsList);
		PageInfo<Prescription> page = new PageInfo<Prescription>(prsList);
		request.setAttribute("page", page);

		return "prescription/currentPrsList";
	}

	@RequiresRoles(value = { "ADMIN", "RECEIVE" }, logical = Logical.OR)
	@RequestMapping(value = "/printReceiveList")
	public String printReceiveList(HttpServletRequest request,
			@RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId) {
		if (prsService.printReceiveList(hospitalId)) {
			request.setAttribute("successMsg", "打印接方标签成功！");
		} else {
			request.setAttribute("errorMsg", "打印解放标签出错，请重试！");
		}

		return InternalResourceViewResolver.FORWARD_URL_PREFIX + "process/receiveList?hospitalId=" + hospitalId;
	}

	// 打印包装标签
	@RequiresRoles(value = { "ADMIN", "PACKAGE" }, logical = Logical.OR)
	@RequestMapping(value = "/printPackageList")
	public String printPackageList(HttpServletRequest request,
			@RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId) {

		List<Prescription> prsList = null;
		if (hospitalId == 0) {
			prsList = prsService.listPrsWithProcessNoUser(Constants.PACKAGE);
		} else {
			prsList = prsService.listPrsWithProHospitalNoUser(Constants.PACKAGE, hospitalId);
		}

		PrintHelper.startAndSetup();
		for (Prescription prs : prsList) {
			PrintHelper.printPrescription(prs.getPatient_name(), prs.getOuter_id(), prs.getPacket_num(), prs.getSex(),
					prs.getHospital_name(), prs.getUuid(), prs.getCreate_time());
		}
		PrintHelper.close();
		request.setAttribute("successMsg", "打印完成！");

		return InternalResourceViewResolver.FORWARD_URL_PREFIX + "process/packageList?hospitalId=" + hospitalId;
	}

	// 生成出货清单
	@RequiresRoles(value = { "ADMIN", "SHIP" }, logical = Logical.OR)
	@RequestMapping(value = "/printShipListXls")
	public ResponseEntity<byte[]> printShipListXls(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId) throws IOException {
		request.setAttribute("nav", "出库流程列表");

		File file = prsService.printShipListXls(hospitalId);
		if (file == null) {
			return null;
		} else {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		}
	}

}
