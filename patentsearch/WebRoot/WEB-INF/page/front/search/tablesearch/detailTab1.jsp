﻿<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
 <script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
 <link href="/css/index.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet"
	href="/js/jquery-ui/themes/base/jquery-ui.css" />
<script src="/js/jquery-ui/ui/jquery-ui.js"></script>
<script type="text/javascript" src="/js/detail/tab1.js"></script>
</head>
<body>
	<sub><span style="font-size: 18px;">专利名称：${title}</span></sub>
	<table width="100%" cellpadding="3" border="0"
		style="border-right-width: 0px; width: 100%; vertical-align: middle; border-top-width: 0px; border-bottom-width: 0px; border-left-width: 0px"
		border="0" cellspacing="1" cellpadding="4">
		<tr>
			<td
				style="background-color: #dbe0e6; width: 125px; text-align: right; vertical-align: middle;">
				申请号:</td>
			<td style="width: 175px" id="tdApno">${cnDescriptionItem.appnoWithDot}</td>
			<td
				style="background-color: #dbe0e6; width: 130px; text-align: right; vertical-align: middle;">
				申请日:</td>
			<td style="width: 155px"><a
				href='/front/search/table_tableSearch?searchType=table&searchFormula=F AD ${apdPara}' target='_blank'>${cnDescriptionItem.apdString}</a>
			</td>
			<td
				style="background-color: #dbe0e6; width: 120px; text-align: right; vertical-align: middle;">
				国家/省市:</td>
			<td>
			<a
				href='/front/search/table_tableSearch?searchType=table&searchFormula=F CO ${cnDescriptionItem.nc}' target='_blank'>${ncText} </a>
			
			</td>
		</tr>
		<tr id="trFmXx">
			<td
				style="background-color: #dbe0e6; width: 125px; text-align: right; vertical-align: middle;">
				公开号:</td>
			<td style="width: 175px">${cnDescriptionItem.pubnr}</td>
			<td
				style="background-color: #dbe0e6; width: 130px; text-align: right; vertical-align: middle;">
				公开日:</td>
			<td style="width: 155px">
			<a
				href='/front/search/table_tableSearch?searchType=table&searchFormula=F PD ${pudPara}' target='_blank'>${cnDescriptionItem.pudString} </a>
			</td>
			<td
				style="background-color: #dbe0e6; width: 120px; text-align: right; vertical-align: middle;">
				主分类号:</td>
			<td>
			<a href='/front/search/table_tableSearch?searchType=table&searchFormula=F MC ${IpcMainPara}' target='_blank'>${cnDescriptionItem.ipcMain}</a>
			</td>
		</tr>
		<tr>
			<td
				style="background-color: #dbe0e6; width: 125px; text-align: right; vertical-align: middle;">
				授权公告号:</td>
			<td style="width: 175px">${cnDescriptionItem.appnr} </td>
			<td
				style="background-color: #dbe0e6; width: 130px; text-align: right; vertical-align: middle;">
				 授权公告日:</td>
			<td style="width: 155px"><a
				href='/front/search/table_tableSearch?searchType=table&searchFormula=F GD ${grpdPara}' target='_blank'>${cnDescriptionItem.grpdString} </a>
			</td>
			<td id="tdClassLab"
				style="background-color: #dbe0e6; width: 120px; text-align: right; vertical-align: middle;">
				分类号:</td>
			<td>
			<a href='/front/search/table_tableSearch?searchType=table&searchFormula=F IC ${IpcMainPara}' target='_blank'>${cnDescriptionItem.ipcMain}</a>; 
			<c:forEach items="${IpcMinorPara}" var="entry">
				<a href='/front/search/table_tableSearch?searchType=table&searchFormula=F IC ${entry.value}'
				target='_blank'>${entry.key}</a>;
			</c:forEach>
			</td>
		</tr>
		<tr>
			<td
				style="background-color: #dbe0e6; width: 135px; text-align: right; vertical-align: middle">
				申请人:</td>
			<td colspan="4" style="vertical-align: middle">
			<c:forEach items="${applicantList}" var="entry">
					<a
				href='/front/search/table_tableSearch?searchType=table&searchFormula=F PA ${entry}'
				target='_blank'>${entry}</a>
				</c:forEach>

</td>
			<td style="text-align: center; vertical-align: middle; width: 350px;"
				rowspan="7">
				<div id="divImgFt" class="center"
					style='border: 1px solid #CCC; padding: 2px; width: 300px; height: 300px; vertical-align: middle; line-height: 300px;'>
					<img id='ImageFt' src='${cnDescriptionItem.futuURL}' style="width: 300px;height:300px"
						alt='摘要附图' />
				</div>
			</td>
		</tr>
		<tr>
			<td
				style="background-color: #dbe0e6; width: 135px; text-align: right; vertical-align: middle">
				发明人:</td>
			<td colspan="4"
				style="vertical-align: middle; word-break: break-all; width: 600px;">
				<c:forEach items="${inventorList}" var="entry">
					<a href='/front/search/table_tableSearch?searchType=table&searchFormula=F IN ${entry}' target='_blank'>${entry}</a>
				</c:forEach>
			
			</td>
		</tr>
		<tr>
			<td
				style="background-color: #dbe0e6; width: 135px; text-align: right; vertical-align: middle">
				代理人:</td>
			<td colspan="4"
				style="vertical-align: middle; word-break: break-all; width: 600px;">
				<a
				href='/front/search/table_tableSearch?searchType=table&searchFormula=F AT ${cnDescriptionItem.agent}'
				target='_blank'>${cnDescriptionItem.agent}</a>
				</td>
		</tr>
		<tr>
			<td
				style="background-color: #dbe0e6; width: 135px; text-align: right; vertical-align: middle">
				代理机构:</td>
			<td colspan="4"
				style="vertical-align: middle; word-break: break-all; width: 600px;">
				
				<a
				href='/front/search/table_tableSearch?searchType=table&searchFormula=F AG ${cnDescriptionItem.agency}' target='_blank'>${agencyText} </a>
				</td>
		</tr>
		<tr>
			<td
				style="background-color: #dbe0e6; width: 135px; text-align: right; vertical-align: middle">
				申请人地址:</td>
			<td colspan="4"
				style="vertical-align: middle; word-break: break-all; width: 600px;">
				<a
				href='/front/search/table_tableSearch?searchType=table&searchFormula=F DZ ${cnDescriptionItem.address}'
				target='_blank'>${cnDescriptionItem.address}</a>
				</td>
		</tr>
		<tr>
			<td
				style="background-color: #dbe0e6; width: 135px; text-align: right; vertical-align: middle">
				优先权:</td>
			<td colspan="4"
				style="vertical-align: middle; word-break: break-all; width: 600px;">
				<c:forEach items="${cnDescriptionItem.pris}" var="cnDescriptionItem_PRI" varStatus="status">
				${cnDescriptionItem_PRI.co}${cnDescriptionItem_PRI.nr}-${cnDescriptionItem_PRI.dateString}
				</c:forEach>

			</td>
		</tr>
		<tr>
			<td style="text-align: left;" colspan="5"><b> <span
					id="ctl00_ContentPlaceHolder1_labAbsJYSM">摘要：</span></b> <span>${cnDescriptionItem.abstr}</span>
				&nbsp;&nbsp;<!--<a href='javascript:void(0);'
				onclick="transABS(this,'摘要汉英机器翻译','CN')"> <img title='翻译'
					src='../images/Trans_20.jpg' /></a> --></td>
		</tr>
	</table>
	<table>
		<tr>
			<td style="width: 70%; text-align: left;"></td>
			<td style="width: 175px; vertical-align: middle; text-align: center;">
			</td>
		</tr>
	</table>

	<div id="divAccordion">
		<h3>主权利要求</h3>
		<div id="divClaim1">
			<span id="LabelClaim"><span>${cnDescriptionItem.claim} </span>
				&nbsp;&nbsp;</span>
		</div>

		<%--<h3>自动标引</h3>
		<div id="divAutoIndexWord"></div>

		<h3>自定义标注</h3>
		<div></div>

	--%></div>
<script type="text/javascript">
$(function() {
	$("#divAccordion").accordion();
});
</script>
</body>
</html>