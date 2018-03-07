package com.eliezer.program;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Atm {
	
	static String userInput;
	static boolean continuar;
	boolean userAuthenticated;
	private BankDatabase bankDatabase;
	private int currentAccountNumber;
	private Account c;
	private Manager m;
	ImageIcon harp = new ImageIcon("images\\harp.jpg");

	public Atm() {
		userAuthenticated = false;
		currentAccountNumber = 0;
		bankDatabase = new BankDatabase();
		c = new Account();
		m = new Manager();
	}

	/***
	 * Method used to authenticate user
	 * @throws IOException, {@link ClassNotFoundException}
	 * */

	private void authenticateUser() throws IOException, ClassNotFoundException {

		int type;
		try {

			do {

				String op = (String) JOptionPane.showInputDialog(null, 
						"1 - Client\n"
					  + "2 - Administrator\n"
					  + "3 - Exit\n", "ATM System", 
					  JOptionPane.INFORMATION_MESSAGE, harp, null, "");
				
				type = Integer.parseInt(op);

				if(type == 1) {

					String str = (String) JOptionPane.showInputDialog(null, "Account Number: ",
							"Login",JOptionPane.QUESTION_MESSAGE, harp, null, "");

					int accountNumber = Integer.parseInt(str);

					String str2 = (String) JOptionPane.showInputDialog(null, "PIN Number: ",
							"Login", JOptionPane.QUESTION_MESSAGE, harp, null, "");
					
					int pin = Integer.parseInt(str2);

					userAuthenticated = 
							bankDatabase.authenticateUser(accountNumber, pin);

					if(userAuthenticated) {
						
						currentAccountNumber = accountNumber;
						
						ArrayList<Account> accountsRestored = 
								(ArrayList<Account>) ObjectRestorer.restore("accounts.bin");
						
						for (int i = 0; i < accountsRestored.size(); i++) {
							
							if(accountsRestored.get(i).getNumber() == currentAccountNumber) {
								
								Account con = (Account) ObjectRestorer.restore(
										accountsRestored.get(i).getClient().getFirstName().
										toLowerCase()+".bin");
								
								this.c = con;
								
								String path = 
										con.getClient().getFirstName().toLowerCase()+ ".bin";
								
								File file = new File(path);
								
								if(!file.exists()) {
									
									ObjectSaver.save(con, path);
								}
								
								ObjectRestorer.restore(path);

							}
						}

					} else
						JOptionPane.showMessageDialog(null, 
								"Invalid account number or PIN. please try again."
								,"", JOptionPane.INFORMATION_MESSAGE, harp);

				} else if(type == 2) {

					//manager login

					String strLogin = (String) JOptionPane.showInputDialog(null, "Login: ",
							"Login",JOptionPane.QUESTION_MESSAGE, harp, null, "");

					String strPassword = (String) JOptionPane.showInputDialog(null, "Password: ","Login", 
							JOptionPane.QUESTION_MESSAGE, harp, null, "");
					
					Manager man = (Manager) ObjectRestorer.restore("eliezer.bin");

					if(man.getLogin().equals(strLogin) && 
							man.getPassword().equals(strPassword)) {
						
						this.m = man;
						
						performManagerTransactions();
						
					} else
						JOptionPane.showMessageDialog(null, "Invalid login or password. please try again."
								,"", JOptionPane.INFORMATION_MESSAGE, harp);

				} else if(type == 3) {
					 System.exit(0);
				} else
					JOptionPane.showMessageDialog(null, "Invalid option. please try again."
							,"", JOptionPane.INFORMATION_MESSAGE, harp);
				
			} while(type < 0 || type > 2);

		} catch (NumberFormatException e) {
			
			JOptionPane.showMessageDialog(null, "Invalid input!"+"\n"+ e,"",
					JOptionPane.ERROR_MESSAGE, harp);
		}
	}

	/***
	 * Method that attempts to authenticate an user calling the method authenticateUser()
	 * @throws IOException - if the file coudn't be find
	 * @throws ClassNotFoundException - if the class coudn't be found
	 * */

	public void run() throws IOException, ClassNotFoundException {
		
		ArrayList<Account> accountsRestored = (ArrayList<Account>) ObjectRestorer.restore("accounts.bin");
		
		for (Account currentAccount : accountsRestored) {
			
			System.out.println(currentAccount.toString()+"\n");
		}

		while(true) {
			
			while(!userAuthenticated) {
				
				authenticateUser();
			}
			
			performTransactions();
		}
	}

	/***
	 * Methos used to perform the transactions of the {@link Atm} system
	 * @throws IOException - if the file coudn't be found
	 * */

	public void performTransactions() throws IOException {
		
		try {

			boolean continuar = true;
			String op;
			int receiverAccountNumber = 0;
			double value = 0;
			String password = null;
			String newPassword = null;

			Locale locale = new Locale("en", "US");
			
			Calendar calendar = Calendar.getInstance();
			
			DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL,locale);

			do {
				op = (String) JOptionPane.showInputDialog(null, 
						dateFormat.format(calendar.getTime()) +
						"\n" + "Welcome " + this.c.getClient().getFirstName() + " " + 
								this.c.getClient().getLastName() + "\n\n" 
						+ "Choose your option:" + "\n\n"
						+ "1 - Balance\n"
						+ "2 - Withdrawal\n"
						+ "3 - Deposit\n"
						+ "4 - Display Account Details\n"
						+ "5 - Transference\n"
						+ "6 - Change PIN Number\n"
						+ "0 - Finish session\n","CCT CENTRAL BANK IN IRELAND --- " + 
						this.c.getClient().getFirstName() + " " + this.c.getClient().getLastName(),
						JOptionPane.QUESTION_MESSAGE, harp, null, "");

				switch (op) {
				
				case "1":
					c.displayBalance();;
					break;

				case "2":
					c.withdrawal(value);
					break;

				case "3":
					c.deposit(value);
					break;

				case "4":
					c.showAccountDetails();
					break;

				case "5":
					c.transfer(receiverAccountNumber, value);
					break;

				case "6":
					c.changePIN(password, newPassword);
					break;

				case "0":
					c.exit();
					break;
				}
				
				if(Integer.parseInt(op) < 0 || Integer.parseInt(op) > 6) {
					
					JOptionPane.showMessageDialog(null, "Invalid option!","",
							JOptionPane.INFORMATION_MESSAGE, harp);
				}
			} while(continuar);

		} catch(NumberFormatException e) {
			
			JOptionPane.showMessageDialog(null, "Invalid option!"+"\n"+ e,"",
					JOptionPane.ERROR_MESSAGE, harp);
		}

	}

	/***
	 * Method that perform only if the user is a {@link Manager}
	 * */

	public void performManagerTransactions() {

		int op;
		
		do {

			String strOption = (String) JOptionPane.showInputDialog(null, 
					"1 - Bank Summary\n" +
							"2 - Stocks\n" +
							"3 - Logout\n", "Welcome " + 
							this.m.getFirstName() + " " + this.m.getLastName(),
							JOptionPane.INFORMATION_MESSAGE, harp, null, "");

			switch (strOption) {

			case "1":
				m.bankSummary();
				break;

			case "2":
				m.showStocks(); 
				break;

			case "3":
				m.exit();
				break;
			}
			
			op = Integer.parseInt(strOption);
			
		} while(op > 0 && op < 3);

	}


}
