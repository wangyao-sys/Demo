package com.kanghua.controller;



import javax.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.kanghua.commons.base.BaseController;
import com.kanghua.commons.result.PageInfo;
import com.kanghua.commons.shiro.ShiroUser;
import com.kanghua.commons.utils.StringUtils;
import com.kanghua.model.Message;
import com.kanghua.service.IMessageService;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@Controller
@RequestMapping("/message")
public class MessageController extends BaseController {

    @Autowired 
    private IMessageService messageService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/message/messageList";
    }
    
    @GetMapping("/manager2")
    public String manager2() {
        return "admin/message/messageList2";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(Message message, Integer page, Integer rows, String sort,String order) {
    	 PageInfo pageInfo = new PageInfo(page, rows, sort, order);
         Map<String, Object> condition = new HashMap<String, Object>();
         if (StringUtils.isNotBlank(message.getTitle())) {
             condition.put("title", message.getTitle());
         }
         pageInfo.setCondition(condition);
         messageService.selectMessagePage(pageInfo);
         return pageInfo;
    }
    
    
    @PostMapping("/dataGrid2")
    @ResponseBody
    public PageInfo dataGrid2(Message message, Integer page, Integer rows, String sort,String order) {
    	 PageInfo pageInfo = new PageInfo(page, rows, sort, order);
         Map<String, Object> condition = new HashMap<String, Object>();
         if (StringUtils.isNotBlank(message.getTitle())) {
             condition.put("title", message.getTitle());
         }
         pageInfo.setCondition(condition);
         messageService.selectMessagePage(pageInfo);
         return pageInfo;
    }
    /**
     * 添加页面
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "admin/message/messageAdd2";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object  add(@Valid Message message) {
       // message.setCreateTime(new Date());
       // message.setUpdateTime(new Date());
       // message.setDeleteFlag(0);
    	ShiroUser userVo  = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    	System.out.println("userVo::"+userVo.getId());//用户ID
    	
    	message.setUid(userVo.getId().intValue());
    	message.setDate(new Date());
    	message.setState(1);
        messageService.insert(message);
		return renderSuccess("删除成功！");
        
    }
    
    /**
     * 删除
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public Object delete(Long id) {
        messageService.deleteById(id.intValue());
        return renderSuccess("删除成功！");
        
    }
    
    /**
     * 编辑
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/editPage")
    public String editPage(Model model, Long id) {
        Message message = messageService.selectById(id);
        model.addAttribute("message", message);
        return "admin/message/messageEdit2";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid Message message) {
        boolean b = messageService.updateById(message);
        if (b) {
            return renderSuccess("编辑成功！");
        } else {
            return renderError("编辑失败！");
        }
    }
}
