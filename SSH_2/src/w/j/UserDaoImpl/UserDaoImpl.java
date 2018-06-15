package w.j.UserDaoImpl;

import java.util.List;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;



import w.j.UserDao.UserDao;
import w.j.domain.Contact;
import w.j.domain.User;

public class UserDaoImpl  extends HibernateDaoSupport implements UserDao {

	public void save(User user) {
	
		this.getHibernateTemplate().save(user);		
	}
	public List<User> findOneUser(String name, String password) {
    List<User> list=(List) this.getHibernateTemplate().find("from User u where u.name=? and u.password=?",new String[]{name,password});	
             return  list;		
	}
	public List<Contact> showAll() {
		Session session =this.getSessionFactory().openSession();
		String hql="from Contact";
		List<Contact> list= session.createQuery(hql).list();     
		return list;	
	}
	public void delete(Integer id) {
		Contact  contact=this.getHibernateTemplate().load(Contact.class, id);
		getHibernateTemplate().delete(contact);
	}
	public List<Contact> findByRelation(String relation) {
		 List<Contact> list=(List) this.getHibernateTemplate().find("from Contact c where c.relation=?",relation);	
		     return list;
	}
	public void save(Contact contact) {  
    	this.getHibernateTemplate().save(contact);		
	}

}
