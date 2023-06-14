import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ProjectManager {
	
	// global variables
	static List<String> importFileData = new ArrayList<>();
	static Person contractor;
	static Person customer = new Person("customer", " ", " ", " ", " ", " ");
	static Person architect = new Person("architect", " ", " ", " ", " ", " ");
	static Person structuralEng = new Person("structuralEng", " ", " ", " ", " ", " " );
	static String fullname = "";
	static final String ERRORMESSAGE = "Check input and try again"; 
	static List<Object> projectsObject = new ArrayList<>();
	static List<Object> architectObject = new ArrayList<>();
	static List<Object> structuralEngObject = new ArrayList<>();
	static List<Object> customerObject = new ArrayList<>();
	static float totalPaid = 0;
	static int erfNumber = 0;
	static float totalCost = 0;
	static String projectName = "";
	static String objName = "";
	static String nameOrNumberSearch = "";
	static int i = 0;
	static int a = 0;
	static String dueDate = "";
	static String projToTable = "";
	public static final Scanner input = new Scanner(System.in);
	static final String DBCONNECTION = "jdbc:mysql://localhost:3306/poisepms?allowPublicKeyRetrieval=true&useSSL=false";
	static final String SQLUSERNAME = "";
	static final String SQLPASSWORD = "";
	
	//methods
	
	//calling main method
	static void mainCaller() {
		main(null);
	}
	
	//editing mainMenu
	static void editMenuu() {
		Scanner input = new Scanner(System.in);
		
		System.out.println("Choose one of the options below:");
		System.out.println("""
							1.\tChange amount that has been paid
							2.\tEdit Contractor details
							3.\tChange Due Date
							4.\tFinalise (mark as complete)
							0.\tBack
							:
							""");
			
		int option2 = input.nextInt();
			
		if (option2 == 1) {
			editAmountPaid();
		}
		else if (option2 == 2) {
			editContractorDetails();
		}	
		else if (option2 == 3) {
			editDueDate();
		}
		else if (option2 == 4) {
			finalizeProject();
		}
		else if (option2 == 0) {
			mainCaller();
		}
		input.close();
	}
	
	//creating new project object
	static void collectProjectData() {

		
		try {
			Connection connection = DriverManager.getConnection(
			        DBCONNECTION,
			        SQLUSERNAME,
			        SQLPASSWORD
			        );
			
			Statement statement = connection.createStatement();
            ResultSet results;
			//requesting input data from user
			
			//object and project_name	
			System.out.println("\nPlease enter the following information:\n");
			System.out.println("\nEnter the name of the project:");
			projectName = input.nextLine();
			objName = projectName;
			
			//clients name
			System.out.println("\nFirst and Last name of the Client:");
			fullname = input.nextLine();
			String[] splitName = fullname.split(" ");
				
			//building type
			System.out.println("\nThe type of building being designed:");
			String buildingType = input.nextLine();
			
			//creating project_name if project name is left empty
			if (projectName.isEmpty()) {		
				
				projectName = splitName[1] + buildingType;
				objName = projectName;
			}
				
			//project address
			System.out.println("\nThe project address:");
			String projectAddress = input.nextLine();
	
			//ERF number 
			while (true) {
				try {
					System.out.println("\nThe ERF number:\t");
					String erf = input.nextLine();
					erfNumber = Integer.parseInt(erf);
					break;
				}catch (NumberFormatException e) {
					System.out.println("\nValue Entered is not number, Try again");
				 }
			}
			
			//due date
			while(true) {
				
				System.out.println("\nProject Due date (dd/mm/yyyy):");
				dueDate = input.nextLine();
			
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				dateFormat.setLenient(false);
			
			    try {
			      dateFormat.parse(dueDate.trim());
			      break;
			    } 
			    catch (ParseException pe) {
			      System.out.println(ERRORMESSAGE);
			    }
			}
			
			//project cost
			while (true) {
				try {
					System.out.println("\nTotal Cost of the the project:");
					String cost = input.nextLine();
					totalCost = Float.parseFloat(cost);
					break;
				}	
				catch (NumberFormatException e) {
					System.out.println("Value Entered is not number, Try again");
				}
			}
			
			//amount paid
			while (true) {
				try {
					System.out.println("\nAmount paid to date:");
					String paid = input.nextLine();
					totalPaid = Float.parseFloat(paid);
					break;
				}	
				catch (NumberFormatException e) {
				System.out.println("Value Entered is not number, Try again");
				}
			}
	
			//instructions to user for creating instances in person class
			System.out.println("""
					The following data on the Customer, Architect and Structural Engineer will now be requested:
					1.\tfull name
					2.\ttelephone number
					3.\temail address
					4.\tphysical address.
					
					!!Separate each item by using a ','!!
					
					Customer:
					(telephone number, email address, physical address)
					
					""");
			
			// Instances for customer architect and builder in person class
			
			
			while(true) {
				String customerDetails = input.nextLine();
				String[] splitCustomerDetails = customerDetails.split(",");
				
				if (splitCustomerDetails.length == 3) {
					//Customer
					customer.setName(fullname);
					customer.setNumber(splitCustomerDetails[0]);
					customer.setEmail(splitCustomerDetails[1]);
					customer.setAddress(splitCustomerDetails[2]);
					break;
				}
				else {
					System.out.println(ERRORMESSAGE);
				}	
			}
			
			// collecting data for architect
			while(true) {
				// choose architect from list of available 
				System.out.println("Enter a name from the available architects in the list below"
						+ "\nor enter 'new' to add a architect to the database and assign them to the project:\n\n");
				
				List<String> emptyList  = new ArrayList<>();
				for (i = 0; i < architectObject.size(); i++) {
					if (((Person) architectObject.get(i)).getProjName().equals(" ")) {
						System.out.println("\t" + ((Person) architectObject.get(i)).getName());
						System.out.println("");
						emptyList.add(((Person) architectObject.get(i)).getName());
					}
				}		
				
				// if the list is empty then add the request new details
				if (emptyList.isEmpty()) {
					System.out.println("There are no available architects, please add new");
					System.out.println("\nArchitect:\n(full name, telephone numer, email address, physical address)\n");
    				String architectDetails = input.nextLine();
    				String[] splitArchitectDetails = architectDetails.split(",");
					if (splitArchitectDetails.length == 4) {
						//architect
						architect.setName(splitArchitectDetails[0]);
						architect.setNumber(splitArchitectDetails[1]);
						architect.setEmail(splitArchitectDetails[2]);
						architect.setAddress(splitArchitectDetails[3]);
						architect.setProjname(projectName);
						
						statement.executeUpdate(
    		                    "INSERT INTO architect VALUES " + 
    		                    		"('" + splitArchitectDetails[0] + "', '" + splitArchitectDetails[1] + "', '" + 
    		                    		splitArchitectDetails[2] + "', '" + splitArchitectDetails[3] +
    		                    		"', '" + projectName + "')");
					}break;
						
				}
           	
				// selecting architect from list
				while(true) {
					String nameOrNew = input.nextLine();
					
					if (!nameOrNew.equals("new")) {
					// check if input is part of list
					results = statement.executeQuery(
							"SELECT name FROM architect WHERE name='" + nameOrNew + "'");
					
					if (results.next()) {
						results = statement.executeQuery(
								"SELECT name, email, number, address, projName FROM architect WHERE name='"+ nameOrNew + "'");
						if (results.next()){
							architect.setName(results.getString("name"));
							architect.setEmail(results.getString("email"));
							architect.setNumber(results.getString("number"));
							architect.setAddress(results.getString("address"));
							architect.setProjname(results.getString("projName"));
							
							statement.executeUpdate(
									"UPDATE architect SET projName='" + projectName + "' WHERE name='" + architect.getName() + "'");
							
							
						}
						break;
					}
					else {
						System.out.println(ERRORMESSAGE);
					}
					}
					
					// creating new architect
					else if(nameOrNew.equals("new")) {
						while(true) {
	        				System.out.println("\nArchitect:\n(full name, telephone numer, email address, physical address)\n");
	        				String architectDetails = input.nextLine();
	        				String[] splitArchitectDetails = architectDetails.split(",");
	    					if (splitArchitectDetails.length == 4) {
	    						//architect
	    						architect.setName(splitArchitectDetails[0]);
	    						architect.setNumber(splitArchitectDetails[1]);
	    						architect.setEmail(splitArchitectDetails[2]);
	    						architect.setAddress(splitArchitectDetails[3]);
	    						architect.setProjname(projectName);
	    						
	    						statement.executeUpdate(
		    		                    "INSERT INTO architect VALUES " + 
		    		                    		"('" + splitArchitectDetails[0] + "', '" + splitArchitectDetails[1] + "', '" + 
		    		                    		splitArchitectDetails[2] + "', '" + splitArchitectDetails[3] +
		    		                    		"', '" + projectName + "')");
	  
	    						break;
	    					}
	    					else {
	    							System.out.println(ERRORMESSAGE);
	    					}	
	        			}			
					}
				}break;
			}
			
			while(true) {
				// choose engineer from list 
				System.out.println("Enter a name from the available Structural Engineers in the list below"
						+ "\nor enter 'new' to add a Engineer to the database and assign them to the project:\\n\n");
				
				List<String> emptyList  = new ArrayList<>();
				for (i = 0; i < structuralEngObject.size(); i++) {
					if (((Person) structuralEngObject.get(i)).getProjName().equals(" ")) {
						System.out.println(i + ".\t" + ((Person) structuralEngObject.get(i)).getName());
						System.out.println("");
						emptyList.add(((Person) structuralEngObject.get(i)).getName());
					}
           		}
				
				// if list is empty then request data to addd a new engineer
				if (emptyList.isEmpty()) {
					System.out.println("There are no available engineers, please add new");
					System.out.println("\nEngineer:\n(full name, telephone numer, email address, physical address)\n");
					String engineerDetails = input.nextLine();
					String[] splitEngineerDetails = engineerDetails.split(",");
					if (splitEngineerDetails.length == 4) {
						//architect
						structuralEng.setName(splitEngineerDetails[0]);
						structuralEng.setNumber(splitEngineerDetails[1]);
						structuralEng.setEmail(splitEngineerDetails[2]);
						structuralEng.setAddress(splitEngineerDetails[3]);
						structuralEng.setProjname(projectName);
						
						statement.executeUpdate(
			                    "INSERT INTO structuraleng VALUES " + 
			                    		"('" + splitEngineerDetails[0] + "', '" + splitEngineerDetails[1] + "', '" + 
			                    		splitEngineerDetails[2] + "', '" + splitEngineerDetails[3] +
			                    		"', '" + projectName + "')");
					}break;
				}
				
				// adding engineer from the list
				while(true) {
					String nameOrNew = input.nextLine();
					
					if (!nameOrNew.equals("new")) {
						// check if input is part of list
						results = statement.executeQuery(
								"SELECT name FROM structuraleng WHERE name='" + nameOrNew + "'");
						if (results.next()) {
							
							ResultSet nameFromList = statement.executeQuery(
									"SELECT name, email, number, address, projName FROM structuraleng WHERE name='"+ nameOrNew + "'");
							
							if (nameFromList.next()) {
								structuralEng.setName(nameFromList.getString("name"));
								structuralEng.setEmail(nameFromList.getString("email"));
								structuralEng.setNumber(nameFromList.getString("number"));
								structuralEng.setAddress(nameFromList.getString("address"));
								structuralEng.setProjname(nameFromList.getString("projName"));
								
								statement.executeUpdate(
										"UPDATE structuraleng SET projName='" + projectName + "' WHERE name='" + structuralEng.getName() + "'");
							}
							break;
						}
						else {
							System.out.println(ERRORMESSAGE);
						}
					}
					
					// adding new engineer
					else if(nameOrNew.equals("new")) {
						while(true) {
	        				System.out.println("\nEngineer:\n(full name, telephone numer, email address, physical address)\n");
	        				String engineerDetails = input.nextLine();
	        				String[] splitEngineerDetails = engineerDetails.split(",");
	    					if (splitEngineerDetails.length == 4) {
	    						//architect
	    						structuralEng.setName(splitEngineerDetails[0]);
	    						structuralEng.setNumber(splitEngineerDetails[1]);
	    						structuralEng.setEmail(splitEngineerDetails[2]);
	    						structuralEng.setAddress(splitEngineerDetails[3]);
	    						structuralEng.setProjname(projectName);
	    						
	    						statement.executeUpdate(
		    		                    "INSERT INTO structuraleng VALUES " + 
		    		                    		"('" + splitEngineerDetails[0] + "', '" + splitEngineerDetails[1] + "', '" + 
		    		                    		splitEngineerDetails[2] + "', '" + splitEngineerDetails[3] +
		    		                    		"', '" + projectName + "')");
	    						break;
	    					}
	    					else {
	    						System.out.println(ERRORMESSAGE);
	    					}	
	        			}			
					}
				}break;
			}
			
			
			//projects number using size of array containing projects
			int projectNumber = 1 + projectsObject.size();
			float amountOutstanding = totalCost - totalPaid;
			//instance in project_details
			ProjectDetails objName = new ProjectDetails(projectNumber, projectName, buildingType,
														projectAddress, erfNumber, totalCost, totalPaid, amountOutstanding, 
														dueDate,  customer, architect, structuralEng, 
														"incomplete", " ");	
            // adding project to list
            projectsObject.add(objName);

            // String that will be used to add values in projects table
    		statement.executeUpdate(
    				"INSERT INTO projects VALUES ('" + projectNumber + "', '" + projectName + "', '" + buildingType + "', '" +
        					projectAddress + "', '" + erfNumber + "', '" + totalCost + "', '"  + totalPaid + "', '" +  
        					dueDate + "', '" +  customer.getName() + "', '" + architect.getName() + "', '" + structuralEng.getName() + 
        					"', 'incomplete', ' ','" + amountOutstanding + "')");
    		
    		// adding data to customers table
            statement.executeUpdate(
                    "INSERT INTO customers VALUES ('" + customer.getName() + "', '" + customer.getEmail() + "', '" + customer.getNumber() +
                    "', '" + customer.getAddress() + "', '" +  projectName + "', '"  + amountOutstanding + "')");
            
            System.out.println(objName);
            
            statement.close();
            connection.close();
			}
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
		mainMenu();
	}
				
	//editing amount paid
	static void editAmountPaid() {
		
		System.out.println((projectsObject.get(i)));
				
		//determining outstanding amount
		float amountOutstanding = ((ProjectDetails) projectsObject.get(i)).getTotalCost() -
								((ProjectDetails) projectsObject.get(i)).getTotalPaid();
			
		//displaying information
		System.out.println("\nTotal project cost is:\tR "
						+ ((ProjectDetails) projectsObject.get(i)).getTotalCost()
						+ "\nAmount paid:\t\tR "
						+ ((ProjectDetails) projectsObject.get(i)).getTotalPaid()
						+ "\nAmount outstanding:\tR " + amountOutstanding);
				
		System.out.println("\n\nEnter latest amount paid:");
		float latestAmount = input.nextFloat();
				
		//new amount paid
		float newPaidTotal = latestAmount + ((ProjectDetails) projectsObject.get(i)).getTotalPaid();
				
		//setting new amount paid
		((ProjectDetails) projectsObject.get(i)).setTotalPaid(newPaidTotal);
				
		float newAmountOutstanding = ((ProjectDetails) projectsObject.get(i)).getTotalCost() -
									((ProjectDetails) projectsObject.get(i)).getTotalPaid();
				
		System.out.println("Amount has been updated!\n");
		System.out.println("\nTotal project cost is:\tR "
						+ ((ProjectDetails) projectsObject.get(i)).getTotalCost()
						+ "\nAmount paid:\t\tR "
						+ ((ProjectDetails) projectsObject.get(i)).getTotalPaid()
						+ "\nAmount outstanding:\tR " + newAmountOutstanding
						+ "\n\n");
		
		try {
			Connection connection = DriverManager.getConnection(
			        DBCONNECTION,
			        SQLUSERNAME,
			        SQLPASSWORD
			        );
			
			Statement statement = connection.createStatement();
            
            // updating values in sql tables
            statement.executeUpdate(
            		"UPDATE projects SET totalPaid=" + ((ProjectDetails) projectsObject.get(i)).getTotalPaid() + "WHERE projName='" +
                    		((ProjectDetails) projectsObject.get(i)).getProjectName() + "'");
            
            statement.executeUpdate(
            		"UPDATE projects SET amountOutstanding=" + newAmountOutstanding + "WHERE projName='" +
                    		((ProjectDetails) projectsObject.get(i)).getProjectName() + "'");
			
            connection.close();
            statement.close();
            
		}catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		editMenuu();
	}
		
	// updating contractor details
	static void editContractorDetails() {
		
		try {
			Connection connection = DriverManager.getConnection(
			        DBCONNECTION,
			        SQLUSERNAME,
			        SQLPASSWORD
			        );
			
			Statement statement = connection.createStatement();
				
		System.out.println("\nWhich contractor's details would you like to edit??");
		System.out.println("""
							a -\tarchitect
							b -\tstructural engineer
							0 -\tpevious menu
							s
							""");	
		
		char choice = input.next().charAt(0);
		
		if (choice == 'a') {
			contractor = ((ProjectDetails) projectsObject.get(i)).architect();
		}
		else if (choice == 'b') {
			contractor = ((ProjectDetails) projectsObject.get(i)).structuralEng();
		}
		else if (choice == '0') {
			statement.close();
            connection.close();
			editMenuu();
		}
		
		while (true) {	
			System.out.println(contractor);
			System.out.println("""
								What would you like to edit?:
								1.\tName
								2.\tNumber
								3.\tEmail
								4.\tAddress
								5.\tPrevious menu
								 
								 """);
				
				int edit = input.nextInt();
				
				if (edit == 1) {	
							
					System.out.println("Enter new name:");
					input.nextLine();
					String newName = input.nextLine();
						
					contractor.setName(newName);
					
					// updating values in sql tables
					statement.executeUpdate(
		                    "UPDATE " + contractor.getDesignation() + " SET name='" + newName + "' WHERE projName='" +
		                    ((ProjectDetails) projectsObject.get(i)).getProjectName() + "'");
						
					System.out.println("Name has successfully changed");
					
				}
				else if (edit == 2) {
						
					System.out.println("Enter new number:");
					input.nextLine();
					String newNumber = input.nextLine();
						
					contractor.setNumber(newNumber);
					
					// updating values in sql tables
					statement.executeUpdate(
		                    "UPDATE " + contractor.getDesignation() + " SET number='" + newNumber + "' WHERE projName='" +
		                    ((ProjectDetails) projectsObject.get(i)).getProjectName() + "'");
					
					System.out.println("Number successfully changed");
					
						
				}
				else if (edit == 3) {
						
					System.out.println("Enter new email:");
					input.nextLine();
					String newEmail = input.nextLine();
						
					contractor.setEmail(newEmail);
					
					// updating values in sql tables
					statement.executeUpdate(
		                    "UPDATE " + contractor.getDesignation() + " SET email='" + newEmail + "' WHERE projName='" +
		                    ((ProjectDetails) projectsObject.get(i)).getProjectName() + "'");
					
					System.out.println("Email successfully changed");
					
				}
				else if (edit == 4) {
						
					System.out.println("Enter new address:");
					input.nextLine();
					String newAddress = input.nextLine();
						
					contractor.setAddress(newAddress);
					
					// updating values in sql tables
					statement.executeUpdate(
		                    "UPDATE " + contractor.getDesignation() + " SET address='" + newAddress + "' WHERE projName='" +
		                    ((ProjectDetails) projectsObject.get(i)).getProjectName() + "'");
						
					System.out.println("Address successfully changed");
				
						
				}
				else if (edit == 5) {
					editContractorDetails();
					break;
				}
				else {
					System.out.println(ERRORMESSAGE);
				}
			}
            statement.close();
            connection.close();
		}
		catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		}
	}
	
	// editing due date
	static void editDueDate() {

		try {
			Connection connection = DriverManager.getConnection(
			        DBCONNECTION,
			        SQLUSERNAME,
			        SQLPASSWORD
			        );
			
			Statement statement = connection.createStatement();
			
			String dueDate = ((ProjectDetails) projectsObject.get(i)).getDueDate();
			String projName = ((ProjectDetails) projectsObject.get(i)).getProjectName();
					
			System.out.println("\nProject name (dd/mm/yyyy):\t" + projName 
							+ "\nDue date:\t\t\t" + dueDate );
					
			System.out.println("\n\nEnter new due date (dd/mm/yyyy):");
			String newDate = input.nextLine();
					
			((ProjectDetails) projectsObject.get(i)).setDueDate(newDate);
			
			// updating values in sql tables
			statement.executeUpdate(
                    "UPDATE projects SET dueDate='" + newDate + "' WHERE projName='" +
                    ((ProjectDetails) projectsObject.get(i)).getProjectName() + "'");
			
					
			System.out.println("\n\nDue date successfully changed\n\n");
					
			System.out.println("Project name:\t" + projName 
							+ "\nDue date:\t" + ((ProjectDetails) projectsObject.get(i)).getDueDate()
							+ "\n\n");
			statement.close();
            connection.close();
			editMenuu();
		}
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
		
	// finalizing project
	static void finalizeProject() {
		
		try {
			Connection connection = DriverManager.getConnection(
			        DBCONNECTION,
			        SQLUSERNAME,
			        SQLPASSWORD
			        );
			
			Statement statement = connection.createStatement();
			
			while(true) {
				
				System.out.println("\n\nEnter date of completion (dd/mm/yyyy)");
				String completionDate = input.nextLine();
			
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				dateFormat.setLenient(false);
			
			    try {	
			    	dateFormat.parse(completionDate.trim());
			    	((ProjectDetails) projectsObject.get(i)).setCompletionDate(completionDate);
			    	
			    	// updating outstanding completion date and status in projects table
			    	statement.executeUpdate(
		                    "UPDATE projects SET completionDate='" + completionDate + "' WHERE projName='" +
		                    ((ProjectDetails) projectsObject.get(i)).getProjectName() + "'");
			    	
			    	((ProjectDetails) projectsObject.get(i)).setComplete("Complete");
			    	
			    	statement.executeUpdate(
		                    "UPDATE projects SET completionstatus='complete' WHERE projName='" +
		                    ((ProjectDetails) projectsObject.get(i)).getProjectName() + "'");
			    	
			    	
			    	// removing architect and engineer from project
			    	statement.executeUpdate(
			    			"UPDATE architect SET projName=' ' WHERE name='" + 
			    					((ProjectDetails) projectsObject.get(i)).getArchitect() + "'");
			    	
			    	statement.executeUpdate(
			    			"UPDATE structuraleng SET projName=' ' WHERE name='" +
			    					((ProjectDetails) projectsObject.get(i)).getStructuralEng() + "'");
			    	
					System.out.println("\n\nCompletion Status Changed");
							
					// invoice displayed only if amount has not been fully paid
					float amountOutstanding = ((ProjectDetails) projectsObject.get(i)).getTotalCost() -
											((ProjectDetails) projectsObject.get(i)).getTotalPaid();
					
					// updating outstanding amount in projects and customers tables
					if (amountOutstanding == 0) {
						System.out.println("Project Cost has been paid in full.");
						
						statement.executeUpdate(
			                    "UPDATE projects SET amountOutstanding='" + amountOutstanding + "' WHERE projName='" +
			                    ((ProjectDetails) projectsObject.get(i)).getProjectName() + "'");
						
						statement.executeUpdate(
			                    "UPDATE customers SET outstanding='" + amountOutstanding + "' WHERE projName='" +
			                    ((ProjectDetails) projectsObject.get(i)).getProjectName() + "'");
						
						mainCaller();
					}
		
					else {

						System.out.println(((ProjectDetails) projectsObject.get(i)).customer());
						System.out.println("Outstanding amount:\tR " + amountOutstanding + "\n\n");
						
						statement.executeUpdate(
			                    "UPDATE projects SET amountOutstanding='" + amountOutstanding + "' WHERE projName='" +
			                    ((ProjectDetails) projectsObject.get(i)).getProjectName() + "'");
						
						statement.executeUpdate(
			                    "UPDATE customers SET outstanding='" + amountOutstanding + "' WHERE projName='" +
			                    ((ProjectDetails) projectsObject.get(i)).getProjectName() + "'");
						
						mainCaller();
						
					}
			      break;
			    } 
			    catch (ParseException e) {
			      System.out.println(ERRORMESSAGE);
			    }
			}
            statement.close();
            connection.close();
		}catch (SQLException e1) {
			e1.printStackTrace();
			}
	}
	
	// importing data from database
	static void importFromDB() {
	
		try {
			Connection connection = DriverManager.getConnection(
			        DBCONNECTION,
			        SQLUSERNAME,
			        SQLPASSWORD
			        );
			
			Statement statement = connection.createStatement();
            ResultSet architects;
            ResultSet structuralEngs;
            ResultSet project;
            ResultSet customers;
			
			architects = statement.executeQuery("SELECT * FROM architect");
		
			//importing architects
			while (architects.next()) {
				Person importArchitect = new Person("architect", architects.getString("name"), architects.getString("number"), architects.getString("email"), 
									architects.getString("address"), architects.getString("projName"));
				architectObject.add(importArchitect);		
       		}
			
			structuralEngs = statement.executeQuery("SELECT * FROM structuraleng");
			
			//importing structural_eng
			while (structuralEngs.next()) {
				
				Person importStructuralEng = new Person("structuraleng", structuralEngs.getString("name"), structuralEngs.getString("number"), structuralEngs.getString("email"), 
							structuralEngs.getString("address"), structuralEngs.getString("projName"));
				structuralEngObject.add(importStructuralEng);
				      
       		}
			// importing list of customers
			customers = statement.executeQuery("SELECT * FROM customers");
			while (customers.next()) {
				
				Person importCustomer = new Person("customer", customers.getString("name"), customers.getString("number"), customers.getString("email"), 
						customers.getString("address"), customers.getString("projName"));
				customerObject.add(importCustomer);
       		}
	
			
			project = statement.executeQuery("SELECT * FROM projects");
			
			
			// creating list of projectDetails objects from the projects table
			// the objects contain "empty person objects"
			while(project.next()) {
				ProjectDetails newProject = new ProjectDetails(Integer.parseInt(project.getString("projNumber")), 
						project.getString("projName"), 
						project.getString("buildingType"), 
						project.getString("projectAddress"), 
						Integer.parseInt(project.getString("erfNumber")), 
						Float.parseFloat(project.getString("totalCost")), 
						Float.parseFloat(project.getString("totalPaid")),
						(Float.parseFloat(project.getString("totalCost")) - Float.parseFloat(project.getString("totalPaid"))),
						project.getString("dueDate"), 
						customer, 
						architect, 
						structuralEng,
						project.getString("completionStatus"), 
						project.getString("completionDate"));
				projectsObject.add(newProject);
			}
			
			// matching customers to projects
			// overriding empty person objects
			for (a = 0; a < projectsObject.size(); a ++) {
				for (i = 0; i < customerObject.size(); i ++) {
					if (((Person) customerObject.get(i)).getProjName().equals(((ProjectDetails) projectsObject.get(a)).getProjectName())){
						((ProjectDetails) projectsObject.get(a)).setCutsomer(((Person) customerObject.get(i)));
					}
				}
			}
			
			// matching architect to projects
			// overriding empty person objects
			for (a = 0; a < projectsObject.size(); a ++) {
				for (i = 0; i < architectObject.size(); i ++) {
					if (((Person) architectObject.get(i)).getProjName().equals(((ProjectDetails) projectsObject.get(a)).getProjectName())){
						((ProjectDetails) projectsObject.get(a)).setArchitect(((Person) architectObject.get(i)));
					}
				}
			}
			
			// matching engineer to projects
			// overriding empty person objects
			for (a = 0; a < projectsObject.size(); a ++) {
				for (i = 0; i < structuralEngObject.size(); i ++) {
					if (((Person) structuralEngObject.get(i)).getProjName().equals(((ProjectDetails) projectsObject.get(a)).getProjectName())){
						((ProjectDetails) projectsObject.get(a)).setStructuralEng(((Person) structuralEngObject.get(i)));
					}
				}
			}
			mainMenu();
		}
		catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		}
	}

	//view all projects
	static void viewProjects() {
	
		for (i = 0; i < projectsObject.size(); i++) {
				System.out.println(projectsObject.get(i));
		}
		mainMenu();
	}

	//view incomplete projects
	static void viewIncompleteProjects() {
		
		for (i = 0; i < projectsObject.size(); i++) {
			if (((ProjectDetails) projectsObject.get(i)).getComplete().equals("incomplete")) {
				System.out.println(projectsObject.get(i));
			}
		}
		mainMenu();
	}
	
	//view overdue projects
	static void viewOverDueProjects() {
		
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
		String currentDate = dateFormat.format(date);
		
		for (i = 0; i < projectsObject.size(); i++) {
			if (((ProjectDetails) projectsObject.get(i)).getDueDate().compareTo(currentDate) > 0 ||
				((ProjectDetails) projectsObject.get(i)).getComplete().equals("incomplete") ||
				((ProjectDetails) projectsObject.get(i)).getCompletionDate().equals("")){
						System.out.println(projectsObject.get(i));
			}
		}
		mainMenu();
	}

	//main menu
	static void mainMenu() {
		
		System.out.println("""
				Choose and option from the list bellow:
				1.\tCreate a new project.
				2.\tEdit a project data.
				3.\tView incomplete projects
				4.\tView overdue projects
				5.\tView projects
				0.\tExit
			
				:
				"""
				);
	
		String option1 = input.nextLine() ;
	
		if (option1.equals("0")) {
			input.close();
			System.out.println("Goodbye!!");
		}
		else if (option1.equals("1")) {
			collectProjectData();
		}
		else if (option1.equals("2")) {
			// searching through list of objects matching project name or number to user input	
			if (projectsObject.isEmpty()) {
				System.out.println("\n\nNo projects have been registered. First add projects\n\n");
				mainMenu();
			}
			else {
				System.out.println("Enter project name or number.\n:");	
			nameOrNumberSearch = input.nextLine();
			}	
			for (i = 0; i < projectsObject.size(); i++) {
				if (((ProjectDetails) projectsObject.get(i)).getProjectName().equals(nameOrNumberSearch)){
					System.out.println(projectsObject.get(i));
					editMenuu();
				}
				else {
					try {int check = Integer.parseInt(nameOrNumberSearch);
						if(((ProjectDetails) projectsObject.get(i)).getProjectNumber() == check) {
							System.out.println(projectsObject.get(i));
							editMenuu();		
						}
					}
					catch (NumberFormatException e) {
						System.out.println("\nProject not found. check input and try again");
						mainMenu();
					}
				}
			}
		}
		
		else if (option1.equals("3")) {
			viewIncompleteProjects();
		}
		else if (option1.equals("4")) {
			viewOverDueProjects();
		}
		else if (option1.equals("5")) {
			viewProjects();
		}
		else {
			System.out.println("\nCheck input and try again\n");
			mainMenu();
		}
	}
	
	
	//main method
	public static void main (String[] args) {
	
		System.out.println("import data from database?");
		System.out.println("(yes/no)\n");
		String importData = input.nextLine();
		
		if (importData.equals("yes")) {
			importFromDB();
			
		}else if (importData.equals("no")) {
			mainMenu();
		}
		else {
			System.out.println(ERRORMESSAGE + "\n\n");
			mainCaller();
		}
	}
}
