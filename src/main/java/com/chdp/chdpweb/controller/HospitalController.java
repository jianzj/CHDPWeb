package com.chdp.chdpweb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.chdp.chdpweb.bean.Hospital;
import com.chdp.chdpweb.service.HospitalService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/hospital")
public class HospitalController {

	@Autowired
	private HospitalService hospitalService;

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, @RequestParam(name = "pageNum", defaultValue = "1") int pageNum) {
		request.setAttribute("nav", "医院管理");

		List<Hospital> hospitalList = hospitalService.getHospitalList(pageNum);
		request.setAttribute("hospitalList", hospitalList);

		PageInfo<Hospital> page = new PageInfo<Hospital>(hospitalList);
		request.setAttribute("page", page);

		return "hospital/hospitalList";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request) {
		request.setAttribute("nav", "医院管理");
		return "hospital/addHospital";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, Hospital hospital) {
		request.setAttribute("nav", "医院管理");
		if (hospitalService.doesHospitalExist(hospital)) {
			request.setAttribute("errorMsg", "此医院名已经存在，请调整后重新输入！");
		} else {
			if (hospitalService.addHospital(hospital)) {
				request.setAttribute("successMsg", "添加医院成功！");
			} else {
				request.setAttribute("errorMsg", "添加医院失败，请稍后重试！");
			}
		}
		request.setAttribute("hospitalAdd", hospital);
		return "hospital/addHospital";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, @RequestParam(name = "hospitalId") Integer hospitalId,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum, RedirectAttributes redirectAttributes) {
		if (hospitalId == null) {
			redirectAttributes.addFlashAttribute("errorMsg", "未知的医院ID！");
		} else {
			if (hospitalService.isHospitalInUse(hospitalId)) {
				redirectAttributes.addFlashAttribute("errorMsg", "尚有未完成处方，不能删除！");
			} else {
				if (hospitalService.deleteHospital(hospitalId)) {
					redirectAttributes.addFlashAttribute("successMsg", "删除医院成功！");
				} else {
					redirectAttributes.addFlashAttribute("errorMsg", "删除医院失败，请稍后重试！");
				}
			}
		}

		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "../hospital/list?pageNum=" + pageNum;
	}

}
