package com.revature.banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.text.ParseException;
import java.text.SimpleDateFormat; 



public class BankMenu {
	
	private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();
	private static final Logger logger = LogManager.getLogger(BankMenu.class);
    
	// If the user made a wrong input 
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        int d = Integer.parseInt(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	void mainMenu() {
		
		String input;
	    
		do {
			System.out.println("");
			System.out.println("************ New Western Bank **************");
			System.out.println("   1. Create a New Bank Account             ");
			System.out.println("   2. Customer Login             ");
			System.out.println("   3. Employee Login             ");
			System.out.println("   4. Exit             ");
	        Scanner s = ScannerSingleton.getScanner();  
	
	        input = s.nextLine();
	        
	        if(!isNumeric(input)) {
	        	System.out.println("Please choose valid Integer !!");
	        	continue;
	        }
	        
			//convert input to number
			
			switch (Integer.parseInt(input)) {
				case 1: try {
					newAccount();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						break;
				case 2: custLogin();
				 		break;	
				case 3: empLogin();
						break;		
					
				default:
					System.out.println("Exiting the Menu .. ");
					System.exit(0);
			}
		}
		while(true);
		
	}

	// 1.This method is called for creating a New Account for customer.
	
	void newAccount() throws SQLException, ParseException {
		
		Scanner s = ScannerSingleton.getScanner();
		
		Customer newCust = new Customer();
		
		System.out.println(" Enter First Name:  ");	
		newCust.Fname = s.nextLine();
		
		System.out.println(" Enter Last Name:  ");
		newCust.Lname = s.nextLine();
		
		System.out.println(" Enter Date of Birth as dd-mm-yyyy :  ");
				
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-mm-yyyy");
		java.util.Date date = sdf1.parse(s.nextLine());
		newCust.DOB = new java.sql.Date(date.getTime()); 
		
		System.out.println(" Enter Gender:  ");
		newCust.Gender = s.nextLine();
		
		System.out.println(" Enter Login User name:  ");
		newCust.Username = Integer.parseInt(s.nextLine());
		
		System.out.println(" Enter Password:  ");
		newCust.Password = s.nextLine();
		
		System.out.println(" Enter amount to deposit: ");
		newCust.acctBalance = Float.parseFloat(s.nextLine());
			
		// Insert customer information into Database
		 		
		  try { 
			  
			  Connection conn = cu.getConnection(); 
			  
			  // Create a statement
			  
			  PreparedStatement ps = conn.prepareStatement("INSERT INTO \"Banking\".customer" +
				        "  (fname, lname, dob, gender, username,pass,accbalance,accapproved) VALUES " +
				        " (?, ?, ?, ?, ?, ?, ?, ?);"); 
			  ps.setString(1,newCust.Fname); 
			  ps.setString(2, newCust.Lname);
			  ps.setDate(3, newCust.DOB); 
			  ps.setString(4, newCust.Gender); 
			  ps.setLong(5, newCust.Username);
			  ps.setString(6, newCust.Password);
			  ps.setFloat(7, newCust.acctBalance);
			  ps.setBoolean(8, false);
		 		
			  // Step 3: Execute the query or update
			  
			  logger.info(ps);
			  ps.executeUpdate(); 
			  System.out.println("Hurray, Account created successfully for " +newCust.Fname );
			  
		  } catch (SQLException e) {
		  
		     // print SQL exception information	
			  
			 System.out.println(" New Account creation failed!!");
		     printSQLException(e);
		     
		  }
				
	}
	
	// Print Exceptions when running SQL
	
	public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
	
	// 2. This method is called for customer to Login.
	
	void custLogin(){
		
		int UserId;
		int custId = 0;
		String Password;
		ResultSet rs;
		
		System.out.println(" Enter Customer User Id:  ");
		Scanner s = ScannerSingleton.getScanner();
		
		UserId = Integer.parseInt(s.nextLine());
		
		System.out.println(" Enter Password:  ");
		Password = s.nextLine();
		
		// Check for customer login details in database
		
		 try { 
			  
			  Connection conn = cu.getConnection(); 
			  
			  // Step 2:Create a statement
			  
			  PreparedStatement ps = conn.prepareStatement("Select custid  FROM \"Banking\".customer WHERE" +
				        " username = ? " +
				        " and pass = ?" +
				        " and accapproved = 'true'"); 
			  
			  ps.setLong(1,UserId); 
			  ps.setString(2, Password);
			  		 		  
			  logger.info(ps);
			  rs = ps.executeQuery();         
			  if (rs.next()) {                
			   custId = rs.getInt("custid");        
			        
			   System.out.println("Customer Id = " + custId );
			                                    
			  }
			  else {
				 System.out.println("Login Unsuccessful, Please check username/password or if Account is Approved" );  
				 return;
			  }
			  rs.close();         // Close the ResultSet    
			  ps.close();        // Close the PreparedStatement
		  } catch (SQLException e) {
		  
		     // print SQL exception information
			  
		     printSQLException(e);
		  }
		// Successful or Failure
		System.out.println("Login Successful    ");
		
		try {
			Customer cust = new Customer(custId);
			custMenu(cust);
		}
		catch ( BankCustomException e) {
			e.getStackTrace();
		}
		
		
		
	}
	
	// 3. This method is called for Employee Login.
	
	void empLogin() {
		
		String Username, Password;
		
		System.out.println(" Enter Employee User Id:  ");
		Scanner s = ScannerSingleton.getScanner();
		Username = s.nextLine();
		
		System.out.println(" Enter Password:  ");
		Password = s.nextLine();
		
		
		// Check if the Employee login is successful
		
		try { 
			  
			  Connection conn = cu.getConnection(); 
			  
			  // Step 2:Create a statement
			  
			  PreparedStatement ps = conn.prepareStatement("Select *  FROM \"Banking\".employee WHERE" +
				        " username = ? " +
				        " and pass = ?" ); 
			  
			  ps.setLong(1,Integer.parseInt(Username)); 
			  ps.setString(2, Password);
			  
			  // Step 3: Execute the query or update
			  
			  logger.info(ps);
			  ResultSet rs = ps.executeQuery();         
			  if (rs.next()) {                
			   
				  System.out.println("Login Successful !!");				                                
			  }
			  else {
				  System.out.println("Login Unsuccessful, please check username/password");
				  return;
			  }
			  rs.close();         // Close the ResultSet    
			  ps.close();        // Close the PreparedStatement
			  
		  } catch (SQLException e) {
		  
		     // print SQL exception information	
			  
		     printSQLException(e);
		     return;
		  }
				
		// Get employee Id from Employee table based on username/password
		empMenu();
				
	}
	
	// This method is to call Customer Menu
	
	void custMenu(Customer cust) {
		
		String input;
		boolean outOfLoop = false;
		
		do {
			System.out.println("");
			System.out.println("**************** Customer Menu ******************");
			System.out.println("   1. View Account Balance            ");
			System.out.println("   2. Withdrawal            ");
			System.out.println("   3. Deposit            ");
			System.out.println("   4. Money Transfer             ");
			System.out.println("   5. Exit             ");
			
	        Scanner s = ScannerSingleton.getScanner();
	
	        input = s.nextLine();
	        
	        if(!isNumeric(input)) {
	        	System.out.println("Please choose valid Integer !!");
	        	continue;
	        }
			
			switch (Integer.parseInt(input)) {
				case 1: cust.viewAcc();
						break;
				case 2: withdrawalMenu(cust);
				 		break;	
				case 3: depositMenu(cust);
						break;	
				case 4: transferMenu(cust);
				break;	
								
				default:
					System.out.println("Exiting the Menu .. ");
					outOfLoop = true;
			}
		}while(!outOfLoop);
		
	}
	
	// This method is for Withdrawal menu.
	
    void withdrawalMenu(Customer cust) {
		
		String input;
		
		System.out.println("");
		System.out.println("**************** Customer Menu ******************");
		System.out.println("   Enter Money to Withdraw :            ");
		
        Scanner s = ScannerSingleton.getScanner();

        input = s.nextLine();
        
        // If an invalid amount is entered, give error and go back to Customer menu
        
        if(!isNumeric(input)) {
        	System.out.println("Please choose valid Integer !!");
        	return;
        }
		cust.Withdrawal(Integer.parseInt(input));		
	}

    // This is for Deposit menu.
    
    void depositMenu(Customer cust) {
		
		String input;
		
		System.out.println("");
		System.out.println("**************** Customer Menu ******************");
		System.out.println("   Enter Money to Deposit :            ");
		
        Scanner s = ScannerSingleton.getScanner();

        input = s.nextLine();
        
        // If an invalid amount is entered, give error and go back to menu
        
        if(!isNumeric(input)) {
        	System.out.println("Please choose valid Integer !!");
        	return;
        }
        
		cust.deposit(Integer.parseInt(input));		
		
	}

    // This is for Transfer money to different account.
    
    void transferMenu(Customer cust) {
		
		int account, amount;
		
		System.out.println("");
		System.out.println("**************** Customer Menu ******************");
		System.out.println("   Enter The Account to Transfer :            ");
		Scanner s = ScannerSingleton.getScanner();

        account = Integer.parseInt(s.nextLine());

		System.out.println("   Enter Transfer Amount :            ");
		
		amount = Integer.parseInt(s.nextLine());
        
		cust.TransMoney(account, amount);
		
	}
		
    // this is for Employee menu.
    
	void empMenu() {
		
		String enter;
		int custId = 0;
		boolean outOfLoop = false;
		
		do {
			System.out.println("");
			System.out.println("**************** Employee Menu ******************");
			System.out.println("   1. All Customers List             ");
			System.out.println("   2. View Customer             ");
			System.out.println("   3. Exit             ");
			
	        Scanner s = ScannerSingleton.getScanner();
	        enter = s.nextLine();
	        
	        // If an invalid entry is made, give error and go back to menu
	        
	        if(!isNumeric(enter)) {
	        	System.out.println("Please choose valid Integer !!");
	        	return;
	        }
	
	        switch (Integer.parseInt(enter)) {
		
				case 1: viewAllCustomers();
						continue;
				case 2: System.out.println(" Enter Customer Id :" );
					    custId = Integer.parseInt(s.nextLine());
						break;
				default:
					System.out.println("Exiting the Menu .. ");					
					return;
			}
			
			try {
				Customer cust = new Customer(custId);
						
				boolean innerLoop = false;
				do {
					System.out.println("");
					System.out.println("**************** Employee Menu ******************");
					System.out.println("   1. View Customer Account Balance            ");
					System.out.println("   2. View Customer Transactions            ");
					System.out.println("   3. Approve or Reject Customer Account            ");
					System.out.println("   4. Exit             ");
							
			        enter = s.nextLine();
			        
			        // If an invalid entry is made, give error and go back to Employee menu
			        
			        if(!isNumeric(enter)) {
			        	System.out.println("Please choose valid Integer !!");
			        	return;
			        }
			        
					switch (Integer.parseInt(enter)) {
						case 1: cust.viewAcc();
								break;
						case 2: cust.viewTransactions();
						 		break;	
						case 3: cust.appOrreject();
								break;	
						
						default:
							System.out.println("Exiting the Menu .. ");
							innerLoop = true;
					}
				}while(!innerLoop);
		
			}
			catch (BankCustomException e) {
				System.out.println("Customer Not Found !!");
			}
			
		}while(!outOfLoop);
		
	}
	
	void viewAllCustomers(){
		
		ResultSet rs;
		int custId=0;
		String fName;
		String lName;
		
				
		// Check for customer login details in database
		
		 try { 
			  
			  Connection conn = cu.getConnection(); 
			  
			  // Step 2:Create a statement
			  
			  PreparedStatement ps = conn.prepareStatement("Select custid, fname, lname  FROM \"Banking\".customer " +
				          " order by custid"); 
			  
			  
			  logger.info(ps);
			  rs = ps.executeQuery();         
			  
			  System.out.println("CustomerId  FirstName     LastName " );
			  System.out.println("==========  ===========   ============" );
			  while (rs.next()) {                
			   custId = rs.getInt("custid"); 
			   fName = rs.getString("fname");
			   lName = rs.getString("lname");
			        
			   System.out.println("    " + custId + "        " + fName + "            " + lName );
			                                    
			  }
			  
			  rs.close();         // Close the ResultSet    
			  ps.close();        // Close the PreparedStatement
		  } catch (SQLException e) {
		  
		     // print SQL exception information
			  
		     printSQLException(e);
		  }
		
	}
	
}
	


