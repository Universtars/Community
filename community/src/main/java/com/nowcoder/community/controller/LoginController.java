package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author LCY
 * @create 2021-05-28-下午 06:15
 */
@Slf4j(topic = "LoginController")
@Controller
public class LoginController implements CommunityConstant {

    /*
    生成验证码的实例
     */
    @Autowired
    private Producer kaptchaProducer;

    @Autowired
    private UserService userService;


    @Value("${server.servlet.context-path}")
    private String contextPath;

    /*
     **** 页面跳转 ****
     */

    /**
     * 跳转到注册页
     * 访问 /register地址后，跳转到thymeleaf定义好的页面上
     *
     * @return 跳转到注册页
     */
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String jumpToRegister() {
        return "/site/register";
    }

    /**
     * 跳转到登录页
     *
     * @return thymeleaf中的登录位置
     */
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String jumpToLogin() {
        return "/site/login";
    }


    /*
     **** 注册模块 ****
     */

    /**
     * 处理注册POST请求
     * ① 成功注册用户
     * ② 若注册不成功，给出提示信息
     *
     * @param model 配合thymeleaf，携带模板信息
     * @param user  前端传来的用户数据（username,password,email）
     * @return 跳转的页面：当前页面、注册成功的提示页
     */
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String registerResult(Model model, User user) {
        Map<String, Object> registerMap = userService.register(user);
//        log.info("输出INFO级别日志");
//        log.warn("输出WARN级别日志");
//        log.error("输出ERROR级别日志");
        // 若用户在注册时输入的信息满足，则跳转到信息提示结果页
        if (registerMap.isEmpty()) {
            // 提示信息
            model.addAttribute("mag", "您的账号已经注册成功!请稍后根据邮箱中的链接进行激活");
            // 倒计时后，跳转的路径
            model.addAttribute("target", "/index");
            return "site/operate-result";
        }
        // 用户在注册时输入的注册信息有误（此前存在过），仍停留在当前页面，并给出对应的提示信息
        else {
            // 传递对应的错误信息
            model.addAttribute("usernameMsg", registerMap.get("usernameMsg"));
            model.addAttribute("passwordMsg", registerMap.get("passwordMsg"));
            model.addAttribute("emailMsg", registerMap.get("emailMsg"));
            return "site/register";
        }
    }


    /**
     * 用户点击邮件中的激活链接，验证激活码，跳转到结果页并给出提示
     *
     * @param model  传入验证结果、结果页中稍后跳转的目标页
     * @param userId 根据邮箱链接中得到的用户id
     * @param code   根据邮箱链接中得到的激活码
     * @return 跳转到结果显示页
     */
    // http:localhost:8080/community/activation/[userId]/[activatecode]
    @RequestMapping(path = "/activation/{userId}/{activatecode}", method = RequestMethod.GET)
    public String handleActivateRes(Model model,
                                    @PathVariable("userId") int userId, @PathVariable("activatecode") String code) {
        User curUser = userService.getUserById(userId);
        int checkActivationRes = userService.checkActivationRes(userId, code);
//        log.info("{}",checkActivationRes);
        if (checkActivationRes == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "验证码正确，账号已激活");
            model.addAttribute("target", "/login");
        } else if (checkActivationRes == ACTIVATION_FAIL) {
            model.addAttribute("msg", "验证码不正确，请重新注册");
            model.addAttribute("target", "/register");
        } else {
            model.addAttribute("msg", "该账号已经激活过，请直接登录");
            model.addAttribute("target", "/login");
        }
        return "site/operate-result";
    }


    /**
     * 浏览器访问/kaptcha，生成验证码
     *
     * @param response
     * @param session
     */
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // 将验证码存入session
        session.setAttribute("kaptcha", text);

        // 将图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            log.error("响应验证码失败:" + e.getMessage());
        }
    }


    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param code       验证码
     * @param rememberme 是否点击"记住我"
     * @param session    为了验证验证码，之前已经将验证码存入session中了
     * @param response   为了给浏览器cookie：登录凭证target
     * @param model      向其中添加错误信息，稍后将其传送到结果页
     * @return
     */
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String code, boolean rememberme,
                        HttpSession session, HttpServletResponse response, Model model) {
        // 检查验证码 login
        String verifyCodeInSession = (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(verifyCodeInSession) || StringUtils.isBlank(code)
                || !verifyCodeInSession.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "验证码有误！");
            return "/site/login";
        }
        // 检查用户名、密码
        long expiredSeconds = rememberme ? VALID_TIME_REMEMBER : VALID_TIME_NOT_REMEMBER;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if (!map.containsKey("ticket")) {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        } else {
            // 将ticket当作cookie传送给浏览器 redirect ?  index
            String ticket = map.get("ticket").toString();
            Cookie cookie = new Cookie("ticket", ticket);
            cookie.setPath(contextPath);
            cookie.setMaxAge((int) expiredSeconds);
            response.addCookie(cookie);
            // 重定向到首页
            return "redirect:/index";
        }
    }

    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public String logOut(@CookieValue("ticket") String toValidTicket){
        userService.logOut(toValidTicket);
        return "redirect:/index";
    }


}
