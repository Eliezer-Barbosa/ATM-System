package com.eliezer.program;

import java.io.IOException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale.Category;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Account implements Serializable {

	private static final long serialVersionUID = -7734183048750593905L;
	private Client client;
	private int number;
	private int pin;
	private double balance;
	private String userInput;
	private String transaction_type;
	private double value;
	private String category;
	
	//Constructor with four arguments
	public Account(int accountNumber, int pin, double balance, Client client) {
		this.setNumber(accountNumber);
		this.setPin(pin);
		this.setBalance(balance);
		this.setClient(client);
	}
	
	//Constructor default with no arguments
	public Account() {
	
	}
	
	/**
	 * Method used to perform transfers between accounts
	 * @param accountNumberReceiver {@link Integer} - number of the Account's number receiver
	 * @param value {@link Double} - amount to be transferred to the receiver
	 * @return accountReceiver {@link Account}
	 * */
	
	public Account transfer(int accountNumberReceiver, double value) {
		
		String input = JOptionPane.showInputDialog(null, "Receiver account number: ");
		
		accountNumberReceiver = Integer.parseInt(input);
		
		Account[] accountsRestored = (Account[]) ObjectRestorer.restore("accounts.bin");
		
		for (Account currentAccount : accountsRestored ) {
			
			if(currentAccount.getNumber() == accountNumberReceiver) {
				
				Account accountReceiver = (Account) ObjectRestorer.restore(
						currentAccount.getClient().getFirstName().toLowerCase()+".bin");
				
				System.out.println(accountReceiver.getClient().getFirstName() + "'account has been found");
				
				String amountTransf = JOptionPane.showInputDialog(null, "Amount to be transferred: ");
				
				value = Double.parseDouble(amountTransf);

				Account accountOrigin = (Account) ObjectRestorer.restore(
						this.getClient().getFirstName().toLowerCase()+".bin");
				
				accountOrigin.setValue(value);

				if(accountOrigin.getBalance() >= value) {

					int dialogResult = JOptionPane.showConfirmDialog(null, 
							"Proceed with the transference transaction?\n"
									+ "Receiver: " + accountReceiver.getClient().getFirstName() + "\n"
									+ "Amount due: " + NumberFormat.getCurrencyInstance().
									format(accountOrigin.getValue()) + "\n");
					
					if(dialogResult == JOptionPane.YES_OPTION){
						
						accountOrigin.setBalance(accountOrigin.getBalance() - value);
						
						accountReceiver.setBalance(accountReceiver.getBalance() + value);
						
						accountOrigin.setTransaction_type(accountOrigin.getClient().getFirstName() + 
								" Transferred " + NumberFormat.getCurrencyInstance().
								format(accountOrigin.getValue()) + " to " + 
								accountReceiver.getClient().getFirstName());

						accountReceiver.setTransaction_type(accountOrigin.getClient().getFirstName() + 
								" has Transferred " + NumberFormat.getCurrencyInstance().
								format(accountOrigin.getValue()) + " to " + 
								accountReceiver.getClient().getFirstName());

						ObjectSaver.save(accountOrigin, accountOrigin.getClient().getFirstName()
								.toLowerCase()+".bin");

						ObjectSaver.save(accountReceiver, accountReceiver.getClient().getFirstName()
								.toLowerCase()+".bin");

						JOptionPane.showMessageDialog(null, "Transference has been completed!");

						try {
							
							FileCreator.createFile(accountOrigin.getClient().getFirstName()
									.toLowerCase() + "Transactions" + ".bin", accountOrigin);
						} catch (IOException e) {
							e.printStackTrace();
						}

						try {
							
							FileCreator.createFile(accountReceiver.getClient().getFirstName()
									.toLowerCase() + "Transactions" + ".bin", accountReceiver);
						} catch (IOException e) {
							e.printStackTrace();
						}

						return accountReceiver;
						
					} if(dialogResult == JOptionPane.NO_OPTION ||
							dialogResult == JOptionPane.CANCEL_OPTION) {
						transfer(accountNumberReceiver, value);
					}
					
					if(dialogResult == JOptionPane.CLOSED_OPTION) {
						accountOrigin.exit();
					}

				} if(accountOrigin.getBalance() < value) {
					JOptionPane.showMessageDialog(null, "Insufficient funds...");
					transfer(accountNumberReceiver, value);
				}


			}

		}
		JOptionPane.showMessageDialog(null, "Account " + accountNumberReceiver + " not found.");
		return transfer(accountNumberReceiver, value);
	}

	/***
	 * Method used to perform deposit into an {@link Account}
	 * @param value {@link Double} - amount to be deposited into the {@link Account}
	 * */

	public void deposit(double value) {
		
		try {
			Account con = (Account) ObjectRestorer.restore(
					this.getClient().getFirstName().toLowerCase()+".bin");
			
			userInput = JOptionPane.showInputDialog(null, "Amount to be deposited: ",
					"Deposit Service",JOptionPane.PLAIN_MESSAGE);
			value = Double.parseDouble(userInput);
			con.setValue(value);
			con.setTransaction_type(con.getClient().getFirstName() + 
					" has deposited " + NumberFormat.getCurrencyInstance().format(con.getValue()));
			if(con.getValue() > 0){
				con.setBalance(con.getBalance() + value);
				JOptionPane.showMessageDialog(null, "transaction complete\n"
						+ "Current balance: " + 
						NumberFormat.getCurrencyInstance().format(con.getBalance()),
						"Thank You",
						JOptionPane.INFORMATION_MESSAGE);
				
				ObjectSaver.save(con, 
						this.getClient().getFirstName().toLowerCase()+".bin");
				try {
					FileCreator.createFile(
							con.getClient().getFirstName().toLowerCase() + 
							"Transactions" + ".bin",
							con);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
 			if(value <= 0){
				JOptionPane.showMessageDialog(null, "Invalid deposit! "
						+ "Negative number / Equal zero."
						+"\n"+ value,"Sorry",JOptionPane.ERROR_MESSAGE);
			}
		}catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Invalid deposit!"+"\n"+ e,"Sorry",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/***
	 * Method used to perform withdrawal from the {@link Account}
	 * @param value {@link Double}- Amount to be withdrawal from the {@link Account}
	 * @return an {@link Boolean} 
	 * */
	
	public boolean withdrawal(double value) {
		try {
			Account con = (Account) ObjectRestorer.restore(
					this.getClient().getFirstName().toLowerCase()+".bin");
			userInput = JOptionPane.showInputDialog(null, 
					"Amount to be withdrawn: ","Withdraw Service",JOptionPane.PLAIN_MESSAGE);
			value = Double.parseDouble(userInput);
			con.setValue(value);
			if(value > con.getBalance()){
				JOptionPane.showMessageDialog(null,
						"Insufficient funds!",
						"No Money",JOptionPane.WARNING_MESSAGE);
				return false;
			}else if(value < 0) {
				JOptionPane.showMessageDialog(null,
						"Invalid! negative number",
						"Negative number",JOptionPane.WARNING_MESSAGE);
				return false;
			}else if(value == 0) {
				JOptionPane.showMessageDialog(null,
						"Invalid! Zero",
						"Zero",JOptionPane.WARNING_MESSAGE);
				return false;
			}
			else
				con.setBalance(con.getBalance() - value);
			con.setTransaction_type(con.getClient().getFirstName()
					+ " has withdrawn " + NumberFormat.getCurrencyInstance().format(con.getValue()));

			JOptionPane.showMessageDialog(null, "transaction complete\n"
					+ "Current balance: " + 
					NumberFormat.getCurrencyInstance().format(con.getBalance()),"Success!",
					JOptionPane.PLAIN_MESSAGE);
			ObjectSaver.save(con, 
					this.getClient().getFirstName().toLowerCase()+".bin");
			try {
				FileCreator.createFile(con.getClient().getFirstName().toLowerCase() + 
						"Transactions" + ".bin",
						con);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Invalid withdrawal!"+"\n"+ e,"Sorry",
					JOptionPane.ERROR_MESSAGE);
		}
		return true;

	}
	
	/***
	 * Method used to display the current balance of the {@link Account}
	 * @throws IOException - if the file couldn't be found
	 * */
	
	public void displayBalance() throws IOException {
		Account con = (Account) ObjectRestorer.restore(
				this.getClient().getFirstName().toLowerCase()+".bin");

		JOptionPane.showMessageDialog(null, 
				NumberFormat.getCurrencyInstance().format(con.getBalance()),"Current balance",
				JOptionPane.PLAIN_MESSAGE);

	}
	
	/***
	 * Method used to exit from the {@link Atm} system
	 * */
	
	public void exit() {
		Account con = (Account) ObjectRestorer.restore(
				this.getClient().getFirstName().toLowerCase()+".bin");
		
		JOptionPane.showMessageDialog(null, "You have been logged out!", "Thank you " + 
		con.getClient().getFirstName(), JOptionPane.PLAIN_MESSAGE, new ImageIcon("images\\logout.png"));
		Atm atm = new Atm();
		
		try {
			atm.run();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * Method used to display the {@link Account} details 
	 * 
	 * */
	
	public void showAccountDetails() {

		Account con = (Account) ObjectRestorer.restore(
				this.getClient().getFirstName().toLowerCase()+".bin");

		JOptionPane.showMessageDialog(null, con.toString(), con.getClient().getFirstName()
				 + "'s Account details", JOptionPane.PLAIN_MESSAGE);

	}

	@Override
	public String toString() {

		Account con = (Account) ObjectRestorer.restore(
				this.getClient().getFirstName().toLowerCase()+".bin");


		return "Account number: " + con.getNumber() + "\n" +
		"Client name: " + con.getClient().getLastName().toUpperCase() + "," + " " +
		con.getClient().getFirstName() + "\n" + 
		"Balance: " + NumberFormat.getCurrencyInstance().
		format(con.getBalance()) + "\n" + 
		"Category: " + con.getCategory() + "\n" +
		"PIN: " + con.getPin();
	}
	
	/***
	 * Method used to change the current PIN number to another
	 * @param pin {@link String} - the current PIN number to be changed
	 * @param newPin {@link String} - the new PIN number 
	 * */
	
	public void changePIN(String pin, String newPin) {

		Account con = (Account) ObjectRestorer.restore(
				this.getClient().getFirstName().toLowerCase()+".bin");
		
		int attempt = 0;
		
		do {
			
			pin = JOptionPane.showInputDialog(null, "Current Pin: ");
			int i = Integer.parseInt(pin);
			
			if(i == con.getPin()) {
				
				newPin = JOptionPane.showInputDialog(null, "New Pin: ");
				int nPin = Integer.parseInt(newPin);
				con.setPin(nPin);

				ObjectSaver.save(con, this.getClient().getFirstName().toLowerCase()+".bin");

				JOptionPane.showMessageDialog(null, "PIN has been changed!\n"
						+ "previous PIN: " + i + "\n"
						+ "Current PIN: " + con.getPin());
				return;
			}else {
				attempt++;
				JOptionPane.showMessageDialog(null, "PIN does not match! Please try again\n"
						+ "attempts: " + attempt);
			}if(attempt == 3) {
				JOptionPane.showMessageDialog(null, "Attempts has been exceeded!\n"
						+ "attempts: " + attempt);
				exit();
			}
			
		} while(attempt < 3);

	}
	
	/***
	 * Method used to validate the user PIN number
	 * @param userPIN {@link Integer} - number to be validate
	 * @return {@link Boolean}
	 * */
	
	public boolean validatePIN(int userPIN) {
		Account con = (Account) ObjectRestorer.restore(
				this.getClient().getFirstName().toLowerCase()+".bin");
		if(userPIN == con.getPin())
			return true;
		else
			return false;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getTransaction_type() {
		return transaction_type;
	}

	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	/***
	 * Method used to get the category of the {@link Account} accordind to its current balance
	 * @return {@link Category} {@link String}
	 * 
	 * */
	
	public String getCategory() {

		if(this.getBalance() > 0 && this.getBalance() <= 1000) {
			this.category = "Small";
		}else if(this.getBalance() > 1000 && this.getBalance() <= 2000) {
			this.category = "Medium";
		}else if(this.getBalance() > 2000 && this.getBalance() <= 3000) {
			this.category = "Large";
		}else if(this.getBalance() > 3000) {
			this.category = "Extra Large";
		}else {
			this.category = "Empty";
		}
		return category;
	}

	public void setCategory(String category) {
		this.category = category;	
	}


}
