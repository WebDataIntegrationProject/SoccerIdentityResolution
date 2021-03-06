package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.matchingRules.transfermarket_kaggle;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.ErrorAnalysisPlayers;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.FeaturesToCSV;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.blockers.PlayerBlockerByBirthYear;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.blockers.PlayerBlockerByBirthYearAndMonth;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.blockers.PlayerBlockerByFirstLettersOfName;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.blockers.PlayerBlockerByNameSoundex;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.ClubNameComparatorLevenshteinOptimized;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.ClubNameComparatorMongeElkan;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.PlayerBirthDateComparatorExactDateComparison;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.PlayerBirthDateComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.PlayerNameComparatorCosine;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.PlayerNameComparatorDoubleMetaphone;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.PlayerNameComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.PlayerNameComparatorJaroWinkler;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.PlayerNameComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.PlayerNameComparatorMongeElkan;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.PlayerNameComparatorPhoneticsCombination;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.PlayerNameComparatorSoundex;
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
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Record;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.RecordCSVFormatter;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

/**
 * Data Set Transfermarket ↔ Jokecamp
 * Learning Combination Rules for Players
 */
public class IR_linear_combination_simple_players
{

    static boolean WRITE_FEATURE_SET_FOR_EXTERNAL_TOOL = true;

    public static void main( String[] args ) throws Exception
    {

        // loading data
    	HashedDataSet<Player, Attribute> dataTransfermarket = new HashedDataSet<>();
        new PlayerXMLReader().loadFromXML(new File("data/input/transfermarket.xml"), "/clubs/club/players/player", dataTransfermarket);
        HashedDataSet<Player, Attribute> dataKaggle = new HashedDataSet<>();
        new PlayerXMLReader().loadFromXML(new File("data/input/kaggle.xml"), "/clubs/club/players/player", dataKaggle);
        
        System.out.println("Sample from transfermarket: " + dataTransfermarket.getRandomRecord());
        System.out.println("Sample from kaggle: " + dataKaggle.getRandomRecord());
        System.out.println("KAGGLE: " + dataKaggle.getRandomRecord().getBirthDate());
        System.out.println("TM: " + dataTransfermarket.getRandomRecord().getBirthDate());


        // create a matching rule
        LinearCombinationMatchingRule<Player, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
                0.85);


        // add comparators
       // matchingRule.addComparator(new PlayerNameComparatorCosine(true), 0.8);			//0.9474 (0.8*Cosine + 0.2*Date > 0.8) on preprocessed data
       // matchingRule.addComparator(new PlayerNameComparatorJaccard(true), 1.0);			// 0.9750 (0.8*Jaccard + 0.2*Date > 0.8) (same for: 1.0*Jaccard > 0.8)  on preprocessed data
        //matchingRule.addComparator(new PlayerNameComparatorLevenshtein(true), 0.8);			// 0.9211 (0.8*Levenshtein + 0.2*Date > 0.8)
       //matchingRule.addComparator(new PlayerNameComparatorJaroWinkler(true), 0.8); 	//0.9750 (0.8*JaroWinkler + 0.2*Date > 0.8)  on preprocessed data
       // matchingRule.addComparator(new PlayerNameComparatorSoundex(true), 0.8);			// 0.9474 (0.8*Soundex + 0.2*Date > 0.8)  on preprocessed data
       // matchingRule.addComparator(new PlayerNameComparatorPhoneticsCombination(true), 0.8); // 0.9474 (0.8*PhoneticsCombination + 0.2*Date > 0.8)  on preprocessed data
        //matchingRule.addComparator(new PlayerNameComparatorMongeElkan(true, "jaroWinkler"), 1); // 0.9750  on preprocessed data
        //matchingRule.addComparator(new PlayerNameComparatorMongeElkan(true, "cosine"), 0.8);	// 0.8250  on preprocessed data
        //matchingRule.addComparator(new PlayerNameComparatorDoubleMetaphone(true), 0.8);		// 0.9333 (0.8*DoubleMetaphone + 0.2*Date > 0.8)  on preprocessed data
        matchingRule.addComparator(new PlayerNameComparatorMongeElkan(true, "doubleMetaphone"), 0.8);	// F1:0.9784 [P:0.9714, R:0.9855] (0.8*MongeElkan(DoubleMetaphone) + 0.2*Date > 0.9)  on preprocessed data
        
        matchingRule.addComparator(new PlayerBirthDateComparatorExactDateComparison(), 0.2);

        // create a blocker (blocking strategy)
         StandardRecordBlocker<Player, Attribute> blocker = new StandardRecordBlocker<Player, Attribute>(new PlayerBlockerByBirthYearAndMonth());
        // StandardRecordBlocker<Player, Attribute> blocker = new StandardRecordBlocker<Player, Attribute>(new PlayerBlockerByNameSoundexLastName());
//        StandardRecordBlocker<Player, Attribute> blocker = new StandardRecordBlocker<Player, Attribute>(new PlayerBlockerByBirthYear());
//        NoBlocker<Player, Attribute> blocker = new NoBlocker<>();
//		SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByDecadeGenerator(), 1);


        // Initialize Matching Engine
        MatchingEngine<Player, Attribute> engine = new MatchingEngine<>();

        // Execute the matching
        Processable<Correspondence<Player, Attribute>> correspondences = engine.runIdentityResolution(
                dataTransfermarket, dataKaggle, null, matchingRule,
                blocker);

        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/transfermarket_kaggle_correspondences_players.csv"), correspondences);

        // load the gold standard (test set)
        MatchingGoldStandard gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File(
                "data/goldstandard/completeGoldstandard/gs_kaggle_2_transfermarkt_players.csv"));

        // evaluate your result
        MatchingEvaluator<Player, Attribute> evaluator = new MatchingEvaluator<Player, Attribute>(true);
        Performance perfTest = evaluator.evaluateMatching(correspondences.get(),
                gsTest);
        new ErrorAnalysisPlayers().printFalsePositives(correspondences, gsTest);
        new ErrorAnalysisPlayers().printFalseNegatives(dataTransfermarket, dataKaggle, correspondences, gsTest);
        // print the evaluation result
        System.out.println("Transfermarket ↔ Kaggle");
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
                    dataKaggle, dataTransfermarket, gsTest, matchingRule, null
            );

            new RecordCSVFormatter().writeCSV(new File("data/output/transfermarket_kaggle_features.csv"), features);
            System.out.println(FeaturesToCSV.writeFeaturesInCSV(features, "data/output/transfermarket_kaggle_features2.csv"));
            System.out.println("Finished Writing.");
        }

    }


}
