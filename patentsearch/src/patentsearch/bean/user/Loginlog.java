package patentsearch.bean.user;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * 前台用户实体
 * 
 */
@Entity
public class Loginlog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 612841872983735907L;

	/* 登陆账号 */
	@Id@GeneratedValue
	private int id;

	@Temporal(TemporalType.DATE)
	private Date addtime;
	
	@Column(length = 20)
	private String username;
	
	@Column(length = 20)
	private String name;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
