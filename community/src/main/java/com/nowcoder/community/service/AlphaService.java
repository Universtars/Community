package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author LCY
 * @create 2021-05-24-下午 08:29
 */
@Service
public class AlphaService {

    @Autowired
    AlphaDao serveceDao;

    public String useDao(){
        return "service处理了   "+serveceDao.sayHello();
    }


    // 构造器
    public AlphaService(){
    }

    // 在Bean被创建之后调用
    @PostConstruct
    public void init(){
    }

    // 在Bean被销毁前调用
    @PreDestroy
    public void destory(){
    }
}
