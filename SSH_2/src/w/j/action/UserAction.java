package w.j.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import w.j.UserService.UserService;
import w.j.domain.Contact;
import w.j.domain.User;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
@SuppressWarnings("serial")
public class UserAction  extends ActionSupport implements ModelDriven<User> {
	
	private User user = new User();
	private Contact contact= new Contact();
	private List<Contact> list;
	
	public List<Contact> getList() {
		return list;
	}
	public void setList(List<Contact> list) {
		this.list = list;
	}
	
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getUser() {
		return user;
	}
	
	private UserService userService;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public String login()
	{
		List<User>list= userService.login(user.getName(),user.getPassword());		
		User u = list.get(0);
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute("user", u);
		if(list!=null&&!list.equals("")){
		return SUCCESS;}
	   return null;
	}
	public String showAll()
	{
	    list=userService.showAll();
		return SUCCESS;
	}
	public String deleteOne()
	{
		@SuppressWarnings("unchecked")
		Map<String,Object> request = (Map<String, Object>) ActionContext.getContext().get("request");
		int id = (Integer) request.get("id");
		userService.delete(id);
		return SUCCESS;
	}
	/*public String findByRelation()
	{
		Map<String,Object> request = (Map<String, Object>) ActionContext.getContext().get("request");
		String relation=(String) request.get("relation");	
		System.out.println(relation);		
		list=userService.findByRelation(relation);		
		return SUCCESS;
	}*/
	/*public String addContact()
	{
		HttpSession session = ServletActionContext.getRequest().getSession();
		User u =(User) session.getAttribute("user");      
	        contact.setUser(u);	     
            userService.addContact(contact);
		return SUCCESS;
	}*/
	public User getModel() {
		return user;
	}

}
