package patentsearch.bean.cndescriptionitem;

import java.util.Date;
/*
 * 中文著录项->优先权信息[多项]
 */
public class CNDescriptionItem_HISTORY {
	/*法律状态内容  [字符型]*/
	private String legal_mk;
	/* 法律状态日期  [日期型 XXXX 年 X 月 XX 日]*/
	private Date rec_date;
	public String getLegal_mk() {
		return legal_mk;
	}
	public void setLegal_mk(String legalMk) {
		legal_mk = legalMk;
	}
	public Date getRec_date() {
		return rec_date;
	}
	public void setRec_date(Date recDate) {
		rec_date = recDate;
	}
	@Override
	public String toString() {
		return "CNDescriptionItem_HISTORY [legal_mk=" + legal_mk
				+ ", rec_date=" + rec_date + "]";
	}
	
}
