<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>分类检索</title>
<!-- <link rel="stylesheet" type="text/css" href="/css/userCenter/userCenter.css"> -->
<link href="/js/easyui/themes/icon.css" rel="stylesheet" type="text/css" />
<link href="/js/easyui/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="/css/index.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
<script src="/js/easyui/jquery.easyui.min.js"></script>
<script src="/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script src="/js/list/jquery.textSearch-1.0.js"></script>
<script src="/js/myValidate.js"></script>
<style type="text/css">
	.left_content a{
font: 12px/30px "宋体";
color: #0c5f76;
text-decoration: underline;
font-weight: bold;
	}
</style>

<style>
		.imgGray {
			filter: gray;
			filter: grayscale(1);
			-webkit-filter: grayscale(1);
		}
		</style>
</head>
<body>
<%@include file="/WEB-INF/page/front/share/top.jsp"%>
		<div id="mid" class="base">
			<div id="left" >
				<div class="left_ti">
					<span>分类导航</span>
				</div>
				<div class="left_content">
					<div id="ipctypelist" class="easyui-accordion" data-options="fit:true" style="width: 220px;">
                    <div id="IPC" title="IPC分类" style="padding: 10px; overflow: hidden;">
                        <ul id="listIPC">
                            <li><a href="javascript:void(0);" onclick="showipc('A')" target="rightFrame" title="人类生活必需">A&nbsp;人类生活必需</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('B')" target="rightFrame" title="作业；运输">B&nbsp;作业；运输</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('C')" target="rightFrame" title="化学；冶金">C&nbsp;化学；冶金</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('D')" target="rightFrame" title="纺织；造纸">D&nbsp;纺织；造纸</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('E')" target="rightFrame" title="固定建筑物">E&nbsp;固定建筑物</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('F')" target="rightFrame" title="机械工程；照明；加热；武器；爆破">F&nbsp;机械工程；照明；加热；武器；爆破</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('G')" target="rightFrame" title="物理">G&nbsp;物理</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('H')" target="rightFrame" title="电学">H&nbsp;电学</a></li>
                        </ul>
                    </div>
                    <div id="ADM" title="洛迦诺分类" style="padding: 10px; min-height: 250px;">
                        <ul id="listADM" style="display: block;">
                            <li><a href="javascript:void(0);" onclick="showipc('01')" target="rightFrame" title="食品">01&nbsp;食品</a></li><li>
                                <a href="javascript:void(0);" onclick="showipc('02')" target="rightFrame" title="服装和服饰用品">02&nbsp;服装和服饰用品</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('03')" target="rightFrame" title="其它类未列入的旅行用品，箱子，阳伞和个人用品">
                                03&nbsp;其它类未列入的旅行用品，箱子，阳伞和个人用品</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('04')" target="rightFrame" title="刷子类">04&nbsp;刷子类</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('05')" target="rightFrame" title="纺织品，人造或天然材料片材类">05&nbsp;纺织品，人造或天然材料片材类</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('06')" target="rightFrame" title="家具">06&nbsp;家具</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('07')" target="rightFrame" title="其他类未列入的家用物品">07&nbsp;其他类未列入的家用物品</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('08')" target="rightFrame" title="工具和金属器具">08&nbsp;工具和金属器具</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('09')" target="rightFrame" title="用于商品运输或装卸的包装和容器">09&nbsp;用于商品运输或装卸的包装和容器</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('10')" target="rightFrame" title="钟，表和其它计量仪器，检查和信号仪器">
                                10&nbsp;钟，表和其它计量仪器，检查和信号仪器</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('11')" target="rightFrame" title="装饰品">11&nbsp;装饰品</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('12')" target="rightFrame" title="运输或提升工具">12&nbsp;运输或提升工具</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('13')" target="rightFrame" title="发电，配电和输电的设备">13&nbsp;发电，配电和输电的设备</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('14')" target="rightFrame" title="录音，通讯或信息再现设备">14&nbsp;录音，通讯或信息再现设备</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('15')" target="rightFrame" title="其它类未列入的机械">15&nbsp;其它类未列入的机械</a></li><li>
                                <a href="javascript:void(0);" onclick="showipc('16')" target="rightFrame" title="照相，电影摄影和光学仪器">16&nbsp;照相，电影摄影和光学仪器</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('17')" target="rightFrame" title="乐器">17&nbsp;乐器</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('18')" target="rightFrame" title="印刷和办公机械">18&nbsp;印刷和办公机械</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('19')" target="rightFrame" title="文具用品，办公设备，艺术家用品及教学材料">
                                19&nbsp;文具用品，办公设备，艺术家用品及教学材料</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('20')" target="rightFrame" title="销售和广告设备，标志">20&nbsp;销售和广告设备，标志</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('21')" target="rightFrame" title="游戏，玩具，帐篷和体育用品">21&nbsp;游戏，玩具，帐篷和体育用品</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('22')" target="rightFrame" title="武器，烟火，涉猎用品，捕杀有害动物的器具">
                                22&nbsp;武器，烟火，涉猎用品，捕杀有害动物的器具</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('23')" target="rightFrame" title="液体分配设备，卫生，供暖，通风和空调设备，固体燃料">
                                23&nbsp;液体分配设备，卫生，供暖，通风和空调设备，固体燃料</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('24')" target="rightFrame" title="医疗和实验室设备">24&nbsp;医疗和实验室设备</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('25')" target="rightFrame" title="建筑构件和施工元件">25&nbsp;建筑构件和施工元件</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('26')" target="rightFrame" title="照明设备">26&nbsp;照明设备</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('27')" target="rightFrame" title="烟草和吸烟用具">27&nbsp;烟草和吸烟用具</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('28')" target="rightFrame" title="药品，化妆品，梳妆用品和器具">28&nbsp;药品，化妆品，梳妆用品和器具</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('29')" target="rightFrame" title="防火灾，防事故救援装置和设备">29&nbsp;防火灾，防事故救援装置和设备</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('30')" target="rightFrame" title="动物的管理与驯养设备">30&nbsp;动物的管理与驯养设备</a></li><li>
                                <a href="javascript:void(0);" onclick="showipc('31')" target="rightFrame" title="其他类未列入的食品或饮料制作机械和设备">31&nbsp;其他类未列入的食品或饮料制作机械和设备</a></li>
                            <li><a href="javascript:void(0);" onclick="showipc('99')" target="rightFrame"  title="其它杂项">99&nbsp;其它杂项</a></li>
                        </ul>
                    </div>
                </div>
				</div>
       		</div>
       		<div id="right" style="height: 540px;width:770px">
                <div id="right_top" class="right_top">
            <input type="radio" id="rdipc" name="stype" value="ipc" checked="checked"><label for="rdipc">按分类号查找</label>
            <input type="radio" id="rdkey" name="stype" value="key" ><label for="rdkey">按关键字查找</label>
            <input type="text" id="IPCInput" onkeydown="ipckeydown()" name="IPCInput" style="width: 300px; height: 22px;">
            <span class="button" onclick="ipcSearch()">查询</span>
        </div>
       			<iframe id="rightFrame" name="rightFrame" src="/front/user/ipc_classNavList" style="border:0px;scrolling:no;width:100%;height:500px"></iframe>
       		</div>
		</div>
		<%@include file="/WEB-INF/page/front/share/footer.jsp"%>
<script type="text/javascript">
function getType(){
	var acc = $('#ipctypelist').accordion('getSelected');
	//console.log(acc);
    return acc.attr('id');
}
function showipc(strclass) {
    var type = getType();
    var f = document.getElementById("rightFrame"); 
    //console.log(f);
    f.src='/front/user/ipc_classNavList?type='+type+'&nodeStr='+strclass;
} 
function ipcSearch(){
    var searchClass=$('input:radio:checked').val();
    var type=getType();
    var v=$("#IPCInput").val();
   // console.log(type);     //IE不兼容
   // console.log(v);
    if(v=="") $.messager.alert("警告","请输入检索内容！");
    else{
        var f = document.getElementById("rightFrame");
        f.src='/front/user/ipc_classNavSearch?type='+type+'&value='+v+'&searchClass='+searchClass;    
    }
}
 
</script>
</body>
</html>