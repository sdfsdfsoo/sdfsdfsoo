package patentsearch.bean.base;

public class Count {
	//专利号 十三位不加点
	private String appno;
	//法律状态
	private String category;
	//申请日
	private String apd;
	//公开日
	private String pud;
	//公告日
	private String appd;
	//国省
	private String nc;
	//ipc主分类号
	private String ipcMain;
	//申请人
	private String appl;
	//发明人
	private String Inventor;
	public String getAppno() {
		return appno;
	}
	public void setAppno(String appno) {
		this.appno = appno;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getApd() {
		return apd;
	}
	public void setApd(String apd) {
		this.apd = apd;
	}
	public String getPud() {
		return pud;
	}
	public void setPud(String pud) {
		this.pud = pud;
	}
	public String getAppd() {
		return appd;
	}
	public void setAppd(String appd) {
		this.appd = appd;
	}
	public String getNc() {
		return nc;
	}
	public void setNc(String nc) {
		this.nc = nc;
	}
	public String getIpcMain() {
		return ipcMain;
	}
	public void setIpcMain(String ipcMain) {
		this.ipcMain = ipcMain;
	}
	public String getAppl() {
		return appl;
	}
	public void setAppl(String appl) {
		this.appl = appl;
	}
	public String getInventor() {
		return Inventor;
	}
	public void setInventor(String inventor) {
		Inventor = inventor;
	}
	@Override
	public String toString() {
		return "Count [Inventor=" + Inventor + ", apd=" + apd + ", appd="
				+ appd + ", appl=" + appl + ", appno=" + appno + ", category="
				+ category + ", ipcMain=" + ipcMain + ", nc=" + nc + ", pud="
				+ pud + "]";
	}
	
	
	
}
