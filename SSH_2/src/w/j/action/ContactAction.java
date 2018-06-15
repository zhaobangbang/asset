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
public class ContactAction extends ActionSupport implements ModelDriven<Contact>{

	
	private Contact  contact =new Contact();
	private UserService userService;
	private List<Contact> list;
	public List<Contact> getList() {
		return list;
	}
	public void setList(List<Contact> list) {
		this.list = list;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public String findByRelation()
	{
	@SuppressWarnings("unchecked")
	Map<String,Object> request = (Map<String, Object>) ActionContext.getContext().get("request");
		String relation=(String) request.get("relation");
		System.out.println(relation);
		list=userService.findByRelation(relation);
		return SUCCESS;
	}
	public String addContact()
	{
		HttpSession session = ServletActionContext.getRequest().getSession();
		   User u =(User) session.getAttribute("user");   
		        contact.setUser(u);
	            userService.addContact(contact);
		        return SUCCESS;
	}
	public Contact getModel() {
		return contact;
	}

}
