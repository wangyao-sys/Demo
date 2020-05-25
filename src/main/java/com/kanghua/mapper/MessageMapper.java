package com.kanghua.mapper;


import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.kanghua.model.Message;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 */
public interface MessageMapper extends BaseMapper<Message> {
	 List<Map<String, Object>> selectMessagePage(Pagination page, Map<String, Object> params);
	

}