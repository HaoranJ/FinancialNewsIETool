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
		sc.close();
	}
}
