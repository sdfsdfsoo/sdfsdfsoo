package patentsearch.web.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import patentsearch.bean.privilege.PrivilegeGroup;
import patentsearch.bean.privilege.SystemPrivilege;
import patentsearch.bean.privilege.SystemPrivilegePK;
import patentsearch.utils.base.WebTool;

/**
 * 权限校验标签
 * 
 */
public class PermissionTag extends TagSupport {
	private String module;
	private String privilege;

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	/*
	 * 审核功能要根据管理员权限与审核状态综合考虑 state=0_放弃本次审核,11_一级审核通过,10_一级审核不通过
	 * 21_二级审核通过,20_二级审核不通过, 31_三级审核通过,30_三级审核不通过, 41_四级审核通过,40_四级审核不通过,
	 * 51_五级审核通过,50_五级审核不通过
	 */
	@Override
	public int doStartTag() throws JspException {
		/*
		 * boolean result = false; Master employee =
		 * WebTool.getLoginedMaster((HttpServletRequest
		 * )pageContext.getRequest());//获取登录到系统的员工 SystemPrivilege privilege =
		 * new SystemPrivilege(new SystemPrivilegePK(this.module,
		 * this.privilege)); for(PrivilegeGroup group : employee.getGroups()){
		 * if(group.getPrivileges().contains(privilege)){ result = true; break;
		 * } } return result? EVAL_BODY_INCLUDE : SKIP_BODY;
		 */
		return 0;
	}

}
