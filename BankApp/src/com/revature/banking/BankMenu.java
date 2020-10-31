package com.revature.banking;

import java.util.Scanner;

public class BankMenu {

	void mainMenu() {
		String input;
	    
		System.out.println("**************** ABC Bank ******************");
		System.out.println("   1. Create a New Bank Account             ");
		System.out.println("   2. Customer Login             ");
		System.out.println("   3. Employee Login             ");
		System.out.println("   4. Exit             ");
        Scanner s = new Scanner(System.in);

        input = s.nextLine();
		System.out.println("Pressed Input :" +input);
	    
		//convert input to number
		// 1, newAccount()
		// 2 customerLogin()
		// 3. employeeLogin()
		// 4.anything else - Exit
		
		switch (Integer.parseInt(input)) {
			case 1: newAccount();
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

	// This method is called for creating a New Account for customer.
	
	void newAccount() {
		
		Customer newCust = new Customer();
		
		System.out.println(" Enter First Name:  ");
		Scanner s = new Scanner(System.in);
		
		newCust.Fname = s.nextLine();
		
		System.out.println(" Enter Last Name:  ");
		newCust.Lname = s.nextLine();
		
		System.out.println(" Enter Date of Birth:  ");
		newCust.DOB = s.nextLine();
		
		System.out.println(" Enter Gender:  ");
		newCust.Gender = s.nextLine();
		
		System.out.println(" Enter Login User name:  ");
		newCust.Username = Integer.parseInt(s.nextLine());
		
		System.out.println(" Enter Password:  ");
		newCust.Password = s.nextLine();
		
		// Store this info in Database
	}
	
	// This method is called for customer to Login.
	
	void custLogin(){
		
		String UserId, Password;
		
		System.out.println(" Enter Customer User Id:  ");
		Scanner s = new Scanner(System.in);
		UserId = s.nextLine();
		
		System.out.println(" Enter Password:  ");
		Password = s.nextLine();
		
		// Check for customer login details in database
		// Successful or Failure
		System.out.println("Login Successful    ");
		custMenu(1);
		
	}
	
	// This method is called for Employee Login.
	
	void empLogin() {
		
		String Username, Password;
		
		System.out.println(" Enter Employee User Id:  ");
		Scanner s = new Scanner(System.in);
		Username = s.nextLine();
		
		System.out.println(" Enter Password:  ");
		Password = s.nextLine();
		
		// Check if the Employee login is successful
		
		// Get employee Id from Employee table based on username/password
		int empId = 1;
		
		empMenu(empId);
				
	}
	
	// This method is to call Customer Menu
	
	void custMenu(int custId) {
		
		String input;
		
		System.out.println("**************** Customer Menu ******************");
		System.out.println("   1. View Account Balance            ");
		System.out.println("   2. Withdrawal            ");
		System.out.println("   3. Deposit            ");
		System.out.println("   4. Money Transfer             ");
		System.out.println("   5. Exit             ");
		
        Scanner s = new Scanner(System.in);

        input = s.nextLine();
		System.out.println("Pressed Input :" +input);
		
		Customer cust = new Customer(custId);
	    
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
				System.exit(0);
		}
		
	}
	
	// This method is for Withdrawal menu.
	
    void withdrawalMenu(Customer cust) {
		
		String input;
		
		System.out.println("**************** Customer Menu ******************");
		System.out.println("   Enter Money to Withdraw :            ");
		
        Scanner s = new Scanner(System.in);

        input = s.nextLine();
		System.out.println("Pressed Input :" +input);
		
		cust.Withdrawal(Integer.parseInt(input));		
		
	}

    // This is for Deposit menu.
    
    void depositMenu(Customer cust) {
		
		String input;
		
		System.out.println("**************** Customer Menu ******************");
		System.out.println("   Enter Money to Deposit :            ");
		
        Scanner s = new Scanner(System.in);

        input = s.nextLine();
		System.out.println("Pressed Input :" +input);
		
		cust.deposit(Integer.parseInt(input));		
		
	}

    // This is for Transfer money to different account.
    
    void transferMenu(Customer cust) {
		
		int account, amount;
		
		System.out.println("**************** Customer Menu ******************");
		System.out.println("   Enter The Account to Transfer :            ");
		Scanner s = new Scanner(System.in);

        account = Integer.parseInt(s.nextLine());

		System.out.println("   Enter Transfer Amount :            ");
		
		amount = Integer.parseInt(s.nextLine());

        System.out.println("Pressed Input :" +account + " amount : " + amount);
		cust.TransMoney(account, amount);
		
	}
		
    // this is for Employee menu.
    
	void empMenu(int custId) {
		
		String enter;
		
		System.out.println("**************** Employee Menu ******************");
		System.out.println("   1. Enter Customer Account             ");
		System.out.println("   2. Exit             ");
        Scanner s = new Scanner(System.in);
        enter = s.nextLine();
		System.out.println("Customer Account Entered :" + enter);

		
		switch (Integer.parseInt(enter)) {
			case 1: custId = Integer.parseInt(enter);
					break;
			default:
				System.out.println("Exiting the Menu .. ");
				System.exit(0);
		}
		
		System.out.println("**************** Employee Menu ******************");
		System.out.println("   1. View Customer Account Balance            ");
		System.out.println("   2. view Customer Transactions            ");
		System.out.println("   3. Approve or Reject Customer Account            ");
		System.out.println("   4. Exit             ");
		

        enter = s.nextLine();
		System.out.println("Pressed Input :" + enter);
		
		Customer cust = new Customer(custId);
	    
		switch (Integer.parseInt(enter)) {
			case 1: cust.viewAcc();
					break;
			case 2: cust.viewTransactions();
			 		break;	
			case 3: appOrreject();
					break;	
			
			default:
				System.out.println("Exiting the Menu .. ");
				System.exit(0);
		}
		
	}

	private void appOrreject() {
		// TODO Auto-generated method stub
		
	}

}
	


