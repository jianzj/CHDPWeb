package com.chdp.chdpweb.controller;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chdp.chdpweb.service.HospitalService;
import com.chdp.chdpweb.service.PrescriptionService;
import com.chdp.chdpweb.service.ProcessService;
import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.Prescription;
import com.chdp.chdpweb.bean.User;
import com.chdp.chdpweb.bean.Process;

@Controller
@RequestMapping("/prescription")
public class PrescriptionController {

	@Autowired
	private PrescriptionService prsService;
	@Autowired
	private HospitalService hospitalService;
	@Autowired
	private ProcessService proService;
	
	@RequiresRoles("RECEIVE")
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request){
		Prescription lastestPrs = prsService.getLastestPrs();
		if (lastestPrs != null){
			request.setAttribute("lastestPrs", lastestPrs);
		}
		return "prescription/addPrescription";
	}
	
	@RequiresRoles("RECEIVE")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, Prescription prs){
		
		String hospitalName = request.getParameter("hospital_name");
		if (hospitalService.getHospitalIdByName(hospitalName) <= 0) {
			request.setAttribute("errorMsg", "您输入的医院不存在，请添加后重新录入！");
			return "hospital/addHospital";
		}
		prs.setHospital_id(hospitalService.getHospitalIdByName(hospitalName));
		
		if (prsService.validPrescriptionHospitalInfo(prs)){
			request.setAttribute("errorMsg", "与此处方相同医院名称，相同订单编号的处方已经存在，请检查输入！");
		}else{
			prs.setUuid(UUID.randomUUID().toString());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = df.format(new Date());
			prs.setCreate_time(currentTime);
			
			// Set these values as default.
			prs.setProcess(Constants.RECEIVE);
			
			if(prsService.createPrescription(prs)){
				Prescription newPrs = prsService.getPrescriptionByUuid(prs.getUuid());
				User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
				Process newProcess = new Process();
				newProcess.setProcess_type(Constants.RECEIVE);
				newProcess.setUser_id(currentUser.getId());
				newProcess.setPrescription_id(newPrs.getId());
				newProcess.setBegin(currentTime);
				newProcess.setPrevious_process_id(0);
				if (proService.createProccess(newProcess) != -1){
					int newProId = proService.getProcessIdwithProcess(newProcess);
					newPrs.setProcess_id(newProId);
					if (prsService.updatePrescriptionProcess(newPrs)){
						request.setAttribute("successMsg", "添加处方成功！");
					}else{
						request.setAttribute("errorMsg", "添加处方失败，请稍后重试！");
						proService.deleteProcess(newProId);
						prsService.deletePrescription(newPrs.getId());
					}
				}else{
					request.setAttribute("errorMsg", "添加处方失败，请稍后重试！");
					prsService.deletePrescription(newPrs.getId());
				}			
			}else{
				request.setAttribute("errorMsg", "添加处方失败，请稍后重试！");
			}			
		}
				
		request.setAttribute("prsAdd", prs);
		return "prescription/addPrescription";
	}

	@RequiresRoles("RECEIVE")
	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public String modify(HttpServletRequest request, @Param("prsId") Integer prsId){
		
		if (prsId == null){
			request.setAttribute("errorMsg", "未知处方ID，无法修改！");
		}else{
			request.setAttribute("prsModify", prsService.getPrescription(prsId));
		}
		
		request.setAttribute("modifyPrsFrom", "receiveList");
		return "prescription/modifyPrescription";
	}
	
	@RequiresRoles("RECEIVE")
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public void modifyPost(HttpServletRequest request, HttpServletResponse response, @Param("prsId") Integer prsId) throws IOException{
		
		if (prsId == null){
			request.setAttribute("errorMsg", "未知处方ID，无法修改");
		}else{
			Prescription prs = prsService.getPrescription(prsId);
			String price = request.getParameter("price");
			double formatPrice = prsService.formatPricewithPrs(prs, price);
			prs.setPrice(formatPrice);
			
			String patient_name = request.getParameter("patient_name");
			int packet_num = Integer.parseInt(request.getParameter("packet_num"));
			if (prs.getPacket_num() != packet_num){
				prs.setPacket_num(packet_num);
			}
			
			String hospitalName = request.getParameter("hospital_name");
			String outer_id = request.getParameter("outer_id");
			
			boolean hospitalInfoChanged = false;
			if (prs.getHospital_name() != hospitalName || prs.getOuter_id() != outer_id){
				hospitalInfoChanged = true;
			}
			prs.setPatient_name(patient_name);
			if (hospitalInfoChanged){
				prs.setHospital_name(hospitalName);
				prs.setOuter_id(outer_id);
				if (hospitalService.getHospitalIdByName(hospitalName) <= 0 || prsService.validPrescriptionHospitalInfo(prs)){
					request.setAttribute("errorMsg", "您输入的医院名称或医院编号有误，请校验后重新输入");
				}else{
					if(prsService.updatePrsInReceive(prs)){
						request.setAttribute("successMsg", "修改处方成功！");
					}else{
						request.setAttribute("errorMsg", "处方修改失败，请稍后重试！");
					}
				}
			}else{
				if(prsService.updatePrsInReceive(prs)){
					request.setAttribute("successMsg", "修改处方成功！");
				}else{
					request.setAttribute("errorMsg", "处方修改失败，请稍后重试！");
				}
			}
		}
		
		if (request.getAttribute("modifyPrsFrom") != null && request.getAttribute("modifyPrsFrom") == "receiveList"){
			response.sendRedirect(request.getContextPath() + "/process/receiveList");
		}else{
			response.sendRedirect(request.getContextPath() + "/process/receiveList");
		}
		
	}
	
	@RequiresRoles("RECEIVE")
	@RequestMapping(value = "/delete")
	public void delete(HttpServletRequest request, HttpServletResponse response, @Param("prsId") Integer prsId,@Param("process") Integer process) throws IOException{
		
		if (prsId == null || process == null){
			request.setAttribute("errorMsg", "未知处方ID，或处方处于未知状态，无法删除！");
		}else{
			Prescription prs = prsService.getPrescription(prsId);
			if (prs != null){
				if (prs.getProcess() != process){
					request.setAttribute("errorMsg", "处方已经处于流转阶段，无法删除！");
				}else{
					if (prsService.deletePrescription(prs.getId())){
						List<Process> processList = proService.getProcessListwithPrsID(prs.getId());
						Iterator<Process> iter = processList.iterator();
						while (iter.hasNext()){
							Process item = iter.next();
							proService.deleteProcess(item.getId());
						}
						request.setAttribute("successMsg", "删除处方成功！");
					}else{
						request.setAttribute("errorMsg", "删除处方出错，请稍后重试！");
					}
				}
			}else{
				request.setAttribute("errorMsg", "处方不存在，无法删除！");
			}
		}

		response.sendRedirect(request.getContextPath() + "/process/receiveList");
	}

}
