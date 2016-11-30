function query() {
    var pattern = $("#TextBoxPattern").val();
    if (pattern != "") {
        if (patentType == "cn") {
            pattern = analysisSearchQuery(pattern, "cn")
            window.location.href = "FileNotFound.htm-aspxerrorpath=-Js-frmPatentList.aspx.htm"/*tpa=http://211.160.117.107/Js/frmPatentList.aspx?db=cn&Source=0&Pattern=*/ + encodeURIComponent(pattern.replace(/\+/g, "@"));
        } else {
            pattern = analysisSearchQuery(pattern, "docdb")
            window.location.href = "FileNotFound.htm-aspxerrorpath=-Js-frmPatentList.aspx.htm"/*tpa=http://211.160.117.107/Js/frmPatentList.aspx?db=en&Source=0&Pattern=*/ + encodeURIComponent(pattern.replace(/\+/g, "@"));
        }
    } else {
        alert("请输入查询条件");
    }
}

function getOneItemQuery(dbType) {
    var strUserInputTxt = document.getElementById("searchContent").value.Trim();
    strUserInputTxt = strUserInputTxt.replace("“", "\"");
    strUserInputTxt = strUserInputTxt.replace("”", "\"");
    var strRs = "";
    if (strUserInputTxt.EndWith("\"") && strUserInputTxt.StartWith("\"")) {
        strRs = strUserInputTxt.substring(1, strUserInputTxt.length - 1);
        if (dbType.toUpperCase() == "CN") {
            strRs = strRs + "/TI+" + strRs + "/AB+" + strRs + "/CL+" + strRs + "/IN+" + strRs + "/PA";
        } else {
            strRs = strRs + "/TI+" + strRs + "/AB+" + strRs + "/PA+"+ strRs + "/IN+" + strRs + "/CT";
        }
    }

    return strRs
}

///dbType[cn|wd]
function simpleSearchNew() {
    // 获取检索式
    //var dbType = patentType.toLowerCase();
	var searchscope= document.getElementsByName("searchscope")[0].value;
    var dbType="";
    if(searchscope=="Cn"){
    	dbType="cn";
    }else{
    	dbType="en";
    }
    if (dbType == "en") {
        dbType = "wd";
    }
    var strClearQuery = getOneItemQuery(dbType);    //
    var strFinalQuery = "F XX ";
    if (strClearQuery == "") {
        strClearQuery = getClearQuery(dbType);//去空格
        if (strClearQuery == "" || strClearQuery == "请您在不同的检索关键字之间加上空格") {
            alert("请输入检索条件");
            return;
        }
        // 检索式长度判断
        if (shortLen(strClearQuery) == true) {
            strClearQuery = strClearQuery.replace(/(\/)+/g, " ");
            if (strClearQuery == "") {
                zeroResultError(dbType);
                return;
            }
            strFinalQuery = strFinalQuery + "(" + strClearQuery + "/AB)";
        } else {
            // 分析检索式
            var analysisQuery = getAnalysisQuery(strClearQuery, dbType);
            if (analysisQuery != "" && analysisQuery != null) {
                strFinalQuery += analysisQuery;   // getAnalysisQuery(strClearQuery, dbType);
            } else {
                zeroResultError(dbType);
                return;
            }
        }
    } else {
        strFinalQuery = "F XX ";
        strFinalQuery += strClearQuery;
    }
    var reg33 = /[贾流洋]+/gi;
    strFinalQuery= strFinalQuery.replace(reg33, " ");
      $('#strFinalQuery').val(strFinalQuery);
   // console.log(strFinalQuery);
    

    // 还原全局变量
    resetGlobalParams();
}