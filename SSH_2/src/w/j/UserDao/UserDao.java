package w.j.UserDao;

import java.util.List;


import w.j.domain.Contact;
import w.j.domain.User;

public interface UserDao {

	public void save(User user);
	
	public List<User> findOneUser(String name,String password);

	public List<Contact> showAll();
	
	public void delete(Integer id);
	
	public List<Contact> findByRelation(String relation);
	
	public void save(Contact contact);
}
