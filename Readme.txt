Please go through Report.pdf first. :)
Then, please download Stanford CoreNLP in the following link.
http://nlp.stanford.edu/software/corenlp.shtml#Download
And put all .jar files in Stanford CoreNLP into the ‘lib’ directory, please note that we need to remove stanford-corenlp-3.5.2.jar.

After the configurations, we can try the financial extractor demo following the instructions below.
=======================================================================================
cd into ModifiedStanfordNLP directory
In the ‘lib’ directory, the external libraries are included. ModifiedStanfordNLP.jar is the tweaked StanfordCoreNLP.jar, FinancialExtractor.jar is our own Financial-News-Relation-Extracting tool, the rest is necessary libraries to compile our source code.

1. Output the predictions of the test corpus based on trained model
The ‘test.txt’ is the test corpus to be predicted.

How to run:
java -mx1g -cp lib/*:. edu.stanford.nlp.pipeline.StanfordCoreNLP -annotators tokenize,ssplit,pos,lemma,ner,regexner,parse,relation -outputFormat text -sup.relation.model Training_Model/roth_relation_model_pipeline.ser -file test.txt -regexner.mapping jg-regexner.txt

What is the prediction output:
the test.txt.out file.

2. Store the predictions and implement search functions

How to run:
java -cp lib/*:. edu.nyu.cs.nlp.Main

How to search:
after running the program, please enter the company name(s) according to the instructions in terminal.

What is the processed predictions as .csv file:
trainResultsProcessed.csv

3. Data Visualization
Using the trainResultsProcessed.csv file, we designed the graphic network to show the relations on Cytoscape platform.
 