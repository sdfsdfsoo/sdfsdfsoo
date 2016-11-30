package patentsearch.bean.cndescriptionitem;

import java.util.Date;

import patentsearch.bean.util.xml.DateUtil;
/*
 * 中文著录项->优先权信息[多项]
 */
public class CNDescriptionItem_PRI {
	/*优先权国  [字符型 2 位]*/
	private String co;
	/*优先权号  [字符型]*/
	private String nr;
	/* [优先权日期  [日期型 XXXX 年 X 月 XX 日]*/
	private Date date;
	public String getCo() {
		return co;
	}
	public void setCo(String co) {
		this.co = co;
	}
	public String getNr() {
		return nr;
	}
	public void setNr(String nr) {
		this.nr = nr;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDateString() {
		return DateUtil.dateToTextString(this.date);
	}
	public void setDateString(String in) {
	}
	@Override
	public String toString() {
		return "CNDescriptionItem_PRI [co=" + co + ", date=" + date + ", nr="
				+ nr + "]";
	}
	
	
}
