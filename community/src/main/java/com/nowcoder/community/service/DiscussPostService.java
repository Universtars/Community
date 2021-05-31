package com.nowcoder.community.service;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LCY
 * @create 2021-05-26-下午 09:00
 */
@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> getPosts(int id, int offset, int limit) {
        return discussPostMapper.getDiscussPosts(id, offset, limit);
    }

    public int getPostCount(int id) {
        return discussPostMapper.getDiscussPostCount(id);
    }
}
