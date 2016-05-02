package com.chdp.chdpweb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chdp.chdpweb.bean.Herb;
import com.chdp.chdpweb.service.HerbService;

@Controller
@RequestMapping("/app/herb")
public class HerbAppController {

	@Autowired
	private HerbService herbService;

	@RequestMapping(value = "/getHerbByType", method = RequestMethod.POST)
	@ResponseBody
	public List<Herb> getHerbByType(HttpServletRequest request) {
		int type = Integer.parseInt(request.getParameter("type"));
		List<Herb> herbList = herbService.getHerbListByType(type);
		return herbList;
	}

	@RequestMapping(value = "/getHerbs", method = RequestMethod.GET)
	@ResponseBody
	public List<Herb> getHerbs() {
		List<Herb> herbList = herbService.getHerbs();
		return herbList;
	}
}
