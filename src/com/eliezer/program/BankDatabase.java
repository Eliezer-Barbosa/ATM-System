package com.eliezer.program;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BankDatabase {

	//private Account[] arrayAccounts;
	private List<Account> accountsList;

	public BankDatabase() {

		Client c1 = new Client("Oliver", "Guy", "Simon");
		Client c2 = new Client("George", "Alisson", "Clooney");
		Client c3 = new Client("Harry", "The", "Potter");
		Client c4 = new Client("Jack", "Owen", "Stuart");
		Client c5 = new Client("Charlie", "Jazz", "Grey");
		Client c6 = new Client("James", "Joseph", "Kid");
		Client c7 = new Client("Ethan", "Lee", "Archer");
		Client c8 = new Client("Edward", "Cloud", "Ash");
		Client c9 = new Client("Jake", "Fleet", "Jackson");

		Account account1 = new Account(111213, 11, 1000, c1);
		Account account2 = new Account(222324, 22, 2000, c2);
		Account account3 = new Account(333435, 33, 3000, c3);
		Account account4 = new Account(444546, 44, 4000, c4);
		Account account5 = new Account(555657, 55, 5000, c5);
		Account account6 = new Account(666768, 66, 6000, c6);
		Account account7 = new Account(777879, 77, 7000, c7);
		Account account8 = new Account(888990, 88, 8000, c8);
		Account account9 = new Account(990011, 99, 9000, c9);

		Manager manager = new Manager("Eliezer", "Maia", "Barbosa", "mm", "mm");

		//arrayAccounts = new Account[9];
		accountsList = new ArrayList<>();
		
		accountsList.add(account1);
		accountsList.add(account2);
		accountsList.add(account3);
		accountsList.add(account4);
		accountsList.add(account5);
		accountsList.add(account6);
		accountsList.add(account7);
		accountsList.add(account8);
		accountsList.add(account9);
		
		/*step 1
		 * for(int i = 0; i < accountsList.size(); i++) {
			ObjectSaver.save(accountsList.get(i), accountsList.get(i).getClient().getFirstName().toLowerCase() + ".bin");
		}*/
		
		/*step 2
		 * for(int i = 0; i < accountsList.size(); i++) {
			ObjectSaver.save(accountsList.get(i), "accounts.bin");
		}*/
		
		/*step 3
		 * ObjectSaver.save(accountsList, "accounts.bin");*/
		
		/*step 4
		 * ObjectSaver.save(manager, manager.getFirstName().toLowerCase() + ".bin");*/

		/*arrayAccounts[0] = account1;
		arrayAccounts[1] = account2;
		arrayAccounts[2] = account3;
		arrayAccounts[3] = account4;
		arrayAccounts[4] = account5;
		arrayAccounts[5] = account6;
		arrayAccounts[6] = account7;
		arrayAccounts[7] = account8;
		arrayAccounts[8] = account9;*/

	}		

	/***
	 * @param accountNumber {@link Integer} - number to be verified against an array an {@link Account}
	 * @return {@link Account} - whether the {@link Account} exists or not
	 * */

	public Account getAccount(int accountNumber) throws IOException, ClassNotFoundException {
		ArrayList<Account>accountsRestored = (ArrayList<Account>) ObjectRestorer.restore("accounts.bin");
		for (Account currentAccount : accountsRestored ) {
			if(currentAccount.getNumber() == accountNumber) {
				Account con = (Account) ObjectRestorer.restore(
						currentAccount.getClient().getFirstName().toLowerCase()+".bin");
				System.out.println(con.getClient().getFirstName()+"'s account has been found");
				return con;
			} 

		}
		return null;
	}

	/***
	 * @param userAccountNumber {@link Integer} - to be verified
	 * @param userPIN {@link Integer} - to be verified
	 * @return {@link Boolean}
	 * */

	public boolean authenticateUser(int userAccountNumber, int userPIN) 
			throws ClassNotFoundException, IOException {
		Account userAccount = getAccount(userAccountNumber);
		if(userAccount != null)
			return userAccount.validatePIN(userPIN);
		else
			return false;
	}

}
