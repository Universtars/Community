package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LCY
 * @create 2021-05-26-下午 08:22
 */
@Mapper
@Repository
public interface DiscussPostMapper {
    /*
    返回指定的贴子列表
    offset从哪一行开始，limit每行显示的数量
    userId是可选的，意为可以看指定用户的贴子
     */
    public List<DiscussPost> getDiscussPosts(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    /*
    查看发帖数量
    userId是可选的，默认为查看所有的
    动态拼一个条件，且该方法只有一个条件，要取别名，使用@Param("userId")
     */
    public int getDiscussPostCount(@Param("userId") int userId);

    /**
     * 新增一条贴子
     * @param discussPost
     * @return 受影响的数据库行数
     */
    int insertDiscussPost(DiscussPost discussPost);
}
