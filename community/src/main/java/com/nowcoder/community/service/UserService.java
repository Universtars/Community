package com.nowcoder.community.service;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LCY
 * @create 2021-05-26-下午 08:58
 */
@Slf4j(topic = "UserService")
@Service
public class UserService implements CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    /*
    用于用户注册时的依赖
     */
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    /*
    用户用户登录时的依赖
     */
    @Autowired
    private LoginTicketMapper loginTicketMapper;

    /**
     * 用于处理注册的逻辑
     *
     * @return 若用户输入的数据已经被注册过，则用map记录，并返回
     */
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        // ① 空值处理
        if (user == null) {
            throw new IllegalArgumentException("用户 - 参数为空");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "用户名为空");
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "用户密码为空");
        }
        if (StringUtils.isBlank((user.getEmail()))) {
            map.put("emailMsg", "用户邮箱为空");
        }

        // ② 验证账号 -  是否存在
        User userInSQL = userMapper.selectByName(user.getUsername());
        if (userInSQL != null) {
            map.put("usernameMsg", "该账号已经存在");
            return map;
        }
        userInSQL = userMapper.selectByEmail(user.getEmail());
        if (userInSQL != null) {
            map.put("emailMsg", "该邮箱已经存在");
            return map;
        }

        // ③ 注册用户 - 到DB
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID().substring(0, 5));
        // http://images.nowcoder.com/head/100t.png
        user.setHeaderUrl("http://images.nowcoder.com/head/" + (int) (Math.random() * 1000) + "t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // ④ 发送验证邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http:localhost:8080/community/activation/[userId]/[activatecode]
//        String userUrl = "http:localhost:8080/community/activation/"+user.getId()+"/"+user.getActivationCode();
        String userUrl = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", userUrl);
        String content = templateEngine.process("mail/activation", context);
        mailClient.sendMail(user.getEmail(), "项目-激活邮件", content);
        return map;
    }


    /**
     * 根据用户点击的激活码，给出激活结果
     * 激活当前用户、当前用户已经激活过、激活码不匹配
     *
     * @param userId   根据邮件地址得到的userId
     * @param linkCode 邮件地址上的激活码
     * @return 跳转到【操作结果】页面，并给出相应的提示
     */
    public int checkActivationRes(int userId, String linkCode) {
        User user = userMapper.selectById(userId);
        // status==1表示已经激活了 status==0表示没有激活
        if (user.getActivationCode().equals(linkCode) && user.getStatus() == 0) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else if (user.getActivationCode().equals(linkCode) && user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else {
            return ACTIVATION_FAIL;
        }
    }


    /**
     * 处理登录业务
     * @param userName
     * @param password
     * @param expiredSeconds
     * @return 带有错误信息 或者 登录凭证的map
     */
    public Map<String, Object> login(String userName, String password, long expiredSeconds) {
        Map<String, Object> map = new HashMap<>();
        // ① 判断空值
        if (StringUtils.isBlank(userName)) {
            map.put("usernameMsg", "用户名不能为空！");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }

        // ② 验证有效性
        User userInDB = userMapper.selectByName(userName);
        if (userInDB == null) {
            map.put("usernameMsg", "该账号不存在！");
            return map;
        }
        if(userInDB.getStatus()==0){
            map.put("usernameMsg","该账户未激活！");
            return map;
        }
        String curUserPassword = password + userInDB.getSalt();
        curUserPassword = CommunityUtil.md5(curUserPassword);
        if (!userInDB.getPassword().equals(curUserPassword)) {
            map.put("passwordMsg", "密码错误！");
            return map;
        }

        // ③ 生成登录凭证
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userInDB.getId());
        ticket.setTicket(CommunityUtil.generateUUID().substring(0, 6));
        ticket.setStatus(0);// 该登录凭证是有效的
        ticket.setExpired(new Date(System.currentTimeMillis()+expiredSeconds*1000));
        loginTicketMapper.insertOneTicket(ticket);
        map.put("ticket", ticket.getTicket());
        return map;
    }


    /**
     * 登录模块 - 退出功能
     * @param ticket 要退出的登录凭证
     */
    public void logOut(String ticket){
        // 在DB中，修改该ticket的状态为1，表示该ticket失效
        loginTicketMapper.updateStatusByTicket(ticket,1);
    }

    public User getUserById(int id) {
        return userMapper.selectById(id);
    }

    /**
     * 从DB中拿到用户的ticket
     * ① MySQL login-ticket表
     * @param ticket
     * @return
     */
    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }
}
