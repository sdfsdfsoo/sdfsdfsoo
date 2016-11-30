function iniRight() {
    var ztype = getUrlParam('type');
    if (ztype == null) {
        ztype = "QY";
    }
    var rightlist = $('#rightlist').val();

    if (ztype.toUpperCase() == "ZT") {
        if (rightlist.indexOf('zt_addnode') > 0) {
            var item = $('#mm').menu('findItem', '添加分类');
            $('#mm').menu('enableItem', item.target);

            var item1 = $('#addzt').menu('findItem', '添加主分类');
            $('#addzt').menu('enableItem', item1.target);            
        }
        if (rightlist.indexOf('zt_ednode') > 0) {
            var item = $('#mm').menu('findItem', '修改名称');
            $('#mm').menu('enableItem', item.target);
        }
       
        if (rightlist.indexOf('zt_delnode') > 0) {
            var item = $('#mm').menu('findItem', '删除');
            $('#mm').menu('enableItem', item.target);
        }
        if (rightlist.indexOf('zt_addsp') > 0) {
            
            $('#cnzt_addsp').linkbutton("enable");
            $('#enzt_addsp').linkbutton("enable");
            var item = $('#mm').menu('findItem', '修改数据');
            $('#mm').menu('enableItem', item.target);
        }
        if (rightlist.indexOf('zt_edsp') > 0) {
            $('#cnzt_edsp').linkbutton("enable");
            $('#enzt_edsp').linkbutton("enable");
            var item = $('#mm').menu('findItem', '修改数据');
            $('#mm').menu('enableItem', item.target);
        }
        if (rightlist.indexOf('zt_delsp') > 0) {
            $('#cnzt_delsp').linkbutton("enable");
            $('#enzt_delsp').linkbutton("enable");
            var item = $('#mm').menu('findItem', '修改数据');
            $('#mm').menu('enableItem', item.target);
        }
        if (rightlist.indexOf('zt_eddata') < 0) {
            $("#a_eddata").hide();
            $("#a_eddata").attr("onclick", "");
        }
        if (rightlist.indexOf('zt_deldata') < 0) {
            $("#a_deldata").hide();
            $("#a_deldata").attr("onclick", "");
        }
        if (rightlist.indexOf('ADD2CO') < 0) {
            $("#ADD2CO").hide();
            $("#ADD2CO").attr("onclick", "");
        }
        if (rightlist.indexOf('GJPLXZ') < 0) {
            $("#GJPLXZ").hide();
            $("#GJPLXZ").attr("onclick", "");
        }
        
    }
    else {
        if (rightlist.indexOf('qy_addnode') > 0) {
            var item = $('#mmqy').menu('findItem', '添加分类');
            $('#mmqy').menu('enableItem', item.target);

            var item1 = $('#addqy').menu('findItem', '添加主分类');
            $('#addqy').menu('enableItem', item1.target);  
        }
        if (rightlist.indexOf('qy_ednode') > 0) {
            var item = $('#mmqy').menu('findItem', '修改名称');
            $('#mmqy').menu('enableItem', item.target);
        }        
          
       
        if (rightlist.indexOf('qy_delnode') > 0) {
            var item = $('#mmqy').menu('findItem', '删除');
            $('#mmqy').menu('enableItem', item.target);
        }
        if (rightlist.indexOf('qy_addsp') > 0) {
            
            $('#cnzt_addsp').linkbutton("enable");
            $('#enzt_addsp').linkbutton("enable");
            var item = $('#mmqy').menu('findItem', '修改数据');
            $('#mmqy').menu('enableItem', item.target);
        }
        if (rightlist.indexOf('qy_delsp') > 0) {
            $('#cnzt_delsp').linkbutton("enable");
            $('#enzt_delsp').linkbutton("enable");
            var item = $('#mmqy').menu('findItem', '修改数据');
            $('#mmqy').menu('enableItem', item.target);
        }
        if (rightlist.indexOf('qy_edsp') > 0) {
            $('#cnzt_edsp').linkbutton("enable");
            $('#enzt_edsp').linkbutton("enable");
            var item = $('#mmqy').menu('findItem', '修改数据');
            $('#mmqy').menu('enableItem', item.target);
        }
        if (rightlist.indexOf('qy_eddata') < 0) {
            $("#a_eddata").hide();
            $("#a_eddata").attr("onclick", "");
        }
        if (rightlist.indexOf('qy_deldata') < 0) {
            $("#a_deldata").hide();
            $("#a_deldata").attr("onclick", "");
        }
        if (rightlist.indexOf('ADD2CO') < 0) {
            $("#ADD2CO").hide();
            $("#ADD2CO").attr("onclick", "");
        }
        if (rightlist.indexOf('GJPLXZ') < 0) {
            $("#GJPLXZ").hide();
            $("#GJPLXZ").attr("onclick", "");
        }
    }

}

//隐藏添加 按钮
function inifunbutton() {
    
    var rightlist = $('#rightlist').val();
    var yonghuleixing = $('#yonghuleixing').val();
    if (yonghuleixing == "企业") {
        $('#ztlist').hide();
        $(".spaddtozt").each(function () {
            $(this).html("加入企业库");
        });
        
        if (rightlist.indexOf('qy_adddata') < 0) {
            $("#a_adddata").hide();
            $("#a_adddata").attr("onclick", "");
        }
        else if (rightlist.indexOf('qy_adddata') < 0) {
            $("#a_adddata").hide();
            $("#a_adddata").attr("onclick", "");
        }
    }
    else {
        $('#ztlist').show();
        if (rightlist.indexOf('zt_adddata') < 0) {
            $("#a_adddata").hide();
            $("#a_adddata").attr("onclick", "");
        }
        else if (rightlist.indexOf('zt_adddata') < 0) {
            $("#a_adddata").hide();
            $("#a_adddata").attr("onclick", "");
        }
    }
    //ZLXMECJSGL
    //判断二次检索
    if (rightlist.indexOf('ZLXMECJSGL') < 0) {
        var ss = $("#ZLXMECJSGL");
        if (ss.length > 0) {
            $("#ZLXMECJSGL").remove();
        }
    }

    //PLXZ
    //判断批量导出
    if (rightlist.indexOf(',PLXZ,') < 0) {
        $("#PLXZ").remove();
    }
    if (rightlist.indexOf('GJPLXZ') < 0) {
        $("#GJPLXZ").hide();
        $("#GJPLXZ").attr("onclick", "");
    }
    if (rightlist.indexOf('ADD2CO') < 0) {
        $("#ADD2CO").hide();
        $("#ADD2CO").attr("onclick", "");
    }

    
}