package com.chdp.chdpweb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.chdp.chdpweb.bean.Order;

public interface OrderDao {

	@Insert("insert order(uuid, hospital_id, create_time) values(#{order.uuid}, #{order.hospital_id}, #{order.create_time})")
	int createOrder(@Param("order") Order order);

	@Insert("insert CHDP.order(uuid, hospital_id, create_time, create_user_id, status) " +
	           "values(#{order.uuid}, #{order.hospital_id}, #{order.create_time}, #{order.create_user_id}, #{order.status})")
	int createFullOrder(@Param("order") Order order);
	
	@Select("select id from CHDP.order where uuid = #{orderUuid}")
	int getOrderIdByUuid(@Param("orderUuid") String orderUuid);
	
	@Update("update order set create_time = #{order.create_time}, create_user_id = #{order.create_user_id} " +
	           "where uuid = #{order.uuid}")
	int updateCreateAction(@Param("order") Order order);
	
	@Update("update order set outbound_time = #{order.outbound_time}, outbound_user_id = #{order.outbound_user_id} " +
	          "where id = #{order.id}")
	int updateOutboundAction(@Param("order") Order order);
	
	@Update("update order set status = #{order.status} where uuid = #{order.uuid}")
	int updateOrderStatus(@Param("order") Order order);

	@Select("select o.*, h.name as hospital_name from CHDP.order as o, hospital as h where o.id = #{id} and o.hospital_id = h.id")
    Order getOrderById(@Param("id") int id);
	
	@Select("select o.*, h.name as hospital_name from CHDP.order as o, hospital as h where o.uuid = #{uuid} and o.hospital_id = h.id")
    Order getOrderByUuid(@Param("uuid") String uuid);

	@Select("select o.*, h.name as hospital_name from CHDP.order as o, hospital as h where o.status = #{order_status} and " +
				"h.name = #{hospital} and o.hospital_id = h.id and o.create_time > #{start} and o.create_time < #{end}")
	List<Order> listOrder(@Param("hospital")String hospital, @Param("start") String start, @Param("end") String end, @Param("order_status") int order_status);
	
	@Select("select o.*, h.name as hospital_name from CHDP.order as o, hospital as h where o.status = #{order_status} and " +
				"o.hospital_id = h.id and o.create_time > #{start} and o.create_time < #{end}")
	List<Order> listOrderAllHospital(@Param("start") String start, @Param("end") String end, @Param("order_status") int order_status);
	
	@Select("select count(*) from CHDP.order as o, prescription as p where o.id = #{orderId} and p.process = 11 and o.status = 2 and p.process_id = o.id")
	int countPrsNumInOrder(@Param("orderId") int orderId);
	
	@Select("select count(*) from CHDP.order as o, prescription as p where o.id = #{orderId} and p.process_id = o.id")
	int countPrsNumWithOrder(@Param("orderId") int orderId);
}
