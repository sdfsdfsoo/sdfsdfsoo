document.write("<script src=\"../js/FormatHTML.js\"></script>");
document.write("<script src=\"../js/FormatTable.js\"></script>");
document.write("<script src=\"../js/Export.js\"></script>");
document.write("<script src=\"../js/AjaxDoPatSearch.js\"></script>");
document.write("<script src=\"../js/AddToCO.js\"></script>");
document.write("<script src=\"../js/AddToZT.js\"></script>");
document.write("<script src=\"../js/Cart.js\"></script>");
document.write("<script src=\"../js/CheckSPCN.js\"></script>");
document.write("<script src=\"../js/CheckSPEN.js\"></script>");
document.write("<script src=\"../js/RegImgZoom.js\"></script>");
//document.write("<script src=\"../js/Trans.js\"></script>");
document.write("<script src=\"../js/right.js\"></script>");

var table;
var from;
var strSort = "PD|DESC";
var strSortText;
var ddsort;
var ddsort1;
var _showtype;
function ShowPL(showtype) {    
    if (table != null) {      
        table.ShowTable(showtype);
        SetChecked();
        if (showtype == "0") {
            $('.thumbnail').show();
        }
        else {
            $('.thumbnail').hide();
        }
    }
    else {
        ShowPatentList(showtype, "", "", "");
        if (showtype == "0") {
            $('.thumbnail').show();
        }
        else {
            $('.thumbnail').hide();
        }

    }
    _showtype = showtype;
}

function ShowPatentList(showtype, pageindex, pagesize, sort) {
    //
    var db = requestUrl('db');
    var itemcount = requestUrl('Nm');
    var SearchNo = requestUrl('No');
    var kw = requestUrl('kw');
    from = requestUrl('from');
    if (from == "") from = "FI";
    if (pageindex == "") {
        pageindex = $('#hidPageIndex').val();
    }
    if (showtype == "") {
        showtype =  _showtype;
    }
    if (pagesize == "") {
        pagesize = $('#hidpagesize').val();
    }
    if (sort == null || sort == "") {
        sort = strSort;
    }
    else {
        strSort = sort;
    }

    ShowTable(db, SearchNo, from, pageindex, pagesize, itemcount, showtype, sort);
}

function ShowTable(type, nodeid, sourcetype, pageindex, pagesize, itemcount, showtype, sort) {
    if (sort == null || sort == "") {
        sort = strSort;
    }
    else {
        strSort = sort;
    }
    var myDialog = showProcess();
    $('#list').datagrid('loading')
    $.ajax({
        type: "POST",
        url: "../comm/GetList.aspx/GetPageList",
        data: "{'Type': '" + type + "', 'NodeId':'" + nodeid + "', 'SourceType': '" + sourcetype + "', 'ItemCount':'" + itemcount + "', 'pageindex':'" + pageindex + "', 'rows':'" + pagesize + "','Sort':'" + sort + "'}",
        timeout: 30000, // set time out 30 seconds
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            var msg = XMLHttpRequest.responseText;
            if (textStatus == "timeout") {
                msg = "超时，请稍后再试！";
            }
            myDialog.close();
            showMessage("错误", msg);
            return;
        },
        success: function (msg) {
            $('#hidType').val(type);
            $('#hidPageIndex').val(pageindex);
            $('#hidpagesize').val(pagesize);
             _showtype = showtype;
            $('#hidItemCount').val(itemcount);
            var data = $.parseJSON(msg.d);
            FormatTable(type, data, pageindex, pagesize, showtype);
            myDialog.close();
        }
    });
}
function FormatTable(type, data, pageindex, pagesize, showtype) {
    table = new zlTable("divlist", type, data);
    var yonghuleixing = $('#yonghuleixing').val();
    table.ShowTable(showtype, yonghuleixing);
    var strhl = getGLKws(requestUrl("Query").replace("#", ""));
    $('#divlist').removeHighlight();
    var tmpary = strhl.split(';');
    for (var i = 0; i < tmpary.length; i++) {
        $('#divlist').highlight(tmpary[i]);
    }
    SetPage(data.total, pagesize, pageindex);
    SetChecked();
    if (showtype == "0") {
        $('.thumbnail').show();
    }
    else {
        $('.thumbnail').hide();
    }

    closeProcess();

}

//展示表格检索
function ShowTabSearch(title) {

    var type = requestUrl('db');
    var Qsrc = requestUrl("Qsrc");
    if (Qsrc == "") Qsrc = "";
    var url = "";
    if (title == "重新检索") {

        switch (Qsrc) {
            case "0":
                url = "SmartQuery.aspx";
                break;
            case "1":
                url = (type.toUpperCase() == "CN" ? "frmCnTbSearch.aspx" : "frmWdTbSearch.aspx");
                break;
            case "2":
                url = (type.toUpperCase() == "CN" ? "frmcnExpertSearch.aspx" : "frmEnExpertSearch.aspx");
                break;
            case "3":
                url = "frmIPCSearch.aspx";
                break;
            default:
                url = "SmartQuery.aspx";
                break;
        }
        window.location.href = "./" + url;
        return;
    }
    if (type.toUpperCase() == 'CN') {
        $('#endivQueryTable').hide();
        $('#cndivQueryTable').show();
        cnClearSearch();

    }
    else if (type.toUpperCase() == "EN") {
        docdbClearSearch();
        $('#cndivQueryTable').hide();
        $('#endivQueryTable').show();

    }
    TableSearchDialog(title);
    $("#DseachTitle").html(title);
}

//SSearch

function SSearch() {
    var type = requestUrl('db');
    var Query = requestUrl('Query');
    var title = $('#DseachTitle').html();

    setViewResult(" "); // 清空验证域
    var strQuery;
    strQuery = $("#TxtSearch").val();
    if (strQuery == "") {
        getztsp();
        strQuery = $("#TxtSearch").val();
    }
    if (strQuery == "") return;
    var newquery = "";
    var operator = "*";
    var Qsrc = "";
    switch (title) {
        case "过滤检索":
            operator = "-";
            Qsrc = "5";
            break;
        case "二次检索":
            operator = "*";
            Qsrc = "4";
            break;
    }
    if (operator != "") {
    
        if (strQuery.indexOf("F XX ") >= 0) {
            strQuery = strQuery.replace("F YY ", "").replace("F XX ", "");
        }
        var patternUrl = requestUrl("Query").replace("#", "");
        patternUrl = decodeURIComponent(patternUrl).Trim();
        var leixing = getEndFlag(patternUrl);
        patternUrl = getSearchQuery(patternUrl);
        patternUrl = "(" + patternUrl.replace("F YY ", "").replace("F XX ", "").replace("#", "") + ")";
        strQuery = "(" + strQuery.replace("F YY ", "").replace("F XX ", "") + ")";
        if (leixing == null || leixing == "") {
            newquery = "F XX " + patternUrl + operator + strQuery;
            if (type.toUpperCase() == "CN") {
                SearchDbType = "CN";
                newquery = getMergeSearchEndFlag(newquery.Trim());
            }
            else {
                SearchDbType = "EN";
                newquery = getMergeSearchEndFlag(newquery.Trim(), 'enckbox');
            }
        }
        else {
            newquery = "F XX " + patternUrl + operator + strQuery + leixing;
        }


    } else {
        newquery = strQuery;
    }
    $("#TxtSearch").val('');
    var _MainSearchSrc = requestUrl("Qsrc");
    DoPatSearch("", newquery, type.toUpperCase(), Qsrc, "", _MainSearchSrc);

}


function clsSP() {
    var type = requestUrl('db');
    if (type.toUpperCase() == 'CN') {
        $("#endivQueryTable").hide();
        $("#cndivQueryTable").show();
        cnClearSearch();
    }
    else {
        $("#endivQueryTable").show();
        $("#cndivQueryTable").hide();
        docdbClearSearch();
    }
}

//
function getztsp() {
    var type = requestUrl('db');
    setViewResult(" "); // 清空验证域
    var strQuery = "";
    if (type.toUpperCase() == "CN") {
        if (strQuery == "") {
            strQuery = getTableSearchQueryCN().Trim();
        }
        var pattern = validateLogicSearchQueryCN(strQuery);
        if (pattern == "") {
            showMessage("请输入检索条件");
            return;
        }
        pattern = pattern.Trim();
        if (pattern.substring(0, 5) == "Error") {
            showMessage(pattern);
            return;
        }
        else {            
            $("#TxtSearch").val(strQuery);
        }

    }
    else {
        if (strQuery == "") {
            strQuery = getTableSearchQueryEN().Trim();
        }
        var pattern = validateLogicSearchQueryEN(strQuery);
        pattern = pattern.Trim();
        if (pattern == "") {
            showMessage("请输入检索条件");
            return;
        }
        if (pattern.substring(0, 5) == "Error") {
            showMessage(pattern);
            return;
        }
        else {
            SearchDbType = "EN";            
            $("#TxtSearch").val(pattern);
            strQuery = pattern;
        }

    }

}

function GOTOST() {

    var db = requestUrl('db');
    var type = db;
    if (from != "FI") { type = from };
    var num = requestUrl('Nm');
    var id = requestUrl('No');
    if (parseInt(num) > 100000) {
        showMessage("提示", "统计结果不能超过10万！请缩小检索结果后重新检索！");
        return;
    }
    else {
        window.open("../comm/autost.aspx?type=" + type + "&db=" + db + "&id=" + id + "&Nm=" + num);
    }
}

function showRight(cpic) {
    var type = requestUrl('db').toUpperCase();
    for (var i = 0; i < table.data.rows.length; i++) {
        var rowData = table.data.rows[i];
        var id;
        if (type.toUpperCase() == "EN") {
            id = rowData.StrSerialNo;
        }
        else {
            id = rowData.CPIC;
        }
        if (id == cpic) {
            var title = FormatTitle(type, rowData.StrTitle, rowData.StrANX, rowData.ZhuanLiLeiXing, rowData.StrSerialNo);
            var AN = rowData.StrApNo;
            var AD = FormatAppDate1(rowData.StrApDate, type);
            var PN = FormatPubNo1(rowData.StrPubNo, type);
            var PD = FormatPubDate1(rowData.StrPubDate, type);
            var GPNo = FormatGPNo1(rowData.StrAnnNo, type);
            var GPDate = FormatGPDate1(rowData.StrAnnDate, type);
            var IPC = formatIPC1(rowData.StrMainIPC, type, rowData.ZhuanLiLeiXing);
            var PA = FormatApply1(rowData.StrApply, type);
            var IN = formatIN1(rowData.StrInventor, type);
            var ABS = FormatABS(rowData.StrAbstr);
            var IMG = FormatImg(rowData.StrFtUrl, rowData.StrTitle, rowData.StrSerialNo);
            var CL = FormatCL(rowData.StrClaim);
            var CY = rowData.StrCountryCode;
            var address = rowData.StrShenQingRenDiZhi;
            var AGC = FormatAG1(rowData.StrAgency, type);
            var AG = FormatAT1(rowData.StrDaiLiRen, type);
            if (rowData.ZhuanLiLeiXing != "3" && type.toUpperCase() == "CN") {
                PN = PN;
                PD = PD;
            }
            if (type.toUpperCase() != "CN") {
                PN = PN.replace("公开号", "公开(公告)号");
                PD = PD.replace("公开日", "公开(公告)日");
            }
            if (type.toUpperCase() == "CN") {
                PN = GPNo;
                PD = GPDate;
            }


            var html = tmp.replace('<!--title-->', title);
            html = html.replace('<!--AN-->', AN);
            html = html.replace('<!--AD-->', AD);
            html = html.replace('<!--PN-->', PN);
            html = html.replace('<!--PD-->', PD);
            html = html.replace('<!--IPC-->', IPC);
            html = html.replace('<!--PA-->', PA);
            html = html.replace('<!--IN-->', IN);
            html = html.replace('<!--AB-->', ABS);
            html = html.replace('<!--IMG-->', IMG);
            html = html.replace('<!--CL-->', CL);
            html = html.replace('<!--CY-->', CY);
            html = html.replace('<!--address-->', address);
            html = html.replace('<!--AGC-->', AGC);
            html = html.replace('<!--AG-->', AG);
            $(".right_columns").html(html);
            
            if ($(".right_columns").css("height") > $(".left_columns").css("height")) {
                $(".left_columns").css("height", $(".right_columns").css("height"));
            }
            break;
        }

    }
}
var tmp = '<center> <h3 class="title"> <!--title--></h3> </center>  <table cellpadding="0" cellspacing="0">  <tr class="row1"> <td class="dt_title"> 申请号：</td> <td> <!--AN--> </td> <td class="dt_title"> 申请日： </td> <td> <!--AD--> </td> </tr>  <tr class="row2"><!--PN--><!--PD--></tr>  <tr class="row1"> <td class="dt_title"> 申请人：</td> <td colspan="3"> <!--PA--> </td> </tr>  <tr class="row2"> <td class="dt_title"> 地址：</td> <td colspan="3"> <!--address--> </td> </tr> <tr class="row1"> <td class="dt_title"> 发明人：</td> <td colspan="3"> <!--IN--> </td> </tr>  <tr class="row2"><td class="dt_title"> 代理人：</td> <td colspan="3"><!--AG--></td></tr> <tr class="row1"><td class="dt_title"> 代理机构：</td> <td colspan="3"><!--AGC--></td></tr> <tr class="row2"><!--IPC--><td class="dt_title"> 国省代码： </td> <td> <!--CY--> </td> </tr>  </table> <h3 class="title"> <b>摘要</b></h3> <div class="r_desc"> <div class="r_desc_l"><!--AB--></div> <div class="r_desc_r"><!--IMG--></div> <div class="clear"></div> </div> <h3 class="title"> <b>主权利要求</b></h3> <div class="r_clams"> <!--CL--> </div>';