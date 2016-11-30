﻿<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title></title>
		<link rel="stylesheet" href="/js/jquery-ui/themes/base/jquery-ui.css" />
		<link href="/css/index.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="/css/law/law.css" />
		<link rel="stylesheet" href="/js/jquery-ui/themes/base/jquery-ui.css" />
		<link rel="stylesheet" type="text/css"
			href="/js/artdialog/ui-dialog.css" />
		<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
		<script src="/js/jquery-ui/ui/jquery-ui.js"></script>
		<script src="/js/jquery-ui/ui/i18n/jquery.ui.datepicker-zh-CN.js"></script>
		<script type="text/javascript" src="/js/law/law.js"></script>
		<style>
		.imgGray {
			filter: gray;
			filter: grayscale(1);
			-webkit-filter: grayscale(1);
		}
		</style>
	</head>
	<body>
	<%@include file="/WEB-INF/page/front/share/top.jsp"%>
			
			<div id="mid" class="base">

				<div class="div_Content_xiwl">
					<div id="DivPatDetailTabs" style="width:100%;height: 850px">
						<ul id="ulPatTabs">
							<li>
								<a href="#tabMianXml">法律状态</a>
							</li>
							<li>
								<a href='#TabZlqlzy'>专利权利转移</a>
							</li>
							<li>
								<a href='#TabZlzybq'>专利质押保全</a>
							</li>
							<li>
								<a href='#TabZlssxk'>专利实施许可</a>
							</li>
							<li>
								<a href='#TabZlqwxxg'>专利权无效宣告</a>
							</li>
							<li>
								<a href='#TabZlqzz'>专利权终止</a>
							</li>
							<li>
								<a href='#TabZlhf'>专利申请或专利权的恢复</a>
							</li>

						</ul>
						<!-- 中国专利法律状态检索 -->
						<div id="tabMianXml">
							<form name="" id="" method="post"
								action="/front/search/legal_legalSearch">
								<table>
									<tr>
										<td align="right">
											专利申请号：
										</td>
										<td align="left">
											<input name="cnLegalStatus.shenQingH" type="text" id="Txt1"
												class="input" title="" />
										</td>
										<td align="right">
											法律状态公告日：
										</td>
										<td align="left">
											<input name="cnLegalStatus.legalDate" type="text"
												id="Txt2" title="" />
										</td>
									</tr>
									<tr>
										<td align="right">
											法律状态：
										</td>
										<td align="left">
											<input name="cnLegalStatus.legalStatusInfo" type="text"
												id="Txt3" class="input"  size="55"  title="" />
										</td>
										<%--<td align="right">
											质权人：
										</td>
										<td align="left">
											<input name="cnLegalStatus.zhiQuanR" type="text" id="Txt4"
												class="input" title="" />
										</td>
									--%></tr>
									<tr>
										<td colspan="4" align="center">
											<input type="submit" name="" value="查询" id=""
												class="buttoncss" />
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<input id="Button1" type="reset" value="重置"
												class="buttoncss" />
										</td>
									</tr>
								</table>
							</form>
						</div>
							<!-- 专利权利转移-->
						<div id="TabZlqlzy">
							<form name="" id="" method="post"
								action="/front/search/legal_legalSearch">
							<!-- <input name="cnLegalStatus.legalStatusInfo" type="hidden" value="专利权的转移" /> -->	
								<table>
									<tr>
										<td align="right">
											专利申请号：
										</td>
										<td align="left">
										<!-- 必须有title属性，否则提示功能无效 -->
											<input name="cnLegalStatus.shenQingH" type="text" id="Txt5"
												class="input" title=""
												  />
										</td>
										<td align="right">
											IPC分类号：
										</td>
										<td align="left">
											<input name="cnLegalStatus.ipc" type="text" id="Txt6" class="input" title=""
												  />
										</td>
									</tr>
									<%--<tr>
										<td align="right">
											专利名称：
										</td>
										<td align="left">
											<input name="cnLegalStatus.zhuanLiMc" type="text" id="Txt7"  title=""
												class="input" />
										</td>
										<td align="right">
											摘要：
										</td>
										<td align="left">  
											<input name="cnLegalStatus.zhaiYao" type="text" id="Txt8" class="input" title=""/>
										</td>
									</tr>
									--%>
									<tr>

										<td align="right">
											变更前(权利人或地址)：
										</td>
										<td align="left">
											<input name="cnLegalStatus.bianGengQ" type="text" id="Txt11" title=""
												class="input" />
										</td>
										<td align="right">
											变更后(权利人或地址)：
										</td>
										<td align="left">
										<input name="zhuangTai" type="hidden" id="zhuangTai" 
										 value="专利申请权、专利权的转移,专利申请权的转移,专利权的转移"/>
											<input name="cnLegalStatus.bianGengH" type="text" id="Txt12" title=""
												class="input" />
										</td>
									</tr>
									 
									<tr>
										<%--<td align="right">
											主权利要求：
										</td>
										<td align="left">  
											<input name="cnLegalStatus.zhuQuanLi" type="text" id="Txt9" class="input" title=""/>
										</td>
										--%><td align="right">
											登记生效日：
										</td>
										<td align="left">
											<input name="cnLegalStatus.dengJiSxr" type="text" title=""
												id="Txt10" class="input"
												  />
										</td>
									</tr>
									<tr>
										<td colspan="4" align="center">
											<input type="submit" name="" value="查询" id=""
												class="buttoncss" />
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<input id="Button2" type="reset" value="重置"  
class="buttoncss" />
										</td>
									</tr>
								</table>
							</form>
						</div>
							<!-- 专利质押保全-->
						<div id="TabZlzybq">
						<form name="" id="" method="post"
								action="/front/search/legal_legalSearch">
							<table>
								<tr>
									<td align="right">
										专利申请号：
									</td>
									<td align="left">
										<input name="cnLegalStatus.shenQingH" type="text" id="Txt13" class="input"
											title="" />
									</td>
									<td align="right">
										IPC分类号：
									</td>
									<td align="left">
										<input name="cnLegalStatus.ipc" type="text" id="Txt14" class="input" 
											title=""/>
									</td>
								</tr>
								<%--<tr>
									<td align="right">
										专利名称：
									</td>
									<td align="left">
										<input  name="cnLegalStatus.zhuanLiMc" type="text" id="Txt15" class="input" title="" />
									</td>
									<td align="right">
										摘要：
									</td>
									<td align="left">
										<input name="cnLegalStatus.zhaiYao" type="text" id="Txt16" class="input" title="" />
									</td>
								</tr>
								--%><tr>
									<%--<td align="right">
										主权利要求：
									</td>
									<td align="left">
										<input name="cnLegalStatus.zhuQuanLi" type="text" id="Txt17" class="input" title=""/>
									</td>

									--%><td align="right">
										法律状态：
									</td>
									<td align="left">
										 <input name="zhuangTai" type="hidden" id="zhuangTai" 
										 value="专利权质押合同登记的生效,专利权质押合同登记的变更,专利权质押合同登记的注销"/>
										 <select name="cnLegalStatus.legalStatusInfo" id="Txt18" title="">
											<option selected="selected" value="">
												请选择专利质押保全法律状态
											</option>
											<option value="专利权质押合同登记的生效">
												专利权质押合同登记的生效
											</option>
											<option value="专利权质押合同登记的变更">
												专利权质押合同登记的变更
											</option>
											<option value="专利权质押合同登记的注销">
												专利权质押合同登记的注销
											</option>
										</select>
									</td>
								</tr>

								<tr>
									<td align="right">
										变更日：
									</td>
									<td align="left">
										<input name="cnLegalStatus.bianGengR" type="text" id="Txt19" class="input"
											title="" />
									</td>

									<td align="right">
										登记生效日：
									</td>
									<td align="left">
										<input name="cnLegalStatus.dengJiSxr" type="text" id="Txt20" class="input" title=""
											  />
									</td>

								</tr>
								<tr>
									<td align="right">
										合同备案号：
									</td>
									<td align="left">
										<input name="cnLegalStatus.heTongBah" type="text" id="Txt21" class="input" title=""/>
									</td>
									<td align="right">
										解除日：
									</td>
									<td align="left">
										<input name="cnLegalStatus.jieChuR" type="text" id="Txt22" class="input" title=""
											 />
									</td>

								</tr>
								<%--<tr>
									<td align="right">
										出质人：
									</td>
									<td align="left">
										<input name="cnLegalStatus.chuZhiR" type="text" id="Txt23" class="input" title="" />
									</td>
									<td align="right">
										质权人：
									</td>
									<td align="left">
										<input name="cnLegalStatus.zhiQuanR" type="text" id="Txt24" class="input" title="" />
									</td>
									<td>
									</td>
									<td>
									</td>
								</tr>
								--%><tr>
									<td colspan="4" align="center">
										<input type="submit" name="" value="查询" id=""
											class="buttoncss" />
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<input id="Button3" type="reset" value="重置"  
class="buttoncss" />
									</td>
								</tr>
							</table>
							</form>
						</div>
							<!-- 专利实施许可-->
						<div id="TabZlssxk">
						<form name="" id="" method="post"
								action="/front/search/legal_legalSearch">
							<table>
								<tr>
									<td align="right">
										专利申请号：
									</td>
									<td align="left">
										<input name="cnLegalStatus.shenQingH" type="text" id="Txt25" class="input" title=""
											 />
									</td>
									<td align="right">
										IPC分类号：
									</td>
									<td align="left">
										<input name="cnLegalStatus.ipc" type="text" id="Txt26" class="input" title=""
											  />
									</td>
								</tr>
								<%--<tr>
									<td align="right">
										专利名称：
									</td>
									<td align="left">
										<input name="cnLegalStatus.zhuanLiMc" type="text" id="Txt27" class="input"  title=""/>
									</td>
									<td align="right">
										摘要：
									</td>
									<td align="left">
										<input name="cnLegalStatus.zhaiYao" type="text" id="Txt28" class="input"  title=""/>
									</td>
								</tr>
								--%><tr>
									<%--<td align="right">
										主权利要求：
									</td>
									<td align="left">
										<input name="cnLegalStatus.zhuQuanLi" type="text" id="Txt29" class="input" title=""/>
									</td>
									--%><td align="right">
										许可种类：
									</td>
									<td align="left">
										<select name="cnLegalStatus.xuKeZl" id="Txt30" title="">
											<option selected="selected" value="">
												请选择许可种类
											</option>
											<option value="独占">
												独占
											</option>
											<option value="独占许可">
												独占许可
											</option>
											<option value="独占许可、分许可">
												独占许可、分许可
											</option>
											<option value="独占许可、普通许可">
												独占许可、普通许可
											</option>
											<option value="分许可">
												分许可
											</option>
											<option value="交叉许可">
												交叉许可
											</option>
											<option value="排他许可">
												排他许可
											</option>
											<option value="普通">
												普通
											</option>
											<option value="普通许可">
												普通许可
											</option>
											<option value="普通许可、分许可">
												普通许可、分许可
											</option>
										</select>
									</td>
								</tr>
								<tr>
									<td align="right">
										合同备案号：
									</td>
									<td align="left">
										<input name="cnLegalStatus.heTongBah" type="text" id="Txt31" class="input" title="" />
									</td>
									<td align="right">
										合同备案阶段：
									</td>
									<td align="left">
									<input name="zhuangTai" type="hidden" id="zhuangTai" 
										 value="专利实施许可合同备案的生效,专利实施许可合同备案的变更,专利实施许可合同备案的注销"/>
										<select name="cnLegalStatus.legalStatusInfo" id="Txt32" title="">
											<option selected="selected" value="">
												请选择合同备案阶段
											</option>
											<option value="专利实施许可合同备案的生效">
												专利实施许可合同备案的生效
											</option>
											<option value="专利实施许可合同备案的变更">
												专利实施许可合同备案的变更
											</option>
											<option value="专利实施许可合同备案的注销">
												专利实施许可合同备案的注销
											</option>
										</select>
									</td>
								</tr>
								<tr>
									<td align="right">
										备案日：
									</td>
									<td align="left">
										<input name="cnLegalStatus.beiAnRq" type="text" id="Txt33" class="input"
											title="" />
									</td>
									<td align="right">
										变更日：
									</td>
									<td align="left">
										<input name="cnLegalStatus.bianGengR" type="text" id="Txt34" class="input"
											title=""/>
									</td>
								</tr>
								<tr>
									<td align="right">
										解除日：
									</td>
									<td align="left">
										<input name="cnLegalStatus.jieChuR" type="text" id="Txt35" class="input"
											title=""/>
									</td>

								</tr>
								<tr>
									<td align="right">
										变更前权利人：
									</td>
									<td align="left">
										<input name="cnLegalStatus.bianGengQ" type="text" id="Txt36" class="input" title=""/>
									</td>
									<td align="right">
										变更后权利人：
									</td>
									<td align="left">
										<input name="cnLegalStatus.bianGengH" type="text" id="Txt37" class="input" title="" />
									</td>
								</tr>
								<tr>
									<td>
									</td>
									<td>
									</td>
									<td>
									</td>
								</tr>
								<tr>
									<td colspan="4" align="center">
										<input type="submit" name="" value="查询" id=""
											class="buttoncss" />
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<input id="Button4" type="reset" value="重置"  
class="buttoncss" />
									</td>
								</tr>
							</table>
							</form>
						</div>
						<!-- 专利权无效宣告 -->
						<div id="TabZlqwxxg">
							 <form name="" id="" method="post"
								action="/front/search/legal_legalSearch">
						
								<table>
									<tr>
										<td align="right">
											专利申请号：
										</td>
										<td align="left">
										<!-- 必须有title属性，否则提示功能无效 -->
											<input name="cnLegalStatus.shenQingH" type="text" id="Txt38"
												class="input" title=""
												  />
										</td>
										<td align="right">
											IPC分类号：
										</td>
										<td align="left">
											<input name="cnLegalStatus.ipc" type="text" id="Txt39" class="input" title=""
												  />
										</td>
									</tr>
									<%--<tr>
										<td align="right">
											专利名称：
										</td>
										<td align="left">
											<input name="cnLegalStatus.zhuanLiMc" type="text" id="Txt40"  title=""
												class="input" />
										</td>
										<td align="right">
											摘要：
										</td>
										<td align="left">
											<input name="cnLegalStatus.zhaiYao" type="text" id="Txt41" class="input" title=""/>
										</td>
									</tr>
									--%><tr>
										<%--<td align="right">
											主权利要求：
										</td>
										<td align="left">
											<input name="cnLegalStatus.zhuQuanLi" type="text" id="Txt42" class="input" title=""/>
										</td>
											--%><td align="right">
										专利权的无效宣告：
									</td>
									<td align="left">
									<input name="zhuangTai" type="hidden" id="zhuangTai" 
										 value="专利权的无效宣告,专利权全部无效,专利权部分无效"/>
										 <select name="cnLegalStatus.legalStatusInfo" id="Txt43" title="">
											<option selected="selected" value="">
												请选择专利权的无效宣告类型
											</option>
											<option value="专利权的无效宣告">
												全部
											</option>
											<option value="专利权全部无效">
												专利权全部无效
											</option>
											<option value="专利权部分无效">
												专利权部分无效
											</option>
										</select>
									</td>
									</tr>
									<tr>
										<td colspan="4" align="center">
											<input type="submit" name="" value="查询" id=""
												class="buttoncss" />
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<input id="Button2" type="reset" value="重置"  
class="buttoncss" />
										</td>
									</tr>
								</table>
							</form>
						</div>
						<!-- 专利权终止 -->
						<div id="TabZlqzz">
							 		 <form name="" id="" method="post"
								action="/front/search/legal_legalSearch">
								<table>
									<tr>
										<td align="right">
											专利申请号：
										</td>
										<td align="left">
										<!-- 必须有title属性，否则提示功能无效 -->
											<input name="cnLegalStatus.shenQingH" type="text" id="Txt44"
												class="input" title=""
												  />
										</td>
										<td align="right">
											IPC分类号：
										</td>
										<td align="left">
											<input name="cnLegalStatus.ipc" type="text" id="Txt45" class="input" title=""
												  />
										</td>
									</tr>
									<%--<tr>
										<td align="right">
											专利名称：
										</td>
										<td align="left">
											<input name="cnLegalStatus.zhuanLiMc" type="text" id="Txt46"  title=""
												class="input" />
										</td>
										<td align="right">
											摘要：
										</td>
										<td align="left">
											<input name="cnLegalStatus.zhaiYao" type="text" id="Txt47" class="input" title=""/>
										</td>
									</tr>
									--%><tr>
										<%--<td align="right">
											主权利要求：
										</td>
										<td align="left">
											<input name="cnLegalStatus.zhuQuanLi" type="text" id="Txt48" class="input" title=""/>
										</td>
											--%><td align="right">
										专利权终止：
									</td>
									<td align="left">
									<input name="zhuangTai" type="hidden" id="zhuangTai" 
										 value="专利权终止,未缴年费专利权终止,专利权有效期届满"/>
										 <select name="cnLegalStatus.legalStatusInfo" id="Txt49" title="">
											<option selected="selected" value="">
												请选择专利权终止类型
											</option>
											<option value="专利权终止">
												全部
											</option>
											<option value="未缴年费专利权终止">
											未缴年费专利权终止
											</option>
											<option value="专利权有效期届满">
												专利权有效期届满
											</option>
										</select>
									</td>
									</tr>

									<tr>
										<td colspan="4" align="center">
											<input type="submit" name="" value="查询" id=""
												class="buttoncss" />
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<input id="Button2" type="reset" value="重置"  
class="buttoncss" />
										</td>
									</tr>
								</table>
							</form>
						</div>
						<!-- 专利申请或专利权的恢复 -->
						<div id="TabZlhf">
							  <form name="" id="" method="post"
								action="/front/search/legal_legalSearch">	 	   
								<table>
									<tr>
										<td align="right">
											专利申请号：
										</td>
										<td align="left">
										<!-- 必须有title属性，否则提示功能无效 -->
											<input name="cnLegalStatus.shenQingH" type="text" id="Txt50"
												class="input" title=""
												  />
										</td>
										<td align="right">
											IPC分类号：
										</td>
										<td align="left">
											<input name="cnLegalStatus.ipc" type="text" id="Txt51" class="input" title=""
												  />
										</td>
									</tr>
									<%--<tr>
										<td align="right">
											专利名称：
										</td>
										<td align="left">
											<input name="cnLegalStatus.zhuanLiMc" type="text" id="Txt52"  title=""
												class="input" />
										</td>
										<td align="right">
											摘要：
										</td>
										<td align="left">
											<input name="cnLegalStatus.zhaiYao" type="text" id="Txt53" class="input" title=""/>
										</td>
									</tr>
									--%><tr>
										<%--<td align="right">
											主权利要求：
										</td>
										<td align="left">
											<input name="cnLegalStatus.zhuQuanLi" type="text" id="Txt54" class="input" title=""/>
										</td>
											--%><td align="right">
										专利申请或者专利权的恢复：
									</td>
									<td align="left">
									<input name="zhuangTai" type="hidden" id="zhuangTai" 
										 value="专利申请或者专利权的恢复,专利申请的恢复,专利权的恢复"/>
										 <select name="cnLegalStatus.legalStatusInfo" id="Txt55" title="">
											<option selected="selected" value="">
												请选择专利申请或者专利权的恢复类型
											</option>
											<option value="专利申请或者专利权的恢复">
												专利申请或者专利权的恢复
											</option>
											<option value="专利申请的恢复">
											专利申请的恢复
											</option>
											<option value="专利权的恢复">
												专利权的恢复
											</option>
										</select>
									</td>
									</tr>

									<tr>
										<td colspan="4" align="center">
											<input type="submit" name="" value="查询" id=""
												class="buttoncss" />
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<input id="Button2" type="reset" value="重置"  
class="buttoncss" />
										</td>
									</tr>
								</table>
							</form>
						</div>
					</div>
				</div>

			</div>
		<%@include file="/WEB-INF/page/front/share/footer.jsp"%>
		<script type="text/javascript" src="/js/detail/detail.js"></script>
	</body>
</html>