<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp"%>
<html>

	<head>
		<link rel="stylesheet" href="/css/vip.css" type="text/css">
		<script type="text/javascript">
function enterSubmit(keyCode) {
		if (keyCode == 13)
		if(${urladdress == null}){
		
		}else{
		window.location.href='${urladdress}'
		}
			 

	}
	
</script>
	</head>
	<body onkeydown="enterSubmit(event.keyCode)">
  
		<div align="center">
			<table>
				<tr>
					<td height="40"></td>
				</tr>
				<tr>
					<td align="center">
						<table width="305" border="0" cellpadding="0" cellspacing="0"
							align="center">
							<tr>
								<td height="212" align="center" valign="middle"
									bgcolor="#95CBFD">
									<table width="295" border="0" cellpadding="0" bgcolor="#FFFFFF">
										<tr>
											<td width="288" align="center" bgcolor="#C2E1FE">
												<table width="100" border="0" cellpadding="0"
													cellspacing="0">
													<tr>
														<td>
															&nbsp;
														</td>
													</tr>
												</table>
												<table width="273" border="0" cellpadding="0"
													cellspacing="10" bgcolor="#FFFFFF">
													<tr>
														<td width="253" height="60" align="center" valign="bottom"
															class="font12">
															<p>
																<c:out value="${message}" escapeXml="false" />
															</p>
														</td>
													</tr>
													<tr>
														<td height="80" align="center" valign="middle">
															<font size="2"><span class="content"> <input
																		type="button" name="sure" value="确 定"
																		onclick="javascript:<c:if test="${urladdress == null}">window.close();</c:if><c:if test="${urladdress != null}">window.location.href='${urladdress}'</c:if>">
															</span>
															</font>
														</td>
													</tr>
												</table>
												<table width="100" border="0" cellpadding="0"
													cellspacing="0">
													<tr>
														<td>
															&nbsp;
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="40">
						<br>
						<br>
						<br>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
