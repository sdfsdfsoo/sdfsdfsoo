package patentsearch.bean.base;

import java.util.List;

import patentsearch.utils.base.WebTool;


public class PageView<T> {

	/*分页数据*/
	private List<T> records;
	/*页码索引*/
	private PageIndex pageindex;
	/*总页数*/
	private int totalpage = 1;
	/*总记录数*/
	private int totalrecords;
	/*当前页码*/
	private int currentpage = 1;
	/*每页显示记录数*/
	private int maxresult = 12;
	
	public PageView(int maxresult,int currentpage){
		this.maxresult = maxresult;
		this.currentpage = currentpage;
		
		
	}
	
	public List<T> getRecords() {
		return records;
	}
	public void setRecords(List<T> records) {
		this.records = records;
	}
	public PageIndex getPageindex() {
		return pageindex;
	}
	
	public long getTotalpage() {
		return totalpage;
	}
	
	public long getTotalrecords() {
		return totalrecords;
	}
	public void setTotalrecords(int totalrecords) {
		this.totalrecords = totalrecords;
		this.totalpage = totalrecords%this.maxresult==0?totalrecords/this.maxresult:totalrecords/this.maxresult+1;
		this.pageindex = WebTool.getPageIndex(this.currentpage, this.totalpage);
	}
	public int getCurrentpage() {
		return currentpage;
	}

	public int getMaxresult() {
		return maxresult;
	}
	public void setMaxresult(int maxresult) {
		this.maxresult = maxresult;
	}
	
	
}
