package patentsearch.bean.base;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Patent implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id;
	/* 申请号[字符串 8 位或 12 位] */
	private String appno;
	/* 发明公开号 [字符型] */
	private String pubnr;
	/* 公告号 [字符型] */
	private String appnr;
	/* 申请日（XXXX 年 X 月 XX 日） */
	private Date apd;
	/* 申请日（20100707） */
	//private String apdText;
	/* 公开日 [日期型 XXXX 年 X 月 XX 日] */
	private Date pud;
	/* 公开日 （20100707） */
	//private String pudText;
	/* 授权日 [日期型 XXXX 年 X 月 XX 日] */
	//private Date grd;
	/* 授权公告日  */
	private Date grpd;
	/* 公告日 [日期型 XXXX 年 X 月 XX 日] */
	private Date appd;
	/* 公告日（20100707） */
	//private String appdText;
	/* 省市代码 [字符型 2 位] */
	private String nc;
	/* 说明书页数 [数值型] */
	//private Integer pnum;
	/* 附图页数 [数值型] */
	//private Integer fnum;
	/* 权利要求页数 [数值型] */
	//private Integer cnum;
	/* 代理机构代码[字符型 5 位] */
	private String agency;
	/* 范畴分类号[字符型 3 位] [多项] */
	//private String fieldc;
	/* 联系地址 [字符型] */
	private String address;
	/* 代理人 [字符型] */
	private String agent;
	/* 发明名称 [字符型] */
	private String title;
	/* 邮编 [字符型] */
	private String zip;
	/* 主 IPC 号 [字符型] */
	private String ipcMain;
	/* IPC [字符型] [多项] */
	private String ipcChild;
	/* 优先权信息[多项] */
	//private Set<CNDescriptionItem_PRI> pris = new HashSet<CNDescriptionItem_PRI>();
	/* 关键词 [字符型] [多项] */
	//private String keyword;
	/* 申请人 [字符型] [多项] */
	private String appl;
	/* 发明人 [字符型] [多项] */
	private String inventor;
	
	private String areaId;
	
	/* 摘要 [字符型] */
	//private String abstr;
	/* 主权利要求 [字符型] */
	//private String claim;
	/* 审批历史[多项] */
	//private Set<CNDescriptionItem_HISTORY> histories = new HashSet<CNDescriptionItem_HISTORY>();
	/* PCT 信息 */
	//private CNDescriptionItem_PCT pct;
	/* 图形数据信息[多项] */
	//private Set<CNDescriptionItem_IMAGE> images = new HashSet<CNDescriptionItem_IMAGE>();
	  
	/* 法律状态 */
	//private CnLegalStatus cnLegalStatus;
	/* 法律状态字符串 */
	private String legalStatus;
	private String patentType;
	private String yearPayStatus;
	private long rightManId;
	private String rightMan;
	private String rightManAddress;
	private Date endLine;
	private String reasonCode;
	private String delFlag;
	/* 中国专利摘要附图URL */
	//private String futuURL;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAppno() {
		return appno;
	}
	public void setAppno(String appno) {
		this.appno = appno;
	}
	public String getPubnr() {
		return pubnr;
	}
	public void setPubnr(String pubnr) {
		this.pubnr = pubnr;
	}
	public String getAppnr() {
		return appnr;
	}
	public void setAppnr(String appnr) {
		this.appnr = appnr;
	}
	public Date getApd() {
		return apd;
	}
	public void setApd(Date apd) {
		this.apd = apd;
	}
	public Date getPud() {
		return pud;
	}
	public void setPud(Date pud) {
		this.pud = pud;
	}
	public Date getGrpd() {
		return grpd;
	}
	public void setGrpd(Date grpd) {
		this.grpd = grpd;
	}
	public String getNc() {
		return nc;
	}
	public void setNc(String nc) {
		this.nc = nc;
	}
	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getIpcMain() {
		return ipcMain;
	}
	public void setIpcMain(String ipcMain) {
		this.ipcMain = ipcMain;
	}
	public String getIpcChild() {
		return ipcChild;
	}
	public void setIpcChild(String ipcChild) {
		this.ipcChild = ipcChild;
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
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getLegalStatus() {
		return legalStatus;
	}
	public void setLegalStatus(String legalStatus) {
		this.legalStatus = legalStatus;
	}
	public String getPatentType() {
		return patentType;
	}
	public void setPatentType(String patentType) {
		this.patentType = patentType;
	}
	public String getYearPayStatus() {
		return yearPayStatus;
	}
	public void setYearPayStatus(String yearPayStatus) {
		this.yearPayStatus = yearPayStatus;
	}
	public long getRightManId() {
		return rightManId;
	}
	public void setRightManId(long rightManId) {
		this.rightManId = rightManId;
	}
	public String getRightMan() {
		return rightMan;
	}
	public void setRightMan(String rightMan) {
		this.rightMan = rightMan;
	}
	public String getRightManAddress() {
		return rightManAddress;
	}
	public void setRightManAddress(String rightManAddress) {
		this.rightManAddress = rightManAddress;
	}
	public Date getEndLine() {
		return endLine;
	}
	public void setEndLine(Date endLine) {
		this.endLine = endLine;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public Date getAppd() {
		return appd;
	}
	public void setAppd(Date appd) {
		this.appd = appd;
	}

}
