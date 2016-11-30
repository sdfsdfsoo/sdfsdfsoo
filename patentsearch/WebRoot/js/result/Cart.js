function addCart() {    
    if (!$("#cart").is(":visible")) {
        $("#cart").show();
    }
    if ($("div#cart .item").length == 4) {
        alert("最多只能对比4篇专利！");
        return;
    }
    var anx = arguments[0];
    var apno = arguments[1];
    var title = arguments[2];
    if ($("#childItem" + anx).length == 0) {
        $("#cartItems").html($("#cartItems").html() + "<div id='childItem" + anx + "' class='clear'><div class='item' title='" + title + "--" + apno + "'>" + title + "--" + apno + "</div> <a href='javascript:void(0);' onclick=removeCart('childItem" + anx + "') class='btnTiny'>X</a><div class='clear'></div></div>");
    }
}
function removeCart() {
    var id = arguments[0];
    $("#" + id).remove();
}
function clearCart() {
    $("#cartItems").html("");
}
function submitCart() {
    var compareIds = "";
    $("div[id^='childItem']").each(function () {
        compareIds += $(this).attr("id").substring(9) + "|";
    });
    var type = requestUrl("db");
    if (type == "") {
        type = $("#hidtype").val();
    }
    type = type.toUpperCase();
    if (compareIds != "") {
        window.open("../my/ComparePatent.aspx?type=" + type + "&Ids=" + compareIds );
    } else {
        alert("请添加要比较的专利");
    }
}
function closeCart() {
    $("#cart").hide();
}
