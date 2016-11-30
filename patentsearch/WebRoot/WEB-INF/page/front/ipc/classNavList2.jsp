<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>分类导航检索结果列表</title>
		<link rel="stylesheet" type="text/css" href="/css/index.css">
		<link href="/js/easyui/themes/icon.css" rel="stylesheet"
			type="text/css" />
		<link href="/js/easyui/themes/default/easyui.css" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
		<script src="/js/easyui/jquery.easyui.min.js"></script>
		<script src="/js/easyui/locale/easyui-lang-zh_CN.js"></script>

	</head>
	<body>
		<div id="rights" class="sright">
			<div id="ipc_right_content" style="">
				<c:if test="${ !empty objects}">
				<dl class="ipc_title">
						<dt class="sp_title">
							分类
						</dt>
						<dd class="sp_desc">
							类描述
						</dd>
					</dl>
					<ul id="ipc_result">

						<c:forEach items="${ objects}" var="o" varStatus="status">
							<c:if test="${o.hasChild }">
								<li
									class='<c:if test="${o.menuFlag }">ipc_m</c:if><c:if test="${!o.menuFlag }">ipc_c</c:if>'>
									<div class="ipc_class">
										<a href="javascript:void(0);"
											onclick="showipc('${o.classType }')" title="">${o.classType}</a>
									</div>
									<div class="ipc_desc">
										<a href="javascript:void(0);"
											onclick="showipc('${o.classType }')" title="">${o.des}</a>
									</div>
									<c:if test="${!status.first }">
										<div class="ipc_sbtn">
											<span class="lansebutton"> <c:if test="${o.type==0 }">
													<a target="_blank" href="/front/search/table_tableSearch?searchType=table&searchFormula=F IC ${o.classType}">中国专利</a>
												</c:if> <c:if test="${o.type==1 }">
													<a target="_blank" href="/front/search/table_tableSearch?searchType=table&searchFormula=F IC ${o.classTypePara}">中国专利</a>
												</c:if> </span>
												<c:if test="${o.menuFlag }">
															<span class="lansebutton"> <c:if test="${o.type==0 }">
															<a target="_blank" href="/front/search/table_tableSearch?searchType=table&searchscope=DocDB&searchFormula=F MC ${o.classType}">世界专利</a>
														</c:if> <c:if test="${o.type==1 }">
															<a target="_blank" href="/front/search/table_tableSearch?searchType=table&searchscope=DocDB&searchFormula=F MC ${o.classTypePara}">世界专利</a>
														</c:if> </span>
														</c:if> 
														
										</div>
									</c:if>
									<div style="clear: both"></div>
								</li>
							</c:if>
							<c:if test="${!o.hasChild }">
								<li
									class='<c:if test="${o.menuFlag }">ipc_m</c:if><c:if test="${!o.menuFlag }">ipc_c</c:if>'>
									<div class="ipc_class">
										${o.classType}
									</div>
									<div class="ipc_desc">
										${o.des}
									</div>
									<div class="ipc_sbtn">
										<span class="lansebutton"> <c:if test="${o.type==0 }">
												<a target="_blank"
													href="/front/search/table_tableSearch?searchType=table&searchFormula=F IC ${o.classType}">中国专利</a>
											</c:if> <c:if test="${o.type==1 }">
												<a target="_blank"
													href="/front/search/table_tableSearch?searchType=table&searchFormula=F IC ${o.classTypePara}">中国专利</a>
											</c:if> </span>
											<c:if test="${o.menuFlag }">
												<span class="lansebutton"> <c:if test="${o.type==0 }">
													<a target="_blank"
														href="/front/search/table_tableSearch?searchType=table&searchscope=DocDB&searchFormula=F IC ${o.classType}">世界专利</a>
												</c:if> <c:if test="${o.type==1 }">
													<a target="_blank"
														href="/front/search/table_tableSearch?searchType=table&searchscope=DocDB&searchFormula=F IC ${o.classTypePara}">世界专利</a>
												</c:if> </span>
											</c:if>	
									</div>
									<div style="clear: both"></div>
								</li>
							</c:if>
						</c:forEach>
					</ul>
				</c:if>
			</div>
		</div>
		<script type="text/javascript">
	function getType() {
		var acc = $('#ipctypelist .accordion-header-selected ~ div',
				parent.document).attr("id");
		//var acc = window.parent.document.getElementById("ipctypelist");
		console.log($(acc));
		//console.log(acc);
		return acc;
	}
	function showipc(strclass) {
		var type = getType();
		window.location.href = '/front/user/ipc_classNavList?type=' + type
				+ '&nodeStr=' + strclass;
	}
	function search(key, value) {

	}
</script>
	</body>
</html>