package com.chdp.chdpweb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.chdp.chdpweb.bean.User;

@Repository
public interface UserDao {
	@Select("select * from user where usercode = #{usercode}")
	User getUser(@Param("usercode") String usercode);

	@Select("select * from user where id = #{id}")
	User getUserById(@Param("id") int id);

	@Update("update user set password = #{user.password} where id = #{user.id}")
	int changePassword(@Param("user") User user);

	@Select("select * from user")
	List<User> getUserList();

	@Delete("delete from user where id = #{userId}")
	int deleteUser(int userId);

	@Insert("insert into user(usercode,password,name,authority) values(#{user.usercode},#{user.password},#{user.name},#{user.authority})")
	int addUser(@Param("user") User user);
	
	@Update("update user set name = #{user.name},authority = #{user.authority} where id = #{user.id}")
	int updateUser(@Param("user") User user);
}
