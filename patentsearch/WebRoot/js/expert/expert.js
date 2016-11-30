$(function() {
	var arry = new Array([22]);
	arry[1] = "<p style='float:left;text-align:left'><strong>申请号</strong><br/>申请号检索输入4—12位(不带校验位),早期的8位申请号输入2—8位(不带校验位)。<br/>   申请号为8位的专利，系统会自动转换为12位显示。例如：85107482，显示检索结果198510007482。<br/>    &nbsp;&nbsp;1.已知完整申请号，应键入：200820028064/AN<br/>   &nbsp;&nbsp;2.已知申请号前五位，应键入：20082/AN <br/>    &nbsp;&nbsp;3.已知申请号包含200820028064或200890100326，应键入：200820028064/AN+200890100326/AN</p>";
	arry[2] = "<p style='float:left;text-align:left'><strong>申请日</strong><br/>&nbsp;&nbsp;1.已知完整日期，应键入：19990205/AD <br/>   &nbsp;&nbsp;2.已知月份，应键入：199902/AD<br/>    &nbsp;&nbsp;3.已知年份，应键入：1999/AD<br/>    &nbsp;&nbsp;4.已知时间的连续范围(不大于5年)，应键入：20060202>20090101/AD<br/>   &nbsp;&nbsp;5.已知时间范围包含2008年或2009年，应键入：2008/AD+2009/AD</p>";
	arry[3] = "<p style='float:left;text-align:left'><strong>公开号</strong><br/>公开公告号检索必须输入2—9位。<br/>   1.已知完整公开（公告）号，应键入：101969536/PN<br/>   &nbsp;&nbsp;2.已知公开（公告）号前五位，应键入：10196/PN<br/>    &nbsp;&nbsp;3.已知公开（公告）号包含101969536或202139867U，应键入：101969536/PN+202139867U/PN</p>";
	arry[4] = "<p style='float:left;text-align:left'><strong>公开日</strong><br/>&nbsp;&nbsp;1.已知完整日期，应键入：19990205/PD <br/>   &nbsp;&nbsp;2.已知月份，应键入：199902/PD<br/>    &nbsp;&nbsp;3.已知年份，应键入：1999/PD<br/>    &nbsp;&nbsp;4.已知时间的连续范围(不大于5年)，应键入：20060202>20090101/PD<br/>   &nbsp;&nbsp;5.已知时间范围包含2008年或2009年，应键入：2008/PD+2009/PD</p>";
	arry[5] = "<p style='float:left;text-align:left'><strong>公告号</strong><br/>公开公告号检索必须输入2—9位。<br/>   1.已知完整公开（公告）号，应键入：101969536/GN<br/>   &nbsp;&nbsp;2.已知公开（公告）号前五位，应键入：10196/GN<br/>    &nbsp;&nbsp;3.已知公开（公告）号包含101969536或202139867U，应键入：101969536/GN+202139867U/GN</p>";
	arry[6] = "<p style='float:left;text-align:left'><strong>公告日</strong><br/>&nbsp;&nbsp;1.已知完整日期，应键入：19990205/GD <br/>   &nbsp;&nbsp;2.已知月份，应键入：199902/GD<br/>    &nbsp;&nbsp;3.已知年份，应键入：1999/GD<br/>    &nbsp;&nbsp;4.已知时间的连续范围(不大于5年)，应键入：20060202>20090101/GD<br/>   &nbsp;&nbsp;5.已知时间范围包含2008年或2009年，应键入：2008/GD+2009/GD</p>";
	arry[7] = "<p style='float:left;text-align:left'><strong>分类号</strong><br/>&nbsp;&nbsp;1.已知完整IPC号是A47J 27/66，应键入：A47J02766/IC</font><br/>   &nbsp;&nbsp;2.已知IPC号前五位是A47J 2，应键入：A47J02/IC<br/>    &nbsp;&nbsp;3.已知IPC号包含A47J或A01B，应键入：A47J/IC + A01B/IC</p>";
	arry[8] = "<p style='float:left;text-align:left'><strong>IPC 分类</strong><br/>例:A01B/MC</p>";
	arry[9] = "<p style='float:left;text-align:left'><strong>范畴分类</strong><br/>&nbsp;&nbsp;已知范畴分类号是23F，应键入：23F/CT</p>";
	arry[10] = "<p style='float:left;text-align:left'><strong>优先权号</strong><br/>&nbsp;&nbsp;1.已知完整优先权号，应键入：EP86113792/PR <br /> &nbsp;&nbsp;2.已知优先权号前五位，应键入：EP861/PR <br/>   &nbsp;&nbsp;3.已知优先权号包含EP86113792或EP200800988，应键入：EP86113792/PR + EP200800988/PR </p>";
	arry[11] = "<p style='float:left;text-align:left'><strong>国省代码</strong><br/>&nbsp;&nbsp;已知专利所在国省为中国北京，应键入：11/CO</p>";
	arry[12] = "<p style='float:left;text-align:left'><strong>发明人</strong><br/>&nbsp;&nbsp;1.已知完整发明（申请<br />代理）人姓名，应键入：袁隆平/IN <br/>   &nbsp;&nbsp;2.已知发明（申请<br />代理）人的一半姓名，应键入：袁隆/IN <br/>   &nbsp;&nbsp;3.已知发明（申请<br />代理）人姓名包含袁隆平和王光烈，应键入：袁隆平/IN*王光烈/IN<br/>   &nbsp;&nbsp;4.已知发明（申请<br />代理）人姓名包含袁隆平或王光烈，应键入：袁隆平/IN+王光烈/IN<br/>   &nbsp;&nbsp;5.已知发明（申请<br />代理）人姓名包含袁隆平，不包含王光烈，应键入：袁隆平/IN-王光烈/IN<br/>   6.已知发明（申请<br />代理）人姓名包含袁隆平，不包含王光烈和赵旭日，应键入：袁隆平/IN-（王光烈/IN*赵旭日/IN）</p>";
	arry[13] = "<p style='float:left;text-align:left'><strong>申请人</strong><br/>&nbsp;&nbsp;1.已知完整发明（申请<br />代理）人姓名，应键入：袁隆平/PA <br/>   &nbsp;&nbsp;2.已知发明（申请<br />代理）人的一半姓名，应键入：袁隆/PA <br/>   &nbsp;&nbsp;3.已知发明（申请<br />代理）人姓名包含袁隆平和王光烈，应键入：袁隆平/PA*王光烈/PA<br/>   &nbsp;&nbsp;4.已知发明（申请<br />代理）人姓名包含袁隆平或王光烈，应键入：袁隆平/PA+王光烈/PA<br/>   &nbsp;&nbsp;5.已知发明（申请<br />代理）人姓名包含袁隆平，不包含王光烈，应键入：袁隆平/PA-王光烈/PA<br/>   6.已知发明（申请<br />代理）人姓名包含袁隆平，不包含王光烈和赵旭日，应键入：袁隆平/PA-（王光烈/PA*赵旭日/PA）</p>";
	arry[14] = "<p style='float:left;text-align:left'><strong>关键词</strong><br/>&nbsp;&nbsp;1.已知内容包含计算机，应键入：计算机/TX<br/>   &nbsp;&nbsp;2.已知内容包含计算机和系统，应键入：计算机/TX*系统/TX<br/>   &nbsp;&nbsp;3.已知内容包含计算机或控制板，应键入：计算机/TX+控制板/TX<br/>   &nbsp;&nbsp;4.已知内容包含计算机，不包含键盘，应键入：计算机/TX-键盘/TX<br/>   &nbsp;&nbsp;5.已知内容包含计算机，不包含应用和系统，应键入：计算机/TX-（应用/TX*系统/TX）</p>";
	arry[15] = "<p style='float:left;text-align:left'><strong>发明名称</strong><br/>&nbsp;&nbsp;1.已知内容包含计算机，应键入：计算机/TI<br/>   &nbsp;&nbsp;2.已知内容包含计算机和系统，应键入：计算机/TI*系统/TI<br/>   &nbsp;&nbsp;3.已知内容包含计算机或控制板，应键入：计算机/TI+控制板/TI<br/>   &nbsp;&nbsp;4.已知内容包含计算机，不包含键盘，应键入：计算机/TI-键盘/TI<br/>   &nbsp;&nbsp;5.已知内容包含计算机，不包含应用和系统，应键入：计算机/TI-（应用/TI*系统/TI）</p>";
	arry[16] = "<p style='float:left;text-align:left'><strong>代理机构</strong><br/>&nbsp;&nbsp;已知专利的代理机构代码为11296，应键入：11296/AG</p>";
	arry[17] = "<p style='float:left;text-align:left'><strong>代理人</strong><br/>&nbsp;&nbsp;1.已知完整发明（申请<br />代理）人姓名，应键入：袁隆平/AT <br/>   &nbsp;&nbsp;2.已知发明（申请<br />代理）人的一半姓名，应键入：袁隆/AT <br/>   &nbsp;&nbsp;3.已知发明（申请<br />代理）人姓名包含袁隆平和王光烈，应键入：袁隆平/AT*王光烈/AT<br/>   &nbsp;&nbsp;4.已知发明（申请<br />代理）人姓名包含袁隆平或王光烈，应键入：袁隆平/AT+王光烈/AT<br/>   &nbsp;&nbsp;5.已知发明（申请<br />代理）人姓名包含袁隆平，不包含王光烈，应键入：袁隆平/AT-王光烈/AT<br/>   6.已知发明（申请<br />代理）人姓名包含袁隆平，不包含王光烈和赵旭日，应键入：袁隆平/AT-（王光烈/AT*赵旭日/AT）</p>";
	arry[18] = "<p style='float:left;text-align:left'><strong>申请人地址</strong><br/>&nbsp;&nbsp;1.已知完整申请人地址，应键入：北京市海淀区中关村/DZ | 2.已知申请人的一半地址，应键入：北京市海淀区/DZ | 3.已知申请人地址包含北京市或上海市，应键入：北京市/DZ+上海市/DZ</p>";
	arry[19] = "<p style='float:left;text-align:left'><strong>摘要</strong><br/>&nbsp;&nbsp;1.已知内容包含计算机，应键入：计算机/AB<br/>   &nbsp;&nbsp;2.已知内容包含计算机和系统，应键入：计算机/AB*系统/AB<br/>   &nbsp;&nbsp;3.已知内容包含计算机或控制板，应键入：计算机/AB+控制板/AB<br/>   &nbsp;&nbsp;4.已知内容包含计算机，不包含键盘，应键入：计算机/AB-键盘/AB<br/>   &nbsp;&nbsp;5.已知内容包含计算机，不包含应用和系统，应键入：计算机/AB-（应用/AB*系统/AB）</p>";
	arry[20] = "<p style='float:left;text-align:left'><strong>主权利要求</strong><br/>&nbsp;&nbsp;1.已知内容包含计算机，应键入：计算机/CL<br/>   &nbsp;&nbsp;2.已知内容包含计算机和系统，应键入：计算机/CL*系统/CL<br/>   &nbsp;&nbsp;3.已知内容包含计算机或控制板，应键入：计算机/CL+控制板/CL<br/>   &nbsp;&nbsp;4.已知内容包含计算机，不包含键盘，应键入：计算机/CL-键盘/CL<br/>   &nbsp;&nbsp;5.已知内容包含计算机，不包含应用和系统，应键入：计算机/CL-（应用/CL*系统/CL）</p>";
	arry[21] = "<p style='float:left;text-align:left'><strong>权利要求</strong><br/>&nbsp;&nbsp;1.已知内容包含计算机，应键入：计算机/CS<br/>   &nbsp;&nbsp;2.已知内容包含计算机和系统，应键入：计算机/CS*系统/CS<br/>   &nbsp;&nbsp;3.已知内容包含计算机或控制板，应键入：计算机/CS+控制板/CS<br/>   &nbsp;&nbsp;4.已知内容包含计算机，不包含键盘，应键入：计算机/CS-键盘/CS<br/>   &nbsp;&nbsp;5.已知内容包含计算机，不包含应用和系统，应键入：计算机/CS-（应用/CS*系统/CS）</p>";
	arry[22] = "<p style='float:left;text-align:left'><strong>说明书</strong><br/>&nbsp;&nbsp;1.已知内容包含计算机，应键入：计算机/DS<br/>   &nbsp;&nbsp;2.知内容包含计算机和系统，应键入：计算机/DS*系统/DS<br/>   &nbsp;&nbsp;3.已知内容包含计算机或控制板，应键入：计算机/DS+控制板/DS<br/>   &nbsp;&nbsp;4.已知内容包含计算机，不包含键盘，应键入：计算机/DS-键盘/DS<br/>   &nbsp;&nbsp;5.已知内容包含计算机，不包含应用和系统，应键入：计算机/DS-（应用/DS*系统/DS）</p>";

	arry[23] = "<p style='float:left;text-align:left'><strong>申请号</strong><br/>&nbsp;&nbsp;1.已知完整的申请号，应键入：WO2012US70742/AN<br/>   &nbsp;&nbsp;2.已知申请号的前五位，应键入：WO2012US/AN<br/>   &nbsp;&nbsp;3.已知申请号包含WO2012US70742或WO2012US67473，应键入：WO2012US70742/AN+WO2012US67473/AN<br/>   &nbsp;&nbsp;注：申请号前两位字母为国别代码，申请号检索必须输入国别代码</p>";
	arry[24] = "<p style='float:left;text-align:left'><strong>申请日</strong><br/>&nbsp;&nbsp;1.已知完成日期，应键入：20130520/AD<br/>   &nbsp;&nbsp;2.已知月份，应键入：201305/AD<br/>   &nbsp;&nbsp;3.已知年份，应键入：2013/AD<br/>   &nbsp;&nbsp;4.已知时间连续范围（不大于5年），应键入：20080202>20110101/AD<br/>   &nbsp;&nbsp;5.已知时间范围包含2011年或2012年，应键入：2011/AD+2012/AD<br/>   &nbsp;&nbsp;注：申请日/公开日检索日期格式为YYYY或YYYYMM或YYYYMMDD</p>";
	arry[25] = "<p style='float:left;text-align:left'><strong>公开/公告号</strong><br/>&nbsp;&nbsp;1.已知完整的公开号，应键入：WO2014084868A1/PN<br/>   &nbsp;&nbsp;2.已知公开号前五位，应键入：WO20140/PN<br/>   &nbsp;&nbsp;3.已知公开号包含WO2014084868A1/PN或WO2014085796A2/PN，应键入：WO2014084868A1/PN+WO2014085796A2/PN<br/>   &nbsp;&nbsp;注：公开/公告号前两位字母为国别代码，公开/公告号检索必须输入国别代码</p>";
	arry[26] = "<p style='float:left;text-align:left'><strong>公开/公告日</strong><br/>&nbsp;&nbsp;1.已知完成日期，应键入：20130520/PD<br/>   &nbsp;&nbsp;2.已知月份，应键入：201305/PD<br/>   &nbsp;&nbsp;3.已知年份，应键入：2013/PD<br/>   &nbsp;&nbsp;4.已知时间连续范围（不大于5年），应键入：20080202>20110101/PD<br/>   &nbsp;&nbsp;5.已知时间范围包含2011年或2012年，应键入：2011/AD+2012/PD<br/>   &nbsp;&nbsp;注：申请日/公开日检索日期格式为YYYY或YYYYMM或YYYYMMDD</p>";
	arry[27] = "<p style='float:left;text-align:left'><strong>分类号</strong><br/>&nbsp;&nbsp;1.已知完整的IPC分类号是A47L 3/02,应键入：A47L3/02/IC<br/>   &nbsp;&nbsp;2.已知IPC分类号的前五位是A47L 3，应键入：A47L3/IC<br/>   &nbsp;&nbsp;3.已知IPC分类号包含A47L或B64D，应键入：A47L/IC+B64D/IC</p>";
	arry[28] = "<p style='float:left;text-align:left'><strong>主分类号</strong><br/>&nbsp;&nbsp;1.已知完整的IPC分类号是A47L 3/02,应键入：A47L3/02/MC<br/>   &nbsp;&nbsp;2.已知IPC分类号的前五位是A47L 3，应键入：A47L3/MC<br/>   &nbsp;&nbsp;3.已知IPC分类号包含A47L或B64D，应键入：A47L/MC+B64D/MC</p>";
	arry[29] = "<p style='float:left;text-align:left'><strong>优先权号</strong><br/>&nbsp;&nbsp;1.已知完整优先权号，应键入：US201261730605P/PR<br/>   &nbsp;&nbsp;2.已知优先权号前五位，应键入：US201/PR<br/>   &nbsp;&nbsp;3.已知优先权号包含US201213689194或US201313737740，应键入：US201213689194/PR+US201313737740/PR<br/>   &nbsp;&nbsp;注：优先权号检索必须输入国别代码</p>";
	arry[30] = "<p style='float:left;text-align:left'><strong>发明人</strong><br/>&nbsp;&nbsp;1.已知完整的发明人名称为SERRA MARCO，应键入：SERRA MARCO/IN<br/>   &nbsp;&nbsp;2.已知发明人的一半姓名，应键入：SERRA$/IN<br/>   &nbsp;&nbsp;3.已知发明人的姓名包含SERRA MARCO和 EATON EDWIN A，应键入：SERRA MARCO/IN* EATON EDWIN A/IN<br/>   &nbsp;&nbsp;4.已知发明人的姓名包含SERRA MARCO或 EATON EDWIN A，应键入：SERRA MARCO/IN+ EATON EDWIN A/IN<br/>   &nbsp;&nbsp;5.已知发明人的姓名包含SERRA MARCO，不包含EATON EDWIN A，应键入：SERRA MARCO/IN-EATON EDWIN A/IN<br/>   &nbsp;&nbsp;6.	已知发明人的姓名包含SERRA MARCO，不包含EATON EDWIN A和RODGERS JOHN P，应键入：SERRA MARCO/IN-（EATON EDWIN A/IN*RODGERS JOHN P/IN）</p>";
	arry[31] = "<p style='float:left;text-align:left'><strong>申请人</strong><br/>&nbsp;&nbsp;1.已知完整的申请人名称为SERRA MARCO，应键入：SERRA MARCO/PA<br/>   &nbsp;&nbsp;2.已知申请人的一半姓名，应键入：SERRA$/PA<br/>   &nbsp;&nbsp;3.已知申请人的姓名包含SERRA MARCO和 EATON EDWIN A，应键入：SERRA MARCO/PA* EATON EDWIN A/PA<br/>   &nbsp;&nbsp;4.已知申请人的姓名包含SERRA MARCO或 EATON EDWIN A，应键入：SERRA MARCO/PA+ EATON EDWIN A/PA<br/>   &nbsp;&nbsp;5.已知申请人的姓名包含SERRA MARCO，不包含EATON EDWIN A，应键入：SERRA MARCO/PA-EATON EDWIN A/PA<br/>   &nbsp;&nbsp;6.	已知申请人的姓名包含SERRA MARCO，不包含EATON EDWIN A和RODGERS JOHN P，应键入：SERRA MARCO/PA-（EATON EDWIN A/PA*RODGERS JOHN P/PA）</p>";
	arry[32] = "<p style='float:left;text-align:left'><strong>发明名称</strong><br/>&nbsp;&nbsp;1.已知内容包含smart watch，应键入：smart watch/TI<br/>   &nbsp;&nbsp;2.已知内容包含smart watch和system，应键入：smart watch/TI*system/TI<br/>   &nbsp;&nbsp;3.已知内容包含smart watch或inductor,应键入：smart watch/TI+ inductor/TI<br/>   &nbsp;&nbsp;4.已知内容包含smart watch,不包含inductor,应键入：smart watch/TI-inductor/TI<br/>   &nbsp;&nbsp;5.已知内容包含smart watch，不包含button和inductor，应键入：smart watch/TI-(button/TI*inductor/TI)</p>";
	arry[33] = "<p style='float:left;text-align:left'><strong>摘要</strong><br/>&nbsp;&nbsp;1.已知内容包含smart watch，应键入：smart watch/AB<br/>   &nbsp;&nbsp;2.已知内容包含smart watch和system，应键入：smart watch/AB*system/AB<br/>   &nbsp;&nbsp;3.已知内容包含smart watch或inductor,应键入：smart watch/AB+ inductor/AB<br/>   &nbsp;&nbsp;4.已知内容包含smart watch,不包含inductor,应键入：smart watch/AB-inductor/AB<br/>   &nbsp;&nbsp;5.已知内容包含smart watch，不包含button和inductor，应键入：smart watch/AB-(button/AB*inductor/AB)</p>";
	arry[34] = "<p style='float:left;text-align:left'><strong>引用文献</strong><br/>&nbsp;&nbsp;1.已知完整的文献号，应键入：DE9103095U1/CT<br/>   &nbsp;&nbsp;2.已知文献号前五位，应键入：DE910/CT<br/>   &nbsp;&nbsp;3.已知文献号包含DE9103095U1或JP3310460B2，应键入：DE9103095U1/CT+JP3310460B2/CT</p>";
	arry[35] = "<p style='float:left;text-align:left'><strong>欧洲分类</strong><br/>&nbsp;&nbsp;1.已知完整的ECLA号是A47J 36/02，应键入：A47J36/02/EC<br/>   &nbsp;&nbsp;2.已知ECLA号前五位是B03C 1，应键入：B03C01/EC<br/>   &nbsp;&nbsp;3.已知ECLA号包含B03C或C22B，应键入：B03C/EC+C22B/EC</p>";
	
	// <!-- 必须有title属性，否则提示功能无效 -->
	for (var i = 1; i <= 35; i++) {
		$("#Lab" + i).tooltip({
					track : true,
					content : arry[i],
					
				});
	}
	var symbolarry = new Array([5]);
	arry[1] = "<p style='float:left;text-align:left'><strong>*</strong><br/>应用示例：&lt;<br/>  A*B:同时包含A和B</p>";
	arry[2] = "<p style='float:left;text-align:left'><strong>+</strong><br/>应用示例：&lt;<br/>A+B:包含A或者B</p>";
	arry[3] = "<p style='float:left;text-align:left'><strong>-</strong><br/>应用示例：&lt;<br/>  A-B:包含A且不包含B</p>";
	arry[4] = "<p style='float:left;text-align:left'><strong>(</strong><br/>应用示例：&lt;<br/>  （）:括号内的内容优先计算</p>";
	arry[5] = "<p style='float:left;text-align:left'><strong>)</strong><br/>应用示例：&lt;<br/> （）:括号内的内容优先计算";
	// <!-- 必须有title属性，否则提示功能无效 -->
	for (var i = 1; i <= 5; i++) {
		$("#symbol" + i).tooltip({
					track : true,
					content : symbolarry[i]
				});
	}
	$("#SearchTextBox").tooltip({
		track : true,
		content : '<p style="float:left;text-align:left"><strong>生成算式提示</strong><br/>可以使用@进行专利类型过滤,如:A01/IC+2012/AD @LX=FM,XX</p>'
	});
});

function addSearchEntrance(entrance) {
	var strCommand = entrance.toUpperCase();
	// JoinSQuery("/" + strCommand);
	JoinSQuery(strCommand);
}

function JoinSQuery(entrance) {
	var strCommand = entrance;
	var destinationObj = document.getElementById("SearchTextBox");
	// 如果当前活动控件是文本输入框(id值以"Txt开头")，则在当前控件中插入；否则在检索式输入框插入；
	// var activeObj = cursPos;
	// var activeId = activeObj.id.toLowerCase();
	// if (activeId.indexof("txt") != -1)
	// destinationObj = activeObj;

	destinationObj.focus();
	if (typeof document.selection != "undefined") {
		document.selection.createRange().text = strCommand;
	} else {
		var start = destinationObj.selectionStart;
		var oldValue = destinationObj.value;
		destinationObj.value = oldValue.substring(0, start) + strCommand
				+ oldValue.substring(start, oldValue.length);
		destinationObj.selectionStart = start;
		destinationObj.selectionEnd = start + strCommand.length;
	}
}

function ClearSearch() {
	document.getElementById("SearchTextBox").value = "";
}

function addcommand(obj) {
	var strCommand = obj.name;
	JoinSQuery(strCommand);
}
function ajaxSearch() {
	var strq = $('#SearchTextBox').val();
	// var info = validateQueryStr(strq.trim());
	// var info=validateLogicSearchQuery(strq);
	strQuery = validateLogicSearchQuery(strq);
	console.log(strQuery);
	if (strQuery == "") {
		$.messager.alert("警告", "请输入检索式！");
		return;
	} else if (strQuery.indexOf("Error") != -1) {
		showError(strQuery);
		return;
	}
	$('#SearchTextBox').val(strQuery);
	// alert("正确");
	// $('#SearchTextBox').val('F XX '+strq);

	$.ajax({
				type : 'post',
				url : '',
				data : '',
				error : function() {
				},
				success : function(data) {
				}
			});
}

var m_Entrances = "AN,PN,IC,PR,IN,PA,AG,TI,CL,AD,PD,CT,CO,DZ,TX,AB,DS,CS,GN,GD,AE,IE,PE,TE,MC,AT,ST,EC,"; // 所有入口,以，连接多个
function validateLogicSearchQuery(strSearchQuery) {
	var strQuery = strSearchQuery.Trim().ReduceSpace();
	if (strQuery == "")
		return "";
	var strQueryHead = "";
	var strQueryBody = "";

	// 切割检索式
	var splitReg = /^\s*([J|F|j|f]\s{1,}[XX|xx|Xx|xX]*)(.*)$/;
	if (splitReg.test(strQuery)) // 带检索头
	{
		//F AN 201210065538.1+200820028064.2
		//strQueryHead = RegExp.$1.toUpperCase();
		strQueryHead = RegExp.$1.toUpperCase();
		strQueryBody = RegExp.$2;
		strQueryHead = strQueryHead.Trim();
		strQueryBody = strQueryBody.Trim();
	} else // 不带检索头
	{
		strQueryBody = strQuery;
	}
	//alert("strQueryHead=" + strQueryHead);
	//alert("strQueryBody=" + strQueryBody);
	// 验证检索式
	if (strQueryHead == "J") // 交叉检索，客户端不予验证
	{
		return strQueryHead + " " + strQueryBody;
	} else {
		var simpleSearchReg = /^([A-Za-z]{2})\s+([^\/].*)$/;
		if (simpleSearchReg.test(strQueryBody)) // 简单检索
		{
			strQueryHead = "F";
			var key = RegExp.$1.toUpperCase();
			var value = RegExp.$2;
			var res = getValueAccordingToKey(key, value);
			if (res.substring(0, 5) == "Error") {
				return res;
			} else
				strQueryBody = key + " " + res;
		} 
		else if (strQueryBody.indexOf("/") == -1) // 不是简单检索又不包含符号/，则认为它是交叉检索，比如2+3
	    {
			var regJ = /[\-|+|*]/;
			if (regJ.test(strQueryBody)) // 交叉检索
				return "J " + strQueryBody;
			else
				return "Error:检索式不合法";
		} else // 复杂检索，带/连接符号
		{
			// 从检索式中循环找符合正则式/\/\s*[A-Za-z]{2}/的结构，该结构即为检索入口
			// 向前寻找检索入口对应的内容，遇到以下字符则停止
			// 1 [+|-|*-]; 2 [(|)] 3 字符串头
			// 找到后，消除检索内容之前的空白和括号等，然后进行验证和修改

			strQueryHead = " F XX"
			strQueryBodyTemp = strQueryBody;

			var getValueKeyReg = /\/\s*([A-Za-z]{2})/g;
			var strResult = "";
			var begin = 0;
			var end = 0;

			while (strResult = getValueKeyReg.exec(strQueryBody)) {
				var key = RegExp.$1.toUpperCase();
				var oldKey = RegExp.$1;
				end = strResult.index;
				var value = strQueryBody.substring(begin, end);
				begin = strResult.index + strResult[0].length;

				// 去掉value前后的空格以及+-*(符号
				value = value.TrimLink();

				var res = getValueAccordingToKey(key, value); // 验证并修改检索式
				if (res.substring(0, 5) == "Error") {
					return res;
				} else {

					var zhuanyiValue = value.replace("+", "\\+");
					zhuanyiValue = zhuanyiValue.replace("-", "\\-");
					zhuanyiValue = zhuanyiValue.replace("*", "\\*");
					zhuanyiValue = zhuanyiValue.replace("(", "\\(");
					zhuanyiValue = zhuanyiValue.replace(")", "\\)");
					zhuanyiValue = zhuanyiValue.replace("（", "\\（");
					zhuanyiValue = zhuanyiValue.replace("）", "\\）");
					var strReg = zhuanyiValue + "\\s*\\\/\\s*" + oldKey;
					var reg = new RegExp(strReg, "gi");
					strQueryBodyTemp = strQueryBodyTemp.replace(reg, res + "/"
									+ key);
				}

			}
			strQueryBody = strQueryBodyTemp.Trim();

		}
	}

	return strQueryHead + " " + strQueryBody;

}

// 测试是否为合法的检索入口，测试对应的检索入口内容是否符合规范
// 格式错误，返回"Error:"开头的字符串字符串，如果正确，则返回修改后的strVal,8510 + 8610/AD将变为198510/AD +
// 198610/AD
// value 是可以嵌套括号的，因此验证的时候需要去掉括号和空白
function getValueAccordingToKey(key, value) {
	var entrances = m_Entrances;
	if (entrances.indexOf(key + ",") == -1) {
		return "Error:非法检索入口!";
	}

	var txtId = key;
	var strVal = value.split(/[\-|+|*]/); // 逻辑链接符
	var linkArr = value.match(/[\-|+|*]/g); // 逻辑链接符

	// strRes = "Error:禁止包含非法字符:/,not,and or!";

	for (var i = 0; i < strVal.length; i++) {
		var maxInputLength = 200;
		if (strVal[i].length > maxInputLength) {
			var resTemp = "Error:检索入口字符串长度不能超过";
			resTemp = resTemp + maxInputLength + "个字符！";
			return resTemp;
		}
		var regExp1 = /\s+and\s+/g;
		var regExp2 = /\s+not\s+/g;
		var regExp3 = /\s+or\s+/g;
		if (regExp1.test(strVal[i]) || regExp2.test(strVal[i])
				|| regExp3.test(strVal[i])) {
			return "Error:禁止包含非法字符:not,and or!";
		}
	}

	// strRes = "Error:申请号必须为数字，且长度为4-12位!";
	if (txtId == "AG") {
		for (var i = 0; i < strVal.length; i++) {
			var valueTemp = strVal[i].Trim();
			valueTemp = valueTemp.TrimLink();
			var reg = /^\d{5}$/;
			if (!reg.test(valueTemp)) {
				return "Error:请输入正确的代理机构代码!";
			} else if ("00000" == value) {
				return 'Error:代理机构代码不能为"00000"';
			}
		}
	}
	//
	if (txtId == "AE" || txtId == "IE" || txtId == "PE" || txtId == "TE") {
		for (var i = 0; i < strVal.length; i++) {
			// var valueTemp = strVal[i].Trim();
			// valueTemp = valueTemp.TrimLink();
			// var reg = /^([\u4E00-\u9FA5]|[\uFE30-\uFFA0])*$/gi;
			// if (reg.test(valueTemp)) {
			// return "Error:英文入口中不能包含汉字!";
			// }

		}

	}
	if (txtId == "IN") {
		for (var i = 0; i < strVal.length; i++) {
			 var valueTemp = strVal[i].Trim();
			 valueTemp = valueTemp.TrimLink();
			 var reg = /(\d)/;
			 if (reg.test(valueTemp)) {
			 return "Error:发明人不可包含数字!";
			 }

		}

	}
	if (txtId == "PA") {
		for (var i = 0; i < strVal.length; i++) {
			 var valueTemp = strVal[i].Trim();
			 valueTemp = valueTemp.TrimLink();
			 var reg = /(\d)/;
			 if (reg.test(valueTemp)) {
			 return "Error:申请人不可包含数字!";
			 }

		}

	}
	if (txtId == "AT") {
		for (var i = 0; i < strVal.length; i++) {
			 var valueTemp = strVal[i].Trim();
			 valueTemp = valueTemp.TrimLink();
			 var reg = /(\d)/;
			 if (reg.test(valueTemp)) {
			 return "Error:代理人不可包含数字!";
			 }

		}

	}

	// strRes = "Error:申请号必须为数字，且长度为4-12位!";
	if (txtId == "AN") {
		for (var i = 0; i < strVal.length; i++) {
			var valueTemp = strVal[i].Trim();
			
			if(valueTemp.length>12){
				valueTemp=valueTemp.substring(0,12);
			}
			
			valueTemp = valueTemp.TrimLink();
			var res = getApplyNo(valueTemp);
			if (res.substring(0, 5) == "Error") {
				return res;
			} else {
				value = value.replace(strVal[i].Trim(), res);

			}
		}
	}
   //国省代码必须为两们数字
	if (txtId == "CO") {
        for (var i = 0; i < strVal.length; i++) {
            var valueTemp = strVal[i].Trim();
            valueTemp = valueTemp.TrimLink();

            var reg = /^[0-9a-z]{2}$/gi;
            if (!reg.test(valueTemp)) {
                return "Error:国省代码格式:2位数字";
            }
        }
    }

	// 优先权必须为2位以上字母或数字
	if (txtId == "PR") {
		for (var i = 0; i < strVal.length; i++) {
			var valueTemp = strVal[i].Trim();
			valueTemp = valueTemp.TrimLink();
			var reg = /^([A-Z]{2})(\d{1,})$/;
			if (!reg.test(valueTemp)) {
				return "Error:" + (txtId == "PN" ? "公开" : "公告")
						+ "号必须为2-9位的数字";
			}
			if (valueTemp.length < 2) {
				return "Error:优先权必须为2位及以上字母或数字";
			}

		}
	}

	if (txtId == "AD" || txtId == "PD" || txtId == "GD") {
		for (var i = 0; i < strVal.length; i++) {
			var valueTemp = strVal[i].Trim();
			valueTemp = valueTemp.TrimLink();

			var old = valueTemp;
			var res = isDate(old);

			if (res.substring(0, 5) == "Error") {
				return res;
			} else
				value = value.replace(old, res); // 设置value为修正后的日期值
		}
	}
	// strRes = "Error:公开号必须为2-9位的数字，检索式支持尾部通配符*";
	if (txtId == "PN" || txtId == "GN") {
		var sp=  document.getElementsByName("searchscope")[0].value;
		if("Cn"==sp){
			for (var i = 0; i < strVal.length; i++) {
				var valueTemp = strVal[i].Trim();
				valueTemp = valueTemp.TrimLink();
	
				//var reg = /^(\d{2,8})(\d|[A-Za-z])$/;
				var reg = /^(\d{2,8})([0-9A-Za-z])$/;
				if (!reg.test(valueTemp)) {
					return "Error:" + (txtId == "PN" ? "公开" : "公告")
							+ "号必须为2-9位的数字";
				}
			}
		}
		if("DocDB"==sp){
			for (var i = 0; i < strVal.length; i++) {
				var valueTemp = strVal[i].Trim();
				valueTemp = valueTemp.TrimLink();
	
				var reg = /^[a-zA-Z][a-zA-Z](\d{0,12})(\d|\*)*$/;
				if (!reg.test(valueTemp)) {
					return "Error:必须以两位国别代码开头，紧跟着0-14位字母或者数字!";
				}
			}
		}
	}
	// strRes = "Error:范畴分类格式:2位数字+1位字母";
	if (txtId == "CT") {
		var sp=  document.getElementsByName("searchscope")[0].value;
		if("Cn"==sp){
			for (var i = 0; i < strVal.length; i++) {
				var valueTemp = strVal[i].Trim();
				valueTemp = valueTemp.TrimLink();
	
				var reg = /^(\d{2})[A-Za-z]$/;
				if (!reg.test(valueTemp)) {
					return "Error:范畴分类格式:2位数字+1位字母";
				}
			}
		}
	}
	// strRes = "Error:ipc 输入不合法"
	if (txtId == "IC" || txtId == "MC") {
		for (var i = 0; i < strVal.length; i++) {
			var valueTemp = strVal[i].Trim();
			valueTemp = valueTemp.TrimLink();
			var reg = /[^a-zA-Z0-9\/]/g;
			 var _ipcQuery = valueTemp.replace(reg, '');
			 if (_ipcQuery.length != valueTemp.length)
			 return "Error:"+txtId+"不合法";
			if (valueTemp.length > 12)
				return "Error:IPC分类检索项长度应为3—12位";

			var strValTemp = valueTemp;
			if (strValTemp.indexOf('/') == -1) { // 如果检索项不包含/，则不进一步做验证；
				continue;
			}

			var arr = new Array();
			arr = strValTemp.split(/[\/]/);
			if (arr.length > 2)
				return "Error:IC入口中只能包含一个/";
			var head = arr[0].Trim();
			var end = (arr.length == 2) ? arr[1].Trim() : "";
			// headArr = head.split(/[\s]/);
			// if (headArr.length ==1 && headArr[0].length != 7)
			// return "Error:IC入口/前须为7位IC代码";
			// else if (headArr.length == 2 )
			// {
			// if (headArr[0].length != 4)
			// return "Error:IC /前第一段必须为4位代码";
			// else if (headArr[1].length > 3)
			// return "Error:IC /前第二段必须少于3位代码";
			//                    
			// }
			if (end.length > 4)
				return "Error:IC /后必须少于4位代码";

			// 去掉/，替换头和尾
			// value = value.replace(strValTemp,head+end);

		}
	}
	return value;
}

// 验证和修改申请号
function getApplyNo(apNo) {
	var searchscope=  document.getElementsByName("searchscope")[0].value;
	var _apNo = apNo;
	if("Cn"==searchscope){
		var year1 = apNo.substring(0, 2);
		if (parseInt(year1, 10) > 50) {
			_apNo = "19" + _apNo;
			if (_apNo.length > 5)
				_apNo = _apNo.substring(0, 5) + "00" + _apNo.substring(5);
		}
		var year2 = apNo.substring(0, 1);
		if (year2 == "0") {
			_apNo = "20" + _apNo;
			if (_apNo.length > 5)
				_apNo = _apNo.substring(0, 5) + "00" + _apNo.substring(5);
		}
		var reg = /^\d{4,12}(\.?[\d|X|x])*$/i;
		if (!reg.test(_apNo)) {
			return "Error:申请号输入为4-12位数字(不含校验位)，支持输入校验位[.?或?]!";
		}
	}
	if("DocDB"==searchscope){
		var year1 = apNo.substring(0, 2);
		if (parseInt(year1, 10) > 50) {
			_apNo = "19" + _apNo;
			if (_apNo.length > 5)
				_apNo = _apNo.substring(0, 5) + "00" + _apNo.substring(5);
		}
		var year2 = apNo.substring(0, 1);
		if (year2 == "0") {
			_apNo = "20" + _apNo;
			if (_apNo.length > 5)
				_apNo = _apNo.substring(0, 5) + "00" + _apNo.substring(5);
		}
		var reg = /^[a-zA-Z][a-zA-Z]\d{0,12}(\.?[\d|X|x])*$/i;
		if (!reg.test(_apNo)) {
			return "Error:必须以两位国别代码开头，紧跟着0-14位字母或者数字!";
		}
	}
	return _apNo;
}
// 判断是否为正确的日期格式，如YYYYMMDD或YYYY或YYYYMM或YY或YYMM或YYMMDD
// 例如：19861010或者 201002-20100215 20100506+20100308 20100102>20100402 小日期在前
// 如果错误则返回以"Error"开头的字符串,否则返回修改过后的日期
function isDate(strDate) {
	var arr = new Array();
	arr = strDate.split(">");
	if (arr.length > 2) {
		return "Error:范围检索日期数太多";
	} else if (arr.length == 1) {
		var date = isSingleDate(arr[0]);
		if (date == "false")
			return "Error:日期格式如YYYYMMDD或YYYY或YYYYMM或YY或YYMM或YYMMDD";
		else
			return date;
	} else if (arr.length == 2) {
		var first = isSingleDate(arr[0]);
		var second = isSingleDate(arr[1]);
		if (first == "false" || second == "false") {
			return "Error:范围检索时日期输入格式应为 yyyymmdd>yyyymmdd,在先的日期在前";
		} else {

			if (first.length != 8 || second.length != 8
					|| parseInt(first, 10) > parseInt(second, 10))
				return "Error:范围检索时日期输入格式应为 yyyymmdd>yyyymmdd,在先的日期在前";
			else{
				var year1 = parseInt(first.substring(0,4));
				var year2 = parseInt(second.substring(0,4));
				if((year2-year1)>5){
					return "Error:日期区间不可大于5年";
				}
			}
				return first + ">" + second;
		}
	}

	return strDate;
}

// 判断是否为正确的日期格式，如YYYYMMDD或YYYY或YYYYMM或YY或YYMM或YYMMDD
// 如果以0开头，则补充"20"；如果以
// 返回"false"，"198601"类似的字段
function isSingleDate(strDate) {
	var str = strDate;
	var year1 = str.substring(0, 2);
	if (parseInt(year1, 10) > 50)
		str = "19" + str;
	var year2 = str.substring(0, 1);
	if (year2 == "0")
		str = "20" + str;
	// alert(str);
	var reg = /^(\d{4})(\d{2})?(\d{2})?$/;
	if (reg.test(str) && RegExp.$2 <= 12 && RegExp.$3 <= 31) {
		return str;
	} else {
		return "false";
	}
}