package patentsearch.bean.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/** 
 * 专利分类  
 */  
@Entity  
public class PatentCategory {  
	/**分类Id   */ 
    @Id  @GeneratedValue(strategy=GenerationType.AUTO)  
    private int id;  
    /**分类名称   */  
    @Column(length=20)
    private String name;  
    /**  添加时间 */
    @Temporal(TemporalType.TIMESTAMP)  
    private Date createTime = new Date();  
    /** * 父节点  */ 
    @ManyToOne  
    private PatentCategory parent;  
    /**  子节点  */ 
    @OneToMany(mappedBy="parent",fetch=FetchType.EAGER)
    private Set<PatentCategory> children = new HashSet<PatentCategory>();
    /* 所属用户*/
	@ManyToOne(cascade=CascadeType.REFRESH,optional=false)
	@JoinColumn(name="user_id")
	private Users user; 
	 /*拥有专利收藏信息  */ 
    @OneToMany(mappedBy="patentCategory",fetch=FetchType.EAGER)  
    private Set<PatentStoreInfo> PatentStoreInfos = new HashSet<PatentStoreInfo>();
    
    /**个性库类型   */  
    @Column
    private Integer categoryType=0;  //初始化默认为关注库
    
    
	public Integer getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(Integer categoryType) {
		this.categoryType = categoryType;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public PatentCategory getParent() {
		return parent;
	}
	public void setParent(PatentCategory parent) {
		this.parent = parent;
	}
	 
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	public Set<PatentCategory> getChildren() {
		return children;
	}
	public void setChildren(Set<PatentCategory> children) {
		this.children = children;
	}
	public Set<PatentStoreInfo> getPatentStoreInfos() {
		return PatentStoreInfos;
	}
	public void setPatentStoreInfos(Set<PatentStoreInfo> patentStoreInfos) {
		PatentStoreInfos = patentStoreInfos;
	}
	 
    
    
}