<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>专利检索结果</title>
		<link href="/css/index.css" rel="stylesheet" type="text/css" />
		<link href="/css/new/style.css" rel="stylesheet" type="text/css" />
		<link href="/js/easyui/themes/icon.css" rel="stylesheet"
			type="text/css" />
		<link href="/js/easyui/themes/default/easyui.css" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
		<link href="/js/artdialog2/ui-dialog.css" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript" src="/js/artdialog2/dialog-min.js"></script>


		<script src="/js/jquery.easyui.minNew.js"></script>
		<script src="/js/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script src="/js/layer/layer.min.js"></script>
		<script type="text/javascript" src="/js/detail/tab1.js"></script>
		
		<script type="text/javascript" src="/js/simplePagination/jquery.simplePagination.js"></script>
		
		<link href="/js/simplePagination/simplePagination.css" type="text/css" rel="stylesheet">
		
		
		<link href="/css/new/style.css" type="text/css" rel="stylesheet">
		<link href="/css/new/B_SeachPage.css" rel="stylesheet" type="text/css" />
		<link href="/css/new/B_cprs2010.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="/css/new/jquery-ui.css" />
		<link rel="stylesheet" type="text/css" href="/css/new/tableNew.css" />
		
		<style type="text/css">
		
		/* 半透明的遮罩层 */
		#overlay {
		    background: #000;
		    filter: alpha(opacity=50); /* IE的透明度 */
		    opacity: 0.5;  /* 透明度 */
		    display: none;
		    position: absolute;
		    top: 0px;
		    left: 0px;
		    width: 100%;
		    height: 100%;
		    z-index: 100; /* 此处的图层要大于页面 */
		    display:none;
		}
		
		</style>
		
		<script>

		/* 显示遮罩层 */
		function showOverlay() {
		    $("#overlay").height(pageHeight());
		    $("#overlay").width(pageWidth());

		    // fadeTo第一个参数为速度，第二个为透明度
		    // 多重方式控制透明度，保证兼容性，但也带来修改麻烦的问题
		    $("#overlay").fadeTo(200, 0.5);
		}

		/* 隐藏覆盖层 */
		function hideOverlay() {
		    $("#overlay").fadeOut(200);
		}

		/* 当前页面高度 */
		function pageHeight() {
		    return document.body.scrollHeight;
		}

		/* 当前页面宽度 */
		function pageWidth() {
		    return document.body.scrollWidth;
		}
		
		
		var showtype=2;
		$(function(){
		  $("#hide").click(function(){
		  $("#handle_show").hide();
		  $("#handle_hid").show();
		  });
		  $("#show").click(function(){
		  $("#handle_show").show();
		  $("#handle_hid").hide();
		  });
		  $('#tw').click(function(){
			  showtype=1;
			  $("[name='ids']").removeAttr("checked");
			  $("#ids").removeAttr("checked");
				$(this).parent().addClass('zsfs').siblings().removeClass('zsfs');
			    $("#lb_show").hide();
		        $("#tw_show").show();
		        getPicTextList();	
		        
			  });
		  $('#lb').click(function(){
			  showtype=2;
			  $("[name='ids']").removeAttr("checked");
			  $("#ids").removeAttr("checked");
				$(this).parent().addClass('zsfs').siblings().removeClass('zsfs');
			    $("#tw_show").hide();
		        $("#lb_show").show();	
		        getTableList();
			  });
		});
		</script>

		<script type="text/javascript">

		 
		
		var allSelectedState=false;
		var allSelectedStateItem=false;
	//兼容getName方法
	function getElementsNameForIE(tag,name) {
	    var returns = document.getElementsByName(name);  
	    if(returns.length > 0) return returns;  
	    returns = new Array();  
	    var e = document.getElementsByTagName(tag);  
	    for(var i = 0; i < e.length; i++){  
	        if(e[i].getAttribute("name") == name){  
	            returns[returns.length] = e[i];  
	        }  
	    }  
	    return returns;  
	}
 	function changeId(){
		  var ids="";
		  var idsObject=getElementsNameForIE('input','ids');
		   for (i=0; i<idsObject.length; i++){
		                if (idsObject[i].type=="checkbox" && idsObject[i].checked){    
		                 ids+=idsObject[i].value+",";       
		                }
		        }   
		    document.getElementById('ids').value=ids.substring(0,ids.length-1);
    }
    function changeItem(){
	   var items="";
	   var itemsObject=getElementsNameForIE('input','items');
	   for (var j=0; j<itemsObject.length; j++){
	                if (itemsObject[j].type=="checkbox" && itemsObject[j].checked){    
	                 items+=itemsObject[j].value+",";       
	                }
	        }   
	    var itemTemp=0; 
       for (var j=0; j<itemsObject.length; j++){
      	  if (itemsObject[j].type=="checkbox" && itemsObject[j].checked){  
      	 	 itemTemp++;
      	 }
       }
      	  if(itemTemp==10){
      	  	  $("[name='itemsAll']").attr("checked",'true');
      	  	allSelectedStateItem=true;
      	  	  
      	  }else{
      	  	 $("[name='itemsAll']").removeAttr("checked");
      	  	allSelectedStateItem=false;
      	  }
	    document.getElementById('items').value=items.substring(0,items.length-1);
	}
function selectAll(){

	/*var ids="";
	 var idsObject= $("[name='ids']");
     if(document.getElementById("ids").checked==true){
             $("[name='ids']").attr("checked",'true');
             for (i=0; i<idsObject.length; i++){
				                if (idsObject[i].type=="checkbox"){    
				            	  ids+=idsObject[i].value+",";       
				                }
				        }  
			 document.getElementById('ids').value=ids.substring(0,ids.length-1);
			 document.getElementById("ids").checked=true;
            
     }else{
        $("[name='ids']").removeAttr("checked");
        document.getElementById('ids').value="";
        document.getElementById("ids").checked=false;
     }
     */
	
 	 var ids="";
	 var idsObject;
	 if(showtype==1){
		 idsObject= $(".idstype1");
	 }
	 else if(showtype==2){
		 idsObject= $(".idstype2");
	 }
      if(document.getElementById("ids").checked==true){
    	  if(showtype==1){
    		  $(".idstype1").attr("checked",'true');
    		 }
   		 else if(showtype==2){
   			 $(".idstype2").attr("checked",'true');
   		 }
              
              for (i=0; i<idsObject.length; i++){
				                if (idsObject[i].type=="checkbox"){    
				            	  ids+=idsObject[i].value+",";       
				                }
				        }  
			 document.getElementById('ids').value=ids.substring(0,ids.length-1);
			 document.getElementById("ids").checked=true;
             
      }else{
         $("[name='ids']").removeAttr("checked");
         document.getElementById('ids').value="";
         document.getElementById("ids").checked=false;
      }
}
function selectAllItem(){
 	 var items="";
	 var itemsObject= $("[name='items']");
      if(!allSelectedStateItem){
              $("[name='items']").attr("checked",'true');
            
              for (i=0; i<itemsObject.length; i++){
				                if (itemsObject[i].type=="checkbox"){    
				            	  items+=itemsObject[i].value+",";       
				                }
				        }  
			 document.getElementById('items').value=items.substring(0,items.length-1);
			 allSelectedStateItem=true;
      }else{
         $("[name='items']").removeAttr("checked");
         document.getElementById('items').value="";
         allSelectedStateItem=false;
      }
}
	function toexcel(idValue){
        document.getElementById('idValue').value=idValue;
	    $('#dlg6').dialog('open');
	}
  function toexcelWithSelectedItems(){
  		var ids=""
  		if(document.getElementById('idValue').value=="jly"){
  			ids=document.getElementById('ids').value;
  		}else{
  			ids= document.getElementById('idValue').value;
	  		document.getElementById('idValue').value="jly";
  		}
  		var items= document.getElementById('items').value;
  		window.open("/front/search/table_toExcel?searchscope=${searchscope}&appnos="+ids+"&items="+items);
		//window.location.href = "/front/search/table_toExcel?searchscope=${searchscope}&appnos="+ids+"&items="+items;
		
		$("[name='ids']").removeAttr("checked");
        document.getElementById('ids').value="";
        allSelectedState=false;
        
		$('#dlg6').dialog('close');
  }
		// 将发明人按，分开并生成单个超链接/
  function createInventor(inventors){
      var output = ""; 
      $.each(inventors,function(n,value) {    
             output += '<a  target="_blank"  href="/front/search/table_tableSearch?searchscope=${searchscope}&searchType=table&searchFormula=F IN '+value+'">'+ value + '</a>'+',';  
                     });  
       output=output.substr(0,output.length-1);
      return output;
  }  
// 将申请人按，分开并生成单个超链接/
     function createApplicant(applicants){
     //在前台将字符串切成数组
      var output = ""; 
      $.each(applicants,function(n,value) {    
             output += '<a target="_blank" href="/front/search/table_tableSearch?searchscope=${searchscope}&searchType=table&searchFormula=F PA '+value+'">'+ value + '</a>'+',';  
                     });  
        output=output.substr(0,output.length-1);
      return output;
  }  
  function getfutu(futupubnr,pudText){
      var futuUrl=null;
      	    $.ajax({  
                     type: "POST",  
                     url: '/front/search/table_getfutuurl',    
                      data:{"rowDatapubnr":futupubnr,"rowDatapudText":pudText},  
                     success: function(msg){  
                      var json= jQuery.parseJSON(msg);
                          futuUrl=json.urlStr;
                     }  
                 });
  		return futuUrl;
  }
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
  function downPdf(url,type){
	  $.layer({
          type: 2,
          title: 'pdf下载',
          maxmin: true,
          shadeClose: true, //开启点击遮罩关闭层
          area : ['220px' , '150px'],
          iframe: {src: '/front/search/table_pdfShowDiv?url='+url+'&type='+type,scrolling:'no'}
      });
  }
 /*
var patent = $
		.extend(
				{},
				$.fn.datagrid.defaults.view,
				{
					renderRow : function(target, fields, frozen, rowIndex,
							rowData) {
						var cc = [];
						if(rowData.patentType=='实用新型'||rowData.patentType=='发明'||rowData.patentType=='PCT发明专利申请'||rowData.patentType=='PCT实用新型专利申请'){
						
						cc.push('<td  style="padding:10px 5px;border:0;">');
						cc.push('<div class="title"> <input name="ids" type="checkbox" onchange="changeId()" value="'+rowData.appnoValue+'"/><a target="_blank" href="/front/search/table_patentDetailUI?cnDescriptionItem.appno='+rowData.appnoValue+'">'
								+ rowData.title + '(<font color=red>'+rowData.patentType +'</font>)</a><a target=_blank href=/front/search/legal_legalSearch?cnLegalStatus.shenQingH='+rowData.appnoValue+'><img src=/images/law/'+ rowData.legalStatus+'.jpg title=点击查看专利法律状态详情 /> </a> </div>');
						//cc.push('<div class="thumbnail"><img src="'+rowData.futuURL+'" style="position: relative; zoom: 100%; cursor: move;" ></div>');

						cc.push('<div class="thumbnail"><img class="resizePic" id="ImageFt'+rowIndex+'" src="'+rowData.futuURL+'" style="position: relative; zoom: 100%; cursor: move;"onload="resizeFt(this,'+"'"+"/1.gif"+"'"+')"  alt="摘要附图" /></div>');

						
						cc.push('<div class="details">');
						cc.push('<div class="note">');
						cc.push('<div class="notebase">');
						cc.push('<div class="iApNo biblio-item">申请号：<a target=_blank href="/front/search/table_patentDetailUI?cnDescriptionItem.appno='+rowData.appnoValue+'">'
								+ rowData.appnoWithDot+ '</a></div>');
						cc.push('<div class="iAD biblio-item">申请日：<a target=_blank href="/front/search/table_tableSearch?searchType=table&searchFormula=F AD '+rowData.apdValue+'">'
								+ rowData.apdText + '</a></div>');
					                  
						cc.push('<div class="iPN biblio-item">公开号：<a target=_blank href="/front/search/table_patentDetailUI?cnDescriptionItem.appno='+rowData.appnoValue+'">'
								+ rowData.pubnr+ '</a></div>');
						cc.push('<div class="iPD biblio-item">公开日：<a target=_blank href="/front/search/table_tableSearch?searchType=table&searchFormula=F PD '+rowData.pudValue+'">'
								+ rowData.pudText + '</a></div></br>');
						cc.push('<div class="iGN biblio-item">公告号：<a href="#">'
								+ rowData.appnr + '</a></div>');
						cc.push('<div class="iGP biblio-item">公告日：<a target=_blank href="/front/search/table_tableSearch?searchType=table&searchFormula=F GD '+rowData.appdValue+'">'
								+ rowData.appdText + '</a></div>');	 
						cc.push('<div class="iIC biblio-item2">IPC主分类：<a target=_blank href="/front/search/table_tableSearch?searchType=table&searchFormula=F MC '+rowData.ipcMainPara+'">'
								+ rowData.ipcMain + '</a></div>');
						cc.push('<div class="iPA biblio-item">申请人：'+createApplicant(rowData.appl)+'</div>');
						cc.push('<div class="iIN biblio-item2">发明人：'+createInventor(rowData.inventor)+'</div>');
						cc.push('<div class="iAB biblio-item1">摘要：'
								+ rowData.abstr + '</div>');
					    cc.push('<div class="command">');
						cc.push('<a  onClick="downPdf('+"'"+rowData.pdfURL + "'" +', '+ "'" + rowData.patentType +"'" + ' )" target=_blank  ><img src="/images/result/smallshoucang_bj.png" alt="">调取PDF文件</a>&nbsp;-&nbsp;');
						cc.push('<a href="javascript:void(0);" onclick=toexcel('+rowData.appnoValue+')><img src="/images/result/smalldaochu_bj.png" alt="">导出著录项(Excel)</a>&nbsp;-&nbsp;');
						cc.push('<a  target="_blank"  href="/front/search/table_tableSearch?searchType=table&searchFormula=F MC '+rowData.ipcMainPara+'"><img src="/images/result/smalltong_bj.png" alt="">同类专利对比</a>&nbsp;-&nbsp;');
						cc.push('<a href="javascript:void(0);" onClick="openDlg_new('+'\''+rowData.appnoValue+'\','+'\''+rowData.titleValue+'\','+'\''+rowData.apdValue+'\','+'\''+rowData.appl+'\')"><img src="/images/result/smallduibi_bj.png" alt="">加入企业库</a>');
						cc.push('</div>');		
					   cc.push('</div></div>');
						cc.push('</td>');
						}else  if(rowData.patentType=='外观设计'){
					 
						    cc.push('<td  style="padding:10px 5px;border:0;">');
						cc
								.push('<div class="title"> <input name="ids" type="checkbox" onchange="changeId()" value="'+rowData.appnoValue+'"/><a target="_blank" href="/front/search/table_patentDetailUI?cnDescriptionItem.appno='+rowData.appnoValue+'">'
								+ rowData.title + '(<font color=red>'+rowData.patentType +'</font>)</a><a target=_blank href=/front/search/legal_legalSearch?cnLegalStatus.shenQingH='+rowData.appnoValue+'><img src=/images/law/'+ rowData.legalStatus+'.jpg title=点击查看专利法律状态详情 /> </a> </div>');
						cc.push('<div class="thumbnail"><img class="resizePic" id="ImageFt" src="'+rowData.futuURL+'" style="position: relative; zoom: 100%; cursor: move;"onload="resizeFt(this,'+"'"+rowData.futuURL+"'"+')"  alt="摘要附图" /></div>');

						cc.push('<div class="details">');
						cc.push('<div class="note">');
						cc.push('<div class="notebase">');
						cc.push('<div class="iApNo biblio-item">申请号：<a  target="_blank"  href="/front/search/table_patentDetailUI?cnDescriptionItem.appno='+rowData.appnoValue+'">'
								+ rowData.appnoWithDot+ '</a></div>');
						cc.push('<div class="iAD biblio-item">申请日：<a target="_blank"  href="/front/search/table_tableSearch?searchType=table&searchFormula=F AD '+rowData.apdValue+'">'
								+ rowData.apdText + '</a></div>');
					 
						cc.push('<div class="iGN biblio-item">公告号：'
								+ rowData.appnr + '</div>');
						cc.push('<div class="iGP biblio-item">公告日：<a target="_blank"  href="/front/search/table_tableSearch?searchType=table&searchFormula=F GD '+rowData.appdValue+'">'
								+ rowData.appdText + '</a></div>');	 
						cc.push('<div class="iIC biblio-item">外观设计分类：<a target="_blank"  href="/front/search/table_tableSearch?searchType=table&searchFormula=F MC '+rowData.ipcMainPara+'">'
								+ rowData.ipcMain + '</a></div>');
						cc.push('<div class="iPA biblio-item">申请人：'+createApplicant(rowData.appl)+'</div>');
						cc.push('<div class="iIN biblio-item2">发明人：'+createInventor(rowData.inventor)+'</div>');
						cc.push('<div class="iAB biblio-item1">摘要：'
								+ rowData.abstr + '</div>');
					    cc.push('<div class="command">');
						//cc.push('<a href="'+rowData.pdfURL +'" target=_blank  ><img src="/images/result/smallshoucang_bj.png" alt="">调取PDF文件</a>&nbsp;-&nbsp;');
						cc.push('<a target="_blank"  href="javascript:void(0);" onclick=toexcel('+rowData.appnoValue+')><img src="/images/result/smalldaochu_bj.png" alt="">导出著录项(Excel)</a>&nbsp;-&nbsp;');
						cc.push('<a target="_blank"  href="/front/search/table_tableSearch?searchType=table&searchFormula=F MC '+rowData.ipcMainPara+'"><img src="/images/result/smalltong_bj.png" alt="">同类专利对比</a>&nbsp;-&nbsp;');
						cc.push('<a  href="javascript:void(0);" onClick="openDlg_new('+'\''+rowData.appnoValue+'\','+'\''+rowData.titleValue+'\')"><img src="/images/result/smallduibi_bj.png" alt="">加入企业库</a>');
						cc.push('</div>');
					    cc.push('</div></div>');
						cc.push('</td>');
						}else{
					 
						    cc.push('<td  style="padding:10px 5px;border:0;">');
						cc
								.push('<div class="title"> <input name="ids" type="checkbox" onchange="changeId()" value="'+rowData.pubnr+'"/><a target="_blank" href="/front/search/table_patentDetailUI?searchscope='+rowData.searchscope+'&enDescriptionItem.pubnr='+rowData.pubnr+'">'
								+ rowData.title + '</a> </div>');
						cc.push('<div class="thumbnail"><img class="resizePic" id="ImageFt" src="'+rowData.futuURL+'" style="position: relative; zoom: 100%; cursor: move;"onload="resizeFt(this,'+"'"+rowData.futuURL+"'"+')"  alt="摘要附图" /></div>');

						cc.push('<div class="details">');
						cc.push('<div class="note">');
						cc.push('<div class="notebase">');
						cc.push('<div class="iApNo biblio-item">申请号：'
								+ rowData.appnoValue+ '</a></div>');
						cc.push('<div class="iAD biblio-item">申请日：<a target="_blank"  href="/front/search/table_tableSearch?searchscope=DocDB&searchType=table&searchFormula=F AD '+rowData.apdValue+'">'
								+ rowData.apdText + '</a></div>');
					 
						cc.push('<div class="iGN biblio-item">公开(公告)号：<a target="_blank"  href="/front/search/table_patentDetailUI?searchscope=DocDB&enDescriptionItem.pubnr='+rowData.pubnr+'">'
								+ rowData.pubnr + '</a></div>');
						cc.push('<div class="iGP biblio-item">公开(公告)日：<a target="_blank"  href="/front/search/table_tableSearch?searchscope=DocDB&searchType=table&searchFormula=F PD '+rowData.pudValue+'">'
								+ rowData.pudText + '</a></div><br/>');	 
						cc.push('<div class="iIC biblio-item">分类号：<a target="_blank"  href="/front/search/table_tableSearch?searchscope=DocDB&searchType=table&searchFormula=F MC '+rowData.interIpcFormula+'">'
								+ rowData.interIpc + '</a></div>');
						cc.push('<div class="iPA biblio-item">申请人：'+createApplicant(rowData.appl)+'</div><br/>');
						cc.push('<div class="iIN biblio-item2">发明人：'+createInventor(rowData.inventor)+'</div>');
						cc.push('<div class="iAB biblio-item1">摘要：'
								+ rowData.abstr + '</div>');
					    cc.push('<div class="command">');
						cc.push('<a onClick="downPdf()"  target=_blank  ><img src="/images/result/smallshoucang_bj.png" alt="">调取PDF文件</a>&nbsp;-&nbsp;');
						cc.push('<a target="_blank"  href="javascript:void(0);" onclick=toexcel("'+rowData.pubnr+'")><img src="/images/result/smalldaochu_bj.png" alt="">导出著录项(Excel)</a>&nbsp;-&nbsp;');
						cc.push('<a target="_blank"  href="/front/search/table_tableSearch?searchscope=DocDB&searchType=table&searchFormula=F MC '+rowData.interIpcFormula+'"><img src="/images/result/smalltong_bj.png" alt="">同类专利对比</a>&nbsp;-&nbsp;');
						cc.push('<a href="javascript:void(0);" onClick="openDlg_new('+'\''+rowData.pubnr+'\','+'\''+rowData.titleValue+'\','+'\''+rowData.apdValue+'\','+'\''+rowData.appl+'\')"><img src="/images/result/smallduibi_bj.png" alt="">加入企业库</a>');
						cc.push('</div>');
					    cc.push('</div></div>');
						cc.push('</td>');
						}

						return cc.join('');
					}
				});

*/

function getPicTextList(){
	$.ajax({
		beforeSend:function(){
			showOverlay();
		},
		complete:function(XMLHttpRequest,textStatus){
			hideOverlay();
		},
		type:'post',
		data:{
			patentType:"'"+$("#patentType").val()+"'",
			ipcMType:"'"+$("#ipcMType").val()+"'",
			searchFormulaOld:'${searchFormulaOld}',
			searchType:'${searchType}',
			searchFormula:'${searchFormula}',
			page:${page},
			searchscope:'${searchscope}',
			
			keyword:'${keyword}',
			title:'${cnDescriptionItem.title}',
			titleEn:'${enDescriptionItem.title}',
			abstr:'${cnDescriptionItem.abstr}',
			abstrEn:'${enDescriptionItem.abs}',
			claim:'${cnDescriptionItem.claim}',
			appl:'${cnDescriptionItem.appl}',
			applEn:'${enDescriptionItem.appl}',
			ipcMain:'',
			ipcMainEn:'',
			appno:'${cnDescriptionItem.appno}',
			appnoEn:'${enDescriptionItem.appno}',
			apd:'${cnDescriptionItem.apdText}',
			apdEn:'${enDescriptionItem.apdText}',
			pubnr:'${cnDescriptionItem.pubnr}',
			pubnrEn:'${enDescriptionItem.pubnr}',
			pud:'${cnDescriptionItem.pudText}',
			pudEn:'${enDescriptionItem.pudText}',
			appnr:'${cnDescriptionItem.appnr}',
			appd:'${cnDescriptionItem.appdText}',
			inventor:'${cnDescriptionItem.inventor}',
			inventorEn:'${enDescriptionItem.inventor}',
			fieldc:'${cnDescriptionItem.fieldc}',
			address:'${cnDescriptionItem.address}',
			nc:'${cnDescriptionItem.nc}',
			agency:'${cnDescriptionItem.agency}',
			agent:'${cnDescriptionItem.agent}',
			claim:'${cnDescriptionItem.claim}',
			
		},
		url:'/front/search/table_getSearchResultList',
		success:function(data){
			var temp = JSON.parse(data);
			var rows = temp.rows;
			//alert(rows);
			var html='';
			for(var  i=0; i<rows.length;i++){
				if(rows[i].patentType=='实用新型'||rows[i].patentType=='发明'||rows[i].patentType=='PCT发明专利申请'||rows[i].patentType=='PCT实用新型专利申请'){
					html+='<div class="teletextzs" >';
					html+='<div class="patent_img"><img src="'+rows[i].futuURL+'"></div>';
					html+='<div class="patent_info">';
					html+='<h1>';
					html+=' <input name="ids" class="idstype1" type="checkbox" onchange="changeId()" value="'+rows[i].appno+'"/>'+ rows[i].patentType +'&nbsp;';
					html+=' <span><a  target="_blank" href="/front/search/table_patentDetailUI?cnDescriptionItem.appno='+ rows[i].appno +'">'+rows[i].title+'</a></span> &nbsp;&nbsp;';
					html+='<font class="yxzl">'+ rows[i].cnLegalStatusStr +'</font></h1>';
					html+=' <h2>申请号：<a  target="_blank" href="/front/search/table_patentDetailUI?cnDescriptionItem.appno='+ rows[i].appno +'">'+ rows[i].appnoWithDot +'</a>&nbsp;&nbsp;';
					html+='申请日：<a  target="_blank"  href="/front/search/table_tableSearch?searchType=table&searchFormula=F AD '+ rows[i].apdValue +'">'+ rows[i].apdText +'&nbsp;</a>&nbsp;';
					html+='公开号：<a  target="_blank" href="/front/search/table_patentDetailUI?cnDescriptionItem.appno='+ rows[i].appno +'">'+ rows[i].pubnr +'</a>&nbsp;&nbsp;';
					html+='公开日：<a  target="_blank" href="/front/search/table_tableSearch?searchType=table&searchFormula=F PD '+ rows[i].pudValue +'">'+ rows[i].pudText +'</a><br>';
					html+='公告号：<a  target="_blank" href="/front/search/table_patentDetailUI?cnDescriptionItem.appno='+ rows[i].appno +'">'+ rows[i].appnr +'</a>&nbsp;&nbsp;';
					html+='	公告日：';
					if(rows[i].appdText=='无'){
						html+='<a href="javascript:void(0)">'+ rows[i].appdText +'</a> &nbsp;&nbsp;';
					}
					if(rows[i].appdText!='无'){
						html+='<a  target="_blank" href="/front/search/table_tableSearch?searchType=table&searchFormula=F GD '+ rows[i].appdValue +'">'+  rows[i].appdText +'</a>&nbsp;&nbsp;';
					}
					html+='IPC主分类：<a  target="_blank" href="/front/search/table_tableSearch?searchType=table&searchFormula=F MC '+ rows[i].ipcMainPara +'">'+ rows[i].ipcMain +'</a><br>';
					html+='申请人：<a  target="_blank" href="/front/search/table_tableSearch?searchType=table&searchFormula=F PA '+ rows[i].appl +'">'+ rows[i].appl +'</a>&nbsp;&nbsp;';
					html+='发明人：<a href="#">'+ rows[i].inventor +'</a></h2>';
					html+='<h3>';
					html+='摘要：'+ rows[i].abstr ;
					html+='</h3>';
					html+='</div>';
					html+='</div>';
				}
				else if(rows[i].patentType=='外观设计'){
					html+='<div class="teletextzs" >';
					html+='<div class="patent_img"><img src="'+rows[i].futuURL+'"></div>';
					html+='<div class="patent_info">';
					html+='<h1>';
					html+=' <input name="ids" class="idstype1" type="checkbox" onchange="changeId()" value="'+rows[i].appno+'"/>'+ rows[i].patentType +'&nbsp;';
					html+=' <span><a  target="_blank" href="/front/search/table_patentDetailUI?cnDescriptionItem.appno='+ rows[i].appno +'">'+rows[i].title+'</a></span> &nbsp;&nbsp;';
					html+='<font class="yxzl">'+ rows[i].cnLegalStatusStr +'</font></h1>';
					html+=' <h2>申请号：<a  target="_blank" href="/front/search/table_patentDetailUI?cnDescriptionItem.appno='+ rows[i].appno +'">'+ rows[i].appnoWithDot +'</a>&nbsp;&nbsp;';
					html+='申请日：<a  target="_blank"  href="/front/search/table_tableSearch?searchType=table&searchFormula=F AD '+ rows[i].apdValue +'">'+ rows[i].apdText +'&nbsp;</a>&nbsp;';
					html+='公开号：<a  target="_blank" href="/front/search/table_patentDetailUI?cnDescriptionItem.appno='+ rows[i].appno +'">'+ rows[i].pubnr +'</a>&nbsp;&nbsp;';
					html+='公开日：<a  target="_blank" href="/front/search/table_tableSearch?searchType=table&searchFormula=F PD '+ rows[i].pudValue +'">'+ rows[i].pudText +'</a><br>';
					html+='公告号：<a  target="_blank" href="/front/search/table_patentDetailUI?cnDescriptionItem.appno='+ rows[i].appno +'">'+ rows[i].appnr +'</a>&nbsp;&nbsp;';
					html+='	公告日：';
					if(rows[i].appdText=='无'){
						html+='<a href="javascript:void(0)">'+ rows[i].appdText +'</a> &nbsp;&nbsp;';
					}
					if(rows[i].appdText!='无'){
						html+='<a  target="_blank" href="/front/search/table_tableSearch?searchType=table&searchFormula=F GD '+ rows[i].appdValue +'">'+  rows[i].appdText +'</a>&nbsp;&nbsp;';
					}
					html+='外观设计分类：<a target="_blank"  href="/front/search/table_tableSearch?searchType=table&searchFormula=F MC '+rows[i].ipcMainPara+'">'+ rows[i].ipcMain + '</a><br>';
					html+='申请人：<a  target="_blank" href="/front/search/table_tableSearch?searchType=table&searchFormula=F PA '+ rows[i].appl +'">'+ rows[i].appl +'</a>&nbsp;&nbsp;';
					html+='发明人：<a href="#">'+ rows[i].inventor +'</a></h2>';
					html+='<h3>';
					html+='摘要：'+ rows[i].abstr ;
					html+='</h3>';
					html+='</div>';
					html+='</div>';
				}
				else{
					html+='<div class="teletextzs" >';
					html+='<div class="patent_img"><img src="'+ rows[i].futuURL +'"></div>';
					html+='<div class="patent_info">';
					html+='<h1>';
					html+='<input name="ids" class="idstype1" type="checkbox" onchange="changeId()" value="'+ rows[i].pubnr +'"/>&nbsp;';
					html+='<span><a  target="_blank" href="javascript:void(0)">'+ rows[i].title +'</a></span> &nbsp;&nbsp;';
					html+='<font class="yxzl"></font></h1>';
					html+='<h2>申请号：'+ rows[i].appno +'&nbsp;&nbsp;';
					html+='申请日：<a  target="_blank"  href="javascript:void(0)">'+ rows[i].apdText +'&nbsp;</a>&nbsp;';
					html+='公开号：<a  target="_blank" href="javascript:void(0)">'+ rows[i].pubnr +'</a>&nbsp;&nbsp;';
					html+='公开日：<a  target="_blank" href="javascript:void(0)">'+  rows[i].pudText+'</a><br>';
					html+='IPC主分类：<a  target="_blank" href="javascript:void(0)">'+ rows[i].interIpc +'</a><br>';
					html+='申请人：'+ rows[i].appl +'&nbsp;&nbsp;';
					html+='发明人：<a href="javascript:void(0)">'+ rows[i].inventor +'</a></h2>';
					html+='<h3>';
					html+='摘要：'+rows[i].abs;
					html+='</h3>';
					html+='</div>';
					html+='</div>';
					
				}
				
			}
			$("#tw_show").html(html);
		}
	});
}

function getTableList(){
	$.ajax({
		type:'post',
		beforeSend:function(){
			showOverlay();
		},
		complete:function(XMLHttpRequest,textStatus){
			hideOverlay();
		},
		data:{
			patentType:"'"+$("#patentType").val()+"'",
			ipcMType:"'"+$("#ipcMType").val()+"'",
			searchFormulaOld:'${searchFormulaOld}',
			searchType:'${searchType}',
			searchscope:'${searchscope}',
			searchFormula:'${searchFormula}',
			page:${page},

			keyword:'${keyword}',
			title:'${cnDescriptionItem.title}',
			titleEn:'${enDescriptionItem.title}',
			abstr:'${cnDescriptionItem.abstr}',
			abstrEn:'${enDescriptionItem.abs}',
			claim:'${cnDescriptionItem.claim}',
			appl:'${cnDescriptionItem.appl}',
			applEn:'${enDescriptionItem.appl}',
			ipcMain:'',
			ipcMainEn:'',
			appno:'${cnDescriptionItem.appno}',
			appnoEn:'${enDescriptionItem.appno}',
			apd:'${cnDescriptionItem.apdText}',
			apdEn:'${enDescriptionItem.apdText}',
			pubnr:'${cnDescriptionItem.pubnr}',
			pubnrEn:'${enDescriptionItem.pubnr}',
			pud:'${cnDescriptionItem.pudText}',
			pudEn:'${enDescriptionItem.pudText}',
			appnr:'${cnDescriptionItem.appnr}',
			appd:'${cnDescriptionItem.appdText}',
			inventor:'${cnDescriptionItem.inventor}',
			inventorEn:'${enDescriptionItem.inventor}',
			fieldc:'${cnDescriptionItem.fieldc}',
			address:'${cnDescriptionItem.address}',
			nc:'${cnDescriptionItem.nc}',
			agency:'${cnDescriptionItem.agency}',
			agent:'${cnDescriptionItem.agent}',
			claim:'${cnDescriptionItem.claim}',
		},
		url:'/front/search/table_getSearchResultList',
		success:function(data){
			var temp = JSON.parse(data);
			var rows = temp.rows;
			var html='';
			for(var  i=0; i<rows.length;i++){
				if(rows[i].patentType=='实用新型'||rows[i].patentType=='发明'||rows[i].patentType=='PCT发明专利申请'||rows[i].patentType=='PCT实用新型专利申请'||rows[i].patentType=='外观设计'){
					html+='<ul>';
					html+='<li style="width:30px;"><input name="ids" class="idstype2" type="checkbox" onchange="changeId()" value="'+ rows[i].appno +'"/></li>';
					html+='<li style="width:400px;overflow:hidden"><a  target="_blank" href="/front/search/table_patentDetailUI?cnDescriptionItem.appno='+ rows[i].appno +'">'+ rows[i].title +'</a></li>';
					html+='<li style="width:150px;"><a  target="_blank" href="/front/search/table_patentDetailUI?cnDescriptionItem.appno='+ rows[i].appno +'">'+ rows[i].appnoWithDot +'</a></li>';
					html+='<li style="width:100px;"><a  target="_blank"  href="/front/search/table_tableSearch?searchType=table&searchFormula=F AD '+ rows[i].apdValue +'">'+ rows[i].apdText +'&nbsp;</a></li>';
					html+='<li style="width:88px;">'+ rows[i].cnLegalStatusStr +'</li>';
					html+='<li style="width:150px;"><a  target="_blank" href="/front/search/table_tableSearch?searchType=table&searchFormula=F MC '+ rows[i].ipcMainPara +'">'+ rows[i].ipcMain +'</a></li>';
					html+='</ul>';
				}
				else{
					html+='<ul>';
					html+='<li style="width:30px;"><input name="ids" class="idstype2" type="checkbox" onchange="changeId()" value="'+ rows[i].appno +'"/></li>';
					html+='<li style="width:400px;overflow:hidden"><a  target="_blank" href="/front/search/table_patentDetailUI?cnDescriptionItem.appno='+ rows[i].appno +'">'+ rows[i].title +'</a></li>';
					html+='<li style="width:150px;"><a  target="_blank" href="/front/search/table_patentDetailUI?cnDescriptionItem.appno='+ rows[i].appno +'">'+ rows[i].appno +'</a></li>';
					html+='<li style="width:100px;"><a  target="_blank"  href="/front/search/table_tableSearch?searchType=table&searchFormula=F AD '+ rows[i].apdText +'">'+ rows[i].apdText +'&nbsp;</a></li>';
					html+='<li style="width:88px;">'+ '无' +'</li>';
					html+='<li style="width:150px;"><a  target="_blank" href="/front/search/table_tableSearch?searchType=table&searchFormula=F MC '+ rows[i].ipcMainPara +'">'+ rows[i].interIpc +'</a></li>';
					html+='</ul>';
				}
			}
			$("#listtab").html(html);
		}
	})
}

$(function() {
	getTableList();

	//ajax获取各类型专利的数目
	/**
	$.ajax({ 
		type:"post",
		url:"table_getPatentTypeNum",
		data:{
			searchFormulaPatentTypeNum:'${searchFormulaOld}'
		},
		success:function(data){
			var num = data.split("_");
			$("#fmnum").html("("+num[0]+")");
			$("#xxnum").html("("+num[1]+")");
			$("#wgnum").html("("+num[2]+")");
		},
		error:function(){

		}
			
	});

	$.ajax({ 
		type:"post",
		url:"table_getPatentIPCTypeNum",
		data:{
			searchFormulaPatentTypeNum:'${searchFormulaOld}'
		},
		success:function(data){
			var num = data.split("_");
			$("#anum").html("("+num[0]+")");
			$("#bnum").html("("+num[1]+")");
			$("#cnum").html("("+num[2]+")");
			$("#dnum").html("("+num[3]+")");
			$("#enum").html("("+num[4]+")");
			$("#fnum").html("("+num[5]+")");
			$("#gnum").html("("+num[6]+")");
			$("#hnum").html("("+num[7]+")");
		},
		error:function(){

		}
			
	});
	*/

	$("div.holder").pagination({ 
        items: ${total},
        itemsOnPage: 20,
        cssStyle: 'light-theme',
        currentPage:${page},
        onPageClick:function(pageNumber, event){
            var temp='${searchFormula}';
            temp=temp.replace(/\+/g,"_");
        	if($("#patentType").val()!=""){
				  window.location="/front/search/table_tableSearch?patentType="+ $("#patentType").val() +"&ipcMType="+  $("#ipcMType").val()  +"&page="+pageNumber+"&searchFormulaOld=${searchFormulaOld}&searchscope=${searchscope}&searchType=table&searchFormula="+temp;
					
			  }
			  else{
				  window.location="/front/search/table_tableSearch?patentType="+ $("#patentType").val() +"&ipcMType="+  $("#ipcMType").val() +"&page="+pageNumber+"&searchFormulaOld=${searchFormulaOld}&searchscope=${searchscope}&searchType=table&searchFormula="+temp;
					
			  }
			  
        }
    })

	var patentTypeList = '${patentType}'.split("_");
	var ipcMTypeList = '${ipcMType}'.split("_");
	var patentTypeStr = '${patentType}';
	var ipcMTypeStr = '${ipcMType}';
	var strs = "";
	/**
	if( '${patentType}'!=""){
		for(i=0;i<patentTypeList.length;i++){
			//document.write(patentTypeList[i]+"<br />");
			$("#optionlist").append("<span>"+patentTypeList[i]+"</span><a href='javascript:delOptions(\"1\",\"" + patentTypeList[i]+"\")'>删除</a>&nbsp");
			
			strs = strs+patentTypeList[i];
		}
	}
	if('${ipcMType}'!=""){
		for(j=0;j<ipcMTypeList.length;j++){
			//document.write(ipcMTypeList[j]+"<br />");
			$("#optionlist").append("<span>"+ipcMTypeList[j]+"</span><a href='javascript:delOptions(\"4\",\"" + ipcMTypeList[j]+"\")'>删除</a>&nbsp");
			strs = strs+ipcMTypeList[j];
		}
	}
	*/

	if(patentTypeStr.indexOf("FM")!=-1){
		$("#fmck").attr("checked","checked");
	}
	if(patentTypeStr.indexOf("XX")!=-1){
		$("#xxck").attr("checked","checked");
	}
	if(patentTypeStr.indexOf("WG")!=-1){
		$("#wgck").attr("checked","checked");
	}

	if(ipcMTypeStr.indexOf("A/IC")!=-1){
		$("#ack").attr("checked","checked");
	}
	if(ipcMTypeStr.indexOf("B/IC")!=-1){
		$("#bck").attr("checked","checked");
	}
	if(ipcMTypeStr.indexOf("C/IC")!=-1){
		$("#cck").attr("checked","checked");
	}

	if(ipcMTypeStr.indexOf("D/IC")!=-1){
		$("#dck").attr("checked","checked");
	}
	if(ipcMTypeStr.indexOf("E/IC")!=-1){
		$("#eck").attr("checked","checked");
	}
	if(ipcMTypeStr.indexOf("F/IC")!=-1){
		$("#fck").attr("checked","checked");
	}

	if(ipcMTypeStr.indexOf("G/IC")!=-1){
		$("#gck").attr("checked","checked");
	}
	if(ipcMTypeStr.indexOf("H/IC")!=-1){
		$("#hck").attr("checked","checked");
	}
	


});
function addToPersonChoose(){
	var ids= document.getElementById('ids').value;
	if(ids=='on'||ids==''){
	 $.messager.alert('专利选择提示','请在批量加入企业个性库操作之前，完成对专利的选择!','信息');
	}else{
			
	 		$('#dlg3').dialog('open');
	// window.location.href = "/front/search/table_addPatentIn?appnos="+ids;
	}
}

function addToPersonAll(){
var ids2="";
	
	$.ajax({
		url:'/front/search/table_getSearchResult',
		data:{
			searchFormula: '${searchFormula}',
			searchType: '${searchType}',
			keyword:'${keyword}',
			title:'${cnDescriptionItem.title}',
			titleEn:'${enDescriptionItem.title}',
			abstr:'${cnDescriptionItem.abstr}',
			abstrEn:'${enDescriptionItem.abs}',
			claim:'${cnDescriptionItem.claim}',
			appl:'${cnDescriptionItem.appl}',
			applEn:'${enDescriptionItem.appl}',
			ipcMain:'',
			ipcMainEn:'',
			appno:'${cnDescriptionItem.appno}',
			appnoEn:'${enDescriptionItem.appno}',
			apd:'${cnDescriptionItem.apdText}',
			apdEn:'${enDescriptionItem.apdText}',
			pubnr:'${cnDescriptionItem.pubnr}',
			pubnrEn:'${enDescriptionItem.pubnr}',
			pud:'${cnDescriptionItem.pudText}',
			pudEn:'${enDescriptionItem.pudText}',
			appnr:'${cnDescriptionItem.appnr}',
			appd:'${cnDescriptionItem.appdText}',
			inventor:'${cnDescriptionItem.inventor}',
			inventorEn:'${enDescriptionItem.inventor}',
			fieldc:'${cnDescriptionItem.fieldc}',
			address:'${cnDescriptionItem.address}',
			nc:'${cnDescriptionItem.nc}',
			agency:'${cnDescriptionItem.agency}',
			agent:'${cnDescriptionItem.agent}',
			claim:'${cnDescriptionItem.claim}',
			searchscope:'${searchscope}',
			searchFormulaId:'${SearchFormulaEntity.id}',
			rows:${total },
			page:1
		},
		type:'post',
		dataType:'text',
		success:function(data){
			
			data=JSON.parse(data);
			var rows=data.rows;
			if(rows==0){
				$.messager.show({
					title:'检索结果提示',
					msg:'很抱歉，对于您输入的检索条件，系统为您检索到0条记录！',
					timeout:80000,
					showType:'slide',
					style:{
						right:'',
						top:document.body.scrollTop+document.documentElement.scrollTop,
						bottom:''
					}
				});
				return false;
			}
			for(var i=0;i<rows.length;i++){
				if(ids2==""){
					ids2=ids2+rows[i].appno;
				}
				else{
					ids2=ids2+","+rows[i].appno;
				}
				
			}
			$("#ids").val(ids2);
			addToPersonChoose();
			//alert(ids2);
			
		},
		error:function(data){

		}
	});
}
function downChoosePDF(){
	var ids= document.getElementById('ids').value;
	if(ids=='on'||ids==''){
	 $.messager.alert('专利选择提示','请在专利全文批量下载(PDF)操作之前，完成对专利的选择!','信息');
	}else{
	 	window.open( "/front/search/table_batchDownloadPdfs?appnos="+ids);
	}
}
function downAllPDF(){
	var ids2="";
	var num=0;
	if(${user.level}==2){
		num=50;
	}
	if(${user.level}==3){
		num=200;
	}
	if(${user.level}==4){
		num=200;
	}
	if(window.confirm("您一天最多下载"+num+"个，超出部分将无法下载")){
		
	}
	else{
		return false;
	}
	$.ajax({
		url:'/front/search/table_getSearchResult',
		async:true,
		data:{
			searchFormula: '${searchFormula}',
			searchType: '${searchType}',
			keyword:'${keyword}',
			title:'${cnDescriptionItem.title}',
			titleEn:'${enDescriptionItem.title}',
			abstr:'${cnDescriptionItem.abstr}',
			abstrEn:'${enDescriptionItem.abs}',
			claim:'${cnDescriptionItem.claim}',
			appl:'${cnDescriptionItem.appl}',
			applEn:'${enDescriptionItem.appl}',
			ipcMain:'',
			ipcMainEn:'',
			appno:'${cnDescriptionItem.appno}',
			appnoEn:'${enDescriptionItem.appno}',
			apd:'${cnDescriptionItem.apdText}',
			apdEn:'${enDescriptionItem.apdText}',
			pubnr:'${cnDescriptionItem.pubnr}',
			pubnrEn:'${enDescriptionItem.pubnr}',
			pud:'${cnDescriptionItem.pudText}',
			pudEn:'${enDescriptionItem.pudText}',
			appnr:'${cnDescriptionItem.appnr}',
			appd:'${cnDescriptionItem.appdText}',
			inventor:'${cnDescriptionItem.inventor}',
			inventorEn:'${enDescriptionItem.inventor}',
			fieldc:'${cnDescriptionItem.fieldc}',
			address:'${cnDescriptionItem.address}',
			nc:'${cnDescriptionItem.nc}',
			agency:'${cnDescriptionItem.agency}',
			agent:'${cnDescriptionItem.agent}',
			claim:'${cnDescriptionItem.claim}',
			searchscope:'${searchscope}',
			searchFormulaId:'${SearchFormulaEntity.id}',
			rows:${total },
			page:1
		},
		type:'post',
		dataType:'text',
		success:function(data){
			
			data=JSON.parse(data);
			var rows=data.rows;
			if(rows==0){
				$.messager.show({
					title:'检索结果提示',
					msg:'很抱歉，对于您输入的检索条件，系统为您检索到0条记录！',
					timeout:8000,
					showType:'slide',
					style:{
						right:'',
						top:document.body.scrollTop+document.documentElement.scrollTop,
						bottom:''
					}
				});
			}
				for(var i=0;i<rows.length;i++){
					if(ids2==""){
						ids2=ids2+rows[i].appno;
					}
					else{
						ids2=ids2+","+rows[i].appno;
					}
					
				}
				window.open("/front/search/table_batchDownloadPdfs?appnos="+ids2);
		
			
			//alert(ids2);
			
		},
		error:function(data){

		}
	});
	 
}

function _loadSuccess(data){
	tatal=data.total;
	if(data.total==0)
	  $.messager.show({
	title:'检索结果提示',
	msg:'很抱歉，对于您输入的检索条件，系统为您检索到0条记录！',
	timeout:8000,
	showType:'slide',
	style:{
		right:'',
		top:document.body.scrollTop+document.documentElement.scrollTop,
		bottom:''
	}
});
}

function _loadError( ){
	  
	  $.messager.show({
	title:'检索结果提示',
	msg:'很抱歉，对于您输入的检索条件，系统为您检索到0条记录！',
	timeout:8000,
	showType:'slide',
	style:{
		right:'',
		top:document.body.scrollTop+document.documentElement.scrollTop,
		bottom:''
	}
});
}
// 返回所选择检索专利类型字符串
function getPatentTypeStr() {
	var patentTypeStr = "@LX=";
	var patenttype_fm = document.getElementById("patenttype_fm").checked;
	var patenttype_wg = document.getElementById("patenttype_wg").checked;
	var patenttype_xx = document.getElementById("patenttype_xx").checked;
	if (patenttype_fm) {
		patentTypeStr = patentTypeStr + "FM,";
	}
	if (patenttype_wg) {
		patentTypeStr = patentTypeStr + "WG,";
	}
	if (patenttype_xx) {
		patentTypeStr = patentTypeStr + "XX,";
	}
	if (patentTypeStr.length > 4) {
		patentTypeStr = patentTypeStr.substring(0, patentTypeStr.length - 1);

		return patentTypeStr;
	} else {
		return "";
	}
}
//////////////////////////////////////////////////////////////
 
       function confimOrnot(){  
           deleteNode();
           $('#dlg5').dialog('close');
       }
       function confirmPWD(){  
            $('#dlg5').dialog('open');
       }
//////////////////////////////////////////////////////////////

 	   function deleteNode(){  
           var  categorytype =$("#categorytype").val();
          	if(categorytype==0){	
          		  var selected = $('#n_tt0').tree('getSelected');  
            }else if(categorytype==1){
          		  var selected = $('#n_tt1').tree('getSelected');  
            }
              var text = document.getElementById("nodename").value;
             if (selected){  
	                 $.ajax({  
	                     type: "POST",  
	                     url: '/front/user/category_deleteTreeNode',  
	                     data:{"id":selected.id,"text":text},  
	                   	  success: function(msg){  
                    		 var json= jQuery.parseJSON(msg);
                           	 if(json.result=="success"){
                           	 	if(categorytype==0){	
                           	 	   $('#n_tt0').tree('remove',selected.target);	
					            }else if(categorytype==1){
                           	 	   $('#n_tt1').tree('remove',selected.target);	
          					    }
                           	 }
                           	 	 alert(json.msg);
                    	 }  
	                 });
	            }else{  
	                alert("请选择要删除的节点");  
	            } 
        } 
       function addNode(){  
            var  categorytype =$("#categorytype").val();
          	if(categorytype==0){	
          		  var selected = $('#n_tt0').tree('getSelected');  
            }else if(categorytype==1){
          		  var selected = $('#n_tt1').tree('getSelected');  
            }
            var text = document.getElementById("nodename").value;
             if (selected){  
                 $.ajax({  
                     type: "POST",  
                     url: '/front/user/category_addTreeNode',  
                     data:{"id":selected.id,"text":text,"categorytype":categorytype},  
                     success: function(msg){  
                        var json= jQuery.parseJSON(msg);
                        	// alert(json.msg);
                          	if(categorytype==0){	
		                          	$('#n_tt0').tree('append', {
										parent: selected.target,
										data: [{
											text: text
										}]
									}); 
							}else if(categorytype==1){
								$('#n_tt1').tree('append', {
										parent: selected.target,
										data: [{
											text: text
										}]
									}); 
							}
							//location.reload();				
                     }  
                 });  
            
                         	
        }else{  
            alert("请选择要添加节点的位置！");  
        } 
             $('#dlg2').dialog('close');
    }    
      function  openDlg_new(appnoValue,title,apdValue,appl){
    	 // alert("000");
          	$("#_appno").val(appnoValue);
          	$("#_title").val(title);
          	$("#_apd").val(apdValue);
          	$("#_appl").val(appl);
			 $('#dlg3').dialog('open');
		}
	    function openDlg(categorytype){
	    		$("#categorytype").val(categorytype);
	    		if($("#categorytype").val()==0){
	    		    $("#n_tt0").tree({  
			            url:'/front/user/category_getTreeJsonAjax0',
			            dnd:true,  
			            animate: true    
			        }); 
	    		  $('#dlg0').dialog('open');
	    		}else if($("#categorytype").val()==1){
	    		      $("#n_tt1").tree({  
				            url:'/front/user/category_getTreeJsonAjax1',
				            dnd:true,
				            animate: true    
				        });  
	    		  $('#dlg1').dialog('open');
	    		}
	    		  $('#dlg3').dialog('close');
	    		
	    }
		 function addPatentIn(){
			  var appnoValue=document.getElementById("_appno").value;
			  var ids=document.getElementById("ids").value;
			  var title=document.getElementById("_title").value;
			  var apd=document.getElementById("_apd").value;
			  var appl=document.getElementById("_appl").value;
			  var text = document.getElementById("nodename").value;
			   var  categorytype =$("#categorytype").val();
          	if(categorytype==0){	
          		  var selected = $('#n_tt0').tree('getSelected');  
            }else if(categorytype==1){
          		  var selected = $('#n_tt1').tree('getSelected');  
            }
			  
				$("#_appno").val("");
		   if (appnoValue){ 
                 $.ajax({  
                     type: "POST",  
                     url: '/front/user/category_addPatentIn',  
                     data:{"appnoValue":appnoValue,"id":selected.id,"text":text,"title":title,"apd":apd,"appl":appl,"searchscope":"${searchscope}"},  
                     success: function(msg){  
                          var json= jQuery.parseJSON(msg);
                      		 alert(json.msg);
                     }  
                 }); 
            }else if(ids){
            	$.ajax({  
                     type: "POST",  
                     beforeSend:function(){
	        			showOverlay();
	        		},
	        		complete:function(XMLHttpRequest,textStatus){
	        			hideOverlay();
	        		},
                     url: '/front/search/table_addPatentIn',  
                     data:{"appnos":ids,"id":selected.id,"text":text,"searchscope":"${searchscope}"},  
                     success: function(msg){  
                          var json= jQuery.parseJSON(msg);
                      		 alert(json.msg);
                     }  
                 }); 
            }else{  
                alert("您还未进行选择！");  
            } 
            if($("#categorytype").val()==0){
		   		$('#dlg0').dialog('close');
            }else if($("#categorytype").val()==1){
		   	    $('#dlg1').dialog('close');
            }
	}
		   function editCategory(){  
           	 var categorytype=$('#categorytype').val();
           	 if(categorytype==0){
   				var selected = $('#n_tt0').tree('getSelected'); 
			}else if(categorytype==1){
   				var selected = $('#n_tt1').tree('getSelected'); 
			}  
              var text = document.getElementById("nodename").value;
              var newname = document.getElementById("newname").value;
 	   		if(selected){
 	   				$.ajax({  
	                     type: "POST",  
	                     url: '/front/user/category_editTreeNode',  
	                     data:{"id":selected.id,"text":text,"newname":newname},  
	                   	  success: function(msg){ 
                    			 var json= jQuery.parseJSON(msg);
	                           	 if(categorytype==0){
	                           	    $("#nodename").val(newname);
					   				 $('#n_tt0').tree('update', {
									target: selected.target,
									text: newname
								});
								}else if(categorytype==1){
								   $("#nodename").val(newname);
					   				 $('#n_tt1').tree('update', {
									target: selected.target,
									text: newname
								});
								} 
	                           	 alert(json.msg);
                    	 }  
	                 });
 	   		
 	   		 }else{  
	                alert("请选择要修改的节点");  
	            } 
 	   			 $('#dlg4').dialog('close');
 	   }
 </script>
 <style type="text/css">
 	.padding5{
 		padding:5px
 	}
 </style>
	</head>
	<script type="text/javascript">
	function delOptions(type,value){
		if(type=='1'){
			//alert(value);
			var str = value;
			if($("#patentType").val().indexOf(value+'_')!=-1){
				str=$("#patentType").val().replace(value+'_',"");
				$("#patentType").val(str);
			}
			else if($("#patentType").val().indexOf('_'+value)!=-1){
				str=$("#patentType").val().replace('_'+value,"");
				$("#patentType").val(str);
			}
			else{
				str=$("#patentType").val().replace(value,"");
				$("#patentType").val(str);
			}
		}
		if(type=='4'){
			var str = value;
			if($("#ipcMType").val().indexOf(value+'_')!=-1){
				str=$("#ipcMType").val().replace(value+'_',"");
				$("#ipcMType").val(str);
			}
			else if($("#ipcMType").val().indexOf('_'+value)!=-1){
				str=$("#ipcMType").val().replace('_'+value,"");
				$("#ipcMType").val(str);
			}
			else{
				str=$("#ipcMType").val().replace(value,"");
				$("#ipcMType").val(str);
			}
		}
		resultFilter(0,0);
		
	}

	function fmckClick(){
		if($("#fmck").attr("checked")=="checked"||$("#fmck").attr("checked")==true){
			//$("#fmck").attr("checked",false);
			
			resultFilter('FM','1');
		}
		else{
			delOptions('1','FM');
		}
	}

	function xxckClick(){
		if($("#xxck").attr("checked")=="checked"||$("#xxck").attr("checked")==true){
			//$("#fmck").attr("checked",false);
			
			resultFilter('XX','1');
		}
		else{
			delOptions('1','XX');
		}
	}


	function wgckClick(){
		if($("#wgck").attr("checked")=="checked"||$("#wgck").attr("checked")==true){
			//$("#fmck").attr("checked",false);
			
			resultFilter('WG','1');
		}
		else{
			delOptions('1','WG');
		}
	}

	function ackClick(){
		if($("#ack").attr("checked")=="checked"||$("#ack").attr("checked")==true){
			//$("#fmck").attr("checked",false);
			
			resultFilter('A','4');
		}
		else{
			delOptions('4','A/IC');
		}
	}
	function bckClick(){
		if($("#bck").attr("checked")=="checked"||$("#bck").attr("checked")==true){
			//$("#fmck").attr("checked",false);
			
			resultFilter('B','4');
		}
		else{
			delOptions('4','B/IC');
		}
	}
	function cckClick(){
		if($("#cck").attr("checked")=="checked"||$("#cck").attr("checked")==true){
			//$("#fmck").attr("checked",false);
			
			resultFilter('C','4');
		}
		else{
			delOptions('4','C/IC');
		}
	}
	function dckClick(){
		if($("#dck").attr("checked")=="checked"||$("#dck").attr("checked")==true){
			//$("#fmck").attr("checked",false);
			
			resultFilter('D','4');
		}
		else{
			delOptions('4','D/IC');
		}
	}
	function eckClick(){
		if($("#eck").attr("checked")=="checked"||$("#eck").attr("checked")==true){
			//$("#fmck").attr("checked",false);
			
			resultFilter('E','4');
		}
		else{
			delOptions('4','E/IC');
		}
	}
	function fckClick(){
		if($("#fck").attr("checked")=="checked"||$("#fck").attr("checked")==true){
			//$("#fmck").attr("checked",false);
			
			resultFilter('F','4');
		}
		else{
			delOptions('4','F/IC');
		}
	}
	function gckClick(){
		if($("#gck").attr("checked")=="checked"||$("#gck").attr("checked")==true){
			//$("#fmck").attr("checked",false);
			
			resultFilter('G','4');
		}
		else{
			delOptions('4','G/IC');
		}
	}
	function hckClick(){
		if($("#hck").attr("checked")=="checked"||$("#hck").attr("checked")==true){
			//$("#fmck").attr("checked",false);
			
			resultFilter('H','4');
		}
		else{
			delOptions('4','H/IC');
		}
	}

	var flagCheck=false;
	
		function resultFilter(param,type){
			var searchFormula_old='${searchFormulaOld}';
			if(type=="1"){
				if($("#patentType").val().indexOf(param)!=-1){
					alert("此条件已选择");
					return;
				}
				if($("#patentType").val()==""){
					$("#patentType").val(param);
				}
				else{
					$("#patentType").val($("#patentType").val()+"_"+param);
				}
				 
				 
			}
			if(type=="4"){
				if($("#ipcMType").val().indexOf(param+"/IC")!=-1){
					alert("此条件已选择");
					return;
				}
				if($("#ipcMType").val().length>=14){
					
					alert("IPC分类最多选择三个");
					var ipcMTypeStr = '${ipcMType}';
					if(ipcMTypeStr.indexOf("A/IC")!=-1){
						$("#ack").attr("checked","checked");
					}
					if(ipcMTypeStr.indexOf("B/IC")!=-1){
						$("#bck").attr("checked","checked");
					}
					if(ipcMTypeStr.indexOf("C/IC")!=-1){
						$("#cck").attr("checked","checked");
					}

					if(ipcMTypeStr.indexOf("D/IC")!=-1){
						$("#dck").attr("checked","checked");
					}
					if(ipcMTypeStr.indexOf("E/IC")!=-1){
						$("#eck").attr("checked","checked");
					}
					if(ipcMTypeStr.indexOf("F/IC")!=-1){
						$("#fck").attr("checked","checked");
					}

					if(ipcMTypeStr.indexOf("G/IC")!=-1){
						$("#gck").attr("checked","checked");
					}
					if(ipcMTypeStr.indexOf("H/IC")!=-1){
						$("#hck").attr("checked","checked");
					}

					
					if(ipcMTypeStr.indexOf("A/IC")==-1){
						$("#ack").attr("checked",false);
					}
					if(ipcMTypeStr.indexOf("B/IC")==-1){
						$("#bck").attr("checked",false);
					}
					if(ipcMTypeStr.indexOf("C/IC")==-1){
						$("#cck").attr("checked",false);
					}

					if(ipcMTypeStr.indexOf("D/IC")==-1){
						$("#dck").attr("checked",false);
					}
					if(ipcMTypeStr.indexOf("E/IC")==-1){
						$("#eck").attr("checked",false);
					}
					if(ipcMTypeStr.indexOf("F/IC")==-1){
						$("#fck").attr("checked",false);
					}

					if(ipcMTypeStr.indexOf("G/IC")==-1){
						$("#gck").attr("checked",false);
					}
					if(ipcMTypeStr.indexOf("H/IC")==-1){
						$("#hck").attr("checked",false);
					}
					flagCheck=true;
					return;
				}

				if(flagCheck){
					flagCheck=false;
					return;
				}
				var searchFormula_old='${searchFormulaOld}';

				if($("#ipcMType").val()==""){
					$("#ipcMType").val(param+"/IC");
				}
				else{
					$("#ipcMType").val($("#ipcMType").val()+"_"+param+"/IC");
				}				
			}
			var searchFormula_new="";
				
			 if(searchFormula_old.indexOf("@LX") != -1){     //js没有contain方法 用这种方式代替
			  	searchFormula_new=searchFormula_old.substr(0,searchFormula_old.indexOf("@LX"));
			  }else{
			  	searchFormula_new=searchFormula_old;
			  }
			 if(searchFormula_old.indexOf("/IC") == -1){
				 if($("#ipcMType").val()!=""){
						searchFormula_new = searchFormula_new + "*(" + $("#ipcMType").val() + ")";
				}
			 }
			 else{
				 if($("#ipcMType").val()!=""){
						searchFormula_new = searchFormula_new.substr(0,searchFormula_old.indexOf("/IC")+3)+$("#ipcMType").val()+searchFormula_new.substr(searchFormula_old.indexOf("/IC")+4,searchFormula_old.length);
				}
			 }
			


			
			  
			  searchFormula_new=searchFormula_new.replace(/\+/g,"_");
			  searchFormula_old=searchFormula_old.replace(/\+/g,"_");
			  $("#searchFormulaNewT").val(searchFormula_old);
			  //alert("/front/search/table_tableSearch?&searchFormulaOld=${searchFormulaOld}&searchType=table&searchFormula="+searchFormula_new+"@LX="+$("#patentType").val());
			  if($("#patentType").val()!=""){
				  window.location="/front/search/table_tableSearch?patentType="+ $("#patentType").val() +"&ipcMType="+  $("#ipcMType").val() +"&searchFormulaOld=${searchFormulaOld}&searchType=table&searchFormula="+searchFormula_new+"@LX="+$("#patentType").val();
					
			  }
			  else{
				  window.location="/front/search/table_tableSearch?patentType="+ $("#patentType").val() +"&ipcMType="+  $("#ipcMType").val() +"&searchFormulaOld=${searchFormulaOld}&searchType=table&searchFormula="+searchFormula_new;
					
			  }
	         
			  
		}
	</script>

	<body>
	<%@include file="/WEB-INF/page/front/share/top.jsp"%>
			
			<div id="mid" class="base" style="  padding-top: 0px;">
			<div id="condition" name="condition">
				<input type="text" id="patentType" name="patentType" style="display:none" value="${patentType}"></input>
				<input type="text" id="ipcMType" name="ipcMType" style="display:none" value="${ipcMType}"></input>
			</div>
			
				<div style="background-color: White; width: 990px; margin: 0 auto;">
				<div id="optionlist" name="optionlist"></div>
				
				<div class="screen" style="width:970px">
				    <ul>
						<li><span>专利类型：</span>
							 <input name="fmck" id="fmck" type="checkbox" value="FM" onchange="fmckClick('FM','1')"/>
							 <a href="javascript:resultFilter('FM','1')">发明<font id="fmnum"></font></a>&nbsp;&nbsp;
							 <input name="xxck" id="xxck" type="checkbox" value="XX" onchange="xxckClick('XX','1')" />
							 <a href="javascript:resultFilter('XX','1')">实用新型<font id="xxnum"></font></a>&nbsp;&nbsp;
							 <input  name="wgck" id="wgck" type="checkbox" value="WG" onchange="wgckClick('WG','1')" />
							 <a href="javascript:resultFilter('WG','1')">外观<font id="wgnum"></font></a>
						</li>
						<%--<li><span>专利类型（小类）：</span>
							<a href="#">PCT</a>
						</li>
						<li><span>法律状态：</span>
							 <a href="#">审中</a>&nbsp;&nbsp;
							 <a href="#">有效</a>&nbsp;&nbsp;
							 <a href="#">失效</a>
						</li>
						--%>
						<li><span>IPC分类号：</span>
						<input name="ack" id="ack" type="checkbox" value="A" onchange="ackClick()"/>
							<a href="javascript:resultFilter('A','4')">A类<font id="anum"></font></a>&nbsp;&nbsp;
						<input name="bck" id="bck" type="checkbox" value="B" onchange="bckClick()"/>
							<a href="javascript:resultFilter('B','4')">B类<font id="bnum"></font></a>&nbsp;&nbsp;
						<input name="cck" id="cck" type="checkbox" value="C" onchange="cckClick()"/>
							<a href="javascript:resultFilter('C','4')">C类<font id="cnum"></font></a>&nbsp;&nbsp;
						<input name="dck" id="dck" type="checkbox" value="D" onchange="dckClick()"/>
							<a href="javascript:resultFilter('D','4')">D类<font id="dnum"></font></a>&nbsp;&nbsp;
						<input name="eck" id="eck" type="checkbox" value="E" onchange="eckClick()"/>
							<a href="javascript:resultFilter('E','4')">E类<font id="enum"></font></a>&nbsp;&nbsp;
						<input name="fck" id="fck" type="checkbox" value="F" onchange="fckClick()"/>
							<a href="javascript:resultFilter('F','4')">F类<font id="fnum"></font></a>&nbsp;&nbsp;
						<input name="gck" id="gck" type="checkbox" value="G" onchange="gckClick()"/>
							<a href="javascript:resultFilter('G','4')">G类<font id="gnum"></font></a>&nbsp;&nbsp;
						<input name="hck" id="hck" type="checkbox" value="H" onchange="hckClick()"/>
							<a href="javascript:resultFilter('H','4')">H类<font id="hnum"></font></a>
						</li>
				    </ul>
				</div>
				
				<script type="text/javascript">
				 function exportExcel(){
			    	 var ids= document.getElementById('ids').value;
			 		if(ids=='on'||ids==''){
			 		 $.messager.alert('专利选择提示','请在专利著录项批量导出(Excel)操作之前，完成对专利的选择!','信息');
			 		}else{
			 		   $('#dlg6').dialog('open');
			 		//   window.location.href = "/front/search/table_toExcel?searchscope=${searchscope}&appnos="+ids;
			 		}
			     }

				 function exportAllExcel(){


					 	var ids2="";

						var num=0;
						if(${user.level}==2){
							num=50;
						}
						if(${user.level}==3){
							num=500;
						}
						if(${user.level}==4){
							num=500;
						}
						if(window.confirm("您一天最多导出"+num+"条，超出部分将无法导出")){
							
						}
						else{
							return false;
						}

					 	
					 	$.ajax({
					 		url:'/front/search/table_getSearchResult',
					 		async:true,
					 		data:{
					 			searchFormula: '${searchFormula}',
					 			searchType: '${searchType}',
					 			keyword:'${keyword}',
					 			title:'${cnDescriptionItem.title}',
					 			titleEn:'${enDescriptionItem.title}',
					 			abstr:'${cnDescriptionItem.abstr}',
					 			abstrEn:'${enDescriptionItem.abs}',
					 			claim:'${cnDescriptionItem.claim}',
					 			appl:'${cnDescriptionItem.appl}',
					 			applEn:'${enDescriptionItem.appl}',
					 			ipcMain:'',
					 			ipcMainEn:'',
					 			appno:'${cnDescriptionItem.appno}',
					 			appnoEn:'${enDescriptionItem.appno}',
					 			apd:'${cnDescriptionItem.apdText}',
					 			apdEn:'${enDescriptionItem.apdText}',
					 			pubnr:'${cnDescriptionItem.pubnr}',
					 			pubnrEn:'${enDescriptionItem.pubnr}',
					 			pud:'${cnDescriptionItem.pudText}',
					 			pudEn:'${enDescriptionItem.pudText}',
					 			appnr:'${cnDescriptionItem.appnr}',
					 			appd:'${cnDescriptionItem.appdText}',
					 			inventor:'${cnDescriptionItem.inventor}',
					 			inventorEn:'${enDescriptionItem.inventor}',
					 			fieldc:'${cnDescriptionItem.fieldc}',
					 			address:'${cnDescriptionItem.address}',
					 			nc:'${cnDescriptionItem.nc}',
					 			agency:'${cnDescriptionItem.agency}',
					 			agent:'${cnDescriptionItem.agent}',
					 			claim:'${cnDescriptionItem.claim}',
					 			searchscope:'${searchscope}',
					 			searchFormulaId:'${SearchFormulaEntity.id}',
					 			rows:${total },
					 			page:1
					 		},
					 		type:'post',
					 		dataType:'text',
					 		success:function(data){
					 			
					 			data=JSON.parse(data);
					 			var rows=data.rows;
					 			if(rows==0){
					 				$.messager.show({
					 					title:'检索结果提示',
					 					msg:'很抱歉，对于您输入的检索条件，系统为您检索到0条记录！',
					 					timeout:8000,
					 					showType:'slide',
					 					style:{
					 						right:'',
					 						top:document.body.scrollTop+document.documentElement.scrollTop,
					 						bottom:''
					 					}
					 				});
					 			}
					 			for(var i=0;i<rows.length;i++){
					 				if(ids2==""){
					 					ids2=ids2+rows[i].appno;
					 				}
					 				else{
					 					ids2=ids2+","+rows[i].appno;
					 				}
					 				
					 			}
					 			//window.open("/front/search/table_batchDownloadPdfs?appnos="+ids2);
					 			//alert(ids2);
					 			$("#ids").val(ids2);

					 			 var ids= document.getElementById('ids').value;
							 		if(ids=='on'||ids==''){
							 		 $.messager.alert('专利选择提示','请在专利著录项批量导出(Excel)操作之前，完成对专利的选择!','信息');
							 		}else{
							 		   $('#dlg6').dialog('open');
							 		//   window.location.href = "/front/search/table_toExcel?searchscope=${searchscope}&appnos="+ids;
							 		}
					 			
					 		},
					 		error:function(data){

					 		}
					 	});


					 
			    	
			     }
			     

			     function exportPdf(){
			    	 var d = dialog({
							title: '请选择要申请的类别',
						    button: [
						        {
						            value: '下载当前页所选专利全文（PDF）',
						            callback: function () {
						        	downChoosePDF();
						            }
						        },
						        {
						            value: '下载搜索出来的所有专利全文（PDF）',
						            callback: function () {
						        	downAllPDF();
						            }
						        }
						    ]
						});
						d.showModal();
			     }

			     function exportExcelChoose(){
			    	 var d = dialog({
							title: '请选择要导出的类别',
						    button: [
						        {
						            value: '导出当前页所选专利',
						            callback: function () {
						        		exportExcel();
						            }
						        },
						        {
						            value: '导出搜索出来的所有专利',
						            callback: function () {
						        		exportAllExcel();
						            }
						        }
						    ]
						});
						d.showModal();
			     }
			     

			     function addToPersonal(){
			    	 var d = dialog({
							title: '请选择要申请的类别',
						    button: [
						        {
						            value: '批量加入当前页所选专利',
						            callback: function () {
						        	addToPersonChoose();
						            }
						        },
						        {
						            value: '批量加入搜索出来的所有专利',
						            callback: function () {
						        	addToPersonAll();
						            }
						        }
						    ]
						});
						d.showModal();
			     }

			     function comparePatent(){
			    	 var ids= document.getElementById('ids').value;
						if(ids=='on'||ids==''){
						 $.messager.alert('专利选择提示','请在专利对比操作之前，完成对专利的选择!','信息');
						}else{
						 window.location.href = "/front/user/analysis_analysisUI?searchscope=${searchscope}&appnos="+ids;
						}
			     }

			     function analysisPatent(){
			    	 var searchFormula='${searchFormula}';
					    searchFormula=  searchFormula.replace(/\+/g,"_");
			          window.location="/front/search/table_loadingMessage?searchFormula="+searchFormula;
			        //  window.location="/front/search/table_analysisUI?searchFormula="+searchFormula;
			        
			     }
				</script>
				 <div class="handle">
				     <ul id="handle_show">
				       <li><a href="javascript:;" id="hide"><img src="/images/new/hid.png"></a></li>
				       <li>
				       		<c:if test="${user.level==1}">
								<a href="javascript:alert('必须注册之后才能使用')">
							</c:if>
							<c:if test="${user.level!=1}">
								<a href="javascript:exportExcelChoose()">
							</c:if>
				       			<img src="/images/new/handle1.png">
				       		</a>
				       
				       </li>
				       <li>
				       		<c:if test="${user.level==1}">
								<a href="javascript:alert('必须注册之后才能使用')">
							</c:if>
							<c:if test="${user.level!=1}">
								<a href="javascript:exportPdf()">
							</c:if>
				       			<img src="/images/new/handle2.png">
				       		</a>
				       </li>
				       
				       <li>
				       		<c:if test="${user.level==1||user.level==2}">
								<a href="javascript:alert('必须注册之后才能使用')">
							</c:if>
							<c:if test="${user.level!=1&&user.level!=2}">
								<a href="javascript:addToPersonal()">
							</c:if>
				       			<img src="/images/new/handle3.png">
				       		</a>
				       </li>
				       <li>
				       		<c:if test="${user.level==1}">
								<a href="javascript:alert('必须注册之后才能使用')">
							</c:if>
							<c:if test="${user.level!=1}">
								<a href="javascript:comparePatent()">
							</c:if>
				       			<img  src="/images/new/handle4.png">
				       		</a>
				       </li><%--
				       <li>
				       		<c:if test="${user.level==1}">
								<a href="javascript:alert('必须注册之后才能使用')">
							</c:if>
							<c:if test="${user.level!=1}">
								<a href="javascript:analysisPatent()" target="_blank">
							</c:if>
				       			<img src="/images/new/handle5.png">
				       		</a>
				       </li>
				     --%></ul>
				    <ul id="handle_hid">
				       <li><a href="javascript:;" id="show"><img src="/images/new/show.png"></a></li>
				     </ul>
				  </div>
				  
				<div id="pageContent">
				<div id="overlay">
					
				</div>
						<div class="result_title">
					      <ul>
					         <li><a id="button1" href="#"><input type="checkbox" id="ids" onclick="selectAll()">全选</a></li>
					         <li><a href="javascript:;" id="tw"><img src="/images/new/show1.png">图文</a></li>
					         <li    class="zsfs"><a href="javascript:;" id="lb"><img src="/images/new/show2.png">列表</a></li>
					         
         
					         <span>每页显示  20 条     共${total }记录</span>
					      </ul>
					  </div>
					  <div id="tw_show">
						
						</div>
							<div class="listzs" id="lb_show">
						       <ul>
						        <li style="width:30px;"></li>
						        <li style="width:400px;">专利名称</li>
						        <li style="width:150px;">申请号</li>
						        <li style="width:100px;">申请日</li>
						        <li style="width:88px;">状态</li>
						        <li style="width:150px;">主分类号</li>
						     </ul> 
						     <div id="listtab" class="listtab">
						     </div>
						</div>
							<div class="holder"></div>
						<div>
							
							<div id="dlg0" class="easyui-dialog" closed="true" title="企业个性库"
								style="width: 300px; height: 300px; padding: 10px"
								data-options="
							iconCls: 'icon-save',
							toolbar: [{
									text:'重命名',
									iconCls:'icon-edit',
									handler:function(){
										$('#dlg4').dialog('open');
									}
								},'-',{
								text:'添加分类',
								iconCls:'icon-add',
								handler:function(){
									$('#dlg2').dialog('open');
								}
							},'-',{
								text:'删除分类',
								iconCls:'icon-remove',
								handler:function(){
									 confirmPWD();
								}
							}],
							buttons: [{
								text:'确定',
								iconCls:'icon-ok',
								handler:function(){
									addPatentIn();
								}
							},{
								text:'取消',
								handler:function(){
									$('#dlg0').dialog('close');
								}
							}]
						">
								<input id="_appno" type="hidden" />
								<input id="_title" type="hidden" />
								<input id="_apd" type="hidden" />
								<input id="_appl" type="hidden" />
								<ul id="n_tt0" class="easyui-tree"></ul>
							</div>
							<div id="dlg1" class="easyui-dialog" closed="true" title="企业个性库"
								style="width: 300px; height: 300px; padding: 10px"
								data-options="
							iconCls: 'icon-save',
							toolbar: [{
									text:'重命名',
									iconCls:'icon-edit',
									handler:function(){
										$('#dlg4').dialog('open');
									}
								},'-',{
								text:'添加分类',
								iconCls:'icon-add',
								handler:function(){
									$('#dlg2').dialog('open');
								}
							},'-',{
								text:'删除分类',
								iconCls:'icon-remove',
								handler:function(){
									 confirmPWD();
								}
							}],
							buttons: [{
								text:'确定',
								iconCls:'icon-ok',
								handler:function(){
									addPatentIn();
								}
							},{
								text:'取消',
								handler:function(){
									$('#dlg1').dialog('close');
								}
							}]
						">
								<input id="_appno" type="hidden" />
								<input id="_title" type="hidden" />
								<input id="_apd" type="hidden" />
								<input id="_appl" type="hidden" />
								<ul id="n_tt1" class="easyui-tree"></ul>
							</div>
							<div id="dlg2" closed="true" class="easyui-dialog"
								title="请输入分类名称：" data-options="iconCls:'icon-add'"
								style="width: 200px; height: 150px; padding: 10px">
								<input id="nodename"></input>
								<div id="dlg-buttons" align="center">
									<br />
									<a id="getNodeNameId" href="javascript:void(0)"
										class="easyui-linkbutton" onclick="addNode();">确定</a>
									<a href="javascript:void(0)" class="easyui-linkbutton"
										onclick="javascript:$('#dlg2').dialog('close')">取消</a>
								</div>
							</div>
							<div id="dlg3" closed="true" class="easyui-dialog"
								title="请选择要加入的库：" data-options="iconCls:'icon-add'"
								style="width: 200px; height: 150px; padding: 10px">
								<div id="dlg-buttons" align="center">
									<br />
									<input id="categoryType1" value="加入关注专利库" type="button"
										onclick="openDlg(0);"></input>
									<br />
									<br />
									<input id="categoryType2" value="加入本企业专利库" type="button"
										onclick="openDlg(1);"></input>
								</div>
							</div>
							<div id="dlg4" closed="true" class="easyui-dialog"
								title="请输入新名称：" data-options="iconCls:'icon-add'"
								style="width: 200px; height: 150px; padding: 10px">
								<input id="newname"></input>
								<div id="dlg-buttons" align="center">
									<br />
									<a id="getNodeNameId" href="javascript:void(0)"
										class="easyui-linkbutton" onclick="editCategory();">确定</a>
									<a href="javascript:void(0)" class="easyui-linkbutton"
										onclick="javascript:$('#dlg4').dialog('close')">取消</a>
								</div>
							</div>

							<div id="dlg5" closed="true" class="easyui-dialog" title="确定要删除？"
								data-options="iconCls:'icon-remove'"
								style="width: 200px; height: 120px; padding: 10px">
								<div id="dlg-buttons" align="center">
									<br />
									<a id="getNodeNameId" href="javascript:void(0)"
										class="easyui-linkbutton" onclick="confimOrnot();">确定</a>
									<a href="javascript:void(0)" class="easyui-linkbutton"
										onclick="javascript:$('#dlg5').dialog('close')">取消</a>
								</div>
							</div>
							<div id="dlg6" closed="true" class="easyui-dialog"
								title="请选择需要导出的著录项：" data-options="iconCls:'icon-item_export'"
								style="width: 450px; height: 280px; padding: 10px">
								<div id="dlg-buttons" align="left">
									<!-- ///////////////////////////////////////////////////////////// -->
									<div id="ExportCFG" style="min-height: 100px;">
										<ul>
											<li>
												<label>
													<input type="checkbox" id="chkAppNo" value="appno"
														disabled="disabled" checked="checked">
													申请号
												</label>
											</li>
											<li>
												<label>
													<input type="checkbox" id="chkAppDate" value="appdate"
														name="items" onchange="changeItem()">
													申请日
												</label>
											</li>
											<!--  	
											<li>
												<label>
													<input type="checkbox" id="chkPubNo" value="pubno" name="items" onchange="changeItem()">
													公开（公告）号
												</label>
											</li>     
											-->
											<li>
												<label>
													<input type="checkbox" id="chkTitle" value="title"
														disabled="disabled" checked="checked">
													名称
												</label>
											</li>
											<li>
												<label>
													<input type="checkbox" id="chkIPC" value="ipc" name="items"
														onchange="changeItem()">
													分类号
												</label>
											</li>
											<!-- <li>
												<label>
													<input type="checkbox" id="chkMIPC" value="mipc" name="items" onchange="changeItem()">
													主分类号
												</label>
											</li>    -->
											<li>
												<label>
													<input type="checkbox" id="chkPubDate" value="pubdate"
														name="items" onchange="changeItem()">
													公开（公告）日
												</label>
											</li>
											<!--  			<li>
												<label>
													<input type="checkbox" id="chkAT" value="at" name="items" onchange="changeItem()">
													代理人
												</label>
											</li>   -->
											<li>
												<label>
													<input type="checkbox" id="chkAG" value="agent"
														name="items" onchange="changeItem()">
													专利代理机构
												</label>
											</li>
											<!-- 	<li>
												<label>
													<input type="checkbox" id="chkabs" value="abs" name="items" onchange="changeItem()">
													摘要
												</label>
											</li>  -->
											<li>
												<label>
													<input type="checkbox" id="chkCL" value="claim"
														name="items" onchange="changeItem()">
													主权项
												</label>
											</li>
											<li>
												<label>
													<input type="checkbox" id="chkCY" value="countryNC"
														name="items" onchange="changeItem()">
													国省代码
												</label>
											</li>
											<li>
												<label>
													<input type="checkbox" id="chkPA" value="appl" name="items"
														onchange="changeItem()">
													申请（专利权）人
												</label>
											</li>
											<li>
												<label>
													<input type="checkbox" id="chkIN" value="inventor"
														name="items" onchange="changeItem()">
													发明（设计）人
												</label>
											</li>
											<!--  	<li>
												<label>
													<input type="checkbox" id="chkPR" value="pr" name="items" onchange="changeItem()">
													优先权
												</label>
											</li>  -->
											<li>
												<label>
													<input type="checkbox" id="chkdz" value="address"
														name="items" onchange="changeItem()">
													地址
												</label>
											</li>
											<li>
												<label>
													<input type="checkbox" id="chkflzt" value="flzt"
														name="items" onchange="changeItem()">
													法律状态
												</label>
											</li>
											<li>
												<label>
													<input type="checkbox" id="abstr" value="abstr"
														name="items" onchange="changeItem()">
													摘要
												</label>
											</li>
											<li>
												<label>
													<input type="checkbox" id="claim" value="claim"
														name="items" onchange="changeItem()">
													主权利要求书
												</label>
											</li>
											<li>
												<label>
													<a> &nbsp;</a>
												</label>
											</li>
											<li>
												<label>
													<input onclick="selectAllItem()" type="checkbox"
														name="itemsAll" id="items">
													全选
													<a id="getNodeNameId" href="javascript:void(0)"
														class="easyui-linkbutton"
														onclick="toexcelWithSelectedItems();"
														style="background: #00ff00">导出(Excel)</a>
												</label>
											</li>
										</ul>
									</div>
									<!-- ///////////////////////////////////////////////////////////// -->
								</div>
							</div>
							
						</div>
					</div>
				</div>
			</div>
			<input type="hidden" id="idValue" value="jly" />
			<input type="hidden" id="ids" />
			<input type="hidden" id="items" />
			<input type="hidden" id="categorytype" />
			<%@include file="/WEB-INF/page/front/share/footer.jsp"%>
	</body>
</html>