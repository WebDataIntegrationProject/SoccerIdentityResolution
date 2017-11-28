package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.matchingRules.dbpedia_euro16;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.ErrorAnalysisClubs;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.*;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.ClubXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.FeatureVectorDataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.RecordCSVFormatter;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

import java.io.File;

/**
 * Data Set DBPedia ↔ Euro2016
 * Learning Combination Rules for Clubs
 */
public class IR_weka_clubs
{

    static boolean WRITE_FEATURE_SET_FOR_EXTERNAL_TOOL = false;

    public static void main( String[] args ) throws Exception
    {

        // loading data
        HashedDataSet<Club, Attribute> datadbpedia = new HashedDataSet<>();
        new ClubXMLReader().loadFromXML(new File("data/input/dbpedia.xml"), "/clubs/club", datadbpedia);
        HashedDataSet<Club, Attribute> dataeuro2016 = new HashedDataSet<>();
        new ClubXMLReader().loadFromXML(new File("data/input/euro2016.xml"), "/clubs/club", dataeuro2016);

        System.out.println("Sample from dbpedia: " + datadbpedia.getRandomRecord());
        System.out.println("Sample from euro2016: " + dataeuro2016.getRandomRecord());

        String options[] = new String[] {""};
        String modelType = "SimpleLogistic"; // using a logistic regression
        WekaMatchingRule<Club, Attribute> matchingRule = new WekaMatchingRule<>(0.85, modelType, options);


        // add comparators
        matchingRule.addComparator(new ClubNameComparatorLevenshteinOptimized(true)); // 
        matchingRule.addComparator(new ClubNameComparatorJaccard(true)); // 
        matchingRule.addComparator(new ClubNameComparatorJaroWinkler(true)); // 
        matchingRule.addComparator(new ClubNameComparatorSoundex(true)); // 
        matchingRule.addComparator(new ClubNameComparatorMongeElkan(true, "levenshtein", true)); 								
        matchingRule.addComparator(new ClubNameComparatorCosine(true));  
        matchingRule.addComparator(new ClubNameComparatorDoubleMetaphone(true)); //
        matchingRule.addComparator(new ClubCountryComparator()); 
        matchingRule.addComparator(new ClubPlayerFullComparator("data/correspondences/dbpedia_2_euro2016_correspondences_players.csv", false)); 

        // F1:0.8571 [P:0.9231, R: 0.8000]
        


        // create a blocker (blocking strategy)
        //StandardRecordBlocker<Club, Attribute> blocker = new StandardRecordBlocker<Club, Attribute>(new ClubBlockerByBirthYear());
		NoBlocker<Club, Attribute> blocker = new NoBlocker<>();


        // load the gold standard (test set)
        MatchingGoldStandard goldStandardForTraining = new MatchingGoldStandard();
        System.out.println("Loading Training Gold Standard");
        goldStandardForTraining.loadFromCSVFile(new File("data/goldstandard/completeGoldstandard/gs_dbpedia_2_euro2016_WEKA_training_clubs.csv"));

        // train the matching rule's model
        RuleLearner<Club, Attribute> learner = new RuleLearner<>();
        learner.learnMatchingRule(datadbpedia, dataeuro2016, null, matchingRule, goldStandardForTraining);

        // Initialize Matching Engine
        MatchingEngine<Club, Attribute> engine = new MatchingEngine<>();

        // Execute the matching
        Processable<Correspondence<Club, Attribute>> correspondences = engine.runIdentityResolution(
                datadbpedia, dataeuro2016, null, matchingRule,
                blocker);

        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/dbpedia_2_euro2016_correspondences_WEKA_clubs.csv"), correspondences);


        // gold standard for evaluation
        MatchingGoldStandard goldStandardForEvaluation = new MatchingGoldStandard();
        System.out.println("Loading Evaluation Gold Standard");
        goldStandardForEvaluation.loadFromCSVFile(new File("data/goldstandard/completeGoldstandard/gs_dbpedia_2_euro2016_WEKA_test_clubs.csv"));

        // evaluate your result
        MatchingEvaluator<Club, Attribute> evaluator = new MatchingEvaluator<Club, Attribute>(true);
        Performance perfTest = evaluator.evaluateMatching(correspondences.get(),
                goldStandardForEvaluation);
        new ErrorAnalysisClubs().printFalsePositives(correspondences, goldStandardForEvaluation);
        new ErrorAnalysisClubs().printFalseNegatives(datadbpedia, dataeuro2016, correspondences, goldStandardForEvaluation);

        // print the evaluation result
        System.out.println("Transfermarkt ↔ Kaggle");
        System.out
                .println(String.format(
                        "Precision: %.4f\nRecall: %.4f\nF1: %.4f",
                        perfTest.getPrecision(), perfTest.getRecall(),
                        perfTest.getF1()));

        if(WRITE_FEATURE_SET_FOR_EXTERNAL_TOOL) {

            System.out.println("Writing Features for an External Tool...");

            // gold standard for all entries
            MatchingGoldStandard goldStandardForExternalTool = new MatchingGoldStandard();
            goldStandardForExternalTool.loadFromCSVFile(new File("data/goldstandard/gs_dbpedia_2_kaggle_clubs_external_tool.csv"));


            // generate feature data set for RapidMiner
            RuleLearner<Club, Attribute> learner2 = new RuleLearner<>();

            FeatureVectorDataSet features = learner2.generateTrainingDataForLearning(
                    datadbpedia, dataeuro2016, goldStandardForExternalTool, matchingRule, null
            );

            new RecordCSVFormatter().writeCSV(new File("data/output/dbpedia_2_kaggle_features.csv"), features);

            System.out.println("Finished Writing...");
        }

    }

}
