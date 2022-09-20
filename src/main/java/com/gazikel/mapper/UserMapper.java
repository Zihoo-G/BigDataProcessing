package com.gazikel.mapper;

import com.gazikel.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    @Select("select * from `user` where `username` = #{username}")
    public User getUserByName(@Param("username") String username);
}
