package edu.nyu.cs.nlp;
import java.util.*;

public class Main {

	public static void main(String[] args) throws Exception{
		String pathRead = "test.txt.out";
		String pathWrite = "trainResultsProcessed.csv";
		TrainResultsProcessor tr_processer = new TrainResultsProcessor();
		tr_processer.processResults(pathRead, pathWrite);
		Scanner sc = new Scanner(System.in);
		System.out.println("Search one company, enter 1; search a pair of companies, enter 2. ");
		int opt = Integer.parseInt(sc.nextLine());
		if(opt == 1){
			System.out.println("Please enter a company name you want to search. ");
			String company = sc.nextLine();
			tr_processer.searchOneCompany(company);
		}
		if(opt == 2){
			System.out.println();
			System.out.println("Please enter a pair of companies delimited by comma.");
			String pair = sc.nextLine();
			String delimiter = "\\s*" + "," + "\\s*";
			String[] tokens = pair.split(delimiter);
			tr_processer.searchByPair(tokens[0], tokens[1]);
		}
		sc.close();
	}
}
