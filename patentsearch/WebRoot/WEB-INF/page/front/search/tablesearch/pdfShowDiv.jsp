<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp"%>

<c:if test="${type != '实用新型'}">
	<c:if test="${url0!=null}">
	<a href="${url0}" target=_blank  >
	<img src="/images/result/smallshoucang_bj.png" alt="">调取专利申请PDF文件</a>&nbsp;&nbsp;<br><br>
	</c:if>
	
	
	
	<c:if test="${url1!=null}">
	<a href="${url1}" target=_blank  >
	<img src="/images/result/smallshoucang_bj.png" alt="">调取专利授权PDF文件</a>&nbsp;&nbsp;
	</c:if>
</c:if>

<c:if test="${type == '实用新型'}">
	<c:if test="${url0!=null}">
	<a href="${url0}" target=_blank  >
	<img src="/images/result/smallshoucang_bj.png" alt="">调取专利授权PDF文件</a>&nbsp;&nbsp;
	</c:if>
</c:if>

						