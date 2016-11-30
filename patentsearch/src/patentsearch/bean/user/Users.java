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
 * 前台用户实体
 */
@Entity
public class Users implements Serializable {
	private static final long serialVersionUID = 612841872283735907L;
	/* ID */
	@Id@GeneratedValue
	private Integer id;
	/* 用户名 */
	@Column(length = 20)
	private String username;
	/* 密码 */
	@Column(length = 32)
	private String password;
	/* 用户类型:1表示 院校，2表示科研，3表示企业，4表示机关，5表示个人 */
	private Integer userType;
	/* 单位名称或姓名 */
	@Column(length = 50)
	private String name;
	/* 组织机构代码 */
	@Column(length = 10)
	private String organisationCode;
	/* 身份证号码，当用户类型为个人时有效 */
	@Column(length = 18)
	private String IDNumber;
	/* 联系人姓名 */
	@Column(length = 50)
	private String contactName;
	/* 联系电话0511-12345678 */
	@Column(length = 13)
	private String contactPhone;
	/* 联系人手机号码 */
	@Column(length = 11)
	private String contactCellphone;
	/* 联系人邮箱 */
	@Column(length = 50)
	private String email;
	/* 邮政编码 */
	@Column(length = 6)
	private String postcode;
	/* 网址 */
	@Column(length = 50)
	private String website;
	/* 通讯地址 */
	@Column(length = 400)
	private String address;
	
	@Column(length = 400)
	private int  dowloadnumexcel=0;
	
	@Column(length = 400)
	private int  dowloadnumpdf=0;
	
	@Column(length = 400)
	private String level="2";
	/* 用户状态：0为未审核，1为通过审核，2为审核不通过 */
	private Short state = 0;
	/* 是否启用 */
	private Short isValid = 1;
	/*用户注册时间*/
	@Temporal(TemporalType.TIMESTAMP)
	private Date registerTime=new Date();
	/* 用户所拥有检索式 */
	@OneToMany(mappedBy = "user", cascade = { CascadeType.REMOVE,
			CascadeType.PERSIST })
	private Set<SearchFormula> searchFormulas = new HashSet<SearchFormula>();
	/* 用户所拥有检索式 */
	@OneToMany(mappedBy = "user", cascade = { CascadeType.REMOVE,
			CascadeType.PERSIST })
	private Set<PatentCategory> patentCategories = new HashSet<PatentCategory>();
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	 


	public int getDowloadnumexcel() {
		return dowloadnumexcel;
	}

	public void setDowloadnumexcel(int dowloadnumexcel) {
		this.dowloadnumexcel = dowloadnumexcel;
	}

	public int getDowloadnumpdf() {
		return dowloadnumpdf;
	}

	public void setDowloadnumpdf(int dowloadnumpdf) {
		this.dowloadnumpdf = dowloadnumpdf;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrganisationCode() {
		return organisationCode;
	}

	public void setOrganisationCode(String organisationCode) {
		this.organisationCode = organisationCode;
	}

	public String getIDNumber() {
		return IDNumber;
	}

	public void setIDNumber(String iDNumber) {
		IDNumber = iDNumber;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactCellphone() {
		return contactCellphone;
	}

	public void setContactCellphone(String contactCellphone) {
		this.contactCellphone = contactCellphone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Short getState() {
		return state;
	}

	public void setState(Short state) {
		this.state = state;
	}

	public Short getIsValid() {
		return isValid;
	}

	public void setIsValid(Short isValid) {
		this.isValid = isValid;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	
	public Set<SearchFormula> getSearchFormulas() {
		return searchFormulas;
	}

	public void setSearchFormulas(Set<SearchFormula> searchFormulas) {
		this.searchFormulas = searchFormulas;
	}
	

	public Set<PatentCategory> getPatentCategories() {
		return patentCategories;
	}

	public void setPatentCategories(Set<PatentCategory> patentCategories) {
		this.patentCategories = patentCategories;
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
		Users other = (Users) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Users [username=" + username + ", password=" + password + "]";
	}

	 

}
