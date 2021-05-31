package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LCY
 * @create 2021-05-26-下午 09:20
 */
@Controller
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private DiscussPostService discussPostService;

    /*
    获取首页信息
    首页的post、用户信息
     */
    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){
        page.setRows(discussPostService.getPostCount(0));
        page.setPath("/index");
        List<DiscussPost> posts = discussPostService.getPosts(0, page.getOffset(), page.getLimit());
        List<Map<String,Object>> pageInfo = new ArrayList<>();
        if(posts!=null){
            for(DiscussPost curPost:posts){
                Map<String,Object> map = new HashMap<>();
                map.put("post",curPost);
                User user = userService.getUserById(curPost.getUserId());
                map.put("user",user);
                pageInfo.add(map);
            }
        }
        model.addAttribute("discussPosts",pageInfo);
        return "/index";
    }
}
