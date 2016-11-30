package patentsearch.bean.base;
import java.io.File;
import java.io.Serializable;
/**
  Pdf实体,方便Pdf文件批量下载
 */
public class PdfEntity implements Serializable {
	 
	private String appno;
	 
	private File pdf;

	public String getAppno() {
		return appno;
	}

	public void setAppno(String appno) {
		this.appno = appno;
	}

	public File getPdf() {
		return pdf;
	}

	public void setPdf(File pdf) {
		this.pdf = pdf;
	}

	@Override
	public String toString() {
		return "PdfEntity [appno=" + appno + ", pdf=" + pdf + "]";
	}
	 
	
	 
}
