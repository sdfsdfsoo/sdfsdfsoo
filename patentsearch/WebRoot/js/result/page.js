function regSelectPage() {
    $('#pagetop').pagination({
        onSelectPage: function (pageNumber, pageSize) {            
            ShowPatentList("", pageNumber, pageSize);
        }
    });
    $('#pagebom').pagination({
        onSelectPage: function (pageNumber, pageSize) {            
            ShowPatentList("", pageNumber, pageSize);
        }
    });
}

function regPatentListSelectPage() {
    $('#pagetop').pagination({
        onSelectPage: function (pageNumber, pageSize) {           
            ShowPatentList("", pageNumber, pageSize,"");
        }
    });
    $('#pagebom').pagination({
        onSelectPage: function (pageNumber, pageSize) {            
            ShowPatentList("", pageNumber, pageSize,"");
        }
    });
}
function SetPage(inttotal, intpageSize, intpageNumber) {
    $('#pagetop').pagination({
        displayMsg: '当前显示从 [{from}] 到 [{to}] 共[{total}]条记录',
        total: parseInt(inttotal),
        pageSize: parseInt(intpageSize),
        pageNumber: parseInt(intpageNumber)
    });
    $('#pagebom').pagination({
        displayMsg: '当前显示从 [{from}] 到 [{to}] 共[{total}]条记录',
        total: parseInt(inttotal),
        pageSize: parseInt(intpageSize),
        pageNumber: parseInt(intpageNumber)
    });
}
function SetPage1(inttotal, intpageSize, intpageNumber) {
    $('#pagetop').pagination({
        displayMsg: '从 [{from}] 到 [{to}] 共[{total}]条',
        total: parseInt(inttotal),
        pageSize: parseInt(intpageSize),
        pageNumber: parseInt(intpageNumber)
    });
    $('#pagebom').pagination({
        displayMsg: '从 [{from}] 到 [{to}] 共[{total}]条',
        total: parseInt(inttotal),
        pageSize: parseInt(intpageSize),
        pageNumber: parseInt(intpageNumber)
    });
}


function ChangeShowStype(obj) {
    
    ShowPatentList(type, "", "");
}

function Orderby(sort) {
    ShowPatentList("", "", "", strSort);
    ddsort.setText(strSortText);
    ddsort1.setText(strSortText);
}


//全选 
function SelectAll1(div, obj) {    
    $("div#" + div + " input[type='checkbox']").each(function () {
        $(this).attr('checked', obj.checked);
    });
        
}

//全选 
function SelectAll() {
    var obj = arguments[0];
    if (obj.checked) {
        $("div#divlist input[type='checkbox']").each(function () {
            
            if ($('#hidCpicids').val().toString().indexOf(',' + $(this).attr('id') + ',') < 0) {
                $('#hidCpicids').val($('#hidCpicids').val() + $(this).attr('id') + ",");
                $(this).attr('checked', obj.checked);
            }
        });

    }
    else {
        $("div#divlist input[type='checkbox']").each(function () {
            
            if ($('#hidCpicids').val().toString().indexOf(',' + $(this).attr('id') + ',') >= 0) {
                $('#hidCpicids').val($('#hidCpicids').val().toString().replace("," + $(this).attr('id') + ",", ","));
                $(this).attr('checked', obj.checked);
            }
        });
    }
    $('#hidCpicids').val($('#hidCpicids').val().toString().replace(",undefined,", ","));
}

//分页的时候设置选择框
function SetChecked() {
    $("div#divlist input[type='checkbox']").each(function () {
        if ($('#hidCpicids').val().toString().indexOf($(this).attr('id')) >= 0) {
            $(this).attr('checked', 'true');
        }
    });
}

//选择某一个复选框
function AddCheckId(obj) {
    var id = obj.id;
    if (obj.checked) {
        if ($('#hidCpicids').val().toString().indexOf("," + id + ",") < 0) {
            $('#hidCpicids').val($('#hidCpicids').val() + id + ",");
        }
    }
    else {
        if ($('#hidCpicids').val().toString().indexOf("," + id + ",") >= 0) {
            $('#hidCpicids').val($('#hidCpicids').val().toString().replace("," + id + ",", ","));
        }
    }
    //alert($('#hidCpicids').val());
}

