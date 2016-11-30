<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>畅远专利检索分析系统</title>
<link href="/css/new/style.css" rel="stylesheet" type="text/css" />  
<link href="/css/new/jquery-ui.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
<script src="/js/jquery-ui/ui/jquery-ui.js"></script>

<script src="/js/StrComm.js"></script>
<script src="/js/search/simpleSearch.js"></script>
<script src="/js/search/smartpage.js"></script>
<style type="text/css">
.shadow{ width:100%; height:10px; background:url(/images/top/shadow.png) repeat-x; clear:both;}
</style>
<script type="text/javascript">
		$(document).ready(
				function() {
				//智能检索输入框提示
				var content='<p align=left>一般检索将检索项分为三类：数字字母混合类、纯数字类、其它类</br>数字字母混合类包括：IPC分类号(IC)、优先权(PR)；</br>'+
				'纯数字类包括：申请号(AN)、申请日(AD)、公告号(GN)、公告日(GD)、公开号(PN)、公开日(PD)；</br>'
				+'其它类包括：发明人(IN)、发明名称(TI)、申请人(PA)、主权利要求(CL)、摘要(AB)；</br>'+
				'<font color=red>注意：一般检索不对国省代码(CO)、申请人地址(DZ)、代理机构(AG)</font></p>'
				$("#searchContent").tooltip({
			track:true,
			content: content
		});
					var showUC = '0';
					//console.log(showUC);
					if (showUC == 0) {
						//$("span.userCenter").parent().hide();
						$("span.login").parent().show();
						$("span.register").parent().show();
						$("#Smartnavbom ul li").css('margin', '0px 38px');
					} else if (showUC == 1) {
						//$("span.login").parent().hide();
						//$("span.register").parent().hide();
						$("span.userCenter").parent().show();
						$("span.logout").parent().show();
						$("#userCenterLink").show();
						$("#Smartnavbom ul li").css('margin', '0px 20px');
					}

					$('#Smartnavbom a').hover(
							function() {
								var src = $(this).children("img").attr('src');
								//var x = src.substring(0, src.indexOf('-')) + "-2_bj.jpg";
								$(this).children("img").attr(
										'src',
										src.substring(0, src.indexOf('-'))
												+ "-2_bj.jpg");
							},
							function() {
								var src = $(this).children("img").attr('src');
								$(this).children("img").attr(
										'src',
										src.substring(0, src.indexOf('-'))
												+ "-1_bj.jpg");
							});
				});
	 function smartSearch(){ 
				 // 构建检索式与校验检索式
		  simpleSearchNew();
				 // 提交表单
          document.getElementById('searchForm').submit();
    }
    

//专利类型切换
var patentType = "cn";
function switchPatentType() {
    var obj = arguments[0];
    if (patentType == "cn" && obj.id == "btnPatentEn") {
        $("#btnPatentCn").attr("class", "btnPatentCnOff");
        $("#btnPatentEn").attr("class", "btnPatentEn");
        patentType = "en";
        document.getElementsByName("searchscope")[0].value="DocDB";
    } else if (patentType == "en" && obj.id == "btnPatentCn") {
        $("#btnPatentCn").attr("class", "btnPatentCn");
        $("#btnPatentEn").attr("class", "btnPatentEnOff");
        patentType = "cn";
      document.getElementsByName("searchscope")[0].value="Cn";
    }
}

function contentAreaChange(obj){
	if(obj.value.length>20) 
		obj.value=obj.value.substr(0,20);
}
	</script>

</head>
<body>
<div class="top">
<span>${user.name}&nbsp;&nbsp;
		      	<c:if test="${user.level=='1'}">
					<a href="javascript:alert('必须注册之后才能使用')">用户中心</a>
				</c:if>
				<c:if test="${user.level!='1'}">
					 <a href="/front/user/user_usercenter">用户中心</a>
				</c:if>
		    	 &nbsp;
		    	 
		    	 <c:if test="${user.level=='1'}">
					<a href="javascript:alert('必须注册之后才能使用')">
				</c:if>
				<c:if test="${user.level!='1'}">
					 <a  href="/front/user/user_profileUI">
				</c:if>
		      
		      
		      企业个性库</a>&nbsp;
		      <a href="/help/畅远专利检索分析系统操作手册.PDF" target="_blank">操作手册</a>&nbsp;
		      <a href="/front/user/user_logout" target="_parent">退出登录</a></span>
</div>
<div class="shadow"></div>
<div class="logo"><img src="/images/new/logo2.png"></div>
<div class="SearchLeftPad">
   <div id="BtnUl">
      <ul>
         <li><a id="btnPatentCn" class="btnPatentCn" onclick="switchPatentType(this)">中国专利检索 </a></li>
          <li><a id="btnPatentEn" class="btnPatentEnOff" onclick="switchPatentType(this)">世界专利检索</a></li>
      </ul>
   </div>
   <form action="/front/search/table_tableSearch" id="searchForm">
		<input name="searchType" value="smart" type="hidden" />
		<input name="searchscope" value="Cn" type="hidden" />
		<input id="strFinalQuery" name="searchFormula" type="hidden" />        
        <div id="divText">
			<textarea id="searchContent" name="keyword" class="simpleSearchTxb" title="" onpropertychange="contentAreaChange(this)" oninput="contentAreaChange(this)"></textarea>
            <a id="btnQuery_11" href="javascript:;"> <img id="BtnSearch" alt="检索" src="/images/new/search.png" title="命令行检索" onclick="smartSearch()" /></a>
           
		</div>
	</form>
    
     <div class="win">
       <ul>
          <li id="win6">
          
           		<c:if test="${user.level=='1'}">
					<a href="javascript:alert('必须注册之后才能使用')">
				</c:if>
				<c:if test="${user.level!='1'}">
					  <a  href="/front/user/user_profileUI">
				</c:if>
         
          
          	企业个性库</a></li>
          <li id="win1">
          
         		 <c:if test="${user.level=='1'||user.level=='2'}">
					<a href="javascript:alert('必须注册之后才能使用')">
				</c:if>
				<c:if test="${user.level!='1'&&user.level!='2'}">
					 <a href="/front/search/table_tableSearchUI">
				</c:if>
          	
          
          	高级检索</a></li>
          <li id="win2">
          
          		<c:if test="${user.level=='1'}">
					<a href="javascript:alert('必须注册之后才能使用')">
				</c:if>
				<c:if test="${user.level!='1'}">
					 <a href="/front/search/table_expertSearchUI">
				</c:if>
          
          
          算式检索</a></li>
          <li id="win3">
          
          		<c:if test="${user.level=='1'}">
					<a href="javascript:alert('必须注册之后才能使用')">
				</c:if>
				<c:if test="${user.level!='1'}">
					 <a href="/front/user/ipc_classNav">
				</c:if>
				
			分类检索</a></li>
       </ul> 
       <ul>
          <li id="win8"><a href="/help/畅远专利检索分析系统操作手册.PDF" target="_blank">在线帮助</a></li>
          <li id="win7">
          
          		<c:if test="${user.level=='1'}">
					<a href="javascript:alert('必须注册之后才能使用')">
				</c:if>
				<c:if test="${user.level!='1'}">
					 <a href="/front/user/user_usercenter">
				</c:if>
          
          用户中心</a></li>
          <li id="win4">
          		<c:if test="${user.level=='1'}">
					<a href="javascript:alert('必须注册之后才能使用')">
				</c:if>
				<c:if test="${user.level!='1'}">
					 <a href="/front/search/legal_legalStatusSearchUI">
				</c:if>
          中国法律状态检索</a></li>
          <li id="win5">
          <c:if test="${user.level=='1'}">
					<a href="javascript:alert('必须注册之后才能使用')">
				</c:if>
				<c:if test="${user.level!='1'}">
					 <a href="/front/search/table_searchHistoryUI">
				</c:if>
         历史记录</a></li>
       </ul>
    </div>
</div>
<div class="foot">建议使用谷歌浏览器<br>
开发公司：<a
			href="http://http://www.zjcykj.cn/" target="_blank"
			title="镇江畅远信息科技有限公司">江苏畅远信息科技有限公司</a>  |   技术支持：<a href="http://http://www.zjcykj.cn/" target="_blank"
			title="镇江畅远信息科技有限公司">电话：0511-82016169 传真：0511-88892923 邮箱：zjcy_88@163.com</a><br>
地址：镇江市新区丁卯经十五路99号中心研发区C30栋3楼</div>
</body>
</html>


