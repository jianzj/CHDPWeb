package com.chdp.chdpweb.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.AppResult;
import com.chdp.chdpweb.bean.Order;
import com.chdp.chdpweb.bean.Prescription;
import com.chdp.chdpweb.bean.User;
import com.chdp.chdpweb.common.Utils;
import com.chdp.chdpweb.dao.OrderDao;
import com.chdp.chdpweb.dao.PrescriptionDao;
import com.chdp.chdpweb.dao.UserDao;
import com.github.pagehelper.PageHelper;

@Repository
public class OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private DataSourceTransactionManager transactionManager;
    
    @Autowired
    private PrescriptionDao prsDao;

    public Order getOrder(int id) {
        try {
            return orderDao.getOrderById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public Order getOrderByUuid(String uuid) {
        try {
            return orderDao.getOrderByUuid(uuid);
        } catch (Exception e) {
            return null;
        }
    }
    
    public AppResult finishOrder(int orderId) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        AppResult result = new AppResult();
        try {
            User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
            Order order = new Order();
            order.setId(orderId);
            order.setOutbound_time(Utils.getCurrentDateAndTime());
            order.setOutbound_user_id(user.getId());
            order.setStatus(Constants.ORDER_FINISH);
            orderDao.updateOutboundAction(order);

            try {
                prsDao.finishPrescription(orderId, Utils.getCurrentDateAndTime());
            } catch (Exception e) {
            	transactionManager.rollback(status);
                result.setErrorMsg("更新处方信息失败");
                result.setSuccess(false);

                return result;
            }
        } catch (Exception e) {
        	transactionManager.rollback(status);
            result.setErrorMsg("更新出库单失败");
            result.setSuccess(false);

            return result;
        }

        transactionManager.commit(status);
        result.setSuccess(true);
        return result;
    } 
    
    public boolean createFullOrder(Order newOrder){
    	try{
    		orderDao.createFullOrder(newOrder);
    		return true;
    	}catch (Exception e){
    		return false;
    	}
    }
    
    public int getOrderIdByUuid(String uuid){
    	try{
    		return orderDao.getOrderIdByUuid(uuid);
    	} catch (Exception e){
    		return -1;
    	}
    }
    
    public List<Order> listOrderFinished(int hospitalId, String start, String end){
    	try{
    		if (hospitalId == 0){
    			return orderDao.listOrderAllHospital(start, end, Constants.ORDER_FINISH);
    		}else{
    			return orderDao.listOrder(hospitalId, start, end, Constants.ORDER_FINISH);
    		}
    	}catch (Exception e){
    		return new ArrayList<Order>();
    	}
    }
    
    public List<Order> listOrderFinished(int hospitalId, String start, String end, int pageNum){
    	PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
    	try{
    		if (hospitalId == 0){
    			return orderDao.listOrderAllHospital(start, end, Constants.ORDER_FINISH);
    		}else{
    			return orderDao.listOrder(hospitalId, start, end, Constants.ORDER_FINISH);
    		}
    	}catch (Exception e){
    		return new ArrayList<Order>();
    	}
    }
    
    public int countPrsNumInOrder(int orderId){
    	try{
    		return orderDao.countPrsNumWithOrder(orderId);
    	} catch (Exception e){
    		return 0;
    	}
    }
    
    /** ------------------------------------------------------------------**/
    //获取所有当前正在进行的出库单
    public List<Order> getOrderListByHospitalId(int hospitalId){
    	try{
    		List<Order> orderList = new ArrayList<Order>();
    		if (hospitalId == 0){
    			orderList = orderDao.getOrderListUnfinished();
    		}else {
    			orderList = orderDao.getOrderListByHospitalIdUnfinished(hospitalId);
    		}
    		List<Prescription> prsList = null;
			for (Order order : orderList) {
				prsList = prsDao.getPrsListByOrderIdUnfinished(order.getId());
				order.setPrs_num(prsList.size());
				int packet_num = 0;
				double price_total = 0;
				for (Prescription item : prsList) {
					packet_num += item.getPacket_num();
					price_total += item.getPrice();
				}
				order.setPacket_num(packet_num);
				order.setPrice_total(price_total);
				order.setCreate_user_name(userDao.getUserById(order.getCreate_user_id()).getName());
			}
    		return orderList;
    	} catch (Exception e){
    		return new ArrayList<Order>();
    	}
    }
    
    //获取所有当前正在进行的出库单
    public List<Order> getOrderListByHospitalId(int hospitalId, int pageNum){
    	PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
    	try{
    		List<Order> orderList = new ArrayList<Order>();
    		if (hospitalId == 0){
    			orderList = orderDao.getOrderListUnfinished();
    		}else {
    			orderList = orderDao.getOrderListByHospitalIdUnfinished(hospitalId);
    		}
    		List<Prescription> prsList = null;
			for (Order order : orderList) {
				prsList = prsDao.getPrsListByOrderIdUnfinished(order.getId());
				order.setPrs_num(prsList.size());
				int packet_num = 0;
				double price_total = 0;
				for (Prescription item : prsList) {
					packet_num += item.getPacket_num();
					price_total += item.getPrice();
				}
				order.setPacket_num(packet_num);
				order.setPrice_total(price_total);
				order.setCreate_user_name(userDao.getUserById(order.getCreate_user_id()).getName());
			}
    		return orderList;
    	} catch (Exception e){
    		return new ArrayList<Order>();
    	}
    }
}
