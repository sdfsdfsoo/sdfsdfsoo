<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>分析</title>
 <link href="/css/index.css" rel="stylesheet" type="text/css" /> 
<link  href="/css/userCenter/userCenter.css" rel="stylesheet" type="text/css" >
<link href="/js/easyui/themes/icon.css" rel="stylesheet" type="text/css" />
<link href="/js/easyui/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<script src="/js/jquery-1.8.0.min.js" type="text/javascript" ></script>
<script src="/js/easyui/jquery.easyui.min.js"></script>
<script src="/js/easyui/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="/js/highcharts.js"></script>
		<script type="text/javascript">
		    function ipcSearch(value,row,index){   //技术分类
			     	var searchFormula="${searchFormula}";
			     	 var rowipcName=row.ipcName;   
			     	    searchFormula="F XX ("+rowipcName+"/MC)*"+searchFormula.substring(5);
//	                  var html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+row.ipcSum+"</a>"
	                  var html="<a target=_blank href='javascript:void(0)'>"+row.ipcSum+"</a>"
					  return html;
               }
             function ipcQsSearch(value,row,index){   //技术分类趋势
			     	 var rowNcName=value.substring(0,value.indexOf("_"));   
	                if(rowNcName=="0"){
						  return rowNcName;
					 }	  
				     	var searchFormula="${searchFormula}";
				     	  var rowyear=row.year;   
				     	    searchFormula="F XX ("+rowyear.substring(0,4)+"/AD)*("+value.substring(value.indexOf("_")+1,value.length)+"/MC)*"+searchFormula.substring(5);
//		               var  html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+rowNcName+"</a>"
		               var  html="<a target=_blank href='javascript:void(0)'>"+rowNcName+"</a>"
		                   return html;
                } 
               function ipcNcSearch(value,row,index){   //技术分类国省
			     	 var rowNcName=value.substring(0,value.indexOf("_"));   
	                if(rowNcName=="0"){
						  return rowNcName;
					 }	  
				     	var searchFormula="${searchFormula}";
				     	  var rownc=row.nc;   
				     	    searchFormula="F XX ("+rownc.substring(rownc.indexOf("(")+1,rownc.indexOf(")"))+"/MC)*("+value.substring(value.indexOf("_")+1,value.length)+"/MC)*"+searchFormula.substring(5);
//		               var  html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+rowNcName+"</a>"
		               var  html="<a target=_blank href='javascript:void(0)'>"+rowNcName+"</a>"
		                   return html;
            }   
               function ipcSqrSearch(value,row,index){   //技术分类申请人
			     	 var rowNcName=value.substring(0,value.indexOf("_"));   
	                if(rowNcName=="0"){
						  return rowNcName;
					 }	  
				     	var searchFormula="${searchFormula}";
				     	  var rowsqr=row.sqr;   
				     	    searchFormula="F XX ("+rowsqr+"/PA)*("+value.substring(value.indexOf("_")+1,value.length)+"/MC)*"+searchFormula.substring(5);
//		               var  html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+rowNcName+"</a>"
		               var  html="<a target=_blank href='javascript:void(0)'>"+rowNcName+"</a>"
		                   return html;
            }  
$(function() {
         $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_ipc',
				success : function(data) {
						var data = $.parseJSON(data);
							var highcharts =  $('#container1').highcharts({
							 chart: {
         						   type: 'column'
     						   },
						        title: {
						            text: '',
						            x: -20 //cente
						        },
						        subtitle: {
						            text: '',
						            x: -20
						        },
						        xAxis: {
						            categories: data.categories,
						             labels: { 
							                rotation: -90,   //竖直放
	                                        rotation: -45    //45度倾斜 
								       } 
						        },
						      	yAxis : {
										title : {
											text : '件数'
										},
										min:0
									},
						        tooltip: {
						            valueSuffix: ''
						        },
						        legend: {
						            layout: 'vertical',
						            align: 'right',
						            verticalAlign: 'middle',
						            borderWidth: 0
						        },
						        series:  data.series
						    });
				},
				error : function(e) {
					alert("远程获取数据错误");
				}
			});
	 $('#dgIPC').datagrid({
        url:'/front/search/table_getPatentSumData_dataGrid_ipc',
        pagination:true,
        checkbox:true,
        height:930,
        fitColumns:true,
        multiSort:false,
        columns:[[
            {field:'ipcName',title:'技术分类',width:9,align:'center'},
            {field:'ipcSum',title:'专利总数',width:9,align:'center',formatter:ipcSearch},
        ]]
    });	
	$('#tt').tabs({
       border:false,
       onSelect:function(title){
         if(title=="技术分类趋势分析"){
        	 $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_ipcQS?num='+$.now(),
				success : function(data) {
				var data = $.parseJSON(data);
					var highcharts =  $('#container2').highcharts({
				        title: {
				            text: '',
				            x: -20 //cente
				        },
				        subtitle: {
				            text: '',
				            x: -20
				        },
				        xAxis: {
				            categories: data.categories,
				             labels: { 
							                rotation: -90,   //竖直放
	                                        rotation: -45    //45度倾斜 
								       } 
				        },
				      	yAxis : {
								title : {
									text : '件数'
								},
								min:0
							},
				        tooltip: {
				            valueSuffix: ''
				        },
				        legend: {
				            layout: 'vertical',
				            align: 'right',
				            verticalAlign: 'middle',
				            borderWidth: 0
				        },
				        series:  data.series
				    });
				},
				error : function(e) {
					alert("远程获取数据错误");
				}
			});
		 $('#dgipcQS').datagrid({
		            url:'/front/search/table_getPatentSumData_dataGrid_ipcQS',
		            pagination:true,
		            checkbox:true,
		            height:930,
		            fitColumns:true,
		            multiSort:false,
		            columns:[[
		                {field:'year',width:9,align:'center'},
		                {field:'ipcName0',width:9,align:'center',formatter:ipcQsSearch},
		                {field:'ipcName1',width:9,align:'center',formatter:ipcQsSearch},
		                {field:'ipcName2',width:9,align:'center',formatter:ipcQsSearch},
		                {field:'ipcName3',width:9,align:'center',formatter:ipcQsSearch},
		                {field:'ipcName4',width:9,align:'center',formatter:ipcQsSearch},
		                {field:'ipcName5',width:9,align:'center',formatter:ipcQsSearch},
		                {field:'ipcName6',width:9,align:'center',formatter:ipcQsSearch},
		                {field:'ipcName7',width:9,align:'center',formatter:ipcQsSearch},
		                {field:'ipcName8',width:9,align:'center',formatter:ipcQsSearch},
		                {field:'ipcName9',width:9,align:'center',formatter:ipcQsSearch},
		            ]]
		        });		
         } 
	       if(title=="技术分类国省分析"){
		      	 $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_ipcNC',
				success : function(data) {
				var data = $.parseJSON(data);
					var highcharts =  $('#container3').highcharts({
					 chart: {
         						   type: 'column'
     						   },
				        title: {
				            text: '',
				            x: -20 //cente
				        },
				        subtitle: {
				            text: '',
				            x: -20
				        },
				        xAxis: {
				            categories: data.categories,
				             labels: { 
							                rotation: -90,   //竖直放
	                                        rotation: -45    //45度倾斜 
								       } 
				        },
				      	yAxis : {
								title : {
									text : '件数'
								},
								min:0
							},
				        tooltip: {
				            valueSuffix: ''
				        },
				        legend: {
				            layout: 'vertical',
				            align: 'right',
				            verticalAlign: 'middle',
				            borderWidth: 0
				        },
				        series:  data.series
				    });
				},
				error : function(e) {
					alert("远程获取数据错误");
				}
			});
			 $('#dgipcNC').datagrid({
		            url:'/front/search/table_getPatentSumData_dataGrid_ipcNC',
		            pagination:true,
		            checkbox:true,
		            height:930,
		            fitColumns:true,
		            multiSort:false,
		            columns:[[
		                {field:'nc',width:9,align:'center'},
		                {field:'ipcNCName0',width:9,align:'center',formatter:ipcNcSearch},
		                {field:'ipcNCName1',width:9,align:'center',formatter:ipcNcSearch},
		                {field:'ipcNCName2',width:9,align:'center',formatter:ipcNcSearch},
		                {field:'ipcNCName3',width:9,align:'center',formatter:ipcNcSearch},
		                {field:'ipcNCName4',width:9,align:'center',formatter:ipcNcSearch},
		                {field:'ipcNCName5',width:9,align:'center',formatter:ipcNcSearch},
		                {field:'ipcNCName6',width:9,align:'center',formatter:ipcNcSearch},
		                {field:'ipcNCName7',width:9,align:'center',formatter:ipcNcSearch},
		                {field:'ipcNCName8',width:9,align:'center',formatter:ipcNcSearch},
		                {field:'ipcNCName9',width:9,align:'center',formatter:ipcNcSearch},
		            ]]
		        });		
		  }
		    if(title=="技术分类申请人分析"){
		      	 $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_ipcSQR',
				success : function(data) {
				var data = $.parseJSON(data);
					var highcharts =  $('#container4').highcharts({
				        title: {
				            text: '',
				            x: -20 //cente
				        },
				        subtitle: {
				            text: '',
				            x: -20
				        },
				        xAxis: {
				            categories: data.categories,
				             labels: { 
							                rotation: -90,   //竖直放
	                                        rotation: -45    //45度倾斜 
								       } 
				        },
				      	yAxis : {
								title : {
									text : '件数'
								},
								min:0
							},
				        tooltip: {
				            valueSuffix: ''
				        },
				        legend: {
				            layout: 'vertical',
				            align: 'right',
				            verticalAlign: 'middle',
				            borderWidth: 0
				        },
				        series:  data.series
				    });
				},
				error : function(e) {
					alert("远程获取数据错误");
				}
			});
			 $('#dgipcSQR').datagrid({
		            url:'/front/search/table_getPatentSumData_dataGrid_ipcSQR?num='+$.now(),
		            pagination:true,
		            checkbox:true,
		            height:930,
		            fitColumns:true,
		            multiSort:false,
		            columns:[[
		                {field:'sqr',width:9,align:'center'},
		                {field:'ipcSQRName0',width:9,align:'center',formatter:ipcSqrSearch},
		                {field:'ipcSQRName1',width:9,align:'center',formatter:ipcSqrSearch},
		                {field:'ipcSQRName2',width:9,align:'center',formatter:ipcSqrSearch},
		                {field:'ipcSQRName3',width:9,align:'center',formatter:ipcSqrSearch},
		                {field:'ipcSQRName4',width:9,align:'center',formatter:ipcSqrSearch},
		                {field:'ipcSQRName5',width:9,align:'center',formatter:ipcSqrSearch},
		                {field:'ipcSQRName6',width:9,align:'center',formatter:ipcSqrSearch},
		                {field:'ipcSQRName7',width:9,align:'center',formatter:ipcSqrSearch},
		                {field:'ipcSQRName8',width:9,align:'center',formatter:ipcSqrSearch},
		                {field:'ipcSQRName9',width:9,align:'center',formatter:ipcSqrSearch},
		            ]]
		        });		
		  } 
		  
		  
		     
       }
    });
});	

   
</script>

</head>
<body>
     <div id="tt" class="easyui-tabs" style="width:750px;height:500px">
	        <div  title="技术分类构成分析" style="padding:10px">
       			<div id="container1" style="min-width: 100px; height: 250px; margin: 0 auto">
       			</div>
            <table id='dgIPC' ></table>
	        </div>
	          <div title="技术分类趋势分析" style="padding:10px">
       			<div id="container2" style="min-width: 100px; height: 250px; margin: 0 auto">
       			</div>
       			<table id='dgipcQS' ></table>    
	        </div>
	          <div title="技术分类国省分析" style="padding:10px">
       			<div id="container3" style="min-width: 100px; height: 250px; margin: 0 auto">
       			</div>
       			<table id='dgipcNC' ></table>    
	        </div>
	          <div title="技术分类申请人分析" style="padding:10px">
       			<div id="container4" style="min-width: 100px; height: 250px; margin: 0 auto">
       			</div>
       			<table id='dgipcSQR' ></table>    
	        </div>
    	</div>
</body>
</html>