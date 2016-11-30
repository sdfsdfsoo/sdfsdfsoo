//--------------------------------------表格检索-------------------------------------------//
//return
//1、 ajax页面超时。在ajax请求字段中设置"timeout: 15000,"，在返回的error中处理超时错误；
//2、 SocketSearch返回错误：1、 请求已经超时!；2、检索时发生错误!
//3、 Main主机返回的超时信息

//检索式最大长度
var nEnTbQueryMaxLen = 2000;  //255
// 生成检索式
function docdbGenerateSearch() {
    setViewResult(" "); // 清空验证域
    var strQuery = getTableSearchQueryEN().Trim();
    if (strQuery == "") {
        showError("请输入要检索的内容！");
        //setSearchFormula("");
        return;
    }

    var res = validateLogicSearchQuery(strQuery);
    var obj = document.getElementById("TxtSearch");
    if (res.substring(0, 5) == "Error")
        showError(res);
    else {
       
        obj.value = res;
    }
    //obj.style.background-color.value = '#FFFFFF';
    //obj.style.background-color.value = '#0066FF';


}
// 清空所有text框架
function docdbClearSearch() {
    //    $("input[@type=text]").attr("value", '');
    //    var objArr = $("input[@type=text]");
    //    for(var item in objArr)
    //    {
    //       objArr[item].value = "";
    //    }
    for (var i = 1; i <= EN_nTetCount; i++) {
        var obj = document.getElementById("TxtEn" + i);
        if (obj != null) obj.value = "";
    }
    var country = document.getElementsByName("checkbox")
    for (i = 0; i < country.length; i++) {
        country[i].checked = false;
    }
    document.getElementById("TxtSearch").value = "";


}
// 从表格检索页面获取检索式。检索式包括两种格式： 1, F 计算机 TI; 2, F XX (计算机/TI)*(2008/PD)，分别为简单检索和复杂检索。
// 检索式由两部分构成，检索头(F or F XX)和检索体(计算机/TI)*(2008/PD)。
var EN_nTetCount = 13;  //文本框数量

function getTableSearchQueryEN() {
    var arrKey = new Array();       // 保存检索入口字段，如“TI”
    var arrValue = new Array();  // 保存检索入口内容，如“计算机”
    var objTxb; //文本框对象
    var strEntFlag = m_strEntFlag; //检索式中入口及检索内容间的连接符(2005/AN)
    var strLogicSymbol = getLogicSymbolFromTableSearchPage(); // 检索入口之间的逻辑运算符；
    var aN = true;
    var pN = true;

    var res = "";   // 保存表格检索式
    // 遍历检索入口id，获得对应的值，加入数组
    for (var i = 1; i <= EN_nTetCount; i++) {
        var obj = document.getElementById("TxtEn" + i);
        if (obj == null)
            continue;
        var strValue = obj.value.Trim();
        var strKey = obj.lang.toUpperCase();

        if (strKey == "PN") {
            var reg = /^[A-Za-z]{2}/;
            if (reg.test(strValue)) {
                pN = false;
            }
        }
        if (strKey == "AN") {
            var reg = /^[A-Za-z]{2}/;
            if (reg.test(strValue)) {
                aN = false;
            }
        }

        // 给检索入口内容后缀上/key字符
        if (strValue != "") {

            strValue = strValue.replace(/\（/g, "(");
            strValue = strValue.replace(/\）/g, ")");

            strValue = strValue.replace(/\(/g, " ");
            strValue = strValue.replace(/\)/g, " ");

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
var EN_Entrances = "AN,AD,PN,PD,IC,PR,IN,PA,TI,AB,CT,EC,MC,ST,"; //所有入口,以，连接多个
function validateLogicSearchQueryEN(strSearchQuery) {
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
            var res = getValueAccordingToKeyEN(key, value);
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

                var res = getValueAccordingToKeyEN(key, value); // 验证并修改检索式 
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
    var res = strQueryHead.Trim() + " " + strQueryBody.replace("'", " ");    
    return res;

}

// 测试是否为合法的检索入口，测试对应的检索入口内容是否符合规范
// 格式错误，返回"Error:"开头的字符串字符串，如果正确，则返回修改后的strVal
// // value 是可以嵌套括号的，因此验证的时候需要去掉括号和空白
// var EN_Entrances = "AN,AD,PN,PD,IC,PR,IN,PA,TI,AB,CT,EC,";
function getValueAccordingToKeyEN(key, value) {
    var entrances = EN_Entrances;
    if (entrances.indexOf(key + ",") == -1) {
        return "Error:非法检索入口!";
    }
    var txtId = key;
    var strVal = value.split(/[\-|+|*]/);    // 逻辑链接符
    var linkArr = value.match(/[\-|+|*]/g);    // 逻辑链接符


    // strRes = "Error:禁止包含非法字符:/,not,and or!";
    var maxInputLength = 200;    // 每个检索入口有一个最大输入字符数目
    for (var i = 0; i < strVal.length; i++) {
        if (strVal[i].length > maxInputLength) {
            var resTemp = "Error:检索入口字符串长度不能超过";
            resTemp += maxInputLength + "个字符！";
            return resTemp;
        }
        var regExp1 = /\s+and\s+/g;
        var regExp2 = /\s+not\s+/g;
        var regExp3 = /\s+or\s+/g;
        if (regExp1.test(strVal[i]) || regExp2.test(strVal[i]) || regExp3.test(strVal[i])) {
            return "Error:禁止包含非法字符:not,and or!";
        }
    }

    if (txtId == "AB" || txtId == "CL" || txtId == "TI") {
        //for(var i = 0; i<strVal.length; i++) {
        //}

    }

    //strRes = "Error:申请号必须为数字，且长度为4-12位!";
    if (txtId == "CT" || txtId == "PR") {

        for (var i = 0; i < strVal.length; i++) {
            var valueTemp = strVal[i].Trim();
            valueTemp = valueTemp.TrimLink();
            var reg = /^[A-Za-z]{2}\w{0,14}$/;
            if (!reg.test(valueTemp)) {
                return "Error:优先权号或引用文献必须以两位国别代码开头，紧跟着0-14位字母或者数字!";
            }
        }
    }
    //strRes = "Error:申请号必须为数字，且长度为4-12位!";
    if (txtId == "AN" || txtId == "PN") {
        var guobie = GetCountrys();
        if (guobie.length == 2) {
            if (guobie[0] != "ELSE") {
                for (var i = 0; i < strVal.length; i++) {
                    var valueTemp = strVal[i].Trim();
                    valueTemp = valueTemp.TrimLink();
                    var reg = /^[A-Za-z]{2}/;
                    if (reg.test(valueTemp)) {
                        if (valueTemp.substring(0, 2).toUpperCase() != guobie[0]) {
                            return "Error:您选择的国家与输入申请号或公开号的国别不一致!";
                        }
                    }
                }
            }
            else {
                for (var i = 0; i < strVal.length; i++) {
                    var valueTemp = strVal[i].Trim();
                    valueTemp = valueTemp.TrimLink();
                    var reg = /^[A-Za-z]{2}\w{0,14}$/;
                    if (!reg.test(valueTemp)) {
                        return "Error:申请号或公开号必须以两位国别代码开头，紧跟着0-14位字母或者数字!";
                    }
                }
            }
        }
        else {
            for (var i = 0; i < strVal.length; i++) {
                var valueTemp = strVal[i].Trim();
                valueTemp = valueTemp.TrimLink();
                var reg = /^[A-Za-z]{2}\w{0,14}$/;
                if (!reg.test(valueTemp)) {
                    return "Error:申请号或公开号必须以两位国别代码开头，紧跟着0-14位字母或者数字!";
                }
            }
        }

    }
    if (txtId == "AD" || txtId == "PD") {
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



    //strRes = "Error:ipc 输入不合法"
    if (txtId == "IC") {
        //        for(var i = 0; i<strVal.length; i++) {
        //            var valueTemp = strVal[i].Trim();
        //            valueTemp = valueTemp.TrimLink();
        //            
        //            var strValTemp = valueTemp;
        //            if(strValTemp.indexOf('/') == -1){
        //               continue;
        //            }  
        //              
        //            var arr = new Array();
        //            arr = strValTemp.split(/[\/]/);
        //            if (arr.length > 2)
        //                return "Error:IC入口中只能包含一个/";
        //            var head = arr[0].Trim();
        //            var end = arr[1].Trim();
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
        //            if (end.length > 4)
        //                return "Error:IC /后必须少于4位代码";
        //            
        //            // 去掉/，替换头和尾
        //            // value = value.replace(strValTemp,head+end);    
        //                            
        //        }
    }
    return value;
}
//获取选择的国别
function GetCountrys() {
    var str = "";
    $("input[name='checkbox']:checkbox").each(function () {
        if ($(this).attr("checked")) {
            str += $(this).val() + ","
        }
    })
    var countrys = str.split(",");
    return countrys;
}
//获取未选择的国别
function GetNotCountrys() {
    var str = "";
    $("input[name='checkbox']:checkbox").each(function () {
        if (!$(this).attr("checked")) {
            str += $(this).val() + ","
        }
    })
    var countrys = str.split(",");
    return countrys;
}
// 表格检索验证著录项目
// 著录项目错误，则提示错误信息
function validateBib(objTxt) {
    var strVal = objTxt.value;
    var txtId = objTxt.lang.toUpperCase();

    setViewResult(" "); // 清空验证域
    document.getElementById("ResultBlock").style.display = "none";  // 隐藏结果域
    if (strVal == "") return;

    var res = getValueAccordingToKeyEN(txtId, strVal);
    if (res.indexOf("Error:") != -1) {
        setViewResult(res);
        return;
    }

    // setSearchFormula(getTableSearchQueryEN());
}




function ReGenerateSearchEN(strSearchQuery) {
    var strQuery = strSearchQuery.Trim().ReduceSpace();

    if (strQuery == "")
        return "";
    var strQueryHead = "";
    var strQueryBody = "";
    strQuery = strQuery.replace(/（/g, "(");
    strQuery = strQuery.replace(/）/g, ")");
    // 切割检索式
    var splitReg = /^\s*([J|F|j|f]\s*[XX]*)(.*)$/;
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
            var res = getValueAccordingToKeyEN(key, value);
            if (res.substring(0, 5) == "Error") {
                return res;
            }
            else {
                strQueryBody = key + " " + res;
                $('div#endivQueryTable input[lang="' + key + '"]').each(function () {
                    $(this).val(res);
                });
            }
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
               
                var res = getValueAccordingToKeyEN(key, value); // 验证并修改检索式 
                if (res.substring(0, 5) == "Error") {
                    return res;
                }
                else {

                    var zhuanyiValue = value.replace(/\+/g, "\\+");
                    zhuanyiValue = zhuanyiValue.replace(/\-/g, "\\-");
                    zhuanyiValue = zhuanyiValue.replace(/\*/g, "\\*");
                    zhuanyiValue = zhuanyiValue.replace(/\(/g, "\\(");
                    zhuanyiValue = zhuanyiValue.replace(/\)/g, "\\)");
                    //
                    $('div#endivQueryTable input[lang="' + oldKey + '"]').each(function () {
                        $(this).val(zhuanyiValue);
                    });
                    var strReg = zhuanyiValue + "\\s*\\\/\\s*" + oldKey;
                    var reg = new RegExp(strReg, "gi");
                    strQueryBodyTemp = strQueryBodyTemp.replace(reg, res + "/" + key);
                }

            }
            strQueryBody = strQueryBodyTemp.Trim();

        }
    }

    return strQueryHead.Trim() + " " + strQueryBody.replace("'", " ");

}

