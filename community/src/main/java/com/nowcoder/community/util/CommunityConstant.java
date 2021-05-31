package com.nowcoder.community.util;

/**
 * @author LCY
 * @create 2021-05-28-下午 11:22
 */
public interface CommunityConstant {

    /*
    注册 - 用户激活
     */
    int ACTIVATION_SUCCESS = 0;
    int ACTIVATION_REPEAT = 1;
    int ACTIVATION_FAIL = 2;

    /*
    登录-记住我
     */
    int VALID_TIME_REMEMBER = 3600 * 24 * 100;
    int VALID_TIME_NOT_REMEMBER = 3600 * 24;


}
