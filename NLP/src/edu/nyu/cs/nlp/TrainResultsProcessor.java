package edu.nyu.cs.nlp;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.Map.Entry;

public class TrainResultsProcessor {
	private Scanner sc;
	private PrintWriter pw;
	private ArrayList<String> table;
	private HashMap<String, Integer> map;

	public TrainResultsProcessor(){
		table = new ArrayList<>();
		map = new HashMap<>();
	}

	public void processResults(String pathRead, String pathWrite) throws Exception {
		pw = new PrintWriter(pathWrite);
		sc = new Scanner(Paths.get(pathRead));
		pw.println("Subjective_Company,Objective_Company,Relation,Times");;
		String line = "";
		int counter = 0;
		while (sc.hasNextLine()) {
			line = sc.nextLine();
			if (line.trim().length() == 0) {
				int len = table.size();
				if(len%3 != 0) {
					table.clear();
					continue;
				}
				
				for(int i = 0; i < len; i += 3){
					String relation = table.get(i);
					String a = table.get(i+1);
					String b = table.get(i+2);
					
					StringBuilder sb = new StringBuilder();
					sb.append(a + ",");
					sb.append(relation + ",");
					sb.append(b);
					String str = sb.toString();
					putInMap(str);
					counter++;
					sb.delete(0, sb.length());	
				}
				table.clear();
			} else {
				table.add(line);
			} 
		}
		sc.close();
		for(Map.Entry<String, Integer> e : map.entrySet()){
			pw.println(e.getKey() + "," + e.getValue());
		}
		pw.close();
		System.out.println("Training data processed, " + "totally " + counter + " relation entries stored, "
				+ " the output file is " + pathWrite);
	}
	
	public void putInMap(String s){
		if(map.containsKey(s)){
			map.put(s, map.get(s) + 1);
		}else{
			map.put(s, 1);
		}
	}
	
	//@param search a company name
	//@return the relation entries containing the company name.
	public void searchOneCompany(String c) throws FileNotFoundException{
		int ct = 0;
		if(c.trim().length() == 0){
			System.out.println("Sorry, the input is valid.");
			return;
		}
		System.out.println("The results for search the company " + c + " as following. ");
		HashMap<String, Integer> unsorted_map = new HashMap<>();
		for(Map.Entry<String, Integer> it : map.entrySet()){
			if(containsCompany(it.getKey(), c)){
				unsorted_map.put(it.getKey(), it.getValue());
			}
		}
		LinkedHashMap<String, Integer> sorted_map = sortByValue(unsorted_map);
		
		//output the results for the company
		for(Map.Entry<String, Integer> it : sorted_map.entrySet()){
			ct++;
			int times = it.getValue();
			System.out.println(it.getKey() + " = " + times + " times.");
		}
		if(ct == 0){
			System.out.println("Sorry, there is no record for your search.");
		}
		
	}
	
	public void searchByPair(String a, String b) throws FileNotFoundException{
		int ct = 0;
		System.out.println("The results for searching the pair of "
				+ a + " and " + b + " as following. ");
		if(a.trim().length() == 0 || b.trim().length() == 0){
			System.out.println("Sorry, the inputs are invalid.");
			return;
		}
		HashMap<String, Integer> unsorted_map = new HashMap<>();
		for(Map.Entry<String, Integer> it : map.entrySet()){
			if(containsPair(it.getKey(), a, b)){
				unsorted_map.put(it.getKey(), it.getValue());
			}
		}
		LinkedHashMap<String, Integer> sorted_map = sortByValue(unsorted_map);
		
		//output the results for the company
		for(Map.Entry<String, Integer> it : sorted_map.entrySet()){
			ct++;
			int times = it.getValue();
			pw.println(it.getKey() + " = " + times + " times.");
			System.out.println(it.getKey() + " = " + times + " times.");
		}
		if(ct == 0){
			System.out.println("Sorry, there is no record for your search.");
		}
	}
	//check whether a relation entry e contains a company c
	private boolean containsCompany(String e, String c){
		String[] tokens = e.split(",");
		String a = tokens[0], b = tokens[2];
		boolean fa, fb;
    	fa = Pattern.compile(Pattern.quote(a), Pattern.CASE_INSENSITIVE).matcher(c).find()
    			|| Pattern.compile(Pattern.quote(c), Pattern.CASE_INSENSITIVE).matcher(a).find();
    	fb = Pattern.compile(Pattern.quote(b), Pattern.CASE_INSENSITIVE).matcher(c).find()
    			|| Pattern.compile(Pattern.quote(c), Pattern.CASE_INSENSITIVE).matcher(b).find();
    	return fa || fb;
	}
	
	//check whether a relation entry happens between the pair of the companies a, b
	private boolean containsPair(String e, String a, String b){
		String[] tokens = e.split(",");
		boolean fa = nameEquals(tokens[0], a) && nameEquals(tokens[2], b);
		boolean fb = nameEquals(tokens[0], b) && nameEquals(tokens[2], a);
		return fa || fb;
	}
	
	private boolean nameEquals(String a, String b){
		return Pattern.compile(Pattern.quote(a), Pattern.CASE_INSENSITIVE).matcher(b).find()
		|| Pattern.compile(Pattern.quote(b), Pattern.CASE_INSENSITIVE).matcher(a).find();
	}
	
	// sort the relation entry by mentioned times
	private LinkedHashMap<String, Integer> sortByValue(Map<String, Integer> map){
		ValueComparator vc = new ValueComparator();
		LinkedList<Entry<String, Integer>> list = new LinkedList<Map.Entry<String,Integer>>(map.entrySet());
		Collections.sort(list, vc);
		LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for(Entry<String, Integer> e : list){
			sortedMap.put(e.getKey(), e.getValue());
		}
		return sortedMap;
	}
}

class ValueComparator implements Comparator<Entry<String, Integer>>{
	public int compare(Entry<String, Integer> a, Entry<String, Integer> b){
		return b.getValue().compareTo(a.getValue());
	}
}

