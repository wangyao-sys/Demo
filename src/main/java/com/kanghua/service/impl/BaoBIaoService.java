package com.kanghua.service.impl;

import com.kanghua.commons.shiro.PasswordHash;
import com.kanghua.commons.shiro.ShiroUser;
import com.kanghua.commons.utils.StringUtils;
import com.kanghua.model.User;
import com.kanghua.model.vo.UserVo;
import com.kanghua.service.IUserService;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BaoBIaoService {
    @Autowired
    private PasswordHash passwordHash;
   @Autowired
    private IUserService userService;
    public Map<String,Object> importExcel(String excelFileFileName) throws Exception{
        Map<String,Object>  map = new HashMap<>();
        /*List<User> list = userService.selectByLoginName(userVo);
        if (list != null && !list.isEmpty()) {
            return renderError("登录名已存在!");
        }
        String salt = StringUtils.getUUId();
        String pwd = passwordHash.toHex(userVo.getPassword(), salt);
        userVo.setSalt(salt);
        userVo.setPassword(pwd);
        userService.insertByVo(userVo);*/
        FileInputStream in = new FileInputStream(excelFileFileName);
        Workbook workbook;
        if(excelFileFileName.indexOf(".xlsx")>-1){
            workbook = new XSSFWorkbook(new FileInputStream(excelFileFileName));
        } else {
            workbook = new HSSFWorkbook(new FileInputStream(excelFileFileName));
        }
        //读取sheet
        Sheet sheet = workbook.getSheetAt(0);
        //获得总行数
        int rowNum=sheet.getLastRowNum();
        int physicalNumberOfCells = sheet.getRow(0).getPhysicalNumberOfCells();
        //List<Student> studentList = new ArrayList<Student>();
        ShiroUser userVo2  = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        for (int i = 0; i <=rowNum; i++) {
            Row row = null;
            String s1 = "";
            if(i > 0) {
                UserVo  userVo = new UserVo();
                String salt = StringUtils.getUUId();

                String loginName = "";
                
                userVo.setSalt(salt);

                row = sheet.getRow(i);
                loginName = row.getCell(0).toString();
                //String age = row.getCell(3).toString();
               /* DecimalFormat df1 = new DecimalFormat("#");
                int age = (int)row.getCell(3).getNumericCellValue();*/
                
                if(String.valueOf(row.getCell(0)).indexOf("E")==-1){
                    loginName = String.valueOf(row.getCell(0));
                }else {
                    DecimalFormat df = new DecimalFormat("#");
                    int numericCellValue = (int)row.getCell(0).getNumericCellValue();
                    loginName = numericCellValue+"";
                }
                userVo.setLoginName(loginName);
                userVo.setUserType(1);
                //userVo.setAge(getValue(age));
                String password = "";
                if(String.valueOf(row.getCell(3)).indexOf("E")==-1){
                    password = String.valueOf(row.getCell(3));
                }else {
                    DecimalFormat df = new DecimalFormat("#");
                    double numericCellValue = row.getCell(3).getNumericCellValue();
                    //password = numericCellValue+"";
                    password = df.format(numericCellValue);
                }
                userVo.setPhone(password);
                String pwd = passwordHash.toHex(password, salt);
                userVo.setPassword(pwd);
                String sex = row.getCell(2).toString();
                if(StringUtils.isNotBlank(sex)) {
                    if(sex.equals("男")) {
                        userVo.setAge(1);
                    }else if(sex.equals("女")) {
                        userVo.setAge(0);
                    }
                }
                userVo.setOrganizationId(1);
                userVo.setName(row.getCell(1).toString());
                userVo.setRoleIds("3");
                userVo.setTeachid(userVo2.getId().intValue());
                userService.insertByVo(userVo);
            }

        }
        map.put("ifSuccess",true);
        return map;
    }
    
    private Integer getValue(String age) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String,Object> importTeachExcel(String excelFileFileName) throws Exception{
        Map<String,Object>  map = new HashMap<>();
        /*List<User> list = userService.selectByLoginName(userVo);
        if (list != null && !list.isEmpty()) {
            return renderError("登录名已存在!");
        }
        String salt = StringUtils.getUUId();
        String pwd = passwordHash.toHex(userVo.getPassword(), salt);
        userVo.setSalt(salt);
        userVo.setPassword(pwd);
        userService.insertByVo(userVo);*/
        FileInputStream in = new FileInputStream(excelFileFileName);
        Workbook workbook;
        if(excelFileFileName.indexOf(".xlsx")>-1){
            workbook = new XSSFWorkbook(new FileInputStream(excelFileFileName));
        } else {
            workbook = new HSSFWorkbook(new FileInputStream(excelFileFileName));
        }
        //读取sheet
        Sheet sheet = workbook.getSheetAt(0);
        //获得总行数
        int rowNum=sheet.getLastRowNum();
        int physicalNumberOfCells = sheet.getRow(0).getPhysicalNumberOfCells();
        //List<Student> studentList = new ArrayList<Student>();
        ShiroUser userVo2  = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        for (int i = 0; i <=rowNum; i++) {
            Row row = null;
            String s1 = "";
            if(i > 0) {
                UserVo  userVo = new UserVo();
                String salt = StringUtils.getUUId();

                String loginName = "";
                userVo.setSalt(salt);

                row = sheet.getRow(i);
                loginName = row.getCell(0).toString();
                //String age = row.getCell(3).toString();
                if(String.valueOf(row.getCell(0)).indexOf("E")==-1){
                    loginName = String.valueOf(row.getCell(0));
                }else {
                    DecimalFormat df = new DecimalFormat("#");
                    int numericCellValue = (int)row.getCell(0).getNumericCellValue();
                    loginName = numericCellValue+"";
                }
                userVo.setLoginName(loginName);
                userVo.setUserType(1);
                //userVo.setAge(Integer.valueOf(age).intValue());
                String password = "";
                if(String.valueOf(row.getCell(3)).indexOf("E")==-1){
                    password = String.valueOf(row.getCell(3));
                }else {
                	 DecimalFormat df = new DecimalFormat("#");
                     double numericCellValue = row.getCell(3).getNumericCellValue();
                     //password = numericCellValue+"";
                     password = df.format(numericCellValue);
                }
                userVo.setPhone(password);
                String pwd = passwordHash.toHex(password, salt);
                userVo.setPassword(pwd);
                String sex = row.getCell(2).toString();
                if(StringUtils.isNotBlank(sex)) {
                    if(sex.equals("男")) {
                        userVo.setAge(1);
                    }else if(sex.equals("女")) {
                        userVo.setAge(0);
                    }
                }
                userVo.setOrganizationId(2);
                userVo.setName(row.getCell(1).toString());
                userVo.setRoleIds("2");
                userService.insertByVo(userVo);
            }

        }
        map.put("ifSuccess",true);
        return map;
    }
    
}
