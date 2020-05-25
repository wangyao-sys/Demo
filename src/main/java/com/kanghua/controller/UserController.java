package com.kanghua.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.kanghua.commons.base.BaseController;
import com.kanghua.commons.result.PageInfo;
import com.kanghua.commons.shiro.PasswordHash;
import com.kanghua.commons.shiro.ShiroDbRealm;
import com.kanghua.commons.shiro.ShiroUser;
import com.kanghua.commons.utils.StringUtils;
import com.kanghua.model.FileUpload;
import com.kanghua.model.Role;
import com.kanghua.model.User;
import com.kanghua.model.vo.UserVo;
import com.kanghua.service.FileUploadService;
import com.kanghua.service.IUserService;

/**
 * @description：用户管理
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
	@Autowired
	private IUserService userService;
	@Autowired
	private PasswordHash passwordHash;

	@Autowired
	private FileUploadService fileUploadService;
	/**
	 * 用户管理页
	 *
	 * @return
	 */
	@GetMapping("/manager")
	public String manager() {
		return "admin/user/user";
	}

	/**
	 * 用户管理列表
	 *
	 * @param userVo
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 */
	@PostMapping("/dataGrid")
	@ResponseBody
	public Object dataGrid(UserVo userVo, Integer page, Integer rows, String sort, String order) {
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		Map<String, Object> condition = new HashMap<String, Object>();

		if (StringUtils.isNotBlank(userVo.getName())) {
			condition.put("name", userVo.getName());
		}
		if (userVo.getOrganizationId() != null) {
			condition.put("organizationId", userVo.getOrganizationId());
		}
		if (userVo.getCreatedateStart() != null) {
			condition.put("startTime", userVo.getCreatedateStart());
		}
		if (userVo.getCreatedateEnd() != null) {
			condition.put("endTime", userVo.getCreatedateEnd());
		}
		pageInfo.setCondition(condition);
		userService.selectDataGrid(pageInfo);
		return pageInfo;
	}

	@GetMapping("/manager2")
	public String manager2() {
		return "admin/user/user2";
	}
	
	@PostMapping("/dataGrid2")
	@ResponseBody
	public Object dataGrid2(UserVo userVo, Integer page, Integer rows, String sort, String order) {
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		ShiroUser userVo1  = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("tid", userVo1.getId());
		if (StringUtils.isNotBlank(userVo.getName())) {
			condition.put("name", userVo.getName());
		}
		if (userVo.getOrganizationId() != null) {
			condition.put("organizationId", userVo.getOrganizationId());
		}
		if (userVo.getCreatedateStart() != null) {
			condition.put("startTime", userVo.getCreatedateStart());
		}
		if (userVo.getCreatedateEnd() != null) {
			condition.put("endTime", userVo.getCreatedateEnd());
		}
		pageInfo.setCondition(condition);
		userService.selectFileDownPage(pageInfo);
		return pageInfo;
	}

	
	
	
	/**
	 * 添加用户页
	 *
	 * @return
	 */
	@GetMapping("/addPage")
	public String addPage() {
		return "admin/user/userAdd";
	}

	@PostMapping("/tree")
	@ResponseBody
	public Object tree() {
		System.out.println(userService.selectTree());
		return userService.selectTree();
	}


	/**
	 * 添加用户
	 *
	 * @param userVo
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	public Object add(@Valid UserVo userVo) {
		List<User> list = userService.selectByLoginName(userVo);
		if (list != null && !list.isEmpty()) {
			return renderError("登录名已存在!");
		}
		String salt = StringUtils.getUUId();
		String pwd = passwordHash.toHex(userVo.getPassword(), salt);
		userVo.setSalt(salt);
		userVo.setPassword(pwd);
		userService.insertByVo(userVo);
		return renderSuccess("添加成功");
	}

	/**
	 * 编辑用户页
	 *
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/editPage")
	public String editPage(Model model, Long id) {
		UserVo userVo = userService.selectVoById(id);
		List<Role> rolesList = userVo.getRolesList();
		List<Long> ids = new ArrayList<Long>();
		for (Role role : rolesList) {
			ids.add(role.getId());
		}
		model.addAttribute("roleIds", ids);
		model.addAttribute("user", userVo);
		return "admin/user/userEdit";
	}

	@GetMapping("/fileeditPage")
	public String fileeditPage(Model model, Long id) {
		FileUpload fileUpload = fileUploadService.selectById(id);
		model.addAttribute("fileUpload", fileUpload);
		return "admin/fileupload/fileUploadEdit";
	}
	/**
	 * 编辑用户
	 *
	 * @param userVo
	 * @return
	 */
	@RequiresRoles("admin")
	@PostMapping("/edit")
	@ResponseBody
	public Object edit(@Valid UserVo userVo) {
		List<User> list = userService.selectByLoginName(userVo);
		if (list != null && !list.isEmpty()) {
			return renderError("登录名已存在!");
		}
		// 更新密码
		if (StringUtils.isNotBlank(userVo.getPassword())) {
			User user = userService.selectById(userVo.getId());
			String salt = user.getSalt();
			String pwd = passwordHash.toHex(userVo.getPassword(), salt);
			userVo.setPassword(pwd);
		}
		userService.updateByVo(userVo);
		return renderSuccess("修改成功！");
	}

	/**
	 * 修改密码页
	 *
	 * @return
	 */
	@GetMapping("/editPwdPage")
	public String editPwdPage() {
		return "admin/user/userEditPwd";
	}

	@Autowired
	private ShiroDbRealm shiroDbRealm;

	/**
	 * 修改密码
	 *
	 * @param oldPwd
	 * @param pwd
	 * @return
	 */
	@PostMapping("/editUserPwd")
	@ResponseBody
	public Object editUserPwd(String oldPwd, String pwd) {
		User user = userService.selectById(getUserId());
		String salt = user.getSalt();
		if (!user.getPassword().equals(passwordHash.toHex(oldPwd, salt))) {
			return renderError("老密码不正确!");
		}
		// 修改密码时清理用户的缓存
		shiroDbRealm.removeUserCache(user.getLoginName());
		userService.updatePwdByUserId(getUserId(), passwordHash.toHex(pwd, salt));
		return renderSuccess("密码修改成功！");
	}


	@RequestMapping("finduser") 
	public String finduser(HttpServletRequest request){
		ShiroUser userVo  = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		System.out.println("userVo::"+userVo.getId());//用户ID
		User user=	userService.selectById(userVo.getId());
		request.setAttribute("user", user);
		return "admin/user/STEdit";

	}


	@RequestMapping("edit2") 
	public void edit(User user){
		userService.updateById(user);
		System.out.println(user);

	}


	@PostMapping("/edit3")
	@ResponseBody
	public String edit2(@Valid User user,HttpServletRequest request,HttpServletResponse response) {
		String data="修改成功";
		JSONObject json= new JSONObject();
		json.put("data", data);
		String name=request.getParameter("name");
		String phone=request.getParameter("phone");
		String age=request.getParameter("age");
		String sex=request.getParameter("sex");
		user.setName(name);
		user.setPhone(phone);
		user.setAge(Integer.valueOf(age));
		user.setSex(Integer.valueOf(sex));
		userService.updateById(user);
		return "200";

	}


	/**
	 * 删除用户
	 *
	 * @param id
	 * @return
	 */
	@RequiresRoles("admin")
	@PostMapping("/delete")
	@ResponseBody
	public Object delete(Long id) {
		Long currentUserId = getUserId();
		if (id == currentUserId) {
			return renderError("不可以删除自己！");
		}
		userService.deleteUserById(id);
		return renderSuccess("删除成功！");
	}
}