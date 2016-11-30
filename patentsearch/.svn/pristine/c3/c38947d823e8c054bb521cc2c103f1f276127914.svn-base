package patentsearch.web.action.privilege;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import patentsearch.bean.privilege.PrivilegeGroup;
import patentsearch.bean.privilege.SystemPrivilege;
import patentsearch.service.privilege.PrivilegeGroupService;
import patentsearch.service.privilege.SystemPrivilegeService;

import com.opensymphony.xwork2.ActionContext;
 
/**
 * 初始化 (此action是在系统安装完后就执行)
 */
@Controller
public class SystemInitAction  {
	 
	@Resource SystemPrivilegeService privilegeService;
	@Resource PrivilegeGroupService groupService;
	//@Resource MasterService employeeService;

 
	public String init(){
			  
		initSystemPrivilege();
		initPrivilegeGroup();
		initAdmin();
		ActionContext.getContext().put("message", "系统初始化完成");
		ActionContext.getContext().put("urladdress", "/master/login");
		return "message";
	}
	/**
	 * 初始化管理员账号
	 */
	private void initAdmin() {
		/*if(employeeService.getCount()==0){
			Master employee = new Master();
			employee.setUsername("admin");
			employee.setPassword("123456");
			employee.getGroups().addAll(groupService.getScrollData().getResultlist());//赋予权限
			employeeService.save(employee);
		}	*/	
	}
	/**
	 * 初始化系统权限组
	 */
	private void initPrivilegeGroup() {
		if(groupService.getCount()==0){
			PrivilegeGroup group = new PrivilegeGroup();
			group.setName("系统权限组");
			group.getPrivileges().addAll(privilegeService.getScrollData().getResultlist());
			groupService.save(group);
		}		
	}
	/**
	 * 初始化权限
	 */
	private void initSystemPrivilege() {
		 if(privilegeService.getCount()==0){
			//if(true){
			List<SystemPrivilege> privileges = new ArrayList<SystemPrivilege>();
			//资助审核权限
		    privileges.add(new SystemPrivilege("audit", "grade_1", "一级审核功能"));
			privileges.add(new SystemPrivilege("audit", "grade_2", "二级审核功能"));
			privileges.add(new SystemPrivilege("audit", "grade_3", "三级审核功能"));
			privileges.add(new SystemPrivilege("audit", "grade_4", "四级审核功能"));
			privileges.add(new SystemPrivilege("audit", "grade_5", "五级审核功能")); 
			//用户管理
			privileges.add(new SystemPrivilege("user", "menu", "用户管理"));
			//用户管理->注册用户管理
			privileges.add(new SystemPrivilege("user", "regedit", "用户管理->注册用户管理"));
			//用户管理->后台用户管理
			privileges.add(new SystemPrivilege("user", "console", "用户管理->后台用户管理"));
			//用户管理->用户角色管理
			privileges.add(new SystemPrivilege("user", "role", "用户管理->用户角色管理"));
			//统计信息权限
			privileges.add(new SystemPrivilege("statisticalInfor", "menu", "统计信息"));
			//资助范围权限
			privileges.add(new SystemPrivilege("aidScope", "menu", "资助范围"));
			//资助政策管理权限
			privileges.add(new SystemPrivilege("aidPolicy", "menu", "资助政策"));
			//系统功能权限
			privileges.add(new SystemPrivilege("systemFunction", "menu", "系统功能"));
			//关闭用户申报功能
			privileges.add(new SystemPrivilege("systemFunction", "closeSystem", "关闭用户申报功能"));
			 
		     
			privilegeService.batchSave(privileges);
		}		
	}
	
	 

}
