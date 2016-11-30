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
		        function fmrSearch(value,row,index){   //发明人
		        
			     	var searchFormula="${searchFormula}";
			     	 var rowNcName=row.inventorName;   
			     	    searchFormula="F XX ("+rowNcName+"/IN)*"+searchFormula.substring(5);
//	                  var html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+row.inventorSum+"</a>"
	                  var html="<a target=_blank href='javascript:void(0)'>"+row.inventorSum+"</a>"
					  return html;
               }
                 function fmrQsSearch(value,row,index){   //发明人趋势
			     	 var rowNcName=value.substring(0,value.indexOf("_"));   
	                if(rowNcName=="0"){
						  return rowNcName;
					 }	  
				     	var searchFormula="${searchFormula}";
				     	  var rowyear=row.year;   
				     	    searchFormula="F XX ("+rowyear.substring(0,4)+"/AD)*("+value.substring(value.indexOf("_")+1,value.length)+"/IN)*"+searchFormula.substring(5);
//		               var  html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+rowNcName+"</a>"
		               var  html="<a target=_blank href='javascript:void(0)'>"+rowNcName+"</a>"
		                   return html;
                }
                function fmrNcSearch(value,row,index){   //发明人国省
			     	 var rowNcName=value.substring(0,value.indexOf("_"));   
	                if(rowNcName=="0"){
						  return rowNcName;
					 }	  
				     	var searchFormula="${searchFormula}";
				     	  var rownc=row.nc;   
				     	    searchFormula="F XX ("+rownc.substring(rownc.indexOf("(")+1,rownc.indexOf(")"))+"/CO)*("+value.substring(value.indexOf("_")+1,value.length)+"/IN)*"+searchFormula.substring(5);
//		               var  html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+rowNcName+"</a>"
		               var  html="<a target=_blank href='javascript:void(0)'>"+rowNcName+"</a>"
		                   return html;
            }
$(function() {
         $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_famingren',
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
    $('#dgFMR').datagrid({
        url:'/front/search/table_getPatentSumData_dataGrid_inventor',
        pagination:true,
        checkbox:true,
        height:930,
        fitColumns:true,
        multiSort:false,
        columns:[[
            {field:'inventorName',title:'发明人',width:9,align:'center'},
            {field:'inventorSum',title:'专利总数',width:9,align:'center',formatter:fmrSearch},
        ]]
    });	
	$('#tt').tabs({
       border:false,
       onSelect:function(title){
         if(title=="发明人趋势分析"){
        	 $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_famingrenQS',
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
		 $('#dgfmrQS').datagrid({
		            url:'/front/search/table_getPatentSumData_dataGrid_fmrQS',
		            pagination:true,
		            checkbox:true,
		            height:930,
		            fitColumns:true,
		            multiSort:false,
		            columns:[[
		                {field:'year',width:9,align:'center'},
		                {field:'fmrName0',width:9,align:'center',formatter:fmrQsSearch},
		                {field:'fmrName1',width:9,align:'center',formatter:fmrQsSearch},
		                {field:'fmrName2',width:9,align:'center',formatter:fmrQsSearch},
		                {field:'fmrName3',width:9,align:'center',formatter:fmrQsSearch},
		                {field:'fmrName4',width:9,align:'center',formatter:fmrQsSearch},
		                {field:'fmrName5',width:9,align:'center',formatter:fmrQsSearch},
		                {field:'fmrName6',width:9,align:'center',formatter:fmrQsSearch},
		                {field:'fmrName7',width:9,align:'center',formatter:fmrQsSearch},
		                {field:'fmrName8',width:9,align:'center',formatter:fmrQsSearch},
		                {field:'fmrName9',width:9,align:'center',formatter:fmrQsSearch},
		            ]]
		        });		
         } 
	       if(title=="发明人国省分析"){
		      	 $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_famingrenNC',
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
			 $('#dgfmrNC').datagrid({
		            url:'/front/search/table_getPatentSumData_dataGrid_fmrNC',
		            pagination:true,
		            checkbox:true,
		            height:930,
		            fitColumns:true,
		            multiSort:false,
		            columns:[[
		                {field:'nc',width:9,align:'center'},
		                {field:'sqrNCName0',width:9,align:'center',formatter:fmrNcSearch},
		                {field:'sqrNCName1',width:9,align:'center',formatter:fmrNcSearch},
		                {field:'sqrNCName2',width:9,align:'center',formatter:fmrNcSearch},
		                {field:'sqrNCName3',width:9,align:'center',formatter:fmrNcSearch},
		                {field:'sqrNCName4',width:9,align:'center',formatter:fmrNcSearch},
		                {field:'sqrNCName5',width:9,align:'center',formatter:fmrNcSearch},
		                {field:'sqrNCName6',width:9,align:'center',formatter:fmrNcSearch},
		                {field:'sqrNCName7',width:9,align:'center',formatter:fmrNcSearch},
		                {field:'sqrNCName8',width:9,align:'center',formatter:fmrNcSearch},
		                {field:'sqrNCName9',width:9,align:'center',formatter:fmrNcSearch},
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
	        <div  title="发明人构成分析" style="padding:10px">
       			<div id="container1" style="min-width: 100px; height: 250px; margin: 0 auto">
       			</div>
            <table id='dgFMR' ></table>    
	        </div>
	          <div title="发明人趋势分析" style="padding:10px">
       			<div id="container2" style="min-width: 100px; height: 250px; margin: 0 auto">
       			</div>
       			<table id='dgfmrQS' ></table>    
	        </div>
	          <div title="发明人国省分析" style="padding:10px">
       			<div id="container3" style="min-width: 100px; height: 250px; margin: 0 auto">
       			</div>
       			<table id='dgfmrNC' ></table>    
	        </div>
    	</div>
</body>
</html>