package com.kanghua.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.kanghua.commons.result.Tree;
import com.kanghua.commons.shiro.ShiroUser;
import com.kanghua.model.Resource;

/**
 *
 * Resource 表数据服务层接口
 *
 */
public interface IResourceService extends IService<Resource> {

    List<Resource> selectAll();

    List<Tree> selectAllMenu();

    List<Tree> selectAllTree();

    List<Tree> selectTree(ShiroUser shiroUser);

}