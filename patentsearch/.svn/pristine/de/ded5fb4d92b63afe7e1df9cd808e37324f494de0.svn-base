
function InitAddtoCo(pageType) {
    $('#AddToCo').unbind("click");
    var pid = $("#btnActiveTab").attr("title");
    //alert($("#btnActiveTab").attr("title"));
    if (pid == null || pid == "") {
        pid = getUrlParam('xy');
    }

    if (pageType != "EN") {
        pageType = "CN";
    }

    $('#AddToCo').click(function () { showAddCO(pid, pageType); });
}

var objFtZoom = null;

function resizeFt(obj, src) {
    var nSizi = 300;
    var imgTmp = new Image();
    imgTmp.onload = function () {
        var imgObj = this;

        if (imgObj.height == 165 && imgObj.width == 200) {  //&& imgObj.fileSize == "16301"
            $('#ImageFt').attr("onload", "");
            $('#ImageFt').attr("src", "../Images/NoImg_300.jpg");
            return;
        }

        if (imgObj.height > imgObj.width && imgObj.height > nSizi) {
            imgObj.width = imgObj.width / (imgObj.height / nSizi);
            imgObj.height = nSizi;
        } else if (imgObj.width > nSizi) {
            imgObj.height = imgObj.height / (imgObj.width / nSizi);
            imgObj.width = nSizi;
        }

        $('#divImgFt').html("");
        imgObj.id = "ImageFt";
        imgObj.alt = '摘要附图'
        $('#divImgFt').append(imgObj);

        objFtZoom = new imageZoom("ImageFt", {
            mul: 2, viewerPos: { h: -1, v: 0 }
        });
        $('#divFtZoom').show();
    };

    imgTmp.onerror = function () {
        $('#ImageFt').attr("onload", ""); //unbind("onload");
        $('#ImageFt').attr("src", "../Images/NoImg_300.jpg");
    }

    imgTmp.src = src;
}

function ToSize(i) {
    objFtZoom.imgToSize(i);
}

function imgRoll() {
    objFtZoom.imgRoll();
}

function imgReverse(i) {
    objFtZoom.imgReverse(i);
}
function imgReverse(i) {
    objFtZoom.imgReverse(i);
}

function LoadPdfFile(objdivNamee, objUrl) {
    try {
        //alert(objUrl);
        $('#divPfpage').html($('#HiddenField1Pdf').val());
        //$('#HiddenField1Pdf').val("");
        return;
    } catch (e) {
    }
}

//标引词汇
function GetAutoIndexWord() {
    try {

        if ($("[id$='hidAutoIndexWord']").val() != "") {
            $("[id$='divAutoIndexWord']").html($("[id$='hidAutoIndexWord']").val());
            return;
        }

        $.ajax({
            type: "POST",
            url: "frmPatDetails.aspx/getAutoBiaoYin",
            data: "{'strANX':'" + getUrlParam('Id') + "'}",
            timeout: 3000,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                var msg = XMLHttpRequest.responseHTML;
                if (textStatus == "timeout") {
                    msg = "请求超时，请稍后刷新页面再试！";
                }
                //alert(msg);
                //$("[href$='.jpg']") 
                $("[id$='divAutoIndexWord']").html("请求超时，未加载标引内容,请稍后刷新页面再试！")

                $("[id$='hidAutoIndexWord']").val("请求超时，未加载标引内容,请稍后刷新页面再试！")
            },
            success: function (msg) {
                $("[id$='divAutoIndexWord']").html(msg.d)
                $("[id$='hidAutoIndexWord']").val(msg.d)
            }
        });
    } catch (e) {
    }

}

//标注内容
function GetUserUserCollect() {
    try {
        $.ajax({
            type: "POST",
            url: "../comm/AutoCollects.aspx?PID=8779247",
            timeout: 30000,
            contentType: "application/json; charset=utf-8",
            dataType: "html",
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                var msg = XMLHttpRequest.responseHTML;
                if (textStatus == "timeout") {
                    msg = "请求超时，请稍后再试！";
                }
                //alert(msg);
                $('#divSetBz').html("暂无标注内容!")
            },
            success: function (msg) {
                $('#divSetBz').html(msg)
            }
        });
    } catch (e) {
    }

}

function showImg() {
    src = arguments[0];
    $("#ImagePicture").attr("src", src).css("opacity", "0.2").fadeTo(500, 1);
}

function showDiv() {
    var h3Id = arguments[0];
    var divId = arguments[1];
    if ($("#" + h3Id).attr("class") == "open") {
        $("#" + h3Id).attr("class", "");
    } else {
        $("#" + h3Id).attr("class", "open");
    }
    $("#" + divId).toggle();
}


//中文-检测专利类型
function checkPType() {
    var RegPtype = new RegExp("(^.{2}3.{5}.*$)|(^.{2}3.{5}\..$)|(^.{4}3.{7}.*$)|(^.{4}3.{7}\..$)");
    var strApNo = $("#tdApno").text().Trim();
    var ulHtml = "<li><a href='#tabMianXml'>著录项目信息</a></li>";
    if (RegPtype.test(strApNo)) {
        //$("#DivPatDetailTabs").tabs("option", "hide", 1);
        //$("#DivPatDetailTabs").tabs({ hide: false });

        ulHtml += "<li><a href='#TabDegImgs'>外观图形</a></li>";
        $("#DivtabPdf").hide();
        $("#divTabClams").hide();
        $("#divTabDes").hide();

        $("#divClaim1").hide();
        $("#h3Claim").hide();
        $("#trFmXx").hide();
        $("#tdClassLab").text("外观分类号:");
    } else {
        //$("#DivPatDetailTabs").tabs({ disabled: 1 });
        ulHtml += "<li><a href='#DivtabPdf'>全文PDF</a></li>";
        ulHtml += "<li><a href='#divTabClams'>权利要求</a></li>";
        ulHtml += "<li><a href='#divTabDes'>说明书</a></li>";

        $("#TabDegImgs").hide();
        $("#trFmXx").show();
    }

    ulHtml += "<li><a href='#divTabLegal'>法律状态</a></li>";
    $("#ulPatTabs").html(ulHtml);
}

var DialogUiLoading;
function ActivateTabPanel(objTbaUi) {

    // <li><a href="#tabMianXml">著录项目信息</a></li>
    //            <li><a href="#TabDegImgs">外观图形</a></li>
    //            <li><a href="#DivtabPdf">全文PDF</a></li>
    //            <li><a href="#divTabDes">说明书</a></li>
    //            <li><a href="#divTabClams">权利要求</a></li>
    //            <li><a href="#divTabLegal">法律状态</a></li>

    var tabName = $(objTbaUi.newTab).text().Trim();

    $("#hidActiveTabTi").val(tabName);
    $("#hidActiveTabIdx").val($("#DivPatDetailTabs").tabs("option", "active"));
    var isLoadData = true;
    switch (tabName) {
        case '著录项目信息':
            isLoadData = false;
            return;
        case "说明书":
            break;
        case '权利要求':
            break;
    }

    if (objTbaUi.newPanel.html().indexOf("Loading......") > -1) {
        DialogUiLoading = showProcess();
        $("#btnActiveTab").click();
    }
}


function IniteTabs(isPostBack, pageType) {
    try {
        $("#divAccordion").accordion({
            heightStyle: "content",
            activate: function (event, ui) {
                if ($(ui.newHeader).text().Trim() == "自动标引") {
                    GetAutoIndexWord();
                }
            }
        });

        if (pageType != "EN") {
            checkPType();
        }

        if (isPostBack) {
            $('#DivPatDetailTabs').tabs({ active: $("#hidActiveTabIdx").val() });

            //closed loading....            
            //DialogUiLoading.close();
            var tabName = $("#hidActiveTabTi").val();          
            if (tabName == "全文PDF") {
                $('#AddToCo').hide();
                $("[id$='LinkButtonDownload']").hide();
            } else {
                $('#AddToCo').show();
                $("[id$='LinkButtonDownload']").show();
                InitAddtoCo(pageType);
            }            
        } else {
            InitAddtoCo(pageType);
        }

        $('#DivPatDetailTabs').tabs({
            fxFade: true,
            fxSpeed: 'fast',
            activate: function (event, ui) {
                ActivateTabPanel(ui);
            }
        });
        DialogUiLoading.close();   //throw a error, 
    } catch (e) {
    }
}


function InitGL() {
    try {
        var strhl = getGLKws(requestUrl("qy").replace("#", ""));
        $('#tabMianXml').removeHighlight();
        var tmpary = strhl.split(';');
        for (var i = 0; i < tmpary.length; i++) {
            $('#tabMianXml').highlight(tmpary[i]);
        }
    } catch (e) {
    }
}