package com.chdp.chdpweb.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.chdp.chdpweb.service.HospitalService;
import com.chdp.chdpweb.service.OrderService;
import com.chdp.chdpweb.service.PrescriptionService;
import com.chdp.chdpweb.service.ProcessService;
import com.chdp.chdpweb.service.UserService;
import com.github.pagehelper.PageInfo;
import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.Hospital;
import com.chdp.chdpweb.bean.Order;
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
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	
	@RequiresRoles("RECEIVE")
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request){
		Prescription lastestPrs = prsService.getLastestPrs();
		if (lastestPrs != null){
			request.setAttribute("lastestPrs", lastestPrs);
		}
		
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		if (hospitalList.size() > 0){
			request.setAttribute("hospitalList", hospitalList);
		}
		return "prescription/addPrescription";
	}
	
	@RequiresRoles("RECEIVE")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, Prescription prs){

		String price = request.getParameter("prs_price");
		double priceNum = 0;
		try{
			priceNum = Double.parseDouble(price);
		}catch(Exception e){
			request.setAttribute("errorMsg", "您输入的价格格式不正确，请重新输入！");
			List<Hospital> hospitalList = hospitalService.getHospitalList();
			request.setAttribute("hospitalList", hospitalList);
			
			request.setAttribute("prsAdd", prs);
			return "prescription/addPrescription";
		}
		
		prs.setPrice(priceNum);
		String hospitalName = request.getParameter("hospital_name");
		if (hospitalService.getHospitalIdByName(hospitalName) <= 0) {
			request.setAttribute("errorMsg", "您输入的医院不存在，请联系管理员！");
			List<Hospital> hospitalList = hospitalService.getHospitalList();
			request.setAttribute("hospitalList", hospitalList);
			
			request.setAttribute("prsAdd", prs);
			return "prescription/addPrescription";
		}
		prs.setHospital_id(hospitalService.getHospitalIdByName(hospitalName));
		prs.setHospital_name(hospitalName);
		
		if (prsService.validPrescriptionHospitalInfo(prs)){
			request.setAttribute("errorMsg", "与此处方相同医院名称，相同订单编号的处方已经存在，请检查输入！");
		}else{
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String currentTime = df.format(new Date());
			prs.setCreate_time(currentTime);
			
			//生成UUID，形如20160502213800386, 当前日期+三位随机数
			int randomNum = (int)(Math.random()*900)+100;
			String uuid = currentTime + String.valueOf(randomNum);
			prs.setUuid(uuid);
			
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
				int result = proService.createProccess(newProcess);
				if (result != -1){
					newPrs.setProcess_id(newProcess.getId());
					if (prsService.updatePrescriptionProcess(newPrs)){
						request.setAttribute("successMsg", "添加处方成功！");
					}else{
						request.setAttribute("errorMsg", "添加处方失败，请稍后重试！");
						proService.deleteProcess(newProcess.getId());
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
	
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		request.setAttribute("hospitalList", hospitalList);
		
		request.setAttribute("prsAdd", prs);
		return "prescription/addPrescription";
	}

	@RequiresRoles(value = {"ADMIN", "RECEIVE", "PACKAGE", "SHIP"}, logical = Logical.OR)
	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public  String modify(HttpServletRequest request, @Param("prsId") Integer prsId, @Param("from") String from){
		
		if (from == null){
			from = "RECEIVE";
		}

		List<Hospital> hospitalList = hospitalService.getHospitalList();
		request.setAttribute("hospitalList", hospitalList);
		request.setAttribute("hospital", "ALL");
		List<Prescription> prsList = null;
		
		if (prsId == null){
			request.setAttribute("errorMsg", "未知处方ID，无法修改！"); 
			if (from.equals("RECEIVE")){
				prsList = prsService.listPrsWithProcessNoUser(Constants.RECEIVE);
				request.setAttribute("receiveList", prsList);
				return "process/receiveList";
			}else if(from.equals("PACKAGE")){
				prsList = prsService.listPrsWithProcessNoUser(Constants.PACKAGE);
				request.setAttribute("packageList", prsList);
				return "process/packageList";
			}else if(from.equals("SHIP")){
				prsList = prsService.listPrsWithProcessNoUser(Constants.SHIP);
				request.setAttribute("shipList", prsList);
				return "process/shipList";
			}else if(from.equals("CURRENT")){
				prsList = prsService.listPrsWithProcessUnfinished(1);
				request.setAttribute("hospital", "ALL");
				request.setAttribute("process", 0);
				request.setAttribute("currentPrsList", prsList);
				PageInfo<Prescription> page = new PageInfo<Prescription>(prsList);
				request.setAttribute("page", page);	
				return "prescription/currentPrsList";
			}
		}
		
		User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
		if (from.equals("RECEIVE") && ((currentUser.getAuthority()&1024) == 0 && (currentUser.getAuthority()&512) == 0)){
			request.setAttribute("errorMsg", "您暂无此操作权限！");
			prsList = prsService.listPrsWithProcessNoUser(Constants.RECEIVE);
			request.setAttribute("receiveList", prsList);
			return "process/receiveList";
		}else if(from.equals("PACKAGE") && ((currentUser.getAuthority()&1024) == 0 && (currentUser.getAuthority()&2) == 0)){
			request.setAttribute("errorMsg", "您暂无此操作权限！");
			prsList = prsService.listPrsWithProcessNoUser(Constants.PACKAGE);
			request.setAttribute("packageList", prsList);
			return "process/packageList";
		}else if(from.equals("SHIP") && ((currentUser.getAuthority()&1024) == 0 && (currentUser.getAuthority()&1) == 0)){
			request.setAttribute("errorMsg", "您暂无此操作权限！");
			prsList = prsService.listPrsWithProcessNoUser(Constants.SHIP);
			request.setAttribute("shipList", prsList);
			return "process/shipList";
		}else if(from.equals("CURRENT") && ((currentUser.getAuthority()&1024) == 0)){
			request.setAttribute("errorMsg", "您暂无此操作权限！");
			prsList = prsService.listPrsWithProcessUnfinished(1);
			request.setAttribute("hospital", "ALL");
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

	@RequiresRoles(value = {"ADMIN", "RECEIVE", "PACKAGE", "SHIP"}, logical = Logical.OR)
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modifyPost(HttpServletRequest request, @Param("prsId") Integer prsId, @Param("from") String from, @Param("prs") Prescription prs){
		
		if (from == null){
			from = "RECEIVE";
		}
		
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		request.setAttribute("hospitalList", hospitalList);
		request.setAttribute("hospital", "ALL");
		List<Prescription> prsList = null;
		
		if (prsId == null){
			request.setAttribute("errorMsg", "未知处方ID，无法修改！"); 
			if (from.equals("RECEIVE")){
				prsList = prsService.listPrsWithProcessNoUser(Constants.RECEIVE);
				request.setAttribute("receiveList", prsList);
				return "process/receiveList";
			}else if(from.equals("PACKAGE")){
				prsList = prsService.listPrsWithProcessNoUser(Constants.PACKAGE);
				request.setAttribute("packageList", prsList);
				return "process/packageList";
			}else if(from.equals("SHIP")){
				prsList = prsService.listPrsWithProcessNoUser(Constants.SHIP);
				request.setAttribute("shipList", prsList);
				return "process/shipList";
			}else if(from.equals("ALL")){
				prsList = prsService.listPrsWithProcessUnfinished(1);
				request.setAttribute("process", 0);
				request.setAttribute("currentPrsList", prsList);
				PageInfo<Prescription> page = new PageInfo<Prescription>(prsList);
				request.setAttribute("page", page);	
				return "prescription/currentPrsList";
			}
		}
		
		User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
		if (from.equals("RECEIVE") && ((currentUser.getAuthority()&1024) == 0 && (currentUser.getAuthority()&512) == 0)){
			request.setAttribute("errorMsg", "您暂无此操作权限！");
			prsList = prsService.listPrsWithProcessNoUser(Constants.RECEIVE);
			request.setAttribute("receiveList", prsList);
			return "process/receiveList";
		}else if(from.equals("PACKAGE") && ((currentUser.getAuthority()&1024) == 0 && (currentUser.getAuthority()&2) == 0)){
			request.setAttribute("errorMsg", "您暂无此操作权限！");
			prsList = prsService.listPrsWithProcessNoUser(Constants.PACKAGE);
			request.setAttribute("packageList", prsList);
			return "process/packageList";
		}else if(from.equals("SHIP") && ((currentUser.getAuthority()&1024) == 0 && (currentUser.getAuthority()&1) == 0)){
			request.setAttribute("errorMsg", "您暂无此操作权限！");
			prsList = prsService.listPrsWithProcessNoUser(Constants.SHIP);
			request.setAttribute("shipList", prsList);
			return "process/shipList";
		}else if(from.equals("CURRENT") && ((currentUser.getAuthority()&1024) == 0)){
			request.setAttribute("errorMsg", "您暂无此操作权限！");
			prsList = prsService.listPrsWithProcessUnfinished(1);
			request.setAttribute("process", 0);
			request.setAttribute("currentPrsList", prsList);
			PageInfo<Prescription> page = new PageInfo<Prescription>(prsList);
			request.setAttribute("page", page);	
			return "prescription/currentPrsList";
		}
		
		Prescription currentPrs = prsService.getPrsNoUser(prsId);
		
		String price = request.getParameter("price_num");
		double priceNum = 0;
		try{
			priceNum = Double.parseDouble(price);
		}catch (Exception e){
			request.setAttribute("errorMsg", "您输入的价格格式不正确，请重新输入！");
			request.setAttribute("prsModify", prs);
			request.setAttribute("from", from);
			request.setAttribute("prsId", prsId);
			return "prescription/modifyPrs";
		}
		
		prs.setPrice(priceNum);		
		if (prsService.equalTwoPrescription(currentPrs, prs)){
			request.setAttribute("errorMsg", "您并未作出任何修改");
			request.setAttribute("prsModify", prs);
			request.setAttribute("from", from);
			request.setAttribute("prsId", prsId);
			return "prescription/modifyPrs";
		}else{
			if((!prsService.equalHospitalInfo(currentPrs, prs)) && prsService.validPrescriptionHospitalInfo(prs)){
				request.setAttribute("errorMsg", "您输入的医院名称已经存在，请校验后重新输入！");
				request.setAttribute("prsModify", prs);
				request.setAttribute("from", from);
				request.setAttribute("prsId", prsId);
				return "prescription/modifyPrs";
			}else{
				prs.setId(prsId);
				prs.setHospital_id(hospitalService.getHospitalIdByName(prs.getHospital_name()));
				if(prsService.updatePrsInReceive(prs)){
					request.setAttribute("successMsg", "修改处方成功！");
				}else{
					request.setAttribute("errorMsg", "处方修改失败，请稍后重试！");
				}
			}
		}
		
		
		if (from.equals("RECEIVE")){
			prsList = prsService.listPrsWithProcessNoUser(Constants.RECEIVE);
			request.setAttribute("receiveList", prsList);
			return "process/receiveList";
		}else if(from.equals("PACKAGE")){
			prsList = prsService.listPrsWithProcessNoUser(Constants.PACKAGE);
			request.setAttribute("packageList", prsList);
			return "process/packageList";
		}else if(from.equals("SHIP")){
			prsList = prsService.listPrsWithProcessNoUser(Constants.SHIP);
			request.setAttribute("shipList", prsList);
			return "process/shipList";
		}else if (from.equals("CURRENT")){
			prsList = prsService.listPrsWithProcessUnfinished(1);
			request.setAttribute("process", 0);
			request.setAttribute("currentPrsList", prsList);
			PageInfo<Prescription> page = new PageInfo<Prescription>(prsList);
			request.setAttribute("page", page);	
			return "prescription/currentPrsList";
		}
		return "process/receiveList";
	}
	
	@RequiresRoles("RECEIVE")
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, @Param("prsId") Integer prsId, @Param("hospital") String hospital){
		
		if (hospital == null){
			hospital = "ALL";
		}
		
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		request.setAttribute("hospitalList", hospitalList);
		request.setAttribute("hospital", hospital);
		List<Prescription> prsList = null;
		
		if (prsId == null){
			request.setAttribute("errorMsg", "未知处方ID，无法删除！");
		}else{
			Prescription prs = prsService.getPrsNoUser(prsId);
			if (prs != null){
				if (prs.getProcess() != Constants.RECEIVE){
					request.setAttribute("errorMsg", "处方已经进入流转阶段，无法删除！");
				}else{
					if (prsService.deletePrescription(prs.getId())){
						proService.deleteProcess(prs.getProcess_id());
						request.setAttribute("successMsg", "删除处方成功！");
					}else{
						request.setAttribute("errorMsg", "删除处方出错，请稍后重试！");
					}
				}
			}else{
				request.setAttribute("errorMsg", "处方不存在，无法删除！");
			}
		}
		
		if (hospital.equals("ALL")){
			prsList = prsService.listPrsWithProcessNoUser(Constants.RECEIVE);
		}else{
			prsList = prsService.listPrsWithProHospitalNoUser(Constants.RECEIVE, hospital);
		}
		request.setAttribute("receiveList", prsList);
		return "process/receiveList";
	}
	
	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/currentList", method = RequestMethod.GET)
	public String getCurrentList(HttpServletRequest request, @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
				@Param("hospital") String hospital, @Param("process") Integer process){
		
		request.setAttribute("nav", "当前处方列表");
		
		if (hospital == null || hospital == ""){
			hospital = "ALL";
		}
		if (process == null){
			process = 0;
		}
		
		List<Prescription> prsList = null;
		
		if (hospital.equals("ALL") && process == 0){
			prsList = prsService.listPrsWithProcessUnfinished(pageNum);
		}else if (hospital.equals("ALL")){
			prsList = prsService.listPrsWithProcess(process, pageNum);
		}else if (process == 0){
			prsList = prsService.listPrsWithHospital(hospital, pageNum);
		}else{
			prsList = prsService.listPrsWithParams(process, hospital, pageNum);
		}
		
		request.setAttribute("hospital", hospital);
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
	public String getHistoryList(HttpServletRequest request, @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
				@Param("hospital") String hospital, @Param("startTime") String start, @Param("endTime") String end){
		
		request.setAttribute("nav", "历史处方列表");
		
		if (hospital == null || hospital == ""){
			hospital = "ALL";
		}
		if (start == null || start == ""){
			start = Constants.DEFAULT_START;
		}
		if (end == null || end == ""){
			end = Constants.DEFAULT_END;
		}

		List<Prescription> prsList = null;
		if(hospital.equals("ALL")){
			prsList = prsService.listPrsWithProcessAndTime(Constants.FINISH, pageNum, start, end);
		}else{
			prsList = prsService.listPrsWithParamsAndTime(Constants.FINISH, hospital, pageNum, start, end);
		}
		
		request.setAttribute("hospital", hospital);
		request.setAttribute("startTime", start);
		request.setAttribute("endTime", end);

		List<Hospital> hospitalList = hospitalService.getHospitalList();
		request.setAttribute("hospitalList", hospitalList);
		
		request.setAttribute("historyPrsList", prsList);
		PageInfo<Prescription> page = new PageInfo<Prescription>(prsList);
		request.setAttribute("page", page);	
	
        return "prescription/historyPrsList";
	}
	
	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/hospitalDimensionList", method = RequestMethod.GET)
	public String listHospitalDimension(HttpServletRequest request, @Param("hospital") String hospital, @Param("startTime") String start, @Param("endTime") String end){
		
		// Initial params if empty;
		if (hospital == null || hospital == ""){
			hospital = "ALL";
		}
		if (start == null || start == ""){
			start = Constants.DEFAULT_START;
		}
		if (end == null || end == ""){
			end = Constants.DEFAULT_END;
		}
		request.setAttribute("hospital", hospital);
		request.setAttribute("startTime", start);
		request.setAttribute("endTime", end);
		
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		List<Hospital> newHospList = new ArrayList<Hospital>();
		Iterator<Hospital> itr = hospitalList.iterator();
		if (hospital.equals("ALL")){
			int num = 0;
			while (itr.hasNext()){
				Hospital temp = itr.next();
				num = prsService.countPrsNumForHospital(temp.getName(), Constants.FINISH, start, end);
				temp.setFinishedPrsNum(num);
				newHospList.add(temp);
			}
		}else{
			Hospital item = hospitalService.getHospitalByName(hospital);
			if (item != null){
				int num = prsService.countPrsNumForHospital(item.getName(), Constants.FINISH, start, end);
				item.setFinishedPrsNum(num);
				newHospList.add(item);
			}
		}
		
		request.setAttribute("hospitalList", hospitalList);
		Collections.sort(newHospList);
		request.setAttribute("displayHospitalList", newHospList);
		return "prescription/hospitalDimension";
	}
	
	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/orderDimensionList", method = RequestMethod.GET)
	public String listOrderDimension(HttpServletRequest request, @Param("hospital") String hospital, @Param("startTime") String start, @Param("endTime") String end,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum){
		
		request.setAttribute("nav", "出货单维度统计");
		
		// Initial params if empty;
		if (hospital == null || hospital == ""){
			hospital = "ALL";
		}
		if (start == null || start == ""){
			start = Constants.DEFAULT_START;
		}
		if (end == null || end == ""){
			end = Constants.DEFAULT_END;
		}
		request.setAttribute("hospital", hospital);
		request.setAttribute("startTime", start);
		request.setAttribute("endTime", end);
		
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		
		List<Order> orderList = orderService.listOrderFinished(hospital, start, end, pageNum);
		List<Order> finalOrderList = new ArrayList<Order>();
		
		Iterator<Order> itr = orderList.iterator();
		Order item = null;
		while (itr.hasNext()){
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
	public String listUserDimension(HttpServletRequest request, @Param("userAuth") Integer userAuth, @Param("startTime") String start, @Param("endTime") String end){
		
		// Initial params if empty;
		if (userAuth == null || userAuth < 0){
			userAuth = 0;
		}
		if (start == null || start == ""){
			start = Constants.DEFAULT_START;
		}
		if (end == null || end == ""){
			end = Constants.DEFAULT_END;
		}
		request.setAttribute("userAuth", userAuth);
		request.setAttribute("startTime", start);
		request.setAttribute("endTime", end);
		
		List<User> userList = userService.getUserListNoAdmin();
		Iterator<User> itr = userList.iterator();
		List<User> finalUserList = new ArrayList<User>();

		User item = null;
		while (itr.hasNext()){
			item = itr.next();
			if (userAuth != 0 && (item.getAuthority()&userAuth) == 0){
				continue;
			}
			int process_type = proService.getProcessTypebyUserAuth(item.getAuthority());
			int prsNum = prsService.countDealPrsByUser(item.getId(), process_type, start, end);
			item.setDone_prs_num(prsNum);
			int errorNum = prsService.countErrorProByUser(item.getId(), start, end);
			item.setError_num(errorNum);
			String position = userService.getPositionwithAuthority(item);
			item.setPosition(position);
			finalUserList.add(item);
		}
		Collections.sort(finalUserList);
		request.setAttribute("finalUserList", finalUserList);
		return "prescription/userDimension";
	}
	
	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/dimensionPrsList", method = RequestMethod.GET)
	public String dimensionPrsList(HttpServletRequest request, @Param("hospital") String hospital, @Param("startTime") String start, @Param("endTime") String end,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum, @Param("from") String from, @Param("userId") Integer userId){
		
		request.setAttribute("nav", "处方汇总列表");
		
		if (from == null || from == ""){
			from = "HOSPITAL";
		}
		
		if (hospital == null || hospital == "" || hospital.equals("ALL") || userId == null || userId <=0 ){
			if (from.equals("HOSPITAL")){
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/hospitalDimensionList";
			}else if(from.equals("USER")){
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/userDimensionList";
			}
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/hospitalDimensionList";
		}
		
		if (start == null || start == ""){
			start = Constants.DEFAULT_START;
		}
		if (end == null || end == ""){
			end = Constants.DEFAULT_END;
		}
		
		request.setAttribute("startTime", start);
		request.setAttribute("endTime", end);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("hospital", hospital);
		request.setAttribute("userId", userId);
		request.setAttribute("from", from);
		
		List<Prescription> prsList = null;
		if (from.equals("HOSPITAL")){
			prsList = prsService.listPrsWithParamsAndTime(Constants.FINISH, hospital, pageNum, start, end);
		}else if (from.equals("USER")) {
			User user = userService.getUserById(userId);
			int process_type = proService.getProcessTypebyUserAuth(user.getAuthority());
			prsList = prsService.listPrswithUser(user.getId(), process_type, start, end);
		}else{
			prsList = prsService.listPrsWithParamsAndTime(Constants.FINISH, hospital, pageNum, start, end);
		}
		
		PageInfo<Prescription> page = new PageInfo<Prescription>(prsList);
		request.setAttribute("page", page);	
		request.setAttribute("finishPrsList", prsList);
	
		return "prescription/dimensionPrs";		
	}
	
	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/printSingleLabel")
	public String printSingleLabelPackage(HttpServletRequest request, @Param("hospital") String hospital, @Param("process") Integer process,
											@Param("pageNum") Integer pageNum, @Param("printType") String printType, @Param("from") String from, @Param("prsId") Integer prsId){
		if (hospital == null){
			hospital = "ALL";
		}
		if (process == null){
			process = 0;
		}
		request.setAttribute("hospital", hospital);
		request.setAttribute("process", process);
		
		if (pageNum == null){
			pageNum = 1;
		}
		request.setAttribute("pageNum", pageNum);
		
		if (printType == null){
			printType = "PACKAGE";
		}
		request.setAttribute("printType", printType);
		
		if (from == null){
			from = "CURRENT";
		}
		request.setAttribute("from", from);
		
		if (from.equals("CURRENT")){
			request.setAttribute("nav", "当前处方列表");
		}
		
		if (prsId == null){
			request.setAttribute("errorMsg", "未知处方无法打印！");
		}else{
			Prescription prs = prsService.getPrsNoUser(prsId);
			if (printType.equals("PACKAGE")){
				if(prsService.printSinglePackage(prs.getUuid() )){
					request.setAttribute("successMsg", "打印成功！");
				}else{
					request.setAttribute("errorMsg", "打印失败，请稍后重试！");
				}
			}else if(printType.equals("PRS")){
				if(prsService.printSinglePrs(prs.getUuid() )){
					request.setAttribute("successMsg", "打印成功！");
				}else{
					request.setAttribute("errorMsg", "打印失败，请稍后重试！");
				}
			}
		}
				
		List<Prescription> prsList = null;
		
		if (hospital.equals("ALL") && process == 0){
			prsList = prsService.listPrsWithProcessUnfinished(pageNum);
		}else if (hospital.equals("ALL")){
			prsList = prsService.listPrsWithProcess(process, pageNum);
		}else if (process == 0){
			prsList = prsService.listPrsWithHospital(hospital, pageNum);
		}else{
			prsList = prsService.listPrsWithParams(process, hospital, pageNum);
		}
		
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		request.setAttribute("hospitalList", hospitalList);
		
		request.setAttribute("currentPrsList", prsList);
		PageInfo<Prescription> page = new PageInfo<Prescription>(prsList);
		request.setAttribute("page", page);	
	
		if (from.equals("CURRENT")){
			return "prescription/currentPrsList";
		}
        return "prescription/currentPrsList";
		
	}
	
	@RequiresRoles("RECEIVE")
	@RequestMapping(value = "/printReceiveList")
	public String printReceiveList(HttpServletRequest request, @Param("hospital") String hospital){
		
		if (hospital == null){
			hospital = "ALL";
		}
		
		List<Prescription> prsList = null;
		if (hospital.equals("ALL")){
			prsList = prsService.listPrsWithProcessNoUser(Constants.RECEIVE);
		}else{
			prsList = prsService.listPrsWithProHospitalNoUser(Constants.RECEIVE, hospital);
		}
		
		Iterator<Prescription> itr = prsList.iterator();
		Prescription printItem = null;
		int count = 0;
		while (itr.hasNext()){
			printItem = itr.next();
			if (prsService.printReceiveLabel(printItem.getUuid())){
				
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				String currentTime = df.format(new Date());
				User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
				
				Process newProcess = new Process();
				Process currentProcess = proService.getProcessById(printItem.getProcess_id());
				newProcess.setProcess_type(Constants.CHECK);
				newProcess.setUser_id(currentUser.getId());
				newProcess.setPrescription_id(printItem.getId());
				newProcess.setBegin(currentTime);
				newProcess.setPrevious_process_id(printItem.getProcess_id());
				int result = proService.createProccess(newProcess);
				if (result != -1){
					currentProcess.setFinish(currentTime);
					if (proService.updateProcessTime(currentProcess)){
						printItem.setProcess(Constants.CHECK);
						printItem.setProcess_id(newProcess.getId());
						if(prsService.updatePrescriptionProcess(printItem)){
							count += 1;
						}else{
							request.setAttribute("errorMsg", "打印到处方" + String.valueOf(printItem.getId()) + "出错，请重新打印");
							break;
						}
					}else{
						request.setAttribute("errorMsg", "打印到处方" + String.valueOf(printItem.getId()) + "出错，请重新打印");
						break;
					}
				}else{
					request.setAttribute("errorMsg", "打印到处方" + String.valueOf(printItem.getId()) + "出错，请重新打印");
					break;
				}

			}else{
				request.setAttribute("errorMsg", "打印到处方" + String.valueOf(printItem.getId()) + "出错，请重新打印");
				break;
			}
		}
		
		if (count == prsList.size()){
			request.setAttribute("successMsg", "打印完成！");
		}else{
			request.setAttribute("errorMsg", "打印中存在错误，漏打了" + String.valueOf(prsList.size() - count) + "份处方标签");
		}
		
		/**
		if (hospital.equals("ALL")){
			prsList = prsService.listPrsWithProcessNoUser(Constants.RECEIVE);
		}else{
			prsList = prsService.listPrsWithProHospitalNoUser(Constants.RECEIVE, hospital);
		} **/
		prsList = prsService.listPrsWithProcessNoUser(Constants.RECEIVE);
		
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		
		request.setAttribute("hospitalList", hospitalList);
		//request.setAttribute("hospital", hospital);
		request.setAttribute("hospital", "ALL");
		request.setAttribute("receiveList", prsList);
		
		return "process/receiveList";
	}
	
	//打印包装标签
	@RequiresRoles("PACKAGE")
	@RequestMapping(value = "/printPackageList")
	public String printPackageList(HttpServletRequest request, @Param("hospital") String hospital){
		
		if (hospital == null){
			hospital = "ALL";
		}
		
		List<Prescription> prsList = null;
		if (hospital.equals("ALL")){
			prsList = prsService.listPrsWithProcessNoUser(Constants.PACKAGE);
		}else{
			prsList = prsService.listPrsWithProHospitalNoUser(Constants.PACKAGE, hospital);
		}
		
		Iterator<Prescription> itr = prsList.iterator();
		Prescription printItem = null;
		int count = 0;
		while (itr.hasNext()){
			printItem = itr.next();
			if (prsService.printPackageLabel(printItem.getUuid())){
				
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				String currentTime = df.format(new Date());
				
				
				/** Old logic, will be removed.
				 * 
				User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
				Process newProcess = new Process();
				Process currentProcess = proService.getProcessById(printItem.getId());
				newProcess.setProcess_type(Constants.SHIP);
				newProcess.setUser_id(currentUser.getId());
				newProcess.setPrescription_id(printItem.getId());
				newProcess.setBegin(currentTime);
				newProcess.setPrevious_process_id(printItem.getProcess_id());
				if (proService.createProccess(newProcess) != -1){
					int newProId = proService.getProcessIdwithProcess(newProcess);
					currentProcess.setFinish(currentTime);
					if (proService.updateProcessTime(currentProcess)){
						printItem.setProcess(Constants.SHIP);
						printItem.setProcess_id(newProId);
						if(prsService.updatePrescriptionProcess(printItem)){
							count += 1;
						}else{
							request.setAttribute("errorMsg", "打印到处方" + String.valueOf(printItem.getId()) + "出错，请重新打印");
							break;
						}
					}else{
						request.setAttribute("errorMsg", "打印到处方" + String.valueOf(printItem.getId()) + "出错，请重新打印");
						break;
					}
				}else{
					request.setAttribute("errorMsg", "打印到处方" + String.valueOf(printItem.getId()) + "出错，请重新打印");
					break;
				} **/
				printItem.setProcess(Constants.SHIP);
				printItem.setProcess_id(-1);
				if(prsService.updatePrescriptionProcess(printItem)){
					Process currentProcess = proService.getProcessById(printItem.getId());
					currentProcess.setFinish(currentTime);
					if (proService.updateProcessTime(currentProcess)){
						count += 1;
					}else{
						request.setAttribute("errorMsg", "打印到处方" + String.valueOf(printItem.getId()) + "出错，请重新打印");
						break;
					}
				}else{
					request.setAttribute("errorMsg", "打印到处方" + String.valueOf(printItem.getId()) + "出错，请重新打印");
					break;
				}
			}else{
				request.setAttribute("errorMsg", "打印到处方" + String.valueOf(printItem.getId()) + "出错，请重新打印");
				break;
			}
		}
		
		if (count == prsList.size()){
			request.setAttribute("successMsg", "打印完成！");
		}else{
			request.setAttribute("errorMsg", "打印中存在错误，漏打了" + String.valueOf(prsList.size() - count) + "份处方标签");
		}
		
		/**
		if (hospital.equals("ALL")){
			prsList = prsService.listPrsWithProcessNoUser(Constants.RECEIVE);
		}else{
			prsList = prsService.listPrsWithProHospitalNoUser(Constants.RECEIVE, hospital);
		} **/
		prsList = prsService.listPrsWithProcessNoUser(Constants.PACKAGE);
		
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		
		request.setAttribute("hospitalList", hospitalList);
		//request.setAttribute("hospital", hospital);
		request.setAttribute("hospital", "ALL");
		request.setAttribute("packageList", prsList);
		
		return "process/packageList";
	}

	//生成出货清单
	@RequiresRoles("PACKAGE")
	@RequestMapping(value = "/printShipListXls")
	public String printShipListXls(HttpServletRequest request, @Param("hospital") String hospital){
		
		if (hospital == null){
			hospital = "ALL";
		}
				
		List<String> usedHospitals = new ArrayList<String>();
		if (hospital.equals("ALL")){
			usedHospitals = prsService.listInProgressHospitalwithProcess(Constants.SHIP);
		}else{
			usedHospitals.add(hospital);
		}
		
		List<Prescription> prs = null;
		String hospitalName = "";
		int count = 0;
		
		Iterator<String> itr = usedHospitals.iterator();
		User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
		
		while(itr.hasNext()){
			hospitalName = itr.next();
			prs = prsService.listPrsWithProHospitalNoUser(Constants.SHIP, hospitalName);
			if (prsService.generatePrsListXls(hospitalName,currentUser, prs)){
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				String currentTime = df.format(new Date());
				
				//生成UUID，形如20160502213800386, 当前日期+三位随机数
				int randomNum = (int)(Math.random()*900)+100;
				String uuid = currentTime + String.valueOf(randomNum);
				Order newOrder = new Order();
				newOrder.setUuid(uuid);
				newOrder.setHospital_id(hospitalService.getHospitalIdByName(hospitalName));
				newOrder.setCreate_time(currentTime);
				newOrder.setCreate_user_id(currentUser.getId());
				newOrder.setStatus(Constants.ORDER_BEGIN);
				if (orderService.createFullOrder(newOrder)){
					newOrder.setId(orderService.getOrderIdByUuid(newOrder.getUuid()));
					
					Iterator<Prescription> prsItems = prs.iterator();
					Prescription item = null;
					while (prsItems.hasNext()){
						item = prsItems.next();
						item.setProcess_id(newOrder.getId());
						prsService.updatePrescriptionProcess(item);
					}
				}else{
					request.setAttribute("errorMsg", "打印 " + hospitalName + " 的出货单出错，请重新打印！");
					break;
				}
				count += 1;
			}else{
				request.setAttribute("errorMsg", "打印 " + hospitalName + " 的出货单出错，请重新打印！");
				break;
			}
		}
		
		
		if (count == usedHospitals.size()){
			request.setAttribute("successMsg", "打印完成！");
		}
		/**else{
			request.setAttribute("errorMsg", "打印中存在错误，漏打了" + String.valueOf(usedHospitals.size() - count) + "张出货单");
		}**/
		
		
		List<Prescription> prsList = prsService.listPrsWithProcessNoUser(Constants.SHIP);
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		
		request.setAttribute("hospitalList", hospitalList);
		//request.setAttribute("hospital", hospital);
		request.setAttribute("hospital", "ALL");
		request.setAttribute("shipList", prsList);
		
		return "process/shipList";
	}
	
}
