package w.j.UserService;

import java.util.List;


import w.j.domain.Contact;
import w.j.domain.User;

public interface UserService {

	public void regist(User user);
	
	public List<User> login(String name,String password);

	public List<Contact> showAll();
	
	public void delete(Integer id);

	public List<Contact> findByRelation(String relation);

	public void addContact(Contact contact);
}
