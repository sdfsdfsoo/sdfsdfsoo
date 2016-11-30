package patentsearch.web.action.front.user;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import patentsearch.bean.user.Loginlog;
import patentsearch.bean.user.Users;
import patentsearch.service.user.LoginlogService;
import patentsearch.service.user.UserService;
import patentsearch.utils.base.CheckCode;
import patentsearch.utils.base.MD5;
import patentsearch.utils.base.WebTool;
import patentsearch.web.action.base.BaseAction;

import com.opensymphony.xwork2.ActionContext;

/**
 * 用户操作action   user_*
 * 
 */
@Controller
@Scope("prototype")
public class UserAction extends BaseAction {
	@Resource
	UserService userService;
	
	@Resource
	LoginlogService loginlogService;
	 
	/* 用户实体 */
	private Users user;
	private String checkCode;
	private String companyName;

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	/**
	 * 登录界面
	 */
	public String loginUI() {
		if(companyName!=null&&!"".equals(companyName.trim())){
			//验证公司名，并返回用户名和密码
			Users user = userService.checkCompany(companyName);
			if(user==null){
				Users user1 = new Users();
				user1.setIsValid((short)1);
				user1.setName(companyName);
				user1.setUsername(companyName);
				user1.setPassword("pingtai123456");
				userService.save(user1);
				user = userService.checkCompany(companyName);
			}
			
			ServletActionContext.getRequest().getSession()
					.setAttribute("user", user);
			ActionContext.getContext().put("message",
					"欢迎[" + user.getUsername() + "]登录成功");
			ActionContext.getContext().put("urladdress",
					"/front/search/table_smartSearchUI");
			return "loginsuccess";
		}
		return "loginUI";
	}
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * 注册页面
	 */
	public String registerUI() {
		if(user!=null){
			ServletActionContext.getRequest().setAttribute("regUserName",user.getUsername());
			ServletActionContext.getRequest().setAttribute("regName",user.getName());
			ServletActionContext.getRequest().setAttribute("regPhone",user.getContactPhone());
			ServletActionContext.getRequest().setAttribute("regCellPhone",user.getContactCellphone());
			ServletActionContext.getRequest().setAttribute("regAddress",user.getAddress());
			ServletActionContext.getRequest().setAttribute("regEmail",user.getEmail());
			ServletActionContext.getRequest().setAttribute("regUserType",user.getUserType());
		}
		return "registerUI";
	}
	/**
	 * 企业在线页面
	 */
	public String profileUI() {
		return "profileUI";
	}
	/**
	 * 向用户登录页面输出验证码
	 */
	public void generateCheckCode() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
		response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expire", 0);
		CheckCode checkCode = new CheckCode();
		try {
			checkCode.getRandcode(ServletActionContext.getRequest(), response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 输出图片方法

	}
	/**
	 * 用户登录验证
	 */
	public String check() {
		ActionContext.getContext().put("urladdress", "/front/user/user_loginUI");
		// 获取session中的验证字符串
		String sessionCheckCode = ((String) ServletActionContext.getRequest()
				.getSession().getAttribute(CheckCode.RANDOMCODEKEY))
				.toUpperCase();
		if (!sessionCheckCode.equals(this.checkCode.toUpperCase())) {
			ActionContext.getContext().put("message", "验证码错误");
			return "message";
		}
		if (user.getUsername().trim() != null
				&& !"".equals(this.user.getUsername().trim())) {
			 
			Users loginUser = userService.find(user.getUsername(), user
					.getPassword());
			
			if (loginUser != null) {
				

				Loginlog loginlog = new Loginlog();
				loginlog.setAddtime(new Date());
				loginlog.setName(loginUser.getName());
				loginlog.setUsername(loginUser.getUsername());
				loginlogService.save(loginlog);
				
				if (loginUser.getIsValid() == 0) {
					ActionContext.getContext().put("message", "账号:"+user.getUsername()+"已经被停用");
					return "message";
				}
				if (loginUser.getState() == 0) {
					ActionContext.getContext().put("message", "您的账号审核中，请稍候再登陆");
					ActionContext.getContext().put("urladdress",
							"/front/user/user_loginUI");
					return "message";
				} else if (loginUser.getState() == 1) {
					// 如果查询到的用户不为空，则将登陆用户的信息存入session中
					ServletActionContext.getRequest().getSession()
							.setAttribute("user", loginUser);
					ActionContext.getContext().put("message",
							"欢迎[" + loginUser.getUsername() + "]登录成功");
					ActionContext.getContext().put("urladdress",
							"/front/search/table_smartSearchUI");
					return "loginsuccess";
				} else if (loginUser.getState() == 2) {
					ServletActionContext.getRequest().getSession()
							.setAttribute("user", loginUser);
					ActionContext.getContext().put(
							"message",
							"您的账号未通过审核，请修改相关资料!");
					ActionContext.getContext().put(
							"urladdress",
							"/front/user/user_editRegisterUI?user.username="
									+ loginUser.getUsername());
					return "message";
				}

			} else {
				ActionContext.getContext().put("message", "登录名或密码错误");
			}
		} else {
			ActionContext.getContext().put("message", "登录名不能为空");
		} 
		return "message";

	}
	
	public String editRegisterUI(){
		user= WebTool.getLoginedUser(ServletActionContext.getRequest());
		ServletActionContext.getRequest().setAttribute("user",user);
		return "editRegisterUI";
	}
	
	/**
	 * 处理用户注册
	 */
	@SuppressWarnings("unchecked")
	public String register() {
		// 对密码MD5加密
		user.setPassword(MD5.MD5Encode(user.getPassword()));
		try{	
			userService.save(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ActionContext.getContext().put("message", "抱歉，注册失败，请重新注册");
			ActionContext.getContext().put("urladdress", "/front/user/user_registerUI");
			e.printStackTrace();
			return "message";
		} 
		ActionContext.getContext().put("message", "恭喜您，注册成功");
		ActionContext.getContext().put("urladdress", "/front/user/user_loginUI");
		return "message";
	}
	/**
	 * 检查用户名是否存在
	 * 存在返回fail，不在在返回success
	 */
	public void checkByUsername() {
		String result = null;
		if (userService.checkByUsername(this.user.getUsername())) {
			//用户名存在
			Users user = userService.selectByUsername(this.user.getUsername());
			if(user.getPassword().equals("pingtai123456")){
				userService.delete(user);
				result = "success";
			}else{
				result = "fail";
			}
		} else {
			//用户名不存在
			result = "success";
		}
		try {
			ServletActionContext.getResponse().getWriter().write(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	/**
	 * 判断组织机构代码代码是否已经存在
	 */
	public void checkByOrganisationCode() {
		/*String ajaxMessage = null;
		if (userService
				.checkByOrganisationCode(this.user.getOrganisationCode())) {
			// 如果用户名存在
			ajaxMessage = "fail";
		} else {
			ajaxMessage = "success";
		}
		try {
			ServletActionContext.getResponse().getWriter().write(ajaxMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	/**
	 * 判断身份证号是否已经存在
	 */
	public void checkByIDNumber() {
		/*String ajaxMessage = null;
		if (userService.checkByIDNumber(this.user.getIDNumber())) {
			// 如果用户名存在
			ajaxMessage = "fail";
		} else {
			ajaxMessage = "success";
		}
		System.out.println("用户注册状态:" + ajaxMessage);
		try {
			ServletActionContext.getResponse().getWriter().write(ajaxMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	

	/**
	 * 注销登录
	 * 
	 * @return
	 */
	public String logout() {
		
		ServletActionContext.getRequest().getSession().removeAttribute("user");
		ActionContext.getContext().put("message", "注销成功");
		ActionContext.getContext().put("urladdress", "/front/user/user_loginUI");
		return "message";
	}
	
	/**
	 * 修改密码
	 */
	public void editPassword() {
		Users user= WebTool.getLoginedUser(ServletActionContext.getRequest());
		String txtPWD= ServletActionContext.getRequest().getParameter("txtPWD");	
		user.setPassword(MD5.MD5Encode(txtPWD));
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			userService.update(user);
		} catch (Exception e) {
			map.clear();
			map.put("msg", "修改密码失败！");
			e.printStackTrace();
		}
		map.clear();
		map.put("msg", "修改密码成功！");

		
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 修改注册信息
	 */
	public String editRegister() {
		Users user= WebTool.getLoginedUser(ServletActionContext.getRequest());
		String name= ServletActionContext.getRequest().getParameter("_name");	
		String phone= ServletActionContext.getRequest().getParameter("_phone");	
		String mobile= ServletActionContext.getRequest().getParameter("_mobile");	
		String address= ServletActionContext.getRequest().getParameter("_address");	
		String email= ServletActionContext.getRequest().getParameter("_email");	
		user.setName(name);
		user.setContactPhone(phone);
		user.setContactCellphone(mobile);
		user.setAddress(address);
		user.setEmail(email);
		
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			userService.update(user);
		} catch (Exception e) {
			map.clear();
			map.put("message", "信息修改失败！");
			
			e.printStackTrace();
			ActionContext.getContext().put("message", "信息修改失败");
			ActionContext.getContext().put("urladdress", "/front/user/user_editRegisterUI");
			return "message";
		}
		map.clear();
		map.put("message", "信息修改成功！");
		ActionContext.getContext().put("message", "信息修改成功");
		ActionContext.getContext().put("urladdress", "/front/user/user_loginUI");
		return "message";
		

		
		/*JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		

	}

	@SuppressWarnings("unchecked")
	public String update() {
		/*List<Attachment> materials = (List<Attachment>) ServletActionContext
				.getRequest().getSession().getAttribute("materials");
		if (materials != null && materials.size() > 0) {
			Set<Attachment> declareMaterials = new HashSet<Attachment>();
			for (Attachment material : materials) {
				material.setUser(user);
				declareMaterials.add(material);
			}
			user.setAttachments(declareMaterials);
		}
		// 将用户状态设置成未审核状态
		user.setState((short) 0);
		// 判断用户密码是否有改变
		User loginUser = WebTool.getLoginedUser(ServletActionContext
				.getRequest());
		if (user.getPassword() != null && !"".equals(user.getPassword())
				&& !loginUser.getPassword().equals(user.getPassword())) {
			user.setPassword(MD5.MD5Encode(user.getPassword()));
		}
		userService.update(user);
		ActionContext.getContext().put("message", "注册信息修改成功");
		ActionContext.getContext().put("urladdress", "/user/front/login");
		ServletActionContext.getRequest().getSession().setAttribute(
				"materials", null);
		ServletActionContext.getRequest().getSession().setAttribute(
				"materialMap_1", null);
		ServletActionContext.getRequest().getSession().setAttribute(
				"materialMap_2", null);
		ServletActionContext.getRequest().getSession().setAttribute(
				"materialMap_3", null);
		ServletActionContext.getRequest().getSession().setAttribute(
				"materialMap_4", null);*/
		return "message";
	}
	
	@SuppressWarnings("unchecked")
	public void loadJson() {
		Users user= WebTool.getLoginedUser(ServletActionContext.getRequest());
		StringBuilder str=new StringBuilder();
		str.append("{ \"usertype\":\"");
		str.append(user.getUserType());
		str.append("\",");
		str.append("\"username\":\"");
		str.append(user.getUsername());
		str.append("\",");
		str.append("\"name\":\"");
		str.append(user.getName());
		str.append("\",");
		str.append("\"phone\":\"");
		str.append(user.getContactPhone());
		str.append("\",");
		str.append("\"mobile\":\"");
		str.append(user.getContactCellphone());
		str.append("\",");
		str.append("\"address\":\"");
		str.append(user.getAddress());
		str.append("\",");
		str.append("\"email\":\"");
		str.append(user.getEmail());
		str.append("\" }");
		JSONObject resultObj = JSONObject.fromObject(str.toString());
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

	 public String usercenter(){
		 Users user= WebTool.getLoginedUser(ServletActionContext.getRequest());
		 ServletActionContext.getRequest().setAttribute("user",user);
		 
		return "usercenter";
		 
	 }
	 
	 public void updateUser(){
		 Users user= WebTool.getLoginedUser(ServletActionContext.getRequest());
			String name= ServletActionContext.getRequest().getParameter("name");	
			String phone= ServletActionContext.getRequest().getParameter("phone");	
			String mobile= ServletActionContext.getRequest().getParameter("mobile");	
			String address= ServletActionContext.getRequest().getParameter("address");	
			String email= ServletActionContext.getRequest().getParameter("email");
			
			if(name!=null&&!name.trim().equals("")){
				user.setName(name);
			}
			if(phone!=null&&!phone.trim().equals("")){
				user.setContactPhone(phone);
			}
			if(mobile!=null&&!mobile.trim().equals("")){
				user.setContactCellphone(mobile);
			}
			if(address!=null&&!address.trim().equals("")){
				user.setAddress(address);
			}
			if(email!=null&&!email.trim().equals("")){
				user.setEmail(email);
			}
			
			Map<String, Object> map = new HashMap<String, Object>();
			try{
				userService.update(user);
			} catch (Exception e) {
				map.clear();
				map.put("msg", "信息修改失败！");
				e.printStackTrace();
			}
			map.clear();
			map.put("msg", "信息修改成功！");

			
			JSONObject resultObj = JSONObject.fromObject(map);
			try {
				ServletActionContext.getResponse().setCharacterEncoding("utf-8");
				ServletActionContext.getResponse().getWriter().print(resultObj);
				ServletActionContext.getResponse().flushBuffer();
			} catch (Exception e) {
				e.printStackTrace();
			}
	 }
	 
}
