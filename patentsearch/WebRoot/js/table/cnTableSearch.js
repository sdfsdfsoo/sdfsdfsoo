//--------------------------------------表格检索-------------------------------------------//
//return
//1、 ajax页面超时。在ajax请求字段中设置"timeout: 15000,"，在返回的error中处理超时错误；
//2、 SocketSearch返回错误：1、 请求已经超时!；2、检索时发生错误!
//3、 Main主机返回的超时信息

//检索式最大长度
var nQueryMaxLen = 1000;  //125;

function cnTableSearch(lang) {
    var strQuery = document.getElementById("TxtSearch").value.Trim();
    if (strQuery == "") {
        cnGenerateSearch();
        strQuery = document.getElementById("TxtSearch").value.Trim();
    }
    var reg = /(\d{9})(a|b|A|B)(\/PN|\/GN)/g; //去掉公开公告最后一位字母
    if (reg.test(strQuery)) {
        strQuery = strQuery.replace(reg, "$1$3");
    }
    var res = validateLogicSearchQuery(strQuery);


    var view = '查看';
    var viewPage = "cn/PatentGeneralList.html?";
    if (lang == 'En') {
        view = 'View';
        viewPage = "cn/PatentGeneralList.html?Ty=E&";
    }

    // 验证检索式
    if (res == "") {
        showError("检索式为空！");
        setSearchFormula("");
        return;
    }
    else if (res.indexOf("Error") != -1) {
        showError(res);
        setSearchFormula("");
        return;
    }
    else if (res.length > nQueryMaxLen) {
        showError("检索式不能超过" + nQueryMaxLen + "个字符！");
        setSearchFormula("");
        return;
    }
    //var res = "F XX (200302/AD + 200201/AD) * (计算机/TI)";
    //var res = "F XX ( (200302/AD + 200201/AD) * (计算机/TI)) + (200501/AD)";

    document.getElementById("TxtSearch").value = res;

    // 显示进度条
    showProgress();
    res = encodeURIComponent(res);
    $.ajax({
        type: "POST",
        url: "cnTableSearch.aspx/tableSearch",
        data: "{'strSearchQuery':'" + res + "'}",
        timeout: 30000, // set time out 30 seconds
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            closeProgress();
            var msg = XMLHttpRequest.responseText;
            if (textStatus == "timeout") {
                msg = "检索超时，请稍后再试！";
            }
            showError(msg);

            return;

        },
        success: function (msg) {
            // 撤销进度条
            closeProgress();

            var strMsg = msg.d;
            if (strMsg.indexOf('申请号校验位错误') != -1) { // 申请号校验位判断
                alert('检索结果为零');
                return;
            } else if (strMsg.indexOf("<hit") != -1) // main返回正确结果
            {
                var num = "";
                var splitReg = /<hits:\s*(\d+)>/;
                if (splitReg.test(strMsg)) {
                    num = RegExp.$1;
                }
                // 将检索编号替换从999替换到000
                strMsg = strMsg.replace("(999)", "(000)");

                if (num == "0" || num == 0) {
                    alert("检索结果为零！");
                    return;
                } else {

                    //addToTab(viewPage + "No=999&Query=" + encodeURIComponent(res) + "&Nm=" + num, 'CN000', true,true);
                    window.location.href = viewPage + "No=999&Query=" + encodeURIComponent(res) + "&Nm=" + num;
                    return;
                }

                var strView = "";
                if (parseInt(num) != 0)
                    strView = "&nbsp;&nbsp;<a href='#' onclick=\"addToTab('" + viewPage + "No=999&Query=" + encodeURIComponent(res) + "&Nm=" + num + "','CN000',true,true)\" >" + view + "</a>";

                strMsg = strMsg.replace(">", "&gt");
                strMsg = strMsg.replace("<", "&lt");

                document.getElementById("ResultBlock").style.display = "block";
                document.getElementById("LabResult").innerHTML = strMsg + strView;
            }
            else if (strMsg.indexOf("<error") != -1) // main返回错误结果
            {
                showError(strMsg);

            }
            else if (strMsg.indexOf("请求已经超时!") != -1 || strMsg.indexOf("检索时发生错误!") != -1) {
                showError(strMsg);
            }
            else {
                showError(strMsg);
            }
        }
    });
}

// 生成检索式
function cnGenerateSearch() {
    setViewResult(" "); // 清空验证域
    var strQuery = getTableSearchQuery().Trim();
    if (strQuery == "") {
        showError("请输入要检索的内容！");
        //setSearchFormula("");
        return;
    }

    var reg = /(\d{7,9})([A-Za-z])(\/PN|\/GN)/g; //去掉公开公告最后一位字母
    if (reg.test(strQuery)) {
        strQuery = strQuery.replace(reg, "$1$3");
    }
    var res = validateLogicSearchQuery(strQuery);
    res = res.Trim();
    var obj = document.getElementById("TxtSearch");
    if (res.substring(0, 5) == "Error")
        showError(res);
    else
        obj.value = getMergeSearchEndFlag(res);
    //obj.style.background-color.value = '#FFFFFF';
    //obj.style.background-color.value = '#0066FF';


}
// 清空所有text框架
function cnClearSearch() {
    //    $("input[@type=text]").attr("value", '');
    //    var objArr = $("input[@type=text]");
    //    for(var item in objArr)
    //    {
    //       objArr[item].value = "";
    //    }
    for (var i = 1; i <= m_nTetCount; i++) {
        var obj = document.getElementById("Txt" + i);
        if (obj != null) obj.value = "";
    }
    document.getElementById("TxtSearch").value = "";


}
// 从表格检索页面获取检索式。检索式包括两种格式： 1, F 计算机 TI; 2, F XX (计算机/TI)*(2008/PD)，分别为简单检索和复杂检索。
// 检索式由两部分构成，检索头(F or F XX)和检索体(计算机/TI)*(2008/PD)。
var m_strSimpleSeachHead = "F"; //简单检索
var m_strComplexSeachHead = "F XX"; //复杂检索
var m_strEntFlag = "/"; //检索式中入口及检索内容间的连接符，如(2005/AN)中的“/”
var m_nTetCount = 23;  //文本框数量

function getTableSearchQuery() {
    var arrKey = new Array();       // 保存检索入口字段，如“TI”
    var arrValue = new Array();  // 保存检索入口内容，如“计算机”
    var objTxb; //文本框对象
    var strEntFlag = m_strEntFlag; //检索式中入口及检索内容间的连接符(2005/AN)
    var strLogicSymbol = getLogicSymbolFromTableSearchPage(); // 检索入口之间的逻辑运算符；

    var res = "";   // 保存表格检索式
    // 遍历检索入口id，获得对应的值，加入数组
    for (var i = 1; i <= m_nTetCount; i++) {
        var obj = document.getElementById("Txt" + i);
        if (obj == null)
            continue;
        var strValue = obj.value.Trim();
        var strKey = obj.lang.toUpperCase();


        // 给检索入口内容后缀上/key字符
        if (strValue != "") {

            //            strValue = strValue.replace(/\（/g, "(");
            //            strValue = strValue.replace(/\）/g, ")");

            //            strValue = strValue.replace(/\(/g, " ");
            //            strValue = strValue.replace(/\)/g, " ");

            strValue = strValue.replace(/\（/g, " ");
            strValue = strValue.replace(/\）/g, " ");


            // 在检索内容后缀上检索入口
            var _strTemp = "";
            //var regValue2 = /([^\-|+|*]*)(\**)([\s*\)\s*]*)([^\-|+|*]*)([\-|+|*])/g;
            var regValue = /([^\-|+|*]*)([\-|+|*])/g;
            var strValueReg = strValue + "+"; // 给value后缀上一个+号，以便处理最后一个检索入口

            // 测试用例
            // 1 (The + automatic(7), ) * rolling
            // 2 (The + automatic((7)), ) * rolling
            // 3 (The + (abc)automatic((7)) ) * rolling
            // 设num为匹配字符串中右括号数量减左括号的数量，则从右往左数)num个，在此位置缀入检索入口即可
            while (strResXH = regValue.exec(strValueReg)) {
                var str1 = RegExp.$1;
                var str2 = RegExp.$2;
                str1 = str1.Trim();
                var lastcharacter = str1.substring(str1.length - 1);

                if (lastcharacter == ")") {
                    var youkuohaoarr = str1.split(")");
                    var youkuohaonum = youkuohaoarr.length - 1;
                    if (youkuohaonum != 0) {
                        var zuokuohaoarr = str1.split("(");
                        var zuokuohaonum = zuokuohaoarr.length - 1;
                        // 在第zuokuohaonum+1个右括号之前缀入检索入口
                        var tempres = "";
                        for (var n = 0; n <= youkuohaonum; n++) {
                            if (n == zuokuohaonum) {
                                tempres = tempres + youkuohaoarr[n] + "/" + strKey + ")";
                            }
                            else {
                                tempres = tempres + youkuohaoarr[n] + ")";
                            }
                        }
                        tempres = tempres.substring(0, tempres.length - 1); // 去掉最后一个括号
                        _strTemp = _strTemp + tempres + str2;
                    }
                    else {
                        if ("" != youkuohaoarr[0].Trim())
                            _strTemp = _strTemp + str1 + "/" + strKey + str2;
                        else
                            _strTemp = _strTemp + str1 + str2;
                    }
                }
                else {
                    _strTemp = _strTemp + str1 + "/" + strKey + str2;
                }

            }
            // 去掉最后一个字段的+
            strValue = _strTemp.substring(0, _strTemp.length - 1)

            res += "(" + strValue + ")" + strLogicSymbol;
        }

    }
    if (res.length != 0) {
        res = res.TrimEnd();
        return "F XX " + res;
    }
    else
        return "";

}


//--------------------------------------检索式验证-------------------------------------------//

// 针对检索入口进行验证，错误则返回错误提示，以“Error”开头，否则即为修改后的正确检索式
// 验证的内容针对检索入口及检索入口的内容是否合法验证，并予以适当修改
// 不进行页面提示设置
// 以下都是合法的检索式：1， F  TI 计算机； 2， F XX (计算机/TI)*(2008/PD)； 3，计算机 TI；4，(计算机/TI)*(2008/PD)； 5， F XX (计算机/TI)；
// var res = "F XX (200302/AD + 200201/AD) * (计算机/TI)";
// var res = "F XX ( (200302/AD + 200201/AD) * (计算机/TI)) + (200501/AD)";
var m_Entrances = "AN,PN,IC,PR,IN,PA,AG,TI,CL,AD,PD,CT,CO,DZ,TX,AB,DS,CS,GN,GD,AE,IE,PE,TE,MC,AT,"; //所有入口,以，连接多个
function validateLogicSearchQuery(strSearchQuery) {
    var strQuery = strSearchQuery.Trim().ReduceSpace();

    if (strQuery == "")
        return "";
    var strQueryHead = "";
    var strQueryBody = "";
    strQuery = strQuery.replace(/（/g, "(");
    strQuery = strQuery.replace(/）/g, ")");
    // 切割检索式
    var splitReg = /^\s*([J|F|j|f]\s{1,}[XX|xx|Xx|xX]*)(.*)$/;
    if (splitReg.test(strQuery)) // 带检索头
    {
        strQueryHead = RegExp.$1.toUpperCase();
        strQueryBody = RegExp.$2;
        strQueryHead = strQueryHead.Trim();
        strQueryBody = strQueryBody.Trim();
    }
    else // 不带检索头
    {
        strQueryBody = strQuery;
    }

    // 验证检索式
    if (strQueryHead == "J") // 交叉检索，客户端不予验证
    {

        return strQueryHead + " " + strQueryBody;
    }
    else {
        var simpleSearchReg = /^([A-Za-z]{2})\s+([^\/].*)$/;

        if (simpleSearchReg.test(strQueryBody)) // 简单检索
        {
            strQueryHead = "F";
            var key = RegExp.$1.toUpperCase();
            var value = RegExp.$2;
            var res = getValueAccordingToKey(key, value);
            if (res.substring(0, 5) == "Error") {
                return res;
            }
            else
                strQueryBody = key + " " + res;
        }
        else if (strQueryBody.indexOf("/") == -1) // 不是简单检索又不包含符号/，则认为它是交叉检索，比如2+3
        {
            var regJ = /[\-|+|*]/;
            if (regJ.test(strQueryBody))    // 交叉检索
                return "J " + strQueryBody;
            else
                return "Error:检索式不合法";
        }
        else  // 复杂检索，带/连接符号
        {
            // 从检索式中循环找符合正则式/\/\s*[A-Za-z]{2}/的结构，该结构即为检索入口
            // 向前寻找检索入口对应的内容，遇到以下字符则停止
            // 1    [+|-|*-];   2   [(|)]   3  字符串头
            // 找到后，消除检索内容之前的空白和括号等，然后进行验证和修改

            strQueryHead = " F XX"
            strQueryBodyTemp = strQueryBody;

            var getValueKeyReg = /\/\s*([A-Za-z]{2})/g;
            //var getValueKeyReg = /\/\n\s*([A-Za-z]{2})/g;  //修改PAD中联合检索
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
                }
                else {

                    var zhuanyiValue = value.replace(/\+/g, "\\+");
                    zhuanyiValue = zhuanyiValue.replace(/\-/g, "\\-");
                    zhuanyiValue = zhuanyiValue.replace(/\*/g, "\\*");
                    zhuanyiValue = zhuanyiValue.replace(/\(/g, "\\(");
                    zhuanyiValue = zhuanyiValue.replace(/\)/g, "\\)");

                    var strReg = zhuanyiValue + "\\s*\\\/\\s*" + oldKey;
                    var reg = new RegExp(strReg, "gi");
                    strQueryBodyTemp = strQueryBodyTemp.replace(reg, res + "/" + key);
                }

            }
            strQueryBody = strQueryBodyTemp.Trim();

        }
    }

    return strQueryHead.Trim() + " " + strQueryBody.Replace("'", " ");

}

// 测试是否为合法的检索入口，测试对应的检索入口内容是否符合规范
// 格式错误，返回"Error:"开头的字符串字符串，如果正确，则返回修改后的strVal,8510 + 8610/AD将变为198510/AD + 198610/AD
// value 是可以嵌套括号的，因此验证的时候需要去掉括号和空白
function getValueAccordingToKey(key, value) {
    var entrances = m_Entrances;
    if (entrances.indexOf(key + ",") == -1) {
        return "Error:非法检索入口!";
    }

    var txtId = key;
    var strVal = value.split(/[\-|+|*]/);    // 逻辑链接符
    var linkArr = value.match(/[\-|+|*]/g);    // 逻辑链接符


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
        if (regExp1.test(strVal[i]) || regExp2.test(strVal[i]) || regExp3.test(strVal[i])) {
            return "Error:禁止包含非法字符:not,and or!";
        }
    }

    //strRes = "Error:请输入正确的代理机构代码!";
    if (txtId == "AG") {
        for (var i = 0; i < strVal.length; i++) {
            var valueTemp = strVal[i].Trim();
            valueTemp = valueTemp.TrimLink();
            var reg = /^\d{5}$/;
            if (!reg.test(valueTemp)) {
                return "Error:请输入正确的代理机构代码!";
            }
            else if ("00000" == value) {
                return 'Error:代理机构代码不能为"00000"';
            }
        }
    }
    //strRes = 
    //    if ( txtId == "PR" )
    //    {
    //        for(var i = 0; i<strVal.length; i++) {
    //            var valueTemp = strVal[i].Trim();
    //            valueTemp = valueTemp.TrimLink();
    //            var reg = /^.{2,}$/;
    //            if (!reg.test(valueTemp)) {
    //                return "Error:优先权须为2位以上字母或数字组合!";
    //            }
    //        }
    //    }
    // 国省代码
    if (txtId == "CO") {
        for (var i = 0; i < strVal.length; i++) {
            var valueTemp = strVal[i].Trim();
            valueTemp = valueTemp.TrimLink();
            var reg = /^\w{2}$/;
            if (!reg.test(valueTemp)) {
                return "Error:请输入两位国省代码!";
            }
        }
    }
    // 
    if (txtId == "AE" || txtId == "IE" || txtId == "PE" || txtId == "TE") {
        for (var i = 0; i < strVal.length; i++) {
            //            var valueTemp = strVal[i].Trim();
            //            valueTemp = valueTemp.TrimLink();
            //            var reg = /^([\u4E00-\u9FA5]|[\uFE30-\uFFA0])*$/gi;
            //            if (reg.test(valueTemp)) {
            //                return "Error:英文入口中不能包含汉字!";
            //            }
        }

    }


    //strRes = "Error:申请号必须为数字，且长度为4-12位!";
    if (txtId == "AN") {
        for (var i = 0; i < strVal.length; i++) {
            var valueTemp = strVal[i].Trim();
            valueTemp = valueTemp.TrimLink();
            var res = getApplyNo(valueTemp);

            if (res.substring(0, 5) == "Error") {
                return res;
            }
            else {
                value = value.replace(strVal[i].Trim(), res);

            }
        }
    }

    // 优先权必须为2位以上字母或数字
    if (txtId == "PR") {
        for (var i = 0; i < strVal.length; i++) {
            var valueTemp = strVal[i].Trim();
            valueTemp = valueTemp.TrimLink();
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
            }
            else
                value = value.replace(old, res);    // 设置value为修正后的日期值
        }
    }
    //strRes = "Error:公开号必须为2-9位的数字，检索式支持尾部通配符*";
    if (txtId == "PN" || txtId == "GN") {
        for (var i = 0; i < strVal.length; i++) {
            var valueTemp = strVal[i].Trim();
            valueTemp = valueTemp.TrimLink();

            var reg = /^(\d{2,8})(\d|\*)*$/;
            if (!reg.test(valueTemp)) {
                return "Error:" + (txtId == "PN" ? "公开" : "公告") + "号必须为2-9位的数字，检索式支持前方一致";
            }
        }
    }
    //strRes = "Error:范畴分类格式:2位数字+1位字母"; 
    if (txtId == "CT") {
        for (var i = 0; i < strVal.length; i++) {
            var valueTemp = strVal[i].Trim();
            valueTemp = valueTemp.TrimLink();

            var reg = /^(\d{2})[A-Za-z]$/;
            if (!reg.test(valueTemp)) {
                return "Error:范畴分类格式:2位数字+1位字母";
            }
        }
    }
    //strRes = "Error:ipc 输入不合法"
    if (txtId == "IC" || txtId == "MC") {
        for (var i = 0; i < strVal.length; i++) {
            var valueTemp = strVal[i].Trim();
            valueTemp = valueTemp.TrimLink();
            var reg = /[^a-zA-Z0-9\/]/g;
            //            var _ipcQuery = valueTemp.replace(reg, '');
            //            if (_ipcQuery.length != valueTemp.length)
            //                return "Error:IPC不合法";
            if (valueTemp.length > 12)
                return "Error:IPC分类检索项长度应为3-12位";

            var strValTemp = valueTemp;
            if (strValTemp.indexOf('/') == -1) { // 如果检索项不包含/，则不进一步做验证；
                continue;
            }

            var arr = new Array();
            arr = strValTemp.split(/[\/]/);
            if (arr.length > 2)
                return "Error:" + txtId + "入口中只能包含一个/";
            var head = arr[0].Trim();
            var end = (arr.length == 2) ? arr[1].Trim() : "";
            //            headArr = head.split(/[\s]/);
            //            if (headArr.length ==1 && headArr[0].length != 7)
            //                return "Error:IC入口/前须为7位IC代码";
            //            else if (headArr.length == 2 )
            //            {
            //                if (headArr[0].length != 4)
            //                    return "Error:IC /前第一段必须为4位代码";
            //                else if (headArr[1].length > 3)
            //                    return "Error:IC /前第二段必须少于3位代码";
            //                    
            //            }
            if (end.length > 4)
                return "Error:" + txtId + " /后必须少于4位代码";

            // 去掉/，替换头和尾
            // value = value.replace(strValTemp,head+end);    

        }
    }
    return value;
}

// 验证和修改申请号
function getApplyNo(apNo) {
    var _apNo = apNo;
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
    
    if(_apNo.length>12){
    	_apNo=_apNo.substring(0, 12);
    }
    
    if (!reg.test(_apNo)) {
        return "Error:申请号输入为4-12位数字(不含校验位)，支持输入校验位[.?或?]!";
    }
    return _apNo;
}
// 判断是否为正确的日期格式，如YYYYMMDD或YYYY或YYYYMM或YY或YYMM或YYMMDD 
// 例如：19861010或者  201002-20100215 20100506+20100308 20100102>20100402 小日期在前
// 如果错误则返回以"Error"开头的字符串,否则返回修改过后的日期
function isDate(strDate) {
    var arr = new Array();
    arr = strDate.split(">");
    if (arr.length > 2) {
        return "Error:范围检索日期数太多";
    }
    else if (arr.length == 1) {
        var date = isSingleDate(arr[0]);
        if (date == "false")
            return "Error:日期格式如YYYYMMDD或YYYY或YYYYMM或YY或YYMM或YYMMDD";
        else
            return date;
    }
    else if (arr.length == 2) {
        var first = isSingleDate(arr[0]);
        var second = isSingleDate(arr[1]);
        if (first == "false" || second == "false") {
            return "Error:范围检索时日期输入格式应为 yyyymmdd>yyyymmdd,在先的日期在前";
        }
        else {

            if (first.length != 8 || second.length != 8 || parseInt(first, 10) > parseInt(second, 10))
                return "Error:范围检索时日期输入格式应为 yyyymmdd>yyyymmdd,在先的日期在前";
            else
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
    //alert(str);	    
    var reg = /^(\d{4})(\d{2})?(\d{2})?$/;
    if (reg.test(str) && RegExp.$2 <= 12 && RegExp.$3 <= 31) {
        return str;
    }
    else {
        return "false";
    }
}

// 表格检索验证著录项目
// 著录项目错误，则提示错误信息
function validateBib(objTxt) {
    var strVal = objTxt.value;
    var txtId = objTxt.lang.toUpperCase();

    setViewResult(" "); // 清空验证域
    // document.getElementById("ResultBlock").style.display = "none";  // 隐藏结果域
    if (strVal == "") return;

    var res = getValueAccordingToKey(txtId, strVal);
    if (res.indexOf("Error:") != -1) {
        setViewResult(res);
        return;
    }

    // setSearchFormula(getTableSearchQuery());
}

