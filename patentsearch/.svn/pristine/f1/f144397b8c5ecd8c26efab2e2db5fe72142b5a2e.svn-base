//显示移动到树 type = zt|qy
//id == ,1,2,3,4,5,
function chanageTree() {
    var ztid = $('#zttype').find("option:selected").val();
    $('#AddZT').tree({
        url: '../comm/getNodes.aspx?ztid=' + ztid + '&fileter=true'
    });
}
function ShowAddToZT() {
    
    var yonghuleixing = $('#yonghuleixing').val();
    var title = "添加到专题库";
    if (yonghuleixing == "企业") {
        title = "添加到企业在线数据库";
    }
    var ztid = $('#zttype').find("option:selected").val();
    var CpicIds;    
    var obj = arguments[0];
    
    if (obj != null) {
        CpicIds = obj;
    }
    else {
        CpicIds = $('#hidCpicids').val(); //专利ID号
        if (CpicIds == "" || CpicIds == ",") {
            showMessage('提示请选择专利');
            return;
        }
        else {

        }
    }  

    $('#AddZT').tree({
        url: '../comm/getNodes.aspx?ztid=' + ztid + '&fileter=true'
    });

    AddZTDialog('AddToZT', title, CpicIds);
}

//添加到专题库
function AddToZT(CpicIds) {
    
    var NewNodeIds = "";               //节点IDS 
    var nodes = $('#AddZT').tree('getChecked');

    if (nodes.length <= 0) {
        showMessage("选择分类");
        return;
    }
    for (var i = 0; i < nodes.length; i++) {
        if (NewNodeIds != '') NewNodeIds += ',';
        NewNodeIds += nodes[i].attributes.Nid;
    }

    var type = requestUrl('db');
    if (type == "") {
        type = $("#hidtype").val();
    }    
    // 显示进度条
    var myDialog = showProcess();
    $.ajax({
        type: "POST",
        url: "../comm/editNodes.aspx/AddToTH",
        data: "{'pids':'" + CpicIds + "','nodids':'" + NewNodeIds + "','type':'" + type + "'}",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (msg) {
            myDialog.close();
            var res = msg.d;
            if (res == "succ") {
                art.dialog.get('DGAddZT').close();
                showMessage("添加成功！");                
            } else {
                showMessage("添加失败");
            }
           
        }
    });

}