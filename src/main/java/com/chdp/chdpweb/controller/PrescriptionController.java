package com.chdp.chdpweb.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.Hospital;
import com.chdp.chdpweb.bean.Order;
import com.chdp.chdpweb.bean.Prescription;
import com.chdp.chdpweb.bean.User;
import com.chdp.chdpweb.common.Utils;
import com.chdp.chdpweb.printer.PrintHelper;
import com.chdp.chdpweb.service.HospitalService;
import com.chdp.chdpweb.service.PrescriptionService;
import com.chdp.chdpweb.service.ProcessService;
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

	@RequiresRoles(value = { "ADMIN", "RECEIVE" }, logical = Logical.OR)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request) {
		request.setAttribute("nav", "接方流程列表");

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
		request.setAttribute("nav", "接方流程列表");

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

		if (prsIdStr == null) {
			request.setAttribute("errorMsg", "未知处方，无法修改！");
			return "errormsg";
		}

		int prsId = Integer.parseInt(prsIdStr);

		Subject currentUser = SecurityUtils.getSubject();

		if (from.equals("RECEIVE") && (!currentUser.hasRole("ADMIN") && !currentUser.hasRole("RECEIVE"))) {
			return "unauthenticated";
		} else if (from.equals("PACKAGE") && (!currentUser.hasRole("ADMIN") && !currentUser.hasRole("PACKAGE"))) {
			return "unauthenticated";
		} else if (from.equals("SHIP") && (!currentUser.hasRole("ADMIN") && !currentUser.hasRole("SHIP"))) {
			return "unauthenticated";
		} else if (from.equals("CURRENT") && !currentUser.hasRole("ADMIN")) {
			return "unauthenticated";
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

		if (prsIdStr == null) {
			request.setAttribute("errorMsg", "未知处方ID，无法修改！");
			return "errormsg";
		}

		int prsId = Integer.parseInt(prsIdStr);

		Subject currentUser = SecurityUtils.getSubject();

		if (from.equals("RECEIVE") && (!currentUser.hasRole("ADMIN") && !currentUser.hasRole("RECEIVE"))) {
			return "unauthenticated";
		} else if (from.equals("PACKAGE") && (!currentUser.hasRole("ADMIN") && !currentUser.hasRole("PACKAGE"))) {
			return "unauthenticated";
		} else if (from.equals("SHIP") && (!currentUser.hasRole("ADMIN") && !currentUser.hasRole("SHIP"))) {
			return "unauthenticated";
		} else if (from.equals("CURRENT") && !currentUser.hasRole("ADMIN")) {
			return "unauthenticated";
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
			if (prsService.validPrescriptionHospitalInfo(prs)) {
				request.setAttribute("errorMsg", "与此处方相同医院名称，相同订单编号的处方已经存在，请检查输入！");
				request.setAttribute("prsModify", prs);
				request.setAttribute("from", from);
				request.setAttribute("prsId", prsId);
				return "prescription/modifyPrs";
			} else {
				prs.setId(prsId);
				if (prsService.updatePrsInReceive(prs)) {
					request.setAttribute("successMsg", "修改处方成功！");
				} else {
					request.setAttribute("errorMsg", "处方修改失败，请稍后重试！");
				}
			}
		}

		request.setAttribute("prsModify", prs);
		request.setAttribute("from", from);
		request.setAttribute("prsId", prsId);

		return "prescription/modifyPrs";
	}

	// 新Delete方法，删除已选择的处方
	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/deletePrsSelected")
	public String deletePrsSelected(HttpServletRequest request,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "hospitalId", defaultValue = "0") int hospitalId,
			@RequestParam(name = "process", defaultValue = "0") int process,
			RedirectAttributes redirectAttributes) {

		String start = request.getParameter("startTime");
		String end = request.getParameter("endTime");

		if (start == null || start.equals("")) {
			start = Utils.getOneMonthAgoTime();
		}
		if (end == null || end.equals("")) {
			end = Utils.getCurrentTime();
		}
		
		String from = request.getParameter("pageFrom");
		if (from == null || from.equals("")) {
			from = "currentList";
		}
		
		String prsListStr = request.getParameter("prsList");
		if (prsListStr == null || prsListStr.equals("")){
			redirectAttributes.addFlashAttribute("errorMsg", "删除处方出错，请重试！");
		}else{
			String[] prsList = prsListStr.split("/");
			if (prsService.deletePrsSelected(prsList)) {
				redirectAttributes.addFlashAttribute("successMsg", "已删除选择的处方！");
			} else {
				request.setAttribute("errorMsg", "删除选择标签出错，请重试！");
			}
		}
        System.out.println(InternalResourceViewResolver.REDIRECT_URL_PREFIX);
		if (from.equals("historyList")) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "../prescription/historyList?hospitalId=" + hospitalId + "&startTime=" + start + "&endTime=" + end + "&pageNum=" + pageNum;
		}
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "../prescription/currentList?hospitalId=" + hospitalId + "&process=" + process + "&pageNum=" + pageNum;
	}

	@RequiresRoles(value = { "ADMIN", "RECEIVE" }, logical = Logical.OR)
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String prsIdStr = request.getParameter("prsId");
		String hospitalIdStr = request.getParameter("hospitalId");

		if (prsIdStr == null) {
			redirectAttributes.addFlashAttribute("errorMsg", "未知处方ID，无法删除！");
		} else {
			int prsId = Integer.parseInt(prsIdStr);
			Prescription prs = prsService.getPrsNoUser(prsId);
			if (prs != null) {
				if (prs.getProcess() != Constants.RECEIVE) {
					redirectAttributes.addFlashAttribute("errorMsg", "处方已经进入流转阶段，无法删除！");
				} else {
					if (prsService.deletePrescription(prs.getId())) {
						proService.deleteProcess(prs.getProcess_id());
						redirectAttributes.addFlashAttribute("successMsg", "删除处方成功！");
					} else {
						redirectAttributes.addFlashAttribute("errorMsg", "删除处方出错，请稍后重试！");
					}
				}
			} else {
				redirectAttributes.addFlashAttribute("errorMsg", "处方不存在，无法删除！");
			}
		}

		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "../process/receiveList?hospitalId=" + hospitalIdStr;
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
			@RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {
		request.setAttribute("nav", "医院维度统计");

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
			request.setAttribute("page", new PageInfo<Hospital>(new ArrayList<Hospital>()));
		} else {
			List<Hospital> returnHospitalList = prsService.getHospitalListByHospitalId(hospitalId, start, end, pageNum);
			Collections.sort(returnHospitalList);
			request.setAttribute("displayHospitalList", returnHospitalList);
			PageInfo<Hospital> page = new PageInfo<Hospital>(returnHospitalList);
			request.setAttribute("page", page);
		}

		return "prescription/hospitalDimension";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/orderDimensionList", method = RequestMethod.GET)
	public String listOrderDimension(HttpServletRequest request,
			@RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum) {
		request.setAttribute("nav", "出库单维度统计");

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
			request.setAttribute("page", new PageInfo<Order>(new ArrayList<Order>()));
		} else {
			List<Order> orderList = prsService.getOrderListByHospitalId(hospitalId, start, end, pageNum);
			Collections.sort(orderList);
			PageInfo<Order> page = new PageInfo<Order>(orderList);
			request.setAttribute("page", page);
			request.setAttribute("currentOrderList", orderList);
		}

		return "prescription/orderDimension";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/userDimensionList", method = RequestMethod.GET)
	public String listUserDimension(HttpServletRequest request,
			@RequestParam(value = "userAuth", defaultValue = "0") int userAuth,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {
		request.setAttribute("nav", "员工维度统计");

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
			request.setAttribute("page", new PageInfo<User>(new ArrayList<User>()));
		} else {
			List<User> userList = prsService.getUserListForPrsSummary(userAuth, start, end, pageNum);
			Collections.sort(userList);
			request.setAttribute("returnUserList", userList);
			PageInfo<User> page = new PageInfo<User>(userList);
			request.setAttribute("page", page);
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
		} else if (from.equals("ORDER") && orderId == 0) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/orderDimensionList";
		} else if (from.equals("CURRENT_ORDER") && orderId == 0) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "order/currentOrders";
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
			} else if (from.equals("ORDER")) {
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/orderDimensionList";
			} else if (from.equals("CURRENT_ORDER")) {
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "order/currentOrders";
			} else {
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/currentList";
			}
		}

		List<Prescription> prsList = null;

		if (from.equals("HOSPITAL")) {
			prsList = prsService.listPrsWithParamsAndTime(Constants.FINISH, hospitalId, pageNum, start, end);
		} else if (from.equals("USER")) {
			prsList = prsService.getPrsListByUserId(userId, start, end, pageNum);
		} else if (from.equals("ORDER")) {
			prsList = prsService.getPrsListByOrderId(orderId, start, end, pageNum);
		} else if (from.equals("CURRENT_ORDER")) {
			prsList = prsService.getPrsListByOrderIdInProcess(orderId, pageNum);
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
			if (printType.equals("PACKAGE"))
				PrintHelper.printPackage(prs.getPatient_name(), prs.getOuter_id(), prs.getPacket_num(), prs.getSex(),
						prs.getHospital_name(), prs.getUuid(), prs.getCreate_time());
			else
				PrintHelper.printPrs(prs.getPatient_name(), prs.getOuter_id(), prs.getPacket_num(), prs.getSex(),
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
			@RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId,
			RedirectAttributes redirectAttributes) {
		
		String prsListStr = request.getParameter("prsList");
		if (prsListStr == null || prsListStr.equals("")){
			redirectAttributes.addFlashAttribute("errorMsg", "打印接方标签出错，请重试！");
		}else{
			String[] prsList = prsListStr.split("/");
			if (prsService.printReceiveList(prsList)) {
				redirectAttributes.addFlashAttribute("successMsg", "打印接方标签成功！");
			} else {
				redirectAttributes.addFlashAttribute("errorMsg", "打印接方标签出错，请重试！");
			}
		}

		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "../process/receiveList?hospitalId=" + hospitalId;
	}

	// 打印包装标签
	@RequiresRoles(value = { "ADMIN", "PACKAGE" }, logical = Logical.OR)
	@RequestMapping(value = "/printPackageList")
	public String printPackageList(HttpServletRequest request,
			@RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId,
			RedirectAttributes redirectAttributes) {
		
		String prsListStr = request.getParameter("prsList");
		if (prsListStr == null || prsListStr.equals("")){
			redirectAttributes.addFlashAttribute("errorMsg", "打印接方标签出错，请重试！");
		}else{
			String[] prsStrList = prsListStr.split("/");
			List<Prescription> prsList = prsService.getPrsListByIds(prsStrList);

			PrintHelper.startAndSetup();
			for (Prescription prs : prsList) {
				PrintHelper.printPackage(prs.getPatient_name(), prs.getOuter_id(), prs.getPacket_num(), prs.getSex(),
						prs.getHospital_name(), prs.getUuid(), prs.getCreate_time());
			}
			PrintHelper.close();
			redirectAttributes.addFlashAttribute("successMsg", "打印包装标签完成！");
		}
		
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "../process/packageList?hospitalId=" + hospitalId;
	}

	// 生成出库清单
	@RequiresRoles(value = { "ADMIN", "SHIP" }, logical = Logical.OR)
	@RequestMapping(value = "/printShipListXls")
	public String printShipListXls(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId,
			RedirectAttributes redirectAttributes) throws IOException {
		// String filename = prsService.printShipListXls(hospitalId);
		String filename = prsService.printShipListXls_New(hospitalId);
		if (filename != null) {
			redirectAttributes.addFlashAttribute("successMsg", "出库单生成成功！ <a class='btn btn-primary' href='"
					+ request.getContextPath() + "/shipFile/" + filename + "' target='_blank'>请点击此处下载</a>");
		} else {
			redirectAttributes.addFlashAttribute("errorMsg", "出库单生成成功出错，请重试！");
		}

		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "../process/shipList?hospitalId=" + hospitalId;
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/printHospitalDimensionXls")
	public String printHospitalDimensionXls(HttpServletRequest request, HttpServletResponse resposne,
			@RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId) throws IOException {
		String start = request.getParameter("startTime");
		String end = request.getParameter("endTime");
		if (start == null || start.equals("")) {
			start = Utils.getOneMonthAgoTime();
		}
		if (end == null || end.equals("")) {
			end = Utils.getCurrentTime();
		}
		String startTime = Utils.formatStartTime(start);
		String endTime = Utils.formatEndTime(end);

		if (!Utils.validStartEndTime(startTime, endTime)) {
			request.setAttribute("errorMsg", "您输入的时间间隔有误，请重新输入!");
		} else {
			List<Hospital> hospitalList = prsService.getHospitalListByHospitalId(hospitalId, startTime, endTime);
			if (hospitalList.size() != 0) {
				Collections.sort(hospitalList);
				String filename = prsService.generateHospitalDimensionXls(hospitalList, start, end);
				if (filename != null) {
					return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "../tmpFile/"
							+ URLEncoder.encode(filename, "UTF-8");
				} else {
					request.setAttribute("errorMsg", "医院统计清单导出出错，请重试！");
				}
			} else {
				request.setAttribute("errorMsg", "尚无可打印统计信息！");
			}
		}

		return "errormsg";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/printUserDimensionXls")
	public String printUserDimensionXls(HttpServletRequest request, HttpServletResponse resposne,
			@RequestParam(value = "userAuth", defaultValue = "0") int userAuth) throws IOException {
		String start = request.getParameter("startTime");
		String end = request.getParameter("endTime");
		if (start == null || start.equals("")) {
			start = Utils.getOneMonthAgoTime();
		}
		if (end == null || end.equals("")) {
			end = Utils.getCurrentTime();
		}
		String startTime = Utils.formatStartTime(start);
		String endTime = Utils.formatEndTime(end);

		if (!Utils.validStartEndTime(startTime, endTime)) {
			request.setAttribute("errorMsg", "您输入的时间间隔有误，请重新输入!");
		} else {
			List<User> userList = prsService.getUserListForPrsSummary(userAuth, startTime, endTime);

			if (userList.size() != 0) {
				Collections.sort(userList);
				String filename = prsService.generateUserDimensionXls(userList, start, end);
				if (filename != null) {
					return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "../tmpFile/"
							+ URLEncoder.encode(filename, "UTF-8");
				} else {
					request.setAttribute("errorMsg", "用户统计清单导出出错，请重试！");
				}
			} else {
				request.setAttribute("errorMsg", "尚无可打印统计信息！");
			}
		}

		return "errormsg";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/printOrderDimensionXls")
	public String printOrderDimensionXls(HttpServletRequest request, HttpServletResponse resposne,
			@RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId) throws IOException {
		String start = request.getParameter("startTime");
		String end = request.getParameter("endTime");
		if (start == null || start.equals("")) {
			start = Utils.getOneMonthAgoTime();
		}
		if (end == null || end.equals("")) {
			end = Utils.getCurrentTime();
		}
		String startTime = Utils.formatStartTime(start);
		String endTime = Utils.formatEndTime(end);

		if (!Utils.validStartEndTime(startTime, endTime)) {
			request.setAttribute("errorMsg", "您输入的时间间隔有误，请重新输入!");
		} else {
			List<Order> orderList = prsService.getOrderListByHospitalId(hospitalId, startTime, endTime);
			if (orderList.size() != 0) {
				Collections.sort(orderList);
				String filename = prsService.generateOrderDimensionXls(orderList, start, end);
				if (filename != null) {
					return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "../tmpFile/"
							+ URLEncoder.encode(filename, "UTF-8");
				} else {
					request.setAttribute("errorMsg", "出库单统计清单导出出错，请重试！");
				}
			} else {
				request.setAttribute("errorMsg", "尚无可打印统计信息！");
			}
		}

		return "errormsg";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/regenerateShipListXls")
	public String regenerateShipListXls(HttpServletRequest request, HttpServletResponse resposne,
			@RequestParam(value = "orderId", defaultValue = "0") int orderId,
			@RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId) throws IOException {
		String start = request.getParameter("startTime");
		String end = request.getParameter("endTime");
		if (start == null || start.equals("")) {
			start = Utils.getOneMonthAgoTime();
		}
		if (end == null || end.equals("")) {
			end = Utils.getCurrentTime();
		}
		String startTime = Utils.formatStartTime(start);
		String endTime = Utils.formatEndTime(end);

		if (orderId == 0) {
			request.setAttribute("errorMsg", "未知出库单，无法打印!");
		} else if (!Utils.validStartEndTime(startTime, endTime)) {
			request.setAttribute("errorMsg", "您输入的时间间隔有误，请重新输入!");
		} else {
			String filename = prsService.regenerateShipListXls(orderId, start, end);
			if (filename != null) {
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "../shipFile/"
						+ URLEncoder.encode(filename, "UTF-8");
			} else {
				request.setAttribute("errorMsg", "导出出库单失败，请重试！");
			}
		}

		return "errormsg";
	}
}
