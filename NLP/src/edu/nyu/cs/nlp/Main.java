package edu.nyu.cs.nlp;

public class Main {

	public static void main(String[] args) throws Exception{
		String pathRead = "test.txt.out";
		String pathWrite = "trainResultsProcessed.txt";
		TrainResultsProcesser tr_processer = new TrainResultsProcesser();
		tr_processer.processResults(pathRead, pathWrite, " ");

	}

}
