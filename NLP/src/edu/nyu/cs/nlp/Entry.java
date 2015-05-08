package edu.nyu.cs.nlp;
import java.util.regex.*;

//the entry for entity A and entity B with their relation
public class Entry {
	
    private String entity_A;
    private String entity_B;
    private String relation;
    
    public Entry(String a, String b, String r){
    	this.entity_A = a;
    	this.entity_B = b;
    	this.relation = r;
    }
    
    public String getEntityA(){
    	return entity_A;
    }
    public String getEntityB(){
    	return entity_B;
    }
    public String getRelation(){
    	return relation;
    }
    
    public boolean equals(Entry e){
    	boolean f = this.getEntityA().equals(e.getEntityA()) && this.getEntityB().equals(e.getEntityB())
    			&& this.getRelation().equals(e.getRelation());
    	return f;
    	/*boolean fa, fb, fr;
    	fa = Pattern.compile(Pattern.quote(this.getEntityA()), Pattern.CASE_INSENSITIVE).matcher(e.getEntityA()).find()
    			|| Pattern.compile(Pattern.quote(e.getEntityA()), Pattern.CASE_INSENSITIVE).matcher(this.getEntityA()).find();
    	fb = Pattern.compile(Pattern.quote(this.getEntityB()), Pattern.CASE_INSENSITIVE).matcher(e.getEntityB()).find()
    			|| Pattern.compile(Pattern.quote(e.getEntityB()), Pattern.CASE_INSENSITIVE).matcher(this.getEntityB()).find();
    	fr = this.getRelation().equals(e.getRelation());
    	return fa && fb && fr;		*/	
    }
    
    public int hashCode(){
    	return this.getEntityA().hashCode();
    }
    
    public boolean containsCompany(String c){
    	boolean fa, fb;
    	fa = Pattern.compile(Pattern.quote(this.getEntityA()), Pattern.CASE_INSENSITIVE).matcher(c).find()
    			|| Pattern.compile(Pattern.quote(c), Pattern.CASE_INSENSITIVE).matcher(this.getEntityA()).find();
    	fb = Pattern.compile(Pattern.quote(this.getEntityB()), Pattern.CASE_INSENSITIVE).matcher(c).find()
    			|| Pattern.compile(Pattern.quote(c), Pattern.CASE_INSENSITIVE).matcher(this.getEntityB()).find();
    	return fa || fb;
    }
    
    public String toString(){
    	return entity_A + "," + entity_B + "," + relation;
    }
}
