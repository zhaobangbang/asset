package util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private static SessionFactory  factory;
	
	static{
		Configuration config= new Configuration().configure();
		factory = config.buildSessionFactory();
		
		//当虚拟机关闭时执行该任务
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			
			public void run() {
				System.out.println("虚拟机关闭了，资源释放");
				factory.close();
			}
		}));
	}
	
	public static Session openSession(){
		 Session session = factory.openSession();
		 return session;
	}
	
	public static Session getCurrentSession(){
		 Session session = factory.getCurrentSession();
		 return session;
	}
	
	public static void main(String[] args){
		Session s = openSession();
	}

}
