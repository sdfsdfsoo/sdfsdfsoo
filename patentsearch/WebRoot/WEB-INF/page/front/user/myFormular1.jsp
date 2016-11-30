<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ include file="/WEB-INF/page/share/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
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
   var filename = $('#fileupload').val();

   var temp=filename.split(".")
   var last=temp[temp.length-1];
   
   if(last!='xls'){
       alert('请上传正确格式的文件');
       return false;

   }
   $('#uploadFormularPackageForm').submit();
</script>
<div>
	<a href="javascript:openUpload()">上传</a>
	<form runat="server" name="uploadFormularPackageForm" id="uploadFormularPackageForm" action="uploadFormularPackage" method="post" enctype="multipart/form-data">
		<input type="file" name="uploadfile" id="uploadfile" onchange="filechange()"/>
	</form>
</div>
	<table  style="width:780px">
		<tr>
			<th style="30%">alertTime</th>
			<th style="40%">formula</th>
			<th style="30%">hits</th>
		</tr>
		<c:forEach var="item" items="${searchFormulas}" varStatus="status">
		<tr>
			<td>${item.alterTime }</td>
			<td>${item.formula }</td>
			<td>${item.hits }</td>
			</tr>
		</c:forEach>
	</table>
</body>

</html>