package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import com.techelevator.Campground;

public class Menu {

	private PrintWriter out;
	private Scanner in;

	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while(choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if(selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch(NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if(choice == null) {
			out.println("\n*** "+userInput+" is not a valid option ***\n");
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for(int i = 0; i < options.length; i++) {
			int optionNum = i+1;
			out.println(optionNum+") "+options[i]);
		}
		out.print("\nPlease choose an option >>> ");
		out.flush();
	}
	public long getLongFromUser() {
		long userInput = 0;
			try {
				userInput = in.nextLong();
			} catch (NumberFormatException e) {
				System.out.println("Invalid Entry");
			}
			in.nextLine();
		return userInput;
	}

	public LocalDate getDateFromUser() {
		LocalDate localDate = null;
		while (localDate == null) {
			try {
				String[] userInput = in.nextLine().split("/");
				localDate = LocalDate.of(Integer.parseInt(userInput[2]), Integer.parseInt(userInput[0]), Integer.parseInt(userInput[1]));
			} catch (Exception e) {
				System.out.println("Invalid Entry");
				System.out.println("Please try again using the format: ___/___/___  ");
				System.out.println("(Month) / (Day) / (Year)");
			}	
		}
		return localDate;
	}

	public String getStringFromUser() {
		String userInput = in.nextLine();
		return userInput;
	}
}
