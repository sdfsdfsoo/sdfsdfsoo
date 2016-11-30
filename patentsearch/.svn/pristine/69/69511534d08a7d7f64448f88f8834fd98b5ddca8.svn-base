package patentsearch.bean.base;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
/**
  法律状态一览表
 */
@Entity
public class LegalStatusDetail implements Serializable {
	private static final long serialVersionUID = 2230191844092955392L;
	/* 主键id */
	@Id@GeneratedValue
	private Integer id;
	/* 专利申请号*/
	@Transient 
	private String appno;
	/* 状态代码*/
	@Column(length = 6)
	private String code;
	/* 法律状态*/
	@Column(length = 100)
	private String legalStatus;
	/* 法律状态信息*/
	@Column(length = 200)
	private String legalStatusInfo;
	/* 备注*/
	@Column(length = 200)
	private String comment;
	/* 分类*/
	@Column(length = 20)
	private String category;
	/* 父类编码*/
	@Column(length = 6)
	private String parentCode;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getAppno() {
		return appno;
	}
	public void setAppno(String appno) {
		this.appno = appno;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLegalStatus() {
		return legalStatus;
	}
	public void setLegalStatus(String legalStatus) {
		this.legalStatus = legalStatus;
	}
	public String getLegalStatusInfo() {
		return legalStatusInfo;
	}
	public void setLegalStatusInfo(String legalStatusInfo) {
		this.legalStatusInfo = legalStatusInfo;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LegalStatusDetail other = (LegalStatusDetail) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "LegalStatusDetail [appno=" + appno + ", category=" + category
				+ ", code=" + code + ", comment=" + comment + ", legalStatus="
				+ legalStatus + ", legalStatusInfo=" + legalStatusInfo + "]";
	}
	
	 
}
