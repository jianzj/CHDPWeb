package com.chdp.chdpweb.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.chdp.chdpweb.bean.Hospital;
import com.chdp.chdpweb.bean.Order;
import com.chdp.chdpweb.service.HospitalService;
import com.chdp.chdpweb.service.OrderService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private HospitalService hospitalService;
	
	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/currentOrders", method = RequestMethod.GET)
	public String currentOrders(HttpServletRequest request, @RequestParam(name = "hospitalId", defaultValue = "0") int hospitalId,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum){
		
		request.setAttribute("nav", "当前出库单列表");
		request.setAttribute("hospitalId", hospitalId);
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		request.setAttribute("hospitalList", hospitalList);
		
		List<Order> orderList = orderService.getOrderListByHospitalId(hospitalId, pageNum);
		Collections.sort(orderList);
		PageInfo<Order> page = new PageInfo<Order>(orderList);
		request.setAttribute("page", page);
		request.setAttribute("currentOrderList", orderList);
		
		return "order/currentOrderList";
	}
	
}
