package com.nowcoder.community;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import sun.rmi.runtime.Log;

import java.util.Date;

/**
 * @author LCY
 * @create 2021-05-26-上午 10:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class UserTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void testSelect() {
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }

    @Test
    public void testInsert() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123");
        user.setSalt("abc");
        user.setEmail("ee@163.com");
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode("1234");
        user.setHeaderUrl("star.com");
        int status = userMapper.insertUser(user);
        System.out.println(status);
        System.out.println(userMapper.selectByName("test").getId());
    }

    @Test
    public void testUpdate() {
        userMapper.updateStatus(150, 2);
        userMapper.updateHeader(150, "test.com");
        userMapper.updatePassword(150, "eed");
    }

    @Test
    public void testLoginTicketInsert(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(123);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date());
        loginTicketMapper.insertOneTicket(loginTicket);
    }

    @Test
    public void testLoginTicketSelectAndUpdate(){
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatusByTicket("abc",0);
        System.out.println(loginTicket);
    }

}
