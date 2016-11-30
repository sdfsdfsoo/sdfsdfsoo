<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="/css/index.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="/js/jquery-ui/themes/base/jquery-ui.css">
		<link rel="stylesheet" type="text/css"
			href="/js/artdialog/ui-dialog.css">

		<link rel="stylesheet" type="text/css" href="/css/index.css">
		<link rel="stylesheet" type="text/css"
			href="/css/analysis/compare.css">
		<script type="text/javascript" src="/js/artdialog/dialog.js"></script>
		<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
		<script src="/js/jquery-ui/ui/jquery-ui.js"></script>
		<script src="/js/jquery-ui/ui/i18n/jquery.ui.datepicker-zh-CN.js"></script>
		<title>专利对比</title>
		<script type="text/javascript">
		function selectLeftPatent(object){
		   var pubnr_left=object.value;
		   document.getElementById('pubnr_left').value=object.value;
		   var pubnr_right=document.getElementById('pubnr_right').value;
		   var pubnrs=document.getElementById('pubnrs').value;
		   window.location.href = "/front/user/analysis_analysisUI?searchscope=DocDB&appnos="+pubnrs+"&itemLeftEN.pubnr="+pubnr_left+"&itemRightEN.pubnr="+pubnr_right;
		}
		function selectRightPatent(object){
		   var pubnr_left=document.getElementById('pubnr_left').value;
		   var pubnr_right=object.value;
		   document.getElementById('pubnr_right').value=object.value;
		    var pubnrs=document.getElementById('pubnrs').value;
		   window.location.href = "/front/user/analysis_analysisUI?searchscope=DocDB&appnos="+pubnrs+"&itemLeftEN.pubnr="+pubnr_left+"&itemRightEN.pubnr="+pubnr_right;
		}
		</script>
	</head>
	<body>
		<div style="width: 1000px; margin: 0 auto;">
			<%@include file="/WEB-INF/page/front/share/help.jsp"%>
			<%@include file="/WEB-INF/page/front/share/top.jsp"%>
			  <input name="appnos" id="pubnrs" type="hidden" value="${appnos}"/> 
			 
			<div id="mid" class="base">
				<div class="command" style="float: none">
					<div class="commandLeft">
						专利(左边):
                         <input name="itemLeftEN.pubnr" id="pubnr_left" type="hidden" value="${itemLeftEN.pubnr}"/>  
						 <select   onclick="selectLeftPatent(this)" style="width:400px;height:30px">
							<c:forEach items="${resultList}" var="map"
								varStatus="status">
								
								<option value="${map.pubnr }" <c:if test="${map.pubnr==itemLeftEN.pubnr}">selected</c:if>	 >
									${map.title }(${map.pubnr })
								</option>
							</c:forEach>
						</select>
					</div>
					<div class="compareRight">
						专利(右边):
						<input name="itemRightEN.pubnr"  id="pubnr_right" type="hidden" value="${itemRightEN.pubnr}"/>  
						<select   onclick="selectRightPatent(this)" style="width:400px;height:30px">
							<c:forEach items="${resultList}" var="map"
								varStatus="status">
								<option value="${map.pubnr }" <c:if test="${map.pubnr==itemRightEN.pubnr}">selected</c:if>	 >
									${map.title }(${map.pubnr })
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
													target='_blank'>${itemLeftEN.title}</a>
											</th>
											<th colspan="4" style="text-align: center;">
												<a
													href=''
													target='_blank'>${itemRightEN.title}</a>
											</th>
										</tr>
										<tr>
											<th width="10%">
												申请日：
											</th>
											<td width="15%">
												${itemLeftEN.apdText}
											</td>
											<th width="10%">
												申请号：
											</th>
											<td width="15%" style="border-right: #6595d6 1px solid;">
												${itemLeftEN.appno}
											</td>
											<th width="10%">
												申请日：
											</th>
											<td width="15%">
												${itemRightEN.apdText}
											</td>
											<th width="10%">
												申请号：
											</th>
											<td width="15%">
												${itemRightEN.appno}
											</td>
										</tr>
										<tr>
											<th>
												发明人：
											</th>
											<td>
												${itemLeftEN.inventor}
											</td>
											<th>
												申请人：
											</th>
											<td style="border-right: #6595d6 1px solid;">
												${itemLeftEN.appl}
											</td>
											<th>
												发明人：
											</th>
											<td>
												${itemRightEN.inventor}
											</td>
											<th>
												申请人：
											</th>
											<td>
												${itemRightEN.appl}
											</td>
										</tr>
										<tr>
											<th>
												国家/省市：
											</th>
											<td>
												
											</td>
											<th>
												申请人地址：
											</th>
											<td style="border-right: #6595d6 1px solid;">
												
											</td>
											<th>
												国家/省市：
											</th>
											<td>
											
											</td>
											<th>
												申请人地址：
											</th>
											<td>
												
											</td>
										</tr>
										<tr>
											<th>
												公开号：
											</th>
											<td>
												${itemLeftEN.pubnr}
											</td>
											<th>
												公开日：
											</th>
											<td style="border-right: #6595d6 1px solid;">
												${itemLeftEN.pudText}
											</td>
											<th>
												公开号：
											</th>
											<td>
												${itemRightEN.pubnr}
											</td>
											<th>
												公开日：
											</th>
											<td>
												${itemRightEN.pudText}
											</td>
										</tr>
										<tr>
											<th>
												优先权：
											</th>
											<td colspan="3">
                                                  ${itemLeftEN.pris} 
											</td>
											<th>
												优先权：
											</th>
											<td colspan="3">
												 ${itemRightEN.pris}
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
												 ${itemLeftEN.abs} 
											</td>
											<td>
												 ${itemRightEN.abs}
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
												<iframe id='irmLagel'
													src='/front/search/table_detailTab5UI?cnDescriptionItem.appno=${itemLeft.appno}'
													style='z-index: 0;' frameborder='0' width='100%'
													height='300'></iframe>
											</td>
											<td>
												<iframe id='irmLagel'
													src='/front/search/table_detailTab5UI?cnDescriptionItem.appno=${itemRight.appno}'
													style='z-index: 0;' frameborder='0' width='100%'
													height='300'></iframe>
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

		</div>
	</body>
	<script type="text/javascript">
	$("#accordion").accordion( {
		heightStyle : "content",
		activate : function(event, ui) {
			switch ($(ui.newHeader).text().trim()) {
			case '权利要求书':
				GetClaims();
				return;
			case "说明书":
				GetDes();
				break;
			}
		}
	});
</script>
</html>