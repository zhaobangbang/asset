package com.lansitec.common;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class InitUploadContext {
	public static SpringContextHelper helper;
    public static  ApplicationContext context;
    public InitUploadContext(){
    	context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        helper = (SpringContextHelper) context.getBean("springContextHelper");
    }
	public static SpringContextHelper getHelper() {
		return helper;
	}
	public static ApplicationContext getContext() {
		return context;
	}
	public static void setHelper(SpringContextHelper helper) {
		InitUploadContext.helper = helper;
	}
	public static void setContext(ApplicationContext context) {
		InitUploadContext.context = context;
	}
    
}
