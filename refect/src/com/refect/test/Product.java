package com.refect.test;

import java.util.Date;



@SuppressWarnings("serial")
public class Product implements java.io.Serializable {

	// Fields
	private String name;
	private String age;
	private int height;
	private Long width;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Long getWidth() {
		return width;
	}

	public void setWidth(Long width) {
		this.width = width;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", age=" + age + ", height=" + height + ", width=" + width + "]";
	}

}