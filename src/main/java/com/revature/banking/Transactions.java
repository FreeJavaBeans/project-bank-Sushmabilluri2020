package com.revature.banking;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Transactions {
	
	// constructor for the customer account details
	
	int custId;
	float amount;
	char tran_kind;
	Date tran_date;
	
	public Transactions () {
		
	}
	
	// method for inserting data into transaction table.
	
	static void insertTrans(int custid, char transtype, float amount) {
		
		try { 
			ConnectionUtil cu = ConnectionUtil.getConnectionUtil();
			Connection conn = cu.getConnection();
			
			  // Step 2:Create a statement
			
			  PreparedStatement ps = conn.prepareStatement("INSERT INTO \"Banking\".transactions" +
				        "  (custid, tran_kind, amount, tran_date) VALUES " +
				        " (?, ?, ?, ?);"); 
			  ps.setInt(1,custid); 
			  ps.setString(2, String.valueOf(transtype));
			  ps.setFloat(3, amount); 
			  ps.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis())); 
			 
			 
			  // Step 3: Execute the query or update
			  ps.executeUpdate(); 
			  
		  } catch (SQLException e) {
		  
		     // print SQL exception information		  
		     BankMenu.printSQLException(e);
		     
		  }
		
	}
	// method for getting transaction from the table
	
	static void getTrans(int custid) {
		ResultSet rs;
	
	try { 
		ConnectionUtil cu = ConnectionUtil.getConnectionUtil();
		  
		  Connection conn = cu.getConnection(); 
		  // Step 2:Create a statement
		  PreparedStatement preparedStatement = conn.prepareStatement("Select custid, tran_kind, amount, tran_date FROM \"Banking\".transactions WHERE" +
			        " custid = ? " ); 
		  
		  preparedStatement.setLong(1,custid); 
		 
		 
		  rs = preparedStatement.executeQuery();
		  System.out.println("Transactions for Customer : " + custid);
		  System.out.println("CustID   TRAN_TYPE  AMOUNT   DATE");
		  System.out.println("---------------------------------");
		  while (rs.next()) { 
			  System.out.println(+rs.getInt("custid") + "          " + rs.getString("tran_kind") + "        " + 
		          rs.getFloat("amount") + "       " + rs.getDate("tran_date"));  
			                                   
		  }
		   
		  rs.close();                       // Close the ResultSet    
		  preparedStatement.close();        // Close the PreparedStatement
	  } catch (SQLException e) {
	  
	     // print SQL exception information		  
		  BankMenu.printSQLException(e);
	  }
	
	}
	
	
}

