//获得收费规则列表
function ShowCostRules(serviceCode, serviceName) {
    if (serviceCode === "" || serviceName === "")
        return;
    //$.unfunkyUI();
    var wsUser = new Patentquery.My.UserService;
    var successCallbackObj = new Object();
    successCallbackObj._control = this;
    successCallbackObj.CallbackMethod = function (result, textContent, methodName) {
        if (result.CauseException) {
            customAlert("获取数据信息出现异常");
        }
        else {
            var htmlTable = result.ResultObjs[0];
            if (htmlTable === "") {
                customAlert("未取得相应数据信息");
            }
            else {
                var html = "<div style=\" padding: 10px 10px 10px 10px;line-height:20px;width:600px;overflow-y:auto;\">" +
                            "<p>" + htmlTable + "</p>" +
                            "<p style=\"text-align:center;\">" +
                            "</p><span id=\"selectRule\" style=\"display: none\"></span>" +
                            "</div>";
                
                art.dialog({
                    title: serviceName,
                    content: html,
                    id: 'hyfy2',
                    padding: '0px',
                    lock:true,
                    button: [
                        {
                            value: '定制',
                            callback: function () {
                                okEvent(serviceName);
                                return false;
                            }
                        },
                        {
                            value: '取消'
                        }
                    ]
                });
            }
        }
    };
    var failedCallback = new Object();
    failedCallback.CallbackMethod = function(error, textContent, methodName) {
        customAlert(methodName + error.get_message());
    };
    wsUser.GetRuleInfoByID(serviceCode, serviceName, Function.createDelegate(successCallbackObj, successCallbackObj.CallbackMethod), Function.createDelegate(failedCallback, failedCallback.CallbackMethod));
    successCallbackObj = null;
    failedCallback = null;
}
//定制窗口
function SetCustomeServiceVisible(serviceName, serviceCode, count, obj, ruleCode) {
    if (serviceCode == "") {
        alert("系统暂时无此服务.");
        return;
    }
    var msg = "<div >您暂时未定制" + serviceName + "服务.<br />";
    art.dialog({
        title: serviceName,
        content: msg,
        id: 'hyfy',
        padding: '0px',
        lock: true,
        button: [
        {
            value: '定制服务',
            callback: function () {
                ShowCostRules(serviceCode, serviceName);
            }
        },
        {
            value: '使用积分',
            callback: function () {
                UseFunctionByNumber(serviceCode, serviceName, count, obj, ruleCode);
            }
        },
        {
            value: '关闭'
        }
    ]

    });
}

function UseFunctionByNumber(functionCode, serviceName, count, obj, ruleCode) {
    var wsUser = new Patentquery.My.UserService;
    var successCallbackObj = new Object();
    successCallbackObj._control = this;
    successCallbackObj.CallbackMethod = function (result, textContent, methodName) {
        if (result.CauseException) {
            alert("获取数据信息出现异常");
        }
        else {
            var returnValue = result.ResultObjs[0];
            switch (returnValue) {
                case 0: //用户未登录
                    alert(result.Message);
                    break;
                case 1: //有余额并可用
                    var accountMoney = result.ResultObjs[1]; //账户余额
                    var serviceMoney = result.ResultObjs[2]; //本次支付
                    if (serviceName.indexOf("翻译") !== -1)
                        serviceMoney = Math.ceil(serviceMoney / 10);
                    showEnoughScoreWnd(accountMoney, serviceMoney, obj, functionCode, serviceName, ruleCode);
                    break;
                case 2: //有余额，但不够支付一次
                    var accountMoney = result.ResultObjs[1]; //账户余额
                    var serviceMoney = result.ResultObjs[2]; //本次支付
                    //serviceMoney = Math.ceil(serviceMoney / 10);
                    showNotEnoughScoreWnd(accountMoney, serviceMoney, serviceName, functionCode);

                    break;
                case 3: //无余额
                    alert(result.Message);
                    break;
            }
        }
    };
    var failedCallback = new Object();
    failedCallback.CallbackMethod = function (error, textContent, methodName) {
        customAlert("UseFunctionByNumber:" + error.get_message());
    };
    wsUser.GetUserMoney(functionCode, count, serviceName, Function.createDelegate(successCallbackObj, successCallbackObj.CallbackMethod), Function.createDelegate(failedCallback, failedCallback.CallbackMethod));
    successCallbackObj = null;
    failedCallback = null;
}

//更新使用次数
function UpdateUseTimes(ruleCode, useTimes, functionCode, type) {
    var wsUser = new Patentquery.My.UserService;
    var successCallbackObj = new Object();
    successCallbackObj._control = this;
    successCallbackObj.CallbackMethod = function (result, textContent, methodName) {
        if (result.CauseException) {
            alert(result.Message);
        }
        else {
            //caneclEvent();
        }
    };
    var failedCallback = new Object();
    failedCallback.CallbackMethod = function (error, textContent, methodName) {
        alert(methodName + error.get_message());
    };
    wsUser.UpdateServiceUseTimes(ruleCode, useTimes, functionCode, type, Function.createDelegate(successCallbackObj, successCallbackObj.CallbackMethod), Function.createDelegate(failedCallback, failedCallback.CallbackMethod));
    successCallbackObj = null;
    failedCallback = null;
}

function okEvent(serviceName) {
    serviceName = decodeURIComponent(serviceName);
    var hint = $("#divHint");
    if (!!hint) {
        hint.html("");
    }
    var obj = $("#selectRule");
    var ruleCode = "";
    if (!!obj)
        ruleCode = obj.val();
    //alert(ruleCode + serviceName);
    if (ruleCode === "") {
        customAlert("请选择要定制的功能");
    }
    if (serviceName != "" && (ruleCode == null || ruleCode != ""))
        updateSelectRules(serviceName, ruleCode);
}
function updateSelectRules(functionName, ruleCode) {
    var wsUser = new Patentquery.My.UserService;
    var successCallbackObj = new Object();
    successCallbackObj._control = this;
    successCallbackObj.CallbackMethod = function (result, textContent, methodName) {
        if (result.CauseException) {
            //customAlert("服务定制失败");
        }
        else {
            if (result.ResultObjs != null && result.ResultObjs.length > 0) {
                //var fn = encodeURIComponent(result.ResultObjs[0]);
                var fn = encodeURIComponent(functionName);
                var lt = result.ResultObjs[1];
                var ft = result.ResultObjs[2];
                var my = result.ResultObjs[3];
                var fc = result.ResultObjs[4];
                var rc = result.ResultObjs[5];
                var url = "../NewPatentInterface/AlipayIndex.aspx?fn=" + fn + "&limit=" + lt + "&ft=" + ft + "&my=" + my + "&fc=" + fc + "&rc=" + rc + "";
                window.open(url, "支付页面");
                //                var html = result.ResultObjs[0];
                //                setTimeout(function() {
                //                    document.write(html);
                //                    document.close();
                //                    //window.open(html, "定制" + functionName);
                //                }, 10);
//                $.unfunkyUI();
//                $.unfunkyUI();
//                //alert("服务定制成功");
//                CloseRulesTable();
            }
            else {
                //customAlert(result.Message);
            }
        }
    };
    var failedCallback = new Object();
    failedCallback.CallbackMethod = function (error, textContent, methodName) {
        customAlert("SetSelectRules:" + error.get_message());
    };
    wsUser.UpdateSelectCostRules(functionName, ruleCode, Function.createDelegate(successCallbackObj, successCallbackObj.CallbackMethod), Function.createDelegate(failedCallback, failedCallback.CallbackMethod));
    successCallbackObj = null;
    failedCallback = null;
}

var oldRow = "";
var oldColor = "";
function SetSelectRules(ruleCode, obj, type) {
    if (!!obj) {
        ResetInputRadioButton();
        obj.checked = "checked";
    }
    var obj = $("#selectRule");
    if (!!obj)
        obj.val(ruleCode);
    if (!!oldRow && oldRow != "") {
        var oldObj = $("#" + oldRow);
        if (!!oldObj && oldColor != "")
            oldObj.css("background-color", oldColor);
    }
    var trObj = $("#" + ruleCode);
    oldRow = ruleCode;
    if (!!trObj) {
        oldColor = trObj.css("background-color");
        trObj.css("background-color", "#FFD700");
    }
}

function ResetInputRadioButton() {
    $(":input[type=radio]").each(function () {
        this.checked = "";
    })
}