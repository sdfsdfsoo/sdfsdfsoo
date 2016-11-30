function buildCprs(){
	var searchHead="F xx ";
	var searchbody="";
	var sybol="";
	$("input[type='text']").each(
		function(i,n){
			if(this.value!=""){
				searchbody=searchbody+sybol+"*("+this.value+"/"+this.lang.toUpperCase()+")";
			}
		}
		);
	var i=searchbody.length;
	if(i==0) alert("请输入检索式！");
	else{
		searchbody=searchbody.substring(1,i);
		$("#TxtSearch").val(searchHead+searchbody);
	}
}

function ajaxSearch(){
	$.ajax({
		type:'post',
		url:'',
		data:'',
		error:function(){},
		success:function(msg){

		}
	});
}
var m_nTetCount = 23; 
function cnClearSearch() {
    for (var i = 1; i <= m_nTetCount; i++) {
        var obj = document.getElementById("Txt" + i);
        if (obj != null) obj.value = "";
    }
    document.getElementById("TxtSearch").value = "";


}

function addcommand(obj) {
    var strCommand = obj.name;

    var destinationObj = document.getElementById("TxtSearch");
    destinationObj.focus();
    if (typeof document.selection != "undefined") {
        document.selection.createRange().text = strCommand;
    }
    else {
        var start = destinationObj.selectionStart;
        var oldValue = destinationObj.value;
        destinationObj.value = oldValue.substring(0, start) + strCommand + oldValue.substring(start, oldValue.length);
        destinationObj.selectionStart = start;
        destinationObj.selectionEnd = start + strCommand.length;
    }
}