package com.eliezer.program;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Stocks implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2014969002920044032L;
	private int quantityOfClients = 0;
	private double value, calculation;
	private double sum = 0;
	ImageIcon stocks = new ImageIcon("images\\stocks.png");
	
	public void calculateStocks() {
		
		ArrayList<Account> accountsRestored = (ArrayList<Account>) ObjectRestorer.restore("accounts.bin");
		
		for (Account currentAccount : accountsRestored ) {
			
			Account con = (Account) ObjectRestorer.restore(
				currentAccount.getClient().getFirstName().toLowerCase()+".bin");
		
			sum = sum + con.getBalance();
			quantityOfClients++;
		}
		System.out.println("Total Balance: " + NumberFormat.getCurrencyInstance().format(sum));
		
		calculation = (((sum / quantityOfClients) * 0.12) / 100);
		System.out.println("Stock Value: " + NumberFormat.getCurrencyInstance().format(calculation));
		
		setValue(calculation);
		
		JOptionPane.showMessageDialog(null, "Stock value: " + 
				NumberFormat.getCurrencyInstance().format(this.getValue()),
				"Stocks", JOptionPane.PLAIN_MESSAGE, stocks);
		
		
	}
	
	public int getQuantityOfClients() {
		return quantityOfClients;
	}
	public void setQuantityOfClients(int quantityOfClients) {
		this.quantityOfClients = quantityOfClients;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}


}
