package com.nowcoder.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * @author LCY
 * @create 2021-05-24-下午 08:02
 */
@Repository("AlphaHibernate")  // 注入bean，同时指定了名字
@Primary // 该bean是众多AlphaDao接口的实现类中的首选项
public class AlphaDaoHibernateImpl implements AlphaDao{
    @Override
    public String sayHello() {
        return "Hello Hibernate";
    }
}
