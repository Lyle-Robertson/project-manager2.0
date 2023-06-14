public class ProjectDetails {

	//Class attributes
	private int projectNumber;
	private int erfNumber;
	private float totalCost;
	private float totalPaid;
	private float amountOutstanding;
	private String projectName;
	private String buildingType;
	private String projectAddress;
	private String dueDate;
	private Person customer;
	private Person architect;
	private Person structuralEng;
	private String complete;
	private String completionDate;
	
	
	// constructor
	public ProjectDetails(int projectNumber, String projectName, String buildingType,
							String projectAddress, int erfNumber, float totalCost, float totalPaid, float amountOutstanding,
							String dueDate, Person customer, Person architect, Person structuralEng,
							String complete, String completionDate) {
		
		this.projectNumber = projectNumber;
		this.projectName = projectName;
		this.buildingType = buildingType;
		this.projectAddress = projectAddress;
		this.erfNumber = erfNumber;
		this.totalCost = totalCost;
		this.totalPaid = totalPaid;
		this.amountOutstanding = amountOutstanding;
		this.dueDate = dueDate;
		this.customer = customer;
		this.architect = architect;
		this.structuralEng = structuralEng;
		this.complete = complete;
		this.completionDate = completionDate;
		
	}
	
	// setters

	
	public void setTotalPaid(float newTotalPaid) {
		totalPaid = newTotalPaid;
	}
	
	public void setAmountOutstanding(float newAmountOutstanding) {
		amountOutstanding = newAmountOutstanding;
	}
	
	public void setDueDate(String newDueDate) {
		dueDate = newDueDate;
	}
	
	public void setComplete(String completed) {
		complete = completed;
	}
	
	public void setCompletionDate(String dateCompleted) {
		completionDate = dateCompleted;
	}
	
	public void setArchitect(Person newArchitect) {
		architect = newArchitect;
	}
	
	public void setStructuralEng(Person newEng) {
		structuralEng = newEng;
	}
	
	public void setCutsomer(Person newCustomer) {
		customer = newCustomer;
	}
	// getters
	public int getProjectNumber() {
		return projectNumber;
	}
	
	public String getProjectName() {
		return projectName;
	}
	
	public float getTotalCost() {
		return totalCost;
	}
	
	public float getTotalPaid() {
		return totalPaid;
	}
	
	public float getAmountOutstanding() {
		return amountOutstanding;
	}
	
	public String getDueDate() {
		return dueDate;
	}
	
	public String getComplete() {
		return complete;
	}
	
	public String getCompletionDate() {
		return completionDate;
	}
	
	public Person customer() {
		return customer;
	}
	
	public Person architect() {
		return architect;
	}
	
	public String getArchitect() {
		return architect.getName();
	}
	
	public Person structuralEng() {
		return structuralEng;
	}
	
	public String getStructuralEng() {
		return structuralEng.getName();
	}
	public String getCustomer() {
		return customer.getName();
	}
	// Method
	public String toString() {
		return 	"\nProject Details:\n" +
				"\nProject name:\t\t" + projectName +
				"\nProject number:\t\t" + projectNumber +
				"\nBuilding type:\t\t" + buildingType + 
				"\nProject address:\t" + projectAddress +
				"\nERF number:\t\t" + erfNumber +
				"\nTotal Cost:\t\tR" + totalCost +
				"\nTotal Paid:\t\tR" + totalPaid +
				"\nTotal Outstanding:\t" + amountOutstanding +
				"\nDue Date:\t\t" + dueDate +
				customer +
				architect +
				structuralEng +
				"\nCompletion Status:\t" + complete +
				"\nCompletion Date:\t" + completionDate + "\n\n"; 
		
	}						
}