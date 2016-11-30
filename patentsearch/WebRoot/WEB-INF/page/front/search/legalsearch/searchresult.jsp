<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>法律状态检索结果</title>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" /> 
<link href="/js/easyui/themes/icon.css" rel="stylesheet" type="text/css" />
<link href="/js/easyui/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="/css/index.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="/css/law/lawResult.css">
<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
<script src="/js/easyui/jquery.easyui.min.js"></script>
<script src="/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script src="/js/law/legalStatusRecordView.js" charset="utf-8"></script>
<script type="text/javascript">
 //law 行记录视图动态生成
$(function() {
	$('#tt').datagrid({
	    loadMsg:'江苏畅远信息科技有限公司友情提示：系统正在为您努力检索专利法律状态信息，请稍等！',
	     url:'/front/search/legal_getSearchResult',
	     pagination:true ,
		 view : law,
		showHeader : false,
		 pagePosition : top,
		 onLoadSuccess : _loadSuccess,
		queryParams: {
		 shenQingH:'${cnLegalStatus.shenQingH}'
		,legalDate:'${cnLegalStatus.legalDate}'
		,legalStatusInfo:'${cnLegalStatus.legalStatusInfo}'
		,legalCode:'${cnLegalStatus.legalCode}'
		,zhiQuanR:'${cnLegalStatus.zhiQuanR}'
		,zhuanLiMc:'${cnLegalStatus.zhuanLiMc}'
		,dengJiSxr:'${cnLegalStatus.dengJiSxr}'
		,rangYuR:'${cnLegalStatus.rangYuR}'
		,shouRangR:'${cnLegalStatus.shouRangR}'
		,bianGengQ:'${cnLegalStatus.bianGengQ}'
		,bianGengH:'${cnLegalStatus.bianGengH}'
		,bianGengR:'${cnLegalStatus.bianGengR}'
		,heTongBah:'${cnLegalStatus.heTongBah}'
		,jieChuR:'${cnLegalStatus.jieChuR}'
		,chuZhiR:'${cnLegalStatus.chuZhiR}'
		,beiAnRq:'${cnLegalStatus.beiAnRq}'
		,xuKeZl:'${cnLegalStatus.xuKeZl}'
		,zhuQuanLi:'${cnLegalStatus.zhuQuanLi}'
		,ipc:'${cnLegalStatus.ipc}'
		,zhaiYao:'${cnLegalStatus.zhaiYao}'
		,zhuangTai:'${zhuangTai}'
	                }
		 
	});
});
function _loadSuccess(data){
	if(data.total==0)
	  $.messager.show({
		title:'中国法律状态检索结果提示',
		msg:'很抱歉，对于您输入的检索条件，系统为您检索到0条记录！',
		timeout:5000,
		showType:'slide',
		style:{
			right:'',
			top:document.body.scrollTop+document.documentElement.scrollTop,
			bottom:''
		}
	});
}

</script>
</head>
<body>
		<%@include file="/WEB-INF/page/front/share/top.jsp"%>
		<div id="mid" class="base">
			<div class="divContent">
				<h2>中国专利法律状态检索结果</h2>
				<div id="pageContent">
				<table id="tt" style="height: 550px"    >
					<thead>
						<tr>
							<th field="attr1" width="60" sortable="true"></th>
							<th field="attr2" width="60" sortable="true"></th>
							<th field="attr3" width="60" sortable="true"></th>
							<th field="attr4" width="60" sortable="true"></th>
							<th field="attr5" width="60" sortable="true"></th>
							<th field="attr6" width="60" sortable="true"></th>
							<th field="attr7" width="60" sortable="true"></th>
							<th field="attr8" width="60" sortable="true"></th>
							<th field="attr9" width="60" sortable="true"></th>
							<th field="attr10" width="60" sortable="true"></th>
						</tr>
					</thead>
				</table>
				  
			</div>
		</div>
</div>
<%@include file="/WEB-INF/page/front/share/footer.jsp"%>
		 

</body>
</html>