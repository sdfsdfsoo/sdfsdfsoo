<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
$(function(){
  $("#hide").click(function(){
  $("#handle_show").hide();
  $("#handle_hid").show();
  });
  $("#show").click(function(){
  $("#handle_show").show();
  $("#handle_hid").hide();
  });
  $('#tw').click(function(){
		$(this).parent().addClass('zsfs').siblings().removeClass('zsfs');
	    $("#lb_show").hide();
        $("#tw_show").show();	
	  });
  $('#lb').click(function(){
		$(this).parent().addClass('zsfs').siblings().removeClass('zsfs');
	    $("#tw_show").hide();
        $("#lb_show").show();	
	  });
 $('.help_close').click(function(){
	    $(".help").hide();
        $(".help_open").show();	
	  });
 $('.help_open').click(function(){
	    $(".help_open").hide();
        $(".help").show();	
	  });
});
</script>
 <script>
	var lanren = {
		navFast:function(){
			var defaultTop = (window.screen.height - $('.handle').height())/2 - 100; //默认顶部保持上下居中，再往上去100像素
			$(window).scroll(function(){
				var offsetTop = defaultTop + $(window).scrollTop()+'px';
				$('.handle').animate({top:offsetTop},
				{	duration: 600,	//滑动速度
     				queue: false    //此动画将不进入动画队列
     			});						  
			});
		}
	};
	lanren.navFast();
</script>
	<style>
		
		#topdiv{ background:#f3f3f3;margin:0 auto; text-align:center; font-family:"����"; color:#666; font-size:14px;}
		div{ margin:0 auto; padding:0px;}
		ul,li,dl,dt,dd{ margin:0px; padding:0px;}
		.head{ width:100%;height:50px;line-height:50px; font-size:12px;background:#FFF;}
		.head ul{ width:1000px; margin:0 auto; text-align:left;}
		.head span{ float:right;}
		.head a{ color:#666; text-decoration:underline;}
		.shadow{ width:100%; height:10px; background:url(/images/top/shadow.png) repeat-x; clear:both;}
		.menunew{ width:960px;height:98px; background:#FFF; border:#ddd solid 1px; margin-top:19px; padding-left:19px; padding-right:10px;}
		.menunew li{ width:160px; height:98px;float:left; list-style:none;}
		.menunew a{ color:#0076bc; text-decoration:none;}
		.menunew li span{ width:160px; height:68px; display:block;}
		#separate{ background:url(/images/top/separate.png) right center no-repeat;}
		#menu1{ background:url(/images/top/menu1.png) center no-repeat;}
		#menu2{ background:url(/images/top/menu2.png) center no-repeat;}
		#menu3{ background:url(/images/top/menu3.png) center no-repeat;}
		#menu4{ background:url(/images/top/menu4.png) center no-repeat;}
		#menu5{ background:url(/images/top/menu5.png) center no-repeat;}
		#menu6{ background:url(/images/top/menu6.png) center no-repeat;}
		
		.help{ width:100%; height:60px;opacity:0.9;position:fixed;bottom:0px;z-index:2; background:#333; padding-bottom:10px; padding-top:10px; text-align:left; color:#fff; line-height:30px; font-size:20px;font-family:"Microsoft YaHei"; }
		.help span{ float:left;margin-left:50px;height:60px;}
		.help .word{ background:url(../images/new/word.png) no-repeat left; padding-left:70px; font-size:14px;}
		.help .word a{ color:#fff;}
		.help_close{ float:right; width:80px; line-height:60px; font-size:16px; text-align:center; cursor:pointer;}
		.help_open{ float:left; width:80px; line-height:60px; font-size:16px;height:60px;opacity:0.9;position:fixed;bottom:0px;z-index:2; background:#333; padding-bottom:10px; padding-top:10px; text-align:left; color:#fff;font-family:"Microsoft YaHei"; text-align:center; cursor:pointer; display:none;}
				
	</style>
	<div id="topdiv">
	
	
	
		<div class="head">
		   <ul>
		      <span>${user.name}&nbsp;&nbsp;
		      	<c:if test="${user.level==1}">
					<a href="javascript:alert('必须注册之后才能使用')">
				</c:if>
				<c:if test="${user.level!=1}">
					 <a href="/front/user/user_usercenter">
				</c:if>
		    	 用户中心</a>&nbsp;
		    	 
		    	 <c:if test="${user.level==1}">
					<a href="javascript:alert('必须注册之后才能使用')">
				</c:if>
				<c:if test="${user.level!=1}">
					 <a  href="/front/user/user_profileUI">
				</c:if>
		      
		      
		      企业个性库</a>&nbsp;
		      <a href="/help/畅远专利检索分析系统操作手册.PDF" target="_blank">操作手册</a>&nbsp;
		      <a href="/front/user/user_logout" target="_parent">退出登录</a></span>
		      <img src="/images/top/title.png">
		   </ul>
		</div>
		<div class="shadow"></div>
		<div class="menunew">
		   <ul>
		      <li id="separate"><a href="/front/search/table_smartSearchUI" target=""><span id="menu1"></span>一般检索</a></li>
		      <li id="separate">
		      	<c:if test="${user.level==1||user.level==2}">
					<a href="javascript:alert('必须注册之后才能使用')">
				</c:if>
				<c:if test="${user.level!=1&&user.level!=2}">
					 <a href="/front/search/table_tableSearchUI" target="">
				</c:if>
		      
		      
		     <span id="menu2"></span> 高级检索</a></li>
		      <li id="separate">
		      	<c:if test="${user.level==1}">
					<a href="javascript:alert('必须注册之后才能使用')">
				</c:if>
				<c:if test="${user.level!=1}">
					<a href="/front/search/table_expertSearchUI" target="">
				</c:if>
		      
		      
		      <span id="menu3"></span>算式检索</a></li>
		      <li id="separate">
		      
		      	<c:if test="${user.level==1}">
					<a href="javascript:alert('必须注册之后才能使用')">
				</c:if>
				<c:if test="${user.level!=1}">
					<a href="/front/user/ipc_classNav" target="">
				</c:if>
		      
		      
		      <span id="menu4"></span>分类检索</a></li>
		      <li id="separate">
		      	<c:if test="${user.level==1}">
					<a href="javascript:alert('必须注册之后才能使用')">
				</c:if>
				<c:if test="${user.level!=1}">
					<a href="/front/search/legal_legalStatusSearchUI" target="">
				</c:if>
		      
		      
		      <span id="menu5"></span>中国法律状态检索</a></li>
		      <li>
		      <c:if test="${user.level==1}">
					<a href="javascript:alert('必须注册之后才能使用')">
				</c:if>
				<c:if test="${user.level!=1}">
					<a href="/front/search/table_searchHistoryUI" target="">
				</c:if>
		      
		      
		     <span id="menu6"></span> 历史记录</a></li>
		   </ul>
		</div>
	</div>	
	
	<div class="help" style="  left: 0px;">
   <div class="help_close">隐藏</div>
   <span><strong>说明<br>文档</strong></span>
   <span class="word"><a href="/help/国际IPC分类号.doc">国际IPC<br/>分类号</a></span>
   <span class="word"><a href="/help/逻辑运算符解释说明.doc">逻辑运算符<br/>解释说明</a></span>
   <span class="word"><a href="/help/检索代码符对比说明.doc">检索代码符<br/>解释说明</a></span>
</div>
<div class="help_open" style="left:0px">展开>></div>