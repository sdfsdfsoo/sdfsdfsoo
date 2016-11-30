package patentsearch.bean.user;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
 
/**
 * 检索式实体
 */
@Entity
public class SearchFormula implements Serializable {
 
	private static final long serialVersionUID = 1647127399065513102L;
	/* ID */
	/* 检索序号 */
	@Id@GeneratedValue
	private Integer id;
	/* 检索式 */
	@Column(length = 200)
	private String formula;
	/* 命中次数 */
	@Column 
	private Long hits;
	/*检索式更新时间*/
	@Temporal(TemporalType.TIMESTAMP)
	private Date alterTime=new Date();
	/* 所属用户*/
	@ManyToOne(cascade=CascadeType.REFRESH,optional=false)
	@JoinColumn(name="user_id")
	private Users user;
 
	/* SID */
	@Column 
	private int itemID;
	/* 中国检索还是世界检索 */
	@Column 
	private Integer searchscope=0;
	
	
	public int getItemID() {
		return itemID;
	}


	public void setItemID(int itemID) {
		this.itemID = itemID;
	}


	public Integer getSearchscope() {
		return searchscope;
	}


	public void setSearchscope(Integer searchscope) {
		this.searchscope = searchscope;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}
 

	public String getFormula() {
		return formula;
	}


	public void setFormula(String formula) {
		this.formula = formula;
	}


	public Long getHits() {
		return hits;
	}


	public void setHits(Long hits) {
		this.hits = hits;
	}


	public Date getAlterTime() {
		return alterTime;
	}


	public void setAlterTime(Date alterTime) {
		this.alterTime = alterTime;
	}


	public Users getUser() {
		return user;
	}


	public void setUser(Users user) {
		this.user = user;
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
		SearchFormula other = (SearchFormula) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "SearchFormula [alterTime=" + alterTime + ", formula=" + formula
				+ ", hits=" + hits + ", id=" + id + "]";
	}

 
	 

}
