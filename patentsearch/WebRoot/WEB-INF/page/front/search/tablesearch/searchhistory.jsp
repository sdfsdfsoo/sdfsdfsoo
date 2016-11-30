<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>一般检索</title>
		<link rel="stylesheet" href="/js/jquery-ui/themes/base/jquery-ui.css" />
		<link href="/css/index.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
		<script src="/js/jquery-ui/ui/jquery-ui.js"></script>
		<script src="/js/StrComm.js"></script>
		<script src="/js/search/simpleSearch.js"></script>
		<script src="/js/jpages/jPages.js"></script>
		<link href="/js/jpages/jPages.css" rel="stylesheet" type="text/css" />
		

	</head>
	<body>
		<%@include file="/WEB-INF/page/front/share/top.jsp"%>
			
			
			<div class="record">
			   <ul>
			     <li><strong>检索历史</strong></li>
			   </ul>
			   <ul class="record_th">
			     <li id="record_th1">编号</li>
			     <li id="record_th2">检索式</li>
			     <li id="record_th3">检索时间</li>
			     <li id="record_th4">结果数量</li>
			     <li id="record_th5">操作</li> 
			   </ul>
			   <div class="record_tab" id="itemContainer">
			   <c:forEach var="item" items="${searchHistorys}" varStatus="status">
			      <ul style="  text-align: left;">
			      
			        <li id="record_th1">${status.count}</li>
			        <li id="record_th2"><span>${item.searchformula}</span></li>
			        <li id="record_th3">${item.addtime}</li>
			        <li id="record_th4">${item.resultnum}</li>
			        <li id="record_th5"><a href="${basePath}/front/search/table_tableSearch?searchType=smart&searchscope=Cn&searchFormula=${item.searchformula}">执行结果</a> 
			        <a href="javascript:delHistory(${item.id})">删除</a></li>
			      
			      </ul>
			       </c:forEach> 
			   </div>
			   <div class="holder"></div>
			</div>
			
			
			
			<script>
			$(function(){
				$("div.holder").jPages({
					containerID:"itemContainer",
					perPage:20
				});
			});
				function delHistory(historyId){
					if(!window.confirm("确定删除吗？")){
						return false;
					}
					$.ajax({
						url:'/front/search/table_delHistory',
						data:{
							historyId:historyId
						},
						type:'post',
						dataType:'text',
						success:function(data){
							window.location.reload();
						},
						error:function(data){
							alert("服务器连接失败");
						}
					});
				}
			</script>

			<%@include file="/WEB-INF/page/front/share/footer.jsp"%>
	</body>
</html>
