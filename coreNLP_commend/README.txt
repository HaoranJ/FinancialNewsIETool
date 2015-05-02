Use coreNLP to train relation model:

coreNLP_root : stanford-corenlp-full-2015-04-20 (The Stanford coreNLP we downloaded from the website)

(1) put training input file ( Here we use "output.corp")  into coreNLP_root

(2) open "roth.properties" under coreNLP_root, modifiy:
	trainPath = output.corp
	trainUsePipelineNER=true

(3) create a "tmp" folder under coreNLP_root

(4) open commend prompt, cd to coreNLP_root, input commend line:

java -Xmx1024m -cp stanford-corenlp-3.5.2.jar;stanford-corenlp-3.5.2-models.jar;xom.jar;joda-time.jar;jollyday.jar;javax.json.jar edu.stanford.nlp.ie.machinereading.MachineReading --arguments roth.properties

(5) It start to run and show the training log on the commend prompt.

(6) When it finished, the model will be created under the "tmp" folder

==========================================

Use the trained model:

coreNLP_root : stanford-corenlp-full-2015-04-20 (the same with train model)

(1) put test input file "test.txt" under coreNLP_root

(2) put NER custom tag file "jg-regexner.txt" under coreNLP_root

(3) open commend prompt, cd to coreNLP_root, input commend line:

java -mx1g -cp stanford-corenlp-3.5.2.jar;stanford-corenlp-3.5.2-models.jar;xom.jar;joda-time.jar;jollyday.jar;javax.json.jar edu.stanford.nlp.pipeline.StanfordCoreNLP -annotators tokenize,ssplit,pos,lemma,ner,regexner,parse,relation sup.relation.model=[tmp/roth_relation_model_pipeline.ser] -file test.txt -regexner.mapping jg-regexner.txt 

(4) It will start to run test. 

(5) When it finished, it will generate an output file named:  input_file_name + ".xml" under coreNLP_root 



