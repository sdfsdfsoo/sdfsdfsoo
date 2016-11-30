$(function() {

	var arry = new Array([35]);
	arry[1] = "<strong>发明名称</strong><br/>    &nbsp;&nbsp;1.如已知发明名称包含智能手表，应输入：智能手表<br/>   &nbsp;&nbsp;2.如已知发明名称包含智能手表和系统，应输入：智能手表*系统 <br/>   &nbsp;&nbsp;3.如已知发明名称包含智能手表或感应器，应输入：智能手表+感应器<br/>   &nbsp;&nbsp;4.如已知发明名称包含智能手表不包括按键，应输入：智能手表-按键<br/>   &nbsp;&nbsp;5.如已知发明名称包含智能手表不包括按键和感应器，应输入：智能手表-（按键*感应器）";
	arry[2] = "<strong>摘要</strong><br/>     &nbsp;&nbsp;1.如已知摘要包含智能手表，应输入：智能手表<br/>   &nbsp;&nbsp;2.如已知摘要包含智能手表和系统，应输入：智能手表*系统<br/>   &nbsp;&nbsp;3.如已知摘要包含智能手表或感应器，应输入：智能手表+感应器<br/>   &nbsp;&nbsp;4.如已知摘要包含智能手表不包括按键，应输入：智能手表-按键<br/>   &nbsp;&nbsp;5.如已知摘要包含智能手表不包括按键和感应器，应输入：智能手表-（按键*感应器）";
	arry[3] = "<strong>主权利要求</strong><br/>     &nbsp;&nbsp;1.如已知主权利要求包含智能手表，应输入：智能手表<br/>   &nbsp;&nbsp;2.如已知主权利要求包含智能手表和系统，应输入：智能手表*系统<br/>   &nbsp;&nbsp;3.如已知主权利要求包含智能手表或感应器，应输入：智能手表+感应器<br/>   &nbsp;&nbsp;4.如已知主权利要求包含智能手表不包括按键，应输入：智能手表-按键<br/>   &nbsp;&nbsp;5.如已知主权利要求包含智能手表不包括按键和感应器，应输入：智能手表-（按键*感应器）";
	arry[4] = "<strong>关键词</strong><br/>     &nbsp;&nbsp;1.已知关键词包含计算机，应键入：计算机<br/>   &nbsp;&nbsp;2.已知关键词包含计算机和系统，应键入：计算机*系统<br/>   &nbsp;&nbsp;3.已知关键词包含计算机或控制板，应键入：计算机+控制板<br/>   &nbsp;&nbsp;4.已知关键词包含计算机，不包含键盘，应键入：计算机-键盘<br/>   &nbsp;&nbsp;5.已知关键词包含计算机，不包含应用和系统，应键入：计算机-（应用*系统）";
	arry[5] = "<strong>申请人</strong><br/>     &nbsp;&nbsp;1.如已知完整名称为江苏恒顺醋业股份有限公司，应输入：江苏恒顺醋业股份有限公司<br/>   &nbsp;&nbsp;2.如已知申请人部分名称包含江苏恒顺，应输入：江苏恒顺<br/>   &nbsp;&nbsp;3.如已知申请人名称包含江苏恒顺醋业股份有限公司和江苏大学，应输入：江苏恒顺醋业股份有限公司*江苏大学<br/>   &nbsp;&nbsp;4.如已知申请人名称包含江苏恒顺醋业股份有限公司或江苏大学，应输入：江苏恒顺醋业股份有限公司+江苏大学<br/>   &nbsp;&nbsp;5.如已知申请人名称包含江苏恒顺醋业股份有限公司，不包含江苏大学，应输入：江苏恒顺醋业股份有限公司-江苏大学<br/> 6.如已知申请人名称包含江苏恒顺醋业股份有限公司，不包含江苏大学和江苏科技大学，应输入：江苏恒顺醋业股份有限公司-（江苏大学*江苏科技大学）";
	arry[6] = "<strong>分类号</strong><br/>     &nbsp;&nbsp;1.如已知完整IPC号是H03D  11/ 00，应输入：H03D01100</font><br/>   &nbsp;&nbsp;2.如已知部分IPC号是H03D  11，应输入：H03D01100<br/>    &nbsp;&nbsp;3.如已知IPC号包含H03D 或G06J，应输入：H03D +G06J";
	arry[7] = "<strong>申请号</strong></br>&nbsp;&nbsp;&nbsp;&nbsp;早期的8位申请号输入2-8位（不含校验位），如输入8位的申请号，系统会自动转为12位显示；如：87101256，显示检索结果198710001256。现在的申请号输入4-12位（不含校验位）或者带校验位 如：200710054835.6。<br/>    &nbsp;&nbsp;1.如已知完整申请号，应输入：200710054835<br/>   &nbsp;&nbsp;2.如已知申请号的前5位，应输入：20071<br/>    &nbsp;&nbsp;3.如已知申请号包含200710054835或198710001256，应输入：200710054835+198710001256<br/>&nbsp;&nbsp;<br/> ";
	arry[8] = "<strong>申请日</strong><br/>     &nbsp;&nbsp;1.如已知完整申请日期，应输入：20120316 <br/>   &nbsp;&nbsp;2.如已知申请年月份，应输入：201203<br/>    &nbsp;&nbsp;3.如已知申请年份，应输入：2012<br/>    &nbsp;&nbsp;4.如已知时间申请时间连续范围（不大于5年），应输入：20050202＞20080808<br/>   &nbsp;&nbsp;5.如已知时间范围包含2005年或2008年，应输入：2005+2008";
	arry[9] = "<strong>公开号</strong></br>公开号检索必须输入2-9位数字。<br/> 1.如已知完整公开号，应输入：102188802<br/>   &nbsp;&nbsp;2.如已知部分公开号，应输入：10218<br/>    &nbsp;&nbsp;3.如已知公开号包含102188802或202139867，应输入102188802+202139867";
	arry[10] = "<strong>公开日</strong><br/>     &nbsp;&nbsp;1.如已知完整公开日期，应输入：20120316 <br/>   &nbsp;&nbsp;2.如已知公开年月份，应输入：201203<br/>    &nbsp;&nbsp;3.如已知公开年份，应输入：2012<br/>    &nbsp;&nbsp;4.如已知公开时间的连续范围（不大于5年），应输入：20050202＞20080808<br/>   &nbsp;&nbsp;5.如已知公开时间范围包含2005年或2008年，应输入：2005+2008";
	arry[11] = " <strong>公告号</strong></br>公告号检索必须输入2-9位数字。<br/>   1.如已知完整公告号，应输入：102188802<br/>   &nbsp;&nbsp;2.如已知部分公告号，应输入：10218<br/>    &nbsp;&nbsp;3.如已知公告号包含102188802或202139867，应输入102188802+202139867";
	arry[12] = "<strong>公告日</strong><br/>     &nbsp;&nbsp;1.如已知完整公告日期，应输入：20120316 <br/>   &nbsp;&nbsp;2.如已知公告年月份，应输入：201203 <br/>    &nbsp;&nbsp;3.如已知公告年份，应输入：2012 <br/>    &nbsp;&nbsp;4.如已知公告时间的连续范围（不大于5年），应输入：20050202＞20080808<br/>   &nbsp;&nbsp;5.如已知公告时间范围包含2005年或2008年，应输入：2005+2008";
	arry[13] = "<strong>优先权号</strong><br/>     &nbsp;&nbsp;1.如已知完整优先权号，应输入：EP861135325 <br/>&nbsp;&nbsp;2.如已知部分优先权号，应输入：US29 <br/>   &nbsp;&nbsp;3.如已知优先权号包含EP861135325或EP86113792，应输入：EP861135325+EP86113792";
	arry[14] = "<strong>发明人</strong><br/>     &nbsp;&nbsp;1.如已知完整名称为乔布斯 ，应输入：乔布斯<br/> 2.如已知发明人部分名称，应输入：乔布 斯<br/> 3.如已知发明人名称包含乔布斯和马化腾，应输入：乔布斯*马化腾 <br/>  4.如已知发明人名称包含乔布斯或马化腾，应输入：乔布斯+马化腾| 5.如已知发明人名称包含乔布斯不包含马化腾，应输入：乔布斯-马化腾 <br/>  6.如已知发明人名称包含乔布斯，不包含马化腾和马云，应输入：乔布斯-（马化腾*马云）";
	arry[15] = "<strong>范畴分类</strong><br/>     &nbsp;&nbsp;已知范畴分类号是23F，应键入：23F";
	arry[16] = "<strong>申请人地址</strong><br/>     &nbsp;&nbsp;1.如已知完整申请人地址，应输入：江苏省镇江市京口区中山路 <br/>  2.如已知申请人部分地址，应输入：江苏省镇江市京口区 <br/> 3.如已知申请人地址包含江苏省或上海市，应输入：江苏省+上海市";
	arry[17] = "<strong>国省代码</strong><br/>     &nbsp;&nbsp;已知专利所在国省为中国北京，可通过输入北京或者bj来查询国省代码然后单击查询出来的结果或者直接键入11";
	arry[18] = "<strong>代理机构</strong><br/>     &nbsp;&nbsp;如已知专利的代理机构为南京苏高专利商标事务所（普通合伙），应输入：南京苏高，系统自动转换为对应的代理机构代码32204";
	arry[19] = "<strong>主分类号</strong><br/>     &nbsp;&nbsp; 1.如已知完整IPC号是H03D  11/ 00，应输入：H03D01100</font><br/>   &nbsp;&nbsp;2.如已知部分IPC号是H03D  11，应输入：H03D011<br/>    &nbsp;&nbsp;3.如已知IPC号包含H03D 或G06J，应输入：H03D +G06J";
	arry[20] = "<strong>代理人</strong><br/>     &nbsp;&nbsp;1.如已知完整名称为张三四 ，应输入：张三四 <br/> 2.如已知代理人部分名称，应输入：张三 <br/> 3.如已知代理人名称包含张三四和赵五六，应输入：张三四*赵五六 <br/> 4.如已知代理人名称包含张三四或赵五六，应输入：张三四+赵五六 <br/>  5.如已知代理人名称包含张三四不包含赵五六，应输入：张三四-赵五六 <br/>  6.如已知代理人名称包含张三四，不包含赵五六和马七八，应输入：张三四-（赵五六*马七八）";
	arry[21] = "<strong>权利要求</strong><br/>    &nbsp;&nbsp;1.已知权利要求包含计算机，应键入：计算机<br/>   &nbsp;&nbsp;2.已知权利要求包含计算机和系统，应键入：计算机*系统 <br/>   &nbsp;&nbsp;3.已知权利要求包含计算机或控制板，应键入：计算机+控制板<br/>   &nbsp;&nbsp;4.已知权利要求包含计算机，不包含键盘，应键入：计算机-键盘<br/>   &nbsp;&nbsp;5.已知权利要求包含计算机，不包含应用和系统，应键入：计算机-（应用*系统）";
	arry[22] = "<strong>说明书</strong><br/>    &nbsp;&nbsp;1.已知说明书包含计算机，应键入：计算机<br/>   &nbsp;&nbsp;2.已知说明书包含计算机和系统，应键入：计算机*系统 <br/>   &nbsp;&nbsp;3.已知说明书包含计算机或控制板，应键入：计算机+控制板<br/>   &nbsp;&nbsp;4.已知说明书包含计算机，不包含键盘，应键入：计算机-键盘<br/>   &nbsp;&nbsp;5.已知说明书包含计算机，不包含应用和系统，应键入：计算机-（应用*系统）";
	arry[23] = "<strong>发明名称</strong><br/>    &nbsp;&nbsp;1.已知内容包含smart watch，应键入：smart watch<br/>   &nbsp;&nbsp;2.已知内容包含smart watch和system，应键入：smart watch*system <br/>   &nbsp;&nbsp;3.已知内容包含smart watch或inductor,应键入：smart watch+ inductor<br/>   &nbsp;&nbsp;4.已知内容包含smart watch,不包含inductor,应键入：smart watch-inductor<br/>   &nbsp;&nbsp;5.已知内容包含smart watch，不包含button和inductor，应键入：smart watch-(button*inductor)";
	arry[24] = "<strong>摘要</strong><br/>    &nbsp;&nbsp;1.已知内容包含smart watch，应键入：smart watch<br/>   &nbsp;&nbsp;2.已知内容包含smart watch和system，应键入：smart watch*system <br/>   &nbsp;&nbsp;3.已知内容包含smart watch或inductor,应键入：smart watch+ inductor<br/>   &nbsp;&nbsp;4.已知内容包含smart watch,不包含inductor,应键入：smart watch-inductor<br/>   &nbsp;&nbsp;5.已知内容包含smart watch，不包含button和inductor，应键入：smart watch-(button*inductor)";
	arry[25] = "<strong>申请号</strong><br/>    &nbsp;&nbsp;1.已知完整的申请号，应键入：WO2012US70742<br/>   &nbsp;&nbsp;2.已知申请号的前五位，应键入：WO2012US <br/>   &nbsp;&nbsp;3.已知申请号包含WO2012US70742或WO2012US67473，应键入：WO2012US70742+WO2012US67473<br/>   &nbsp;&nbsp;注：申请号前两位字母为国别代码，申请号检索必须输入国别代码";
	arry[26] = "<strong>申请日</strong><br/>    &nbsp;&nbsp;1.已知完整日期，应键入：20130520<br/>   &nbsp;&nbsp;2.已知月份，应键入：201305 <br/>   &nbsp;&nbsp;3.已知年份，应键入：2013<br/>   &nbsp;&nbsp;4.已知时间连续范围（不大于5年），应键入：20080202>20110101<br/>   &nbsp;&nbsp;5.已知时间范围包含2011年或2012年，应键入：2011+2012<br/>   &nbsp;&nbsp;注：申请日/公开日检索日期格式为YYYY或YYYYMM或YYYYMMDD";
	arry[27] = "<strong>公开/公告号</strong><br/>    &nbsp;&nbsp;1.已知完整的公开号，应键入：WO2014084868A1<br/>   &nbsp;&nbsp;2.已知公开号前五位，应键入：WO20140 <br/>   &nbsp;&nbsp;3.已知公开号包含WO2014084868A1或WO2014085796A2，应键入：WO2014084868A1+WO2014085796A2<br/>   &nbsp;&nbsp;注：公开/公告号前两位字母为国别代码，公开/公告号检索必须输入国别代码";
	arry[28] = "<strong>公开/公告日</strong><br/>    &nbsp;&nbsp;1.已知完整日期，应键入：20130520<br/>   &nbsp;&nbsp;2.已知月份，应键入：201305 <br/>   &nbsp;&nbsp;3.已知年份，应键入：2013<br/>   &nbsp;&nbsp;4.已知时间连续范围（不大于5年），应键入：20080202>20110101<br/>   &nbsp;&nbsp;5.已知时间范围包含2011年或2012年，应键入：2011+2012<br/>   &nbsp;&nbsp;注：申请日/公开日检索日期格式为YYYY或YYYYMM或YYYYMMDD";
	arry[29] = "<strong>申请人</strong><br/>    &nbsp;&nbsp;1.已知完整的申请人名称为SERRA MARCO，应键入：SERRA MARCO<br/>   &nbsp;&nbsp;2.已知申请人的一半姓名，应键入：SERRA$ <br/>   &nbsp;&nbsp;3.已知申请人的姓名包含SERRA MARCO和 EATON EDWIN A，应键入：SERRA MARCO* EATON EDWIN A<br/>   &nbsp;&nbsp;4.已知申请人的姓名包含SERRA MARCO或 EATON EDWIN A，应键入：SERRA MARCO+ EATON EDWIN A<br/>   &nbsp;&nbsp;5.已知申请人的姓名包含SERRA MARCO，不包含EATON EDWIN A，应键入：SERRA MARCO-EATON EDWIN A<br/>   &nbsp;&nbsp;6.已知申请人的姓名包含SERRA MARCO，不包含EATON EDWIN A和RODGERS JOHN P，应键入：SERRA MARCO-（EATON EDWIN A*RODGERS JOHN P）";
	arry[30] = "<strong>发明人</strong><br/>    &nbsp;&nbsp;1.已知完整的发明人名称为SERRA MARCO，应键入：SERRA MARCO<br/>   &nbsp;&nbsp;2.已知发明人的一半姓名，应键入：SERRA$<br/>   &nbsp;&nbsp;3.已知发明人的姓名包含SERRA MARCO和 EATON EDWIN A，应键入：SERRA MARCO* EATON EDWIN A<br/>   &nbsp;&nbsp;4.已知发明人的姓名包含SERRA MARCO或 EATON EDWIN A，应键入：SERRA MARCO+ EATON EDWIN A<br/>   &nbsp;&nbsp;5.已知发明人的姓名包含SERRA MARCO，不包含EATON EDWIN A，应键入：SERRA MARCO-EATON EDWIN A<br/>   &nbsp;&nbsp;6.已知发明人的姓名包含SERRA MARCO，不包含EATON EDWIN A和RODGERS JOHN P，应键入：SERRA MARCO-（EATON EDWIN A*RODGERS JOHN P）";
	arry[31] = "<strong>分类号</strong><br/>    &nbsp;&nbsp;1.已知完整的IPC分类号是A47L 13/257,应键入：A47L13/257<br/>   &nbsp;&nbsp;2.已知IPC分类号的前五位是A47L 3，应键入：A47L3 <br/>   &nbsp;&nbsp;3.已知IPC分类号包含A47L或B64D，应键入：A47L+B64D";
	arry[32] = "<strong>主分类号</strong><br/>    &nbsp;&nbsp;1.已知完整的IPC分类号是F24C 7/08,应键入：F24C7/08<br/>   &nbsp;&nbsp;2.已知IPC分类号的前五位是F24C 7，应键入：F24C7 <br/>   &nbsp;&nbsp;3.已知IPC分类号包含A47L或B64D，应键入：A47L+B64D";
	arry[33] = "<strong>优先权号</strong><br/>    &nbsp;&nbsp;1.已知完整优先权号，应键入：US201261730605P<br/>   &nbsp;&nbsp;2.已知优先权号前五位，应键入：US201 <br/>   &nbsp;&nbsp;3.已知优先权号包含US201213689194或US201313737740，应键入：US201213689194+US201313737740<br/>   &nbsp;&nbsp;注：优先权号检索必须输入国别代码";
	arry[34] = "<strong>引用文献</strong><br/>    &nbsp;&nbsp;1.已知完整的文献号，应键入：EP0468134A2<br/>   &nbsp;&nbsp;2.已知文献号前五位，应键入：EP046 <br/>   &nbsp;&nbsp;3.已知文献号包含US5623614A或EP0468134A2，应键入：US5623614A+EP0468134A2";
	arry[35] = "<strong>欧洲分类</strong><br/>    &nbsp;&nbsp;1.已知完整的ECLA号是A47J 36/02，应键入：A47J36<br/>   &nbsp;&nbsp;2.已知ECLA号前五位是B03C 1，应键入：B03C01 <br/>   &nbsp;&nbsp;3.已知ECLA号包含B03C或C22B，应键入：B03C+C22B";
	for (var i = 1; i <= 35; i++) {
		$("#Txt" + i).tooltip({
					track : true,
					content : arry[i]
				});
	}
});

$(function() {
			$("#Txt17").autocomplete({
						minLength : 0,
						source : "/front/search/table_getCoList",
						delay : 1000,
						select : function(event, ui) {
							$("#Txt17").val(ui.item.value);
							return false;
						}
					});

			$("#Txt18").autocomplete({
						minLength : 0,
						delay : 1000,
						source : "/front/search/table_getAgList",
						select : function(event, ui) {
							$("#Txt18").val(ui.item.value);
							return false;
						}
					});
		});

function buildCprs_old() {
	var searchHead = "F XX ";
	var searchbody = "";
	var sybol = "";
	$("input[type='text']").each(function(i, n) {
		if (this.value != "") {
			searchbody = searchbody + sybol + "*(" + this.value + "/"
					+ this.lang.toUpperCase() + ")";
		}
	});
	var i = searchbody.length;
	if (i == 0)
		alert("请输入检索式！");
	else {
		searchbody = searchbody.substring(1, i);
		$("#TxtSearch").val(searchHead + searchbody);
	}
}

function buildCprsNew() {
	d = dialog({
				title : '',
				content : '',
				cancel : false,
				width : 320,
				fixed : true,
				okValue : "确定",
				ok : function() {
				}
			});
	var flag = "";
	var searchHead = "F XX ";
	var searchbody = "";
	var sybol = "";
	// var strBuild=/([A-Za-z0-9\u4e00-\u9fa5]+)([\+\-\*])([\u4e00-\u9fa5]+)/gi;
	var strBuild = /[ （）A-Za-z0-9\u4e00-\u9fa5$\/\·\、\，]+/gi              //\u4e00-\u9fa5代表所有中文字符
	var str1 = /[\+\-\*]/;

	$("input[type='text']").each(function(i, n) {
		if (this.value != "") {

			// *******************************
			if (this.value != ""  && (this.lang.toUpperCase() == "AN"||this.lang.toUpperCase() == "PN")) {
				if($("#Txt7").val().length>0){     //国内申请号
					if (/\+\++|\+\-+|\-\++|\+\*+|\-\*+|\-\-+|\*\*+/gi
							.test(this.value)) {
						flag = "逻辑运算符不正确！";
						$("#judge").val("false");
						return false;
					}
					//if (!/^(\d{4,13}|\d{2,8})(\.\*|\?)?([\+\-\*](\d{4,12}|\d{2,8})(\.\*|\?)?)*$/
					if (!/^(\d{4,12}|\d{2,8})(\.\w|\w)?([\+\-\*](\d{4,12}|\d{2,8})(\.\*|\?)?)*$/
							.test(this.value)) {
						flag = this.value
								+ "格式不正确，请输入8位或12、13位数字，不需要带检验位";
						$("#judge").val("false");
						return false;
					}
					var txt7 = $("#Txt7").val();// 申请号2-12位
					var _txt7 = txt7.replace(/[（）]*/gi, "");
					if (/[^0-9\(\)\（\）\+\-\*\?\.\X\x]/gi.test(txt7)) {
						flag = "申请号必须为数字，且长度为2-12位!";
						$("#judge").val("false");
						return false;
					}
					var ma = _txt7.match(/\b[0-9]{8}((\.\*)|\?)?\b/gi);
					if (ma != null) {
						for (var i = 0; i < ma.length; i++) {
							if (ma[i].substring(0, 1) == 0) {
								_temp = "20" + ma[i].substring(0, 3) + "00"
										+ ma[i].substring(3, 10);
								_txt7 = _txt7.replace(ma[i], _temp);
							} else {
								_temp = "19" + ma[i].substring(0, 3) + "00"
										+ ma[i].substring(3, 10);
								_txt7 = _txt7.replace(ma[i], _temp);
							}
						}
					}
					searchbody = searchbody
							+ sybol
							+ "*"
							+ _txt7.replace(/[\.\*\?A-Za-z0-9\u4e00-\u9fa5]+/gi,
									"$&/" + this.lang.toUpperCase()).replace(/.*/,
									"($&)");
				}else if($("#Txt25").val().length>0){    //国外是申请号
					if (/\+\++|\+\-+|\-\++|\+\*+|\-\*+|\-\-+|\*\*+/gi
							.test(this.value)) {
						flag = "逻辑运算符不正确！";
						$("#judge").val("false");
						return false;
					}
					var txt25 = $("#Txt25").val();// 申请号2-12位
					var regTxt25= /^[A-Za-z]+$/;
					if(!regTxt25.test(txt25.substring(0,2))){
						flag="申请号必须以两位国别代码开头，紧跟着0-14位字母或者数字!";
						$("#judge").val("false");
						return false;
					}
					var _txt25 = txt25.replace(/[（）]*/gi, "");
					searchbody = searchbody
							+ sybol
							+ "*"
							+ _txt25.replace(/[\.\*\?A-Za-z0-9\u4e00-\u9fa5]+/gi,
									"$&/" + this.lang.toUpperCase()).replace(/.*/,
									"($&)");
				}else if($("#Txt9").val().length>0){    //国外是申请号
					if (/\+\++|\+\-+|\-\++|\+\*+|\-\*+|\-\-+|\*\*+/gi
							.test(this.value)) {
						flag = "逻辑运算符不正确！";
						$("#judge").val("false");
						return false;
					}
					var txt9 = $("#Txt9").val();// 申请号2-12位
					if(!/^\d{2,9}[a-z]?([\+\-\*]\d{2,9}[a-z]?)*$/i.test(txt9)){
						flag="公开号必须为2-9位的数字!";
						$("#judge").val("false");
						return false;
					}
					var _txt9 = txt9.replace(/[（）]*/gi, "");
					searchbody = searchbody
							+ sybol
							+ "*"
							+ _txt9.replace(/[\.\*\?A-Za-z0-9\u4e00-\u9fa5]+/gi,
									"$&/" + this.lang.toUpperCase()).replace(/.*/,
									"($&)");
				}else if($("#Txt27").val().length>0){    //国外是公开号
					if (/\+\++|\+\-+|\-\++|\+\*+|\-\*+|\-\-+|\*\*+/gi
							.test(this.value)) {
						flag = "逻辑运算符不正确！";
						$("#judge").val("false");
						return false;
					}
					var txt27 = $("#Txt27").val();// 公开号号2-12位
					var regTxt27= /^[A-Za-z]+$/;
					if(!regTxt27.test(txt27.substring(0,2))){
						flag="公开号必须以两位国别代码开头，紧跟着0-14位字母或者数字!";
						$("#judge").val("false");
						return false;
					}
					var _txt27 = txt27.replace(/[（）]*/gi, "");
					searchbody = searchbody
							+ sybol
							+ "*"
							+ _txt27.replace(/[\.\*\?A-Za-z0-9\u4e00-\u9fa5]+/gi,
									"$&/" + this.lang.toUpperCase()).replace(/.*/,
									"($&)");
				}
			}else if($("#Txt33").val().length>0){    //国外是公开号
				if (/\+\++|\+\-+|\-\++|\+\*+|\-\*+|\-\-+|\*\*+/gi
						.test(this.value)) {
					flag = "逻辑运算符不正确！";
					$("#judge").val("false");
					return false;
				}
				var txt33 = $("#Txt33").val();// 公开号号2-12位
				var regTxt33= /^[A-Za-z]+$/;
				if(!regTxt33.test(txt33.substring(0,2))){
					flag="优先权号必须以两位国别代码开头，紧跟着0-14位字母或者数字!";
					$("#judge").val("false");
					return false;
				}
				var _txt33 = txt33.replace(/[（）]*/gi, "");
				searchbody = searchbody
						+ sybol
						+ "*"
						+ _txt33.replace(/[\.\*\?A-Za-z0-9\u4e00-\u9fa5]+/gi,
								"$&/" + this.lang.toUpperCase()).replace(/.*/,
								"($&)");
			}else if($("#Txt34").val().length>0){    //国外是公开号
				if (/\+\++|\+\-+|\-\++|\+\*+|\-\*+|\-\-+|\*\*+/gi
						.test(this.value)) {
					flag = "逻辑运算符不正确！";
					$("#judge").val("false");
					return false;
				}
				var txt34 = $("#Txt34").val();// 公开号号2-12位
				var regTxt34= /^[A-Za-z]+$/;
				if(!regTxt34.test(txt34.substring(0,2))){
					flag="引用文献必须以两位国别代码开头，紧跟着0-14位字母或者数字!";
					$("#judge").val("false");
					return false;
				}
				var _txt34 = txt34.replace(/[（）]*/gi, "");
				searchbody = searchbody
						+ sybol
						+ "*"
						+ _txt34.replace(/[\.\*\?A-Za-z0-9\u4e00-\u9fa5]+/gi,
								"$&/" + this.lang.toUpperCase()).replace(/.*/,
								"($&)");
			}
			// ******************************

			else if (this.lang.toUpperCase() == "AD"
					|| this.lang.toUpperCase() == "PD"
					|| this.lang.toUpperCase() == "GD") {
				if (!/^\d{4}$|^\d{6}$|^\d{8}$|^\d{8}>\d{8}$|^\d{4,8}[\+\*-]\d{4,8}$/
						.test(this.value)) {
					flag = "请输入正确的日期格式！";
					$("#judge").val("false");
					return false;
				} else if (/^(\d{8})>(\d{8})$/.test(this.value)) {
					var arr = new Array();
					arr = this.value.split(">");
					console.log(arr[0]);
					console.log(arr[1]);
					if (parseInt(arr[0]) > parseInt(arr[1])) {
						flag = "范围检索时日期输入格式应为 yyyymmdd>yyyymmdd,在先的日期在前";
						$("#judge").val("false");
						return false;
					}

				}
				searchbody = searchbody
						+ sybol
						+ "*"
						+ this.value.replace(/[>0-9]+/gi,
								"$&/" + this.lang.toUpperCase()).replace(/.*/,
								"($&)");

			} else {
				if (/\+\++|\+\-+|\-\++|\+\*+|\*\++|\-\*+|\-\-+|\*\*+/gi
						.test(this.value)) {
//					.test(this.value.trim())) {
					flag = "逻辑运算符不正确！";
					$("#judge").val("false");
					return false;
				}
				
				var strs = this.value.split("+");
				for(var i=0;i<strs.length;i++){
					if(strs[i]==""){
						flag="逻辑运算符不正确";
						$("#judge").val("false");
						return false;
					}
				}
				
				var strs2 = this.value.split("*");
				for(var i=0;i<strs2.length;i++){
					if(strs2[i]==""){
						flag="逻辑运算符不正确";
						$("#judge").val("false");
						return false;
					}
				}
				
				var strs3 = this.value.split("-");
				for(var i=0;i<strs3.length;i++){
					if(strs3[i]==""){
						flag="逻辑运算符不正确";
						$("#judge").val("false");
						return false;
					}
				}
				
				searchbody = searchbody
						+ sybol
						+ "*"
						//+ this.value.replace(/・/,"·").replace(/[（）]+/gi, " ").replace(/.*/,"($&)");
				+ this.value.replace(/・/,"·").replace(/[（）]+/gi, " ").replace(strBuild,"$&/" + this.lang.toUpperCase()).replace(/.*/,"($&)");
			}
		}
	});
	var i = searchbody.length;
	if (flag != "") {
		d.content(flag);
		d.showModal();
	}
//	else if (flag == "" && i == 0) {
//		d.content("请输入检索式！");
//		return false;
//		d.showModal();
//	}
	else {
		flag = validateForm();
		if (flag == "") {
			searchbody = searchbody.substring(1, i);
			// 这里加上了专利类型过滤
			var patentTypeStr = getPatentTypeStr();
			if (patentTypeStr) {
				searchbody = searchbody + patentTypeStr;
			}
			$("#TxtSearch").val(searchHead + searchbody);
			var idsval= document.getElementById('ids').value;
			if(idsval!="on"&&idsval!="CN,US,DE,JP,GB,FR,KR,RU,CH,EP,WO,OT"&&idsval!=""){
				changeId();
				$("#TxtSearch").val($("#TxtSearch").val()+"@CO="+idsval);
			}
		} else {
			d.content(flag);
			d.showModal();
		}
	}
}

function validateForm() {
	// AN
	/*
	 * var txt7 = $("#Txt7").val().trim();//申请号4-12位 var str7=/(\d{4,12})|\s/gi;
	 * if(!str7.test(txt7)&& txt7.length>0){ return "申请号必须为数字，且长度为4-12位!"; }
	 */
	// PN
	var txt9 = $("#Txt9").val();
//	var txt9 = $("#Txt9").val().trim();

	if (txt9.length > 0
			&& !/^\d{2,9}[a-z]?([\+\-\*]\d{2,9}[a-z]?)*$/i.test(txt9)){
		$("#judge").val("false");
		return "公开号必须为2-9位的数字！";
	}
		
	// if(/[^0-9a-z\+\-\*]/gi.test(txt11)) return "公号必须为2-9位的数字！";

	// GN
	var txt11 = $("#Txt11").val();
//	var txt11 = $("#Txt11").val().trim();
	if (txt11.length > 0
			&& !/^\d{2,9}[a-z]?([\+\-\*]\d{2,9}[a-z]?)*$/i.test(txt11)){
		$("#judge").val("false");
		return "公告号必须为2-9位的数字！";
	}
		
	// var str11=/\b(\d{2,9}[a-z]?)/gi;
	// if(/[^0-9a-z\+\-\*]/gi.test(txt11)) return "公告号必须为2-9位的数字！";

	// PR
	var txt13 = $("#Txt13").val();
//	var txt13 = $("#Txt13").val().trim();
	var str13 = /\b((\d|\w){2,})\b/gi;
	if (/[^0-9a-z\+\-\*]/gi.test(txt13)){
		$("#judge").val("false");
		return "优先权必须为2位及以上字母或数字";
	}
		
	if (!str13.test(txt13) && txt13.length > 0) {
		$("#judge").val("false");
		return "优先权必须为2位及以上字母或数字";
	}

	// CT
	var txt15 = $("#Txt15").val();
//	var txt15 = $("#Txt15").val().trim();
	var str15 = /\b(\d{2})([a-z]{1})\b/gi;
	if (/[^0-9a-z]/gi.test(txt15)){
		$("#judge").val("false");
		return "范畴分类格式:2位数字+1位字母!";
	}
		
	if (!str15.test(txt15) && txt15.length > 0) {
		$("#judge").val("false");
		return "范畴分类格式:2位数字+1位字母!";
	}

	// CO
	var txt17 = $("#Txt17").val();
//	var txt17 = $("#Txt17").val().trim();
	var str17 = /\b((\d|\w){2})\b|\s/gi;
	if (/[^0-9a-z]/gi.test(txt17)){
		$("#judge").val("false");
		return "请输入两位国省代码!";
	}
		
	if (!str17.test(txt17) && txt17.length > 0) {
		$("#judge").val("false");
		return "请输入两位国省代码!";
	}

	// AG
	var txt18 = $("#Txt18").val();
//	var txt18 = $("#Txt18").val().trim();
	var str18 = /\b\d{5}\b/;
	if (/[^0-9]/gi.test(txt18)){
		$("#judge").val("false");
		return "请输入正确的代理机构代码!!";
	}
		
	if (!str18.test(txt18) && txt18.length > 0) {
		$("#judge").val("false");
		return "请输入正确的代理机构代码!!";
	}
	if (txt18 == "00000"){
		$("#judge").val("false");
		
		return "代理机构代码不能为'00000'!";
	}

	return "";
}
function ajaxSearch() {
	$.ajax({
				type : 'post',
				url : '',
				data : '',
				error : function() {
				},
				success : function(msg) {

				}
			});
}
var m_nTetCount = 35;
function cnClearSearch() {
	for (var i = 1; i <= m_nTetCount; i++) {
		var obj = document.getElementById("Txt" + i);
		if (obj != null)
			obj.value = "";
	}
	document.getElementById("TxtSearch").value = "";

}
function ajaxSearch() {
	$.ajax({
				type : 'post',
				url : '',
				data : '',
				error : function() {
				},
				success : function(msg) {

				}
			});
}
function addcommand(obj) {
	var strCommand = obj.name;

	var destinationObj = document.getElementById("TxtSearch");
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

// 返回用户在表格检索中，所选择检索专利类型字符串
function getPatentTypeStr() {
	var patentTypeStr = "@LX=";
	var patenttype_fm = document.getElementById("patenttype_fm").checked;
	var patenttype_wg = document.getElementById("patenttype_wg").checked;
	var patenttype_xx = document.getElementById("patenttype_xx").checked;
	if (patenttype_fm) {
		patentTypeStr = patentTypeStr + "FM,";
	}
	if (patenttype_wg) {
		patentTypeStr = patentTypeStr + "WG,";
	}
	if (patenttype_xx) {
		patentTypeStr = patentTypeStr + "XX,";
	}
	if (patentTypeStr.length > 4) {
		patentTypeStr = patentTypeStr.substring(0, patentTypeStr.length - 1);

		return patentTypeStr
	} else {
		return "";
	}

}
