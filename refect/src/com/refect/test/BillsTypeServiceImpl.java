package com.refect.test;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daily.life.admin.dao.IBillsTypeRepository;
import com.daily.life.admin.dao.impl.BillsTypeRepository;
import com.daily.life.admin.model.BillType;
import com.daily.life.admin.service.IBillsTypeService;
import com.daily.life.admin.util.ReflectionUtils;
import com.daily.life.admin.vo.BillTypeVo;

@Service
public class BillsTypeServiceImpl implements IBillsTypeService {

    @Autowired
    private IBillsTypeRepository billsTypeRepository;

    @Override
    public boolean editBillsType(BillTypeVo billTypeVo) {
//        BillType billType = new BillType();
//        billType.setCode(billTypeVo.getCode());
//        billType.setName(billTypeVo.getName());
//        if (StringUtils.isBlank(billTypeVo.getId())) {
//            billType.setCreated(new Date());
//            billType = billsTypeRepository.insert(billType);
//            if (billType.getId() != null) {
//                return true;
//            }
//            return false;
//        } else {
//            billType.setId(billTypeVo.getId());
//            return billsTypeRepository.updateBillsType(billType);
//        }
    	
		String className = "com.daily.life.admin.dao.impl.BillsTypeRepository";
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
			Object obj = clazz.newInstance();
    		
	        String methodName = "insert";
	        
	        Class<?> interfaces[] = clazz.getInterfaces();//获得Dog所实现的所有接口
	                try {
						setInterfaceInfo(clazz);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
    }

    @Override
    public List<BillType> findAll() {
        return billsTypeRepository.findAll();
    }
    
	public static void main(String[] args) throws Exception {

        BillType billType = new BillType();
        billType.setId("111111111111111");

        billType.setCode("1111");
        billType.setName("11");
        
        // TODO Auto-generated method stub
		String className = "com.daily.life.admin.dao.impl.BillsTypeRepository";
		Class<?> clazz = Class.forName(className);
		Object obj = clazz.newInstance();
        		
        String methodName = "insert";
        
        Class<?> interfaces[] = clazz.getInterfaces();//获得Dog所实现的所有接口
        
        
//        IBillsTypeRepository bb = new BillsTypeRepository();
        
//        bb.insert(billType);
        
        //        setInterfaceInfo(clazz);
//        for (Class<?> inte : interfaces) {//打印
//            System.out.println("Dog实现接口："+inte);
//
////    		Object obj1 = inte.newInstance(){};
//    		
////    		System.out.println(obj1);
//
//        	Method[] methods = inte.getMethods();
////        	inte.get
//        	
//        	   for (Method method: methods) {
////        		   if (methodName.equals(method.getName())) {
//			            System.out.println(method.getName());
//			//        	Method method = inte.getDeclaredMethod(methodName, BillType.class);
//			        	System.out.println(method);
////			        	method.invoke(obj, billType);
////        		   }
////        	method.invoke(obj1, billType);
////               	method.invoke(obj, billType);
//               }
//        		   
//        }
        
        
        //调用父类的公共方法  
//        ReflectionUtils.invokeMethod(obj, methodName, new Class[] { BillType.class }, new Object[]{ billType }) ; 
        
//        Method methods = ReflectionUtils.getDeclaredMethod(obj, methodName,  BillType.class);
//    	System.out.println(methods);
//
//        
//    	methods.invoke(obj, billType);
//		cls.getDeclaredMethods();  
//	    cls.getMethods();  
        
        
//        for (Method method: methods) {
//        	System.out.println(method.getName());
//        }
        
//		Method method = obj.getClass().getDeclaredMethod(methodName, BillType.class);
//		method.invoke(obj, billType);
		
        
//        Method[] methods = obj.getClass().getDeclaredMethods();
////		method.invoke(obj, billType);
////		cls.getDeclaredMethods();  
////	    cls.getMethods();  
//        
//        for (Method method: methods) {
//        	System.out.println(method.getName());
//        }
        
		//
//		Field[] fields = clazz.getDeclaredFields();

//		for (Map<String, Object> data : datas) {
//			Object obj = clazz.newInstance();
//
//			for (AesConfig aesConfig : aesConfigs) {
//				PropertyDescriptor pd = new PropertyDescriptor(aesConfig.getFiledProperty(), clazz);
//				Method method = pd.getWriteMethod();
//				Type[] type = method.getGenericParameterTypes();
//				// 只要一个参数
//				String clz = type[0].toString();
////				System.out.println(clz);
//
//				// method.invoke(obj, data.get(aesConfig.getFiledName()));
//				Test.setInvoke(method, obj, data.get(aesConfig.getFiledName()), clz);
//			}
//			 System.out.println(obj);
//
//		}

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
	
	public static void setInterfaceInfo(Class<?> clazz) throws Exception {
        //1.获取所继承的所有接口
        Class<?>[] interfaces = clazz.getInterfaces();
        //2.接口中的方法是在子类中进行调用的，所以需要获取子类的实例化对象
        Object obj = clazz.newInstance();
        for(int i=0;i<interfaces.length;i++){
            //3.获取接口中所有的方法
            Method[] methods = interfaces[i].getMethods();
            for(int j=0;j<methods.length;j++){
                //4.获取方法中所有的参数
                Class<?>[] parameterTypes = methods[j].getParameterTypes();
                //用于保存要传递的数值
                Object[] values = new Object[parameterTypes.length];
                String simplename="";
                for(int k=0;k<parameterTypes.length;k++){
                    //5.根据types进行传值
//                   values[k]=StringUtils.getValues(parameterTypes[k].getSimpleName());
                	System.out.println(methods[j].getName());
            		System.out.println(parameterTypes[k]);

                	if ("insert".equals(methods[j].getName())) {
                		if(!"interface java.lang.Iterable".equals(parameterTypes[k].toString())) {
	                        Method method = clazz.getMethod(methods[j].getName(), parameterTypes);
	                    	System.out.println(parameterTypes[k]);
	                    	System.out.println("1111111111111");
	                        BillType billType = new BillType();
	                        billType.setId("1111111111111111111");
	                        billType.setCode("1111");
	                        billType.setName("11");
	                        
	                      
	                        method.invoke(obj,billType);
	                	}
                	}
                }

                //6.最重要的一步：是子类进行调用方法，所以需要通过子类的class获取方法
                Method method = clazz.getMethod(methods[j].getName(), parameterTypes);
//                method.invoke(obj,values);
            }
            //7获取接口中的相关属性
            Field[] fields = interfaces[i].getFields();
            for(int n=0;n<fields.length;n++){
                fields[i].setAccessible(true);
                System.out.println(fields[n].getName()+"="+fields[n].get(obj));
            }
        }
    }
}


