package patentsearch.bean.cndescriptionitem;

/*
 * 中文著录项->图形数据信息[多项]
 */
public class CNDescriptionItem_IMAGE {
	/*卷期号  [字符型]*/
	private String vol;
	/*页数  [数值型]*/
	private Integer  page;
	public String getVol() {
		return vol;
	}
	public void setVol(String vol) {
		this.vol = vol;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((page == null) ? 0 : page.hashCode());
		result = prime * result + ((vol == null) ? 0 : vol.hashCode());
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
		CNDescriptionItem_IMAGE other = (CNDescriptionItem_IMAGE) obj;
		if (page == null) {
			if (other.page != null)
				return false;
		} else if (!page.equals(other.page))
			return false;
		if (vol == null) {
			if (other.vol != null)
				return false;
		} else if (!vol.equals(other.vol))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CNDescriptionItem_IMAGE [page=" + page + ", vol=" + vol + "]";
	}
	
	
}
