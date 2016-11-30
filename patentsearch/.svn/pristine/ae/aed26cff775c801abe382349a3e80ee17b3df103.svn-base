<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<body >
                  <b><span> ${enDescriptionItem.title }</span></b>
	 <table width="100%" cellpadding="3" border="0" style="word-wrap: break-word; word-break: break-all;">
                        <tr>
                            <td style="width: 125px; background-color: #dbe0e6; text-align: right; vertical-align: middle;">
                                公开/公告号：
                            </td>
                            <td style="width: 450px; text-align: left; vertical-align: middle;">
                                 ${enDescriptionItem.pubnr } [<a href='http://worldwide.espacenet.com/publicationDetails/biblio?DB=EPODOC&locale=cn_EP&FT=D&CC=WO&NR=2014085804A1&KC=A1' target='_blank'>欧局查看</a>]<br/> <c:if test="${enDescriptionItem.pubnrOriginal!=null }"> ${enDescriptionItem.pubnrOriginal }   <span title='申请国家的数据格式'>[原始]</span></c:if>
                                 
                            </td>
                            <td style="background-color: #dbe0e6; width: 125px; text-align: right; vertical-align: middle;">
                                申请号：
                            </td>
                            <td style="vertical-align: middle; word-break: break-all; width: 300px;">
                           	 ${enDescriptionItem.appno }  <span title='欧洲专利局加工整理的格式'>[欧局]</span><br/>${enDescriptionItem.originalAppno }   <span title='申请国家的数据格式'>[原始]</span>
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 125px; background-color: #dbe0e6; text-align: right; vertical-align: middle;">
                                公开/公告日：
                            </td>
                            <td style="width: 450px; text-align: left; vertical-align: middle;">
                                <a href='frmDoSq.aspx?db=EN&Query=F XX (20140605/PD)' target='_blank'> ${enDescriptionItem.pudText }</a>
                            </td>
                            <td style="background-color: #dbe0e6; width: 125px; text-align: right; vertical-align: middle;">
                                申请日：
                            </td>
                            <td style="vertical-align: middle; word-break: break-all; width: 300px;">
                                <a href='frmDoSq.aspx?db=EN&Query=F XX (20131202/AD)' target='_blank'>${enDescriptionItem.apdText}</a>
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 125px; background-color: #dbe0e6; text-align: right; vertical-align: middle;">
                                申请人：
                            </td>
                            <td style="width: 450px; text-align: left; vertical-align: middle;">
                               <c:forEach items="${applicantList}" var="entry">
										<a href='/front/search/table_tableSearch?searchType=table&searchFormula=F PA ${entry}'
											target='_blank'>${entry}</a>
								</c:forEach>
                            </td>
                            <td style="background-color: #dbe0e6; width: 125px; text-align: right; vertical-align: middle;">
                                发明人：
                            </td>
                            <td style="vertical-align: middle; word-break: break-all; width: 300px;">
                            		<c:forEach items="${inventorList}" var="entry">
										<a
									href='/front/search/table_tableSearch?searchType=table&searchFormula=F IN ${entry}'
									target='_blank'>${entry}</a>
									</c:forEach>
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 125px; background-color: #dbe0e6; text-align: right; vertical-align: middle;">
                                优先权：
                            </td>
                            <td colspan="2" style="vertical-align: middle; word-break: break-all; width: 570px;">
                               ${enDescriptionItem.pris}
                            </td>
                            <td rowspan="5">
                                <div id="divImgFt" class="center" style='border: 1px solid #CCC; padding: 2px; width: 300px;
                                    height: 300px; vertical-align: middle; line-height: 300px;'>
                                    <img id='ImageFt' src='${enDescriptionItem.futuURL}' onload="resizeFt(this,'${enDescriptionItem.futuURL}')" alt='摘要附图'/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 125px; background-color: #dbe0e6; text-align: right; vertical-align: middle;">
                                国际分类：
                            </td>
                            <td colspan="2" style="vertical-align: middle; word-break: break-all; width: 570px;">
                                ${enDescriptionItem.interIpc}
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 125px; background-color: #dbe0e6; text-align: right; vertical-align: middle;">
                                欧洲分类：
                            </td>
                            <td colspan="2" style="vertical-align: middle; word-break: break-all; width: 570px;">
                                  ${enDescriptionItem.euroIpc}
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 125px; background-color: #dbe0e6; text-align: right; vertical-align: middle;">
                                引用文献：
                            </td>
                            <td colspan="2" style="vertical-align: middle; word-break: break-all; width: 570px;">
                                  ${enDescriptionItem.references}
                                
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3" style="vertical-align: middle; word-break: keep-all; width: 725px;">
                                <b>摘 &nbsp;要：</b>
                                <span>
                                  ${enDescriptionItem.abs}
                                 </span> 
                            </td>
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
		<h3>同族专利</h3>
		<div id="divClaim1">
			<span id="LabelClaim"><span>${cnDescriptionItem.claim} </span>
				&nbsp;&nbsp;</span>
		</div>

		

		<h3>自定义标注</h3>
		<div></div>

	</div>
<script type="text/javascript">
$(function() {
	$("#divAccordion").accordion();
});
</script>
</body>
</html>