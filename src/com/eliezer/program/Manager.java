package com.eliezer.program;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.stream.Stream;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Manager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7041723148581677118L;
	private String fullName, firstName, middleName, lastName, login, password;
	private double sum;

	public Manager(String firstName, String middleName, String lastName, 
			String login, String password){
		this.setFirstName(firstName);
		this.setMiddleName(middleName);
		this.setLastName(lastName);
		this.setLogin(login);
		this.setPassword(password);
		this.setFullName(this.getFirstName() + " " + this.getMiddleName() + " " +
				this.getLastName());
	}
	
	public Manager() {
		// TODO Auto-generated constructor stub
	}
	
	/***
	 * Method that displays the bank accounts separated by groups of differents categories,
	 * according to its available balance.
	 * */
	
	public void bankSummary() {
		
		double sum = 0;
		int empty = 0, small = 0, medium = 0, large = 0, xLarge = 0;
		String strEmpty = "", strSmall = "", strMedium = "", strLarge = "", strXLarge = "";
		
		ArrayList<Account> accountsRestored = (ArrayList<Account>) ObjectRestorer.restore("accounts.bin");
		
		for (Account currentAccount : accountsRestored ) {
			
			Account con = (Account) ObjectRestorer.restore(
				currentAccount.getClient().getFirstName().toLowerCase()+".bin");
		
			sum = sum + con.getBalance();
			this.setSum(sum);
			
			if(con.getCategory().equals("Small")) {
				strSmall = strSmall + con.getClient().getFirstName() + " ";
				small++;
				}
			else if(con.getCategory().equals("Medium")) {
				strMedium = strMedium + con.getClient().getFirstName() + " ";
				medium++;
				}
			else if(con.getCategory().equals("Large")) {
				strLarge = strLarge + con.getClient().getFirstName() + " ";
				large++;
				}
			else if(con.getCategory().equals("Extra Large")) {
				strXLarge = strXLarge + con.getClient().getFirstName() + " ";
				xLarge++;
				}
			else {
				strEmpty = strEmpty + con.getClient().getFirstName() + " ";
				empty++;
				}
		}
		
		JOptionPane.showMessageDialog(null, "Total: " + 
		NumberFormat.getCurrencyInstance().format(this.getSum()) + "\n" + 
				"Categories:\n " + 
				"--------------\n" +
				empty + " " + "Empty\n" + strEmpty + "\n" +
				"------------------------------------------\n" +
				small + " " + "Small\n" + strSmall + "\n" +
				"------------------------------------------\n" +
				medium + " " + "Medium\n" + strMedium + "\n" +
				"------------------------------------------\n" +
				large + " " + "Large\n" + strLarge + "\n" +
				"------------------------------------------\n" +
				xLarge + " " + "Extra Large\n" + strXLarge + "\n");
	}
	
	/***
	 * Method that log the user out of the {@link Atm} system.
	 * */
	
	public void exit() {
		
		JOptionPane.showMessageDialog(null, "You have been logged out!","Thank you",
				JOptionPane.PLAIN_MESSAGE, new ImageIcon("images\\logout.png"));
		
		Atm atm = new Atm();
		
		try {
			atm.run();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Override
	public String toString() {
		
		return this.getFullName();
	}

	public void showStocks() {
		
		String path = "stocks.bin";
		
		File stocks = new File(path);
		
		if(!stocks.exists()) {
			
			ObjectSaver.save(stocks, path);
		}
		
		ObjectRestorer.restore(path);
		Stocks stk = new Stocks();
		stk.calculateStocks();
		
	}
	

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}


}
