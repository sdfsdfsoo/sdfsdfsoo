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
 * 专利收藏信息
 */  
@Entity  
public class PatentStoreInfo  implements Serializable{  
	private static final long serialVersionUID = -2657359075950707049L;
	/**专利收藏信息Id   */ 
    @Id  @GeneratedValue(strategy=GenerationType.AUTO)  
    private int id;  
   /**专利申请号 */  
    @Column(length=15)
    private String appno;
    /** 申请日（XXXX 年 X 月 XX 日） */
	private String apd;
	/** 申请人 [字符型] [多项] */
	
	private String legalstateold;
	
	private String legalstatenew;
	@Column(length=200)
	private String yearfeedate;
	
	
	public String getYearfeedate() {
		return yearfeedate;
	}
	public void setYearfeedate(String yearfeedate) {
		this.yearfeedate = yearfeedate;
	}
	public String getLegalstateold() {
		return legalstateold;
	}
	public void setLegalstateold(String legalstateold) {
		this.legalstateold = legalstateold;
	}
	public String getLegalstatenew() {
		return legalstatenew;
	}
	public void setLegalstatenew(String legalstatenew) {
		this.legalstatenew = legalstatenew;
	}
	private String appl;
    public String getApd() {
		return apd;
	}
	public void setApd(String apd) {
		this.apd = apd;
	}
	public String getAppl() {
		return appl;
	}
	public void setAppl(String appl) {
		this.appl = appl;
	}
	/**专利名称   */  
    @Column(length=200)
    private String title;  
    /**  添加时间 */
    @Temporal(TemporalType.TIMESTAMP)  
    private Date createTime = new Date();  
    /* 所属分类  */ 
	@ManyToOne(cascade=CascadeType.REFRESH,optional=false)
	@JoinColumn(name="patentCategory_id")
	private PatentCategory patentCategory;
	  /**专利属于中国专利还是世界专利  */  
    @Column
    private Integer searchscope;  
	
	/**重要级别   */  
    @Column
	private Date importantLevel=new Date();
    
    
	public Integer getSearchscope() {
		return searchscope;
	}
	public void setSearchscope(Integer searchscope) {
		this.searchscope = searchscope;
	}
	public Date getImportantLevel() {
		return importantLevel;
	}
	public void setImportantLevel(Date importantLevel) {
		this.importantLevel = importantLevel;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAppno() {
		return appno;
	}
	public void setAppno(String appno) {
		this.appno = appno;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public PatentCategory getPatentCategory() {
		return patentCategory;
	}
	public void setPatentCategory(PatentCategory patentCategory) {
		this.patentCategory = patentCategory;
	}
 
    
}