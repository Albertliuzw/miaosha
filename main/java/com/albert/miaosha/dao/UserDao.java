package com.albert.miaosha.dao;


import com.albert.miaosha.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {
    @Select({"SELECT * ",
            "FROM `user` ",
            "WHERE `id` = #{id}",
    })
    public User getById(@Param("id")int id);
}
