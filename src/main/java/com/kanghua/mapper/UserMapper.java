package com.kanghua.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.kanghua.model.User;
import com.kanghua.model.vo.UserVo;

/**
 *
 * User 表数据库控制层接口
 *
 */
public interface UserMapper extends BaseMapper<User> {

    UserVo selectUserVoById(@Param("id") Long id);

    List<Map<String, Object>> selectUserPage(Pagination page, Map<String, Object> params);
    List<Map<String, Object>> selectFileDownPage(Pagination page, Map<String, Object> params);
}