<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<title>在线专利数据库系统</title>
		<link href="/css/index.css" rel="stylesheet" type="text/css" />   
		
		<link href="/css/table/B_SeachPage.css" rel="stylesheet"
			type="text/css" />
		<link href="/css/table/B_cprs2010.css" rel="stylesheet"
			type="text/css" />
		<link rel="stylesheet" href="/js/jquery-ui/themes/base/jquery-ui.css" />
		<link rel="stylesheet" type="text/css"
			href="/js/artdialog/ui-dialog.css" />
		<link rel="stylesheet" type="text/css" href="/css/table/tableNew.css" />
		<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
		<script src="/js/jquery-ui/ui/jquery-ui.js"></script>
		<script src="/js/jquery-ui/ui/i18n/jquery.ui.datepicker-zh-CN.js"></script>
		<script type="text/javascript" src="/js/artdialog/dialog.js"></script>
		<script type="text/javascript" src="/js/table/tableNew.js"></script>
		
		<link href="/css/new/style.css" type="text/css" rel="stylesheet"/>
		<link href="/css/new/B_SeachPage.css" rel="stylesheet" type="text/css" />
		<link href="/css/new/B_cprs2010.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="/css/new/tableNew.css" />

		<script type="text/javascript">
		//兼容getName方法
	function getElementsNameForIE(tag,name) {
	    var returns = document.getElementsByName(name);  
	    if(returns.length > 0) return returns;  
	    returns = new Array();   
	    var e = document.getElementsByTagName(tag);  
	    for(var i = 0; i < e.length; i++){  
	        if(e[i].getAttribute("name") == name){  
	            returns[returns.length] = e[i];  
	        }  
	    }  
	    return returns;  
	}
	function showDiv1() {
		 var showTable1=getElementsNameForIE("div","divTable1")[0]; 
		  var closeTable2=getElementsNameForIE("div","divTable2")[0]; 
		  
		  var showdivCn= document.getElementById('exampleCn');
		  var closedivDocDB= document.getElementById('exampleDocDB');
		  
		  var formulaCn= document.getElementById('formulaCn');
		  var formulaDocDB= document.getElementById('formulaDocDB');
 			formulaCn.style.display="";
 			formulaDocDB.style.display="none";

 			showdivCn.style.display="";
 			closedivDocDB.style.display="none";
		  
 			showTable1.style.display="";
 			closeTable2.style.display="none";
 			for(var i=23;i<=35;i++){
 			   $("#Txt" + i).val("");
 			}
 			document.getElementsByName("searchscope")[0].value="Cn";
		  $('#patenttype_fm').attr("checked",'true');
 		  $('#patenttype_wg').attr("checked",'true');
 		  $('#patenttype_xx').attr("checked",'true');
 		 $('#patenttype_fm').removeAttr("disabled");
 		 $('#patenttype_wg').removeAttr("disabled");
 		 $('#patenttype_xx').removeAttr("disabled");
	}
	function onLoadAlert(){  //RadioGroup1_1
	     if(document.getElementById('RadioGroup1_1').checked==true){
	      	showDiv2();
	     }
	     if(document.getElementById('RadioGroup1_1').checked==false){
	     	showDiv1();
	     }
    }
	function showDiv2() {
			 var showTable1=getElementsNameForIE("div","divTable1")[0]; 
			  var closeTable2=getElementsNameForIE("div","divTable2")[0]; 
			  
			 var showdivCn= document.getElementById('exampleCn');
		 	 var closedivDocDB= document.getElementById('exampleDocDB');
		 	 
		 	 var formulaCn= document.getElementById('formulaCn');
		 	 var formulaDocDB= document.getElementById('formulaDocDB');
 			formulaCn.style.display="none";
 			formulaDocDB.style.display="";
		 	 
 			showdivCn.style.display="none";
 			closedivDocDB.style.display="";
			  
  			showTable1.style.display="none";
  			closeTable2.style.display="";
  			$(":input").value="";
  			for(var i=1;i<=22;i++){
  			   $("#Txt" + i).val("");
  			}
 		document.getElementsByName("searchscope")[0].value="DocDB";
 		 $('#patenttype_fm').removeAttr("checked");
 		 $('#patenttype_wg').removeAttr("checked");
 		 $('#patenttype_xx').removeAttr("checked");
 		 
		  $('#patenttype_fm').attr("disabled",'disabled');
 		  $('#patenttype_wg').attr("disabled",'disabled');
 		  $('#patenttype_xx').attr("disabled",'disabled');
	}
	function synSearch() {
		//生成检索式
		buildCprsNew();
		getPatentTypeStr();
		if($("#judge").val()=="false"){
			$("#judge").val("true");	
		}else{
			 document.getElementById('tableSearchForm').submit();
		}
	}
	
	function synSearch1(){

		if($("#TxtSearch").val()==""){
			alert("请先生成检索式");
			return false;
		}
		
		if($("#judge").val()=="false"){
			$("#judge").val("true");	
		}else{
			 document.getElementById('tableSearchForm').submit();
		}
	}
	var allSelectedState=true;
	 	function changeId(){
				  var ids="";
				  var idsObject=document.getElementsByName('ids');
				   for (i=0; i<idsObject.length; i++){
				                if (idsObject[i].type=="checkbox" && idsObject[i].checked){    
				                 ids+=idsObject[i].value+",";       
				                }
				        }   
				    document.getElementById('ids').value=ids.substring(0,ids.length-1);
           }
function selectAll(){
 			var ids="";
			 var idsObject= $("[name='ids']");
		      if(!allSelectedState){
		              $("[name='ids']").attr("checked",'true');
		            
		              for (i=0; i<idsObject.length; i++){
						                if (idsObject[i].type=="checkbox"){    
						            	  ids+=idsObject[i].value+",";       
						                }
						        }  
					 document.getElementById('ids').value=ids.substring(0,ids.length-1);
					 allSelectedState=true;
		             
		      }else{
		         $("[name='ids']").removeAttr("checked");
		         $('#ids').removeAttr("checked");
		         document.getElementById('ids').value="";
		         allSelectedState=false;
		      }
}
function keydown(evt) {
  var evt=evt || event;
if(evt.keyCode==13) {
	synSearch();
}
}
</script>
<style>
		.imgGray {
			filter: gray;
			filter: grayscale(1);
			-webkit-filter: grayscale(1);
		}
		</style>
	</head>
	<body onkeydown="keydown(event)" onload="onLoadAlert();">
	<%@include file="/WEB-INF/page/front/share/top.jsp"%>
		<form name="tableSearchForm" id="tableSearchForm" method="get"
			action="/front/search/table_tableSearch">
			<!-- 表格检索类型 -->
			<input id="judge" value="true" type="hidden" />
			<input name="searchType" value="table" type="hidden" />
			<input type="hidden" value="Cn" name="searchscope" id="searchscope"/>
			

			<div id="mid" class="base">
				<div id="mytips"
					style="border-right: gray 1px solid; border-top: gray 1px solid; z-index: 2; display: none; left: 0px; border-left: gray 1px solid; border-bottom: gray 1px solid; position: absolute; text-align: center; font-size: 15px; background-color: #ffffff; vertical-align: middle;">
				</div>
				<div class="div_Content_xiwl home2_con">
					<div class="home_sel">
						<div class="home_sel1" style="width: 550px;">
							<label>
								<input checked="checked" type="radio" name="RadioGroup1" value="中国专利" id="Radio1"
									onclick="showDiv1();" />
								中国专利
							</label>
							<label>
								<input type="radio" name="RadioGroup1" value="世界专利"
									id="RadioGroup1_1" onclick="showDiv2();" />
								世界专利
							</label>
									<input checked style="margin-left: 20px;display:none" type="checkbox"
										id="patenttype_fm" value="FM" />
									<input checked style="margin-left: 20px;display:none" type="checkbox"
										id="patenttype_wg" value="WG" />
									<input checked style="margin-left: 20px;display:none" type="checkbox"
										id="patenttype_xx" value="XX" />
						</div>
					</div>
					<!-- /////////////////////////////////////////////////////////////////////////////////////////// -->
					<div id="divQueryTable" name="divTable1"
						class="home2_table div_xiwl" style="overflow: auto; ">

						<dl>
							<dt id="dtTbTI">
								发明名称(TI):
							</dt>
							<dd>
								<input name="cnDescriptionItem.title" type="text" id="Txt1"
									lang="Ti" onblur="validateBib(this)" title="" />

							</dd>
							<dt>
							</dt>
							<dd>

							</dd>
							<dt id="dtTbAN">
								申请号(AN):
							</dt>
							<dd>
								<input name="cnDescriptionItem.appno" type="text" id="Txt7"
									lang="An" onblur="validateBib(this)" title="" />

							</dd>
							<dt id="dtTbAD">
								申请日(AD):
							</dt>
							<dd>
								<input name="cnDescriptionItem.apdText" type="text" id="Txt8"
									lang="Ad" onblur="validateBib(this)" title="" />

							</dd>
							
							<dt id="dtTbPA">
								申请人(PA):
							</dt>
							<dd>
								<input name="cnDescriptionItem.appl" type="text" id="Txt5"
									lang="Pa" onblur="validateBib(this)" title="" />

							</dd>
							<dt id="dtTbIN">
								发明人(IN):
							</dt>
							<dd>
								<input name="cnDescriptionItem.inventor" type="text" id="Txt14"
									lang="In" onblur="validateBib(this)" title="" />

							</dd>
							
							
							<dt id="dtTbPN">
								公开号(PN):
							</dt>
							<dd>
								<input name="cnDescriptionItem.pubnr" type="text" id="Txt9"
									lang="Pn" onblur="validateBib(this)" title="" />

							</dd>
							<dt id="dtTbPD">
								公开日(PD):
							</dt>
							<dd>
								<input name="cnDescriptionItem.pudText" type="text" id="Txt10"
									lang="Pd" onblur="validateBib(this)" title="" />

							</dd>
							
							
							
							<dt id="dtTbGN">
								公告号(GN):
							</dt>
							<dd>
								<input name="cnDescriptionItem.appnr" type="text" id="Txt11"
									lang="Gn" onblur="validateBib(this)" title="" />

							</dd>
							<dt id="dtTbGD">
								公告日(GD):
							</dt>
							<dd>
								<input name="cnDescriptionItem.appdText" type="text" id="Txt12"
									lang="Gd" onblur="validateBib(this)" title="" />

							</dd>
							
							<dt id="dtTbTX">
								关键词(TX):
							</dt>
							<dd>
								<input name="cnDescriptionItem.keyword" type="text" id="Txt4"
									lang="Tx" onblur="validateBib(this)" title="" />

							</dd>
							<dt id="dtTbDZ">
								申请人地址(DZ):
							</dt>
							<dd>
								<input name="cnDescriptionItem.address" type="text" id="Txt16"
									lang="Dz" onblur="validateBib(this)" title="" />

							</dd>
							
							<dt id="dtTbMC">
								主分类号(MC):
							</dt>
							<dd>
								<input name="cnDescriptionItem.ipcMain" type="text" id="Txt19"
									name1="pattern" lang="MC" title="" />

							</dd>
							<dt id="dtTbIC">
								分类号(IC):
							</dt>
							<dd>
								<input name="cnDescriptionItem.ipcMain" type="text" id="Txt6"
									lang="Ic" onblur="validateBib(this)" title="" />

							</dd>
							
							<dt id="dtTbPR">
								优先权号(PR):
							</dt>
							<dd>
								<input name="xxx" type="text" id="Txt13" lang="Pr"
									onblur="validateBib(this)" title="" />

							</dd>
							<dt id="dtTbCO">
								国省代码(CO):
							</dt>
							<dd>
								<input name="cnDescriptionItem.nc" type="text" id="Txt17"
									name1="pattern" lang="Co" title="" autocomplete="on"
									class="ac_input ui-autocomplete-input" />

							</dd>
							
							<dt id="dtTbAG">
								代理机构(AG):
							</dt>
							<dd>
								<input name="cnDescriptionItem.agency" type="text" id="Txt18"
									name1="pattern" lang="Ag" title="" autocomplete="off"
									class="ac_input" />

							</dd>
							<dt id="dtTbAT">
								代理人(AT):
							</dt>
							<dd>
								<input name="cnDescriptionItem.agent" type="text" id="Txt20"
									name1="pattern" lang="AT" title="" />

							</dd>
							
							<dt id="dtTbCL">
								主权利要求(CL):
							</dt>
							<dd>
								<input name="cnDescriptionItem.claim" type="text" id="Txt3"
									lang="Cl" onblur="validateBib(this)" title="" />

							</dd>
							<dt id="dtTbAB">
								摘要(AB):
							</dt>
							<dd>
								<input name="cnDescriptionItem.abstr" type="text" id="Txt2"
									lang="Ab" onblur="validateBib(this)" title="" />

							</dd>
							
							
							
							<dt id="dtTbCS">
								权利要求(CS):
							</dt>
							<dd>
								<input  type="text" id="Txt21" name1="pattern" lang="CS"
									title="" />

							</dd>
							<dt id="dtTbDS">
								说明书(DS):
							</dt>
							<dd>
								<input  type="text" id="Txt22" name1="pattern" lang="DS"
									title="" />

							</dd>
							
							
							
							
							
							
							
							<dt id="dtTbCT" style="display:none">
								范畴分类(CT):
							</dt>
							<dd  style="display:none">
								<input name="cnDescriptionItem.fieldc" type="text" id="Txt15"
									lang="Ct" onblur="validateBib(this)" title="" />

							</dd>
							
							

							
						<!-- 	
						<dt id="dtTbCS">
								权利要求(CS):
							</dt>
							<dd>
								<input name="" type="text" id="Txt21" name1="pattern" lang="CS"
									title="" />

							</dd>
							<dt id="dtTbDS">
								说明书(DS):
							</dt>
							<dd>
								<input name="" type="text" id="Txt22" name1="pattern" lang="DS"
									title="" />

							</dd>
							 -->
						</dl>
						
						
						<div class="home2_tat2">
							<a href="javascript:;" onclick=cnClearSearch();> <img
									id="Img3" alt="清空检索式" src="/images/table/bt5.jpg"
									style="cursor: hand;" title="清空检索式" /> </a>
							<a href="javascript:;" onclick="buildCprsNew()"
								style="cursor: hand;"> <img id="Img2" alt="生成检索式"
									src="/images/table/bt4.jpg" style="cursor: hand;" title="生成检索式" />
							</a>
							<a href="javascript:;" id="imgBtnSearch1" onclick="synSearch()" >
								<img id="BtnSearch" alt="检索" src="/images/table/bt6.jpg"
									style="cursor: hand; float:right" title="专利检索" /> </a>
						</div>
					</div>
					<!-- ///////////////////////////////////////////////////////////////////////////////////////////////////// -->
					<div id="divQueryTable" name="divTable2"
						class="home2_table div_xiwl" style="overflow: auto;display: none">
						<table width="98%" border="0" cellspacing="7" cellpadding="0">
							<tr>
								<td width="146" height="35">
									<div class="home2_tat">
										<input checked  type="checkbox" id="ids" onclick="selectAll();" />
										全选：
									</div>
								</td>
								<td colspan="6">
									<div class="home3_set">
										<ul>
											<li>
												<label>
													<input checked name="ids" type="checkbox" value="CN" onchange="changeId()"/>
													中国
												</label>
											</li>
											<li>
												<label>
													<input checked name="ids" type="checkbox" value="US" onchange="changeId()"/>
													美国
												</label>
											</li>
											<li>
												<label>
													<input checked name="ids" type="checkbox" value="DE" onchange="changeId()"/>
													德国
												</label>
											</li>
											<li>
												<label>
													<input checked name="ids" type="checkbox" value="JP" onchange="changeId()"/>
													日本
												</label>
											</li>
											<li>
												<label>
													<input checked name="ids" type="checkbox" value="GB" onchange="changeId()" />
													英国
												</label>
											</li>
											<li>
												<label>
													<input checked name="ids" type="checkbox" value="FR" onchange="changeId()"/>
													法国
												</label>
											</li>
											<li>
												<label>
													<input checked name="ids" type="checkbox" value="KR" onchange="changeId()"/>
													韩国
												</label>
											</li>
											<li>
												<label>
													<input checked name="ids" type="checkbox" value="RU" onchange="changeId()"/>
													俄罗斯
												</label>
											</li>
											<li>
												<label>
													<input checked name="ids" type="checkbox" value="CH" onchange="changeId()"/>
													瑞士
												</label>
											</li>
											<li>
												<label>
													<input checked name="ids" type="checkbox" value="EP" onchange="changeId()"/>
													EPO
												</label>
											</li>
											<li>
												<label>
													<input checked name="ids" type="checkbox" value="WO" onchange="changeId()"/>
													WIPO
												</label>
											</li>
											<li style="display: ">
												<label>
													<input checked name="ids" type="checkbox" value="OT" onchange="changeId()"/>
													其他国家及地区&nbsp;
												</label>
											</li>
										</ul>
									</div>
								</td>
							</tr>
						</table>
						<dl>
							<dt id="dtTbTI">
								发明名称(TI)：
							</dt>
							<dd>
								<input name="enDescriptionItem.title" type="text"
									id="Txt23" class="searchinput" lang="Ti"
									onblur="validateBib(this)"
									title="&lt;p>&lt;strong>发明名称&lt;/strong>|例:computer&lt;/p>" />
							</dd>
							<dt id="dtTbAB">
								摘要(AB)：
							</dt>
							<dd>
								<input name="enDescriptionItem.abs" type="text"
									id="Txt24" class="searchinput" lang="Ab"
									onblur="validateBib(this)"
									title="&lt;p>&lt;strong>文摘&lt;/strong>|例:computer&lt;/p>" />
							</dd>
							<dt id="dtTbAN">
								申请号(AN):
							</dt>
							<dd>
								<input name="enDescriptionItem.appno" type="text"
									id="Txt25" class="searchinput" lang="An"
									onblur="validateBib(this)"
									title="&lt;p>&lt;strong>申请号&lt;/strong>|例:GB9818481A&lt;/p>" />
							</dd>		
							<dt id="dtTbAD">
								申请日(AD):
							</dt>
							<dd>
								<input name="enDescriptionItem.apdText" type="text"
									id="Txt26" class="searchinput" lang="Ad"
									onblur="validateBib(this)"
									title="&lt;p>&lt;strong>申请日&lt;/strong>&lt;br/>格式:(YYYY 或 YYYYMM 或 YYYYMMDD)&lt;br/>例:20021201&lt;/p>" />
							</dd>		
							<dt id="dtTbPN">
								公开/公告号(PN):
							</dt>
							<dd>
								<input name="enDescriptionItem.pubnr" type="text"
									id="Txt27" class="searchinput" lang="Pn"
									onblur="validateBib(this)"
									title="&lt;p>&lt;strong>公开号&lt;/strong>&lt;br/>例:GB9818481D&lt;/p>" />
							</dd>		
							<dt id="dtTbPD">
								公开/公告日(PD):
							</dt>
							<dd>
								<input name="enDescriptionItem.pudText" type="text"
									id="Txt28" class="searchinput" lang="Pd"
									onblur="validateBib(this)"
									title="&lt;p>&lt;strong>公开日&lt;/strong>&lt;br/>格式:(YYYY 或 YYYYMM 或 YYYYMMDD)&lt;br/>例:2003&lt;/p>" />
							</dd>		
							<dt id="dtTbPA">
								申请人(PA):
							</dt>
							<dd>
								<input name="enDescriptionItem.appl" type="text"
									id="Txt29" class="searchinput" lang="Pa"
									onblur="validateBib(this)"
									title="&lt;p>&lt;strong>申请人&lt;/strong>&lt;br/>例:tom&lt;/p>" />
							</dd>		
							<dt id="dtTbIN">
								发明人(IN):
							</dt>
							<dd>
								<input name="enDescriptionItem.inventor" type="text"
									id="Txt30" class="searchinput" lang="In"
									onblur="validateBib(this)"
									title="&lt;p>&lt;strong>发明人&lt;/strong>&lt;br/>例:tom&lt;/p>" />
							</dd>		
							<dt id="dtTbIC">
								分类号(IC):
							</dt>
							<dd>
								<input name="enDescriptionItem.ipcMinor" type="text"
									id="Txt31" class="searchinput" lang="Ic"
									onblur="validateBib(this)"
									title="&lt;p>&lt;strong>IPC 分类&lt;/strong>&lt;br/>例:A01B 或 A47J27/66&lt;/p>" />
							</dd>		
							<dt id="dtTbMC">
								主分类号(MC):
							</dt>
							<dd>
								<input name="enDescriptionItem.ipcMain" type="text"
									id="Txt32" class="searchinput" lang="Mc"
									onblur="validateBib(this)"
									title="&lt;p>&lt;strong>主分类号&lt;/strong>&lt;br/>例:A01B 或 A47J27/66&lt;/p>" />
							</dd>		
							<dt id="dtTbPR">
								优先权号(PR):
							</dt>
							<dd>
								<input name="enDescriptionItem.pris" type="text"
									id="Txt33" class="searchinput" lang="Pr"
									onblur="validateBib(this)"
									title="&lt;p>&lt;strong>优先权号&lt;/strong>&lt;br/>例:CN20020149&lt;/p>" />
							</dd>		
							<dt id="dtTbCT">
								引用文献(CT):
							</dt>
							<dd>
								<input name="enDescriptionItem.references" type="text"
									id="Txt34" class="searchinput" lang="Ct"
									onblur="validateBib(this)"
									title="&lt;p>&lt;strong>引用文献&lt;/strong>&lt;br/>例:CN20020149&lt;/p>" />
							</dd>		
							<dt id="dtTbEC">
								欧洲分类(EC):
							</dt>
							<dd>
								<input name="enDescriptionItem.euroIpc" type="text"
									id="Txt35" class="searchinput" lang="Ec"
									onblur="validateBib(this)"
									title="&lt;p>&lt;strong>欧洲分类&lt;/strong>&lt;br/>例:B60R21&lt;/p>" />
							</dd>		
						</dl>
						<div class="home2_tat2">
							<a href="javascript:;" onclick=cnClearSearch();> <img
									id="Img3" alt="清空检索式" src="/images/table/bt5.jpg"
									style="cursor: hand;" title="生成检索式" />
							</a>
							<a href="javascript:;" onclick="buildCprsNew()"> <img
									id="Img2" alt="生成检索式" src="/images/table/bt4.jpg"
									style="cursor: hand;" title="生成检索式" />
							</a>
							<a href="javascript:;" id="BtnSearch"
								onclick="synSearch()" style="float:right" 
> <img
									src="/images/table/bt6.jpg" title="专利检索" />
							</a>
						</div>
					</div>
					<!-- //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->





					<!--table-->
					<div class="home2_down div_xiwl">
						<div id="exampleCn">
							命令行检索 &nbsp;
							<font style="color: Red">[示例:计算机/TI+A01B/IC]</font>
						</div>
						  <div id="exampleDocDB" style="display: none">
                				命令行检索 &nbsp;<font style="color: Red">[示例:compute/TI+A01B/IC]</font>
              			  </div>
						<table>
							<tbody>
								<tr>
									<td>
										<textarea name="searchFormula" id="TxtSearch"
											class="home2_input"></textarea>
									</td>
									<td style="width: 5px">
									</td>
									<td>
										<a href="javascript:;" id="imgBtnSearch2" onclick=
	synSearch1();
>
											<img id="imgBtnSearch2" alt="检索"
												src="/images/table/btnSearch.png"
												style="cursor: hand; margin-top: 17px;" title="命令行检索" /> </a>
									</td>
								</tr>
							</tbody>
						</table>
						<div class="home2_d1" id="formulaCn">
							<ul>
								<li>
									<a href="javascript:;" value="*" name="*"
										onclick=
	addcommand(this);
> <img
											src="/images/table/home-09.jpg"
											title="&lt;strong&gt;应用示例&lt;/strong&gt;&lt;br/&gt;  A*B:同时包含A和B" />
									</a>
								</li>
								<li>
									<a href="javascript:;" value="+" name="+"
										onclick=
	addcommand(this);
> <img
											src="/images/table/home-10.jpg"
											title="&lt;strong&gt;应用示例&lt;/strong&gt;&lt;br/&gt; A+B:包含A或者B" />
									</a>
								</li>
								<li>
									<a href="javascript:;" value="-" name="-"
										onclick=
	addcommand(this);
> <img
											src="/images/table/home-11.jpg"
											title="&lt;strong&gt;应用示例&lt;/strong&gt;&lt;br/&gt;  A-B:包含A且不包含B" />
									</a>
								</li>
								<li>
									<a href="javascript:;" value="(" name="("
										onclick=
	addcommand(this);
> <img
											src="/images/table/home-12.jpg"
											title="&lt;strong&gt;应用示例&lt;/strong&gt;&lt;br/&gt;  （）:括号内的内容优先计算" />
									</a>
								</li>
								<li>
									<a href="javascript:;" value=")" name=")"
										onclick=
	addcommand(this);
> <img
											src="/images/table/home-13.jpg"
											title="&lt;strong&gt;应用示例&lt;/strong&gt;&lt;br/&gt; （）:括号内的内容优先计算" />
									</a>
								</li>
							</ul>
						</div>
			 <div class="home3_d1" id="formulaDocDB" style="display: none">
                <li><a href="javascript:;" value="*" name="*" onclick="addcommand(this)">
                    <img src="/images/table/home-09.jpg" title="应用示例：<br/>  A*B:同时包含A和B" /></a></li>
                <li><a href="javascript:;" value="+" name="+" onclick="addcommand(this)">
                    <img src="/images/table/home-10.jpg" title="应用示例：<br/> A+B:包含A或者B" /></a></li>
                <li><a href="javascript:;" value="-" name="-" onclick="addcommand(this)">
                    <img src="/images/table/home-11.jpg" title="应用示例：<br/>  A-B:包含A且不包含B" /></a></li>
                <li><a href="javascript:;" value="(" name="(" onclick="addcommand(this)">
                    <img src="/images/table/home-12.jpg" title="应用示例：<br/>  （）:括号内的内容优先计算" /></a></li>
                <li><a href="javascript:;" value=")" name=")" onclick="addcommand(this)">
                    <img src="/images/table/home-13.jpg" title="应用示例：<br/> （）:括号内的内容优先计算" /></a></li>
                <li><a href="javascript:;" value="$" name="$" onclick="addcommand(this)">
                    <img src="/images/table/home-14.jpg" title="应用示例：<br/>  A$:所有前缀包含A的单词" /></a></li>
                <li><a href="javascript:;">
                    <img value="ADJ" name="<ADJn>" onclick="addcommand(this)" src="/images/table/home-15.jpg"
                        title="应用示例：<br/> A&nbsp;ADJn&nbsp;B:A和B之间有0-n个词，且A和B前后顺序不能变化；<br/>n属于(0-9)" /></a></li>
                <li><a href="javascript:;">
                    <img value="NEAR" name="<NEARn>" onclick="addcommand(this)" src="/images/table/home-16.jpg"
                        title="应用示例：<br/>  A&nbsp;NEARn&nbsp;B:A和B之间有0-n个词，且A和B前后顺序可以变化；<br/>n属于(0-9)" /></a></li>
                <li><a href="javascript:;">
                    <img value="SKIP" name="<SKIPn>" onclick="addcommand(this)" src="/images/table/home-17.jpg"
                        title="应用示例：<br/>  ASKIPn&nbsp;B:A和B之间只能有n个词，词序不能变化；<br/>n属于(0-9)" /></a></li>
            </div>
						
						<div class="home2_d2">
							<li></li>
							<li style="padding-top: 6px; padding-left: 3px;"></li>
						</div>
						<div id="ResultBlock"
							style="display: none; font-size: 20px; text-align: left;">
							<hr />
							<span id="LabResult" style="height: 30px">命中5篇专利。<a
								href="http://211.160.117.107/My/cn/PatentGeneralList.aspx?No=001">查看</a>
							</span>
							<span id="LabValidationResult"></span>
							<select id="SlctLogicSymbol" size="1"
								style="text-align: left; height: 25px; font-size: medium"
								name="D1">
								<option value="-">
									-(非)
								</option>
								<option selected="selected" value="*">
									*(与)
								</option>
								<option value="+">
									+(或)
								</option>
							</select>
						</div>
					</div>
				</div>

			</div>
			<input type="hidden" id="ids" />
			<%@include file="/WEB-INF/page/front/share/footer.jsp"%>
		</form>

     
	</body>
</html>