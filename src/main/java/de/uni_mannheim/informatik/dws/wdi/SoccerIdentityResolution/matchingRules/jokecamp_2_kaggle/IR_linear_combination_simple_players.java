package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.matchingRules.jokecamp_2_kaggle;

import java.io.File;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.ErrorAnalysisPlayers;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.FeaturesToCSV;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.blockers.PlayerBlockerByFirstLettersOfName;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.PlayerBirthDateComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.PlayerNameComparatorLevenshtein;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.MovieBlockingKeyByDecadeGenerator;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieDateComparator10Years;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieTitleComparatorLevenshtein;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Movie;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.MovieXMLReader;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.PlayerXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.*;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.FeatureVectorDataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.RecordCSVFormatter;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

/**
 * Data Set Jokecamp ↔ Kaggle
 * Learning Combination Rules for Players
 */
public class IR_linear_combination_simple_players
{

    static boolean WRITE_FEATURE_SET_FOR_EXTERNAL_TOOL = false;

    public static void main( String[] args ) throws Exception
    {

        // loading data
        HashedDataSet<Player, Attribute> dataJokecamp = new HashedDataSet<>();
        new PlayerXMLReader().loadFromXML(new File("data/input/jokecamp.xml"), "/clubs/club/players/player", dataJokecamp);
        HashedDataSet<Player, Attribute> dataKaggle = new HashedDataSet<>();
        new PlayerXMLReader().loadFromXML(new File("data/input/kaggle.xml"), "/clubs/club/players/player", dataKaggle);

        System.out.println("Sample from jokecamp others: " + dataJokecamp.getRandomRecord());
        System.out.println("Sample from kaggle: " + dataKaggle.getRandomRecord());

        // create a matching rule
        LinearCombinationMatchingRule<Player, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
                0.9);


        // add comparators
        // matchingRule.addComparator(new MovieDateComparator10Years(), 0.5);
        matchingRule.addComparator(new PlayerNameComparatorLevenshtein(), 1.0);
        //matchingRule.addComparator(new PlayerBirthDateComparatorLevenshtein(), 0.5);

        // create a blocker (blocking strategy)
        StandardRecordBlocker<Player, Attribute> blocker = new StandardRecordBlocker<Player, Attribute>(new PlayerBlockerByFirstLettersOfName());
//        StandardRecordBlocker<Player, Attribute> blocker = new StandardRecordBlocker<Player, Attribute>(new PlayerBlockerByBirthYear());

//        NoBlocker<Player, Attribute> blocker = new NoBlocker<>();
//		SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByDecadeGenerator(), 1);


        // Initialize Matching Engine
        MatchingEngine<Player, Attribute> engine = new MatchingEngine<>();

        // Execute the matching
        Processable<Correspondence<Player, Attribute>> correspondences = engine.runIdentityResolution(
                dataJokecamp, dataKaggle, null, matchingRule,
                blocker);

        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/jokecamp_2_kaggle_correspondences_players.csv"), correspondences);

        // load the gold standard (test set)
        MatchingGoldStandard gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File(
                "data/goldstandard/gs_jokecamp_kaggle_players.csv"));

        // evaluate your result
        MatchingEvaluator<Player, Attribute> evaluator = new MatchingEvaluator<Player, Attribute>(true);
        Performance perfTest = evaluator.evaluateMatching(correspondences.get(),
                gsTest);
        new ErrorAnalysisPlayers().printFalsePositives(correspondences, gsTest);
        new ErrorAnalysisPlayers().printFalseNegatives(dataJokecamp, dataKaggle, correspondences, gsTest);
        // print the evaluation result
        System.out.println("Jokecamp ↔ Kaggle");
        System.out
                .println(String.format(
                        "Precision: %.4f\nRecall: %.4f\nF1: %.4f",
                        perfTest.getPrecision(), perfTest.getRecall(),
                        perfTest.getF1()));




        if(WRITE_FEATURE_SET_FOR_EXTERNAL_TOOL) {

            System.out.println("Writing Features for an External Tool...");

            // generate feature data set for RapidMiner
            RuleLearner<Player, Attribute> learner = new RuleLearner<>();

            FeatureVectorDataSet features = learner.generateTrainingDataForLearning(
                    dataJokecamp, dataKaggle, gsTest, matchingRule, null
            );

            new RecordCSVFormatter().writeCSV(new File("data/output/jokecamp_kaggle_features.csv"), features);
            System.out.println(FeaturesToCSV.writeFeaturesInCSV(features, "data/output/jokecamp_kaggle_features2.csv"));
            System.out.println("Finished Writing.");
        }

    }


}
