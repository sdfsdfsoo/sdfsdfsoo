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

	
	
 	 var ids="";
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
		window.location.href = "/front/search/table_toExcel?searchscope=${searchscope}&appnos="+ids+"&items="+items;
		
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
				
$(function() {

	var patentTypeList = '${patentType}'.split("_");
	var ipcMTypeList = '${ipcMType}'.split("_");
	var strs = "";
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
	

	
	
	
	
     $('#button2').linkbutton({
        iconCls: 'icon-item_export',
		text:'专利著录项批量导出(Excel)',
		onClick: function(){
		var ids= document.getElementById('ids').value;
		if(ids=='on'||ids==''){
		 $.messager.alert('专利选择提示','请在专利著录项批量导出(Excel)操作之前，完成对专利的选择!','信息');
		}else{
		   $('#dlg6').dialog('open');
		//   window.location.href = "/front/search/table_toExcel?searchscope=${searchscope}&appnos="+ids;
		}
		
		}
	});
	$('#button3').linkbutton({
	        iconCls: 'icon-pdf_download',
			text:'专利全文批量下载(PDF)',
			onClick: function(){
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
	});
    $('#button4').linkbutton({
        iconCls: 'icon-batch_import',
		text:'批量加入企业个性库',
		onClick: function(){

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
	});
	$('#button5').linkbutton({
	        iconCls: 'icon-patent_compare',
			text:'批量专利对比',
			onClick: function(){
			var ids= document.getElementById('ids').value;
			if(ids=='on'||ids==''){
			 $.messager.alert('专利选择提示','请在专利对比操作之前，完成对专利的选择!','信息');
			}else{
			 window.location.href = "/front/user/analysis_analysisUI?searchscope=${searchscope}&appnos="+ids;
			}
			
			}
	});
	$('#button6').linkbutton({
	        iconCls: 'icon-patent_compare',
			text:'类型过滤',
			onClick: function(){
			  var   patentTypeStr = getPatentTypeStr();
			  var searchFormula_old='${searchFormula}';
			  var searchFormula_new="";
			 if(searchFormula_old.indexOf("@LX") != -1){     //js没有contain方法 用这种方式代替
			  	searchFormula_new=searchFormula_old.substr(0,searchFormula_old.indexOf("@LX"));
			  }else{
			  	searchFormula_new=searchFormula_old;
			  }
			  searchFormula_new=searchFormula_new.replace(/\+/g,"_");
			  var patentType=patentTypeStr.substr(patentTypeStr.indexOf("=")+1,patentTypeStr.length);
	          window.location="/front/search/table_tableSearch?patentType="+patentType+"&searchType=table&searchFormula="+searchFormula_new+""+patentTypeStr;
			}
	});
	$('#button7').linkbutton({
	        iconCls: 'icon-patent_compare',
			text:'分析',
			onClick: function(){
			  var searchFormula='${searchFormula}';
			    searchFormula=  searchFormula.replace(/\+/g,"_");
	          window.location="/front/search/table_loadingMessage?searchFormula="+searchFormula;
	        //  window.location="/front/search/table_analysisUI?searchFormula="+searchFormula;
	        
			}
	});
               
	$('#tt').datagrid({
	    loadMsg:'江苏畅远信息科技有限公司友情提示：系统正在为您努力检索专利信息，请稍等！',
	    singleSelect:'true',
	    pageSize:50,
	    fitColumns:'true',
		pagination:'true',
	    url:'/front/search/table_getSearchResult',
		view : patent,
		showHeader : false,
		pagePosition : top,
		toolbar:'#toolbar',
	    onLoadSuccess : _loadSuccess,
	    onloadError: _loadError,
		queryParams: {
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
		searchFormulaId:'${SearchFormulaEntity.id}'
	    } 
	    
	});

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
			rows:tatal,
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
			alert(ids2);
			
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
var tatal=0;
function downAllPDF(){
	var ids2="";
	
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
			rows:tatal,
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
                        	 alert(json.msg);
                          	/*if(categorytype==0){	
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
							}*/
							location.reload();				
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
			alert(value);
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
				if($("#ipcMType").val().indexOf(param)!=-1){
					alert("此条件已选择");
					return;
				}
				if($("#ipcMType").val().length>=14){
					alert("IPC分类最多选择三个");
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
			if($("#ipcMType").val()!=""){
				searchFormula_new = searchFormula_new + "*(" + $("#ipcMType").val() + ")";
			}


			
			  
			  searchFormula_new=searchFormula_new.replace(/\+/g,"_");
			  searchFormula_old=searchFormula_old.replace(/\+/g,"_");
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
				
				<div class="screen">
				    <ul>
						<li><span>专利类型：</span>
							 <a href="javascript:resultFilter('FM','1')">发明</a>&nbsp;&nbsp;
							 <a href="javascript:resultFilter('XX','1')">实用新型</a>&nbsp;&nbsp;
							 <a href="javascript:resultFilter('WG','1')">外观</a>
						</li>
						<li><span>专利类型（小类）：</span>
							<a href="#">PCT</a>
						</li>
						<li><span>法律状态：</span>
							 <a href="#">审中</a>&nbsp;&nbsp;
							 <a href="#">有效</a>&nbsp;&nbsp;
							 <a href="#">失效</a>
						</li>
						<li><span>IPC分类号：</span>
							<a href="javascript:resultFilter('A','4')">A类</a>&nbsp;&nbsp;
							<a href="javascript:resultFilter('B','4')">B类</a>&nbsp;&nbsp;
							<a href="javascript:resultFilter('C','4')">C类</a>&nbsp;&nbsp;
							<a href="javascript:resultFilter('D','4')">D类</a>&nbsp;&nbsp;
							<a href="javascript:resultFilter('E','4')">E类</a>&nbsp;&nbsp;
							<a href="javascript:resultFilter('F','4')">F类</a>&nbsp;&nbsp;
							<a href="javascript:resultFilter('G','4')">G类</a>&nbsp;&nbsp;
							<a href="javascript:resultFilter('H','4')">H类</a>
						</li>
				    </ul>
				</div>

				<%--<div>
					<span>专利类型：</span>
					<span style="margin-left:41px">
						<a class="padding5" href="javascript:resultFilter('FM','1')">发明</a>
						<a class="padding5"  href="javascript:resultFilter('XX','1')">实用新型</a>
						<a class="padding5"  href="javascript:resultFilter('WG','1')">外观</a>
					</span><br />
					
					<span>专利类型(小类)：</span>
					<span>
						<a class="padding5">pct</a>
					</span><br />
					
					<span>法律状态：</span>
					<span  style="margin-left:41px">
						<a class="padding5">审中</a>
						<a class="padding5">有效</a>
						<a class="padding5">失效</a>
					</span><br />
					
					<span>IPC分类号：</span>
					<span  style="margin-left:34px">
						<a class="padding5" href="javascript:resultFilter('A','4')">A类</a>
						<a class="padding5" href="javascript:resultFilter('B','4')">B类</a>
						<a class="padding5" href="javascript:resultFilter('C','4')">C类</a>
						<a class="padding5" href="javascript:resultFilter('D','4')">D类</a>
						<a class="padding5" href="javascript:resultFilter('E','4')">E类</a>
						<a class="padding5" href="javascript:resultFilter('F','4')">F类</a>
						<a class="padding5" href="javascript:resultFilter('G','4')">G类</a>
						<a class="padding5" href="javascript:resultFilter('H','4')">H类</a>
					</span>
				
				</div>
					--%><div id="pageContent">

						<table id="tt" style="height: 800px">
							<thead>
								<tr>
									<th field="attr1" width="60" sortable="true"></th>
									<th field="attr2" width="60" sortable="true"></th>
									<th field="attr3" width="60" sortable="true"></th>
									<th field="attr4" width="60" sortable="true"></th>
									<th field="attr5" width="60" sortable="true"></th>
									<th field="attr6" width="60" sortable="true"></th>
									<th field="attr7" width="60" sortable="true"></th>
									<th field="attr8" width="60" sortable="true"></th>
									<th field="attr9" width="60" sortable="true"></th>
									<th field="attr10" width="60" sortable="true"></th>
								</tr>
							</thead>
						</table>
						<div id="toolbar">
							<a id="button1" href="#"> <input type="checkbox" id="ids"
									onclick="selectAll()">全选</a>
							<a id="button2">批量著录项导出(Excel)</a>
							<a id="button3">批量专利全文下载(PDF)</a>
							<!-- //////////////////////////////////////// -->
							<a id="button4">批量加入企业个性库</a>
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
							<a id="button5">批量专利对比</a>
							<c:if test="${searchscope!='DocDB'}">
								<a id="button7" target="_blank">分析</a>
								<br>
								<c:if test="${patentTypeFM!='FM'}">
									<input style="margin-left: 10px" type="checkbox"
										id="patenttype_fm" value="FM" />发明(FM)
								</c:if>
								<c:if test="${patentTypeFM=='FM'}">
									<input checked style="margin-left: 10px" type="checkbox"
										id="patenttype_fm" value="FM" />发明(FM)
								</c:if>
								<c:if test="${patentTypeWG!='WG'}">
									<input style="margin-left: 10px" type="checkbox"
										id="patenttype_wg" value="WG" />外观(WG)
								</c:if>
								<c:if test="${patentTypeWG=='WG'}">
									<input checked style="margin-left: 10px" type="checkbox"
										id="patenttype_wg" value="WG" />外观(WG)
								</c:if>
								<c:if test="${patentTypeXX!='XX'}">
									<input style="margin-left: 10px" type="checkbox"
										id="patenttype_xx" value="XX" />实用新型(XX)
								</c:if>
								<c:if test="${patentTypeXX=='XX'}">
									<input checked style="margin-left: 10px" type="checkbox"
										id="patenttype_xx" value="XX" />实用新型(XX)
								</c:if>
								<a id="button6">类型检索</a>
							</c:if>
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