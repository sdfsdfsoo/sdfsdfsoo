<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/WEB-INF/page/share/taglib.jsp"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<link href="/css/index.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="/js/jquery-ui/themes/base/jquery-ui.css"/>
		<link rel="stylesheet" type="text/css" href="/js/artdialog/ui-dialog.css"/>

		<link rel="stylesheet" type="text/css" href="/css/analysis/compare.css"/>
		<script type="text/javascript" src="/js/artdialog/dialog.js"></script>
		<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
		<script src="/js/jquery-ui/ui/jquery-ui.js"></script>
		<script src="/js/jquery-ui/ui/i18n/jquery.ui.datepicker-zh-CN.js"></script>
		<title>专利对比</title>
		<script type="text/javascript">
		function selectLeftPatent(object){
		   var appno_left=object.value;
		   document.getElementById('appno_left').value=object.value;
		   var appno_right=document.getElementById('appno_right').value;
		   var appnos=document.getElementById('appnos').value;
		   window.location.href = "/front/user/analysis_analysisUI?appnos="+appnos+"&itemLeft.appno="+appno_left+"&itemRight.appno="+appno_right;
		}
		function selectRightPatent(object){
		   var appno_left=document.getElementById('appno_left').value;
		   var appno_right=object.value;
		   document.getElementById('appno_right').value=object.value;
		    var appnos=document.getElementById('appnos').value;
		   window.location.href = "/front/user/analysis_analysisUI?appnos="+appnos+"&itemLeft.appno="+appno_left+"&itemRight.appno="+appno_right;
		}
		</script>
	</head>
	<body>
			
			<%@include file="/WEB-INF/page/front/share/top.jsp"%>
			  <input name="appnos" id="appnos" type="hidden" value="${appnos}"/> 
			 
			<div id="mid" class="base" style="  padding-top: 0px;">
				<div class="command" style="float: none">
					<div class="commandLeft">
						专利(左边):
                         <input name="itemLeft.appno" id="appno_left" type="hidden" value="${itemLeft.appno}"/>  
						 <select   onchange="selectLeftPatent(this)" style="width:400px;height:30px">
						   <option value="" >
									请选择：
								</option>
							<c:forEach items="${resultList}" var="map"
								varStatus="status">
								
								<option value="${map.appno }" <c:if test="${map.appno==itemLeft.appno}">selected</c:if>	 >
									${map.title }(${map.appno })
								</option>
							</c:forEach>
						</select>
					</div>
					<div class="compareRight">
						专利(右边):
						<input name="itemRight.appno"  id="appno_right" type="hidden" value="${itemRight.appno}"/>  
						<select   onchange="selectRightPatent(this)" style="width:400px;height:30px">
						 <option value="" >
									请选择：
								</option>
							<c:forEach items="${resultList}" var="map"
								varStatus="status">
								<option value="${map.appno }" <c:if test="${map.appno==itemRight.appno}">selected</c:if>	 >
									${map.title }(${map.appno })
								</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="box">
					<div class="boxOutter">
						<div class="boxInner">
							<div id="xiangmu">
								<div>
									<table cellspacing="1" class="compare">
										<tr>
											<th colspan="4"
												style="text-align: center; border-right: #6595d6 1px solid;">
												<a
													href=''
													target='_blank'>${itemLeft.title}</a>
											</th>
											<th colspan="4" style="text-align: center;">
												<a
													href=''
													target='_blank'>${itemRight.title}</a>
											</th>
										</tr>
										<tr>
											<th width="10%">
												申请日：
											</th>
											<td width="15%">
												${itemLeft.apdText}
											</td>
											<th width="10%">
												申请号：
											</th>
											<td width="15%" style="border-right: #6595d6 1px solid;">
												${itemLeft.appno}
											</td>
											<th width="10%">
												申请日：
											</th>
											<td width="15%">
												${itemRight.apdText}
											</td>
											<th width="10%">
												申请号：
											</th>
											<td width="15%">
												${itemRight.appno}
											</td>
										</tr>
										<tr>
											<th>
												发明人：
											</th>
											<td>
												${itemLeft.inventor}
											</td>
											<th>
												申请人：
											</th>
											<td style="border-right: #6595d6 1px solid;">
												${itemLeft.appl}
											</td>
											<th>
												发明人：
											</th>
											<td>
												${itemRight.inventor}
											</td>
											<th>
												申请人：
											</th>
											<td>
												${itemRight.appl}
											</td>
										</tr>
										<tr>
											<th>
												国家/省市：
											</th>
											<td>
												${itemLeft.nc}
											</td>
											<th>
												申请人地址：
											</th>
											<td style="border-right: #6595d6 1px solid;">
												${itemLeft.address}
											</td>
											<th>
												国家/省市：
											</th>
											<td>
												${itemRight.nc}
											</td>
											<th>
												申请人地址：
											</th>
											<td>
												${itemRight.address}
											</td>
										</tr>
										<tr>
											<th>
												公开号：
											</th>
											<td>
												${itemLeft.pubnr}
											</td>
											<th>
												公开日：
											</th>
											<td style="border-right: #6595d6 1px solid;">
												${itemLeft.pudText}
											</td>
											<th>
												公开号：
											</th>
											<td>
												${itemRight.pubnr}
											</td>
											<th>
												公开日：
											</th>
											<td>
												${itemRight.pudText}
											</td>
										</tr>
										<tr id="ctl00_ContentPlaceHolder1_gonggao">
											<th>
												授权公告号：
											</th>
											<td>
												${itemLeft.appnr}
											</td>
											<th>
												授权公告日：
											</th>
											<td style="border-right: #6595d6 1px solid;">
												${itemLeft.appdText}
											</td>
											<th>
												授权公告号：
											</th>
											<td>
												${itemRight.appnr}
											</td>
											<th>
												授权公告日：
											</th>
											<td>
												${itemRight.appdText}
											</td>
										</tr>

										<tr>
											<th>
												优先权：
											</th>
											<td colspan="3">

											</td>
											<th>
												优先权：
											</th>
											<td colspan="3">

											</td>
										</tr>
									</table>
								</div>
							</div>
							<div id="accordion">
								<h3>
									摘要信息
								</h3>
								<div>
									<table cellspacing="1" class="compare">
										<tr>
											<td style="border-right: #6595d6 1px solid;" width="50%">
												 ${itemLeft.abstr} 
											</td>
											<td>
												 ${itemRight.abstr}
											</td>
										</tr>
									</table>
								</div>
								<h3>
									摘要附图
								</h3>
								<div>
									<table cellspacing="1" class="compare">
										<tr>
											<td width="50%" style="border-right: #6595d6 1px solid;">
												<img id="ctl00_ContentPlaceHolder1_ImageFtA"
													src="${futuURL_left}"
													style="height: 200px; width: 300px; border-width: 0px;" />
											</td>
											<td>
												<img id="ctl00_ContentPlaceHolder1_ImageFtB"
													src="${futuURL_right}"
													style="height: 200px; width: 300px; border-width: 0px;" />
											</td>
										</tr>
									</table>
								</div>
								<h3>
									法律状态
								</h3>
								<div>
									<table cellspacing="1" class="compare">
										<tr>
											<td width="50%" style="border-right: #6595d6 1px solid;">
											<c:if test="${itemLeft.appno !=''&& itemLeft.appno !=null}">
											<c:if test="${cnLegalStatusList_left !=''&& cnLegalStatusList_left !=null}">
												<iframe id='irmLagel'
													src='/front/search/table_detailTab6UI?cnDescriptionItem.appno=${itemLeft.appno}'
													style='z-index: 0;' frameborder='0' width='100%'
													height='300'></iframe>
											</c:if>
											</c:if>
											</td>
											<td>
											<c:if test="${itemRight.appno !=''&& itemRight.appno !=null}">
											<c:if test="${cnLegalStatusList_right !=''&& cnLegalStatusList_right !=null}">
												<iframe id='irmLagel'
													src='/front/search/table_detailTab6UI?cnDescriptionItem.appno=${itemRight.appno}'
													style='z-index: 0;' frameborder='0' width='100%'
													height='300'></iframe>
											</c:if>
											</c:if>
											</td>
										</tr>
									</table>
								</div>
								<h3 id="hclaim">
									权利要求书
								</h3>
								<div>
									<table cellspacing="1" class="compare">
										<tr>
											<td style="border-right: #6595d6 1px solid;" width="50%">
												 ${clmXmlTxt_left} 
											</td>
											<td>
												 ${clmXmlTxt_right}
											</td>
										</tr>
									</table>
								</div>
								<h3 id="hDes">
									说明书
								</h3>
								<div>
									<table cellspacing="1" class="compare">
										<tr>
											<td style="border-right: #6595d6 1px solid;" width="50%">
												 ${cnDesXmlTxt_left} 
											</td>
											<td>
												 ${cnDesXmlTxt_right}
											</td>
										</tr>
									</table>
								</div>
								
							</div>

						</div>
					</div>
				</div>
			</div>
			
			<%@include file="/WEB-INF/page/front/share/footer.jsp"%>

	</body>
	
</html>