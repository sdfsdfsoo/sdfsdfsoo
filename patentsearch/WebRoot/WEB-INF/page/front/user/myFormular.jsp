<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ include file="/WEB-INF/page/share/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
<script src="/js/jpages/jPages.js"></script>
		<link href="/js/jpages/jPages.css" rel="stylesheet" type="text/css" />
		<link href="/css/index.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="/js/jquery-ui/themes/base/jquery-ui.css" />
</head>
<style>
table {
table-layout: fixed;
}


td {
overflow: hidden;
white-space: nowrap;
text-overflow: ellipsis;
}
</style>
<body  style="width:780px">
<script>
function openUpload(){
	return $('#uploadfile').click();
}
function filechange(){
   var filename = $('#uploadfile').val();

   var temp=filename.split(".");
   var last=temp[temp.length-1];
   
   if(last!='xls'){
       alert('请上传正确格式的文件');
       return false;

   }
   $('#uploadFormularPackageForm').submit();
}
function delPackage(id){

	if(window.confirm("你确定删除吗？")){
		$.ajax({
			type:"post",
			url:"formula_delPackage",
			data:{
				packageid:id
			},
			success:function(data){
				alert(data);
				location.reload();
			}
		});
	}else{
		return false;
	}
	
	
}
$(function(){
	$("div.holder").jPages({
		containerID:"itemContainer"
	});
});
</script>
<div>
	<a href="javascript:openUpload()">上传</a>
	<form runat="server" name="uploadFormularPackageForm" id="uploadFormularPackageForm" action="formula_uploadFormularPackage" method="post" enctype="multipart/form-data">
		<input style="display:none" type="file" name="uploadfile" id="uploadfile" onchange="filechange()"/>
	</form>
</div>
	<%--<table  style="width:780px">
		<tr>
			<th style="width:30px">id</th>
			<th>addtime</th>
			<th>realname</th>
			<th>length</th>
			<th>operation</th>
		</tr>
		<c:forEach var="item" items="${formulaPackages}" varStatus="status">
		<tr>
			<td style="width:5px">${item.id }</td>
			<td>${item.addtime }</td>
			<td>${item.realname }</td>
			<td>${item.length }</td>
			<td><a href="<%=basePath %>${item.path}/${item.savename}" target="_blank">下载</a>
				<a href="javascript:delPackage(${item.id })">删除</a></td>
			</tr>
		</c:forEach>
	</table>
	
	--%><div class="record" style="width:740px">
			  
			   <ul class="record_th">
			     <li id="record_th1" style="width:5%">编号</li>
			     <li id="record_th2" style="width:30%">添加时间</li>
			     <li id="record_th3" style="width:25%">文件名称</li>
			     <li id="record_th4" style="width:15%">文件大小</li>
			     <li id="record_th5" style="width:23%">操作</li> 
			   </ul>
			   <div class="record_tab" id="itemContainer">
			   <c:forEach var="item" items="${formulaPackages}" varStatus="status">
			      <ul style="  text-align: center;">
			      
			        <li id="record_th1" style="width:5%">${item.id }</li>
			        <li id="record_th2" style="width:30%"><span>${item.addtime }</span></li>
			        <li id="record_th3" style="width:25%">${item.realname }</li>
			        <li id="record_th4" style="width:15%">${item.length }</li>
			        <li id="record_th5" style="width:23%"><a href="<%=basePath %>${item.path}/${item.savename}" target="_blank">下载</a>
				<a href="javascript:delPackage(${item.id })">删除</a></li>
			      
			      </ul>
			       </c:forEach> 
			   </div>
			   <div class="holder"></div>
			</div>
			
</body>

</html>