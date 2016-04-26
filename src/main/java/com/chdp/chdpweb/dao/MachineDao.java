package com.chdp.chdpweb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.chdp.chdpweb.bean.Machine;

public interface MachineDao {
	
	@Insert("insert machine(type, name, description) values(#{machine.type}, #{machine.name}, #{machine.description})")
	int createMachine(@Param("machine") Machine machine);
	
	@Select("select * from machines where type = #{type}")
	List<Machine> getMachinesByType(@Param("type") int type);
	
	@Select("select * from machine")
	List<Machine> getMachines();
	
}
