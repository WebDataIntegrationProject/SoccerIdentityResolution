package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.matchingRules.transfermarket_kaggle;

import java.io.File;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.ErrorAnalysisClubs;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.blockers.ClubBlockerByCountry;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.ClubXMLReader;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.ClubFullOrderedComparator;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.ClubNameComparatorAdditionalClubVersions;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.ClubNameComparatorCosine;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.ClubNameComparatorDoubleMetaphone;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.ClubNameComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.ClubNameComparatorJaroWinkler;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.ClubNameComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.ClubNameComparatorLevenshteinOptimized;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.ClubNameComparatorMongeElkan;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.ClubNameComparatorSoundex;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.MovieBlockingKeyByDecadeGenerator;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieDateComparator10Years;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieTitleComparatorLevenshtein;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Movie;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.MovieXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

/**
 * Data Set Transfermarket ↔ Kaggle
 * Learning Combination Rules for Clubs
 */
public class IR_linear_combination_simple_clubs
{
    public static void main( String[] args ) throws Exception
    {
        // loading data
    	HashedDataSet<Club, Attribute> dataTransfermarket = new HashedDataSet<>();
        new ClubXMLReader().loadFromXML(new File("data/input/transfermarket.xml"), "/clubs/club", dataTransfermarket);
        HashedDataSet<Club, Attribute> dataKaggle = new HashedDataSet<>();
        new ClubXMLReader().loadFromXML(new File("data/input/kaggle.xml"), "/clubs/club", dataKaggle);
        
        System.out.println("Sample from transfermarket: " + dataTransfermarket.getRandomRecord());
        System.out.println("Sample from kaggle: " + dataKaggle.getRandomRecord());

        // create a matching rule
        LinearCombinationMatchingRule<Club, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
                0.85);
        // add comparators

        //matchingRule.addComparator(new ClubNameComparatorLevenshteinOptimized(true), 1); // 0.8387
        // matchingRule.addComparator(new ClubNameComparatorJaccard(true), 1); // 0.8077
        //matchingRule.addComparator(new ClubNameComparatorJaroWinkler(true), 1); // 0,8142
        // matchingRule.addComparator(new ClubNameComparatorSoundex(true), 1); // 0.8200
        //matchingRule.addComparator(new ClubNameComparatorCosine(true), 0.8);  // 0.8411 --> with dictionary: 0.8440			// 0.7955
        //matchingRule.addComparator(new ClubNameComparatorDoubleMetaphone(true), 1); //0.8190
        //matchingRule.addComparator(new ClubNameComparatorMongeElkan(true, "cosine", true), 1);
        
        //***BEST:***
        matchingRule.addComparator(new ClubNameComparatorMongeElkan(true, "levenshtein", true), 1.0); // F1:0.8462 [P:0.8462, R:0.8462] (MongeElkan(levenshtein) > 0.85)

        //matchingRule.addComparator(new ClubNameComparatorLevenshteinOptimized(true), 1.0);
        
        
        //matchingRule.addComparator(new ClubNameComparatorAdditionalClubVersions(true), 0.2); // 0.8*cosine + 0.6234
        
        
        
        

        // create a blocker (blocking strategy)
        NoBlocker<Club, Attribute> blocker = new NoBlocker<>();
//		SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByDecadeGenerator(), 1);

        // Initialize Matching Engine
        MatchingEngine<Club, Attribute> engine = new MatchingEngine<>();

        // Execute the matching
        Processable<Correspondence<Club, Attribute>> correspondences = engine.runIdentityResolution(
                dataTransfermarket, dataKaggle, null, matchingRule,
                blocker);

        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/transfermarket_kaggle_correspondences.csv"), correspondences);

        // load the gold standard (test set)
        MatchingGoldStandard gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File(
                "data/goldstandard/completeGoldstandard/gs_kaggle_2_transfermarkt_clubs.csv"));

        // evaluate your result
        MatchingEvaluator<Club, Attribute> evaluator = new MatchingEvaluator<Club, Attribute>(true);
        Performance perfTest = evaluator.evaluateMatching(correspondences.get(),
                gsTest);
        new ErrorAnalysisClubs().printFalsePositives(correspondences, gsTest);
        new ErrorAnalysisClubs().printFalseNegatives(dataTransfermarket, dataKaggle, correspondences, gsTest);
        // print the evaluation result
        System.out.println("Transfermarket ↔ Kaggle");
        System.out
                .println(String.format(
                        "Precision: %.4f\nRecall: %.4f\nF1: %.4f",
                        perfTest.getPrecision(), perfTest.getRecall(),
                        perfTest.getF1()));
    }
}
