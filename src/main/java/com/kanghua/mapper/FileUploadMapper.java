package com.kanghua.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.kanghua.model.FileUpload;
//表数据库控制层接口
public interface FileUploadMapper extends BaseMapper<FileUpload>{
    List<FileUpload> selectfile();
    List<Map<String, Object>> selectFileDownPage(Pagination page, Map<String, Object> params);
    List<FileUpload> selectFileUpload(int index,int size);
    List<Map<String, Object>> selectFileDownPage2(Pagination page, Map<String, Object> params);
    List<Map<String, Object>> selectFileDownPage3(Pagination page, Map<String, Object> params);
    List<Map<String, Object>> selectFileself(Pagination page, Map<String, Object> params);
}
