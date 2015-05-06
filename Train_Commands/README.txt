Use coreNLP to train relation model:

coreNLP_root : stanford-corenlp-full-2015-04-20 (The Stanford coreNLP we downloaded from the website)

(1) put training input file ( Here we use "output.corp")  under coreNLP_root

(2) open "roth.properties" under coreNLP_root, modify:
	trainPath = output.corp
	trainUsePipelineNER=true

(3) create a “Training_Model” folder under coreNLP_root

(4) open command prompt, cd to coreNLP_root, input command line:

java -Xmx1024m -cp stanford-corenlp-3.5.2.jar:stanford-corenlp-3.5.2-models.jar:xom.jar:joda-time.jar:jollyday.jar:javax.json.jar edu.stanford.nlp.ie.machinereading.MachineReading --arguments roth.properties

or 
java -Xmx1024m -cp *:. edu.stanford.nlp.ie.machinereading.MachineReading --arguments roth.properties


(for mac, ; should be replaced with :)

(5) It will start to run and show the training log on the command prompt.

(6) When it finished, the model will be created under the "tmp" folder

==========================================

Use the trained model:

coreNLP_root : stanford-corenlp-full-2015-04-20 (the same with train model)

(1) put test input file "test.txt" under coreNLP_root

(2) put NER custom tag file "jg-regexner.txt" under coreNLP_root

(3) open command prompt, cd to coreNLP_root, input command line:

java -mx1g -cp stanford-corenlp-3.5.2.jar:stanford-corenlp-3.5.2-models.jar:xom.jar:joda-time.jar:jollyday.jar:javax.json.jar edu.stanford.nlp.pipeline.StanfordCoreNLP -annotators tokenize,ssplit,pos,lemma,ner,regexner,parse,relation -sup.relation.model tmp/roth_relation_model_pipeline.ser -file test.txt -regexner.mapping jg-regexner.txt

or
java -mx1g -cp *:. edu.stanford.nlp.pipeline.StanfordCoreNLP -annotators tokenize,ssplit,pos,lemma,ner,regexner,parse,relation —outputFormat text -sup.relation.model Training_Model/roth_relation_model_pipeline.ser -file test.txt -regexner.mapping jg-regexner.txt

*******output text*******(Use this!!!!!!!!!!!!!!)
java -mx1g -cp *:. edu.stanford.nlp.pipeline.StanfordCoreNLP -annotators tokenize,ssplit,pos,lemma,ner,regexner,parse,relation -outputFormat text -sup.relation.model Training_Model/roth_relation_model_pipeline.ser -file test.txt -regexner.mapping jg-regexner.txt

for Windows
java -mx1g -cp stanford-corenlp-3.5.2.jar;stanford-corenlp-3.5.2-models.jar;xom.jar;joda-time.jar;jollyday.jar;javax.json.jar edu.stanford.nlp.pipeline.StanfordCoreNLP -annotators tokenize,ssplit,pos,lemma,ner,regexner,parse,relation -sup.relation.model tmp/roth_relation_model_pipeline.ser -file test.txt -regexner.mapping jg-regexner.txt

(4) It will start to run test. 

(5) When it finished, it will generate an output file named:  input_file_name + ".xml" under coreNLP_root 



