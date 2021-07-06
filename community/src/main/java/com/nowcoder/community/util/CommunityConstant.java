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
    登录-记住我-秒
     */
    int VALID_TIME_REMEMBER = 60 * 60 * 24 * 100;
    int VALID_TIME_NOT_REMEMBER = 3600 * 24;


    /**
     * 实体类型: 帖子
     */
    int ENTITY_TYPE_POST = 1;

    /**
     * 实体类型: 评论
     */
    int ENTITY_TYPE_COMMENT = 2;

}
