package com.nowcoder.community.service;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author LCY
 * @create 2021-05-26-下午 09:00
 */
@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    // 敏感词过滤
//    @Autowired
//    private SensitiveFilter sensitiveFilter;

    public List<DiscussPost> getPosts(int id, int offset, int limit) {
        return discussPostMapper.getDiscussPosts(id, offset, limit);
    }

    public int getPostCount(int id) {
        return discussPostMapper.getDiscussPostCount(id);
    }


    /**
     * 新增一条贴子
     *
     * @param post
     * @return 数据库中受影响的行数
     */
    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 转义HTML标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        // 过滤敏感词
//        post.setTitle(sensitiveFilter.filter(post.getTitle()));
//        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insertDiscussPost(post);
    }
}
