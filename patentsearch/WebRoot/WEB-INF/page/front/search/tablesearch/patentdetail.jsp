<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>

<script src="/js/jquery-ui/ui/jquery-ui.js"></script>
<link href="/css/index.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet"
	href="/js/jquery-ui/themes/base/jquery-ui.css" />

<title>专利信息详情</title>
</head>

<body style="  text-align: left;">
		<%@include file="/WEB-INF/page/front/share/top.jsp"%>

		<div id="mid" class="base" style="  padding-top: 0px;">
			<!--  tab开始 -->
			<div id="divPatDetailTabs" style="min-height: 550px; _height: 550px;">
				<ul id="ulPatTabs">
					<c:if test="${searchscope!='DocDB'}">	
					    <c:if test="${cnDescriptionItem.patentType!='外观设计'}">
							<li><a href="/front/search/table_detailTab1UI?cnDescriptionItem.appno=${cnDescriptionItem.appno}">著录项目信息</a></li>
							<li><a href="/front/search/table_detailTab2UI?cnDescriptionItem.appno=${cnDescriptionItem.appno}">全文PDF</a></li>
							<li><a href="/front/search/table_detailTab3UI?cnDescriptionItem.appno=${cnDescriptionItem.appno}">说明书</a></li>
							<li><a href="/front/search/table_detailTab4UI?cnDescriptionItem.appno=${cnDescriptionItem.appno}">权利要求</a></li>
							<li><a href="/front/search/table_detailTab5UI?cnDescriptionItem.appno=${cnDescriptionItem.appno}">法律状态</a></li>
						</c:if>
					    <c:if test="${cnDescriptionItem.patentType=='外观设计'}">
							<li><a href="/front/search/table_detailTab1UI?cnDescriptionItem.appno=${cnDescriptionItem.appno}">著录项目信息</a></li>
							<li><a href="/front/search/table_detailOutLookUI?cnDescriptionItem.appno=${cnDescriptionItem.appno}">外观图形</a></li>
							<li><a href="/front/search/table_detailTab5UI?cnDescriptionItem.appno=${cnDescriptionItem.appno}">法律状态</a></li>
						</c:if>
					</c:if>
					<c:if test="${searchscope=='DocDB'}">	
						<li><a href="/front/search/table_detailTab1EnUI?enDescriptionItem.pubnr=${enDescriptionItem.pubnr}">著录项目信息</a></li>
						<li><a href="/front/search/table_detailTab2EnUI?enDescriptionItem.pubnr=${enDescriptionItem.pubnr}">全文PDF</a></li>
						<li><a href="/front/search/table_detailTab5EnUI?enDescriptionItem.pubnr=${enDescriptionItem.pubnr}">法律状态</a></li>
					</c:if>
				</ul>
				<div id="tabMianXml" title="著录项目信息" style="padding: 10px;">
					<div></div>
				</div>

				<div id="divTabPdf" title="全文PDF" closable="false"
					style="padding: 2px;">
					<div class="center"></div>
				</div>

				<div id="divTabDes" title="说明书" closable="false"
					style="padding: 10px;">
					<div class="center"></div>
				</div>

				<div id="divTabClams" title="权利要求" icon="icon-reload"
					closable="false" style="padding: 10px;">
					<div class="center"></div>
				</div>

				<div id="divTabLegal" title="法律状态" closable="false"
					style="padding: 10px;">
					<div class="center"></div>
					<div></div>
				</div>
			</div>
		</div>

		<%@include file="/WEB-INF/page/front/share/footer.jsp"%>
		<script type="text/javascript" src="/js/detail/detail.js"></script>
</body>
</html>