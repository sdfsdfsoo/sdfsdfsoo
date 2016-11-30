var law = $.extend({}, $.fn.datagrid.defaults.view, {
	renderRow : function(target, fields, frozen, rowIndex, rowData) {
		var cc = [];
		cc.push('<td  style="padding:10px 5px;border:0;">');
		cc.push('<div class="gridViewItem">');
		cc.push('<div class="title1">');
		cc.push('<b>申请号</b>： <a href="#" target="_blank">' + rowData.attr1
				+ '</a></div>');
		cc.push('<div class="note">');
		cc.push('<b>法律状态公告日</b>：' + rowData.attr2
				+ '<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 法律状态</b>：' + rowData.attr3
				+ '<br />');
		cc.push(' <b>法律状态信息</b>：' + rowData.attr4 + '<br />');
		cc.push('变更事项：'+rowData.attr4+'<BR> 变更前权利人：' + rowData.attr5 + '<BR> 变更后权利人：'
				+ rowData.attr6 + '<BR> 登记生效日：' + rowData.attr7
				+ '</div></div>');
		cc.push('</td>');

		return cc.join('');
	}
});
$(function() {
	$('#tt').datagrid({
		view : law,
		showHeader : false,
		pagePosition : top
	});
});