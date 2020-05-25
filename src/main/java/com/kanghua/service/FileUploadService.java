package com.kanghua.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.kanghua.commons.result.PageInfo;
import com.kanghua.model.FileUpload;

public interface FileUploadService extends IService<FileUpload>{
	void selectFileDownPage(PageInfo pageInfo);
	void selectFileDownPage2(PageInfo pageInfo);
	void selectFileDownPage3(PageInfo pageInfo);
	void selectFileself(PageInfo pageInfo);
	List<FileUpload> selectfile();
}
