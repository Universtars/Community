package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @author LCY
 * @create 2021-05-24-下午 08:11
 */
@Repository("AlphaMyBatis")
public class AlphaDaoMyBatisImpl implements AlphaDao{
    @Override
    public String sayHello() {
        return "Hello MyBatis";
    }
}
