package edu.nyu.cs.nlp;
import java.util.*;

public class Main {

	public static void main(String[] args) throws Exception{
		String pathRead = "test.txt.out";
		String pathWrite = "trainResultsProcessed.csv";
		TrainResultsProcessor tr_processer = new TrainResultsProcessor();
		tr_processer.processResults(pathRead, pathWrite);
        
		System.out.println("Please enter a company name you want to search. ");
		Scanner sc = new Scanner(System.in);
		String company = sc.nextLine();
		tr_processer.searchOneCompany(company);
		
		System.out.println("Please enter a pair of companies delimited by comma.");
		String pair = sc.nextLine();
		String delimiter = "\\s*" + "," + "\\s*";
		String[] tokens = pair.split(delimiter);
		tr_processer.searchByPair(tokens[0], tokens[1]);
		
		sc.close();
	}
}
