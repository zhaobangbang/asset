package w.j.domain;

import java.util.HashSet;
import java.util.Set;

public class User {

	private Integer id;
	private  String name;
	private String password;
	
	private Set<Contact>  contacts =new  HashSet<Contact>();
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Set<Contact> getContacts() {
		return contacts;
	}
	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password
				+ "]";
	}
	public User(Integer id, String name, String password, Set<Contact> contacts) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.contacts = contacts;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
