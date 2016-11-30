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
		         function sqrSearch(value,row,index){   //申请人
			     	var searchFormula="${searchFormula}";
			     	 var rowNcName=row.applName;   
			     	    searchFormula="F XX ("+rowNcName+"/PA)*"+searchFormula.substring(5);
//	                  var html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+row.applSum+"</a>"
	                  var html="<a target=_blank href='javascript:void(0)'>"+row.applSum+"</a>"
					  return html;
               }
               function sqrQsSearch(value,row,index){   //申请人趋势
			     	 var rowNcName=value.substring(0,value.indexOf("_"));   
	                if(rowNcName=="0"){
						  return rowNcName;
					 }	  
				     	var searchFormula="${searchFormula}";
				     	  var rowyear=row.year;   
				     	    searchFormula="F XX ("+rowyear.substring(0,4)+"/AD)*("+value.substring(value.indexOf("_")+1,value.length)+"/PA)*"+searchFormula.substring(5);
//		               var  html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+rowNcName+"</a>"
		               var  html="<a target=_blank href='javascript:void(0)'>"+rowNcName+"</a>"
		                   return html;
                }
                  function sqrNcSearch(value,row,index){   //申请人国省
			     	 var rowNcName=value.substring(0,value.indexOf("_"));   
	                if(rowNcName=="0"){
						  return rowNcName;
					 }	  
				     	var searchFormula="${searchFormula}";
				     	  var rownc=row.nc;   
				     	    searchFormula="F XX ("+rownc.substring(rownc.indexOf("(")+1,rownc.indexOf(")"))+"/CO)*("+value.substring(value.indexOf("_")+1,value.length)+"/PA)*"+searchFormula.substring(5);
//		               var  html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+rowNcName+"</a>"
		               var  html="<a target=_blank href='javascript:void(0)'>"+rowNcName+"</a>"
		                   return html;
            }
               function sqrIpcSearch(value,row,index){   //申请人技术分类
			     	 var rowNcName=value.substring(0,value.indexOf("_"));   
	                if(rowNcName=="0"){
						  return rowNcName;
					 }	  
				     	var searchFormula="${searchFormula}";
				     	  var rownc=row.ipc;   
				     	    searchFormula="F XX ("+rownc+"/MC)*("+value.substring(value.indexOf("_")+1,value.length)+"/PA)*"+searchFormula.substring(5);
//		               var  html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+rowNcName+"</a>"
		               var  html="<a target=_blank href='javascript:void(0)'>"+rowNcName+"</a>"
		                   return html;
            }
            function sqrFmrSearch(value,row,index){   //申请人技术骨干
			     	 var rowNcName=value.substring(0,value.indexOf("_"));   
	                if(rowNcName=="0"){
						  return rowNcName;
					 }	  
				     	var searchFormula="${searchFormula}";
				     	  var rowfmr=row.fmr;   
				     	    searchFormula="F XX ("+rowfmr+"/IN)*("+value.substring(value.indexOf("_")+1,value.length)+"/PA)*"+searchFormula.substring(5);
//		               var  html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+rowNcName+"</a>"
		               var  html="<a target=_blank href='javascript:void(0)'>"+rowNcName+"</a>"
		                   return html;
            }
$(function() {
         $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_shenqingren',
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
		  $('#dgSQR').datagrid({
                   url:'/front/search/table_getPatentSumData_dataGrid_appl',
                   pagination:true,
                   checkbox:true,
                   height:930,
                   fitColumns:true,
                   multiSort:false,
                   columns:[[
                       {field:'applName',title:'申请人',width:9,align:'center'},
                       {field:'applSum',title:'专利总数',width:9,align:'center',formatter:sqrSearch},
                   ]]
               });	
	$('#tt').tabs({
       border:false,
       onSelect:function(title){
         if(title=="申请人趋势分析"){
        	 $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_shenqingrenQS',
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
		 $('#dgsqrQS').datagrid({
		            url:'/front/search/table_getPatentSumData_dataGrid_sqrQS',
		            pagination:true,
		            checkbox:true,
		            height:930,
		            fitColumns:true,
		            multiSort:false,
		            columns:[[
		                {field:'year',width:9,align:'center'},
		                {field:'applName0',width:9,align:'center',formatter:sqrQsSearch},
		                {field:'applName1',width:9,align:'center',formatter:sqrQsSearch},
		                {field:'applName2',width:9,align:'center',formatter:sqrQsSearch},
		                {field:'applName3',width:9,align:'center',formatter:sqrQsSearch},
		                {field:'applName4',width:9,align:'center',formatter:sqrQsSearch},
		                {field:'applName5',width:9,align:'center',formatter:sqrQsSearch},
		                {field:'applName6',width:9,align:'center',formatter:sqrQsSearch},
		                {field:'applName7',width:9,align:'center',formatter:sqrQsSearch},
		                {field:'applName8',width:9,align:'center',formatter:sqrQsSearch},
		                {field:'applName9',width:9,align:'center',formatter:sqrQsSearch},
		            ]]
		        });	
         } 
	       if(title=="申请人国省分析"){
		      	 $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_shenqingrenNC',
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
		 $('#dgsqrNC').datagrid({
		            url:'/front/search/table_getPatentSumData_dataGrid_sqrNC',
		            pagination:true,
		            checkbox:true,
		            height:930,
		            fitColumns:true,
		            multiSort:false,
		            columns:[[
		                {field:'nc',width:9,align:'center'},
		                {field:'sqrNCName0',width:9,align:'center',formatter:sqrNcSearch},
		                {field:'sqrNCName1',width:9,align:'center',formatter:sqrNcSearch},
		                {field:'sqrNCName2',width:9,align:'center',formatter:sqrNcSearch},
		                {field:'sqrNCName3',width:9,align:'center',formatter:sqrNcSearch},
		                {field:'sqrNCName4',width:9,align:'center',formatter:sqrNcSearch},
		                {field:'sqrNCName5',width:9,align:'center',formatter:sqrNcSearch},
		                {field:'sqrNCName6',width:9,align:'center',formatter:sqrNcSearch},
		                {field:'sqrNCName7',width:9,align:'center',formatter:sqrNcSearch},
		                {field:'sqrNCName8',width:9,align:'center',formatter:sqrNcSearch},
		                {field:'sqrNCName9',width:9,align:'center',formatter:sqrNcSearch},
		            ]]
		        });		
		  }
		    if(title=="申请人技术分类构成分析"){
		      	 $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_shenqingrenIPC',
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
		 $('#dgSQRIPC').datagrid({
	            url:'/front/search/table_getPatentSumData_dataGrid_sqrIPC',
	            pagination:true,
	            checkbox:true,
	            height:930,
	            fitColumns:true,
	            multiSort:false,
	            columns:[[
	                {field:'ipc',width:9,align:'center'},
	                {field:'sqripcName0',width:9,align:'center',formatter:sqrIpcSearch},
	                {field:'sqripcName1',width:9,align:'center',formatter:sqrIpcSearch},
	                {field:'sqripcName2',width:9,align:'center',formatter:sqrIpcSearch},
	                {field:'sqripcName3',width:9,align:'center',formatter:sqrIpcSearch},
	                {field:'sqripcName4',width:9,align:'center',formatter:sqrIpcSearch},
	                {field:'sqripcName5',width:9,align:'center',formatter:sqrIpcSearch},
	                {field:'sqripcName6',width:9,align:'center',formatter:sqrIpcSearch},
	                {field:'sqripcName7',width:9,align:'center',formatter:sqrIpcSearch},
	                {field:'sqripcName8',width:9,align:'center',formatter:sqrIpcSearch},
	                {field:'sqripcName9',width:9,align:'center',formatter:sqrIpcSearch},
	            ]]
	        });				
		  } 
		  	    if(title=="申请人技术骨干分析"){
		      	 $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_fmr',
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
			 $('#dgSQRFMR').datagrid({
	            url:'/front/search/table_getPatentSumData_dataGrid_sqrFMR',
	            reload:true,
	            pagination:true,
	            checkbox:true,
	            height:930,
	            fitColumns:true,
	            multiSort:false,
	            columns:[[
	                {field:'fmr',width:9,align:'center'},
	                {field:'sqrfmrName0',width:9,align:'center',formatter:sqrFmrSearch},
	                {field:'sqrfmrName1',width:9,align:'center',formatter:sqrFmrSearch},
	                {field:'sqrfmrName2',width:9,align:'center',formatter:sqrFmrSearch},
	                {field:'sqrfmrName3',width:9,align:'center',formatter:sqrFmrSearch},
	                {field:'sqrfmrName4',width:9,align:'center',formatter:sqrFmrSearch},
	                {field:'sqrfmrName5',width:9,align:'center',formatter:sqrFmrSearch},
	                {field:'sqrfmrName6',width:9,align:'center',formatter:sqrFmrSearch},
	                {field:'sqrfmrName7',width:9,align:'center',formatter:sqrFmrSearch},
	                {field:'sqrfmrName8',width:9,align:'center',formatter:sqrFmrSearch},
	                {field:'sqrfmrName9',width:9,align:'center',formatter:sqrFmrSearch},
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
	        <div  title="申请人构成分析" style="padding:10px">
       			<div id="container1" style="min-width: 100px; height: 250px; margin: 0 auto">
       			</div>
            <table id='dgSQR' ></table>    
	        </div>
	          <div title="申请人趋势分析" style="padding:10px">
       			<div id="container2" style="min-width: 100px; height: 250px; margin: 0 auto">
       			</div>
       			 <table id='dgsqrQS' ></table>    
	        </div>
	          <div title="申请人国省分析" style="padding:10px">
       			<div id="container3" style="min-width: 100px; height: 250px; margin: 0 auto">
       			</div>
       			<table id='dgsqrNC' ></table>    
	        </div>
	          <div title="申请人技术分类构成分析" style="padding:10px">
       			<div id="container4" style="min-width: 100px; height: 250px; margin: 0 auto">
       			</div>
       			<table id='dgSQRIPC' ></table>    
	        </div>
	         <div title="申请人技术骨干分析" style="padding:10px">
       			<div id="container5" style="min-width: 100px; height: 250px; margin: 0 auto">
       			</div>
       			<table id='dgSQRFMR' ></table>    
	        </div>
    	</div>
</body>
</html>