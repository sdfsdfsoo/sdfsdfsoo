package patentsearch.bean.base;

import patentsearch.utils.base.StringUtil;

 

public class ClassNavEntity {
     /*IPC或者外观分类号 */
	public String classType;
	/*IPC或者外观分类号文字描述 */
	public String des;
	/*是否为根结点或者叶子结点 */
	public boolean hasChild;
	/* 不知道什么意思，洪亮写的*/
	public boolean menuFlag;
	/*分类号类型,0:IPC,1:ADM */
	public Integer type;
//	/*是否显示世界专利*/
//	public boolean wordFlag;
//	
//	public boolean isWordFlag() {
//		return wordFlag;
//	}
//	public void setWordFlag(boolean wordFlag) {
//		this.wordFlag = wordFlag;
//	}
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	 /*IPC或者外观分类号,格式化后的参数，可被引擎识别参数 */
	public String getClassTypePara() {
		return StringUtil.getIpcPara(this.classType);
	}
	public void setClassTypePara(String classType) {
		
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}

	public boolean isHasChild() {
		return hasChild;
	}
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}
	
	public boolean isMenuFlag() {
		return menuFlag;
	}
	public void setMenuFlag(boolean menuFlag) {
		this.menuFlag = menuFlag;
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "ClassNavEntity [classType=" + classType + ", des=" + des
				+ ", hasChild=" + hasChild + ", menuFlag=" + menuFlag + "]";
	}
	
	
}
