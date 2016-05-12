package com.chdp.chdpweb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.chdp.chdpweb.bean.Machine;

public interface MachineDao {
	@Insert("insert machine(uuid, type, pour_machine_id, name, description) values(#{machine.uuid}, #{machine.type}, #{machine.pour_machine_id}, #{machine.name}, #{machine.description})")
	int createMachine(@Param("machine") Machine machine);

	@Select("select * from machine where type = #{type} order by type,convert( name using gbk )")
	List<Machine> getMachinesByType(@Param("type") int type);

	@Select("select m.*, pm.name as pour_machine_name from machine as m left join machine as pm on m.pour_machine_id = pm.id order by m.type,convert( m.name using gbk )")
	List<Machine> getMachines();

	@Select("select * from machine where name = #{name}")
	Machine getMachineWithName(@Param("name") String name);

	@Delete("delete from machine where id = #{id}")
	int deleteMachine(@Param("id") int id);

	@Select("select count(*) from machine where name = #{machine.name} and type = #{machine.type}")
	int doesMachineExist(@Param("machine") Machine machine);

	@Select("select count(*) from machine as m, process as p where m.id = #{machineId} and p.machine_id = m.id and p.process_type < 11")
	int isMachineInUse(@Param("machineId") int machineId);

	@Select("select * from machine where uuid = #{uuid} and type = #{type}")
	Machine getMachineByUuidAndType(@Param("uuid") String uuid, @Param("type") int type);

	@Select("select * from machine where id = #{id}")
	Machine getMachineById(@Param("id") int id);

	@Select("select count(m1.id) from machine as m1, machine as m2 where "
			+ "m2.id = #{machineId} and m1.pour_machine_id = m2.id and m1.type = #{machine_type}")
	int getRelatedMachineNum(@Param("machineId") int machineId, @Param("machine_type") int machine_type);
}
