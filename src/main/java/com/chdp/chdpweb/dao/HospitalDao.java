package com.chdp.chdpweb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.chdp.chdpweb.bean.Hospital;

@Repository
public interface HospitalDao {
	
	@Insert("insert into hospital(name, description) values(#{name}, #{description})")
	int createHospital(Hospital hospital);
	
	@Delete("delete from hospital where name = #{name}")
	int deleteHospital(@Param("name") String name);
	
	@Select("select * from hospital where name = #{name}")
	Hospital getHospitalwithName(@Param("name") String name);
	
	@Select("select * from hostpital where id = #{id}")
	Hospital getHospitalwithID(@Param("id") int id);
	
	@Select("select * from hospital")
	List<Hospital> getHospitalList();
	
	@Select("select h.id as id, h.name as name, h.description as description from hospital as h, prescription as p " +
	        "where h.id = p.hospital_id")
	Hospital getHospitalwithAutoNext();
}
