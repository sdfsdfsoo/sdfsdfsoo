
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>全文PDF</title>
		<script type="text/javascript" src="/js/detail/tab2.js"></script>
		<script type="text/javascript" src="/js/detail/pdfobject.js"></script>
		<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
		<script src="/js/jquery-ui/ui/jquery-ui.js"></script>
		<link href="/css/index.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="/js/jquery-ui/themes/base/jquery-ui.css" />
		<link href="/css/index.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<div style="width: 100%;">
			<div style="width: 100%;">
            <iframe  id="irmPdf" frameborder="1" width="100%" src="${pdfURL}" onload="this.height=(document.documentElement.scrollHeight-100);" height="600"></iframe>
			</div>
		</div>
	</body>
</html>