package com.refect.test;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

	public static void main(String[] args) throws Exception {

		// String[][] data = { { "name1", "1", "11", "111" }, { "name2", "2",
		// "22", "222" },
		// { "name3", "3", "33", "333" } };

		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();

		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("姓名", "name1");
		map1.put("年龄", "1");
		map1.put("高度", "11");
		map1.put("宽度", "111");

		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("姓名", "name2");
		map2.put("年龄", "2");
		map2.put("高度", "22");
		map2.put("宽度", "222");

		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("姓名", "name3");
		map3.put("年龄", "3");
		map3.put("高度", "33");
		map3.put("宽度", "333");

		datas.add(map1);
		datas.add(map2);
		datas.add(map3);

		AesConfig aesConfig1 = new AesConfig();
		AesConfig aesConfig2 = new AesConfig();
		AesConfig aesConfig3 = new AesConfig();
		AesConfig aesConfig4 = new AesConfig();

		aesConfig1.setFiledName("姓名");
		aesConfig1.setFiledProperty("name");
		aesConfig1.setClazz("java.lang.String");

		aesConfig2.setFiledName("年龄");
		aesConfig2.setFiledProperty("age");
		aesConfig2.setClazz("java.lang.String");

		aesConfig3.setFiledName("高度");
		aesConfig3.setFiledProperty("height");
		aesConfig3.setClazz("int");

		aesConfig4.setFiledName("宽度");
		aesConfig4.setFiledProperty("width");
		aesConfig4.setClazz("java.lang.Long");

		List<AesConfig> aesConfigs = new ArrayList<AesConfig>();
		aesConfigs.add(aesConfig1);
		aesConfigs.add(aesConfig2);
		aesConfigs.add(aesConfig3);
		aesConfigs.add(aesConfig4);

		String[] ps = { "name", "age", "height", "width", "date" };
		// TODO Auto-generated method stub
		String className = "com.refect.test.Product";
		Class<?> clazz = Class.forName(className);
		//
		Field[] fields = clazz.getDeclaredFields();

		for (Map<String, Object> data : datas) {
			Object obj = clazz.newInstance();

			for (AesConfig aesConfig : aesConfigs) {
				PropertyDescriptor pd = new PropertyDescriptor(aesConfig.getFiledProperty(), clazz);
				Method method = pd.getWriteMethod();
				Type[] type = method.getGenericParameterTypes();
				// 只要一个参数
				String clz = type[0].toString();
//				System.out.println(clz);

				// method.invoke(obj, data.get(aesConfig.getFiledName()));
				Test.setInvoke(method, obj, data.get(aesConfig.getFiledName()), clz);
			}
			 System.out.println(obj);

		}

		// 写数据
		// for (Field f : fields) {
		// PropertyDescriptor pd = new PropertyDescriptor(f.getName(), clazz);
		// System.out.println(f.getName());
		// Method method = pd.getWriteMethod();// 获得写方法
		// System.out.println(method);
		//// method.invoke(obj, 2);//
		// 因为知道是int类型的属性，所以传个int过去就是了。。实际情况中需要判断下他的参数类型
		// }

		// 获取方法
		// Method m = obj.getClass().getDeclaredMethod(methodName,
		// String.class);
		// 调用方法
		// String result = (String) m.invoke(obj, "aaaaa");
		// System.out.println(result);

	}

	public static void setInvoke(Method setMethod, Object obj, Object value, Object paramType) throws Exception {
		if (paramType.equals("class java.lang.String")) {
			setMethod.invoke(obj, (String) value);
		} else if (paramType.equals("class java.util.Date")) {
			setMethod.invoke(obj, new Date());
		} else if (paramType.equals("class java.lang.Boolean")) {
//			Boolean boolname = true;
//			if (cell.getStringCellValue().equals("否")) {
//				boolname = false;
//			}
//			setMethod.invoke(obj, boolname);
		} else if (paramType.equals("class java.lang.Integer")) {
			setMethod.invoke(obj, new Integer((String)value));
		}

		else if (paramType.equals("class java.lang.Long")) {
			setMethod.invoke(obj, new Long((String)value));
		}
	}

}
