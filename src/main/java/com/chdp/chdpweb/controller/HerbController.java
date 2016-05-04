package com.chdp.chdpweb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.chdp.chdpweb.bean.Herb;
import com.chdp.chdpweb.bean.Machine;
import com.chdp.chdpweb.service.HerbService;
import com.chdp.chdpweb.service.MachineService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/herb")
public class HerbController {

	@Autowired
	private HerbService herbService;
	
	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, @RequestParam(name = "pageNum", defaultValue = "1") int pageNum){
		request.setAttribute("nav", "中药管理");
		List<Herb> herbList = herbService.getHerbList(pageNum);
		request.setAttribute("herbList", herbList);
		PageInfo<Herb> page = new PageInfo<Herb>(herbList);
		request.setAttribute("page", page);				
		return "herb/herbList";
	}
	
	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request){
		request.setAttribute("nav", "中药管理");
		return "herb/addHerb";
	}
	
	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, Herb herb){
		request.setAttribute("nav", "中药管理");
		herb.setType(Integer.parseInt(request.getParameter("type")));
		if (herbService.doesHerbExist(herb)){
			request.setAttribute("errorMsg", "相同类型、相同名称的中药已经存在，请调整后添加！");
		}else{
			if (herbService.addHerb(herb)){
				request.setAttribute("successMsg", "添加中药成功！");
			}else{
				request.setAttribute("errorMsg", "添加中药失败，请稍后重试！");
			}
		}
		
		request.setAttribute("herbAdd", herb);
		return "herb/addHerb";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, 
			@RequestParam(name = "herbId") Integer herbId,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum){
		request.setAttribute("nav", "中药管理");
		
		if (herbId == null){
			request.setAttribute("errorMsg", "未知的中药ID！");
		}else{
			if (herbService.deleteHerb(herbId)){
				request.setAttribute("successMsg", "删除中药成功");
			}else{
				request.setAttribute("errorMsg", "删除中药失败，请稍后重试！");
			}
		}
		
		List<Herb> herbList = herbService.getHerbList(pageNum);
		request.setAttribute("herbList", herbList);
		PageInfo<Herb> page = new PageInfo<Herb>(herbList);
		request.setAttribute("page", page);

		return "herb/herbList";
	}
	
}
