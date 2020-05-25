package com.kanghua.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.ui.Model;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.kanghua.commons.base.BaseController;
import com.kanghua.commons.result.PageInfo;
import com.kanghua.commons.shiro.PasswordHash;
import com.kanghua.commons.shiro.ShiroUser;
import com.kanghua.commons.utils.StringUtils;
import com.kanghua.model.FileUpload;
import com.kanghua.model.Role;
import com.kanghua.model.User;
import com.kanghua.model.vo.UserVo;
import com.kanghua.service.FileUploadService;
import com.kanghua.service.IOrganizationService;
import com.kanghua.service.IUserService;

/**
 * 
 * @author Loud
 * 设备管理
 */
@Controller
@RequestMapping("/fileupload")
public class FileUploadController extends BaseController{
	/**
	 * 文件管理页
	 */

	@Autowired
	private IUserService userService;
	
	@Autowired
	private PasswordHash passwordHash;
	@Autowired
	private IOrganizationService organizationService;
	@Autowired
	private FileUploadService fileUploadService;

	public FileUploadService getFileUploadService() {
		return fileUploadService;
	}
	public void setFileUploadService(FileUploadService fileUploadService) {
		this.fileUploadService = fileUploadService;
	}
	@RequestMapping("add")
	public String manager(String filename ){
		System.out.println(filename);
		//String filename=request.getParameter("filename");
		return "/admin/fileuploud/fileUploadAdd";
	}
	@RequestMapping("downpage")
	public String downpage(HttpServletRequest request){
		fileUploadService.selectfile();
		System.out.println(fileUploadService.selectfile());
		request.setAttribute("fileUpload", fileUploadService.selectfile());
		return "/admin/fileuploud/filedown";
	}
	
	@RequestMapping("/del")
    @ResponseBody
    public Object delete(Long id) {
		fileUploadService.deleteById(id.intValue());
        return renderSuccess("删除成功！");
        
    }
	
	@RequestMapping("/upload")
	@ResponseBody
	public Object doupload(@Valid FileUpload fileUpload,@RequestParam("pictureurl1") MultipartFile file[], HttpServletRequest request){
		//String name2 = request.getParameter("name");
		ShiroUser userVo  = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		System.out.println("userVo::"+userVo.getId());//用户ID
		User user =userService.selectById(userVo.getId());
		System.out.println(user.getLoginName());//用户名字
		System.out.println(user.getOrganizationId());//用户所属科室ID
		System.out.println(user.getLoginName());//用户工号
		System.out.println(user.getTeachid());//老师id
		Map<String, Object> map=new HashMap<String, Object>();
		
		try {
			//		文件上传:
			
			//String path = request.getSession().getServletContext().getRealPath("Common/images/scale");  
			String path =null;
			String fileName =null;
			File dest = null;
			for (int i = 0; i < file.length; i++) {
				MultipartFile multipartFile=file[i];
				path ="F:\\picture";
				String suffix =  file[i].getOriginalFilename().substring( file[i].getOriginalFilename().lastIndexOf(".")+1);
				//fileName = UUID.randomUUID().toString()+"."+suffix;
				fileName = file[i].getOriginalFilename(); 
				File dir = new File(path);
				if(!dir.exists()){  
					dir.mkdirs();  
				} 
				dest = new File(path,fileName);     //要拷贝到的目标位置     
				//MultipartFile自带的解析方法  
				multipartFile.transferTo(dest);
				map.put("Pictureurl"+i,fileName);
			}
			System.out.println("path:::"+path);
			/*File dir = new File(path);
			if(!dir.exists()){  
				dir.mkdirs();  
			} */ 
			   //获取文件名  file.getName() :获取表单字段名 name="file"
			
			System.out.println("位置："+dest);
			
			//pic.setPictureurl("F:/picture"+fileName);//
			System.out.println("fileName："+fileName);
			fileUpload.setUrl("F:/picture/"+fileName);
			fileUpload.setName(fileName);
			fileUpload.setState(fileUpload.getState());
			fileUpload.setUserid(userVo.getId().intValue());
			fileUpload.setTeacherid(user.getTeachid());
			fileUpload.setMark(fileUpload.getMark());
			fileUpload.setWeeks(fileUpload.getWeeks());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Boolean b= fileUploadService.insert(fileUpload);
		if(b){
			return renderSuccess("上传成功");
		}else {
			return renderSuccess("上传失败");
		}
		

	}
	
	@RequestMapping("/download2") //匹配的是href中的download请求
    public ResponseEntity<byte[]> download( String filename) throws IOException{
    	//filename=request.getParameter("filename");
        System.out.println(filename);
         String downloadFilePath="F:\\picture";//从我们的上传文件夹中去取
        
        File file = new File(downloadFilePath+File.separator+filename);//新建一个文件
        
        HttpHeaders headers = new HttpHeaders();//http头信息
        
        String downloadFileName = new String(filename.getBytes("UTF-8"),"iso-8859-1");//设置编码
        
        headers.setContentDispositionFormData("attachment", downloadFileName);
        
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        
        //MediaType:互联网媒介类型  contentType：具体请求中的媒体类型信息
        
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers,HttpStatus.CREATED);
        
    }
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/fileuploud/fileUploadList";
    }
 
    
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(FileUpload fileUpload, Integer page, Integer rows, String sort,String order) {
        PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(fileUpload.getName())) {
            condition.put("name", fileUpload.getName());
        }
        if (fileUpload.getId() != null) {
            condition.put("id", fileUpload.getId());
        }
        pageInfo.setCondition(condition);
        fileUploadService.selectFileDownPage(pageInfo);
        return pageInfo;
    }
    
    @GetMapping("/manager2")
    public String manager2() {
        return "admin/fileuploud/fileUploadList2";
    }
    @PostMapping("/dataGrid2")
    @ResponseBody
    public PageInfo dataGrid2(FileUpload fileUpload, Integer page, Integer rows, String sort,String order) {
        PageInfo pageInfo = new PageInfo(page, rows, sort, order);
    	ShiroUser userVo1  = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("tid", userVo1.getId());
        if (StringUtils.isNotBlank(fileUpload.getName())) {
            condition.put("name", fileUpload.getName());
        }
        if (fileUpload.getId() != null) {
            condition.put("id", fileUpload.getId());
        }
        pageInfo.setCondition(condition);
        fileUploadService.selectFileDownPage2(pageInfo);
        return pageInfo;
    }
    
    
    @GetMapping("/manager3")
    public String manager3() {
        return "admin/fileuploud/fileUploadList3";
    }
    @PostMapping("/dataGrid3")
    @ResponseBody
    public PageInfo dataGrid3(FileUpload fileUpload, Integer page, Integer rows, String sort,String order) {
        PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(fileUpload.getName())) {
            condition.put("name", fileUpload.getName());
        }
        if (fileUpload.getId() != null) {
            condition.put("id", fileUpload.getId());
        }
        pageInfo.setCondition(condition);
        fileUploadService.selectFileDownPage3(pageInfo);
        return pageInfo;
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
    
    @RequestMapping("/look")
    @ResponseBody
    private static String useCMDCommand(String url) throws IOException{

        //若打开的目录或文件名中不包含空格,就用下面的方式

        //Runtime.getRuntime().exec("cmd /c start D:/mylocal/测试用例.xls");

        //(可以'运行'或'Win+R',然后输入'cmd /?'查看帮助信息)
        System.out.println("打开文件");   
        Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", url});
		return "1";
        }
    @GetMapping("/editPage")
    public String editPage(Model model, Long id) {
        FileUpload fileUpload = fileUploadService.selectById(id);
        model.addAttribute("fileUpload", fileUpload);
        return "admin/fileuploud/fileUploadEdit222";
    }
    @RequestMapping("/updatelevel")
    @ResponseBody
    public Object updatelevel(FileUpload fileUpload,HttpServletRequest request) {
    	
    	String evaluate=request.getParameter("evaluate");
    	String level=request.getParameter("level");
    	fileUpload.setEvaluate(evaluate);
    	fileUpload.setLevel(Integer.valueOf(level));
    	fileUploadService.updateById(fileUpload);
    	return renderSuccess("评论成功");
    }
}
