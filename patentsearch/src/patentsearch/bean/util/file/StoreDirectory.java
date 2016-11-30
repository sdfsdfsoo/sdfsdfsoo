package patentsearch.bean.util.file;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
/*
 * 用户收藏分类目录信息
 */
public class StoreDirectory implements Serializable{
 
	private static final long serialVersionUID = 6054591960268755863L;

	/* 主键id */
	private Integer mid;
	
	/* id*/
	private String id;
	/* 代理机构名称*/
    private String parent;
	/* 代理机构名称*/
	private String text;
	/* 代理机构名称*/
	private Integer print=0;
	
	/*子节点个数*/
	private int leave=0;
	
	/* 用户*/
	private Integer userId;
	/* 用户收藏分类目录时间*/
	private Date time;
	
	
	
 public int getLeave() {
		return leave;
	}

	public void setLeave(int leave) {
		this.leave = leave;
	}

public StoreDirectory() {
		super();
		this.print = 0;
	}

public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

public StoreDirectory(String text,String parent)
 {
  this.text = text;
  this.parent = parent;
  this.print = 0;
 }
 


 public StoreDirectory(String id, String text, String parent) {
	super();
	this.id = id;
	this.parent = parent;
	this.text = text;
	this.print = 0;
}



public Integer getMid() {
	return mid;
}



public void setMid(Integer mid) {
	this.mid = mid;
}



public String getId() {
  return id;
 }
 public void setId(String id) {
  this.id = id;
 }

 public String getText() {
	return text;
}



public void setText(String text) {
	this.text = text;
}



public String getParent() {
  return parent;
 }
 public void setParent(String parent) {
  this.parent = parent;
 }
 public int getPrint() {
  return print;
 }
 public void setPrint(int print) {
  this.print = print;
 }
}