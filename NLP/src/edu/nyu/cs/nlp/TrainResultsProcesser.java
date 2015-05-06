package edu.nyu.cs.nlp;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class TrainResultsProcesser {
	private Scanner sc;
	private PrintWriter pw;
	private ArrayList<ArrayList<String>> table;

	public TrainResultsProcesser(){
		table = new ArrayList<>();
	}

	public void processResults(String pathRead, String pathWrite, String delimiter) throws Exception {
		pw = new PrintWriter(pathWrite);
		sc = new Scanner(Paths.get(pathRead));
		String line = "";
		String delimiterRevised = "\\s*" + delimiter + "\\s*";

		while (sc.hasNextLine()) {
			line = sc.nextLine();
			if (line.trim().length() == 0) {
				int len = table.size();
				//System.out.println(len);
				if(len <= 1) {
					table.clear();
					continue;
				}
				ArrayList<String> sentence = new ArrayList<>();
				for(int i = 0; i < table.get(0).size(); i++){
					String s = table.get(0).get(i);
					int le = s.length();
					if(s.substring(le-1, le).equals(",")){
						//System.out.println("split " + s);
						sentence.add(s.substring(0,le-1));
						//System.out.println("add " + s.substring(0,le-1));
						sentence.add(",");
					}else{
						sentence.add(s);
					}
				}
				
				for(int i = 1; i < len; i += 3){
					String relation = table.get(i).get(0);
					pw.print(relation + ",");
					int start_A = 0, end_A = 0, start_B = 0, end_B = 0;
					for(String s : table.get(i+1)){
						if(s.substring(0,4).equals("esta")){
							start_A = Integer.parseInt(s.substring(7, s.length()-1));
						}
						if(s.substring(0,4).equals("eend")){
							end_A = Integer.parseInt(s.substring(5, s.length()-1));
						}
					}
					StringBuilder sb = new StringBuilder();
					for(int j = start_A; j < end_A; j++){
						sb.append(sentence.get(j) + " ");
					}
					pw.print(sb.toString() + ",");
					sb.delete(0, sb.length());
					
					for(String s : table.get(i+2)){
						if(s.substring(0,4).equals("esta")){
							start_B = Integer.parseInt(s.substring(7, s.length()-1));
						}
						if(s.substring(0,4).equals("eend")){
							end_B = Integer.parseInt(s.substring(5,s.length()-1));
						}
					}
					System.out.println(start_B + "=" + sentence.get(start_B));
					for(int j = start_B; j < end_B; j++){
						sb.append(sentence.get(j) + " ");
					}
					pw.print(sb.toString() + "\n");
					sb.delete(0, sb.length());
				}
				
				table.clear();
				sentence.clear();
			} else {
				String[] tokens = line.split(delimiterRevised);
				ArrayList<String> list = new ArrayList<String>();
				for (int i = 0; i < tokens.length; i++) {
					String s = tokens[i];
					list.add(s);
				}
				table.add(list);
			}
		}
		sc.close();
		pw.close();
		System.out.println("Training data processed, the output file is " + pathWrite);
	}
}
