package com.chdp.chdpweb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.chdp.chdpweb.bean.Herb;

public interface HerbDao {
	@Insert("insert herb(type, name, description) values(#{herb.type}, #{herb.name}, #{herb.description})")
	int createHerb(@Param("herb") Herb herb);

	@Select("select * from herb order by type,convert( name using gbk )")
	List<Herb> getHerbs();

	@Select("select * from herb where type = #{type} order by convert( name using gbk )")
	List<Herb> getHerbsByType(@Param("type") int type);

	@Select("select * from herb where name = #{name}")
	Herb getHerbByName(@Param("name") String name);

	@Delete("delete from herb where id = #{id}")
	int deleteHerb(@Param("id") int id);

	@Select("select count(*) from herb where name = #{herb.name} and type = #{herb.type}")
	int doesHerbExist(@Param("herb") Herb herb);
}
