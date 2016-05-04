package com.chdp.chdpweb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
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
	
	@Select("select * from machine where name = #{name}")
	Machine getMachineWithName(@Param("name") String name);
	
	@Delete("delete from machine where id = #{id}")
	int deleteMachine(@Param("id") int id);
	
	@Select("select count(*) from machine where name = #{machine.name} and type = #{machine.type}")
	int doesMachineExist(@Param("machine") Machine machine);
	
	@Select("select count(*) from machine as m, process as p where m.id = #{machineId} and p.machine_id = m.id and p.process_type < 11")
	int isMachineInUse(@Param("machineId") int machineId);
}
