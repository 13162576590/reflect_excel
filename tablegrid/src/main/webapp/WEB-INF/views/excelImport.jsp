<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html>
<html>
<head>
<title>Table</title>

<script src="/tablegrid/static/lib/jquery.min.js"></script>
<script src="/tablegrid/static/lib/jquery.easyui.min.js"></script>
<script src="/tablegrid/static/lib/underscore-min.js"></script>

<!-- 
目前相对路径不能这样使用，需要在看看怎么处理
<script src="../../static/jquery.min.js"></script> -->



</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		
		<select id="select" >
		  <option value ="ActuaryExtractCoeff">提取系数</option>
		  <option value ="ActuaryInsuation">机构</option>
		</select>

		<button id="template" >下载模板</button>


		<div id="divDatagrid" data-options="region:'center',border:false">
			<table id="dataGrid"></table>
		</div>
			<table id="edit-template" class="user-list">
				<c:forEach items="${items}" var="item" varStatus="status">  
					<c:choose>
					　　<c:when test="${status.index % 2 == 0}">
							<tr>
						</c:when>
					</c:choose>


						<c:choose>
							<c:when test="${item.nullable} == '1'">  
							    <th>${item.fieldName}：</th>
							    <c:choose>
									<c:when test="${item.paramType} == 'class java.lang.String'">  
								   		<td><input style="width:200px;" type="text" data-options="required:true" 
								   			id=${item.fieldProperty} name=${item.fieldProperty} 
								   			missingMessage="该输入项为必录项" placeholder="请输入${item.fieldName}" 
								   			class="easyui-numberspinner" /></td>
								   	</c:when>
								   	<c:when test="${item.paramType} == 'class java.util.Date'">  
								   		<td><input style="width:200px;" data-options="required:true" 
								   		id=${item.fieldProperty} name=${item.fieldProperty} 
								   		missingMessage="该输入项为必录项"  placeholder="点击选择时间" data-options="required:true" 
								   		onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly /></td>
								   	</c:when>
								   	<c:otherwise> 
										<td><input style="width:200px;" type="number" data-options="required:true" 
										id=${item.fieldProperty} name=${item.fieldProperty} 
										missingMessage="该输入项为必录项" placeholder="请输入${item.fieldName}" 
										class="easyui-numberspinner" /></td>
								   	</c:otherwise>
								</c:choose>
							</c:when>
						   	<c:otherwise> 
						   		<th>${item.fieldName}：</th>
						   			<c:when test="${item.paramType} == 'class java.lang.String'">  
								   		<td><input style="width:200px;" type="text" 
								   			id=${item.fieldProperty} name=${item.fieldProperty} 
								   			missingMessage="该输入项为必录项" placeholder="请输入${item.fieldName}" 
								   			class="easyui-numberspinner" /></td>
								   	</c:when>
								   	<c:when test="${item.paramType} == 'class java.util.Date'">  
								   		<td><input style="width:200px;" 
								   		id=${item.fieldProperty} name=${item.fieldProperty} 
								   		missingMessage="该输入项为必录项"  placeholder="点击选择时间" data-options="required:true" 
								   		onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly /></td>
								   	</c:when>
								   	<c:otherwise> 
										<td><input style="width:200px;" type="number"  
										id=${item.fieldProperty} name=${item.fieldProperty} 
										missingMessage="该输入项为必录项" placeholder="请输入${item.fieldName}" 
										class="easyui-numberspinner" /></td>
								   	</c:otherwise>
						  	</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${item.nullable} == '1'">  
							    <th>${item.fieldName}：</th>
								<td><input style="width:200px;" data-options="required:true" id=${item.fieldProperty} name=${item.fieldProperty} missingMessage="该输入项为必录项" placeholder="请输入${item.fieldName}" class="easyui-numberspinner" /></td>
							</c:when>
						   	<c:otherwise> 
						   		<th>${item.fieldName}：</th>
								<td><input style="width:200px;" id=${item.fieldProperty} name=${item.fieldProperty} placeholder="请输入${item.fieldName}" class="easyui-numberspinner" /></td>
						  	</c:otherwise>
						</c:choose>		
									
					<c:choose>
					　　<c:when test="${status.index % 2 == 0}">
							</tr>
						</c:when>
					</c:choose>
				</c:forEach>  
			</table>
	</div>
	<div id="toolbar" style="display: none;">
		<a onclick="addFun();" href="javascript:void(0);"
			class="easyui-linkbutton"
			data-options="plain:true,iconCls:'pencil_add'">新增</a> <a
			href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'brick_add',plain:true" onclick="searchFun();">查询</a>
		<a href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'brick_delete',plain:true"
			onclick="cleanFun();">重置</a>
	</div>
<%-- 	
	<c:forEach items="${tradeList}" var="item" varStatus="status">  
	  <tr >  
	    <td class="center"><span class="center">${item.trade_name}</span></td>  
	    <td><dfn>￥</dfn>${item.trade_price}</td>  
	    <td>${item.trade_count}</td>  
	    <td>${item.trade_time}</td>  
	  </tr>  
	</c:forEach>  
 --%>
	<div id="template-container"></div>
	
	<script type="text/javascript">
	var dataGrid;
	$(function() {
		// 下拉框选择控件，下拉框的内容是动态查询数据库信息
		//$('#comName').combotree({
		//	url : '${pageContext.request.contextPath}/orgController/codeList',
		//	parentField : 'pid',
		//	lines : true,
		//	panelHeight : 400
		//});

		//先清空datagrid  initTable
		$('#divDatagrid').html('<table id="dataGrid"></table>');//动态创建表
		//通过ajax请求生成的新的datagrid的列名
		$.ajax({
			//url : '${pageContext.request.contextPath}/excelController/tableHead?tableName=' +  $("#select ").val() ,
			url : '${pageContext.request.contextPath}/excelController/tableHead',
			type : "get",
			dataType : 'json',
			data : {tableName: $("#select").val()},
			success : function(data) {//获取表头数据成功后，使用easyUi的datagrid去生成表格		         
				var $dataGrid = $('#dataGrid').datagrid({
					url : '${pageContext.request.contextPath}/excelController/dataGrid?tableName=' +  $("#select").val(),
					fitColumns : true,
					singleSelect : true,
					idField : "id",
					columns : getColumns(data),//外层ajax请求的表头json
					queryParams : {},
					onSelect : function(rowIndex, rowData) {
						alert(rowData.appId);
					}//end of onSelect:function
				});
			},
			error : function(xhr) {
				alert('8888');
			}
		});
		
		$('#select').change(function(){			
			//通过ajax请求生成的新的datagrid的列名
			$.ajax({
				//url : '${pageContext.request.contextPath}/excelController/tableHead?tableName=' +  $("#select ").val() ,
				url : '${pageContext.request.contextPath}/excelController/tableHead',
				type : "get",
				dataType : 'json',
				data : {tableName: $("#select").val()},
				success : function(data) {//获取表头数据成功后，使用easyUi的datagrid去生成表格

					var $dataGrid = $('#dataGrid').datagrid({
						url : '${pageContext.request.contextPath}/excelController/dataGrid?tableName=' +  $("#select").val(),
						fitColumns : true,
						singleSelect : true,
						idField : "id",
						columns : getColumns(data),//外层ajax请求的表头json
						queryParams : {},
						onSelect : function(rowIndex, rowData) {
							alert(rowData.appId);
						}//end of onSelect:function
					});
				},
				error : function(xhr) {
					alert('8888');
				}
			});
		
		}) 
	
		$("#template").click(function(){
            window.open('${pageContext.request.contextPath}/excelController/download?tableName=' + $("#select").val());
		});

		function getColumns(data) {
			console.log("getColumns");

			return getTableHead(tableHeadSort(data));
		}
		
		//表头排序
		function tableHeadSort(data) {
			console.log("tableHeadSort start");
			console.log(data);

			var times = 0;
			for (var i = 0; i < data.length - 1; i++) {
				for (var j = i + 1; j < data.length; j++) {
					if (data[i].colNo > data[j].colNo) {//如果前面的数据比后面的大就交换  
						var temp = data[i];
						data[i] = data[j];
						data[j] = temp;
					}
					//console.log("第" + (++times) + "次排序后：" + data);
				}
			}
			console.log("tableHeadSort end");
			console.log(data);
			
			return data;
		}

		//加载columns和data数据 用于重新加载页面时第一次生成表格确定表头数据
		//注意：此方法仅适用于第一次加载页面和重新加载页面生成表格 在点击下一页等使用分页功能时若使用此方法加载数据会导致表格重新生成，分页功能不可用 故在分页时使用processparam1方法只加载tbody数据 不重新生成表格
		function getTableHead(data) {
			console.log("getTableHead");

			var array = [];
			var columns = [];
			$(data).each(function(index, item) {
				array.push({
					field : item.fieldProperty,
					title : item.fieldName,
					width : 100,
					align : 'center'
				});//初始化表头的属性
			});
			
			console.log("getTableHead---");

			/* var successData = {
				total : data.total,//总记录数
				rows : data.mapputs,//每页显示行数
			}; */
			
			//考虑联合主键情况
		/* 	var keys = [];
			$(data).each(function(index, item) {
				array[index]['field'] = item.fieldProperty;//field赋值 注意field 一定要与 后台传来的mapputs集合里mapput的key 相等!
				array[index]['title'] = item.fieldName;//title赋值
				keys.push(item);
			});	 */		

			var operator = {
					field : 'action',
					title : '操作',
					width : 60,
					formatter : function(value, row, index) {
						var str = '';
						if ($.canEdit) {
							str += $.formatString('<img onclick="editFun(\'{0}\');" src="{1}" title="编辑"/>', row.id,
											'${pageContext.request.contextPath}/style/images/extjs_icons/pencil.png');
						}
						str += '&nbsp;&nbsp;&nbsp;';
						if ($.canDelete) {
							str += $.formatString('<img onclick="deleteFun(\'{0}\');" src="{1}" title="删除"/>', row.id,
											'${pageContext.request.contextPath}/style/images/extjs_icons/cancel.png');
						} 
						return str;
					}
			};
			array.push(operator);
			columns.push(array);	
			
			return columns;
		}

	});
</script>
	


</body>
</html>