<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>



<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>

<script src="/js/jquery-ui/ui/jquery-ui.js"></script>
<link rel="stylesheet"
	href="/js/jquery-ui/themes/base/jquery-ui.css" />
<link href="/css/index.css" rel="stylesheet" type="text/css" />
<link href="/css/detail/tab5.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<div>
	<table class="gridView" cellspacing="0" rules="all" border="1" id="ctl00_ContentPlaceHolder1_GridViewLegal" style="width:100%;border-collapse:collapse;">
		<tbody> 
		<c:if test="${!empty cnLegalStatusList}">
		<c:forEach items="${cnLegalStatusList}" var="cnLegalStatus" varStatus="status">
		<tr onmouseover="currentcolor=this.style.backgroundColor;this.style.backgroundColor='#F3F3F3';" onmouseout="this.style.backgroundColor=currentcolor;">
			<td>
                                    <div class="gridViewItem">
                                        <div class="title">
                                            <b>申请号</b>：${cnLegalStatus.shenQingHWithDot}</div>
                                        <div class="note">
                                            <b>法律状态公告日</b>：${cnLegalStatus.legalDate}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>法律状态</b>：${cnLegalStatus.legalStatus}<br><b>法律状态信息</b>：${cnLegalStatus.legalStatusInfo}<br></div>
                                    </div>
                                </td>
		</tr>
		</c:forEach>
		</c:if>
		<c:if test="${empty cnLegalStatusList}">
		<tr class="empty">
			<td>
                            <img src="${legalstatus_friendly}" alt="">
                            暂无法律状态数据
                        </td>
		</tr>
		</c:if>
		
		
		
	</tbody></table>
</div>
</body>
</html>