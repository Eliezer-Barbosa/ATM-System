package com.eliezer.program;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.text.AbstractDocument.Content;

public class FileCreator {
	
	public static void createFile(String path, Account account) throws IOException{

		SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		FileWriter fileWriter = new FileWriter(path, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);

		printWriter.println(date.format(new Date()));
		printWriter.println("Account: " + account.getNumber());
		printWriter.println(account.getTransaction_type());
		printWriter.println("Funds: " + NumberFormat.getCurrencyInstance().format(account.getBalance()));
		printWriter.println("------------------------------------------");

		fileWriter.close();

	}

	/***
	 * Method that reads the content of a file 
	 * @param path {@link String} - the file to be read
	 * @return {@link Content} {@link String} - the content read from the file
	 * @throws IOException - if the file couldn't be read
	 * 
	 * */

	public static String readFile(String path) throws IOException{

		FileReader fileReader = new FileReader(path);
		BufferedReader br = new BufferedReader(fileReader);

		String content = br.readLine();
		while(content != null) {
			System.out.println(content);
			content = br.readLine();
		}
		br.close();
		
		return content;
	}
	
	/***
	 * Method that creates and writes to a file
	 * @param path {@link String} - the name given to the file
	 * @param stocks {@link Stocks} - gets the contents to write into the file
	 * @throws IOException - if the file couldn't be created
	 * */

	public static void createFile(String path, Stocks stocks) throws IOException{

		SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		FileWriter fileWriter = new FileWriter(path, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);

		printWriter.println(date.format(new Date()));
		printWriter.println("Stock value: " + NumberFormat.getCurrencyInstance().
				format(stocks.getValue()));
		printWriter.println("------------------------------------------");

		fileWriter.close();

	}


}
