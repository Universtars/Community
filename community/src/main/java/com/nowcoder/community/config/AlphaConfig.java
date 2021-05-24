package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * @author LCY
 * @create 2021-05-24-下午 08:46
 */
@Configuration
public class AlphaConfig {

    @Bean   // 标明这是一个可以使用的bean   函数名就是这个bean的名字
    public SimpleDateFormat simpleDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
