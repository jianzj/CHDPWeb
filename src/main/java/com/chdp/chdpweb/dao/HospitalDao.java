package com.chdp.chdpweb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.Hospital;

@Repository
public interface HospitalDao {

	@Select("select * from hospital")
	List<Hospital> getHospitalList();
	
	@Insert("insert hospital(name, description) values(#{hospital.name}, #{hospital.description})")
	int createHospital(@Param("hospital") Hospital hospital);
	
	@Delete("delete from hospital where id = #{id}")
	int deleteHospital(@Param("id") int id);
	
	// Use to decide if one hospital could be removed.
	@Select("select count(*) from hospital as h, prescription as p where " +
	           "h.id = #{hospitalId} and p.hospital_id = h.id and p.process < 11")
	int countPrescriptionsWithHospital(@Param("hospitalId") int hospitalId);

	@Select("select id from hospital where name = #{name}")
	int getHospitalIdwithName(@Param("name") String name);	
	
	@Delete("delete from hospital where name = #{name}")
	int deleteHospitalwithName(@Param("name") String name);
	
	@Select("select * from hospital where id = #{id}")
	Hospital getHospitalwithID(@Param("id") int id);
	
	@Select("select * from hospital where name = #{name}")
	Hospital getHospitalwithName(@Param("name") String name);
	
	@Select("select h.id as id, h.name as name, h.description as description from hospital as h, prescription as p " +
	        "where h.id = p.hospital_id")
	Hospital getHospitalwithAutoNext();
	
	@Select("select count(o.id) from CHDP.order as o where o.status = " + Constants.ORDER_FINISH + " and "
				+ "o.hospital_id = #{hospitalId} and o.create_time >= #{startTime} and o.outbound_time <= #{endTime}")
	int getOrderNumByHospitalId(@Param("hospitalId") int hospitalId, @Param("startTime") String startTime, @Param("endTime") String endTime);
}
