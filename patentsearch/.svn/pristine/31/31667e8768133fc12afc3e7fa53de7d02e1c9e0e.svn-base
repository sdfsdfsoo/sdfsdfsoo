var cnexportdghtml = '<div id="showExportCFG" style="width: 480px; display: none;">' +
        '<div id="ExportCFG" style="min-height:100px;">' +
            '<ul>' +
                '<li><label><input type="checkbox" id="chkAppNo" value="申请号" disabled="disabled" checked="checked" />申请号</label></li>' +
                '<li><label><input type="checkbox" id="chkPubNo" value="公开（公告）号" />公开（公告）号</label></li>' +
                '<li><label><input type="checkbox" id="chkIPC" value="分类号" />分类号</label></li>' +
                '<li><label><input type="checkbox" id="chkMIPC" value="主分类号" />主分类号</label></li>' +
                '<li><label><input type="checkbox" id="chkTitle" value="名称" disabled="disabled" checked="checked" />名称</label></li>' +
                '<li><label><input type="checkbox" id="chkPubDate" value="公开（公告）日" />公开（公告）日</label></li>' +
                '<li><label><input type="checkbox" id="chkAppDate" value="申请日" />申请日</label></li>' +
                '<li><label><input type="checkbox" id="chkAT" value="代理人" />代理人</label></li>' +
                '<li><label><input type="checkbox" id="chkAG" value="专利代理机构" />专利代理机构</label></li>' +
                '<li><label><input type="checkbox" id="chkabs" value="摘要" />摘要</label></li>' +
                '<li><label><input type="checkbox" id="chkCL" value="主权项" />主权项</label></li>' +
                '<li><label><input type="checkbox" id="chkCY" value="国省代码" />国省代码</label></li>' +
                '<li><label><input type="checkbox" id="chkPA" value="申请（专利权）人" />申请（专利权）人</label></li>' +
                '<li><label><input type="checkbox" id="chkIN" value="发明（设计）人" />发明（设计）人</label></li>' +
                '<li><label><input type="checkbox" id="chkPR" value="优先权" />优先权</label></li>' +
                '<li><label><input type="checkbox" id="chkdz" value="地址" />地址</label></li>' +
                '<li><label><input type="checkbox" id="chkflzt" value="法律状态" />法律状态</label></li>' +
            '</ul>' +
        '</div>' +
    '</div>';
var cnexportdghtml1 = '<div id="showExportCFG" style="width: 480px; display: none;">' +
        '<div id="ExportCFG" style="min-height:100px;">' +
//            '<ul>' +
//               '<li><label><input type="checkbox" id="chkAppNo" value="申请号" disabled="disabled" checked="checked" />申请号</label></li>' +
//                '<li><label><input type="checkbox" id="chkPubNo" value="公开（公告）号" />公开（公告）号</label></li>' +
//                '<li><label><input type="checkbox" id="chkIPC" value="分类号" />分类号</label></li>' +
//                '<li><label><input type="checkbox" id="chkMIPC" value="主分类号" />主分类号</label></li>' +
//                '<li><label><input type="checkbox" id="chkTitle" value="名称" disabled="disabled" checked="checked" />名称</label></li>' +
//                '<li><label><input type="checkbox" id="chkPubDate" value="公开（公告）日" />公开（公告）日</label></li>' +
//                '<li><label><input type="checkbox" id="chkAppDate" value="申请日" />申请日</label></li>' +
//                '<li><label><input type="checkbox" id="chkAT" value="代理人" />代理人</label></li>' +
//                '<li><label><input type="checkbox" id="chkAG" value="专利代理机构" />专利代理机构</label></li>' +
//                '<li><label><input type="checkbox" id="chkabs" value="摘要" />摘要</label></li>' +
//                '<li><label><input type="checkbox" id="chkCL" value="主权项" />主权项</label></li>' +
//                '<li><label><input type="checkbox" id="chkCY" value="国省代码" />国省代码</label></li>' +
//                '<li><label><input type="checkbox" id="chkPA" value="申请（专利权）人" />申请（专利权）人</label></li>' +
//                '<li><label><input type="checkbox" id="chkIN" value="发明（设计）人" />发明（设计）人</label></li>' +
//                '<li><label><input type="checkbox" id="chkPR" value="优先权" />优先权</label></li>' +
//                '<li><label><input type="checkbox" id="chkdz" value="地址" />地址</label></li>' +
//                '<li><label><input type="checkbox" id="chkflzt" value="法律状态" />法律状态</label></li>' +
//            '</ul>' +
            '<div style="clear:both"/><ul style="width:400px;"><li style="width:100px;float:left;"><label><input type="radio" name="mycheck" id="chkflzt" value="0" />导出范围：</label></li><li style="width:300px;"><input  class="easyui-numberspinner"  type="text" id="startnum" />--<input  class="easyui-numberspinner"  type="text" id="endnum" /></li></ul>' +
            '<ul style="width:400px;"><li style="width:400px;"><label><input type="radio" name="mycheck" id="chklist" value="1" />导出所选结果</label></li></ul>' +
        '</div>' +
    '</div>';
var check = "";
var cpiclst = "";
function Exports(serviceName)
{
    var exportdghtml = cnexportdghtml1;
    var intmax = $('#pagetop').pagination('options').total;
    var intval;
    if (intmax > 3000) {
        intval = 3000;
    }
    else {
        intval = intmax;
    }

    if ($('#showExportCFG').length > 0) {
        $('#showExportCFG').remove();
    }
    $(document.body).append(exportdghtml);
    $('#filetype').combobox();
    $('#endnum').numberspinner({
        min: 1,
        max: intmax,
        value: intval
    });
    //
    $('#startnum').numberspinner({
        min: 1,
        max: intmax,
        value: 1
    });

    art.dialog({
        title: '导出选项',
        content: document.getElementById('showExportCFG'),
        lock: true,
        id: 'dgExport',
        padding: '2px',
        button: [            
            {
                value: '取消'                
            },
            {
                value: '导出',
                callback: function () {
                    getCheckList(serviceName);
                },
                focus: true
            }
        ]
    });
    }
    function getCheckList(serviceName) {
        
        $("div#ExportCFG input[type='radio']").each(function () {
            if ($(this).attr("checked") == "checked") {
                check = $(this).attr("value");                
            }            
        });
        if(check=="1")
        {
            ExportsData(serviceName, "1")
            return;
        }
        var beginNum = $('#startnum').numberspinner('getValue');
        var endNum = $('#endnum').numberspinner('getValue');        
        var type = "";
        var db = "";
        var id = "";
        var strSort;

        

        db = requestUrl('db');
        if (db == "") {
            db = $("#hidtype").val();
        }
        if (db == "") {
            db = "cn";
        }
        //判断现在哪个页面
        var strlocation = window.location.href.toUpperCase();
        if (strlocation.indexOf('ZTDB.ASPX') > 0) {
            type = requestUrl('type');
            if (type == "") {
                type = "QY";
            }
            if (isSSearch == "FI") {
                type = "FI";
                id = "998";
            }
            else {
                id = $('#hidNodeId').val();
            }
        }
        else if (strlocation.indexOf('FRMPATENTLIST.ASPX') > 0) {

            if (from != "FI") {
                type = from
            }
            else {
                type = db;
            }
            id = requestUrl('No');
        }
        //var FileType = $('#filetype').combobox('getValue');

        var myDialog = showProcess();
        $.ajax({
            type: "POST",
            url: "../comm/export.aspx/ExportDataList",
            data: "{'type': '" + type + "', 'db':'" + db + "','id':'" + id + "','beginNum':'" + beginNum + "','endNum':'" + endNum + "','strSort':'" + strSort + "'}",
            timeout: 120000, // set time out 120 seconds
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                var msg = XMLHttpRequest.responseText;
                if (textStatus == "timeout") {
                    msg = "超时，请稍后再试！";
                }
                showMessage(msg);
                myDialog.close();
                return;
            },
            success: function (msg) {
                cpiclst = msg.d;
                ExportsData(serviceName, cpiclst);
                //
                //$('#ifile').attr("src", "/Comm/ExportExcel.aspx?filename=" + msg.d);
                //                document.getElementById("ifile").src = "/Comm/ExportExcel.aspx?filename=" + msg.d;
                myDialog.close();
            }
        });
    }

    function ExportsData(serviceName,item) {

        var arrayObj = new Array();
        if (item == "1") {
            if ($('#hidCpicids').val() == ',') {
                alert('提示请选择专利');
                return;
            }
            var arrayObj = $('#hidCpicids').val().split(',');
        }
        else {
            var arrayObj = item.split(',');
        }
        
        var count = arrayObj.length-2;
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
                        window.location.href = '../My/SmartQuery.aspx';
                        break;
                    case 1: //用户已定制此服务
                        var ruleCode = result.ResultObjs[3];
                        var numbers = result.ResultObjs[2];
                        var logName = result.ResultObjs[5];
                        var functionCode = result.ResultObjs[1];
                        customExports(serviceName, functionCode, ruleCode, "custom");
                        break;
                    case 2: //用户未定制此服务，弹出定制窗口
                        var code = result.ResultObjs[1];
                        if (code == "") {
                            art.dialog({
                                id: 'hyfy2',
                                title: '提示',
                                dblclick_hide: false,
                                content: '<img src="../js/artDialog/skins/icons/warning.png" alt=""/>&nbsp;&nbsp;&nbsp;&nbsp;系统暂时无此服务.',
                                border: false,
                                lock: true,
                                okValue: '确定',
                                cancel: false,
                                ok: function () {
                                    window.location.href = '../My/SmartQuery.aspx';
                                }
                            });
                            return false;
                        }
                        SetCustomeServiceVisible(serviceName, code, count);
                        //alert(result.Message);
                        break;
                }

            }
        };
        var failedCallback = new Object();
        failedCallback.CallbackMethod = function (error, textContent, methodName) {
            alert("translationBibloCustomServices:" + error.get_message());
        };
        wsUser.UserCustomServices(serviceName, Function.createDelegate(successCallbackObj, successCallbackObj.CallbackMethod), Function.createDelegate(failedCallback, failedCallback.CallbackMethod));
        successCallbackObj = null;
        failedCallback = null;
    }

function showEnoughScoreWnd(accountMoney, serviceMoney, obj, functionCode, serviceName, ruleCode) {
    var html = "<div id='contentDiv' style=\" padding: 10px 10px 10px 10px;line-height:20px;width:400px;\">" +
                "<p>本次功能需要支付: " + serviceMoney + "积分</p>" +
                "<p>您当前账户余额: " + accountMoney + "积分</p>" +
                "<p>注意:点击继续后无法撤销,生成下载号单后启动<br /><a>客户端下载工具</a>下载您需要的数据,请慎重操作.</p>" +
                "</div>";
    art.dialog({
        title: "定制" + serviceName + "服务",
        content: html,
        id: 'hyfy',
        padding: '0px',
        lock: true,
        button: [
        {
            value: '定制服务',
            callback: function () {
                ShowCostRules(functionCode, serviceName);
            }
        },
        {
            value: '继续',
            callback: function () {
                customExports(serviceName, functionCode, "","");
            }
        },
        {
            value: '关闭'
        }
    ]
    });
}
//生成下载号单
function customExports( serviceName, functionCode, ruleCode, custom) {
    if (serviceName === "")
        return;
    var CpicIds = "";
    if (check == "0") {
        CpicIds = cpiclst;
    }
    else {
        CpicIds = $('#hidCpicids').val();
    }


    var ColNames = "";

    var dbtype = "";
    dbtype = requestUrl('db');
    if (dbtype == "") {
        dbtype = $("#hidtype").val();
    }
    if (dbtype == "") {
        dbtype = "cn";
    }
    var type = "0";
    if (serviceName == "") {
        
    }

    //发送服务器
    if (CpicIds == ',') {
        showMessage('提示请选择专利');
        return;
    }
    var myDialog = showProcess();

    var wsUser = new Patentquery.My.UserService;
    var successCallbackObj = new Object();
    successCallbackObj._control = this;
    successCallbackObj.CallbackMethod = function (result, textContent, methodName) {
        if (result.CauseException) {
            alert(result.Message);
        }
        else {
            //UpdateUseTimes(ruleCode, useTimes, functionCode, type);
            myDialog.close();
            closeProcess('dgExport');
            showMessage("生成成功,请启动下载客户端下载");
        }
    };
    var failedCallback = new Object();
    failedCallback.CallbackMethod = function (error, textContent, methodName) {
        alert(methodName + error.get_message());
    };
    wsUser.CreateCpic(CpicIds, serviceName, dbtype, ruleCode, functionCode, custom, Function.createDelegate(successCallbackObj, successCallbackObj.CallbackMethod), Function.createDelegate(failedCallback, failedCallback.CallbackMethod));
    successCallbackObj = null;
}
function showExportCFG() {
    //
    var obj = arguments[0];
    var type = requestUrl('db');
    if (type == "") {
        type = $("#hidtype").val();
    }
    var exportdghtml= cnexportdghtml;
   
    if ($('#showExportCFG').length > 0) {
        $('#showExportCFG').remove();
    }
    $(document.body).append(exportdghtml);

    if (obj != null) {
        art.dialog({
            title: '导出选项',
            content: document.getElementById('showExportCFG'),
            lock: true,
            padding: '2px',
            id: 'dgExport',
            button: [
                {
                    value: '全选',
                    callback: function () {
                        selectcfg('true');
                        return false;
                    },
                    focus: false
                },
                {
                    value: '取消全选',
                    callback: function () {
                        selectcfg('false');
                        return false;
                    },
                    focus: false
                },
                {
                    value: '导出',
                    callback: function () {
                        Export(obj);
                    },
                    focus: true
                }
            ]
        });
    }
    else {
        //发送服务器
        if ($('#hidCpicids').val() == ',') {
            alert('提示请选择专利');
            return;
        }
        art.dialog({
            title: '导出选项',
            content: document.getElementById('showExportCFG'),
            lock: true,
            id: 'dgExport',
            button: [
                {
                    value: '全选',
                    callback: function () {
                        selectcfg('true');
                        return false;
                    },
                    focus: false
                },
                {
                    value: '取消全选',
                    callback: function () {
                        selectcfg('false');
                        return false;
                    },
                    focus: false
                },
                {
                    value: '导出',
                    callback: function () {
                        Export();
                    },
                    focus: true
                }
            ]
        });
    }
}
function showExportCFG1() {
    var type = requestUrl('db');
    if (type == "") {
        type = $("#hidtype").val();
    }
    var exportdghtml = cnexportdghtml1;

    var intmax = $('#pagetop').pagination('options').total;
    var intval;
    if (intmax > 100) {
        intval = 100;
    }
    else {
        intval = intmax;
    }

    if ($('#showExportCFG').length > 0) {
        $('#showExportCFG').remove();
    }
    $(document.body).append(exportdghtml);
    $('#filetype').combobox();
    $('#endnum').numberspinner({
        min: 1,
        max: intmax,
        value: intval
    });
    //
    $('#startnum').numberspinner({
        min: 1,
        max: intmax,
        value: 1
    });

    art.dialog({
        title: '导出选项',
        content: document.getElementById('showExportCFG'),
        lock: true,
        id: 'dgExport',
        padding: '2px',
        button: [
            {
                value: '全选',
                callback: function () {
                    selectcfg('true');
                    return false;
                },
                focus: false
            },
            {
                value: '取消全选',
                callback: function () {
                    selectcfg('false');
                    return false;
                },
                focus: false
            },
            {
                value: '导出',
                callback: function () {
                    Export1();
                },
                focus: true
            }
        ]
    });
}
function selectcfg() {
    //
    var obj = arguments[0];

    if (obj == "true") {
        $("div#ExportCFG input[type='checkbox']").attr("checked", true);
    } else {
        $("div#ExportCFG input[type='checkbox']").attr("checked", false);
    }
    $('#chkAppNo').attr("checked", true);
    $('#chkTitle').attr("checked", true);
}

function Export() {
    //
    var obj = arguments[0];
    var CpicIds = "";
    if (obj == null) {
        CpicIds = $('#hidCpicids').val();
    }
    else {
        CpicIds = obj;
    }

    var ColNames = "";
    $("div#ExportCFG input[type='checkbox']").each(function () {
        if ($(this).attr("checked") == "checked") {
            ColNames += $(this).attr("value") + "|";
        }
    });

    var type = "";
    type = requestUrl('db');
    if (type == "") {
        type = $("#hidtype").val();
    }
    if (type == "") {
        type = "cn";
    }


    //发送服务器
    if (CpicIds == ',') {
        showMessage('提示请选择专利');
        return;
    }
    var myDialog = showProcess();

    $.ajax({
        type: "POST",
        url: "../comm/Export.aspx/ExportData",
        data: "{'CpicIds': '" + CpicIds + "', 'ColNames':'" + ColNames + "','NodeId':'','type':'" + type + "','FileType':'csv'}",
        timeout: 30000, // set time out 30 seconds
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            var msg = XMLHttpRequest.responseText;
            if (textStatus == "timeout") {
                msg = "超时，请稍后再试！";
            }
            showMessage(msg);
            myDialog.close();
            return;
        },
        success: function (msg) {
            //
            //$('#ifile').attr("src", "/Comm/ExportExcel.aspx?filename=" + msg.d);
            document.getElementById("ifile").src = "/Comm/ExportExcel.aspx?filename=" + msg.d;
            myDialog.close();
            closeProcess('dgExport');
        }
    });
    //返回
}
function Export1() {
    //
    var beginNum = $('#startnum').numberspinner('getValue');
    var endNum = $('#endnum').numberspinner('getValue');
    var ColNames = "";
    var type = "";
    var db = "";
    var id = "";
    var strSort;

    $("div#ExportCFG input[type='checkbox']").each(function () {
        if ($(this).attr("checked") == "checked") {
            ColNames += $(this).attr("value") + "|";
        }
    });

    db = requestUrl('db');
    if (db == "") {
        db = $("#hidtype").val();
    }
    if (db == "") {
        db = "cn";
    }
    //判断现在哪个页面
    var strlocation = window.location.href.toUpperCase();
    if (strlocation.indexOf('ZTDB.ASPX') > 0) {
        type = requestUrl('type');
        if (type == "") {
            type = "QY";
        }
        if (isSSearch == "FI") {
            type = "FI";
            id = "998";
        }
        else {
            id = $('#hidNodeId').val();
        }
    }
    else if (strlocation.indexOf('FRMPATENTLIST.ASPX') > 0) {

        if (from != "FI") {
            type = from
        }
        else {
            type = db;
        }
        id = requestUrl('No');
    }
    var FileType = $('#filetype').combobox('getValue'); 
    
    var myDialog = showProcess();
    $.ajax({
        type: "POST",
        url: "../comm/Export.aspx/ExportData2",
        data: "{'type': '" + type + "', 'db':'" + db + "','id':'" + id + "','ColNames':'" + ColNames + "','beginNum':'" + beginNum + "','endNum':'" + endNum + "','strSort':'" + strSort + "','FileType':'" + FileType + "'}",
        timeout: 120000, // set time out 120 seconds
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            var msg = XMLHttpRequest.responseText;
            if (textStatus == "timeout") {
                msg = "超时，请稍后再试！";
            }
            showMessage(msg);
            myDialog.close();
            return;
        },
        success: function (msg) {

            //
            //$('#ifile').attr("src", "/Comm/ExportExcel.aspx?filename=" + msg.d);
            document.getElementById("ifile").src = "/Comm/ExportExcel.aspx?filename=" + msg.d;
            myDialog.close();
        }
    });
    //返回
}

