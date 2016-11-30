function FormatCollection(pid) {
    var s = String.Format('<a href="javascript:void(0);" onclick="showAddCO(\'{0}\')"><img src="/Images/smallshoucang_bj.png" alt=""/>收藏</a>', pid);
    return s;
}
function FormatExport(pid) {
    var s = String.Format('<a href="javascript:void(0);" onclick="showExportCFG(\'{0}\')"><img src="/Images/smalldaochu_bj.png" alt=""/>导出</a>', pid);
    return s;
}
function FormatSimilar(AppNo) {
    var s = String.Format('<a href="javascript:void(0);" onclick="showSimilar(\'{0}\')"><img src="/Images/smalltong_bj.png" alt=""/>同类专利</a>', AppNo);
    return s;
}
function FormatFamily(AppNo, CPIC) {
    var s = String.Format('<a href="javascript:void(0);" onclick="showFamily(\'{0}\',\'{1}\')"><img src="/Images/smalltong_bj.png" alt=""/>同族专利</a>', AppNo, CPIC);
    return s;
}

function FormatCompare(id, cpicid, appno, title) {
    var s = String.Format("<a href=\"javascript:void(0);\" onclick=\"addCart('{0}', '{1}', '{2}');\"><img src=\"/Images/smallduibi_bj.png\" alt=\"\">专利对比</a>", id, appno, title);
    return s;
}
function FormatDelCo(pid) {
    var s = String.Format("<a href=\"javascript:void(0);\" onclick=\"DelCo('{0}');\"><img src=\"/Images/smallquxiaoshou_bj.png\" style=\"height:16px\" alt=\"取消收藏\" />取消收藏</a>", pid);
    return s;
}

function FormatDelZT(pid) {
    var s = String.Format("<a href=\"javascript:void(0);\" onclick=\"DelToZT('{0}');\"><img src=\"/Images/smallshanchu_bj.png\" style=\"height:16px\" alt=\"删除\" />删除</a>", pid);
    return s;
}

function FormatMoveToZT(pid) {
    var s = String.Format("<a href=\"javascript:void(0);\" onclick=\"ShowMoveToZT('{0}');\"><img src=\"/Images/smallzhuanyi_bj.png\" style=\"height:16px\" alt=\"移动到\" />移动到</a>", pid);
    return s;
}

function FormatEditCo(pid) {
    var s = String.Format("<a href=\"javascript:void(0);\" onclick=\"EditCo('{0}');\"><img src=\"../jquery-easyui-1.8.0/themes/icons/pencil.png\" alt=\"修改标注\" />修改标注</a>", pid);
    return s;
}

function FormatAddToZT(pid) {
    var s = String.Format("<a href=\"javascript:void(0);\" onclick=\"ShowAddToZT('{0}')\"><img src=\"/Images/smalljiaru_bj.png\" alt='加入专题库'></img><span class='spaddtozt'>加入专题库</span></a>", pid);
    return s;
}
function FormatImg(imgSrc, title, pid) {
    //var s = String.Format("<a href=\"{0}\" id='aimg" + pid + "' class=\"jqzoom\" rel='gal1'  title=\"{1}\" ><img src=\"{0}\" id='img" + pid + "' title=\"{1}\"  style=\"border: 1px solid #666;\" onload=\"regimg('" + pid +"')\"></a>", imgSrc, title.trim());
    var s = String.Format("<img src=\"../Images/loading.gif\"  title=\"{1}\"  style=\"border: 1px solid #666;\" onload=\"regimg(this,'{0}')\"></a>", imgSrc, title.trim());
    return s;
}


function FormatTitle(type, strTitle, strANX, zhuanLiLeiXing, cpic) {
    if (strTitle == null) strTitle = "无";
    var strReturn = "";
    if (strTitle.trim() == "") strTitle = "无";
    strTitle = strTitle.trim();
    var qy = requestUrl('Query');
    if (qy == null || qy == "") qy = "";
    if (type.toUpperCase() == "CN") {
        strReturn = "<a href=\"../my/frmPatDetails.aspx?Id=" + strANX + "&xy=" + cpic + "&qy=" + qy + "\" target=\"_blank\">" + strTitle.trim() + "</a>";
    }
    else {
        strReturn = "<a href=\"../my/EnPatentDetails.aspx?Id=" + strANX + "&xy=" + cpic + "&qy=" + qy + "\" target=\"_blank\">" + strTitle.trim() + "</a> ";
    }
    return strReturn;
}
function FormatABS(ABS) {
    var strReturn = "";
    if (ABS.trim() == "") {
        strReturn = '<div class="iAB biblio-item1">摘要：无</div>';
    }
    else {
        strReturn = '<div class="iAB biblio-item1"><span>摘要： ' + ABS + "</span></div>";
    }
    return strReturn;
}
function FormatCL(ABS) {
    var strReturn = "";
    if (ABS.trim() == "") {
        strReturn = '<div class="iAB biblio-item1">主权利要求：无</div>';
    }
    else {
        strReturn = '<div class="iAB biblio-item1"><span>主权利要求： ' + ABS + "</span></div>";
    }
    return strReturn;
}
function ShowLawState(lawState, ANX) {
    var strReturn = String.Format("&nbsp;&nbsp;<img src='{0}' title='点击查看法律状态详细' alt='' style='cursor:pointer;vertical-align:middle;' onclick='showLaw(\"{1}\");'/>", lawState, ANX);
    return strReturn;
}
function FormatAppNo(ApNo) {
    return '<div class="iApNo biblio-item">申请号：' + ApNo + '</div>';
}

function FormatAppDate(AppDate, Type) {
    var strReturn = "<div class='iAD biblio-item'>申请日：<a href='../my/frmDoSq.aspx?db=" + Type + "&Query=F XX (" + FormatSearchDate(AppDate) + "/AD)' target=\"_blank\">" + ShowDate(AppDate) + "</a></div>";
    return strReturn;
}
function FormatAppDate1(AppDate, Type) {
    var strReturn = "<a href='../my/frmDoSq.aspx?db=" + Type + "&Query=F XX (" + FormatSearchDate(AppDate) + "/AD)' target=\"_blank\">" + ShowDate(AppDate) + "</a>";
    return strReturn;
}
function FormatApply(strApply, Type) {
    var strReturn = "<div class='iPA biblio-item2'>申请人：";
    var arrayApply = strApply.split(';');
    for (var i = 0; i < arrayApply.length; i++) {

        strReturn += "<a href='../my/frmDoSq.aspx?db=" + Type + "&Query=F XX (" + encodeURIComponent(arrayApply[i].replace(/(\(|（)/g, " ").replace(/(\)|）)/g, " ").replace(/-/g, ' ').replace(/&/g, " ")) + "/PA)' target=\"_blank\">" + arrayApply[i] + "</a>&nbsp;";
    }
    strReturn += "</div>"
    return strReturn;
}
function FormatApply1(strApply, Type) {
    var strReturn = "";
    var arrayApply = strApply.split(';');
    for (var i = 0; i < arrayApply.length; i++) {

        strReturn += "<a href='../my/frmDoSq.aspx?db=" + Type + "&Query=F XX (" + encodeURIComponent(arrayApply[i].replace(/(\(|（)/g, " ").replace(/(\)|）)/g, " ").replace(/-/g, ' ').replace(/&/g, " ")) + "/PA)' target=\"_blank\">" + arrayApply[i] + "</a>&nbsp;";
    }
    strReturn += ""
    return strReturn;
}
function FormatAT1(strApply, Type) {
    var strReturn = "";
    if (strApply.trim() == "") return "无";
    var arrayApply;
    strApply = strApply.replace(/\s+/g, " ");
    if (strApply.indexOf(';') > 0) {
        arrayApply = strApply.split(';');
    }
    else {
        arrayApply = strApply.split(' ');
    }
    for (var i = 0; i < arrayApply.length; i++) {

        strReturn += "<a href='../my/frmDoSq.aspx?db=" + Type + "&Query=F XX (" + encodeURIComponent(arrayApply[i].replace(/(\(|（)/g, " ").replace(/(\)|）)/g, " ").replace(/-/g, ' ').replace(/&/g, " ")) + "/AT)' target=\"_blank\">" + arrayApply[i] + "</a>&nbsp;";
    }
    strReturn += ""
    return strReturn;
}
function FormatAG1(strApply, Type) {
    var strReturn = "";
    if (strApply.trim() == "") return "无";
    var arrayApply = strApply.split(';');
    for (var i = 0; i < arrayApply.length; i++) {

        var agc = arrayApply[i].match(/\d{4,}/g)[0].replace('(', "").replace(')', "");
        strReturn += "<a href='../my/frmDoSq.aspx?db=" + Type + "&Query=F XX (" + encodeURIComponent(agc) + "/AG)' target=\"_blank\">" + arrayApply[i] + "</a>&nbsp;";
    }
    strReturn += ""
    return strReturn;
}
function formatIN(strIN, Type) {
    var strReturn = "<div class='iIN biblio-item2'>发明人：";
    var arry = strIN.split(';');
    for (var i = 0; i < arry.length; i++) {
        strReturn += "<a href='../my/frmDoSq.aspx?db=" + Type + "&Query=F XX (" + encodeURIComponent(arry[i].replace(/(\(|（)/g, " ").replace(/(\)|）)/g, " ").replace(/-/g, ' ').replace(/&/g, " ")) + "/IN)' target=\"_blank\">" + arry[i] + "</a>&nbsp;";
    }
    strReturn += "</div>"
    return strReturn;
}
function formatIN1(strIN, Type) {
    var strReturn = "";
    var arry = strIN.split(';');
    for (var i = 0; i < arry.length; i++) {
        strReturn += "<a href='../my/frmDoSq.aspx?db=" + Type + "&Query=F XX (" + encodeURIComponent(arry[i].replace(/(\(|（)/g, " ").replace(/(\)|）)/g, " ").replace(/-/g, ' ').replace(/&/g, " ")) + "/IN)' target=\"_blank\">" + arry[i] + "</a>&nbsp;";
    }
    strReturn += ""
    return strReturn;
}
function formatIPC(strIPC, Type, zhuanLiLeiXing) {
    var strReturn = "<div class='iIC biblio-item'>";
    if (Type.toUpperCase() == "CN") {
        if (zhuanLiLeiXing == "3") {
            strReturn += "外观分类：";
            if (strIPC == "") {
                strReturn += "无";
            }
            else {
                var arry = strIPC.split(';');
                for (var i = 0; i < arry.length; i++) {
                    var adm = FIPC(arry[i])
                    var showadm = FormatADM(arry[i]);
                    strReturn += "<a href='../my/frmDoSq.aspx?db=" + Type + "&Query=F XX (" + encodeURIComponent(adm) + "/MC)' target=\"_blank\">" + showadm + "</a>&nbsp;";
                }
            }
        }
        else {
            strReturn += "主分类：";
            if (strIPC == "") {
                strReturn += "无";
            }
            else {
                var arry = strIPC.split(';');
                for (var i = 0; i < arry.length; i++) {
                    var adm = FIPC(arry[i]);
                    strReturn += "<a href='../my/frmDoSq.aspx?db=" + Type + "&Query=F XX (" + encodeURIComponent(adm) + "/MC)' target=\"_blank\">" + arry[i] + "</a>&nbsp;";
                }
            }
        }
    }
    else {
        strReturn += "分类号：";
        if (strIPC == "") {
            strReturn += "无";
        }
        else {
            var arry = strIPC.split(';');
            for (var i = 0; i < arry.length; i++) {
                var adm = FenIPC(arry[i]);
                strReturn += "<a href='../my/frmDoSq.aspx?db=" + Type + "&Query=F XX (" + encodeURIComponent(adm) + "/MC)' target=\"_blank\">" + arry[i] + "</a>&nbsp;";
            }
        }
    }
    strReturn += "</div>"
    return strReturn;
}
function formatIPC1(strIPC, Type, zhuanLiLeiXing) {
    var strReturn = '<td class="dt_title">';
    if (Type.toUpperCase() == "CN") {
        if (zhuanLiLeiXing == "3") {
            strReturn += "外观分类：</td><td>";
            if (strIPC == "") {
                strReturn += "无";
            }
            else {
                var arry = strIPC.split(';');
                for (var i = 0; i < arry.length; i++) {
                    var adm = FIPC(arry[i])
                    var showadm = FormatADM(arry[i]);
                    strReturn += "<a href='../my/frmDoSq.aspx?db=" + Type + "&Query=F XX (" + encodeURIComponent(adm) + "/MC)' target=\"_blank\">" + showadm + "</a>&nbsp;";
                }
            }
        }
        else {
            strReturn += "主分类：</td><td>";
            if (strIPC == "") {
                strReturn += "无";
            }
            else {
                var arry = strIPC.split(';');
                for (var i = 0; i < arry.length; i++) {
                    var adm = FIPC(arry[i]);
                    strReturn += "<a href='../my/frmDoSq.aspx?db=" + Type + "&Query=F XX (" + encodeURIComponent(adm) + "/MC)' target=\"_blank\">" + arry[i] + "</a>&nbsp;";
                }
            }
        }
    }
    else {
        strReturn += "分类号：</td><td>";
        if (strIPC == "") {
            strReturn += "无";
        }
        else {
            var arry = strIPC.split(';');
            for (var i = 0; i < arry.length; i++) {
                var adm = FenIPC(arry[i]);
                strReturn += "<a href='../my/frmDoSq.aspx?db=" + Type + "&Query=F XX (" + encodeURIComponent(adm) + "/MC)' target=\"_blank\">" + arry[i] + "</a>&nbsp;";
            }
        }
    }
    strReturn += "</td>"
    return strReturn;
}
function FenIPC(ipc) {
    ipc = ipc.replace(/ /g, "");
    return ipc;
}
function FIPC(ipc) {

    var ind = ipc.indexOf('(');
    if (ind > 0) {
        ipc = ipc.substr(0, ind).trim();
    }
    var tem = ipc.replace(/-/g, "");
    var split = tem.split('/');
    if (split.length == 2)//如果IPC中包含/
    {
        var start4 = split[0].substr(0, 4); //开始4位不变
        var mid3 = split[0].substring(4).Trim().PadLeft(3, '0'); //中间不足3位则左补0
        var last4 = split[1].Trim().PadRight(4, '0'); //最后不足4为则右补0
        return start4 + mid3 + last4;
    }
    else {
        return tem.PadRight(11, '0');
    }
}
function FormatPubNo(strPubNo, Type) {

    var strReturn = "<div class='iPN biblio-item'>";

    if (Type.toUpperCase() == "CN") {
        var reg = /(\d{7,9})([A-Za-z])/g; //去掉公开公告最后一位字母

        if (reg.test(strPubNo)) {
            strPubNo = strPubNo.replace(reg, "$1");
        }
    }
    if (strPubNo == "") {
        strReturn += "公开号：无 ";
    }
    else {
        strReturn += "公开号：" + strPubNo + "";
    }
    strReturn += "</div>";
    return strReturn;
}
function FormatPubNo1(strPubNo, Type) {

    var strReturn = '<td class="dt_title">';

    if (Type.toUpperCase() == "CN") {
        var reg = /(\d{7,9})([A-Za-z])/g; //去掉公开公告最后一位字母

        if (reg.test(strPubNo)) {
            strPubNo = strPubNo.replace(reg, "$1");
        }
    }
    if (strPubNo == "") {
        strReturn += "公开号：</td><td>无";
    }
    else {
        strReturn += "公开号：</td><td>" + strPubNo + "";
    }
    strReturn += "</td>";
    return strReturn;
}
function FormatGPNo(strPubNo, Type) {

    var strReturn = "<div class='iGN biblio-item'>";

    if (Type.toUpperCase() == "CN") {
        var reg = /(\d{7,9})([A-Za-z])/g; //去掉公开公告最后一位字母

        if (reg.test(strPubNo)) {
            strPubNo = strPubNo.replace(reg, "$1");
        }
    }
    if (strPubNo == "") {
        strReturn += "公告号：无 ";
    }
    else {
        strReturn += "公告号：" + strPubNo + "";
    }
    strReturn += "</div>";
    return strReturn;
}
function FormatGPNo1(strPubNo, Type) {

    var strReturn = '<td class="dt_title">';

    if (Type.toUpperCase() == "CN") {
        var reg = /(\d{7,9})([A-Za-z])/g; //去掉公开公告最后一位字母

        if (reg.test(strPubNo)) {
            strPubNo = strPubNo.replace(reg, "$1");
        }
    }
    if (strPubNo == "") {
        strReturn += "公告号：</td><td>无 ";
    }
    else {
        strReturn += "公告号：</td><td>" + strPubNo + "";
    }
    strReturn += "</td>";
    return strReturn;
}

function FormatPubDate(strPubDate, Type) {

    var strReturn = "";
    var relDate = "";
    var tmp = strPubDate;   
    if (tmp.indexOf("年") >= 0) {
        tmp = tmp.replace('年', '-').replace('月', '-').replace('日', '').replace(/./g, "-");

        var arys = tmp.split('-');
        //myDate = new Date(arys[0], --arys[1], arys[2]);
        relDate += arys[0];
        var m = arys[1];
        if (m.length == 1) {
            relDate += "0" + m;
        }
        else {
            relDate += m;
        }
        var d = arys[2];
        if (d.length == 1) {
            relDate += "0" + d;
        }
        else {
            relDate += d;
        }

    }
    else {
        relDate = FormatSearchDate(tmp);
    }
    if (relDate == "") {
        strReturn += "<div class='iPD biblio-item'>公开日：无</div>";
    }
    else {
        strReturn += "<div class='iPD biblio-item'>公开日：<a href='../my/frmDoSq.aspx?db=" + Type + "&Query=F XX (" + encodeURIComponent(relDate) + "/PD)'s target=\"_blank\">" + strPubDate + "</a></div>";
    }
    

    return strReturn;
}
function FormatPubDate1(strPubDate, Type) {

    var strReturn = "";
    var relDate = "";
    var tmp = strPubDate;
    if (tmp.indexOf("年") >= 0) {
        tmp = tmp.replace('年', '-').replace('月', '-').replace('日', '').replace(/./g, "-");

        var arys = tmp.split('-');
        //myDate = new Date(arys[0], --arys[1], arys[2]);
        relDate += arys[0];
        var m = arys[1];
        if (m.length == 1) {
            relDate += "0" + m;
        }
        else {
            relDate += m;
        }
        var d = arys[2];
        if (d.length == 1) {
            relDate += "0" + d;
        }
        else {
            relDate += d;
        }

    }
    else {
        relDate = FormatSearchDate(tmp);
    }
    if (relDate == "") {
        strReturn += '<td class="dt_title">公开日：</td><td>无</td>';
    }
    else {
        strReturn += "<td class='dt_title'>公开日：</td><td><a href='../my/frmDoSq.aspx?db=" + Type + "&Query=F XX (" + encodeURIComponent(relDate) + "/PD)'s target=\"_blank\">" + strPubDate + "</a></td>";
    }


    return strReturn;
}
function FormatGPDate1(strPubDate, Type) {

    var strReturn = "";
    var relDate = "";
    var tmp = strPubDate;
    if (tmp.indexOf("年") >= 0) {
        tmp = tmp.replace('年', '-').replace('月', '-').replace('日', '').replace(/./g, "-");

        var arys = tmp.split('-');
        //myDate = new Date(arys[0], --arys[1], arys[2]);
        relDate += arys[0];
        var m = arys[1];
        if (m.length == 1) {
            relDate += "0" + m;
        }
        else {
            relDate += m;
        }
        var d = arys[2];
        if (d.length == 1) {
            relDate += "0" + d;
        }
        else {
            relDate += d;
        }

    }
    else {
        relDate = FormatSearchDate(tmp);
    }
    if (relDate == "") {
        strReturn += '<td class="dt_title">公告日：</td><td>无</td>';
    }
    else {
        strReturn += "<td class='dt_title'>公告日：</td><td><a href='frmDoSq.aspx?db=" + Type + "&Query=F XX (" + encodeURIComponent(relDate) + "/GD)'s target=\"_blank\">" + strPubDate + "</a></td>";
    }

    return strReturn;
}
function FormatGPDate(strPubDate, Type) {

    var strReturn = "";
    var relDate = "";
    var tmp = strPubDate;
    if (tmp.indexOf("年") >= 0) {
        tmp = tmp.replace('年', '-').replace('月', '-').replace('日', '').replace(/./g, "-");

        var arys = tmp.split('-');
        //myDate = new Date(arys[0], --arys[1], arys[2]);
        relDate += arys[0];
        var m = arys[1];
        if (m.length == 1) {
            relDate += "0" + m;
        }
        else {
            relDate += m;
        }
        var d = arys[2];
        if (d.length == 1) {
            relDate += "0" + d;
        }
        else {
            relDate += d;
        }

    }
    else {
        relDate = FormatSearchDate(tmp);
    }
    if (relDate == "") {
        strReturn += "<div class='iPD biblio-item'>公告日：无</div>";
    }
    else {
        strReturn += "<div class='iGP biblio-item'>公告日：<a href='frmDoSq.aspx?db=" + Type + "&Query=F XX (" + encodeURIComponent(relDate) + "/GD)'s target=\"_blank\">" + strPubDate + "</a></div>";
    }

    return strReturn;
}



function ShowDate(date) {
    var strReturn = "";

    if (date.length == 8) {
        date = date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6, 8) + ".";
    }
    strReturn = date;
    return strReturn;
}

function FormatADM(adm) {
    adm = adm.replace(/-/g, "");
    var newadm;
    if (adm.length == 4) {
        newadm = adm.substr(0, 2) + "-" + adm.substr(2, 2);
    }
    else if (adm.length > 4) {
        newadm = adm.substr(0, 2) + "-" + adm.substr(2, 2) + "-" + adm.substr(4);
    }
    return newadm;
}

function FormatSearchDate(date) {

    var rsdata = date.replace(/[-\.\/年月日]/g, "");
    return rsdata;
}
