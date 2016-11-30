

//专家和表格检索错误提示

// 错误有几种提示方式，均修改为仅提示错误内容
//1、js提示错误，以Error:开头；
//2、web服务器提示错误，仅有错误信息；
//3、引擎返回错误，如(003) J 1+2 <error: 引用的set号不存在>；
//4、ajax通信错误，如 "Message":"给定关键字不在字典中。...."
// return 函数返回值为错误类型
function showError(msg) {
    var _msg = msg;
    var type = 2;ss
    var jsError = /Error:(.*)/;
    var engineError = /\<error:(.*)\>/;
    var ajaxError = /\"Message\":\"(.*?)\""/;
    if (jsError.test(msg)) {
        _msg = RegExp.$1;
        type = 1;
    }
    else if (engineError.test(msg)) {
        //_msg = "检索引擎返回错误！";
        //_msg = "引擎错误提示： " + RegExp.$1;
        _msg = RegExp.$1;
        type = 3;
    }
    else if (ajaxError.test(msg)) {
        //_msg = "AJAX通信错误！";
        //_msg = "AJAX通信错误提示： " + RegExp.$1;
        _msg = "引擎通信错误！";
        type = 4;
    }

    //alert(_msg);
    var myDialog = art.dialog({
        id: 'dgError',
        title: '提示',
        padding: '5px',
        ok: "确定",
        okValue: '确定',
        border: false,
        content: '<img src="../js/artDialog/skins/icons/error.png" alt=""/>&nbsp;&nbsp;&nbsp;&nbsp;' + _msg
    });
    return myDialog;
    return type;
}

function showMessage() {
    if (arguments.length == 1) {
        art.dialog({
            title: '提示',
            content: '<img src="../js/artDialog/skins/icons/warning.png" alt=""/>&nbsp;&nbsp;&nbsp;&nbsp;' + arguments[0],
            border: false,
            lock: true,
            okValue: '确定',
            ok: function () {
            }
        });
    }
    else {
        art.dialog({
            title: arguments[0],
            content: '<img src="../js/artDialog/skins/icons/warning.png" alt=""/>&nbsp;&nbsp;&nbsp;&nbsp;' + arguments[1],
            border: false,
            lock: true,
            okValue: '确定',
            ok: function () {
            }
        });
    }
}


function showProcess() {
    var myDialog = art.dialog({
        title: false,
        id: "process",
        lock: true,
        width: '300px',
        content: '<img src="../Images/loading1.gif" alt="" style="margin-left:-20px;"/>&nbsp;&nbsp;&nbsp;&nbsp;正在处理中,请稍候...'
    });
    return myDialog;
}

function closeProcess() {
    var d;
    try {
        if (arguments.length == 0) {
            d = art.dialog.get('process');
        }
        else {
            d = art.dialog.get(arguments[0]);
        }

        if (d != null) d.close();
    } catch (e) {
    }
}
function hideProcess() {
    var d = art.dialog.get('process');
    if (d != null) d.hidden();
}
function showSimilar(strAppNO) {
    $('#sm').css("width", "570px");
    $('#sm').css("height", "320px");
    $('#ism').attr("src", "../comm/Similar.aspx?appno=" + strAppNO);
    art.dialog({
        title: "同类专利",
        content: document.getElementById('sm'),
        id: 'dlgshowSimilar',
        padding: '0px',
        ok: "确定",
        okValue: '确定',
        lock: true,
        border: false

    });

}
function showFamily(strAppNO, CPIC) {
    $('#sm').css("width", "620px");
    $('#sm').css("height", "320px");
    $('#ism').attr("src", "../comm/Family.aspx?appno=" + strAppNO + "&id=" + CPIC);
    art.dialog({
        title: "同族专利",
        content: document.getElementById('sm'),
        id: 'dlgshowSimilar',
        padding: '0px',
        ok: "确定",
        okValue: '确定',
        lock: true,
        fixed: true,
        border: false

    });

}

function AddZTDialog(ElementId, title, CpicIds) {
    art.dialog({
        id: 'DGAddZT',
        title: title,
        content: document.getElementById(ElementId),
        lock: true,
        border: false,
        okValue: '确定',
        ok: function () {
            AddToZT(CpicIds);
            return false;
        }
    });
}

function MoveZTDialog(ElementId, title, appid) {
    art.dialog({
        id: 'DGMoveZT',
        title: title,
        content: document.getElementById(ElementId),
        lock: true,
        border: false,
        okValue: '确定',
        ok: function () {
            MoveToZT(appid);
            return false;
        }
    });
}



function TableSearchDialog(title) {
    art.dialog({
        id: "DGTableSearch",
        title: title,
        content: document.getElementById("tabSearch"),
        lock: true,
        border: false,
        button: [
         {
             value: '清空检索式',
             callback: function () {
                 clsSP();
                 return false;
             },
             focus: true,
             width: '100px'
         }
            ,
            {
                value: '生成检索式',
                callback: function () {
                    getztsp();
                    return false;
                },
                focus: true,
                width: '100px'
            },
            {
                value: '确定',
                callback: function () {
                    //
                    SSearch();
                    return false;
                },
                focus: true,
                width: '60px'
            }
            ]
    });
}


function showsp() {
    var patternUrl = requestUrl('Query');
    if (patternUrl == "") {
        patternUrl = $("#hidQuery").val();
    }
    patternUrl = decodeURIComponent(patternUrl).Trim();
    patternUrl = patternUrl.replace('F+XX+', '').replace('F XX ', '');
    patternUrl = HTMLEncode(patternUrl);
    art.dialog({
        title: "检索式",
        content: patternUrl,
        border: false,
        lock: true,
        okValue: '确定',
        ok: function () {
        }
    });
}
function showLaw(ANX) {

    if ($('divlaw').length == 0) {
        var lawdlghtml = "<div id='divlaw' style='width: 620px; height: 300px; display: none;'><iframe id='frmlaw' name='ism'  frameborder='0' src='' style='width: 100%;height: 100%;'></iframe></div>";
        $(document.body).append(lawdlghtml)
    }

    $('#divlaw').css("width", "620px");
    $('#divlaw').css("height", "320px");
    $('#frmlaw').attr("src", "../my/frmLawInfo.aspx?Idx=" + ANX);
    art.dialog({
        title: "法律状态",
        content: document.getElementById('divlaw'),
        id: 'dlgshowlaw',
        padding: '0px',
        ok: "确定",
        okValue: '确定',
        lock: true,
        fixed: true,
        border: false

    });
}


