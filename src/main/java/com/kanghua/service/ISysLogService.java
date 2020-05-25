package com.kanghua.service;

import com.baomidou.mybatisplus.service.IService;
import com.kanghua.commons.result.PageInfo;
import com.kanghua.model.SysLog;

/**
 *
 * SysLog 表数据服务层接口
 *
 */
public interface ISysLogService extends IService<SysLog> {

    void selectDataGrid(PageInfo pageInfo);

}