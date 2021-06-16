package com.nowcoder.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 该注解加在方法上
 *  加了该注解的方法：必须“用户登录后”，才可以使用
 *  （加Controller上的方法上，从而限制其对应的请求）
 * @author LCY
 * @create 2021-06-16-下午 07:16
 */

@Target(ElementType.METHOD)  // 声明其加在方法上
@Retention(RetentionPolicy.RUNTIME) // 在运行时生效
public @interface LoginRequired {
}
