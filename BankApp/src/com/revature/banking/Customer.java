package com.revature.banking;

public class Customer {
	
	int custId;
	float acctBalance;
	String Fname, Lname, Gender, Password, DOB;
	int Username;
	boolean AccApproved;
	
	public Customer() {
		AccApproved = false;
		acctBalance = 0;		
	}
	
	
	public Customer(int x){
		custId = x;
	}
            
	public void TransMoney(int account, float amount) {
	
		System.out.println(" Amount to transfer: " + amount);
		
		// create a new account for transferring money
		Customer tranCust = new Customer(account);
		// withdraw from source account
		
		if(this.Withdrawal(amount)) {
			
		   // deposit into destination account
		   tranCust.deposit(amount);
		   // Add to transaction table
           // acctId, T, amount, date
		}
		
	}

	public void deposit(float amount) {
		// TODO Auto-generated method stub
		
        System.out.print("Amount to deposit: ");
        
        if (amount <= 0)
            System.out.println("Can't deposit nonpositive amount.");

       else {

            acctBalance += amount;
            System.out.println("$" + amount + " has been deposited.");
            // Add to transaction table
            // acctId, D, amount, date
       }
    	
	}

	public boolean Withdrawal(float amount) {
		// TODO Auto-generated method stub
		
		
		if (amount <= 0 || amount > acctBalance) {
            System.out.println("Withdrawal can't be completed, due to shortage of funds");
			return false;
		}
       else {
            acctBalance -= amount;
            System.out.println("$" + amount + " has been withdrawn."); 
            // Add to transaction table
            // acctId, W, amount, date
            return true;
         }
	}

	public void viewAcc() {
		// TODO Auto-generated method stub
			
		System.out.println(" View the customer balance :" + acctBalance);
		
		
	}
	
	public void viewTransactions() {
	
		// From Transactions table, for this customer, get all the transactions.
		// custId, D/W/T, Amount, Date
	}
}
