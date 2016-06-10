package com.chdp.chdpweb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.Hospital;
import com.chdp.chdpweb.bean.Node;
import com.chdp.chdpweb.bean.Prescription;
import com.chdp.chdpweb.service.HospitalService;
import com.chdp.chdpweb.service.PrescriptionService;
import com.chdp.chdpweb.service.ProcessService;

@Controller
@RequestMapping("/process")
public class ProcessController {

	@Autowired
	private ProcessService proService;
	@Autowired
	private PrescriptionService prsService;
	@Autowired
	private HospitalService hospitalService;

	@RequiresRoles(value = { "ADMIN", "RECEIVE" }, logical = Logical.OR)
	@RequestMapping(value = "/receiveList")
	public String listPrsInReceiving(HttpServletRequest request,
			@RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId) {
		request.setAttribute("nav", "接方流程列表");

		List<Prescription> prsList = null;
		if (hospitalId == 0) {
			prsList = prsService.listPrsWithProcessNoUser(Constants.RECEIVE);
		} else {
			prsList = prsService.listPrsWithProHospitalNoUser(Constants.RECEIVE, hospitalId);
		}

		List<Hospital> hospitalList = hospitalService.getHospitalList();

		request.setAttribute("hospitalList", hospitalList);
		request.setAttribute("hospitalId", hospitalId);
		request.setAttribute("receiveList", prsList);

		return "process/receiveList";
	}

	@RequiresRoles(value = { "ADMIN", "PACKAGE" }, logical = Logical.OR)
	@RequestMapping(value = "/packageList")
	public String listPrsInPackaging(HttpServletRequest request,
			@RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId) {
		request.setAttribute("nav", "包装流程列表");

		List<Prescription> prsList = null;
		if (hospitalId == 0) {
			prsList = prsService.listPackagePrs();
		} else {
			prsList = prsService.listPackagePrsWithHospital(hospitalId);
		}

		List<Hospital> hospitalList = hospitalService.getHospitalList();

		request.setAttribute("hospitalList", hospitalList);
		request.setAttribute("hospitalId", hospitalId);
		request.setAttribute("packageList", prsList);
		return "process/packageList";
	}

	@RequiresRoles(value = { "ADMIN", "SHIP" }, logical = Logical.OR)
	@RequestMapping(value = "/shipList")
	public String listPrsInShipping(HttpServletRequest request,
			@RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId) {
		request.setAttribute("nav", "出库流程列表");
		
		if (hospitalId == 0){
			hospitalId = hospitalService.getDefaultHospitalId();
			
		}
		request.setAttribute("hospitalId", hospitalId);
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		request.setAttribute("hospitalList", hospitalList);
		
		List<Prescription> prsList = null;
		if (hospitalId != 0){
			// prsList = prsService.listShipPrescription(Constants.SHIP, hospitalId);
			prsList = prsService.getPrsForPrintOrderListUnprinted(hospitalId);
		}
		
		request.setAttribute("shipList", prsList);

		return "process/shipList";
	}

	@RequiresRoles(value = "ADMIN")
	@RequestMapping(value = "/showAllProcs", method = RequestMethod.GET)
	public String showAllProcesses(HttpServletRequest request,
			@RequestParam(value = "prsId", defaultValue = "0") int prsId,
			@RequestParam(value = "from", defaultValue = "CURRENT") String from,
			RedirectAttributes redirectAttributes) {
		if (prsId == 0) {
			redirectAttributes.addFlashAttribute("errorMsg", "未知处方ID！");
			if (from.equals("CURRENT")) {
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/currentList";
			} else if (from.equals("HISTORY")) {
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/historyList";
			} else if (from.equals("HOSPITAL")) {
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/hospitalDimensionListt";
			} else if (from.equals("USER")) {
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/userDimensionList";
			} else if (from.equals("ORDER")) {
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/orderDimensionList";
			} else if (from.equals("CURRENT_ORDER")){
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "order/currentOrders";
			} else {
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/currentList";
			}
		}

		Prescription prs = prsService.getPrsNoUser(prsId);
		List<Node> nodeList = proService.getPrsWorkFlowNods(prs);
		request.setAttribute("nodeList", nodeList);
		request.setAttribute("currentPrs", prs);

		return "process/processDetail";
	}
}
