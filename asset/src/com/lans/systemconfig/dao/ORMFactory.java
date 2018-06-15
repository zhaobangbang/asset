package com.lans.systemconfig.dao;

import java.net.InetAddress;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansi.thirdparty.zwwl.msgSender.ZwwlMsgSender;

import sun.net.util.IPAddressUtil;

public class ORMFactory {
	static Logger logger = LoggerFactory.getLogger(ORMFactory.class);
	public static boolean localDebug = false;
	public static boolean officialEnv = false;
	private static Configuration config;
	private static ServiceRegistry serviceRegistry;
	private static SessionFactory sessionFactory;
	
	/*
	 *127.0.0.0/8��127.0.0.0��127.255.255.255 
	 *172.16.0.0/12��172.16.0.0��172.31.255.255 
	 *192.168.0.0/16��192.168.0.0��192.168.255.255
	 */
	public static boolean internalIp(String ip) {
	    byte[] addr = IPAddressUtil.textToNumericFormatV4(ip);
	    return internalIp(addr);
	}
	public static boolean internalIp(byte[] addr) {
	    final byte b0 = addr[0];
	    final byte b1 = addr[1];
	    //127.x.x.x/8
	    final byte SECTION_1 = (byte)0x7F;
	    final byte SECTION_11 = (byte)0x64;
	    //172.16.x.x/12
	    final byte SECTION_2 = (byte) 0xAC;
	    final byte SECTION_3 = (byte) 0x10;
	    final byte SECTION_4 = (byte) 0x1F;
	    //192.168.x.x/16
	    final byte SECTION_5 = (byte) 0xC0;//-64
	    final byte SECTION_6 = (byte) 0xA8;//-88
	    //����IP��10.31.84.143(test)/����IP��10.129.5.65(official)
	    final byte SECTION_7 = (byte)0x0A;//10
	    final byte SECTION_8 = (byte)0x1F;//31
	    final byte SECTION_9 = (byte)0x81;//-127
	    
	    
	    
	    switch (b0) {
	        case SECTION_1:
	        case SECTION_11:
	            return true;
	        case SECTION_2:
	            if (b1 >= SECTION_3 && b1 <= SECTION_4) {
	                return true;
	            }
	        case SECTION_5:
	            switch (b1) {
	                case SECTION_6:
	                    return true;
	            }
	        case SECTION_7:
	        	switch (b1) {
                case SECTION_8:
                	ZwwlMsgSender.JudgeRunEnviroment(false);
                	officialEnv = false;
                    return false;
                case SECTION_9:
                	ZwwlMsgSender.JudgeRunEnviroment(true);
                	officialEnv = true;
                	return  false;
            }
	        default:
	            return false;
	    }
	}
	
	public static void initialize() {
		InetAddress ia = null;
		String fileName = "hibernate.cfg.xml";
        try {
            ia = InetAddress.getLocalHost();
            String localIp = ia.getHostAddress();
            logger.info("local ip address {}", localIp);
            if (internalIp(localIp) == true) {
            	fileName = "hibernate.cfg.local.xml";
            	localDebug = true;
            	ZwwlMsgSender.JudgeRunEnviroment(false);
            }
            else
            {
            	if(officialEnv == false)
            		fileName = "hibernate.cfg.dev.xml";	
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		//��û��cfg.configure()���ȡ����hibernate.properties�����ļ������������Ϣ
		config = new Configuration().configure(fileName);
		//Hibernate4������һ��ServiceRegistry�ӿڣ�֮ǰ�����û��߷���ȶ���Ҫ��ServiceRegistryע��������Ч
		//������Ҫ����һ��ServiceRegistry���󣬲���������Ϣ����ע�ᣬ
		//Ȼ��Configuration������ݴ����ServiceRegistry�����л�ȡ������Ϣ����SessionFactory
		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
		sessionFactory = config.buildSessionFactory(serviceRegistry);
	}

	public static Session getCurrSession() {
		return sessionFactory.getCurrentSession();
	}

	public static Session openSession() {
		return sessionFactory.openSession();
	}
}
