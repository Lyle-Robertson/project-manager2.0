
public class Person {
	//Class attributes
	private String designation;
	private String name;
	private String number;
	private String email;
	private String address;
	private String projName;
	
	// constructor	
	public Person(String designation, String name, String number, String email, String address, String projName) {
			
		this.designation = designation;
		this.name = name;
		this.number = number;
		this.email = email;
		this.address = address;
		this.projName = projName;
		}
	
	// Setters
	public void setName(String newName) {
		name = newName;
	}
	
	public void setNumber(String newNumber) {
		number = newNumber;
	}
	
	public void setEmail(String newEmail) {
		email = newEmail;
	}
	
	public void setAddress(String newAddress) {
		address = newAddress;
	}
	
	public void setProjname(String newProjName) {
		projName = newProjName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getNumber() {
		return number;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getDesignation() {
		return designation;
	}
	
	public String getProjName() {
		return projName;
	}
	
	// method
	public String toString() {
		return 	"\n\n" + designation +
				"\nName:\t\t\t" + name +
				"\nNumber:\t\t\t" + number +
				"\nEmail:\t\t\t" + email +
				"\nAddress:\t\t" + address + 
				"\nAssigned to:\t\t" + projName +  "\n";	
		
	}
}