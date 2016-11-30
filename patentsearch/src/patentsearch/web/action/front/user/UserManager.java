package patentsearch.web.action.front.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import patentsearch.bean.user.Users;
import patentsearch.service.user.UserService;
import patentsearch.utils.base.ConfigTool;
import patentsearch.utils.base.MD5;
import patentsearch.utils.base.WebTool;
import patentsearch.web.action.base.BaseAction;

import com.opensymphony.xwork2.ActionContext;
import com.sun.xml.internal.ws.api.message.Attachment;

/**
 * 用户信息管理
 * 
 */
@Controller
@Scope("prototype")
public class UserManager extends BaseAction {
	@Resource
	UserService userService;
	private Users user;
	/* 附件说明 */
	private String introduction;
	/* 用户新密码 */
	private String newPassword;

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * 信息维护
	 * 
	 * @return
	 */
	public String edit() {
	/*	String[] areas = ConfigTool.getAreas();
		ActionContext.getContext().put("areas", areas);
		// 取得登录用户的登陆名
		User loginedUser = WebTool.getLoginedUser(ServletActionContext
				.getRequest());
		if (loginedUser != null) {
			// 如果已经登陆
			ActionContext.getContext().put("user", loginedUser);
			// 如果申请者类型为企业，则取得企业类型
			if (loginedUser.getDeclarerType() == 3) {
				Map<String, String> companyTypes = ConfigTool.getCompanyType();
				ActionContext.getContext().put("companyTypes", companyTypes);
			}
			// 获取登录名
			String username = loginedUser.getUsername();
			// 取得用户上传的附件
			List<Attachment> attachments = attachmentService
					.getAttachmentsByUsername(username);
			ActionContext.getContext().put("attachments", attachments);

		} else {
			// 如果没有登陆，则调转到登陆页面
			ActionContext.getContext().put("message", "请先登录");
			ActionContext.getContext().put("urladdress", "/user/front/login");
			return "message";
		}
		ActionContext.getContext().put("currentArea", "信息维护");
		// 从配置文件中获取所有的代理机构
		String[] agencys = ConfigTool.getAgencys();
		ActionContext.getContext().put("agencys", agencys);
		List<String> useragents = new ArrayList<String>();
		if (loginedUser.getAgency() != null && loginedUser.getAgency().length() > 0) {
			for (String s : loginedUser.getAgency().split(",")) {
				useragents.add(s);
			}
			ActionContext.getContext().put("useragents", useragents);
		}*/
		return "edit";
	}

	/**
	 * 更新用户信息
	 * 
	 * @return
	 */
	public String update() {
		/* 接收上传附件 
		Attachment attachment = null;
		User updateUser = WebTool.getLoginedUser(ServletActionContext
				.getRequest());
		 默认视图 
		ActionContext.getContext().put("urladdress", "/user/admin/manage/edit");
		if (this.file != null) {
			// 上传文件大小，要小于1M
			if (this.file.length() > 350 * 1024) {
				ActionContext.getContext().put("message", "上传文件大小不能超过350k");

				return "message";
			}
			// 判断上传文件格式
			if (!volidateFileType(file, fileContentType, fileFileName)) {// 是否为允许上传的格式
				ActionContext.getContext().put("message", "上传格式不正确");
				ActionContext.getContext().put("urladdress",
						"/user/admin/manage/edit");
				return "message";
			}
			if (this.file.length() > 0) {
				attachment = new Attachment();
				String fileSavePath = this.generateFileSavePath();// 生成文件路径
				String fileSaveName = this
						.generateFileSaveName(this.fileFileName);// 生成保存文件名
				 保存图片 
				this.saveFile(this.file, fileSavePath, fileSaveName);

				attachment.setIntroduction(this.introduction);
				attachment.setPath(fileSavePath);
				attachment.setDisplayFilename(this.fileFileName);
				attachment.setStorageFilename(fileSaveName);
				attachment.setUser(updateUser);
				attachmentService.save(attachment);
			}
		}

		if (updateUser == null) {
			// 如果没有登陆，则调转到登陆页面
			ActionContext.getContext().put("message", "请先登录");
			ActionContext.getContext().put("urladdress", "/user/front/login");
			return "message";
		}
		updateUser.setContactCellphone(user.getContactCellphone());
		updateUser.setContactName(user.getContactName());
		updateUser.setContactPhone(user.getContactPhone());
		updateUser.setEmail(user.getEmail());
		updateUser.setPostcode(user.getPostcode());
		updateUser.setAddress(user.getAddress());
		updateUser.setWebsite(user.getWebsite());
		updateUser.setRegion(user.getRegion());
		updateUser.setBankName(user.getBankName());
		updateUser.setBankNo(user.getBankNo());
		updateUser.setPayee(user.getPayee());
		updateUser.setCompanyType(user.getCompanyType());
		updateUser.setName(user.getName());
		updateUser.setAgency(user.getAgency());
		userService.update(updateUser);
		ServletActionContext.getRequest().getSession().setAttribute("user",
				updateUser);
		ActionContext.getContext().put("message", "信息修改成功");*/
		return "message";
	}

	/*
	 * 修改密码
	 */
	public String editPwd() {
		return "editPwd";
	}

	/**
	 * 更新密码
	 */
	public String updatePwd() {
		/*if (userService.updatePassword(user.getUserName(), user.getPassword(),
				newPassword)) {
			// 更新session中user的密码
			User loginedUser = WebTool.getLoginedUser(ServletActionContext
					.getRequest());
			loginedUser.setPassword(MD5.MD5Encode(newPassword));
			ServletActionContext.getRequest().getSession().setAttribute("user",
					loginedUser);

			ActionContext.getContext().put("message", "密码修改成功");
			ActionContext.getContext().put("urladdress",
					"/user/admin/manage/edit");

		} else {
			ActionContext.getContext().put("message", "密码修改失败");
			ActionContext.getContext().put("urladdress",
					"/user/admin/manage/editPwd");
		}*/
		return "message";
	}

	 

}
