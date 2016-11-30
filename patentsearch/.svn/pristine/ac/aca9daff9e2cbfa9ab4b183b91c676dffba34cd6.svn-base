<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>专利分析</title>
 <link href="/css/index.css" rel="stylesheet" type="text/css" /> 
<link  href="/css/userCenter/userCenter.css" rel="stylesheet" type="text/css" >
<link href="/js/easyui/themes/icon.css" rel="stylesheet" type="text/css" />
<link href="/js/easyui/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<script src="/js/jquery-1.8.0.min.js" type="text/javascript" ></script>
<script src="/js/easyui/jquery.easyui.min.js"></script>
<script src="/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"> 
		$(function(){
			document.getElementById('_iframe').src= "/front/search/table_analysisTab1?searchFormula=${searchFormula}";
		})
		
   		function changepng_1(){
   			document.getElementById('1001png').src= "/images/menu/1001_h.png";
   		}
   		function changepng_11(){
   			document.getElementById('1001png').src= "/images/menu/1001.png";
   		}
   		function changepng_2(){
   			document.getElementById('1002png').src= "/images/menu/1002_h.png";
   		}
   		function changepng_22(){ 
   			document.getElementById('1002png').src= "/images/menu/1002.png";
   		}
   		function changepng_3(){
   			document.getElementById('1003png').src= "/images/menu/1003_h.png";
   		}
   		function changepng_33(){
   			document.getElementById('1003png').src= "/images/menu/1003.png";
   		}
   		function changepng_4(){
   			document.getElementById('1004png').src= "/images/menu/1004_h.png";
   		}
   		function changepng_44(){
   			document.getElementById('1004png').src= "/images/menu/1004.png";
   		}
   		function changepng_5(){
   			document.getElementById('1005png').src= "/images/menu/1005_h.png";
   		}
   		function changepng_55(){
   			document.getElementById('1005png').src= "/images/menu/1005.png";
   		}
   		function changepng_6(){
   			document.getElementById('3006png').src= "/images/menu/3006_h.png";
   		}
   		function changepng_66(){
   			document.getElementById('3006png').src= "/images/menu/3006.png";
   		}
   		
   		
   		function setTendAnalysis(){  
   			document.getElementById('_iframe').src= "/front/search/table_analysisTab1?searchFormula=${searchFormula}";
   		}
   		function setAreaAnalysis(){  
   			document.getElementById('_iframe').src= "/front/search/table_analysisTab2?searchFormula=${searchFormula}";
   		}
   		function setApplAnalysis(){  
   			document.getElementById('_iframe').src= "/front/search/table_analysisTab3?searchFormula=${searchFormula}";
   		}
   		function setInventorAnalysis(){  
   			document.getElementById('_iframe').src= "/front/search/table_analysisTab4?searchFormula=${searchFormula}";
   		}
   		function setIpcAnalysis(){  
   			document.getElementById('_iframe').src= "/front/search/table_analysisTab5?searchFormula=${searchFormula}";
   		}
   		function setLawAnalysis(){  
   			document.getElementById('_iframe').src= "/front/search/table_analysisTab6?searchFormula=${searchFormula}";
   		}
</script>
</head>
<body>
	<div style="margin: 0 auto;">
		<%@include file="/WEB-INF/page/front/share/help.jsp"%>
	<%@include file="/WEB-INF/page/front/share/top.jsp"%>
<div id="mid" class="base">
	<div id="left" style="width: 225px;">
      	 <div class="easyui-tabs" style="width:225px;height:500px">
	        <div title="中国通用分析" style="padding:10px">
	               <div style="padding:5px;"><span onmouseover="changepng_1();" onmouseout="changepng_11();" onclick="setTendAnalysis();" class="menuTabDetailList" itemno="1001" title="趋势分析"><img id="1001png" src="/images/menu/1001.png" width="180" height="63"></span></div>
	               <div style="padding:5px;"><span onmouseover="changepng_2();" onmouseout="changepng_22();" onclick="setAreaAnalysis();"  class="menuTabDetailList" itemno="1002" title="国省分析"><img id="1002png" src="/images/menu/1002.png" width="180" height="63"></span></div>
	               <div style="padding:5px;"><span onmouseover="changepng_3();" onmouseout="changepng_33();" onclick="setApplAnalysis();" class="menuTabDetailList" itemno="1003" title="申请人分析"><img id="1003png" src="/images/menu/1003.png" width="180" height="63"></span></div>
	              <div style="padding:5px;"><span onmouseover="changepng_4();" onmouseout="changepng_44();"  onclick="setInventorAnalysis();" class="menuTabDetailList" itemno="1004" title="发明人分析"><img id="1004png" src="/images/menu/1004.png" width="180" height="63"></span></div>
	              <div style="padding:5px;"><span onmouseover="changepng_5();" onmouseout="changepng_55();"  onclick="setIpcAnalysis();" class="menuTabDetailList" itemno="1005" title="技术分类分析"><img id="1005png" src="/images/menu/1005.png" width="180" height="63"></span></div>
	              <div style="padding:5px;"><span onmouseover="changepng_6();" onmouseout="changepng_66();"  onclick="setLawAnalysis();" class="menuTabDetailList" itemno="1005" title="法律状态分析"><img id="3006png" src="/images/menu/3006.png" width="180" height="63"></span></div>
	        </div>
	       <!-- <div title="中国创新分析"  style="padding:10px">
	        </div>
	        --> 
    	</div>
  	</div>
    <div id="right" style="width: 768px; min-height: 535px;" >  <!-- display: none -->
     <iframe id="_iframe"  width="750px" height="520px" frameborder="0" scrolling="yes">
     
     </iframe>
		
		
		</div>	
		
</div>    
		<%@include file="/WEB-INF/page/front/share/footer.jsp"%>

	</div>
</body>
</html>