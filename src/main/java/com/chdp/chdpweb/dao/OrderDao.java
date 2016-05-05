package com.chdp.chdpweb.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.chdp.chdpweb.bean.Order;

public interface OrderDao {

	@Insert("insert order(uuid, hospital_id, create_time) values(#{order.uuid}, #{order.hospital_id}, #{order.create_time})")
	int createOrder(@Param("order") Order order);
	
	@Update("update order set create_time = #{order.create_time}, create_user_id = #{order.create_user_id} " +
	           "where uuid = #{order.uuid}")
	int updateCreateAction(@Param("order") Order order);
	
	@Update("update order set outbound_time = #{order.outbound_time}, outbound_user_id = #{order.outbound_user_id} " +
	          "where id = #{order.id}")
	int updateOutboundAction(@Param("order") Order order);
	
	@Update("update order set status = #{order.status} where uuid = #{order.uuid}")
	int updateOrderStatus(@Param("order") Order order);

	@Select("select o.*, h.name as hospital_name from order as o, hospital as h where o.id = #{id} and o.hospital_id = h.id")
    Order getOrderById(@Param("id") int id);
	
	@Select("select o.*, h.name as hospital_name from order as o, hospital as h where o.uuid = #{uuid} and o.hospital_id = h.id")
    Order getOrderByUuid(@Param("uuid") String uuid);

}
