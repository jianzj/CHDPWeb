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
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.Hospital;
import com.chdp.chdpweb.bean.Order;
import com.chdp.chdpweb.bean.Prescription;
import com.chdp.chdpweb.service.HospitalService;
import com.chdp.chdpweb.service.OrderService;
import com.chdp.chdpweb.service.PrescriptionService;
import com.chdp.chdpweb.service.ProcessService;
import com.chdp.chdpweb.bean.Process;

@Controller
@RequestMapping("/process")
public class ProcessController {

	@Autowired
	private ProcessService proService;
	@Autowired
	private PrescriptionService prsService;
	@Autowired
	private HospitalService hospitalService;
	@Autowired
	private OrderService orderService;
	
	@RequiresRoles(value = {"ADMIN", "RECEIVE"}, logical = Logical.OR)
	@RequestMapping(value = "/receiveList", method = RequestMethod.GET)
	public String listPrsInReceiving(HttpServletRequest request, @RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId){

		List<Prescription> prsList = null;
		if (hospitalId == 0){
			prsList = prsService.listPrsWithProcessNoUser(Constants.RECEIVE);
		}else{
			prsList = prsService.listPrsWithProHospitalNoUser(Constants.RECEIVE, hospitalId);
		}
		
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		
		request.setAttribute("hospitalList", hospitalList);
		request.setAttribute("hospitalId", hospitalId);
		request.setAttribute("receiveList", prsList);
	 	return "process/receiveList";
	 }
	
	@RequiresRoles(value = {"ADMIN", "PACKAGE"}, logical = Logical.OR)
	@RequestMapping(value = "/packageList", method = RequestMethod.GET)
	public String listPrsInPackaging(HttpServletRequest request, @RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId){

		
		List<Prescription> prsList = null;
		if (hospitalId == 0){
			prsList = prsService.listPrsWithProcessNoUser(Constants.PACKAGE);
		}else{
			prsList = prsService.listPrsWithProHospitalNoUser(Constants.PACKAGE, hospitalId);
		}
		
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		
		request.setAttribute("hospitalList", hospitalList);
		request.setAttribute("hospitalId", hospitalId);
		request.setAttribute("packageList", prsList);
	 	return "process/packageList";
	 }

	@RequiresRoles(value = {"ADMIN", "SHIP"}, logical = Logical.OR)
	@RequestMapping(value = "/shipList", method = RequestMethod.GET)
	public String listPrsInShipping(HttpServletRequest request, @RequestParam(value = "hospitalId", defaultValue = "0") int hospitalId){

		List<Prescription> prsList = null;
		if (hospitalId == 0){
			prsList = prsService.listPrsWithProcessNoUser(Constants.SHIP);
		}else{
			prsList = prsService.listPrsWithProHospitalNoUser(Constants.SHIP, hospitalId);
		}
		
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		
		request.setAttribute("hospitalList", hospitalList);
		request.setAttribute("hospitalId", hospitalId);
		request.setAttribute("shipList", prsList);
	 	return "process/shipList";
	 }
	
	@RequiresRoles(value = "ADMIN")
	@RequestMapping(value = "/showAllProcs", method = RequestMethod.GET)
	public String showAllProcesses(HttpServletRequest request, @RequestParam(value = "prsId", defaultValue = "0") int prsId, @RequestParam(value = "from", defaultValue = "CURRENT") String from){

		if (prsId == 0){
			request.setAttribute("errorMsg", "未知处方ID！");
			if (from.equals("CURRENT")){
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/currentList";
			}else if (from.equals("HISTORY")){
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/historyList";
			}else if (from.equals("HOSPITAL")){
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/hospitalDimensionListt";
			}else if (from.equals("USER")){
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/userDimensionList";
			}else if (from.equals("ORDER")){
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/orderDimensionList";
			}
		}
		request.setAttribute("from", from);
		
		Prescription prs = prsService.getPrsNoUser(prsId);
		int processId = 0;
		int orderId = -1;
		if (prs.getProcess() < Constants.SHIP){
			processId = prs.getProcess_id();
		}else {
			processId = prsService.getLastestProcIdwithPrsandProcess(prs.getId(), Constants.SHIP);
			orderId = prs.getProcess_id();
		}
		
		List<Process> processList = proService.getProcessChainWithProcessId(processId);
		request.setAttribute("processList", processList);
		request.setAttribute("currentPrs", prs);
		
		if (orderId > 0){
			Order order = orderService.getOrder(orderId);
			request.setAttribute("shipOrder", order);
		}

		return "process/processDetail";
	}
}
