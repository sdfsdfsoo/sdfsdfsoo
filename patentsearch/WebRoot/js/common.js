document.write("<script src='../js/StrComm.js'></script>");

// 获取逻辑运算符
function getLogicSymbolFromTableSearchPage() {
    var objLogicSymbol = document.getElementById("SlctLogicSymbol");
    return objLogicSymbol.options[objLogicSymbol.selectedIndex].value;
}


// 显示范例
function seashowtip(tips, flag, iwidth, eventTag) {
    var my_tips = document.getElementById("mytips");
    if (my_tips) {
        if (flag) {
            var myEvent = eventTag || window.event;
            my_tips.innerHTML = tips;
            my_tips.style.display = "";
            my_tips.style.width = iwidth;
            my_tips.style.left = myEvent.clientX + 10 + document.documentElement.scrollLeft + "px";
            my_tips.style.top = myEvent.clientY + 10 + document.documentElement.scrollTop + "px";
        }
        else {
            my_tips.style.display = "none";
        }
    }
}

// 显示范例
function showtips(tips, flag, iwidth, eventTag, isLeft) {
    var my_tips = document.getElementById("mytips");
    if (my_tips) {
        if (flag) {
            var myEvent = eventTag || window.event;
            my_tips.innerHTML = tips;
            my_tips.style.display = "";
            my_tips.style.width = iwidth;
            if (isLeft) {
                my_tips.style.left = myEvent.clientX + 10 + document.body.scrollLeft + "px";
                my_tips.style.top = myEvent.clientY + 10 + document.body.scrollTop + "px";
            }
            else {
                my_tips.style.left = myEvent.clientX + 10 + document.body.scrollLeft - 440 + "px";
                my_tips.style.top = myEvent.clientY + 15 + document.body.scrollTop + "px";
            }
        }
        else {
            my_tips.style.display = "none";
        }
    }
}
// 设置检索式
function setSearchFormula(strFormula) {
    document.getElementById("TxtSearch").value = strFormula;
}
// 设置校验结果
function setViewResult(strResult) {
    if (strResult === "") {
        return;
    }
    document.getElementById("LabValidationResult").innerHTML = strResult;

}
//function: 修改用户输入键的默认模式
//document.onkeydown = function enterToTab()
//{
//    if(event.keyCode == 13)
//        event.keyCode = 9;
//}

//隐藏结果域
function clearResult() {
    document.getElementById("ResultBlock").style.display = "none";
}

function change() {
    var obj = document.getElementById("ResultBlock");

    var display = obj.style.display;

    if (display == "none")
        obj.style.display = "block";
    else
        obj.style.display = "none";

}


/////////////////////////////TsingLand///////////////////////////

//文本框接收回车、提交指定按钮
function submitKeyClick(button) {
    if (event.keyCode == 13) {
        event.keyCode = 9;
        event.returnValue = false;
        document.all[button].click();
    }
}

//全选
function checkAll() {
    var obj = arguments[0];
    if (obj.checked) {
        $("input[type='checkbox']").attr("checked", true);
    } else {
        $("input[type='checkbox']").attr("checked", false);
    }
}

//专利类型切换
var patentType = "cn";
function switchPatentType() {
    var obj = arguments[0];
    if (patentType == "cn" && obj.id == "btnPatentEn") {
        $("#btnPatentCn").attr("class", "btnPatentCnOff");
        $("#btnPatentEn").attr("class", "btnPatentEn");
        patentType = "en";
    } else if (patentType == "en" && obj.id == "btnPatentCn") {
        $("#btnPatentCn").attr("class", "btnPatentCn");
        $("#btnPatentEn").attr("class", "btnPatentEnOff");
        patentType = "cn";
    }
}

//获取Url参数
function requestUrl(paras) {
    var url = location.href;
    var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
    var paraObj = {}
    for (i = 0; j = paraString[i]; i++) {
        paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
    }
    var returnValue = paraObj[paras.toLowerCase()];
    if (typeof (returnValue) == "undefined") {
        return "";
    } else {
        return returnValue;
    }
}

function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}


//关闭同类对话框
function closeSimilar() {
    var url = location.href;
    var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
    var paraObj = {}
    for (i = 0; j = paraString[i]; i++) {
        paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
    }
    var returnValue = paraObj[paras.toLowerCase()];
    if (typeof (returnValue) == "undefined") {
        return "";
    } else {
        return returnValue;
    }
}

function HTMLEncode(html) {
    html = html.replace(/\'/g, "’");
    var temp = document.createElement("div");
    (temp.textContent != null) ? (temp.textContent = html) : (temp.innerText = html);
    var output = temp.innerHTML;
    temp = null;
    return output;
}


//高亮检索式内容
function getGLKws(strQuery) {
    var strRs = "";
    try {
        var strQy = decodeURIComponent(strQuery);
        var RegLxCo = new RegExp("@(LX|CO)=.*", "gi");
        var FlgReg = new RegExp("/[a-z|A-Z]{2}", "g");
        var Freg = new RegExp("F [a-z|A-Z]{2} ", "g"); //new RegExp("(F ){0,1}[a-z|A-Z]{2}", "g");
        var FKhReg = new RegExp("[(|)|$]", "g");
        var FhReg = new RegExp("[+|*|-]{1}", "g");

        strQy = strQy.replace(RegLxCo,"").replace(FlgReg, "").replace(Freg, "").replace(FKhReg, "").replace(FhReg, "+");
        var strArryQy = strQy.split("+");

        for (var nId = 0; nId < strArryQy.length; nId++) {
            strRs += strArryQy[nId].Trim() + ";";
            //btnGl_onclick(strArryQy[nId].Trim());
        }

        if (strRs.length > 0) {
            strRs = strRs.substring(0, strRs.length - 1);
        }
    } catch (e) {
    }
    return strRs;
}