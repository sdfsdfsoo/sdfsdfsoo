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
		     function ncSearch(value,row,index){   //国省
		     	var searchFormula="${searchFormula}";
		     	 var rowNcName=row.ncName;   
		     	    searchFormula="F XX ("+rowNcName.substring(rowNcName.indexOf("(")+1,rowNcName.indexOf(")"))+"/CO)*"+searchFormula.substring(5);
//                  var html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+row.ncSum+"</a>"
                  var html="<a target=_blank href='javascript:void(0)'>"+row.ncSum+"</a>"
				  return html;
            }
              function ncQsSearch(value,row,index){   //国省趋势
			     	 var rowNcName=value.substring(0,value.indexOf("_"));   
	                if(rowNcName=="0"){
						  return rowNcName;
					 }	  
				     	var searchFormula="${searchFormula}";
				     	  var rowyear=row.year;   
				     	    searchFormula="F XX ("+rowyear.substring(0,4)+"/AD)*("+value.substring(value.indexOf("_")+1,value.length)+"/CO)*"+searchFormula.substring(5);
//		               var  html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+rowNcName+"</a>"
		               var  html="<a target=_blank href='javascript:void(0)'>"+rowNcName+"</a>"
		                   return html;
            }
              function ncSqrSearch(value,row,index){   //国省申请人
			     	 var rowNcName=value.substring(0,value.indexOf("_"));   
	                if(rowNcName=="0"){
						  return rowNcName;
					 }	  
				     	var searchFormula="${searchFormula}";
				     	  var rowsqr=row.sqr;   
				     	    searchFormula="F XX ("+rowsqr+"/PA)*("+value.substring(value.indexOf("_")+1,value.length)+"/CO)*"+searchFormula.substring(5);
//		               var  html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+rowNcName+"</a>"
		               var  html="<a target=_blank href='javascript:void(0)'>"+rowNcName+"</a>"
		                   return html;
            }
              function ncFmrSearch(value,row,index){   //国省发明人
			     	 var rowNcName=value.substring(0,value.indexOf("_"));   
	                if(rowNcName=="0"){
						  return rowNcName;
					 }	  
				     	var searchFormula="${searchFormula}";
				     	  var rowfmr=row.fmr;   
				     	    searchFormula="F XX ("+rowfmr+"/IN)*("+value.substring(value.indexOf("_")+1,value.length)+"/CO)*"+searchFormula.substring(5);
//		               var  html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+rowNcName+"</a>"
		               var  html="<a target=_blank href='javascript:void(0)'>"+rowNcName+"</a>"
		                   return html;
            }
              function ncIpcSearch(value,row,index){   //国省技术
			     	 var rowNcName=value.substring(0,value.indexOf("_"));   
	                if(rowNcName=="0"){
						  return rowNcName;
					 }	  
				     	var searchFormula="${searchFormula}";
				     	  var rowipc=row.ipc;   
				     	    searchFormula="F XX ("+rowipc+"/MC)*("+value.substring(value.indexOf("_")+1,value.length)+"/CO)*"+searchFormula.substring(5);
//		               var  html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+rowNcName+"</a>"
		               var  html="<a target=_blank href='javascript:void(0)'>"+rowNcName+"</a>"
		                   return html;
            }
$(function() {
         $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_nc',
				success : function(data) {
						var data = $.parseJSON(data);
							var highcharts =  $('#container1').highcharts({
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
		   $('#dgNC').datagrid({
                    url:'/front/search/table_getPatentSumData_dataGrid_nc',
                    pagination:true,
                    checkbox:true,
                    height:930,
                    fitColumns:true,
                    multiSort:false,
                    columns:[[
                        {field:'ncName',title:'国省',width:9,align:'center'},
                        {field:'ncSum',title:'专利总数',width:9,align:'center',formatter:ncSearch},
                    ]]
                });	
	$('#tt').tabs({
       border:false,
       onSelect:function(title){
         if(title=="国省趋势分析"){
        	 $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_ncQS',
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
			   $('#dgNCQS').datagrid({
		            url:'/front/search/table_getPatentSumData_dataGrid_ncQS',
		            pagination:true,
		            checkbox:true,
		            height:930,
		            fitColumns:true,
		            multiSort:false,
		            columns:[[
		                {field:'year',width:9,align:'center'},
		                {field:'ncName0',width:9,align:'center',formatter:ncQsSearch},
		                {field:'ncName1',width:9,align:'center',formatter:ncQsSearch},
		                {field:'ncName2',width:9,align:'center',formatter:ncQsSearch},
		                {field:'ncName3',width:9,align:'center',formatter:ncQsSearch},
		                {field:'ncName4',width:9,align:'center',formatter:ncQsSearch},
		                {field:'ncName5',width:9,align:'center',formatter:ncQsSearch},
		                {field:'ncName6',width:9,align:'center',formatter:ncQsSearch},
		                {field:'ncName7',width:9,align:'center',formatter:ncQsSearch},
		                {field:'ncName8',width:9,align:'center',formatter:ncQsSearch},
		                {field:'ncName9',width:9,align:'center',formatter:ncQsSearch},
		            ]]
		        });	
         } 
	       if(title=="国省申请人分析"){
		      	 $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_ncSQR',
				success : function(data) {
				var data = $.parseJSON(data);
					var highcharts =  $('#container3').highcharts({
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
			  $('#dgNCSQR').datagrid({
		            url:'/front/search/table_getPatentSumData_dataGrid_ncSQR',
		            pagination:true,
		            checkbox:true,
		            height:930,
		            fitColumns:true,
		            multiSort:false,
		            columns:[[
		                {field:'sqr',width:9,align:'center'},
		                {field:'ncsqrName0',width:9,align:'center',formatter:ncSqrSearch},
		                {field:'ncsqrName1',width:9,align:'center',formatter:ncSqrSearch},
		                {field:'ncsqrName2',width:9,align:'center',formatter:ncSqrSearch},
		                {field:'ncsqrName3',width:9,align:'center',formatter:ncSqrSearch},
		                {field:'ncsqrName4',width:9,align:'center',formatter:ncSqrSearch},
		                {field:'ncsqrName5',width:9,align:'center',formatter:ncSqrSearch},
		                {field:'ncsqrName6',width:9,align:'center',formatter:ncSqrSearch},
		                {field:'ncsqrName7',width:9,align:'center',formatter:ncSqrSearch},
		                {field:'ncsqrName8',width:9,align:'center',formatter:ncSqrSearch},
		                {field:'ncsqrName9',width:9,align:'center',formatter:ncSqrSearch},
		            ]]
		        });		
		  }
		    if(title=="国省技术分类分析"){
		      	 $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_ncIPC',
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
			 $('#dgNCIPC').datagrid({
	            url:'/front/search/table_getPatentSumData_dataGrid_ncIPC',
	            pagination:true,
	            checkbox:true,
	            height:930,
	            fitColumns:true,
	            multiSort:false,
	            columns:[[
	                {field:'ipc',width:9,align:'center'},
	                {field:'ncipcName0',width:9,align:'center',formatter:ncIpcSearch},
	                {field:'ncipcName1',width:9,align:'center',formatter:ncIpcSearch},
	                {field:'ncipcName2',width:9,align:'center',formatter:ncIpcSearch},
	                {field:'ncipcName3',width:9,align:'center',formatter:ncIpcSearch},
	                {field:'ncipcName4',width:9,align:'center',formatter:ncIpcSearch},
	                {field:'ncipcName5',width:9,align:'center',formatter:ncIpcSearch},
	                {field:'ncipcName6',width:9,align:'center',formatter:ncIpcSearch},
	                {field:'ncipcName7',width:9,align:'center',formatter:ncIpcSearch},
	                {field:'ncipcName8',width:9,align:'center',formatter:ncIpcSearch},
	                {field:'ncipcName9',width:9,align:'center',formatter:ncIpcSearch},
	            ]]
	        });			
		  } 
		  	    if(title=="国省发明人分析"){
		      	 $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_ncFMR',
				success : function(data) {
				var data = $.parseJSON(data);
					var highcharts =  $('#container5').highcharts({
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
		 $('#dgNCFMR').datagrid({
	            url:'/front/search/table_getPatentSumData_dataGrid_ncFMR',
	            pagination:true,
	            checkbox:true,
	            height:930,
	            fitColumns:true,
	            multiSort:false,
	            columns:[[
	                {field:'fmr',width:9,align:'center'},
	                {field:'ncfmrName0',width:9,align:'center',formatter:ncFmrSearch},
	                {field:'ncfmrName1',width:9,align:'center',formatter:ncFmrSearch},
	                {field:'ncfmrName2',width:9,align:'center',formatter:ncFmrSearch},
	                {field:'ncfmrName3',width:9,align:'center',formatter:ncFmrSearch},
	                {field:'ncfmrName4',width:9,align:'center',formatter:ncFmrSearch},
	                {field:'ncfmrName5',width:9,align:'center',formatter:ncFmrSearch},
	                {field:'ncfmrName6',width:9,align:'center',formatter:ncFmrSearch},
	                {field:'ncfmrName7',width:9,align:'center',formatter:ncFmrSearch},
	                {field:'ncfmrName8',width:9,align:'center',formatter:ncFmrSearch},
	                {field:'ncfmrName9',width:9,align:'center',formatter:ncFmrSearch},
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
	        <div  title="国省构成分析" style="padding:10px">
       			<div id="container1" style="min-width: 100px; height: 250px; margin: 0 auto">
       			</div>
               <table id='dgNC' ></table>    
	        </div>
	          <div title="国省趋势分析" style="padding:10px">
       			<div id="container2" style="min-width: 100px; height: 250px; margin: 0 auto">
       			</div>
       			 <table id='dgNCQS' ></table>    
	        </div>
	          <div title="国省申请人分析" style="padding:10px">
       			<div id="container3" style="min-width: 100px; height: 250px; margin: 0 auto">
       			</div>
       			 <table id='dgNCSQR' ></table>    
	        </div>
		          <div title="国省发明人分析" style="padding:10px">
	       			<div id="container5" style="min-width: 100px; height: 250px; margin: 0 auto">
	       			</div>
       			 <table id='dgNCFMR' ></table>    
	        </div>
		        <div title="国省技术分类分析" style="padding:10px">
	       			<div id="container4" style="min-width: 100px; height: 250px; margin: 0 auto">
	       			</div>
       			 <table id='dgNCIPC' ></table>    
	        </div>
    	</div>
</body>
</html>