function zlTable(name, type, data) {
    this.name = name;
    this.type = type;
    this.data = data;
    this.tbhtml = "";
    this.push = function (strhtml) {
        this.tbhtml += strhtml;
    };
    this.rightlist = $('#rightlist').val();
    this.yonghuleixing = $('#yonghuleixing').val();
    this.FormatBiblio = function (rowData) {
        var AppNo = FormatAppNo(rowData.StrApNo);
        var Appdate = FormatAppDate(rowData.StrApDate, type);
        var PubNo = FormatPubNo(rowData.StrPubNo, type);
        var PubDate = FormatPubDate(rowData.StrPubDate, type);
        var GPNo = FormatGPNo(rowData.StrAnnNo, type);
        var GPDate = FormatGPDate(rowData.StrAnnDate, type);
        var IPC = formatIPC(rowData.StrMainIPC, type, rowData.ZhuanLiLeiXing);
        var PA = FormatApply(rowData.StrApply, type);
        var IN = formatIN(rowData.StrInventor, type);
        var ABS = FormatABS(rowData.StrAbstr);

        this.push('<div class="note">');
        this.push('<div class="notebase">');

        this.push(AppNo);
        this.push(Appdate);
        if (rowData.ZhuanLiLeiXing != "3" && this.type.toUpperCase() == "CN") {
            this.push(PubNo);
            this.push(PubDate);
        }
        if (this.type.toUpperCase() != "CN") {
            this.push(PubNo.replace("公开号", "公开(公告)号"));
            this.push(PubDate.replace("公开日", "公开(公告)日"));
        }
        if (this.type.toUpperCase() == "CN") {
            this.push(GPNo);
            this.push(GPDate);
        }
        this.push("</div>");
        this.push(IPC);
        this.push("<div id='clear'/>");
        this.push(PA);
        this.push(IN);
        this.push(ABS);
        this.push("</div>");
    }
    this.FormatBiblioThin = function (rowData) {
        var AppNo = FormatAppNo(rowData.StrApNo);
        var Appdate = FormatAppDate(rowData.StrApDate, type);
        var PubNo = FormatPubNo(rowData.StrPubNo, type);
        var ABS = FormatABS(rowData.StrAbstr);
        this.push('<div class="note">');
        this.push('<div class="notebase">');

        this.push(AppNo);
        this.push(Appdate);
        if (rowData.ZhuanLiLeiXing != "3" && this.type.toUpperCase() == "CN") {
            this.push(PubNo);
        }
        if (this.type.toUpperCase() != "CN") {
            this.push(PubNo.replace("公开号", "公开(公告)号")); ;
        }
        this.push("</div>");
        this.push("<div id='clear'/>");
        this.push(ABS);
        this.push("</div>");
    }
    this.FormatCOBiblio = function (rowData) {
        this.push('<div class="note">');
        this.push('<div class="notebase">');
        var AppNo = FormatAppNo(rowData.StrApNo);
        var Appdate = FormatAppDate(rowData.StrApDate, type);
        var PubNo = FormatPubNo(rowData.StrPubNo, type);
        var PubDate = FormatPubDate(rowData.StrPubDate, type);
        var GPNo = FormatGPNo(rowData.StrAnnNo, type);
        var GPDate = FormatGPDate(rowData.StrAnnDate, type);
        var IPC = formatIPC(rowData.StrMainIPC, type, rowData.ZhuanLiLeiXing);
        var PA = FormatApply(rowData.StrApply, type);
        var IN = formatIN(rowData.StrInventor, type);
        var Note = "<div class='biblio-item1'>标注内容：<span id='Note" + rowData.StrSerialNo + "'>" + rowData.Note + "</span></div>";
        var NoteDate = "<div class='biblio-item1'>标注时间：<span id='NoteDate" + rowData.StrSerialNo + "'>" + rowData.NoteDate.replace(/\//g, '-') + "</span></div>";
        this.push(AppNo);
        this.push(Appdate);
        if (rowData.ZhuanLiLeiXing != "3") {
            this.push(PubNo);
            this.push(PubDate);
        }
        if (this.type.toUpperCase() != "CN") {
            this.push(PubNo.replace("公开号", "公开(公告)号"));
            this.push(PubDate.replace("公开号", "公开(公告)号"));
        }
        if (this.type.toUpperCase() == "CN") {
            this.push(GPNo);
            this.push(GPDate);
        }
        this.push("</div>");
        this.push(IPC);
        this.push("<div id='clear'/>");
        this.push(PA);
        this.push(IN);
        this.push(Note);
        this.push(NoteDate);
        this.push("</div>");
    }
    this.FormatCommand = function (type, rowData) {
        if (type == "1") {
            this.push("<div class=\"command\">");
            if (this.rightlist.indexOf("ADD2CO") > 0) {
                this.push(FormatCollection(rowData.StrSerialNo)); // 收藏
                this.push("&nbsp-&nbsp;");
            }
            this.push(FormatExport(rowData.StrSerialNo)); // 导出
            this.push("&nbsp-&nbsp;");
            if (this.type.toUpperCase() == "CN") {

                this.push(FormatSimilar(rowData.StrApNo)); //同类专利 
            }
            else {
                this.push(FormatFamily(rowData.StrPubNo, rowData.CPIC)); //同类专利  
            }
            //专利对比
            if (this.rightlist.indexOf("ZLDB") >= 0) {
                this.push("&nbsp-&nbsp;");
                this.push(FormatCompare(rowData.StrANX, rowData.StrSerialNo, rowData.StrApNo, rowData.StrTitle)); //对比
            }

            //添加到专题
            if (this.rightlist.indexOf("zt_adddata") >= 0 || (this.rightlist.indexOf("qy_adddata") >= 0 && this.yonghuleixing == "企业")) {
                this.push("&nbsp-&nbsp;");
                this.push(FormatAddToZT(rowData.StrSerialNo));
            }
            this.push("</div>"); //end command  
        }
        else if (type == "2") {
            this.push("<div class=\"command\">");
            if (this.rightlist.indexOf("ADD2CO") > 0) {
                this.push(FormatCollection(rowData.StrSerialNo)); // 收藏
                this.push("&nbsp-&nbsp;");
            }
            this.push(FormatExport(rowData.StrSerialNo)); // 导出
            this.push("&nbsp-&nbsp;");
            if (this.type.toUpperCase() == "CN") {

                this.push(FormatSimilar(rowData.StrApNo)); //同类专利 
            }
            else {
                this.push(FormatFamily(rowData.StrPubNo, rowData.CPIC)); //同类专利  
            }
            //专利对比
            if (this.rightlist.indexOf("ZLDB") >= 0) {
                this.push("&nbsp-&nbsp;");
                this.push(FormatCompare(rowData.StrANX, rowData.StrSerialNo, rowData.StrApNo, rowData.StrTitle)); //对比
            }
            this.push("&nbsp-&nbsp;");
            if ((this.rightlist.indexOf("zt_eddata") >= 0) || (this.rightlist.indexOf("qy_eddata") >= 0 && this.yonghuleixing == "企业")) {
                this.push(FormatMoveToZT(rowData.StrSerialNo)); // 移动到
                this.push("&nbsp-&nbsp;");
            }
            if ((this.rightlist.indexOf("zt_deldata") >= 0) || (this.rightlist.indexOf("qy_deldata") >= 0 && this.yonghuleixing == "企业")) {
                this.push(FormatDelZT(rowData.StrSerialNo)); // 删除
                this.push("&nbsp-&nbsp;");
            }

            this.push("<span>来源：" + rowData.Form + "</span>");
            this.push("</div><div style='clear:both'/>"); //end command
        }
        else if (type == "3") {
            this.push("<div class=\"command\">");
            this.push(FormatDelCo(rowData.StrSerialNo)); // 删除
            this.push("&nbsp-&nbsp;");
            this.push(FormatEditCo(rowData.StrSerialNo)); // 修改标注
            this.push("&nbsp-&nbsp;");
            this.push(FormatExport(rowData.StrSerialNo)); // 导出                                               
            this.push("&nbsp-&nbsp;");
            if (this.type.toUpperCase() == "CN") {

                this.push(FormatSimilar(rowData.StrApNo)); //同类专利 
            }
            else {
                this.push(FormatFamily(rowData.StrPubNo, rowData.CPIC)); //同类专利  
            }
            //专利对比
            if (this.rightlist.indexOf("ZLDB") >= 0) {
                this.push("&nbsp-&nbsp;");
                this.push(FormatCompare(rowData.StrANX, rowData.StrSerialNo, rowData.StrApNo, rowData.StrTitle)); //对比
            }
            //添加到专题
            if (this.rightlist.indexOf("zt_adddata") >= 0 || (this.rightlist.indexOf("qy_adddata") >= 0 && this.yonghuleixing == "企业")) {
                this.push("&nbsp-&nbsp;");
                this.push(FormatAddToZT(rowData.StrSerialNo));
            }
            this.push("</div><div style='clear:both'/>"); //end command  
        }


    }
    //显示专利列表
    this.ShowTable = function (showtype) {

        if (showtype == "2") {
            this.tbhtml = "<div class='left_columns'>";
        }
        else {
            this.tbhtml = "";
        }
        this.push("<ul class='zltable'>");
        for (var i = 0; i < data.rows.length; i++) {
            var rowData = data.rows[i];
            this.push("<li>");

            this.push('<div class="title">');
            //复选框
            var chck = '<input type="checkbox" id="' + rowData.StrSerialNo + '" value="' + rowData.StrApNo + '" onclick="AddCheckId(this)" />';
            this.push(chck);
            //专利名称
            var title = FormatTitle(type, rowData.StrTitle, rowData.StrANX, rowData.ZhuanLiLeiXing, rowData.StrSerialNo);
            this.push(title);

            //法律状态
            if (type.toUpperCase() == "CN") this.push(ShowLawState(rowData.FaLvZhuangTai, rowData.StrANX));
            this.push("</div>");
            //摘要附图
            var img = '<div class="thumbnail">' + FormatImg(rowData.StrFtUrl, rowData.StrTitle, rowData.StrSerialNo) + '</div>';
            this.push(img);
            //摘要
            this.push('<div class="details">');
            if (showtype == "2") {
                this.FormatBiblioThin(rowData);
            }
            else {
                this.FormatBiblio(rowData);
            }
            this.FormatCommand("1", rowData);
            this.push('</div>');
            this.push('<div style="clear: both" />');


            this.push("</li>");
        }
        this.push("</ul>");
        if (showtype == "2") {
            this.push("</div><div class='right_columns'></div><div class='clear'/>");
        }

        $("#" + name).html(this.tbhtml);
        $("div#" + name + " li").each(function () {
            $(this).mouseenter(function () {
                $(this).addClass("datagrid-row-over");
            });
            //鼠标移走
            $(this).mouseleave(function () {
                $(this).removeClass("datagrid-row-over");
            });
        });
        if (showtype == "2") {
            $("div#" + name + " li").each(function () {
                $(this).click(function () {
                    var cpic = $(this).children().first().find("input:checkbox").attr("id");
                    showRight(cpic);
                });
            });
        }
        inifunbutton();
    };

    //显示分类导航专利列表
    this.ShowTreeQueryTable = function (showtype) {

        this.tbhtml = "";
        this.push("<ul class='zltable'>");
        for (var i = 0; i < data.rows.length; i++) {
            var rowData = data.rows[i];
            this.push("<li>");
            this.push('<div class="title">');
            //复选框
            var chck = '<input type="checkbox" id="' + rowData.StrSerialNo + '" value="' + rowData.StrApNo + '" onclick="AddCheckId(this)" />';
            this.push(chck);
            //专利名称
            var title = FormatTitle(type, rowData.StrTitle, rowData.StrANX, rowData.ZhuanLiLeiXing, rowData.StrSerialNo);
            this.push(title);

            //法律状态
            if (type.toUpperCase() == "CN") this.push(ShowLawState(rowData.FaLvZhuangTai, rowData.StrANX));
            this.push("</div>");
            //摘要附图
            var img = '<div class="thumbnail">' + FormatImg(rowData.StrFtUrl, rowData.StrTitle, rowData.StrSerialNo) + '</div>';
            this.push(img);
            //摘要
            this.push('<div class="details">');
            this.FormatCOBiblio(rowData);
            this.FormatCommand("1", rowData);
            this.push('</div>');
            this.push('<div style="clear: both" />');

            this.push("</li>");
        }
        this.push("</ul>");

        $("#" + name).html(this.tbhtml);

        $("div#" + name + " li").each(function () {
            $(this).mouseenter(function () {
                $(this).addClass("datagrid-row-over");
            });
            //鼠标移走
            $(this).mouseleave(function () {
                $(this).removeClass("datagrid-row-over");
            });
        });
        inifunbutton();
    };

    //显示分类导航专利列表
    this.ShowCOTable = function (showtype) {
        this.tbhtml = "";
        this.push("<ul class='zltable'>");
        for (var i = 0; i < data.rows.length; i++) {
            var rowData = data.rows[i];
            this.push('<li id="td' + rowData.StrSerialNo + '">');

            this.push('<div class="title">');
            //复选框
            var chck = '<input type="checkbox" id="' + rowData.StrSerialNo + '" value="' + rowData.StrApNo + '" onclick="AddCheckId(this)" />';
            this.push(chck);
            //专利名称
            var title = FormatTitle(type, rowData.StrTitle, rowData.StrANX, rowData.ZhuanLiLeiXing, rowData.StrSerialNo);
            this.push(title);

            //法律状态
            if (type.toUpperCase() == "CN") this.push(ShowLawState(rowData.FaLvZhuangTai, rowData.StrANX));
            this.push("</div>");
            //摘要附图
            var img = '<div class="thumbnail">' + FormatImg(rowData.StrFtUrl, rowData.StrTitle, rowData.StrSerialNo) + '</div>';
            this.push(img);
            //摘要
            this.push('<div class="details">');
            this.FormatCOBiblio(rowData);
            this.FormatCommand("3", rowData);
            this.push('</div>');
            this.push('<div style="clear: both" />');
            this.push("</li>");
        }
        this.push("</ui>");

        $("#" + name).html(this.tbhtml);
        $("div#" + name + " li").each(function () {
            $(this).mouseenter(function () {
                $(this).addClass("datagrid-row-over");
            });
            //鼠标移走
            $(this).mouseleave(function () {
                $(this).removeClass("datagrid-row-over");
            });
        });
        inifunbutton();
    };

    //显示专题库 企业现在数据库专利列表
    this.ShowZTTable = function (showtype) {
        this.tbhtml = "";
        this.push("<ul class='zltable'>");
        for (var i = 0; i < data.rows.length; i++) {
            var rowData = data.rows[i];
            this.push('<li id="td' + rowData.StrSerialNo + '"  class="zlitem">');
            this.push('<div class="title">');
            //复选框
            var chck = '<input type="checkbox" id="' + rowData.StrSerialNo + '" value="' + rowData.StrApNo + '" onclick="AddCheckId(this)" />';
            this.push(chck);
            //专利名称
            var title = FormatTitle(type, rowData.StrTitle, rowData.StrANX, rowData.ZhuanLiLeiXing, rowData.StrSerialNo);
            this.push(title);

            //法律状态
            if (type.toUpperCase() == "CN") this.push(ShowLawState(rowData.FaLvZhuangTai, rowData.StrANX));
            this.push("</div>");
            //摘要附图
            var img = '<div class="thumbnail">' + FormatImg(rowData.StrFtUrl, rowData.StrTitle, rowData.StrSerialNo)
            this.push(img);
            this.push("<div style='margin-left: 47px;margin-top: 5px;'>");
            this.push(FormatStarts(rowData.StrSerialNo, rowData.Iscore));
            this.push("</div></div>");

            //摘要
            this.push('<div class="details">');
            this.FormatBiblio(rowData);
            this.FormatCommand("2", rowData);
            this.push('</div>');
            this.push('<div style="clear: both" />');

            this.push("</li>");
        }
        this.push("</ui>");

        $("#" + name).html(this.tbhtml);
        $("div#" + name + " li").each(function () {
            $(this).mouseenter(function () {
                $(this).addClass("datagrid-row-over");
            });
            //鼠标移走
            $(this).mouseleave(function () {
                $(this).removeClass("datagrid-row-over");
            });
        });
        inifunbutton();
    };
}