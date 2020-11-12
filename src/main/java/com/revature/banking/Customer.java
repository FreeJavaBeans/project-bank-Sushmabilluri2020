package com.revature.banking;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class Customer {
	
	int custId;
	float acctBalance;
	String Fname, Lname, Gender, Password;
	Date DOB;
	int Username;
	boolean accApproved;
	
	private static final Logger logger = LogManager.getLogger(BankMenu.class);
	
	public Customer() {
		accApproved = false;
		acctBalance = 0;		
	}
	
	
	public Customer(int x) throws BankCustomException {
		
		custId = x;
		ResultSet rs;
		
		try {
			ConnectionUtil cu = ConnectionUtil.getConnectionUtil();
			Connection conn = cu.getConnection();
			
			// Step 2:Create a statement
			
			PreparedStatement ps= conn.prepareStatement("Select fname, lname, accbalance,accapproved  FROM \"Banking\".customer WHERE" +
			        " custid = ?"); 
		  
			ps.setInt(1,custId); 
			
			logger.info(ps);
			rs = ps.executeQuery();
			
			if (rs.next()) {                
				    
				Fname = rs.getString("fname"); 
				Lname = rs.getString("lname");
				acctBalance = rs.getFloat("accbalance");
				accApproved = rs.getBoolean("accapproved");
				
				System.out.println("First Name = " + Fname + " Last Name = " + Lname);
		                                    
			}
			else {
			  throw new BankCustomException("Customer was not found !!");
			}
			rs.close();         // Close the ResultSet    
			ps.close();        // Close the PreparedStatement
	  } catch (SQLException e) {
	  
	     // print SQL exception information
		  
		  BankMenu.printSQLException(e);
	  }
	}
            
	public void TransMoney(int account, float amount) {
	
		System.out.println(" Amount to transfer: " + amount);
		
			// create a new account for transferring money
		
		try {
			Customer tranCust = new Customer(account);
			
			// withdraw from source account
			
			if(acctBalance >= amount) {
				
				this.Withdrawal(amount);
				tranCust.deposit(amount);
			}
			else {
				System.out.println("Insufficient Funds for Transfer !!!");
			}
			
		}
		catch(BankCustomException e) {
			e.getStackTrace();
		}
		
		
	}

	public void deposit(float amount) {
		// TODO Auto-generated method stub
		
        System.out.print("Amount to deposit: ");
        
        if (amount <= 0)
            System.out.println("Can't deposit nonpositive amount.");

       else {

            acctBalance += amount;
            
            
    		try {
    			ConnectionUtil cu = ConnectionUtil.getConnectionUtil();
    			Connection conn = cu.getConnection(); 
    			
    			// Step 2:Create a statement
    			
    			PreparedStatement ps = conn.prepareStatement("update \"Banking\".customer SET " +
    			        " accbalance = ? " 
    					+ " WHERE custid = ? "); 
    		  
    			ps.setFloat(1,acctBalance);
    			ps.setInt(2,custId);
    			logger.info(ps);	  
    			ps.executeUpdate();         
    			    
    			ps.close();        // Close the PreparedStatement
    		} catch (SQLException e) {
    	  
    			// print SQL exception information	
    			
    			BankMenu.printSQLException(e);
    		}
            
            System.out.println("$" + amount + " has been deposited.");
            
            // Add to transaction table
            // acctId, D, amount, date
            
            Transactions.insertTrans(custId, 'D', amount);
       }
    	
	}

	public boolean Withdrawal(float amount) {
		// TODO Auto-generated method stub
		
		
		if (amount <= 0 || amount > acctBalance) {
			
            System.out.println("Withdrawal can't be completed, due to shortage of funds. Available balance : " +acctBalance);
			return false;
		}
       else {
            acctBalance -= amount;
            
            try {
    			ConnectionUtil cu = ConnectionUtil.getConnectionUtil();
    			Connection conn = cu.getConnection(); 
    			
    			// Step 2:Create a statement
    			
    			PreparedStatement ps = conn.prepareStatement("update \"Banking\".customer SET " +
    			        " accbalance = ? " 
    					+ " WHERE custid = ? "); 
    		  
    			ps.setFloat(1,acctBalance);
    			ps.setInt(2,custId);
    			
    			logger.info(ps);
    			ps.executeUpdate();         
    			    
    			ps.close();        // Close the PreparedStatement
    		} catch (SQLException e) {
    	  
    			// print SQL exception information
    			
    			BankMenu.printSQLException(e);
    		}
            
            System.out.println("$" + amount + " has been withdrawn."); 
            
            // Add to transaction table
            // acctId, W, amount, date
            
            Transactions.insertTrans(custId, 'W', amount);
            return true;
            
         }
	}

	public void viewAcc() {
		// TODO Auto-generated method stub
			
		System.out.println(" View the customer balance :"  +  acctBalance);
		
		Transactions.insertTrans(custId, 'V', acctBalance);
	}
	
	public void viewTransactions() {
	
		// From Transactions table, for this customer, get all the transactions.
		// custId, D/W/T, Amount, Date
		
		Transactions.getTrans(custId);
		
	}
	
	// Approve or Reject a customer account
	
	public void appOrreject() {
		
		boolean approve=false;
		
		if(accApproved) {
			System.out.println("Account is already approved, No need to approve/reject again");
			return;
		}
		
		System.out.println("**************** Employee Menu ******************");
		System.out.println("   1. Approve Customer Account            ");
		System.out.println("   2. Reject Customer Account            ");
		System.out.println("   3. Exit             ");
	
		Scanner s = ScannerSingleton.getScanner();
        String enter = s.nextLine();
		
		switch (Integer.parseInt(enter)) {
			case 1: approve = true;
					break;
			case 2: System.out.println(" Customer rejected.  ");
		    		break;
			default:
				System.out.println("Exiting the Menu .. ");
				System.exit(0);
		}
		
		try {
			ConnectionUtil cu = ConnectionUtil.getConnectionUtil();
			Connection conn = cu.getConnection(); 
			
			// Step 2:Create a statement
			
			PreparedStatement ps = conn.prepareStatement("update \"Banking\".customer SET " +
			        " accapproved = ? " 
					+ " WHERE custid = ? "); 
		  
			ps.setBoolean(1,approve);
			ps.setInt(2,custId);
			
			logger.info(ps);
			ps.executeUpdate();
			
			//Insert transaction
			
			if(approve) {
				Transactions.insertTrans(custId, 'A', 0);
				System.out.println(" Account approved ");
			}
			else {
				Transactions.insertTrans(custId, 'R', 0);
			}
			
			ps.close();        // Close the PreparedStatement
			
		} catch (SQLException e) {
	  
			// print SQL exception information
			
			BankMenu.printSQLException(e);
		}
	}
	
}
