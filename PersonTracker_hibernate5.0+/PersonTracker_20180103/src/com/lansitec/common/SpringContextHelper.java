package com.lansitec.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextHelper implements ApplicationContextAware {

    private ApplicationContext context;
    
    
    //提供一个接口，获取容器中的Bean实例，根据名称获取
    public Object getBean(String beanName)
    {
        return context.getBean(beanName);
    }
    
    @Override
    public void setApplicationContext(ApplicationContext context)
            throws BeansException {
        this.context = context;
        
    }

}
