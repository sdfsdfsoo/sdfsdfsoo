package patentsearch.bean.cndescriptionitem;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import patentsearch.bean.util.xml.DateUtil;
import patentsearch.bean.util.xml.XMLUtil;
import patentsearch.service.legalstatus.LegalStatusService;
import patentsearch.utils.base.ConfigTool;
import patentsearch.utils.base.StringUtil;
import patentsearch.utils.base.WebTool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;

public class CNDescriptionItem {

	/* 申请号[字符串 8 位或 12 位] */
	private String appno;
	/* 发明公开号 [字符型] */
	private String pubnr;
	/* 公告号 [字符型] */
	private String appnr;
	/* 申请日（XXXX 年 X 月 XX 日） */
	private Date apd;
	/* 申请日（20100707） */
	private String apdText;
	private String apdValue;
	/* 公开日 [日期型 XXXX 年 X 月 XX 日] */
	private Date pud;
	/* 公开日 （20100707） */
	private String pudText;
	private String pudValue;
	/* 授权日 [日期型 XXXX 年 X 月 XX 日] */
	private Date grd;
	/* 授权公告日  */
	private Date grpd;
	/* 公告日 [日期型 XXXX 年 X 月 XX 日] */
	private Date appd;
	/* 公告日（20100707） */
	private String appdText;
	
	private String appdValue;
	/* 省市代码 [字符型 2 位] */
	private String nc;
	/* 说明书页数 [数值型] */
	private Integer pnum;
	/* 附图页数 [数值型] */
	private Integer fnum;
	/* 权利要求页数 [数值型] */
	private Integer cnum;
	/* 代理机构代码[字符型 5 位] */
	private String agency;
	/* 范畴分类号[字符型 3 位] [多项] */
	private String fieldc;
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
	private String ipcMinor;
	/* 优先权信息[多项] */
	private Set<CNDescriptionItem_PRI> pris = new HashSet<CNDescriptionItem_PRI>();
	/* 关键词 [字符型] [多项] */
	private String keyword;
	/* 申请人 [字符型] [多项] */
	private String appl;
	/* 发明人 [字符型] [多项] */
	private String inventor;
	/* 摘要 [字符型] */
	private String abstr;
	/* 主权利要求 [字符型] */
	private String claim;
	/* 审批历史[多项] */
	private Set<CNDescriptionItem_HISTORY> histories = new HashSet<CNDescriptionItem_HISTORY>();
	/* PCT 信息 */
	private CNDescriptionItem_PCT pct;
	/* 图形数据信息[多项] */
	private Set<CNDescriptionItem_IMAGE> images = new HashSet<CNDescriptionItem_IMAGE>();
	  
	/* 法律状态 */
	private CnLegalStatus cnLegalStatus;
	/* 法律状态字符串 */
	private String cnLegalStatusStr;
	/* 中国专利摘要附图URL */
	private String futuURL;
	
	private String ipcPara;

	@Resource
	LegalStatusService legalStatusService;
	
	
	
	
	
	
	
	public String getAppdValue() {
		return DateUtil.dateToValueString(this.appd);
	}


	public void setAppdValue(String appdValue) {
		this.appdValue = appdValue;
	}


	public String getIpcPara() {
		 return StringUtil.getIpcPara(this.ipcMain);
	}


	public void setIpcPara(String ipcPara) {
		this.ipcPara = ipcPara;
	}


	public String getApdValue() {
		return DateUtil.dateToValueString(this.apd);
	}


	public void setApdValue(String apdValue) {
		this.apdValue = apdValue;
	}


	public String getPudValue() {
		return DateUtil.dateToValueString(this.pud);
	}


	public void setPudValue(String pudValue) {
		this.pudValue = pudValue;
	}


	public String getCnLegalStatusStr() {
		return cnLegalStatusStr;
	}


	public void setCnLegalStatusStr(String cnLegalStatusStr) {
		this.cnLegalStatusStr = cnLegalStatusStr;
	}


	public String getAppno() {
		return appno;
	}

	
	public void setAppno(String appno) {
		this.appno = appno;
	}

	public String getAppnoWithDot() {
		return XMLUtil.getCheckAppnoWithDot(appno);
	}

	public void setAppnoWithDot(String appno) {
	}

	public String getPubnr() {
		return pubnr != null && !"".equals(pubnr)&& !"".equals(pubnr.trim()) ? pubnr : null;

	}

	public void setPubnr(String pubnr) {
		this.pubnr = pubnr;
	}

	public String getAppnr() {
		return appnr != null && !"".equals(appnr)&&!"".equals(appnr.trim() )? appnr : null;
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

	public String getApdText() {
		
		return DateUtil.dateToTextString(this.apd);
	}

	public void setApdText(String apdText) {
		this.apdText = apdText;
	}

	public Date getPud() {
		return pud;
	}

	public void setPud(Date pud) {
		this.pud = pud;
	}

	public Date getGrd() {
		return grd;
	}

	public void setGrd(Date grd) {
		this.grd = grd;
	}

	public Date getGrpd() {
		return grpd;
	}

	public void setGrpd(Date grpd) {
		this.grpd = grpd;
	}
	public String getGrpdText() {
		return DateUtil.dateToTextString(this.grpd);
	}

	public void setGrpdText(Date grpd) {
	}
	public Date getAppd() {
		return appd;
	}

	public void setAppd(Date appd) {
		this.appd = appd;
	}

	/*
	 * 省市代码信息返回经过处理再进行显示
	 */
	public String getNc() {
		// return provinceCityService.detailInfo(this.nc);
		return this.nc;
	}

	public void setNc(String nc) {
		this.nc = nc;
	}

	public Integer getPnum() {
		return pnum;
	}

	public void setPnum(Integer pnum) {
		this.pnum = pnum;
	}

	public Integer getFnum() {
		return fnum;
	}

	public void setFnum(Integer fnum) {
		this.fnum = fnum;
	}

	public Integer getCnum() {
		return cnum;
	}

	public void setCnum(Integer cnum) {
		this.cnum = cnum;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getFieldc() {
		return fieldc;
	}

	public void setFieldc(String fieldc) {
		this.fieldc = fieldc;
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

	public String getIpcMinor() {
		return ipcMinor;
	}

	public void setIpcMinor(String ipcMinor) {
		this.ipcMinor = ipcMinor;
	}

	public Set<CNDescriptionItem_PRI> getPris() {
		return pris;
	}

	public void setPris(Set<CNDescriptionItem_PRI> pris) {
		this.pris = pris;
	}

	public void addPri(CNDescriptionItem_PRI pri) {
		this.pris.add(pri);
	}

	public String getKeyword() {
		if (this.keyword != null) {
			this.keyword = this.keyword.substring(0, this.keyword.length() - 1);
		}
		return this.keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void addKeyword(String keyword) {
		if (this.keyword == null) {
			this.keyword = "";
		}
		this.keyword = this.keyword + keyword + ",";

	}

	public String getAppl() {
		/*if (this.appl != null && !"".equals(this.appl)) {
			this.appl = this.appl.substring(0, this.appl.length() - 1);
		}*/
		return appl;
	}

	public void setAppl(String appl) {
		this.appl = appl;
	}

	public void addAppl(String appl) {
		if (this.appl == null) {
			this.appl = "";
		}
		this.appl = this.appl + appl + ",";

	}

	public String getInventor() {
		/*if (this.inventor != null && !"".equals(this.inventor)) {
			this.inventor = this.inventor.substring(0,
					this.inventor.length() - 1);
		}*/

		return inventor;
	}

	public void setInventor(String inventor) {
		this.inventor = inventor;
	}

	public void addInventor(String inventor) {
		if (this.inventor == null) {
			this.inventor = "";
		}
		this.inventor = this.inventor + inventor + ",";
	}

	public String getAbstr() {
		return abstr;
	}

	public void setAbstr(String abstr) {
		this.abstr = abstr;
	}

	public String getClaim() {
		return claim;
	}

	public void setClaim(String claim) {
		this.claim = claim;
	}

	public Set<CNDescriptionItem_HISTORY> getHistories() {
		return histories;
	}

	public void setHistories(Set<CNDescriptionItem_HISTORY> histories) {
		this.histories = histories;
	}

	public void addHistory(CNDescriptionItem_HISTORY history) {
		this.histories.add(history);
	}

	public CNDescriptionItem_PCT getPct() {
		return pct;
	}

	public void setPct(CNDescriptionItem_PCT pct) {
		this.pct = pct;
	}

	public Set<CNDescriptionItem_IMAGE> getImages() {
		return images;
	}

	public void setImages(Set<CNDescriptionItem_IMAGE> images) {
		this.images = images;
	}

	public void addImage(CNDescriptionItem_IMAGE image) {
		this.images.add(image);
	}

	public String getPudText() {
		return DateUtil.dateToTextString(this.pud);
	}

	public void setPudText(String pudText) {
		this.pudText = pudText;
	}

	public String getAppdText() {
		return appdText;
	}

	public void setAppdText(String appdText) {
		this.appdText = appdText;
	}

	public String getPatentType() {
		Integer patentTypeBit = null;
		String  patentType=null;
		try {
			patentTypeBit = Integer.parseInt(XMLUtil.formatAppno(this.appno)
					.charAt(4)
					+ "");
		} catch (Exception e) {

		}
		if (patentTypeBit != null) {

			switch (patentTypeBit) {
			case 1:
				patentType="发明";
				break;

			case 2:
				patentType="实用新型";
				break;
			case 3:
				patentType="外观设计";
				break;
			case 8:
				patentType="PCT发明专利申请";
				break;
			case 9:
				patentType="PCT实用新型专利申请";
				break;
			}
		}
		return patentType;
	}

	public void setPatentTypet(String appdText) {
	}

	public CnLegalStatus getCnLegalStatus() {
		/*
		 * System.out.println("legalStatusService:"+legalStatusService.getCnLegalStatusByAppnp
		 * (this.appno)); return
		 * legalStatusService.getCnLegalStatusByAppnp(this.appno);
		 */
		return null;
	}

	public void setCnLegalStatus(CnLegalStatus cnLegalStatus) {
		this.cnLegalStatus = cnLegalStatus;
	}

	@Override
	public String toString() {
		return "CNDescriptionItem [APNNO=" + appno + ", PUBNR=" + pubnr
				+ ", APPNR=" + appnr + ", APD=" + apd + ", PUD=" + pud
				+ ", GRD=" + grd + ", GRPD=" + grpd + ", APPD=" + appd
				+ ", NC=" + nc + ", PNUM=" + pnum + ", FNUM=" + fnum
				+ ", CNUM=" + cnum + ", AGENCY=" + agency + ", FIELDC="
				+ fieldc + ", ADDRESS=" + address + ", AGENT=" + agent
				+ ", TITLE=" + title + ", ZIP=" + zip + ", ipcMinor=" + ipcMain
				+ ", ipcMinor=" + ipcMinor + ", PRI=" + pris + ", KEYWORD="
				+ this.getKeyword() + ", APPL=" + this.getAppl()
				+ ", INVENTOR=" + this.getInventor() + ", ABSTR=" + abstr
				+ ", CLAIM=" + claim + ", HISTORY=" + histories + ", PCT="
				+ pct + ", IMAGE=" + images + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appno == null) ? 0 : appno.hashCode());
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
		CNDescriptionItem other = (CNDescriptionItem) obj;
		if (appno == null) {
			if (other.appno != null)
				return false;
		} else if (!appno.equals(other.appno))
			return false;
		return true;
	}

	/*
	 * 申请日格式化成字符串
	 */
	public String getApdString() {
		return this.apd != null ? DateUtil.dateToTextString(this.apd) : "无";
	}

	/*
	 * 公开日格式化成字符串
	 */
	public String getPudString() {
		return this.pud != null ? DateUtil.dateToTextString(this.pud) : "无";
	}

	/*
	 * 授权公告日格式化成字符串
	 */
	public String getGrpdString() {
		return this.grpd != null ? DateUtil.dateToTextString(this.grpd) : "无";
	}

	public String getFutuURL() {
		if(this.getPatentType()=="外观设计"){  
			String JDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";// SQL数据库引擎
			String connectDB = "jdbc:sqlserver://11.0.0.25:1433; DatabaseName=DB_EPDS";// 数据源
			try {
				Class.forName(JDriver);// 加载数据库引擎，返回给定字符串名的类
			} catch (ClassNotFoundException e) {
				// e.printStackTrace();
				System.out.println("加载数据库引擎失败");
				System.exit(0);
			}
			try {
				String user = "sa";
				String password = "123456";
				Connection con = DriverManager.getConnection(connectDB, user,password);
				System.out.println("连接数据库成功");
				Statement stmt = con.createStatement(); // 创建SQL命令对象
				String strAppd="";
				if(this.appd!=null){
					 strAppd=DateUtil.dateToValueString(this.appd);
				}
				ResultSet rs = stmt.executeQuery("SELECT TOP 1  JUANQIH FROM  GK_JQHPZB where GONGKAIGGR='"+strAppd+ "'");
				String juanqi =null;
				if(rs.next()){
					juanqi=rs.getString("JUANQIH");
					juanqi=juanqi.replace("-", "");
					String ss=ConfigTool.getValue("fulu_pre_waiguan")+juanqi+"/"+XMLUtil.formatAppno(this.appno)+"/000001.jpg";
					if(WebTool.urlIsValid(ss)){
						return ss;
					}
					String appnoWithDot=XMLUtil.getCheckAppnoWithDot(this.appno);
					appnoWithDot=appnoWithDot.replace(".", "");
					ss=ConfigTool.getValue("fulu_pre_waiguan")+juanqi+"/"+appnoWithDot+"/000001.jpg";
					if(WebTool.urlIsValid(ss)){
						return ss;
					}
					System.out.println("外观："+ss);
				}else{
					System.out.println("外观：");
					return ConfigTool.getValue("futu_friendly");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}// 连接数据库对象
		}else{
			String urlStr = XMLUtil.getFuTuByAppno(this.appno);
			String urlStr0= urlStr.substring(0, urlStr.length()-4)+".jpg";
			if (WebTool.urlIsValid(urlStr)) {
				return urlStr;
			}else if (WebTool.urlIsValid(urlStr0)) {
				return urlStr0;
			} else {
				//return ConfigTool.getValue("futu_friendly");
				return urlStr;
			}
		}
		return null;
	}

	public void setFutuURL(String futuURL) {
		this.futuURL = futuURL;
	}

}
