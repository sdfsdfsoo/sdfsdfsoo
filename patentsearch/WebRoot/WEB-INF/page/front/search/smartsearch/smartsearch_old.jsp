<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>一般检索</title>
		<link rel="stylesheet" href="/js/jquery-ui/themes/base/jquery-ui.css" />
		<link href="/css/index.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="/css/search/smartnew.css" />
		<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
		<script src="/js/jquery-ui/ui/jquery-ui.js"></script>
		<script src="/js/StrComm.js"></script>
		<script src="/js/search/simpleSearch.js"></script>
		<script src="/js/search/smartpage.js"></script>
		
		<style>
		.imgGray {
			filter: gray;
			filter: grayscale(1);
			-webkit-filter: grayscale(1);
		}
		</style>

		<script type="text/javascript">
		$(document).ready(
				function() {
				//智能检索输入框提示
				var content='一般检索将检索项分为三类：数字字母混合类、纯数字类、其它类</br>数字字母混合类包括：IPC分类号(IC)、优先权(PR)；</br>'+
				'纯数字类包括：申请号(AN)、申请日(AD)、公告号(GN)、公告日(GD)、公开号(PN)、公开日(PD)；</br>'
				+'其它类包括：发明人(IN)、发明名称(TI)、申请人(PA)、主权利要求(CL)、摘要(AB)；</br>'+
				'<font color=red>注意：一般检索不对国省代码(CO)、申请人地址(DZ)、代理机构(AG)</font>'
				$("#searchContent").tooltip({
			track:true,
			content: content
		});
					var showUC = '0';
					//console.log(showUC);
					if (showUC == 0) {
						//$("span.userCenter").parent().hide();
						$("span.login").parent().show();
						$("span.register").parent().show();
						$("#Smartnavbom ul li").css('margin', '0px 38px');
					} else if (showUC == 1) {
						//$("span.login").parent().hide();
						//$("span.register").parent().hide();
						$("span.userCenter").parent().show();
						$("span.logout").parent().show();
						$("#userCenterLink").show();
						$("#Smartnavbom ul li").css('margin', '0px 20px');
					}

					$('#Smartnavbom a').hover(
							function() {
								var src = $(this).children("img").attr('src');
								//var x = src.substring(0, src.indexOf('-')) + "-2_bj.jpg";
								$(this).children("img").attr(
										'src',
										src.substring(0, src.indexOf('-'))
												+ "-2_bj.jpg");
							},
							function() {
								var src = $(this).children("img").attr('src');
								$(this).children("img").attr(
										'src',
										src.substring(0, src.indexOf('-'))
												+ "-1_bj.jpg");
							});
				});
	 function smartSearch(){ 
				 // 构建检索式与校验检索式
		  simpleSearchNew();
				 // 提交表单
          document.getElementById('searchForm').submit();
    }
    

//专利类型切换
var patentType = "cn";
function switchPatentType() {
    var obj = arguments[0];
    if (patentType == "cn" && obj.id == "btnPatentEn") {
        $("#btnPatentCn").attr("class", "btnPatentCnOff");
        $("#btnPatentEn").attr("class", "btnPatentEn");
        patentType = "en";
        document.getElementsByName("searchscope")[0].value="DocDB";
    } else if (patentType == "en" && obj.id == "btnPatentCn") {
        $("#btnPatentCn").attr("class", "btnPatentCn");
        $("#btnPatentEn").attr("class", "btnPatentEnOff");
        patentType = "cn";
      document.getElementsByName("searchscope")[0].value="Cn";
    }
}
	</script>
	</head>
	<body>
	<%@include file="/WEB-INF/page/front/share/top.jsp"%>
		<div id="divSmarBody">
			
			<div id="divCenter" class="Center">
				<div id="divConntent" class="Conntent">
				 <script type="text/javascript">
        $(document).ready(function () {
            $("#BtnUl li").hover(function () {
                $(this).prevAll("li").children("a").addClass('btnInactive');
                $(this).nextAll("li").children("a").addClass('btnInactive');
                $(this).children("a").addClass('btnActive');
            }, function () {
                $(this).prevAll("li").children("a").removeClass('btnInactive');
                $(this).nextAll("li").children("a").removeClass('btnInactive');
                $(this).children("a").removeClass('btnActive');
            });
            //            $('#btnPatentEn,#btnPatentCn,#btnQuery')
            //                        .wrapInner('<span class="hover"></span>')
            //                        .css('textIndent', '0')
            //        				.each(function () {
            //        				    $('span.hover').css('opacity', 100).hover(function () {
            //        				        //$(this).stop().fadeTo(650, 1);
            //        				    }, function () {
            //        				        // $(this).stop().fadeTo(650, 0);
            //        				    });
            //        				});
        });
    </script>
				
					<div class="SearchLeftPad">
						<div id="BtnUl">
							<ul>
								<li>
									<a id="btnPatentCn" class="btnPatentCn"
										onclick="switchPatentType(this)">中国专利检索 </a>
								</li>
								 <li>
								   <a id="btnPatentEn" class="btnPatentEnOff" onclick="switchPatentType(this)">世界专利检索</a>
                                </li>
								

							</ul>
						</div>
						<form action="/front/search/table_tableSearch" id="searchForm">
							<input name="searchType" value="smart" type="hidden" />
							<input name="searchscope" value="Cn" type="hidden" />
							<input id="strFinalQuery" name="searchFormula" type="hidden" />
							<div id="divText" style="vertical-align: middle;">
								<textarea id="searchContent" name="keyword" cols="20" rows="2"
									class="simpleSearchTxb" title=""></textarea>
								&nbsp;
								<a id="btnQuery_11" href="javascript:;"> <img id="BtnSearch"
										alt="检索" src="/images/search/btnsearch.png"
										style="cursor: hand; margin-top: 10px;" title="命令行检索"
										onclick="smartSearch()" /></a>
							</div>
						</form>
					</div>

				</div>
				<div id="Smartnavbom">
					<ul>
					<!--  
						<li style="margin:0px 25px !important">
							<a href="../My/frmIPCSearch.aspx" target=""> <img
									src="/images/search/big1-1_bj.jpg" /> <br /> IPC分类号查询</a>
						</li>
						-->
						<!-- 
						<li  >
							 <a href="#" target=""> 
							<img src="/images/search/big2-1_bj.jpg"  class="imgGray"/> <br /> 专利分析
							</a>
						</li>
						-->
<!--
						<li  >
							<a href="#" target=""> 
							<img src="/images/search/big3-1_bj.jpg"  class="imgGray"/> <br /> 行业数据库
							</a>
						</li>
						-->
						<li >
							<a href="/front/user/user_profileUI" target=""> 
							<img src="/images/search/big4-1_bj.jpg" /> <br /> 企业个性库
							</a>
						</li>
						<!--
						<li  >
							<a href="#" target=""> 
							<img src="/images/search/big5-1_bj.jpg"  class="imgGray"/> <br /> 在线翻译
							</a>
						</li>
						-->
						<li  >
							<a href="/help/畅远专利检索分析系统操作手册.pdf" target="_blank"> 
							<img src="/images/search/big6-1_bj.jpg" /> <br /> 用户手册
							</a>
						</li>
			</li>
					</ul>
				</div>

				<%@include file="/WEB-INF/page/front/share/footer.jsp"%>
			</div>
		</div>
	</body>
</html>
