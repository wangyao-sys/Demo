package com.kanghua.util;

import java.util.List;

public class PageUtils {

	private int pageNum;
	private int size;
	private int all;
	private int allPage;
	private List list;
	private int begin;
	
	public PageUtils(String n, String sizex,int all){
		if(sizex==null)
			sizex="3";
		if(n==null){
			n="1";
		}
		this.pageNum=Integer.parseInt(n);
		this.size=Integer.parseInt(sizex);
		if(this.pageNum<1){
			this.pageNum=1;
		}
		this.all=all;
		this.allPage=all/size; // 100   10   10    92/10=9..2
		this.allPage=all%size==0?allPage:allPage+1;
		if(pageNum>allPage){ pageNum=allPage;}
		this.begin=(pageNum-1)*size;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getAll() {
		return all;
	}
	public void setAll(int all) {
		this.all = all;
	}
	public int getAllPage() {
		return allPage;
	}
	public void setAllPage(int allPage) {
		this.allPage = allPage;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public int getBegin() {
		return begin;
	}
	public void setBegin(int begin) {
		this.begin = begin;
	}
	
	
}
