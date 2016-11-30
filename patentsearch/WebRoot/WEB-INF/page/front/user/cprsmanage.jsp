<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Insert title here</title>
		<link rel="stylesheet" type="text/css"
			href="/css/userCenter/userCenter.css">
		<link href="/js/easyui/themes/icon.css" rel="stylesheet"
			type="text/css" />
		<link href="/js/easyui/themes/default/easyui.css" rel="stylesheet"
			type="text/css" />
		<link href="/css/index.css" rel="stylesheet" type="text/css" />
		<script src="/js/jquery-1.8.0.min.js" type="text/javascript"></script>
		<script src="/js/easyui/jquery.easyui.min.js"></script>
		<script src="/js/easyui/locale/easyui-lang-zh_CN.js"></script>
	</head>
	<body>
		<div style="width: 1000px; margin: 0 auto;">
			<%@include file="/WEB-INF/page/front/share/help.jsp"%>
			<%@include file="/WEB-INF/page/front/share/top.jsp"%>
			<div id="mid" class="base">
				<div id="left" style="width: 225px;">
					<div id="pinlieft">
						<div id="left_title " class="left_ti" style="text-align: center;">
							&nbsp;
							<span>用户中心</span>&nbsp;
						</div>
						<div class="left_content2" style="padding: 0px; width: 220px">
							<div class="panel"
								style="width: 220px; background-color: #FBEC88">
								<div
									class="panel-header accordion-header accordion-header-selected"
									style="height: 16px; width: 210px; border-width: 0 0 1px;">
									<div class="panel-title ">
										<a href="EditUser.aspx"><span>个人资料</span>
										</a>
									</div>
									<
								</div>
							</div>
							<div class="panel" style="width: 220px;">
								<div class="panel-header accordion-header"
									style="height: 16px; width: 210px; border-width: 0 0 0 0px;">
									<div class="panel-title">
										<a href="frmQueryMag.aspx"><span style="color: #800000;">检索式管理</span>
										</a>
									</div>
								</div>
							</div>
							<div class="panel" style="width: 220px;">
								<div class="panel-header accordion-header"
									style="height: 16px; width: 210px; border-width: 0 0 0 0px;">
									<div class="panel-title">
										<a href="frmCollectList.aspx"><span>我的收藏夹</span>
										</a>
									</div>
								</div>
							</div>
							<div class="easyui-accordion "
								style="width: 220px; height: 420px;">
								<div title="热门收藏" style="overflow: inherit; min-height: 250px;">
									<div id="divtop"
										style="min-height: 250px; width: 202px; padding: 10px;">
										<table id="tbhot" class="easyui-datagrid"
											style="width: 210px; overflow: visible"
											data-options="singleSelect:true,collapsible:true">
											<thead>
												<tr>
													<th data-options="field:'Title',width:210">
													</th>
												</tr>
											</thead>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div id="right" style="width: 768px; height: 535px;">
					<div id="ctl00_ContentPlaceHolder1_UpdatePanel1">

						<div class="Boxdiv_xiwl">
							<div style="padding-left: 20px">
								<table id="ctl00_ContentPlaceHolder1_RadioButtonListTypes"
									border="0">
									<tr>
										<td>
											<input id="ctl00_ContentPlaceHolder1_RadioButtonListTypes_0"
												type="radio"
												name="ctl00$ContentPlaceHolder1$RadioButtonListTypes"
												value="0" checked="checked" />
											<label for="ctl00_ContentPlaceHolder1_RadioButtonListTypes_0">
												中国专利
											</label>
										</td>
										<td>
											<input id="ctl00_ContentPlaceHolder1_RadioButtonListTypes_1"
												type="radio"
												name="ctl00$ContentPlaceHolder1$RadioButtonListTypes"
												value="1"
												onclick="javascript:setTimeout('__doPostBack(\'ctl00$ContentPlaceHolder1$RadioButtonListTypes$1\',\'\')', 0)" />
											<label for="ctl00_ContentPlaceHolder1_RadioButtonListTypes_1">
												世界专利
											</label>
										</td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
								</table>
							</div>
							<div class="Boxdiv_xiwl toolbarAccount">
								<div>
									<table width="100%">
										<tr style="padding-top: 2px; vertical-align: middle">
											<td align="right"
												style="padding-top: 2px; vertical-align: middle">
												来 源：
											</td>
											<td align="left"
												style="padding-top: 2px; vertical-align: middle; width: 145px;">
												<select name="ctl00$ContentPlaceHolder1$ddlModel"
													id="ctl00_ContentPlaceHolder1_ddlModel"
													style="width: 140px;">
													<option selected="selected" value="-1">
														所有
													</option>
													<option value="0">
														智能检索
													</option>
													<option value="1">
														表格检索
													</option>
													<option value="2">
														专家检索
													</option>
													<option value="3">
														分类导航检索
													</option>
													<option value="4">
														二次检索
													</option>
													<option value="5">
														过滤检索
													</option>

												</select>
											</td>
											<td align="right"
												style="padding-top: 2px; vertical-align: middle">
												检索式：
											</td>
											<td align="left"
												style="padding-top: 2px; vertical-align: middle; width: 145px">
												<input name="ctl00$ContentPlaceHolder1$TextBoxKeyword"
													type="text" id="ctl00_ContentPlaceHolder1_TextBoxKeyword"
													style="width: 165px;" />
											</td>
											<td></td>
										</tr>
										<tr>
											<td align="right"
												style="padding-top: 2px; vertical-align: middle">
												检索时间：
											</td>
											<td align="center" colspan="3"
												style="padding-top: 2px; vertical-align: middle; text-align: justify;">
												<input name="ctl00$ContentPlaceHolder1$txtSTime" type="text"
													id="ctl00_ContentPlaceHolder1_txtSTime" class="Wdate"
													onClick="var d5222=$dp.$('ctl00_ContentPlaceHolder1_txtEndTime');WdatePicker({onpicked:function(){d5222.focus();},maxDate:'#F{$dp.$D(\'ctl00_ContentPlaceHolder1_txtEndTime\')}'})"
													style="width: 190px;" />
												&nbsp; 至&nbsp;
												<input name="ctl00$ContentPlaceHolder1$txtEndTime"
													type="text" id="ctl00_ContentPlaceHolder1_txtEndTime"
													class="Wdate"
													onFocus="WdatePicker({minDate:'#F{$dp.$D(\'ctl00_ContentPlaceHolder1_txtSTime\')}'})"
													style="width: 190px;" />
											</td>

											<td style="text-align: left">
												<a id="ctl00_ContentPlaceHolder1_LinkButtonSearch"
													class="btn"
													href="javascript:__doPostBack('ctl00$ContentPlaceHolder1$LinkButtonSearch','')">搜索</a>
											</td>
										</tr>
									</table>
								</div>
							</div>
							<div class="Boxdiv_xiwl">
								<div>
									<table class="gridView" cellspacing="0" border="0"
										id="ctl00_ContentPlaceHolder1_GridView1"
										style="width: 100%; border-collapse: collapse;">
										<tr>
											<th align="left" valign="middle" scope="col">
												<input type="checkbox" id="input2" onclick="selectAll(this)" />
												全选
											</th>
											<th scope="col">
												检索式
											</th>
											<th scope="col">
												命中数
											</th>
											<th scope="col">
												来源
											</th>
											<th scope="col">
												时间
											</th>
											<th scope="col">
												执行
											</th>
										</tr>
										<tr
											onmouseover="currentcolor=this.style.backgroundColor;this.style.backgroundColor='#F3F3F3';"
											onmouseout="this.style.backgroundColor=currentcolor;"
											style="height: 28px;">
											<td align="left" valign="middle">
												<input
													id="ctl00_ContentPlaceHolder1_GridView1_ctl02_CheckBoxSelect"
													type="checkbox"
													name="ctl00$ContentPlaceHolder1$GridView1$ctl02$CheckBoxSelect" />
											</td>
											<td style="width: 310px;">
												<div
													style="width: 300px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;"
													title='(001) F XX (江苏畅远信息科技有限公司/PA)'>
													(001) F XX (江苏畅远信息科技有限公司/PA)
												</div>
											</td>
											<td align="center">
												10
											</td>
											<td align="center">
												表格检索
											</td>
											<td align="center">
												2014-05-12 10:58:32
											</td>
											<td align="center">
												<a id="ctl00_ContentPlaceHolder1_GridView1_ctl02_HyperLink1"
													href="frmPatentList.aspx?db=CN&amp;No=001&amp;kw=&amp;Nm=10&amp;etp=&amp;Query=F%20XX%20(%e9%95%87%e6%b1%9f%e7%95%85%e8%bf%9c%e4%bf%a1%e6%81%af%e7%a7%91%e6%8a%80%e6%9c%89%e9%99%90%e5%85%ac%e5%8f%b8%2fPA)"
													target="_blank">查看</a>
											</td>
										</tr>
									</table>
								</div>
								<a onclick=
	return confirm('您确认要删除么？');;
id="ctl00_ContentPlaceHolder1_LinkButtonDeleteSelected"
									class="btn"
									href="javascript:__doPostBack('ctl00$ContentPlaceHolder1$LinkButtonDeleteSelected','')">删除</a>
								<a id="ctl00_ContentPlaceHolder1_LinkButtonExport" class="btn"
									href="javascript:__doPostBack('ctl00$ContentPlaceHolder1$LinkButtonExport','')">导出</a>
							</div>
						</div>

					</div>
				</div>
				<script type="text/javascript">
	function selectAll(obj) {
		var SetValue = obj.checked; //  $(obj).attr("checked"); //trun|false
		//alert(SetValue);
		//$('input[type=checkbox]').attr('checked', $(checkbox).attr('checked'));
		$("input[type='checkbox']").attr("checked", SetValue);
	}
</script>
			</div>
			<%@include file="/WEB-INF/page/front/share/footer.jsp"%>

		</div>
	</body>
</html>