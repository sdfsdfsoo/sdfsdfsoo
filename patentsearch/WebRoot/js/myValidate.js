$.extend($.fn.validatebox.defaults.rules, {
	minLength : {
		validator : function(value, param) {
			return value.length >= param[0];
		},
		message : 'Please enter at least {0} characters.'
	},
	equals : {
		validator : function(value, param) {
			return value == $(param[0]).val();
		},
		message : '两次输入不一致！'
	},
	pd:{
		validator : function(value, param){
			var str = /[0-9A-z]{6,20}/;
			return str.test(value);
		},
		message : '输入6~20位英文、数字或常用符号！'
	},
	phone: {
                validator: function (value) {
                    return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
                },
                message: '电话号码格式不正确,正确电话号码格式:020-88888888！'
            },
     mobile: {
                validator: function (value) {
                    return /^(13|15|18)\d{9}$/i.test(value);
                },
                message: '手机号码格式不正确，请输入正确的11位手机号码格式！'
            }
	
	
});
function checkUsername(){
	var username = $("input[id='user.username']").val();
	if(!UsernameIsRight(username)){
		$("input[id='user.username']").val("");
		$("input[id='user.username']").blur();
		return false;
	}
	$.ajax({
		   type: "POST",
		   url: "/front/user/user_checkByUsername",
		   data: "user.username=" + username,
		   success: function(msg){
			     if(msg == "fail"){
			    	 $("#checkmessage").html("<font color=\"red\">用户名已经存在，请更换用户名！</font>");
			    	 alert(username+"已经存在，请更换用户名！");
			    	 $("input[id='user.username']").val("");
			    	 $("input[id='user.username']").blur();
			    	 
			     }else{
			    	 $("#checkmessage").html("<font color=\"green\">该用户名可以注册！</font>");
			     }
		     
		   }
		});
}
/* 验证邮箱 */
function isEmail(obj){
	var  reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	if(!reg.test(obj.value)){
		alert("邮箱格式错误!");
		obj.value="";
		obj.blur();
	}
}

/* 验证登录名，登录名由英文字母，数字或“_”组成，长度为6-20，首字符必须是字母 */
function UsernameIsRight(value){
	if(value == ""){
		alert("用户名不能为空");
		return false;
	}else{
		var reg = /^[a-zA-Z]\w{5,19}$/;
		if(!reg.test(value)){
			alert("用户名格式错误");
			return false;
		}
	}
	return true;
		
}
/* 验证手机号码 */
function isphoneNum(obj){
	var  reg = /^(1[3-9][0-9]\d{8})?$/;
	if(!reg.test(obj.value)){
		alert("手机号码格式错误，注意手机号码11位");
		obj.value="";
		obj.blur();
	
	}
}

function isAddress(obj){
	var reg=/^\d{n}$/;
	if(!reg.test(obj.value)){
		alert("地址不可以完全是数字");
		obj.value="";
		obj.blur();
		return false;
	}
	var reg2=/^[a-zA-Z]{n}$/;
	if(!reg2.test(obj.value)){
		alert("地址不可以完全是字母");
		obj.value="";
		obj.blur();
		return false;
	}
}

/* 验证座机号码 */
function isFixedPhone(obj){
	var reg = /^\d{3}-\d{8}|\d{4}-\d{7,8}$/; 
	if(!reg.test(obj.value)){
		alert("固定电话格式错误，格式如0551-82016169或010-82016169");
		obj.value="";
		obj.blur();
		$("#btn_ok").attr("disabled",true);
	}else{
		$("#btn_ok").attr("disabled",null);
	}
}
		
	function JTrim(s)
	{
	    return s.replace(/(^\s*)|(\s*$)/g, "");
	}
	 function formSubmitaa() {
		 
		 if(JTrim( $("input[id='user.username']").val())==""){
				alert("用户名不能为空!");
				return  false;
		}
		if(JTrim($('#txtRealName').val())==""){
			alert("姓名不能为空!");
			return  false;
		}
		if(JTrim($('#txtShouJi').val())==""){
			alert("手机不能为空!");
			return  false;
		}
		
		$('#_form').submit();;
	}