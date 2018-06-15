package test;



import java.util.List;

import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import util.HibernateUtil;
import w.j.UserService.UserService;
import w.j.domain.Contact;
import w.j.domain.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestApp {
	
	@Autowired
	private UserService userService;
	
	@Test
	public void test1()
	{
		User user = new User();
		
		
		user.setName("王小四");
		user.setPassword("333");
		userService.regist(user);
	}
	@Test
	public void test12()
	{
	List<User>list = userService.login("王小四", "333");
	System.out.println(list);
	}
	@Test
	public void test2()
	{
	List<Contact>list = userService.findByRelation("1");
	System.out.println(list);
	}
	@Test
	public void test20()
	{	
		Contact  contact=new Contact();
		User  user=new User();
		user.setId(4);	
	List<Contact>list = userService.findByRelation("1");
	System.out.println(list);
	}

}
