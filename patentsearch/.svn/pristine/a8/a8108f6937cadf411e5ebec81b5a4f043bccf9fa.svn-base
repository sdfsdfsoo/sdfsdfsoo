package patentsearch.web.action.privilege;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import patentsearch.bean.base.PageView;
import patentsearch.bean.base.QueryResult;
import patentsearch.bean.privilege.PrivilegeGroup;
import patentsearch.bean.privilege.SystemPrivilege;
import patentsearch.bean.privilege.SystemPrivilegePK;
import patentsearch.service.privilege.PrivilegeGroupService;
import patentsearch.service.privilege.SystemPrivilegeService;

import com.opensymphony.xwork2.ActionContext;

/**
 *权限组管理 
 */
@Controller
@Scope("prototype")
public class PrivilegeGroupAction {
	@Resource PrivilegeGroupService privilegeGroupService;
	@Resource SystemPrivilegeService systemPrivilegeService;
	/*权限组*/
	private PrivilegeGroup privilegeGroup;
	/*权限数组*/
	private String[] privileges;
	/*当前页*/
	private Integer page = 1;
	
	
	public String[] getPrivileges() {
		return privileges;
	}

	public void setPrivileges(String[] privileges) {
		this.privileges = privileges;
	}

	public PrivilegeGroup getPrivilegeGroup() {
		return privilegeGroup;
	}

	public void setPrivilegeGroup(PrivilegeGroup privilegeGroup) {
		this.privilegeGroup = privilegeGroup;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
	
	/**
	 *权限组分页显示 
	 */
	public String list(){
		PageView<PrivilegeGroup> pageView = new PageView<PrivilegeGroup>(10, page);
		int firstindex = (pageView.getCurrentpage()-1)*pageView.getMaxresult();
		
		QueryResult<PrivilegeGroup> qr = privilegeGroupService.getScrollData(firstindex,pageView.getMaxresult());
		pageView.setRecords(qr.getResultlist());
		pageView.setTotalrecords((int)qr.getTotalrecord());
		ActionContext.getContext().put("pageView", pageView);
		return  "list";
	}
	/**
	 * 添加权限组界面
	 */
	public String add(){
		//获取所有系统权限
		List<SystemPrivilege> privileges = systemPrivilegeService.getScrollData().getResultlist();
		ActionContext.getContext().put("privileges", privileges);
		return "add";
	}
	/**
	 * 添加权限组
	 */
	public String insert(){
		
		if(this.privilegeGroup != null && this.privilegeGroup.getName() != null 
				&& !"".equals(this.privilegeGroup.getName().trim())){
			if (privileges != null && privileges.length > 0) {
				for (String str : privileges) {// 所传过来的字符串格式是：模块，权限
					String[] module_privilege = str.split(",");
					String module = module_privilege[0];
					String privilege = module_privilege[1];
					SystemPrivilegePK privilegePK = new SystemPrivilegePK(module, privilege);
					privilegeGroup.getPrivileges().add(systemPrivilegeService.find(privilegePK));
				}
			}else{
				ActionContext.getContext().put("message", "请给权限组分配权限");
				ActionContext.getContext().put("urladdress", "/control/privilegeGroup/add");
				return "message";
			}
			//添加权限组
			privilegeGroupService.save(privilegeGroup);
			ActionContext.getContext().put("message", "权限组添加成功");
			ActionContext.getContext().put("urladdress", "/control/privilegeGroup/list");
			return "message";
		}
		ActionContext.getContext().put("message", "权限组名称不能为空!!");
		ActionContext.getContext().put("urladdress", "/control/privilegeGroup/add");
		return "message";
	}
	
	/**
	 * 修改权限组界面
	 */
	public String edit(){
		privilegeGroup = privilegeGroupService.find(this.privilegeGroup.getGroupid());
		
		//获取所有系统权限
		List<SystemPrivilege> privileges = systemPrivilegeService.getScrollData().getResultlist();
		ActionContext.getContext().put("privileges", privileges);
		return "edit";
	}
	/**
	 * 更新权限组
	 */
	public String update(){
		
		if(this.privilegeGroup != null && this.privilegeGroup.getName() != null 
				&& !"".equals(this.privilegeGroup.getName().trim())){
			if (privileges != null && privileges.length > 0) {
				for (String str : privileges) {// 所传过来的字符串格式是：模块，权限
					String[] module_privilege = str.split(",");
					String module = module_privilege[0];
					String privilege = module_privilege[1];
					SystemPrivilegePK privilegePK = new SystemPrivilegePK(module, privilege);
					privilegeGroup.getPrivileges().add(systemPrivilegeService.find(privilegePK));
				}
			}else{
				ActionContext.getContext().put("message", "请给权限组分配权限");
				ActionContext.getContext().put("urladdress", "/control/privilegeGroup/edit");
				return "message";
			}
			//添加权限组
			privilegeGroupService.update(privilegeGroup);
			ActionContext.getContext().put("message", "权限组修改成功");
			ActionContext.getContext().put("urladdress", "/control/privilegeGroup/list");
			return "message";
		}
		ActionContext.getContext().put("message", "权限组名称不能为空!!");
		ActionContext.getContext().put("urladdress", "/control/privilegeGroup/edit");
		return "message";
	}
	/**
	 * 删除权限组
	 * @return
	 */
	public String delete(){
		
		privilegeGroupService.delete(privilegeGroup.getGroupid());
		ActionContext.getContext().put("message", "权限组删除成功");
		ActionContext.getContext().put("urladdress", "/control/privilegeGroup/list");
		return "message";
	}
}
