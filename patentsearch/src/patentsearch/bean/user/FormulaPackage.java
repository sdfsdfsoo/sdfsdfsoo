package patentsearch.bean.user;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
 
/**
 * 检索式包实体
 */
@Entity
public class FormulaPackage implements Serializable {
 
	private static final long serialVersionUID = 1L;
	/* ID */
	/* 检索序号 */
	@Id@GeneratedValue
	private Integer id;
	/* 检索式 */
	@Column(length = 200)
	private String path;
	
	@Column(length = 200)
	private String savename;
	
	@Column(length = 200)
	private String realname;
	
	@Column(length = 200)
	private int userid;
	
	@Column(length = 200)
	@Temporal(TemporalType.TIMESTAMP)
	private Date addtime;
	
	@Column(length = 200)
	private String length;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSavename() {
		return savename;
	}

	public void setSavename(String savename) {
		this.savename = savename;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	

	

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}
	

 
	 

}
