package patentsearch.utils.base;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import patentsearch.bean.base.PageIndex;
import patentsearch.bean.user.Users;

public class WebTool {

	public static PageIndex getPageIndex(int currentpage,int totalpage){
		PageIndex pageIndex = new PageIndex();
		if(currentpage-4>=1){
			pageIndex.setFirstindex(currentpage-4);
			if(currentpage+5>totalpage){
				pageIndex.setLastindex(totalpage);
			}else{
				pageIndex.setLastindex(currentpage+5);
			}
		}else{
			pageIndex.setFirstindex(1);
			pageIndex.setLastindex(10>totalpage?totalpage:10);
		}
		return pageIndex;
	}
	/**
	 * 添加cookie
	 * @param response 连接浏览器的对象
	 * @param cookiename
	 * @param value cookie的值
	 * @param maxAge cookie的有效期,以秒为单位
	 */
	public static void addCookie(HttpServletResponse response,String cookiename,String value,int maxAge){
		Cookie cookie = new Cookie(cookiename, value);
		cookie.setPath("/");//表示对根目录下的所有路径都能访问该cookie
		if(maxAge > 0){
			cookie.setMaxAge(maxAge);
		}
		response.addCookie(cookie);
	}
	/**
	 * 通过cookie的名字获取cookie的值
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieByName(HttpServletRequest request,String cookieName){
		Map<String,Cookie> cookiemap = readCookieMap(request);
		if(cookiemap.containsKey(cookieName)){
			return cookiemap.get(cookieName).getValue();
		}else{
			return null;
		}
	}
	//获取所有的cookie，并用cookie的名称作为键值
	protected static Map<String,Cookie> readCookieMap(HttpServletRequest request){
		Map<String,Cookie> cookiemap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for(Cookie cookie : cookies){
				cookiemap.put(cookie.getName(), cookie);
			}
		}
		return cookiemap;
	}
	/**
	 * 获取登录用户
	 */
	 public static Users getLoginedUser(HttpServletRequest request){
		return (Users) request.getSession().getAttribute("user");
	}
	/*public static Master getLoginedMaster(HttpServletRequest request) {
		return (Master) request.getSession().getAttribute("master");
	} */
	 /*
	  * 这里测试内部URL地址速度很快，但是测试外部URL地址速度非常慢
	  */
	public static boolean urlIsValid(String urlStr) {
//System.out.println("resultString="+urlStr);
		try {   
			   if(urlStr.contains("112.83.69.145")){
				   urlStr=urlStr.replaceAll("112.83.69.145", "11.0.0.26");
			   }
//System.out.println("urlStr="+urlStr);
			   URL url=new URL(urlStr); 
	           HttpURLConnection   httpConnection   =   (HttpURLConnection)url.openConnection(); 
	           httpConnection.setConnectTimeout(1000);
	            int   responseCode=httpConnection.getResponseCode(); 
	            //System.out.println("responseCode:"+responseCode);
	            if(responseCode==200||responseCode==401) { 
	            	//System.out.println(urlStr+"有效");
	            	 return true;
	            }        
	        } catch (Exception e) {  
	        	  //System.out.println(urlStr+"无效");
	            //e.printStackTrace();   
	        } 
	      
		return false;
	}
}
