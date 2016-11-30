package patentsearch.bean.base;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
/**
  国省代码一览表
 */
@Entity
public class ProvinceCity implements Serializable {
	 
	private static final long serialVersionUID = -6397795176943308363L;
	/* 主键id */
	@Id
	@GeneratedValue
	private Integer id;
	/* 国省代码 */
	@Column(length = 2)
	private String code;
	/* 国省名称*/
	@Column(length = 50)
	private String name;
	/* 国省拼音*/
	@Column(length = 20)
	private String pinyin;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
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
		ProvinceCity other = (ProvinceCity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ProvinceCity [code=" + code + ", name=" + name + "]";
	}
	
	 
}
