 ///*
//------------- 中文分类
//一、	数字字母混合类
//1、	IPC（IC）
//■	带”/”： 
//规则：1位字母+2位数字+1位字母+0个或者多个空格+1到2位数字+”/”+0到2位数字
//处理：识别出来送引擎时候，处理的时候，中文空格需要将空格换成0；英文多个空格换成一个空格；反斜杠一起送引擎；
//例子：比如对于A01B 43/00，对于中文引擎，应该送A01B043/00/IC，对于英文引擎，应该送A01B 43/00/IC。
//■	不带”/”:
//规则： 1位字母+2位数字+1位字母+“0”+1到2位数字，至少3位。
//2、	优先权（PR）
//规则： 2位字母+2位数字+0到10位数字；至少4位。
//送所有汉字入口，最多20个汉字
//二、	纯数字类
//3、	公开日、公告日、申请日、申请号（AD,PD,GD,AN） 
//为6位和8位时，并且以19、20开头才可检索，否则不送引擎；超过8位自动截取
//4、	申请号（AN）
//只有达到4位已上并且以19、20开头才可检索，否则不送引擎；超过12位自动截取；
//5、	公告号（GN）：
//只有达到2位已上并且以1、2、3开头才可检索，否则不送引擎；超过9位自动截取；
//6、	公开号（PN）
//只有达到2位已上并且以1、2开头才可检索，否则不送引擎；超过9位自动截取；
//三、	其他类
//7、	发明人、名称、申请人、主权利要求、摘要（AB,CL,TI,IN,PA,）
//凡不属于以上类型的，都归入其它类入口

//------------- 英文分类
//一、	数字字母混合类
//1、	IPC（IC、EC）
//■	带”/”： 
//规则：1位字母+2位数字+1位字母+0个或者多个空格+1到2位数字+”/”+0到2位数字
//处理：识别出来送引擎时候，处理的时候，中文空格需要将空格换成0；英文多个空格换成一个空格；反斜杠一起送引擎；
//例子：比如对于A01B 43/00，对于中文引擎，应该送A01B043/00/IC，对于英文引擎，应该送A01B 43/00/IC。
//■	不带”/”:
//规则： 1位字母+2位数字+1位字母+“0”+1到2位数字，至少3位。
//2、	优先权（PR）
//规则： 2位字母+2位数字+0到10位数字；至少4位。
//送所有汉字入口，最多20个汉字
//二、	纯数字类
//3、	公开日、公告日、申请日、申请号（AD,PD, AN） 
//为6位和8位时，并且以19、20开头才可检索，否则不送引擎；超过8位自动截取
//4、	申请号（AN）
//只有达到4位已上并且以19、20开头才可检索，否则不送引擎；超过12位自动截取；
//5、	公开号（PN）
//只有达到2位已上并且以1、2开头才可检索，否则不送引擎；超过9位自动截取；
//三、	其他类
//6、	发明人、名称、申请人、摘要（AB,TI,IN,PA,）
//凡不属于以上类型的，都归入其它类入口

//------------- 忽略的检索入口
//中文专利以下字段不用检索： TX, CO, DZ, CT, AG,
//世界专利以下字段不用检索： CT,EC,

//------------- 处理步骤：
//1. 清除除”/”之外所有的特殊字符(clearSpecialChar)：仅保留数字、字母、汉字、空格和/， 对于连续的/，仅保留一个/； 
//2. 如果仅为1-2位数字或者英文字母，仅送AB入口(shortLen())；
//3. 切词： 按照空格切分输入字符串，生成检索项数组Words；
//   1、处理“/”(extractIpc)： 
//        a)	如果包含有ipc，则根据规则，摘取ipc，置入Words数组；
//        b)	如果不包含ipc，则去掉/；
//   2、 按照空格切词，置入Words数组；
//4. 针对单个检索项，生成检索式。
//    判断数组中每个检索项，判断类型(getType)：
//    a)	如果不属于其它类，则根据类型和检索项，生成检索式(generateQueryBySingleWord)； 
//    b)	如果属于其它类，则首先从字符串中进行依次摘取连续的数字字母混合串、数字串进行判断（按照PR、IPC、（AD,PD,GD,AN）、AN、GN、PN、（AB,CL,TI,IN,PA）的顺序），
//        然后根据分类及摘取的检索项生成检索式（生成检索式时，以+号连接摘取的各项）；
//5. 针对检索项数组，合成检索式：第4步生成的检索式按照*号连接(getAnalysisQuery)；
//6. 送检索；
//7. 根据检索结果，将各项全局变量还原（resetGlobalParams:errorTips ）；

//------------- 优化处理
//注意： 为了减轻引擎的压力，提供两个参数限制检索式长度：
//1、 经过步骤1处理后，长度最长限制为40个字符
//2、 检索项数组最长为3； 
//3、 针对单个检索项生成的检索式中+号最多连接8个子项。即，检索式总长最多24项。

//------------- 命名
//1. OriginQuery 用户输入的检索式
//2. Query: 清除特殊字符后的检索式
//3. ClearQuery: 检索式过长，切割后的检索式
//4. Words: 步骤4切词后生成的数组
//5. Word:  数组中的单项
//6. itemsOfWord: 单项摘取后中的子项数组
//7. itemOfWord: 单项摘取后中的子项
//*/

/*---------------------------------------------Global params begin---------------------------------------------*/
/*--------------性能参数--------------*/
var maxQueryLength = 20; // 30;
var maxWordsLength = 3;
var maxItemsOfWordLength = 8;
var maxOtherEntranceLength = 20;
/*--------------错误提示--------------*/
var errorTips = "";
var inputLengthExceedTips = "输入长度超过" + maxQueryLength + "个字词，后面字词已被忽略。";
var keywordsNumberExceedTips = "输入关键字个数超过" + maxWordsLength + "，后面的关键字已被忽略。";
var useTips = "请您在不同的检索关键字间加上空格";
/*--------------检索入口: 检索入口之间要以,号分隔--------------*/
// 中文各类检索入口，共6类
var cnEntrances = new Array(
    "IC",
    "PR",
    "AD,PD,GD,AN",
    "AN",
    "GN",
    "PN",
    "AB,CL,TI,IN,PA,AT,DZ"
);
// 英文各类检索入口
var wdEntrances = new Array(
    "IC",
    "PR,AN,PN",
    "AD,PD,AN",
    "AB,TI,IN,PA",
    "PN"
);

/*---------------------------------------------Global params end---------------------------------------------*/

function getClearQuery(type) {
    var strOriginalQuery = document.getElementById("searchContent").value.Trim();
    ///////////////////////实用动力（中国）工业有限公司未去（）
    if (strOriginalQuery == "" || strOriginalQuery == useTips) { return ""; }

    var strQuery = clearSpecialChar(strOriginalQuery, type);
    if (strQuery.GetLength() > maxQueryLength) { // 检索式超长
        errorTips += inputLengthExceedTips;
    }

    var strClearQuery = strQuery.substring(0, maxQueryLength);
    return strClearQuery;

}

function clearSpecialChar(strQuery, type) {
    var reg = null;
    if (type == "cn") {
        reg = /[^\u4e00-\u9fa5aa-zA-Z0-9\/\s\.（）]/g;

    } else if (type == "wd") {
        reg = /[^a-zA-Z0-9\/\s]/g;
    }
    if (reg == null) return strQuery;
    var _strQuery = strQuery.replace(reg, '');
    var reg2 = /(\/)+/g;
    _strQuery = _strQuery.replace(reg2, '\/');
     var reg3 = /[（）]+/gi;
     _strQuery= _strQuery.replace(reg3, '贾流洋');
     // alert(_strQuery+"333");
    return _strQuery;
}

function resetGlobalParams() {
    errorTips = "";
}

// 处理输入太短的情况
function shortLen(strQuery) {
    var reg = /(\/)+/g;
    strQuery = strQuery.replace(reg, "")
    var reg = /^\w{0,2}$/;
    if (reg.test(strQuery)) {
        return true;
    }
    return false;
}



// 将ipc/前的空格换成0，以便进行空格切分
function transeformIpc(strQuery) {
    var strLocalQuery = strQuery;
    var reg = /[a-zA-Z]\d{2}[a-zA-Z](\s*)(\d{1,2})\/\d{0,2}/g;
    var ipcRes;
    while ((ipcRes = reg.exec(strQuery)) != null) {
        var itemIpc = ipcRes[0];
        if (RegExp.$2.length == 1) itemIpc = ipcRes[0].replace(RegExp.$1, "00");  // “/”前为1位数字，将数字前的空格替换成为2个0
        if (RegExp.$2.length == 2) itemIpc = ipcRes[0].replace(RegExp.$1, "0");   // “/”前为2位数字，将数字前的空格替换成为1个0
        strLocalQuery = strLocalQuery.replace(ipcRes[0], itemIpc);
    }
    return strLocalQuery;
}

// 将ipc/前的空格换成0，以便进行空格切分
function transeformIpcBack(strQuery, type) {
    var strLocalQuery = strQuery;
    if (type == "wd") { // 世界专利检索时，中间空格要去掉
        var reg = /([a-zA-Z]\d{2}[a-zA-Z])(0+)(\d{1,2})\/\d{0,2}/g;
        var ipcRes;
        while ((ipcRes = reg.exec(strQuery)) != null) {
            var itemIpc = ipcRes[0];
            if (RegExp.$3.length == 1) itemIpc = ipcRes[0].replace(RegExp.$1 + RegExp.$2, RegExp.$1 + "  ");  // “/”前为1位数字，将数字前的空格替换成为2个空格
            if (RegExp.$3.length == 2) itemIpc = ipcRes[0].replace(RegExp.$1 + RegExp.$2, RegExp.$1 + " ");   // “/”前为2位数字，将数字前的空格替换成为1个空格
            strLocalQuery = strLocalQuery.replace(ipcRes[0], itemIpc);
        }
    }
    return strLocalQuery;
}

function getCutWords(strTranseformIpc) {
    // 根据空格切词
    var words = strTranseformIpc.split(/\s+/);
    return words;
}

function isIC(keyword) {
    var regIpc = /^[a-zA-Z]\d{2}[a-zA-Z](\d{3}\/\d{0,2}){0,1}$/g;
    if (regIpc.test(keyword)) return true;
    else return false;
}
// 规则： 2位字母+2位数字+0到10位数字；至少4位。
function isPR(keyword) {
    var regPR = /^[a-zA-Z]{2}\d{2}\d{0,10}$/g;
    if (regPR.test(keyword)) return true;
    else return false;
}
function isDate(keyword) {
    var reg = /^(\d{4})(\d{2})?(\d{2})?$/;
    if (reg.test(keyword)) {
        var intYear = parseInt(RegExp.$1, 10);
        var intMonth = parseInt(RegExp.$2, 10);
        var intDay = parseInt(RegExp.$3, 10);

        // 判断年份
        if (intYear < 1800 || intYear > 2500) return false;
        // 判断月份
        if (intMonth > 12 || intMonth < 1) return false;

        // 判断日期
        var arrayLookup = { '1': 31, '3': 31, '4': 30, '5': 31, '6': 30, '7': 31,
            '8': 31, '9': 30, '10': 31, '11': 30, '12': 31
        };

        if (arrayLookup[intMonth] != null) {
            if (intDay > arrayLookup[intMonth] || intDay <= 0) return false;
        }
        // 判断2月
        if (intMonth - 2 == 0) {
            var booLeapYear = (intYear % 4 == 0 && (intYear % 100 != 0 || intYear % 400 == 0));
            if (((booLeapYear && intDay > 29) || (!booLeapYear && intDay > 28)) || intDay <= 0)
                return false;
        }
        return true;

    }
    else { return false; }
}

//输入 ZL2010 2 0643383.1 或ZL201020643383.1 、CN201601818U检索为零。
//而这对初学者而言是很可能的输入方式，而不会删除”ZL”、“CN”和空格。
//单一入口项(AN/PN)
function analyFullSingle_Enter(keyword) {
    var itemsOfWord = new Array("", "", "", "", "", "");

    keyword = keyword.replace(" ", "");
    var regFullAn12 = /^(ZL|CN)((19|20)\d{2}[12389]\d{7})\.?[\d|X|x]?$/i;
    var regFullAn8 = /^(ZL|CN)([089]\d[12389]\d{5})\.?[\d|X|x]?$/i;
    var regFullPNGN = /^(CN)?((1|2|3|8|9)(\d{8}|\d{6}))[A-Z]?$/i;

    var rsReg;
    var arr = new Array();

    if (regFullAn12.test(keyword)) {
        rsReg = regFullAn12.exec(keyword);
        arr.push(rsReg[2]);
        itemsOfWord[3] = arr;
    } else if (regFullAn8.test(keyword)) {
        rsReg = regFullAn8.exec(keyword);
        arr.push(getApplyNo(rsReg[2]));
        itemsOfWord[3] = arr;
    }
    else if (regFullPNGN.test(keyword)) {
        rsReg = regFullPNGN.exec(keyword);
        arr.push(rsReg[2]);
        itemsOfWord[4] = arr;
        itemsOfWord[5] = arr;
    } else {
        itemsOfWord = null;
    }

    return itemsOfWord;
}


//单一入口项(PN)
function analyFullSingle_Enter_En(keyword) {
    var itemsOfWord = new Array("", "", "", "", "", "");
    keyword = keyword.replace(" ", "");
    var regFullPN = /^([a-z]{2}[0-9]{2,}[0-9A-Z]+[A-Z].?)$/i;

    var rsReg;
    var arr = new Array();

    if (regFullPN.test(keyword)) {
        rsReg = regFullPN.exec(keyword);
        arr.push(rsReg[1]);
        itemsOfWord[4] = arr;
    } else {
        itemsOfWord = null;
    }
    return itemsOfWord;
}


// 中文的三个数字入口
function isANGNPN(keyword) {

    var apNo = getApplyNo(keyword);
    var regFullAN = /^(19|20)[123]\d{9}\.?[\d|X|x]?$/;
    if (apNo != null && regFullAN.test(apNo)) {
        return false;
    }

    //var reg = /^\d{2,}(\.\d)$/;
    var reg = /^\d{4,}$/;
    if (reg.test(keyword)) {
        return true;
    }
    return false;
}

function isAN(keyword) {
    var apNo = getApplyNo(keyword);
    if (apNo == null) {
        return false;
    }
    else {
        return true;
    }
}

function isGN(keyword) {
    var reg = /^(1|2|3)\d\d{0,7}$/;
    if (reg.test(keyword)) { return true; }

    else { return false; }
}
function isPN(keyword) {
    var reg = /^(1|2)\d\d{0,7}$/;
    if (reg.test(keyword)) { return true; }
    else { return false; }
}
function isChinese(keyword) {
    var regChinese = /^[\u4e00-\u9fa5]{1,}$/;
    if (regChinese.test(keyword)) {
        return true;
    }
    return false;
}
// 英文的pr、an、pn三个入口采用同一个规则
function isPRANPN(keyword) {
    var reg = /^[A-Za-z]{2}\d{2,12}$/;
    if (reg.test(keyword)) {
        return true;
    }
    return false;
}
function isEnglish(keyword) {
    var regEnglish = /^[A-Za-z]{1,}$/;   // 纯英文入口
    if (regEnglish.test(keyword)) {
        return true;
    }
    return false;
}


function getIC(keyword) {
    var keywords = new Array();
    var reg = /[a-zA-Z]\d{2}[a-zA-Z](\d{3}\/\d{0,2}){0,1}/g;
    var res;
    while ((res = reg.exec(keyword)) != null) { keywords.push(res[0]); }
    if (keywords.length != 0) return keywords;
    else return null;
}

function getPR(keyword) {
    var keywords = new Array();
    var reg = /[a-zA-Z]{2}\d{2}\d{0,10}/g;
    var res;
    while ((res = reg.exec(keyword)) != null) { keywords.push(res[0]); }
    if (keywords.length != 0) return keywords;
    else return null;

}
// 英文的pr、an、pn三个入口采用同一个规则
function getPRANPN(keyword) {
    var keywords = new Array();
    var reg = /[A-Za-z]{2}\d{2,12}/g;
    var res;
    while ((res = reg.exec(keyword)) != null) { keywords.push(res[0]); }
    if (keywords.length != 0) return keywords;
    else return null;
}
function getDate(keyword) {
    var keywords = new Array();
    var reg = /(1|2)(\d{3})(\d{2})(\d{2})?/g;
    var res;
    while ((res = reg.exec(keyword)) != null) {
        if (isDate(res[0]))
            keywords.push(res[0]);
    }
    if (keywords.length != 0) return keywords;
    else return null;
}

function getANGNPN(keyword) {
    var keywords = new Array();
    var reg = /\d{2,12}/g;
    var res;
    while ((res = reg.exec(keyword)) != null) { keywords.push(res[0]); }
    if (keywords.length != 0) return keywords;
    else return null;
}




// 中文AN，验证并修改，不循环获取
function getAN(keyword) {
    var apNos = new Array();
    var reg = /\d{2,12}(\.\d)*/g;
    var res;
    //while ((res = reg.exec(keyword)) != null) { apNos.push(res[0]); }
    if ((res = reg.exec(keyword)) != null) { apNos.push(res[0]); }
    if (apNos.length == 0) return null;

    // 进一步获取
    var keywords = new Array();
    for (var i = 0; i < apNos.length; i++) {
        var _apNo = getApplyNo(apNos[i]);
        if (_apNo != null) {
            keywords.push(_apNo);
        }
    }

    if (keywords.length == 0) {
        return null;
    }
    else {
        return keywords;
    }
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
    var reg = /^\d{4,12}(\.[\d|X|x])*$/;
    var regFullAN = /^\d{12}\.?[\d|X|x]?$/;
    if (!regFullAN.test(_apNo)) {
        if (!reg.test(_apNo)) {
            return null;
        }
    }
    return _apNo;
}

// 中文GN，不循环获取
function getGN(keyword) {
    var keywords = new Array();
    var reg = /(1|2|3)\d\d{0,7}/g;
    var res;
    //while ((res = reg.exec(keyword)) != null) { keywords.push(res[0]); }
    if ((res = reg.exec(keyword)) != null) { keywords.push(res[0]); }
    if (keywords.length != 0) return keywords;
    else return null;
}
// 中文PN，不循环获取
function getPN(keyword) {
    var keywords = new Array();
    var reg = /(1|2)\d\d{0,7}/g;
    var res;
    //while ((res = reg.exec(keyword)) != null) { keywords.push(res[0]); }
    if ((res = reg.exec(keyword)) != null) { keywords.push(res[0]); }
    if (keywords.length != 0) return keywords;
    else return null;

}
function getChinese(keyword) {
    var keywords = new Array();
    var regChinese = /[\u4e00-\u9fa5]{1,}/g;
    var res;
    while ((res = regChinese.exec(keyword)) != null) { keywords.push(res[0]); }
    if (keywords.length != 0) return keywords;
    else return null;
}
function getOther(keyword) {
    var keywords = new Array();
    var regEnglish = /[0-9A-Za-z]{2,}/g;
    var res;
    while ((res = regEnglish.exec(keyword)) != null) { keywords.push(res[0]); }
    if (keywords.length != 0) return keywords;
    else return null;
}

// 将数组中的字符串从给定字符串中替换成空格
function keywordgetridofitems(keyword, items) {
    for (var i = 0; i < items.length; i++) {
        if (items[i] != null)
            keyword = keyword.replace(items[i], " ");
    }
    return keyword;
}



function gettype(keyword, type) {
    if (type == "cn") {
        if (isIC(keyword)) return 0;
        else if (isPR(keyword)) return 1;
        else if (isDate(keyword)) return 2;
        else if (isANGNPN(keyword)) return 3;
        else if (isPN(keyword)) return 3;
        else if (isGN(keyword)) return 3;
        else if (isAN(keyword)) return 4;
        else if (isChinese(keyword) || isEnglish(keyword)) return 6;
        else return -1;
    }
    else if (type == "wd") {
        if (isIC(keyword)) return 0;
        else if (isAN(keyword)) return 4;
        else if (isPRANPN(keyword)) return 1;
        else if (isDate(keyword)) return 2;
        else if (isEnglish(keyword)) return 3;
        else return -1;
    }
}

function extractItemsFromWord(keyword, srctype) {

    var itemsOfWord = new Array(); // 该数组的单个元素仍然还是数组

    if (srctype == "cn") {
        // 首先获取an，然后获取日期
        // 在数字匹配结束的地方再去掉an
        itemsOfWord[0] = getIC(keyword);
        if (itemsOfWord[0] != null) { keyword = keywordgetridofitems(keyword, itemsOfWord[0]); }

        itemsOfWord[1] = getPR(keyword);
        if (itemsOfWord[1] != null) { keyword = keywordgetridofitems(keyword, itemsOfWord[1]); }

        itemsOfWord[3] = getAN(keyword);

        itemsOfWord[2] = getDate(keyword);
        if (itemsOfWord[2] != null) { keyword = keywordgetridofitems(keyword, itemsOfWord[2]); }

        itemsOfWord[4] = getGN(keyword);
        if (itemsOfWord[4] != null) { keyword = keywordgetridofitems(keyword, itemsOfWord[4]); }
        itemsOfWord[5] = getPN(keyword);
        if (itemsOfWord[5] != null) { keyword = keywordgetridofitems(keyword, itemsOfWord[5]); }

        if (itemsOfWord[3] != null) { keyword = keywordgetridofitems(keyword, itemsOfWord[3]); }

        itemsOfWord[6] = getChinese(keyword);
        if (itemsOfWord[6] != null) { keyword = keywordgetridofitems(keyword, itemsOfWord[6]); itemsOfWord[6] = itemsOfWord[6].concat(getOther(keyword)); }
        else itemsOfWord[6] = getOther(keyword);
        if (itemsOfWord[6] != null) { keyword = keywordgetridofitems(keyword, itemsOfWord[6]); }


    } else if (srctype == "wd") {
        itemsOfWord[0] = getIC(keyword);
        if (itemsOfWord[0] != null) { keyword = keywordgetridofitems(keyword, itemsOfWord[0]); }

        itemsOfWord[1] = getPRANPN(keyword);
        if (itemsOfWord[1] != null) { keyword = keywordgetridofitems(keyword, itemsOfWord[1]); }

        itemsOfWord[2] = getDate(keyword);
        if (itemsOfWord[2] != null) { keyword = keywordgetridofitems(keyword, itemsOfWord[2]); }

        itemsOfWord[3] = getOther(keyword);
        if (itemsOfWord[3] != null) { keyword = keywordgetridofitems(keyword, itemsOfWord[3]); }
    }
    return itemsOfWord;
}

function reduceItemsOfWord(itemsOfWord) {
    var tempItemsOfWord = new Array();
    var length = 0;
    for (var i = 0; i < itemsOfWord.length; i++) {
        var items = itemsOfWord[i];
        length += items.length;

    }
}


function combileItemsAndEntrance(items, entrance, type) {
    var query = "";
    for (var i = 0; i < items.length; i++) {
        if (items[i] == "" || items[i] == null) continue;
        if (i == 0) {
            query = query + items[i] + "/" + entrance;
        }
        else {
            query = query + "+" + items[i] + "/" + entrance;
        }
    }
    return query;
}

//获取检索式
function analysisWord(keyword, srctype) {
    var itemsOfWord = null;   //  new Array("", "", "", "", "", "");
    var strWordQuery = "";

    if (srctype == "cn") {
        itemsOfWord = analyFullSingle_Enter(keyword);
    } else {
        itemsOfWord = analyFullSingle_Enter_En(keyword);
    }
    if (itemsOfWord == null) {
        itemsOfWord = new Array("", "", "", "", "", "");
        var type = gettype(keyword, srctype);
        if (type != -1) {  // 该检索项为某个纯类
            if (type == 3 && srctype == "cn") { // 对于中文的三个检索入口AN,GN,PN，需要进一步判断后加入数组
                // AN
                var arr3 = getAN(keyword);
                if (arr3 != null) {
                    itemsOfWord[3] = arr3;
                }
                // GN
                var arr4 = getGN(keyword);
                if (arr4 != null) {
                    itemsOfWord[4] = arr4;
                }
                // PN
                var arr5 = getPN(keyword);
                if (arr5 != null) {
                    itemsOfWord[5] = arr5;
                }
            } else if (type == 4) {//新增对申请号的精确检索
                var arr3 = getAN(keyword);
                if (arr3 != null) {
                    itemsOfWord[3] = arr3;
                }
            }
            else {
                var arr = new Array();
                arr.push(keyword);
                itemsOfWord[type] = arr;
            }

        }
        else {
            // 该检索项为混合类，需要进一步摘取
            itemsOfWord = extractItemsFromWord(keyword, srctype);
        }
    }


    var arrEntrace = srctype == "cn" ? cnEntrances : wdEntrances;
    for (var i = 0; i < arrEntrace.length; i++) {
        if (itemsOfWord[i] != null && itemsOfWord[i] != "") {
            // 切割检索入口
            var entrances = new Array();
            if (srctype == "cn") entrances = cnEntrances[i].split(",");
            else entrances = wdEntrances[i].split(",");
            // 合成检索式
            for (var j = 0; j < entrances.length; j++) {
                var itemQuery = combileItemsAndEntrance(itemsOfWord[i], entrances[j]);
                if (itemQuery == "") continue;
                if (strWordQuery == "")
                    strWordQuery = strWordQuery + "" + itemQuery + "";
                else
                    strWordQuery = strWordQuery + "+" + itemQuery + "";
            }
        }
    }

    // 检索式单项超长处理，保留检索项最长的项
    var items = strWordQuery.split("+");
    if (items.length > maxItemsOfWordLength) {
        var arrTemp = items.sort(sortLength);
        strWordQuery = arrTemp[0];
        for (var i = 1; i < maxItemsOfWordLength; i++) {
            strWordQuery = strWordQuery + "+" + arrTemp[i];
        }
    }

    // 将ipc转换回去
    strWordQuery = transeformIpcBack(strWordQuery, srctype);
    return strWordQuery;
}

function sortLength(a, b) {
    return b.length - a.length;
}

Array.prototype.delRepeat = function () {
    var newArray = [];
    var provisionalTable = {};
    for (var i = 0, item; (item = this[i]) != null; i++) {
        if (!provisionalTable[item]) {
            newArray.push(item);
            provisionalTable[item] = true;
        }
    }
    return newArray;
}

function getAnalysisQuery(strClearQuery, type) {
    // 转换带/的ipc前的空格为0
    var strTranseformIpc = transeformIpc(strClearQuery);
    // 切词
    var keywords = getCutWords(strTranseformIpc);
    // 去重
    keywords = keywords.delRepeat();
    // 检索关键字过多
    if (keywords.length > maxWordsLength) {
        errorTips += keywordsNumberExceedTips;
        _keywords = [];
        for (var i = 0; i < maxWordsLength; i++) { _keywords[i] = keywords[i]; }
        keywords = _keywords;
    }
    // 处理数组中的每个单项
    var wordQuery = "";
    for (var i = 0; i < keywords.length; i++) {
        var word = analysisWord(keywords[i], type);
        if (word != "" && word != null) {
            if (wordQuery == "") wordQuery = wordQuery + "(" + word + ")";
            else wordQuery = wordQuery + "*(" + word + ")";
        }
    }
    return wordQuery;
}

function zeroResultError(type) {
    var ti = (type == "cn") ? "CN000" : "WD000";
    alert("\u68c0\u7d22\u7ed3\u679c\u4e3a\u96f6\uff01");
    //CloseTab(ti); update:suihaitao
}

function cnSimplePatentSearch() {
    // 获取检索式
    var type = "cn";
    var strClearQuery = getClearQuery("cn");
    if (strClearQuery == "" || strClearQuery == "请您在不同的检索关键字之间加上空格") {
        alert("请输入检索条件");
        return;
    }
    var strFinalQuery = "F YY ";
    // 检索式长度判断

    if (shortLen(strClearQuery) == true) {
        strClearQuery = strClearQuery.replace(/(\/)+/g, "")
        if (strClearQuery == "") {
            zeroResultError(type);
            return;
        }
        strFinalQuery = strFinalQuery + "(" + strClearQuery + "/AB)";
    } else {
        // 分析检索式
        var analysisQuery = getAnalysisQuery(strClearQuery, "cn");
        if (analysisQuery != "" && analysisQuery != null) {
            strFinalQuery += getAnalysisQuery(strClearQuery, "cn");
        } else {
            zeroResultError(type);
            return;
        }
    }

    //alert(strFinalQuery);
    // 显示进度条
    $("#masklayer").css("display", "block");

    $.ajax({
        type: "POST",
        url: "/CPRS2010/cnTableSearch.aspx/tableSearch",
        data: "{'strSearchQuery':'" + encodeURIComponent(strFinalQuery) + "'}",
        timeout: 30000,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            // 撤销进度条
            $("#masklayer").css("display", "none");
            var msg = XMLHttpRequest.responseText;
            if (textStatus == "timeout") {
                zeroResultError(type);
            }

        },
        success: function (msg) {
            // 撤销进度条
            $("#masklayer").css("display", "none");
            strMsg = msg.d;
            if (strMsg.indexOf("<hit") != -1) {
                var num = ""; // 检索结果数
                var splitReg = /<hits:\s*(\d+)>/;
                if (splitReg.test(strMsg)) { num = RegExp.$1; }

                if (errorTips != "") { errorTips = "提示： 您" + errorTips; } // 错误提示

                var viewPage = "http://211.160.117.107/CPRS2010/cn/PatentGeneralList.html?";
                var url = viewPage + "No=999&kw=" + encodeURIComponent(strClearQuery) + "&Nm=" + num + "&errorTips=" + errorTips + "&Query=" + encodeURIComponent(strFinalQuery.substr(4));
                if (num == "0" || num == 0) {
                    zeroResultError(type);
                    return;
                }
                // 打开新页面
                //                addToTab(url, "CN000", true, true);
                //window.open(url); //update:suihaitao
                window.top.location.href = url;
            }
            else {
                // showError(strMsg);
                zeroResultError(type);
            }
        }
    });
    // 还原全局变量
    resetGlobalParams();
}

function wdSimplePatentSearch() {
    // 获取检索式
    var type = "wd";
    var strClearQuery = getClearQuery(type);
    if (strClearQuery == "" || strClearQuery == "请您在不同的检索关键字之间加上空格") {
        alert("请输入检索条件");
        return;
    }
    var strFinalQuery = "F YY ";
    // 检索式长度判断
    if (shortLen(strClearQuery) == true) {
        strClearQuery = strClearQuery.replace(/(\/)+/g, "")
        if (strClearQuery == "") {
            zeroResultError(type);
            return;
        }
        strFinalQuery = strFinalQuery + "(" + strClearQuery + "/AB)";
    } else {
        // 分析检索式
        strFinalQuery += getAnalysisQuery(strClearQuery, "wd");
    }

    //alert(strFinalQuery);
    // 显示进度条
    $("#masklayer").css("display", "block");

    $.ajax({
        type: "POST",
        url: "/CPRS2010/docdbTableSearch.aspx/tableSearch",
        data: "{'strSearchQuery':'" + encodeURIComponent(strFinalQuery) + "'}",
        timeout: 30000,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            // 撤销进度条
            $("#masklayer").css("display", "none");
            var msg = XMLHttpRequest.responseText;
            if (textStatus == "timeout") {
                zeroResultError(type);
            }

        },
        success: function (msg) {
            // 撤销进度条
            $("#masklayer").css("display", "none");
            strMsg = msg.d;
            if (strMsg.indexOf("<hit") != -1) {
                var num = ""; // 检索结果数
                var splitReg = /<hits:\s*(\d+)>/;
                if (splitReg.test(strMsg)) { num = RegExp.$1; }

                if (errorTips != "") { errorTips = "提示： 您" + errorTips; } // 错误提示

                var viewPage = "http://211.160.117.107/CPRS2010/docdb/docdbGeneralLists.html?";
                var url = viewPage + "No=999&kw=" + encodeURIComponent(strClearQuery) + "&Nm=" + num + "&errorTips=" + errorTips + "&Query=" + encodeURIComponent(strFinalQuery.substr(4));
                if (num == "0" || num == 0) {
                    zeroResultError(type);
                    return;
                }
                // 打开新页面
                //                addToTab(url, "WD000", true,true);
                window.top.location.href = url;
                //window.open(url);
            }
            else {
                // showError(strMsg);
                zeroResultError(type);
            }
        }
    });
    // 还原全局变量
    resetGlobalParams();
}

// 用户在文本框输入时，将enter
document.onkeydown = function enterToTab(event) {
    event = event || window.event;
    if (event.keyCode == 13) {
        //alert("enter");
        var obj = document.activeElement;
        //alert(obj.id);
        if (obj.id == 'searchContent') {
            event.keyCode = 9;
            //document.getElementById('BtnSearch').onclick();
            document.getElementById('BtnSearch').click();
        }
    }
}
