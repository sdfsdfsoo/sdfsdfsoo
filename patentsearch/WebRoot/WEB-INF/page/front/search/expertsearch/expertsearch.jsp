<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>算式检索</title>
		<link href="/css/index.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="/js/jquery-ui/themes/base/jquery-ui.css" />
		<link href="${ctx}/js/easyui/themes/icon.css" rel="stylesheet"
			type="text/css" />
		<link href="/js/easyui/themes/default/easyui.css" rel="stylesheet"
			type="text/css" />
		<link rel="stylesheet" href="/css/law/law.css" />
		<link rel="stylesheet" type="text/css"
			href="/js/artdialog/ui-dialog.css" />
		<link href="/css/table/B_cprs2010.css" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
		<script src="/js/easyui/jquery.easyui.min.js"></script>
		<script src="/js/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script src="/js/jquery-ui/ui/jquery-ui.js"></script>
		<!-- <script type="text/javascript" src="/js/law/law.js"></script>-->
		<script type="text/javascript" src="/js/StrComm.js"></script>
		<script type="text/javascript" src="/js/expert/expert.js"></script>
		<script type="text/javascript" src="/js/artdialog/dialog.js"></script>
		<script type="text/javascript" src="/js/sysdialog.js"></script>
		<style>
.imgGray {
	filter: gray;
	filter: grayscale(1);
	-webkit-filter: grayscale(1);
}
</style>
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
		 
		  function sumbitForm(formula,id){
		     document.getElementById('searchFormula').value=formula;
             document.getElementById('searchFormulaEntityid').value=id;
             document.getElementById('formContent').submit();
		 }
          //  function subId(value,row,index){
          //  alert(value);
           //   var  subValue=value.substring(0,value.indexOf("_")); 
            //   var html='<a>'+subValue+'</a>';
             //   return html;
            //}
            function operaterLook(value,row,index){
              // var formula=row.formula.replace("//+/g","%2B");
                //alert("before"+formula);
               // alert("after"+row.formula);
              // var searchscope=document.getElementsByName("searchscope")[0].value;
              // var html="<a target='_blank' href='/front/search/table_tableSearch?searchType=expert&searchscope="+searchscope+"&searchFormula="+row.formula+"&searchFormulaEntity.id="+row.id+"'>执行检索</a>";
              // return html;
               var html='<a  href="javascript:void(0);" onClick="sumbitForm('+'\''+row.formula+'\''+','+'\''+row.id+'\''+')">执行检索</a>';
                return html;
            }
            function hits(value,row,index){
                   if(row.hits==0){
                  	 return '<font color=red>'+row.hits+'条</font>';
                   }else{
                  	 return '<font color=blue>'+row.hits+'条</font>';
                   }
                
            }
                //<!--请完成删除检索式功能 -->
            function deleteFormulas(){

            	if(!window.confirm("你确定删除吗？")){
					return false;
            	}
                
                var searchscope=getElementsNameForIE("input","searchscope")[0].value;
	            if(searchscope=="DocDB"){
	                  	 var row = $('#dgwd').datagrid("getSelections");
	           		}else{
	                  	 var row = $('#dg').datagrid("getSelections");
	           		}	
	         	 var rowids="";
	         	  for(i=0;i<row.length;i++){
	         		rowids=rowids+row[i].id+',';
	         	 }
	            if(rowids!=null&&rowids!=""){
	                var ids= rowids.substring(0,rowids.length-1);
	                $.ajax({
					  url: '/front/user/formula_deleteFormulas',
					   data:{"rowids":rowids,"searchscope":searchscope},  
					  success: success,
					  dataType: 'text'
					});  
				}else{
           		  		alert("请选择要删除的检索算式！");
					}		
			  if(rowids==null){
					 $.messager.alert('检索算式选择提示','请在检索算式删除操作之前，完成对检索算式的选择!','信息'); 
			  }	
            }
            //<!--请完成导出检索式功能 -->
           function exportFormulas(){
            var searchscope=getElementsNameForIE("input","searchscope")[0].value;
           		if(searchscope=="DocDB"){
                  	 var row = $('#dgwd').datagrid("getSelections");
           		}else{
                  	 var row = $('#dg').datagrid("getSelections");
           		}	
           		  var rowids="";
           		  for(i=0;i<row.length;i++){
           		  	rowids=rowids+row[i].id+',';
           		  }
           		  if(rowids!=null&&rowids!=""){
						window.location.href="/front/user/formula_exportFormulasToExcel?rowids="+rowids+"&searchscope="+searchscope;
					}else{
           		  		alert("请选择要导出的检索算式！");
					}					 		
				  if(rowids==null){
					$.messager.alert('检索算式选择提示','请在进行检索算式批量导出操作之前，完成对检索算式的选择!','信息'); 
				  }		
            }

			
        	             
           
           function inportFormulas(){
				return $('#fileupload').click();
           }
           function filechange(){
               var filename = $('#fileupload').val();

               var temp=filename.split(".")
               var last=temp[temp.length-1];
               
               if(last!='xls'){
                   alert('请上传正确格式的文件');
                   return false;

               }

               var radios=document.getElementsByName("RadioGroup1");
               for(var i=0;i<radios.length;i++){
					if(radios[i].checked){
						
						if(radios[i].value=='中国专利'){
							$('#searchscopeupload').val('0');
						}
						else{
							$('#searchscopeupload').val('1');
						}
						
					}
               }
				$('#uploadForm').submit();
           }
               //<!--待增加功能 -->
           function attachFunction(){

            }
            $(function (){
            var searchscope=getElementsNameForIE("input","searchscope")[0].value;
                $('#dg').datagrid({
                    url:'/front/user/formula_getSearchFormulaResult',
                    pagination:true,
                    checkbox:true,
                    height:530,
                    fitColumns:true,
                    multiSort:false,
                    queryParams: {
						searchscope: searchscope
					},
                    sortName:'id',
                    sortOrder:'asc',
					onLoadSuccess:function(data){
						$('#dg').datagrid('unselectAll');
               		},
                    columns:[[
                        {field:'ck',checkbox:true},
                        {field:'id',title:'检索编号',width:80},
                        {field:'hits',title:'命中记录条数',width:120,formatter:hits},
                        {field:'alterTime',title:'最新检索时间',width:300},
                        {field:'formula',title:'检索式',width:300},
                         {field:'operation',title:'操作',width:100,formatter:operaterLook}
                    ]]
                });

              
            });
           
        </script>
		<script type="text/javascript">
		//jQuery.ajaxSettings.traditional = true;
    function doSearch(){ 
     var searchscope=getElementsNameForIE("input","searchscope")[0].value;
     var strq = $('#SearchTextBox').val();
      strQuery = validateLogicSearchQuery(strq);  
        if (strQuery == "") {
                    $.messager.alert("警告","请输入检索式！");   
                    return;
                }
          else if (strQuery.indexOf("Error") != -1) {
                    showError(strQuery);
                    return;
                }
          $('#SearchTextBox').val(strQuery);
		    //
		    //生成检索式
			var searchFormula=$('#SearchTextBox').val();
		    //document.getElementById('tableSearchForm').submit();
		    //向后台添加检索式，并完成重新加载检索式
		    $.ajax({
			  url: '/front/user/formula_addSearchFormula',
			  data:{"searchFormula":searchFormula,"searchscope":searchscope},  
			  success: success,
			  dataType: 'text'
		    });
    }
    function success(response,status,xhr){
         var jsonObject=jQuery.parseJSON(response);
       	  $.messager.show({
				title:'检索式操作友情提示',
				msg:jsonObject.message,
				timeout:8000,
				showType:'slide',
				style:{
					right:'',
					top:document.body.scrollTop+document.documentElement.scrollTop,
					bottom:''
				}
			});
			if(jsonObject.result=='success'){
				 $('#dg').datagrid('reload');
				 $('#dgwd').datagrid('reload');
			}
    
    }
    function onLoadAlert(){  //RadioGroup1_1
	     if(document.getElementById('RadioGroup1_1').checked==true){
	      	showDiv2();
	     }
	     if(document.getElementById('RadioGroup1_1').checked==false){
	     	showDiv1();
	     }
    }
    
    
	function showDiv1() {
		$("#SearchTextBox").val("");
 		document.getElementById("searchscope1").value="Cn";
 		document.getElementById("searchscope2").value="Cn";
		 var showTable1=getElementsNameForIE("div","divTable1")[0]; 
		  var closeTable2=getElementsNameForIE("div","divTable2")[0]; 
 			showTable1.style.display="";
 			closeTable2.style.display="none";
 			 $('#dg').datagrid('reload');
	}
	function showDiv2() {

		$("#SearchTextBox").val("");
		
	$("#Lab111").tooltip({
					track : true,
					content :"asdasdas2222222dasdas"
				});
 		document.getElementById("searchscope1").value="DocDB";
 		document.getElementById("searchscope2").value="DocDB";
			 var showTable1=getElementsNameForIE("div","divTable1")[0]; 
			  var closeTable2=getElementsNameForIE("div","divTable2")[0]; 
  			showTable1.style.display="none";
  			closeTable2.style.display="";
 			 $('#dg').datagrid('reload');
 	      var searchscope=getElementsNameForIE("input","searchscope")[0].value;
          $('#dgwd').datagrid({
               url:'/front/user/formula_getSearchFormulaResult',
               pagination:true,
               checkbox:true,
               height:530,
               fitColumns:true,
               multiSort:false,
               sortName:'id',
               sortOrder:'asc',
               queryParams: {
					searchscope: searchscope
				},
               columns:[[
                   {field:'ck',checkbox:true},
                   {field:'id',title:'检索编号',width:80},
                   {field:'hits',title:'命中记录条数',width:120,formatter:hits},
                   {field:'alterTime',title:'最新检索时间',width:300},
                   {field:'formula',title:'检索式',width:300},
                    {field:'operation',title:'操作',width:100,formatter:operaterLook}
               ]]
           });
	}
</script>
	</head>
	<body onload="onLoadAlert();">
		<%@include file="/WEB-INF/page/front/share/top.jsp"%>

		<form id="formContent" action="/front/search/table_tableSearch"
			method="get">
			<input type="hidden" name="searchType" id="searchType" value="expert" />
			<input type="hidden" name="searchFormula" id="searchFormula" />
			<input type="hidden" name="searchFormulaEntity.id"
				id="searchFormulaEntityid" />
			<input type="hidden" name="searchscope" id="searchscope1" value="Cn" />
		</form>
		<div id="mid" class="base">
			<div class="div_Content_xiwl home2_con">
				<div class="home_sel">
					<div class="home_sel1">
						<label>
							<input type="radio" name="RadioGroup1" value="中国专利" id="Radio1"
								checked="checked" onclick="showDiv1();" />
							中国专利
						</label>
						<label>
							<input type="radio" name="RadioGroup1" value="世界专利"
								id="RadioGroup1_1" onclick="showDiv2();" />
							世界专利
						</label>
					</div>
				</div>
				<!--btn-->
				<div id="divQueryTable" class="div_xiwl home2_table"
					name="divTable1">
					<div class="home4_5 div_xiwl">
						<ul>
							<li id="Lab1" lang="An" value="申请号(AN)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="">
								申请号(AN)
							</li>
							<li id="Lab2" lang="Ad" value="申请日(AD)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="">
								申请日(AD)
							</li>
							<li id="Lab3" lang="Pn" value="公开号(PN)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="">
								公开号(PN)
							</li>
							<li id="Lab4" lang="Pd" value="公开日(PD)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="">
								公开日(PD)
							</li>
							<li id="Lab5" lang="Gn" value="公告号(GN)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="">
								公告号(GN)
							</li>
							<li id="Lab6" lang="Gd" value="公告日(GD)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="">
								公告日(GD)
							</li>
							<li id="Lab7" lang="Ic" value="分类号(IC)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="">
								分类号(IC)
							</li>
							<li id="Lab8" lang="Mc" value="主分类号(MC)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="">
								主分类号(MC)
							</li>
							<%--<li id="Lab9" lang="Ct" value="范畴分类(CT)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="">
								范畴分类(CT)
							</li>
							--%><li id="Lab10" lang="Pr" value="优先权号(PR)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="">
								优先权号(PR)
							</li>
							<li id="Lab11" lang="Co" value="国省代码(CO)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="">
								国省代码(CO)
							</li>
							<li id="Lab12" lang="In" value="发明人(IN)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="">
								发明人(IN)
							</li>
							<li id="Lab13" lang="Pa" value="申请人(PA)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="">
								申请人(PA)
							</li>
							<li id="Lab14" lang="TX" value="关键词(TX)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="">
								关键词(TX)
							</li>
							<li id="Lab15" lang="Ti" value="发明名称(TI)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="">
								发明名称(TI)
							</li>
							<li id="Lab16" lang="Ag" value="代理机构代码(AG)"
								style="cursor: pointer;" onclick="addSearchEntrance(this.lang)"
								title="">
								代理机构代码(AG)
							</li>
							<li id="Lab17" lang="At" value="代理人(AT)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="">
								代理人(AT)
							</li>
							<li id="Lab18" lang="Dz" value="申请人地址(DZ)"
								style="cursor: pointer;" onclick="addSearchEntrance(this.lang)"
								title="">
								申请人地址(DZ)
							</li>
							<li id="Lab19" lang="Ab" value="摘要(AB)" style="cursor: pointer;"
								onclick="addSearchEntrance(this.lang)" title="">
								摘要(AB)
							</li>
							<li id="Lab20" lang="Cl" value="主权利要求(CL)"
								style="cursor: pointer;" onclick="addSearchEntrance(this.lang)"
								title="">
								主权利要求(CL)
							</li>
							<li id="Lab21" lang="CS" value="权利要求(CS)"
								style="cursor: pointer;" onclick="addSearchEntrance(this.lang)"
								title="">
								权利要求(CS)
							</li>
							<li id="Lab22" lang="DS" value="说明书(DS)" style="cursor: pointer;"
								onclick="addSearchEntrance(this.lang)" title="">
								说明书(DS)
							</li>
						</ul>
					</div>

					<!--home4_1-->
					<div class=" home4_r ">
						<div id="cnSearchHisTable" class="home4_6 div_xiwl">
							<div id="ctl00_ContentPlaceHolder1_up">

								<table id='dg' toolbar="#toolbar"></table>
								<div id="toolbar">
									<a href="javascript:void(0)" class="easyui-linkbutton"
										iconCls="icon-remove" plain="true" onclick="deleteFormulas()">批量删除算式</a>
									<a href="javascript:void(0)" class="easyui-linkbutton"
										iconCls="icon-print" plain="true" onclick="exportFormulas()">批量导出算式</a>
									<a href="javascript:void(0)" class="easyui-linkbutton"
										iconCls="icon-add" plain="true" onclick="inportFormulas()">批量导入算式</a>
									<form name="uploadForm" id="uploadForm" action="table_uploadFormularFile" method="post" enctype="multipart/form-data">
										<input type="text" id="searchscopeupload" name="searchscopeupload" style="display:none"/>
										<input type="file" id="fileupload" name="fileupload" style="display:none" onchange="filechange()"/>
									</form>
									<!--<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="attachFunction()">查询算式</a>  -->
								</div>

							</div>
						</div>

					</div>
					<!--concent-->
					<div id="BibliographicValidation"
						style="font-style: italic; color: #FF0000">
						<div id="LabValidationResult">
						</div>
					</div>
				</div>
				<!-- ////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
				<!--btn-->
				<div id="divQueryTable" class="div_xiwl home2_table"
					style="display: none" name="divTable2">
					<div class="home4_5 div_xiwl">
						<ul>
							<li id="Lab23" lang="An" value="申请号(AN)" style="cursor: pointer;"
								onclick="addSearchEntrance(this.lang)" title="">
								申请号(AN)
							</li>
							<li id="Lab24" lang="Ad" value="申请日(AD)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="申请日格式:(YYYY 或 YYYYMM 或 YYYYMMDD)例:20021201/AD">
								申请日(AD)
							</li>
							<li id="Lab25" lang="Pn" value="公开/公告号(PN)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="公开号例:GB9818481D0/PN">
								公开/公告号(PN)
							</li>
							<li id="Lab26" lang="Pd" value="公开/公告日(PD)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="公开日格式:(YYYY 或 YYYYMM 或 YYYYMMDD)例:2003/PD 或 20030101/PD">
								公开/公告日(PD)
							</li>
							<li id="Lab27" lang="Ic" value="分类号(IC)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="IPC 分类例:A01B/IC 或 G06F17/30/IC">
								分类号(IC)
							</li>
							<li id="Lab28" lang="Mc" value="主分类号(MC)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="IPC 分类例:A01B">
								主分类号(MC)
							</li>
							<li id="Lab29" lang="Pr" value="优先权号(PR)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="优先权号例:CN20020149/PR">
								优先权号(PR)
							</li>
							<li id="Lab30" lang="In" value="发明人(IN)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="发明人例:tom/IN">
								发明人(IN)
							</li>
							<li id="Lab31" lang="Pa" value="申请人(PA)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="申请人例:tom/PA">
								申请人(PA)
							</li>
							<li id="Lab32" lang="Ti" value="发明名称(TI)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="发明名称例:computer/TI">
								发明名称(TI)
							</li>
							<li id="Lab33" lang="Ab" value="摘要(AB)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="文摘例:computer/AB">
								摘要(AB)
							</li>
							<li id="Lab34" lang="Ct" value="引用文献(CT)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="引用文献例:US5623614A/CT">
								引用文献(CT)
							</li>
							<li id="Lab35" lang="Ec" value="欧洲分类(EC)"
								onclick="addSearchEntrance(this.lang)" style="cursor: pointer;"
								title="欧洲分类例:B60R21/EC">
								欧洲分类(EC)
							</li>
						</ul>
					</div>

					<!--home4_1-->
					<div class=" home4_r ">
						<div id="cnSearchHisTable" class="home4_6 div_xiwl">
							<div id="ctl00_ContentPlaceHolder1_up">
								<div id="#toolbarwd">
									<a href="javascript:void(0)" class="easyui-linkbutton"
										iconCls="icon-remove" plain="true" onclick="deleteFormulas()">批量删除算式</a>
									<a href="javascript:void(0)" class="easyui-linkbutton"
										iconCls="icon-print" plain="true" onclick="exportFormulas()">批量导出算式</a>
									<a href="javascript:void(0)" class="easyui-linkbutton"
										iconCls="icon-add" plain="true" onclick="inportFormulas()">批量导入算式</a>
									<form runat="server" name="uploadForm" id="uploadForm" action="uploadFormularFile" method="post" enctype="multipart/form-data">
										<input type="text" id="searchscopeupload" name="searchscopeupload" style="display:none"/>
										<input type="file" id="fileupload" name="fileupload" style="display:none" onchange="filechange()"/>
									</form>
									
									<!--<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="attachFunction()">查询算式</a>  -->
								</div>
								<table id='dgwd' toolbar="#toolbarwd"></table>

							</div>
						</div>

					</div>
					<!--concent-->
					<div id="BibliographicValidation"
						style="font-style: italic; color: #FF0000">
						<div id="LabValidationResult">
						</div>
					</div>
				</div>

				<!-- /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
				<div class="home2_down div_xiwl">
					<!--  提交表单 -->
					<form id="tableSearchForm" action="/front/search/table_tableSearch">
						<input type="hidden" name="searchType" value="expert" />
						<input type="hidden" name="searchscope" id="searchscope2"
							value="Cn" />
						<table>
							<tr>
								<td>
									<textarea name="searchFormula" rows="2" cols="20"
										id="SearchTextBox" class="home2_input">
</textarea>
								</td>
								<td style="width: 5px">
								</td>
								<td>
									<a href="javascript:;"> <img id="BtnSearch" alt="生成算式"
											src="/images/expert/btnQuery.png"
											style="cursor: hand; height: 87px;" title="生成算式"
											onclick="doSearch()" /> </a>
								</td>
							</tr>
						</table>
					</form>
					<div class=" home4_d1">
						<ul>
							<li id="symbol1">
								<a href="javascript:;" value="*" name="*"
									onclick="addcommand(this)"> <img
										src="/images/expert/home-09.jpg" title="" /> </a>
							</li>
							<li id="symbol2">
								<a href="javascript:;" value="+" name="+"
									onclick="addcommand(this)"> <img
										src="/images/expert/home-10.jpg" title="" /> </a>
							</li>
							<li>
								<a href="javascript:;" value="-" name="-" id="symbol3"
									onclick="addcommand(this)"> <img
										src="/images/expert/home-11.jpg" title="" /> </a>
							</li>
							<li>
								<a href="javascript:;" value="(" name="(" id="symbol4"
									onclick="addcommand(this)"> <img
										src="/images/expert/home-12.jpg" title="" /> </a>
							</li>
							<li>
								<a href="javascript:;" value=")" name=")" id="symbol5"
									onclick="addcommand(this)"> <img
										src="/images/expert/home-13.jpg" title="" /> </a>
							</li>
						</ul>
					</div>
					<div class="home2_d2 ">

						<a href="javascript:;" onclick="ClearSearch()"> <img id="Img3"
								alt="清空算式" src="/images/expert/bt5.jpg" style="cursor: hand;"
								title="清空算式" /> </a>

					</div>
				</div>
			</div>
		</div>
		<%@include file="/WEB-INF/page/front/share/footer.jsp"%>
		<script type="text/javascript" src="/js/detail/detail.js"></script>
	</body>
</html>