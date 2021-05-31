package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import javafx.scene.effect.DisplacementMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author LCY
 * @create 2021-05-26-下午 08:32
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class DiscussPostTest {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testGetDiscussPosts() {
        List<DiscussPost> discussPosts = discussPostMapper.getDiscussPosts(101, 1, 10);
        for (DiscussPost curPost : discussPosts) {
            System.out.println(curPost);
        }
    }

    @Test
    public void testGetDiscussPostCount() {
        int count = discussPostMapper.getDiscussPostCount(101);
        System.out.println(count);
    }
}
