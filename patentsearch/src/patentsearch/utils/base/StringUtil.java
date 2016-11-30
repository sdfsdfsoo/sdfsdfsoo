 package patentsearch.utils.base;

/**
 * 字条串处理工具
 */
public class StringUtil {
	 

		/**
		 * 将专利著录项字段IPC分类号，进行格式化，输出为检索引擎可识别参数
		 */
		public static String getIpcPara(String in) {
			//System.out.println(in);
			if(in!=null&&!"".equals(in)){
				in=in.trim();
			}
			 StringBuilder strOut=new StringBuilder();
				if(in!=null&&!"".equals(in)){
					if(in.contains("-")){
						if(in.indexOf('-')==in.lastIndexOf('-')){
							return strOut.append(in.substring(0,in.indexOf('-'))).append(in.substring(in.indexOf('-')+1,in.length())).toString();
						} else {
							return strOut.append(in.substring(0,in.indexOf('-'))).append(in.substring(in.indexOf('-')+1,in.lastIndexOf('-'))).append(in.substring(in.lastIndexOf('-')+1,in.length())).toString();
						}
					}else if(in.contains("/")){
						if(in.contains("(")){
							in = in.substring(0,in.indexOf("("));
						}
							 in = in.replace("  ", " ").replace(" ", "0").replace("/", "");
							 return in;
//							 if(in.contains(" ")){
//								 while(" ".equals(in.charAt(in.indexOf(" ")+1)+"")){
//									 in=in.replace("  ", " ");
//								 }
//								   	 for(int i=0;i<(3- in.substring(in.indexOf(' ')+1,in.indexOf('/')).length());i++){
//								   		strOut.append("0");
//								   	 }
//								   	 strOut.append(in.substring(in.indexOf(' ')+1,in.indexOf('/')));
//								   	 if(in.contains("(")){
//									   	 strOut.append(in.substring(in.indexOf('/')+1, in.indexOf('(')));
//									   	 for(int i=0;i<(4-in.substring(in.indexOf('/')+1, in.indexOf('(')).length());i++){
//									   		 strOut.append("0");
//									   	 }
//									   	 return strOut.toString();
//								   	 }else{
//								   		 strOut.append(in.substring(in.indexOf('/')+1, in.length()));
//									   	 for(int i=0;i<(4-in.substring(in.indexOf('/')+1, in.length()).length());i++){
//									   		 strOut.append("0");
//									   	 }
//									   	 return strOut.toString();
//								   	 }
//							  }else{
//								  	 for(int i=0;i<(3- in.substring(4,in.indexOf('/')).length());i++){
//									   		strOut.append("0");
//									   	 }
//									   	 strOut.append(in.substring(4,in.indexOf('/')));
//									   	 if(in.contains("(")){
//										   	 strOut.append(in.substring(in.indexOf('/')+1, in.indexOf('(')));
//										   	 for(int i=0;i<(4-in.substring(in.indexOf('/')+1, in.indexOf('(')).length());i++){
//										   		 strOut.append("0");
//										   	 }
//										   	 return strOut.toString();
//									   	 }else{
//									   		 strOut.append(in.substring(in.indexOf('/')+1, in.length()));
//										   	 for(int i=0;i<(4-in.substring(in.indexOf('/')+1, in.length()).length());i++){
//										   		 strOut.append("0");
//										   	 }
//										   	 return strOut.toString();
//									   	 }
//								  
//							  }
					}
				}
			return in;

		}
		/**
		 *  分类号G06F9/30;G06F9/38   --->  G06F9/30+G06F9/38
		 * @param str
		 * @return
		 */
		public static String stringToFormula(String str) {
			//System.out.println("--+++++++++++++------"+str);
			if(str.contains(";")){
				str=str.replace(';', '+');
			}
			return str;
		}
		/**
		 * 形成仅检索某一年的检索式
		 * @param year
		 * @return
		 */
		public static String stringToFormYear(String searchFormula,int year) {
			String str="*("+year+"0101>"+year+"1231/AD)";
			if(searchFormula.contains("@")){
				str=searchFormula.substring(0,searchFormula.indexOf("@"))+str+searchFormula.substring(searchFormula.indexOf("@"),searchFormula.length());
			}else {
				str=searchFormula+str;
			}
			return str;
		}
		
		public static void main(String[] args) {
			String in="G06F                1/18(2006.01)";
			 while(" ".equals(in.charAt(in.indexOf(" ")+1)+"")){
				 in=in.replace("  ", " ");
				 //System.out.println("xxxxxxxxxxxxx"+in);
			 }
//			String ss= StringUtil.stringToFormYear("F XX (计算机/TI*系统/TI)",2004);
		}


}
