package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import com.sun.deploy.net.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demo演示集合
 */
@Slf4j(topic = "AlphaController")
@Controller
@RequestMapping("/alpha")
public class AlphaController {

    @Autowired
    AlphaService controllerService;

    @RequestMapping("di")
    @ResponseBody
    public String useService() {
        return "Controller处理了   " + controllerService.useDao();
    }


    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello Spring Boot.";
    }


    // GET  /students?current=1&limit=10
    @RequestMapping(path = "/students", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit
    ) {
        System.out.println(current);
        System.out.println(limit);
        return "Stu lists";
    }


    // GET /students/101
    @RequestMapping(path = "/students/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStu(
            @PathVariable("id") int id
    ) {
        System.out.println(id);
        return "Stu No.1";
    }

    // POST
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStu(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return "save success";
    }

    // 响应HTML 方法一
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("name", "DaMing");
        mav.addObject("age", 30);
        mav.setViewName("/demo/view");
        return mav;
    }

    // 响应HTML 方法二
    @RequestMapping(path = "/school", method = RequestMethod.GET)
    public String getSchool(Model model) {
        model.addAttribute("name", "北京");
        model.addAttribute("age", 80);
        return "/demo/view";
    }

    // 响应JSON
    @RequestMapping(path = "emp", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmp() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "DaMing");
        map.put("age", 20);
        map.put("salary", 200.0);
        return map;
    }


    @RequestMapping(path = "emps",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps() {
        List<Map<String, Object>> lists = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "DaMing");
        map1.put("age", 20);
        map1.put("salary", 200.0);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "LiLi");
        map2.put("age", 25);
        map2.put("salary", 300.0);
        Map<String, Object> map3 = new HashMap<>();
        map3.put("name", "Xi");
        map3.put("age", 10);
        map3.put("salary", 400.0);
        lists.add(map1);
        lists.add(map2);
        lists.add(map3);
        return lists;
    }

    /**
     * 服务器向浏览器发送cookie
     * @param response 响应
     * @return
     */
    @RequestMapping(path = "/cookie/set",method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID().substring(0,5));
        // 设置生效范围【在哪些页面下】
        cookie.setPath("/community/alpha");
        // 设置生存时间【单位为秒】
        cookie.setMaxAge(2*60*60);
        response.addCookie(cookie);
        return "set cookie success";
    }

    /**
     * 服务器响应客户端的GET请求，显示服务端之前向浏览器发送的cookie
     * @param code key是"code"的cookie
     */
    @RequestMapping(path = "/cookie/get" ,method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code){
        log.info("cookie的key:code,value:{}",code);
        return "get cookie success";
    }


    /**
     * 服务端存储session
     */
    @RequestMapping(path = "/session/set",method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session){
        session.setAttribute("userName","LCY");
        session.setAttribute("userAge",26);
        return "set session success";
    }
    /**
     * 服务端获取session
     */
    @RequestMapping(path = "/session/get",method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session){
        String userName = (String)session.getAttribute("userName");
        int userAge = (int)session.getAttribute("userAge");
        log.info("userName:{},userAge:{}",userName,userAge);
        return "get session success";
    }

}
