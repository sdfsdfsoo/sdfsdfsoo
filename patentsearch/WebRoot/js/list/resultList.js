var cardview = $.extend({}, $.fn.datagrid.defaults.view, {
	renderRow : function(target, fields, frozen, rowIndex, rowData) {
		var cc = [];
		cc.push('<td colspan=' + fields.length
				+ ' style="padding:10px 5px;border:0;">');
		if (!frozen) {
			var img = 'test.gif';
			cc.push('<img src="images/' + img
					+ '" style="width:150px;float:left">');
			cc.push('<div style="float:left;margin-left:20px;">');
			for (var i = 0; i < fields.length; i++) {
				var copts = $(target).datagrid('getColumnOption', fields[i]);
				cc.push('<p><span class="c-label">' + copts.title + ':</span> '
						+ rowData[fields[i]] + '</p>');
			}
			cc.push('</div>');
		}
		cc.push('</td>');
		return cc.join('');
	}
});
var patent = $
		.extend(
				{},
				$.fn.datagrid.defaults.view,
				{
					renderRow : function(target, fields, frozen, rowIndex,
							rowData) {
						var cc = [];
						// $("rowData").textSearch("实用");
						console.log(rowData.attr10);
						cc.push('<td  style="padding:10px 5px;border:0;">');
						cc
								.push('<div class="title"><a href="" target="_blank">增强低强度材料螺纹孔的插销螺套</a></div>');
						cc
								.push('<div class="thumbnail"><img src="" style="" ></div>');
						cc.push('<div class="details">');
						cc.push('<div class="note">');
						cc.push('<div class="notebase">');
						cc
								.push('<div class="iApNo biblio-item">申请号：<a href="#">'
										+ rowData.attr1 + '</a></div>');
						cc.push('<div class="iAD biblio-item">申请日：<a href="#">'
								+ rowData.attr2 + '</a></div>');
						cc.push('<div class="iPN biblio-item">公开号：无 </div>');
						cc.push('<div class="iPD biblio-item">公开日：无</div>');
						cc.push('<div class="iGN biblio-item">公告号：<a href="#">'
								+ rowData.attr5 + '</a></div>');
						cc.push('<div class="iGP biblio-item">公告日：<a href="#">'
								+ rowData.attr6 + '</a></div></div>');
						cc.push('<div class="iIC biblio-item">主分类：<a href="#">'
								+ rowData.attr7 + '</a></div>');
						cc.push('<div id="clear"></div');
						cc
								.push('<div class="iPA biblio-item2">申请人：<a href="#">'
										+ rowData.attr8 + '</a></div>');
						cc
								.push('<div class="iIN biblio-item2">发明人：<a href="#">'
										+ rowData.attr9 + '</a></div>');
						cc.push('<div class="iAB biblio-item1">摘要：'
								+ rowData.attr10 + '</div></div></div>');
						cc.push('</td>');

						return cc.join('');
					}
				});
$(function() {
	$('#tt').datagrid({
		view : patent,
		showHeader : false,
		pagePosition : top
	});

});