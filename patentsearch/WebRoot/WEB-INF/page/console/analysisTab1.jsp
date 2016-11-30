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
		     function apdSearch(value,row,index){   //按申请年份检索专利信息
		     	var searchFormula="${searchFormula}";
		     	 var rowyear=row.year;   
		     	    searchFormula="F XX ("+rowyear.substring(0,4)+"/AD)*"+searchFormula.substring(5);
                // var html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+row.apdSum+"</a>"
                 var html="<a target=_blank href='javascript:void(0)'>"+row.apdSum+"</a>"
				  return html;
            }
             function pubSearch(value,row,index){   //按公开年份检索专利信息
             	var searchFormula="${searchFormula}";
		     	 var rowyear=row.year;   
		     	    searchFormula="F XX ("+rowyear.substring(0,4)+"/PD)*"+searchFormula.substring(5);
               //  var html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+row.pubSum+"</a>"
                 var html="<a target=_blank href='javascript:void(0)'>"+row.pubSum+"</a>"
				  return html;
            }
             function appdSearch(value,row,index){   //按授权年份检索专利信息
             	var searchFormula="${searchFormula}";
		     	 var rowyear=row.year;   
		     	    searchFormula="F XX ("+rowyear.substring(0,4)+"/GD)*"+searchFormula.substring(5);
                 //var html="<a target=_blank href='/front/search/table_tableSearch?searchFormula="+searchFormula+"'>"+row.appdSum+"</a>"
                 var html="<a target=_blank href='javascript:void(0)'>"+row.appdSum+"</a>"
				  return html;
            }
$(function() {
         $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData',
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
		     $('#dg').datagrid({
                    url:'/front/search/table_getPatentSumData_dataGrid',
                    pagination:true,
                    checkbox:true,
                    height:930,
                    fitColumns:true,
                    multiSort:false,
                    columns:[[
                        {field:'year',width:9,align:'center'},
                        {field:'apdSum',width:9,align:'center',formatter:apdSearch},
                        {field:'pubSum',width:9,align:'center',formatter:pubSearch},
                         {field:'appdSum',width:9,align:'center',formatter:appdSearch},
                    ]]
				
                });	
	$('#tt').tabs({
       border:false,
       onSelect:function(title){
         if(title=="申请趋势分析"){
        	 $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_shenqing',
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
	     $('#dgSQ').datagrid({
             url:'/front/search/table_getPatentSumData_dataGrid_shenqing',
             pagination:true,
             checkbox:true,
             height:930,
             fitColumns:true,
             multiSort:false,
             columns:[[
                 {field:'year',title:'年份',width:9,align:'center'},
                 {field:'apdSum',title:'申请总数',width:9,align:'center',formatter:apdSearch},
             ]]
         });	
         } 
	       if(title=="公开趋势分析"){
		      	 $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_gongkai',
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
			   $('#dgGK').datagrid({
                    url:'/front/search/table_getPatentSumData_dataGrid_gongkai',
                    pagination:true,
                    checkbox:true,
                    height:930,
                    fitColumns:true,
                    multiSort:false,
                    columns:[[
                        {field:'year',title:'年份',width:9,align:'center'},
                        {field:'pubSum',title:'公开总数',width:9,align:'center',formatter:pubSearch},
                    ]]
				
                });	
		  }
		    if(title=="授权趋势分析"){
		      	 $.ajax( {
				type : "post",
				url : '/front/search/table_getPatentSumData_shouquan',
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
			   $('#dgQS').datagrid({
                    url:'/front/search/table_getPatentSumData_dataGrid_shouquan',
                    pagination:true,
                    checkbox:true,
                    height:930,
                    fitColumns:true,
                    multiSort:false,
                    columns:[[
                        {field:'year',title:'年份',width:9,align:'center'},
                         {field:'appdSum',title:'授权总数',width:9,align:'center',formatter:appdSearch},
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
	        <div  title="总体趋势分析" style="padding:10px">
       			<div id="container1" style="min-width: 100px; height: 250px; margin: 0 auto">
       			</div>
                 <table id='dg' ></table>
       			
	        </div>
	          <div title="申请趋势分析" style="padding:10px">
       			<div id="container2" style="min-width: 100px; height: 250px; margin: 0 auto">
       			</div>
       			 <table id='dgSQ' ></table>
	        </div>
	          <div title="公开趋势分析" style="padding:10px">
       			<div id="container3" style="min-width: 100px; height: 250px; margin: 0 auto">
       			</div>
       			 <table id='dgGK' ></table>
	        </div>
	          <div title="授权趋势分析" style="padding:10px">
       			<div id="container4" style="min-width: 100px; height: 250px; margin: 0 auto">
       			</div>
       			 <table id='dgQS' ></table>
	        </div>
    	</div>
</body>
</html>