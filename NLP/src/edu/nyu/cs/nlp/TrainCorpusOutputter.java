package edu.nyu.cs.nlp;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.io.Files;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.util.CoreMap;


public class TrainCorpusOutputter{

	static String inputFiles = "input.txt";
	static String outputFile = "output.corp";
	static Map<String, String> NERMAp;
	static PrintWriter pw;
	
	static List<CoreMap> mentions;
	static int idx_m = 0;

	public static void main(String[] args) throws IOException {
		
		pw = new PrintWriter(new FileWriter(outputFile));

		prepareNERMap();

		/*
		 * First step is initiating the Stanford CoreNLP pipeline (the pipeline
		 * will be later used to evaluate the text and annotate it) Pipeline is
		 * initiated using a Properties object which is used for setting all
		 * needed entities, annotations, training data and so on, in order to
		 * customized the pipeline initialization to contains only the models
		 * you need
		 */
		Properties props = new Properties();

		/*
		 * The "annotators" property key tells the pipeline which entities
		 * should be initiated with our pipeline object, See
		 * http://nlp.stanford.edu/software/corenlp.shtml for a complete
		 * reference to the "annotators" values you can set here and what they
		 * will contribute to the analyzing process
		 */
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, regexner, parse, entitymentions, relation");	
		props.put("regexner.mapping", "jg-regexner.txt");
		StanfordCoreNLP pipeLine = new StanfordCoreNLP(props);

		/*
		 * Next we can add customized annotation and trained data I will
		 * elaborate on training data in my next blog chapter, for now you can
		 * comment those lines
		 */
		//pipeLine.addAnnotator(new RegexNERAnnotator("jg-regexner.txt"));
		// pipeLine.addAnnotator(new
		// TokensRegexAnnotator("some tokenRegex structured file"));

		// Next we generate an annotation object that we will use to annotate
		// the text with
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		// String currentTime = formatter.format( System.currentTimeMillis() );

		// inputText will be the text to evaluate in this example
		File inputFile = new File(inputFiles);
		String inputText = Files.toString(inputFile, Charset.forName("UTF-8"));

		// String inputText = "some text to evaluate";
		Annotation document = new Annotation(inputText);
		// document.set( CoreAnnotations.DocDateAnnotation.class, currentTime );

		// Finally we use the pipeline to annotate the document we created
		pipeLine.annotate(document);

		/*
		 * now that we have the document (wrapping our inputText) annotated we
		 * can extract the annotated sentences from it, Annotated sentences are
		 * represent by a CoreMap Object
		 */
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		/*
		 * Next we can go over the annotated sentences and extract the annotated
		 * words, Using the CoreLabel Object
		 */
		int idx_s = 0;
		int idx_tp = 0;	//token index per sentence, reset to 0 when each sentence begins
		int idx_ta = 0;	//token index per article

		for (CoreMap sentence : sentences) {
			
			mentions = sentence
					.get(CoreAnnotations.MentionsAnnotation.class);
			
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				
				int isMention = checkMentions(pw, idx_s, idx_tp, idx_ta);
				//System.out.println("isMention=" + isMention);
				
				//if it's a mention start, print a mention and continue;
				if(isMention != 0){
					idx_ta++;
					if(isMention == 2){ idx_tp += 1; };
					//System.out.println("idx_tp==" + idx_tp);
					continue;
				}
				
				// Using the CoreLabel object we can start retrieving NLP
				// annotation data
				// Extracting the Text Entity
				String text = token.getString(TextAnnotation.class);

				// Extracting Name Entity Recognition
				String ner = token.getString(NamedEntityTagAnnotation.class);
				// String ner =
				// token.getString(NormalizedNamedEntityTagAnnotation.class);

				String nerM = resetNERMap(ner);

				// Extracting Part Of Speech
				String pos = token
						.get(CoreAnnotations.PartOfSpeechAnnotation.class);

				// Extracting the Lemma
				String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
				

				pw.println(idx_s + "\t" + nerM + "\t" + idx_tp + "\t"
						+ "O" + "\t" + pos + "\t" + lemma + "\t" + "O" + "\t"
						+ "O" + "\t" + "O");

				/*
				 * There are more annotation that are available for extracting
				 * (depending on which "annotators" you initiated with the
				 * pipeline properties", examine the token, sentence and
				 * document objects to find any relevant annotation you might
				 * need
				 */
				idx_tp++;
				idx_ta++;
			}

			/*
			 * Next we will extract the SemanitcGraph to examine the connection
			 * between the words in our evaluated sentence
			 */
			SemanticGraph dependencies = sentence
					.get(CollapsedDependenciesAnnotation.class);

			/*
			 * The IndexedWord object is very similar to the CoreLabel object
			 * only is used in the SemanticGraph context
			 */
			IndexedWord firstRoot = dependencies.getFirstRoot();
			List<SemanticGraphEdge> incomingEdgesSorted = dependencies
					.getIncomingEdgesSorted(firstRoot);

			for (SemanticGraphEdge edge : incomingEdgesSorted) {
				// Getting the target node with attached edges
				IndexedWord dep = edge.getDependent();

				// Getting the source node with attached edges
				IndexedWord gov = edge.getGovernor();

				// Get the relation name between them
				GrammaticalRelation relation = edge.getRelation();
			}

			// this section is same as above just we retrieve the OutEdges
			List<SemanticGraphEdge> outEdgesSorted = dependencies
					.getOutEdgesSorted(firstRoot);
			for (SemanticGraphEdge edge : outEdgesSorted) {
				IndexedWord dep = edge.getDependent();
				// System.out.println("Dependent=" + dep);
				IndexedWord gov = edge.getGovernor();
				// System.out.println("Governor=" + gov);
				GrammaticalRelation relation = edge.getRelation();
				// System.out.println("Relation=" + relation);
			}


			// List<RelationMention> relations =
			// sentence.get(MachineReadingAnnotations.RelationMentionsAnnotation.class);
			// for(RelationMention rm : relations){
			// System.out.println(rm.getExtentString());
			// System.out.println(rm.getType());
			// }

			pw.println("");
			pw.println("");
			idx_tp = 0;
			idx_m = 0;
			idx_s++;
		}
		
		pw.close();

	}
	
	//0: not a mention, 1: part of a mention, 2: end of a mention
	public static int checkMentions(PrintWriter pw, int idx_s, int idx_tp, int idx_ta){
		//System.out.println("checkMentions[0]:" + idx_s + " " + idx_tp + " " + idx_ta);
		
		if(mentions == null || mentions.isEmpty()){ return(0); }
		if(mentions.size() == idx_m ){ return(0); }
		CoreMap cMap = mentions.get(idx_m);
		int idx_mb = cMap.get(CoreAnnotations.TokenBeginAnnotation.class);
		int idx_me = cMap.get(CoreAnnotations.TokenEndAnnotation.class);
		
		//System.out.println("checkMentions[1]:" + idx_mb + " " + idx_me);
		
		if(idx_ta == idx_mb){
			String tags = "", ners = "", texts = "";
			List<CoreLabel> labels = cMap.get(CoreAnnotations.TokensAnnotation.class);
			for(CoreLabel l: labels){
				ners = resetNERMap(l.ner());
				tags += "/" + l.tag();
				texts += "/" + l.originalText();
			}
			pw.println(idx_s + "\t" + ners + "\t" + idx_tp + "\t" + "O" + "\t" + tags.substring(1) + "\t" + texts.substring(1) + "\t" + "O" + "\t" + "O" + "\t" + "O");
			
			if(labels.size() == 1){ 
				idx_m++;
				return(2); 
			}else{
				return(1);				
			}
			
		}else if(idx_ta == idx_me-1){
			idx_m++;
			return(2);					
		}else if(idx_ta > idx_mb && idx_ta < idx_me){
			return(1);
		}else{
			return(0);
		}
	}

	public static void prepareNERMap() {
		NERMAp = new HashMap<String, String>();
		NERMAp.put("LOCATION", "Loc");
		NERMAp.put("ORGANIZATION", "Org");
		NERMAp.put("PERSON", "Peop");
		NERMAp.put("O", "O");
	}
	
	public static String resetNERMap(String key){
		String nerM = NERMAp.get(key);
		if (nerM == null || nerM.isEmpty()) {
			nerM = "OTHER";
		}
		return(nerM);
	}
}
