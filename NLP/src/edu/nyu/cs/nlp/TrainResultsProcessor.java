package edu.nyu.cs.nlp;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;

public class TrainResultsProcessor {
	private Scanner sc;
	private PrintWriter pw;
	private ArrayList<String> table;
	private HashMap<Entry, Integer> map;

	public TrainResultsProcessor(){
		table = new ArrayList<>();
		map = new HashMap<>();
	}

	public void processResults(String pathRead, String pathWrite) throws Exception {
		pw = new PrintWriter(pathWrite);
		sc = new Scanner(Paths.get(pathRead));
		pw.println("Subjective_Company,Objective_Company,Relation");;
		String line = "";

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
					
					Entry e = new Entry(a, b, relation);
					putInMap(e);
					pw.print(a + "," + b + "," + relation + "\n");
				}
				table.clear();
			} else {
				table.add(line);
			} 
		}
		sc.close();
		pw.close();
		System.out.println("Training data processed, the output file is " + pathWrite);
		System.out.println(map);
	}
	
	private void putInMap(Entry e){
		if(map.containsKey(e)){
			map.put(e, map.get(e) + 1);
		}else{
			map.put(e, 1);
		}
	}
	
	//search a company, output the relation entries of it.
	public void searchOneCompany(String c) throws FileNotFoundException{
		String outFileName = "Summary for " + c;
		pw = new PrintWriter(outFileName);
		HashMap<Entry, Integer> unsorted_map = new HashMap<>();
		ValueComparator vc = new ValueComparator(unsorted_map);
		TreeMap<Entry, Integer> sorted_map = new TreeMap<>(vc);
		for(Map.Entry<Entry, Integer> it : map.entrySet()){
			if(it.getKey().containsCompany(c)){
				unsorted_map.put(it.getKey(), it.getValue());
			}
		}
		
		//output the results for the company
		for(Map.Entry<Entry, Integer> it : sorted_map.entrySet()){
			int times = it.getValue();
			pw.println(it.getKey() + " = " + times + "times.");
		}
		pw.close();
		System.out.println(unsorted_map);
		System.out.println(sorted_map);
		System.out.println("The summary for" + c + "has been outputted as the 'Summary for "
				+ c + "' text file. ");
		
	}
}


class ValueComparator implements Comparator<Entry>{
	Map<Entry, Integer> base = new HashMap<>();
	
	public ValueComparator(Map<Entry, Integer> base){
		this.base = base;
	}
	
	public int compare(Entry a, Entry b){
		if (base.get(a) >= base.get(b)) {
            return 1;
        } else {
            return -1;
        }
	}
}
