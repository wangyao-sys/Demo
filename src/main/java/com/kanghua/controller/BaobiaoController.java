package com.kanghua.controller;

import com.alibaba.fastjson.JSONObject;
import com.kanghua.commons.base.BaseController;
import com.kanghua.commons.result.PageInfo;
import com.kanghua.commons.shiro.PasswordHash;
import com.kanghua.commons.shiro.ShiroUser;
import com.kanghua.commons.utils.StringUtils;
import com.kanghua.model.FileUpload;
import com.kanghua.model.Role;
import com.kanghua.model.vo.UserVo;
import com.kanghua.service.FileUploadService;
import com.kanghua.service.IOrganizationService;
import com.kanghua.service.IRoleService;
import com.kanghua.service.IUserService;
import com.kanghua.service.impl.BaoBIaoService;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @description：用户管理
 */
@Controller
@RequestMapping("/baobiao")
public class BaobiaoController extends BaseController {
    @Autowired
    private IUserService userService;
    @Autowired
    private PasswordHash passwordHash;
    @Autowired
    private IOrganizationService organizationService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private BaoBIaoService baoBIaoService;

    @Autowired
	private FileUploadService fileUploadService;
    @RequestMapping("test")
    @ResponseBody
    public void alipayforward(HttpServletRequest req, HttpServletResponse response) throws Exception {
        ShiroUser userVo  = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        System.out.println("userVo::"+userVo.getId());//用户ID
        UserVo user =userService.selectVoById(userVo.getId());

        System.out.println(user.getOrganizationName());//用户名字
        System.out.println(user.getOrganizationId());//用户所属科室ID
        System.out.println(user.getLoginName());//用户工号
        //List<User> list = userService.selectByLoginName(userVo);
        //System.out.println("list::"+list);
        Role role= roleService.selectById(userVo.getId());
        JSONObject json = new JSONObject();
        json.put("role", role.getId());
        response.getWriter().print(json.toString());
    }
    @RequestMapping("/toImportDatePage")
    public String importDate(){
        return "/admin/importExcel";
    }

    @RequestMapping("/toImportTeachDatePage")
    public String importTeachDate(){
        return "/admin/importTeachExcel";
    }
    @RequestMapping("/uploadFile")
    @ResponseBody
    public Map<String,String> uploadFile(MultipartFile file, HttpServletRequest request) throws Exception {
       Map<String,String> map = new HashMap<>();
        String name = file.getName();

        String originalFilename = file.getOriginalFilename();
        String realPath = request.getSession().getServletContext().getRealPath("upload")+"/"+ UUID.randomUUID().toString()+"."+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        File destFile = new File(realPath);
        file.transferTo(destFile);
        String path = destFile.getPath();
        System.out.println(path);
        baoBIaoService.importExcel(realPath);
        map.put("msg", "导入成功");
        return map;
    }
    
    
    @RequestMapping("/uploadteachFile")
    @ResponseBody
    public Map<String,String> uploadteachFile(MultipartFile file, HttpServletRequest request) throws Exception {
       Map<String,String> map = new HashMap<>();
        String name = file.getName();

        String originalFilename = file.getOriginalFilename();
        String realPath = request.getSession().getServletContext().getRealPath("upload")+"/"+ UUID.randomUUID().toString()+"."+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        File destFile = new File(realPath);
        file.transferTo(destFile);
        String path = destFile.getPath();
        System.out.println(path);
        baoBIaoService.importTeachExcel(realPath);
        map.put("msg", "导入成功");
        return map;
    }
    
    
    
    
    
    @GetMapping("/manager4")
    public String manager4() {
        return "admin/fileuploud/fileUploadListself";
    }
    @PostMapping("/dataGrid4")
    @ResponseBody
    public PageInfo dataGrid4(FileUpload fileUpload, Integer page, Integer rows, String sort,String order) {
    	ShiroUser userVo  = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("uid", userVo.getId());
        if (StringUtils.isNotBlank(fileUpload.getName())) {
            condition.put("name", fileUpload.getName());
        }
        pageInfo.setCondition(condition);
        fileUploadService.selectFileself(pageInfo);
        return pageInfo;
    }
}