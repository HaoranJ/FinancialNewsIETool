package edu.nyu.cs.nlp;

import java.util.*;

public class Main {

	public static void main(String[] args) throws Exception {
		String pathRead = "test.txt.out";
		String pathWrite = "trainResultsProcessed.csv";
		TrainResultsProcessor tr_processer = new TrainResultsProcessor();
		tr_processer.processResults(pathRead, pathWrite);
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println();
			System.out.println("Search one company, input 1 and enter; search a pair of companies, input"
					+ " 2 and enter. Exit, input 0 and enter.");
			String s = sc.nextLine();
			if(!isInteger(s)){
				System.out.println("Sorry, your input is invalid.");
				continue;
			}
			int opt = Integer.parseInt(s);
			if (opt == 1) {
				System.out.println("Please enter a company name you want to search. ");
				String company = sc.nextLine();
				tr_processer.searchOneCompany(company);
			}else if (opt == 2) {
				System.out.println();
				System.out.println("Please enter a pair of companies delimited by comma.");
				String pair = sc.nextLine();
				String delimiter = "\\s*" + "," + "\\s*";
				String[] tokens = pair.split(delimiter);
				if(tokens.length != 2){
					System.out.println("Sorry, your input is invalid. Please enter" + 
							" two company names and split them by comma.");
					continue;
				}
				tr_processer.searchByPair(tokens[0], tokens[1]);
			}else if (opt == 0){
				break;
			}else{
				System.out.println("Sorry, your input is invalid.");
			}
		}
		sc.close();
	}
	
	private static boolean isInteger(String s) {
	    return isInteger(s,10);
	}

	public static boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
}
