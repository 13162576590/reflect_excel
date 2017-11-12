package com.refect.test;

public Test2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if(xclass.equals("java.lang.String")) 
		{ 
		setMethod.invoke(tObject, cell.getStringCellValue()); 
		} 
		else if(xclass.equals("java.util.Date")) 
		{ 
		setMethod.invoke(tObject, sf.parse(cell.getStringCellValue())); 
		} 
		else if(xclass.equals("java.lang.Boolean")) 
		{ 
		Boolean boolname=true; 
		if(cell.getStringCellValue().equals("否")) 
		{ 
		boolname=false; 
		} 
		setMethod.invoke(tObject,boolname ); 
		} 
		else if(xclass.equals("java.lang.Integer")) 
		{ 
		setMethod.invoke(tObject,new Integer( cell.getStringCellValue())); 
		} 

		else if(xclass.equals("java.lang.Long")) 
		{ 
		setMethod.invoke(tObject,new Long( cell.getStringCellValue())); 
		} 
		} 
		// 下一列
	
	}

}
