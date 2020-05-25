package com.kanghua.service;


import com.baomidou.mybatisplus.service.IService;
import com.kanghua.commons.result.PageInfo;
import com.kanghua.model.Message;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface IMessageService extends IService<Message> {
	void selectMessagePage(PageInfo pageInfo);
}
