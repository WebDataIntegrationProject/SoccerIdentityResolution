package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.matchingRules.dbpedia_2_jokecamp;

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
 * Data Set DBpedia ↔ Kaggle
 * Learning Combination Rules for Clubs
 */
public class IR_weka_clubs_dbpedia_jokecamp
{

    static boolean WRITE_FEATURE_SET_FOR_EXTERNAL_TOOL = false;

    public static void main( String[] args ) throws Exception
    {

        // loading data
        HashedDataSet<Club, Attribute> dataDbpedia = new HashedDataSet<>();
        new ClubXMLReader().loadFromXML(new File("data/input/dbpedia.xml"), "/clubs/club", dataDbpedia);
        HashedDataSet<Club, Attribute> dataJokecamp = new HashedDataSet<>();
        new ClubXMLReader().loadFromXML(new File("data/input/jokecamp.xml"), "/clubs/club", dataJokecamp);

        System.out.println("Sample from dbpedia: " + dataDbpedia.getRandomRecord());
        System.out.println("Sample from jokecamp: " + dataJokecamp.getRandomRecord());

        String options[] = new String[] {""};
        String modelType = "SimpleLogistic"; // using a logistic regression
        WekaMatchingRule<Club, Attribute> matchingRule = new WekaMatchingRule<>(0.9, modelType, options);


        // add comparators
        //matchingRule.addComparator(new ClubNameComparatorLevenshteinOptimized());
        //matchingRule.addComparator(new ClubNameComparatorLevenshtein(true));
        matchingRule.addComparator(new ClubNameComparatorMongeElkan(true, "levenshtein", true));
        matchingRule.addComparator(new ClubPlayerFullComparator("data/correspondences/dbpedia_2_jokecamp_correspondences_players.csv", false));
        matchingRule.addComparator(new ClubStadiumNameComparatorLevenshtein(true));
        matchingRule.addComparator(new ClubLeagueComparatorLevenshtein(true));


        // create a blocker (blocking strategy)
        //StandardRecordBlocker<Club, Attribute> blocker = new StandardRecordBlocker<Club, Attribute>(new ClubBlockerByBirthYear());
		NoBlocker<Club, Attribute> blocker = new NoBlocker<>();


        // load the gold standard (test set)
        MatchingGoldStandard goldStandardForTraining = new MatchingGoldStandard();
        System.out.println("Loading Training Gold Standard");
        goldStandardForTraining.loadFromCSVFile(new File("data/goldstandard/gs_dbpedia_2_jokecamp_clubs_67.csv"));

        // train the matching rule's model
        RuleLearner<Club, Attribute> learner = new RuleLearner<>();
        learner.learnMatchingRule(dataDbpedia, dataJokecamp, null, matchingRule, goldStandardForTraining);

        // Initialize Matching Engine
        MatchingEngine<Club, Attribute> engine = new MatchingEngine<>();

        // Execute the matching
        Processable<Correspondence<Club, Attribute>> correspondences = engine.runIdentityResolution(
                dataDbpedia, dataJokecamp, null, matchingRule,
                blocker);

        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/dbpedia_2_jokecamp_correspondences_clubs.csv"), correspondences);


        // gold standard for evaluation
        MatchingGoldStandard goldStandardForEvaluation = new MatchingGoldStandard();
        System.out.println("Loading Evaluation Gold Standard");
        goldStandardForEvaluation.loadFromCSVFile(new File("data/goldstandard/gs_dbpedia_2_jokecamp_clubs_33.csv"));

        // evaluate your result
        MatchingEvaluator<Club, Attribute> evaluator = new MatchingEvaluator<Club, Attribute>(true);
        Performance perfTest = evaluator.evaluateMatching(correspondences.get(),
                goldStandardForEvaluation);
        new ErrorAnalysisClubs().printFalsePositives(correspondences, goldStandardForEvaluation);
        new ErrorAnalysisClubs().printFalseNegatives(dataDbpedia, dataJokecamp, correspondences, goldStandardForEvaluation);

        // print the evaluation result
        System.out.println("Dbpedia ↔ Kaggle");
        System.out
                .println(String.format(
                        "Precision: %.4f\nRecall: %.4f\nF1: %.4f",
                        perfTest.getPrecision(), perfTest.getRecall(),
                        perfTest.getF1()));

    }

}
