package w.j.UserServiceImpl;

import java.util.List;

import w.j.UserDao.UserDao;

import w.j.UserService.UserService;
import w.j.domain.Contact;
import w.j.domain.User;

public class UserServiceImpl  implements UserService{

	private  UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void regist(User user) {
		userDao.save(user);
		
	}

	public List<User> login(String name, String password) {
     List<User> list=	userDao.findOneUser(name, password);
		return list;
	}

	public List<Contact> showAll() {
	   return 	userDao.showAll();
		
	}

	public void delete(Integer id) {
		userDao.delete(id);
		
	}

	public List<Contact> findByRelation(String relation) {
		return  userDao.findByRelation(relation);
		
	}

	public void addContact(Contact contact) {
		userDao.save(contact);
		
	}

}
