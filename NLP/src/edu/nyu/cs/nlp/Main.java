package edu.nyu.cs.nlp;

public class Main {

	public static void main(String[] args) throws Exception{
		String pathRead = "test.txt.out";
		String pathWrite = "trainResultsProcessed.txt";
		TrainResultsProcessor tr_processer = new TrainResultsProcessor();
		tr_processer.processResults(pathRead, pathWrite, " ");

	}

}
