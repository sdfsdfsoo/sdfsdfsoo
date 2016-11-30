package patentsearch.bean.user;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 分析用到的数据实体
 */
@Entity
public class AnalysisBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2610355841111990528L;
	/* ID */
	@Id@GeneratedValue
	private Integer id;
	/* 申请号 */
	private Integer appno;       
	/* 申请年份 */
	private Integer apd;
	/* 公开年份 */
	private Integer pud;
	/* 授权年份 */
	private Integer appd;
	/* 国省代码 */
	@Column(length = 10)
	private String nc;
	/* 分类 */
	@Column(length = 10)
	private String ipc;
	/* 申请人 */
	@Column(length = 600)
	private String appl;
	/* 发明人*/
	@Column(length = 600)
	private String inventor;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAppno() {
		return appno;
	}
	public void setAppno(Integer appno) {
		this.appno = appno;
	}
	public Integer getApd() {
		return apd;
	}
	public void setApd(Integer apd) {
		this.apd = apd;
	}
	public Integer getPud() {
		return pud;
	}
	public void setPud(Integer pud) {
		this.pud = pud;
	}
	public Integer getAppd() {
		return appd;
	}
	public void setAppd(Integer appd) {
		this.appd = appd;
	}
	public String getNc() {
		return nc;
	}
	public void setNc(String nc) {
		this.nc = nc;
	}
	public String getIpc() {
		return ipc;
	}
	public void setIpc(String ipc) {
		this.ipc = ipc;
	}
	public String getAppl() {
		return appl;
	}
	public void setAppl(String appl) {
		this.appl = appl;
	}
	public String getInventor() {
		return inventor;
	}
	public void setInventor(String inventor) {
		this.inventor = inventor;
	}
 

}
