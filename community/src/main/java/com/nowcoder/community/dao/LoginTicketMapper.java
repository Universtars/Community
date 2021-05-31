package com.nowcoder.community.dao;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @author LCY
 * @create 2021-05-30-下午 05:11
 */
@Mapper
@Repository
public interface LoginTicketMapper {

    /*
    以ticket字段为纽带
    id,user_id,ticket,status,expired
     */

    /**
     * 向数据库写入一行Ticket
     */
    @Insert({
            "INSERT INTO login_ticket\n" +
            "(user_id,ticket,STATUS,expired)\n" +
            "VALUES(#{userId},#{ticket},#{status},#{expired})"
    })
    int insertOneTicket(LoginTicket loginTicket);

    /**
     * 根据ticket字段，查询一个Ticket实体类
     */
    @Select({
            "SELECT user_id,ticket,STATUS,expired\n" +
            "FROM login_ticket\n" +
            "WHERE ticket=#{ticket}"
    })
    @Options(useGeneratedKeys = true,keyProperty = "id")
    LoginTicket selectByTicket(String ticket);

    /**
     * 更新一个Ticket的status
     */
    @Update({
            "UPDATE login_ticket\n" +
            "SET STATUS=#{status}\n" +
            "WHERE ticket=#{ticket}"
    })
    int updateStatusByTicket(String ticket,int status);

}
