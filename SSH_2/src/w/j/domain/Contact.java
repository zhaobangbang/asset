package w.j.domain;
public class Contact {

	 private Integer id;
	 private String  name;
	 private String sex;
	 private String phone;
	 private String address;
	 private String mobilePhone;
	 private String company;
	 private String comPhone;
	 private String comAddress;
	 private String relation;	 
	 private  User user;
	 
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getComPhone() {
		return comPhone;
	}
	public void setComPhone(String comPhone) {
		this.comPhone = comPhone;
	}
	public String getComAddress() {
		return comAddress;
	}
	public void setComAddress(String comAddress) {
		this.comAddress = comAddress;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Contact [id=" + id + ", name=" + name + ", sex=" + sex
				+ ", phone=" + phone + ", address=" + address
				+ ", mobilePhone=" + mobilePhone + ", company=" + company
				+ ", comPhone=" + comPhone + ", comAddress=" + comAddress
				+ ", relation=" + relation + "]";
	}
	public Contact(Integer id, String name, String sex, String phone,
			String address, String mobilePhone, String company,
			String comPhone, String comAddress, String relation) {
		super();
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.phone = phone;
		this.address = address;
		this.mobilePhone = mobilePhone;
		this.company = company;
		this.comPhone = comPhone;
		this.comAddress = comAddress;
		this.relation = relation;
	}
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
   
}
