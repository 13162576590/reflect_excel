# reflect_excel
reflect_excel

1.tablegrid

a.SpringMVC 3.2 传数据到后台时，数组值是空问题

前端请求:
var params = [];

for (var i=0; i< 2; i++) {
	params.push({
		id : 'id' + i,
		name : 'name' + i,
		address : 'address' + i
	});

}

$.ajax({
	url : '${pageContext.request.contextPath}/excelController/array/params',
	type : "post",
	dataType : 'json',
	data : JSON.stringify(params),  //必须对参数进行转化
	contentType: 'application/json;charset=utf-8',  //必须加上，以json格式，angular.js默认就是该格式
	success : function(data) {//获取表头数据成功后，使用easyUi的datagrid去生成表格

		
	},
	error : function(xhr) {
		alert('8888');
	}
});

//后台spring配置文件也必须配置
<mvc:annotation-driven>
        <mvc:message-converters register-defaults="true">
            <ref bean="fastJsonHttpMessageConverter"/>
        </mvc:message-converters>
</mvc:annotation-driven> 

<bean id="fastJsonHttpMessageConverter" class="com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter"> 
        <property name="supportedMediaTypes">
            <list>
                <value>application/json;charset=UTF-8</value>
                <value>text/html;charset=UTF-8</value>
                <value>text/plain;charset=UTF-8</value>
            </list>
        </property>
</bean>

//Controller如下:
/**
 * 接收一个数组
 * @param users
 */
@RequestMapping("/array/params")
public Json arrayParams(@RequestBody User[] users) { //@RequestBody,必须加上@RequestBody，否则进不来，报500错误
    Json json = new Json();

	System.out.println(users);
	
	System.out.println(users);
	
	return json;
	
}

b.SpringMVC 4.2时没后台spring配置文件不配置也能成功

注意：控制器不加 @RequestBody 报错500如下

严重: Servlet.service() for servlet [DispatcherServlet] in context with path [/tablegrid] threw exception [Request processing failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [[Lcom.cyhong.model.User;]: No default constructor found; nested exception is java.lang.NoSuchMethodException: [Lcom.cyhong.model.User;.<init>()] with root cause


去掉@RequestBody，给User加上无参构造函数也还是不行


