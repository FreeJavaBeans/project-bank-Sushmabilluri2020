package com.revature.banking;

import java.util.Scanner;

public class ScannerSingleton {

	private static Scanner s = new Scanner(System.in);
	
	public static Scanner getScanner() {
		return s;
	}
	
}
