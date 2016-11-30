package patentsearch.bean.cndescriptionitem;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import patentsearch.bean.util.xml.DateUtil;
import patentsearch.bean.util.xml.XMLUtil;
import patentsearch.utils.base.ConfigTool;
import patentsearch.utils.base.WebTool;

   /* 世界专利著录项对象*/
public class EnDescriptionItem {

	/* 专利名称 */
	private String title;
	/*公告公开号 [字符型] */
	private String pubnr;
	/*原始公告公开号 [字符型] */
	private String pubnrOriginal;
	/* 申请号[字符串 8 位或 12 位] */
	private String appno;
	/* 原始申请号[字符串 8 位或 12 位] */
	private String originalAppno;
	/* 公开日 [日期型 XXXX 年 X 月 XX 日] */
	private Date pud;
	/* 公开日 （20100707） */
	private String pudText;
	/* 申请日（XXXX 年 X 月 XX 日） */
	private Date apd;
	/* 申请日（20100707） */
	private String apdText;
	/* 申请人 [字符型] [多项] */
	private String appl;
	/* 发明人 [字符型] [多项] */
	private String inventor;
	/* 优先权信息[多项] */
	private String pris ;
	/*国际分类号*/
	private String interIpc ;
	/*欧洲分类号*/
	private String euroIpc ;
    /*引用文献*/
	private String references ;
	/* 专利摘要 */
	private String abs;
	/* 主 IPC 号 [字符型] */
	private String ipcMain;
	/* IPC [字符型] [多项] */
	private String ipcMinor;
	/* 国外专利摘要附图URL */
	private String futuURL;
	
	
	public String getPubnrOriginal() {
		return pubnrOriginal;
	}
	public void setPubnrOriginal(String pubnrOriginal) {
		this.pubnrOriginal = pubnrOriginal;
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
	public String getOriginalAppno() {
		return originalAppno;
	}
	public void setOriginalAppno(String originalAppno) {
		this.originalAppno = originalAppno;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPubnr() {
		return pubnr;
	}
	public void setPubnr(String pubnr) {
		this.pubnr = pubnr;
	}
	public String getAppno() {
		return appno;
	}
	public void setAppno(String appno) {
		this.appno = appno;
	}
	public Date getPud() {
		return pud;
	}
	public void setPud(Date pud) {
		this.pud = pud;
	}
	public String getPudText() {
		return pudText;
	}
	public void setPudText(String pudText) {
		this.pudText = pudText;
	}
	public Date getApd() {
		return apd;
	}
	public void setApd(Date apd) {
		this.apd = apd;
	}
	public String getApdText() {
		return apdText;
	}
	public void setApdText(String apdText) {
		this.apdText = apdText;
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
	public String getPris() {
		return pris;
	}
	public void setPris(String pris) {
		this.pris = pris;
	}
	public String getInterIpc() {
		return interIpc;
	}
	public void setInterIpc(String interIpc) {
		this.interIpc = interIpc;
	}
	public String getEuroIpc() {
		return euroIpc;
	}
	public void setEuroIpc(String euroIpc) {
		this.euroIpc = euroIpc;
	}
	public String getReferences() {
		return references;
	}
	public void setReferences(String references) {
		this.references = references;
	}
	public String getAbs() {
		return abs;
	}
	public void setAbs(String abs) {
		this.abs = abs;
	}
    public String getFutuURL() {
		
		String urlStr = XMLUtil.getFuTuByPubnr(this.pubnr,this.pudText);
		 
		if (WebTool.urlIsValid(urlStr)) {
			return urlStr;
		} else {
			return ConfigTool.getValue("futu_friendly");

		}

	}

	public void setFutuURL(String futuURL) {
		this.futuURL = futuURL;
	}
	@Override
	public String toString() {
		return "EnDescriptionItem [abs=" + abs.substring(0,5) + ", apd=" + apd + ", apdText="
				+ apdText + ", appl=" + appl + ", appno=" + appno
				+ ", euroIpc=" + euroIpc + ", futuURL=" + futuURL
				+ ", interIpc=" + interIpc + ", inventor=" + inventor
				+ ", ipcMain=" + ipcMain + ", ipcMinor=" + ipcMinor
				+ ", originalAppno=" + originalAppno + ", pris=" + pris
				+ ", pubnr=" + pubnr + ", pubnrOriginal=" + pubnrOriginal
				+ ", pud=" + pud + ", pudText=" + pudText + ", references="
				+ references + ", title=" + title + "]";
	}



	

}
