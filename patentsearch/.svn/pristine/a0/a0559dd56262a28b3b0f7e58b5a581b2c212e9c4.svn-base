//专家和表格检索错误提示
var objDisPlayHef = null;
//dbType:[CN 中国专利=0; EN|WD 世界专利=1]
//_searchSrc:[0 智能检索,//1 表格检索,//2 专家检索,//3 分类导航检索,//4 二次检索,//5 过滤检索]
function DoPatSearch(strkw, strQuery, dbType, _searchSrc, errTip, _MainSearchSrc) {

    dbType = dbType.toUpperCase();
    var myDialog = showProcess();
    $.ajax({
        type: "POST",
        url: "/my/SmartQuery.aspx/DoPatSearch",
        data: "{'strSearchQuery':'" + encodeURIComponent(strQuery) + "', '_strSdbType':'" + dbType + "','_sDoSrc':'" + _searchSrc + "'}",
        timeout: 30000,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            // 撤销进度条
            myDialog.close();
            //$("#masklayer").css("display", "none");
            var msg = XMLHttpRequest.responseText;
            if (textStatus == "timeout") {
                msg = "检索超时，请稍后再试！"
            }
            showError(msg);
        },
        success: function (msg) {
            // 撤销进度条
            myDialog.close();
            //$("#masklayer").css("display", "none");
            strMsg = msg.d;
            try {
                if (strMsg[0].indexOf("<hit") != -1) {
                    var num = strMsg[2]; // 检索结果数
                    var sNo = strMsg[1];

                    if ((num == "0" || num == 0) && _searchSrc != "2") {
                        zeroResultError(dbType);
                        return;
                    }

                    var viewPage = "frmPatentList.aspx";
                    if (dbType == "WD") {
                        dbType = "EN";
                    }

                    //                case 0:
                    //                strReturn = "智能检索";
                    //                break;
                    //            case 1:
                    //                strReturn = "表格检索";
                    //                break;
                    //            case 2:
                    //                strReturn = "专家检索";
                    //                break;
                    //            case 3:
                    //                strReturn = "分类导航检索";
                    //                break;
                    //            case 4:
                    //                strReturn = "二次检索";
                    //                break;
                    //            case 5:
                    //                strReturn = "过滤检索";
                    //                break;
                    //            default:
                    //                strReturn = "-";
                    //                break;
                    if (_searchSrc == "2") {
                        // 更新检索历史
                        updateSearchHis();
                        // 将焦点设为检索框
                        searchboxFocus();
                    } else {
                        if (_MainSearchSrc == null) {
                            _MainSearchSrc = _searchSrc;
                        }
                        var url = viewPage + "?db=" + dbType + "&No=" + sNo + "&kw=" + encodeURIComponent(strkw) + "&Nm=" + num
                        + "&etp=" + encodeURIComponent(errTip) + "&Query=" + encodeURIComponent(strQuery.substr(4)) + "&Qsrc=" + _MainSearchSrc;

                        OpenSearchRs(url);
                    }
                }
                else {
                    showError(strMsg[0]);
                    //zeroResultError(dbType);
                }
            } catch (e) {
                showError(strMsg[0]);
            }
        }
    });
}

function zeroResultError(type) {
    var ti = (type == "cn") ? "CN000" : "WD000";
    alert("\u68c0\u7d22\u7ed3\u679c\u4e3a\u96f6\uff01");
    //CloseTab(ti); update:suihaitao
}

function OpenSearchRs(strUrl) {
    if (objDisPlayHef != null) {
        if (document.all) {
            objDisPlayHef.href = strUrl;
            objDisPlayHef.click();
        } else {
            var evt = document.createEvent("MouseEvents");
            evt.initEvent("click", true, true);
            objDisPlayHef.href = strUrl;
            objDisPlayHef.dispatchEvent(evt);
        }
    } else {
        window.top.location.href = strUrl;
    }
}