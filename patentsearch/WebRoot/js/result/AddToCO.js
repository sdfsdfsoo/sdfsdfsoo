
//参数 ID，type ,或者都不传
function showAddCO() {
    var obj = arguments[0];
    var objtype = arguments[1];
    $('#TextBoxNote').val('');
    var type;
    if (objtype != null) {
        type = objtype;
    }
    else {
        type = getUrlParam('db');
        if (type == null) {
            type = $("#hidtype").val();
        }
    }

    if (obj != null) {
        art.dialog({
            title: "收藏专利",
            content: document.getElementById('AddCO'),
            lock: true,
            id: "addtoco",
            border: false,
            okValue: '确定',
            ok: function () {
                //
                AddCO(obj, type);
                return false;
            }
        });
    }
    else {
        //发送服务器
        if ($('#hidCpicids').val() == ',') {
            showMessage('提示请选择专利');
            return;
        }
        art.dialog({
            title: "收藏专利",
            content: document.getElementById('AddCO'),
            lock: true,
            id: "addtoco",
            border: false,
            okValue: '确定',
            ok: function () {
                AddCO();
                return false;
            }
        });
    }
    var strurl = "../comm/UserCollects.aspx";
    $('#CO').tree({
        url: strurl
        , onExpand: function () {
            addhover1("CO");
        }, onLoadSuccess: function (node, data) {
            addhover1("CO");
        }
    });


}
function AddCO() {
    var obj = arguments[0];
    var obj1 = arguments[1];

    var CpicIds = ""; //专利ID号
    var type = "";
    var NewNodeIds = "";
    if (arguments.length == 2) {
        CpicIds = obj;
        type = obj1;
    }
    else {
        CpicIds = $('#hidCpicids').val();
        type = getUrlParam('db');
        if (type == null) {
            type = $("#hidtype").val();
        }
    }

    var nodes = $('#CO').tree('getChecked');

    if (nodes.length <= 0) {
        showMessage("选择收藏目录");
        return;
    }
    for (var i = 0; i < nodes.length; i++) {
        if (NewNodeIds != '') NewNodeIds += ',';
        NewNodeIds += nodes[i].id;
    }
    
    // 显示进度条   
    var myDialog = showProcess();
    var note = $('#TextBoxNote').val();
    $.ajax({
        type: "POST",
        url: "../comm/UserCollects.aspx/AddToCO",
        data: "{'pids':'" + CpicIds + "','nodids':'" + NewNodeIds + "','type':'" + type + "','Note':'" + HTMLEncode(note) + "'}",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (msg) {
            myDialog.close();
            var res = msg.d;
            if (res == "succ") {
                showMessage("收藏成功！");
                art.dialog.get('addtoco').close();
            } else {
                showMessage("收藏失败");
            }

        }
    });
}

//鼠标悬浮是添加中文+英文，离开移除
function addhover1(treename) {
    var $titleary = $("#" + treename).find("span.tree-title");
    $titleary.each(function (index) {
        var nodeid = $titleary.eq(index).parent().attr("node-id");
        var node = $("#" + treename).tree('find', nodeid);
        if (node) {
            //
            if (node.attributes) {
                var addstr;

                if (node.attributes.live == 0) {

                    addstr = '<span id="cosp' + treename + nodeid + '" style="display:none;float:right;*margin-top:-20px;"><img id="a' + nodeid + '" src="../images/add.png" alt="添加"/>&nbsp;<img  id="e' + nodeid + '" src="../images/edit.png" class="COEdit" alt="修改"/></span>';
                }
                else {
                    addstr = '<span id="cosp' + treename + nodeid + '" style="display:none;float:right;*margin-top:-20px;"><img id="a' + nodeid + '" src="../images/add.png" alt="添加"/>&nbsp;<img  id="e' + nodeid + '" src="../images/edit.png" class="COEdit" alt="修改"/>&nbsp;<img  id="d' + nodeid + '" src="../images/del.gif" class="CODEL" alt="删除"/> </span>';
                }
                //重新注册事件！！

                if ($('#cosp' + treename + nodeid).length > 0) {

                }
                $titleary.eq(index).before(addstr);
                
                //清空事件
                $('#a' + nodeid).unbind();
                $('#e' + nodeid).unbind();
                $('#d' + nodeid).unbind();
                //注册onclick事件
                $('#a' + nodeid).click(function () { appendco('CO', '添加收藏夹', nodeid); });
                $('#e' + nodeid).click(function () { editco('CO', '修改收藏夹', nodeid); });
                $('#d' + nodeid).click(function () { RemoveNodeco('CO', nodeid) });
                //清空事件
                $titleary.eq(index).parent().unbind();
                //鼠标移上                
                $titleary.eq(index).parent().mouseenter(function () {
                    $("#cosp" + treename + nodeid).show();
                });
                //鼠标移走
                $titleary.eq(index).parent().mouseleave(function () {
                    $("#cosp" + treename + nodeid).hide();
                });

            }
        }
    });
}




//Tree 点添加按钮
function appendco(treename, title) {

    var t = $('#' + treename);
    var node;
    if (arguments.length == 3) {
        node = $('#' + treename).tree('find', arguments[2]);
        $('#' + treename).tree('select', node.target);
    }
    node = t.tree('getSelected');
    $("#newclass").val("");
    $("#txtNodeDes").val("");
    $('#dialogtitle').html(title);

    art.dialog({
        title: title,
        content: document.getElementById("DivAddNode"),
        lock: true,
        id: 'addco',
        border: false,
        button: [
            {
                value: '确定',
                callback: function () {
                    //
                    addCoNode();
                    return false;
                },
                focus: true,
                width: '60px'
            }
            ]
    });

}
//编辑节点时
function editco(treename, title) {
    $('#dialogtitle').html(title);
    var node;
    if (arguments.length == 3) {
        node = $('#' + treename).tree('find', arguments[2]);
        $('#' + treename).tree('select', node.target);
    }
    node = $('#' + treename).tree('getSelected');
    $("#newclass").val(node.text);
    $("#txtNodeDes").val(node.attributes.des);
    art.dialog({
        title: title,
        content: document.getElementById("DivAddNode"),
        lock: true,
        id: 'editco',
        border: false,
        button: [
            {
                value: '确定',
                callback: function () {
                    //
                    addCoNode();
                    return false;
                },
                focus: true,
                width: '60px'
            }
            ]
    });
}
//添加节点
function addCoNode() {
    var treeName = "CO";
    //异步加载 添加节点 返回节点的ID，Name
    if ($("#newclass").val() != "") {
        var title = $('#dialogtitle').html();
        var classname = $("#newclass").val();
        var desc = $("#txtNodeDes").val();
        var t = $('#' + treeName);
        var node = t.tree('getSelected');
        var li = 0;
        var nodeid = 0;
        var thid = $("#hidthid").val();
        if (node != null) {
            nodeid = node.id;
            if (node.attributes) {
                li = parseInt(node.attributes.live);
                li += 1;
                //showMessage(li);
            }
        }
        var type = treeName;
        switch (title) {
            case "添加收藏夹":
                //
                $.ajax({
                    type: "POST",
                    url: "/comm/UserCollects.aspx/addNode",
                    data: "{'thid':'" + thid + "','parent':'" + nodeid + "','name':'" + HTMLEncode(classname) + "','des':'" + HTMLEncode(desc) + "','live':'" + li + "'}",
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (msg) {

                        var res = msg.d;
                        var obj = eval('(' + res + ')');
                        if (obj.mess == "exists") {
                            showMessage(classname + ":已经存在！请重新输入");
                            return;
                        }
                        else if (obj.mess != "failed") {
                            //
                            var t = $('#' + treeName);
                            var node = t.tree('getSelected');
                            //
                            t.tree('append', {
                                parent: (node ? node.target : null),
                                data: [{ id: obj.mess, text: classname, attributes: { live: li, des: desc}}]
                            });
                            t.tree('select', t.tree('find', obj.mess).target);
                            art.dialog.get('addco').close();
                        }
                        else {
                            showMessage("添加失败");
                        }
                    }
                });
                break;

            case "添加收藏夹 ":
                //
                nodeid = '0';
                li = '0';
                $.ajax({
                    type: "POST",
                    url: "/comm/UserCollects.aspx/addNode",
                    data: "{'thid':'" + thid + "','parent':'" + nodeid + "','name':'" + HTMLEncode(classname) + "','des':'" + HTMLEncode(desc) + "','live':'" + li + "'}",
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (msg) {
                        //
                        var res = msg.d;
                        var obj = eval('(' + res + ')');
                        if (obj.mess != "failed") {
                            var t = $('#' + treeName);
                            var node = t;
                            t.tree('append', {
                                parent: (node ? node.target : null),
                                data: [{ id: obj.mess, text: classname, attributes: { live: li, des: desc}}]
                            });
                            t.tree('select', t.tree('find', obj.mess).target);
                            art.dialog.get('addco').close();
                        } else {
                            showMessage("添加失败");
                        }

                    }
                });
                break;
            default:
                //修改节点
                $.ajax({
                    type: "POST",
                    url: "/comm/UserCollects.aspx/Rename",
                    data: "{'id':'" + nodeid + "','name':'" + HTMLEncode(classname) + "','des':'" + HTMLEncode(desc) + "'}",
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (msg) {
                        //
                        var res = msg.d;
                        if (res != "failed") {
                            var t = $('#' + treeName);
                            var node = t.tree('getSelected');
                            if (node) {
                                node.text = classname;
                                node.attributes.des = desc;
                                t.tree("update", node);
                            }
                            art.dialog.get('editco').close();
                        } else {
                            showMessage("修改失败");
                        }

                    }
                });
                break;
        }
        return false;
    }
    else {
        $("#showmsg").show();
    }
}
//删除节点
function RemoveNodeco(treename) {
    var Nodeid;
    var nodename;
    var node;
    if (arguments.length == 2) {
        node = $('#' + treename).tree('find', arguments[1]);
        $('#' + treename).tree('select', node.target);
    }
    node = $('#' + treename).tree('getSelected');
    nodename = node.text;
    Nodeid = node.id;
    art.dialog({
        title: '确认删除',
        content: '<img src="../js/artDialog/skins/icons/warning.png"/>您确定删除所选节点:<span style="color:red">' + nodename + '</span>吗？',
        button: [
          {
              value: '取消',
              callback: function () { }

          },
            {
                value: '确定',
                callback: function () {

                    $.ajax({
                        type: "POST",
                        url: "../comm/UserCollects.aspx/deleteNode",
                        data: "{'id':'" + Nodeid + "'}",
                        contentType: "application/json; charset=utf-8",
                        dataType: "json",
                        success: function (msg) {
                            if (msg.d != "failed") {
                                $('#' + treename).tree('remove', node.target);
                            } else {
                                showMessage("删除失败！");
                            }
                        }
                    });
                },
                focus: true
            }
            ]
    });

}