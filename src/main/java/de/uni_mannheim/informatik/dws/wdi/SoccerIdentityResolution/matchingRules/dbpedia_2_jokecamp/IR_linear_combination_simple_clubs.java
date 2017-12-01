package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.matchingRules.dbpedia_2_jokecamp;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.ErrorAnalysisClubs;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.ClubNameComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.ClubNameComparatorMongeElkan;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.ClubXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

import java.io.File;

//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.MovieBlockingKeyByDecadeGenerator;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieDateComparator10Years;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieTitleComparatorLevenshtein;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Movie;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.MovieXMLReader;

/**
 * Data Set DBpedia ↔ Kaggle
 * Learning Combination Rules for Clubs
 */
public class IR_linear_combination_simple_clubs
{
    public static void main( String[] args ) throws Exception
    {
        // loading data
        HashedDataSet<Club, Attribute> dataDbpedia = new HashedDataSet<>();
        new ClubXMLReader().loadFromXML(new File("data/input/dbpedia.xml"), "/clubs/club", dataDbpedia);
        HashedDataSet<Club, Attribute> dataJokecamp = new HashedDataSet<>();
        new ClubXMLReader().loadFromXML(new File("data/input/jokecamp.xml"), "/clubs/club", dataJokecamp);

        System.out.println("Sample from dbpedia: " + dataDbpedia.getRandomRecord());
        System.out.println("Sample from jokecamp: " + dataJokecamp.getRandomRecord());

        // create a matching rule
        LinearCombinationMatchingRule<Club, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
                0.85);
        
        // F1:0.6667 [P:0.7931, R: 0.5750]
        
        // add comparators
        // matchingRule.addComparator(new MovieDateComparator10Years(), 0.5);
        matchingRule.addComparator(new ClubNameComparatorMongeElkan(true, "levenshtein", true), 1.0);

        // create a blocker (blocking strategy)
//		StandardRecordBlocker<Club, Attribute> blocker = new StandardRecordBlocker<Club, Attribute>(new MovieBlockingKeyByDecadeGenerator());
        NoBlocker<Club, Attribute> blocker = new NoBlocker<>();
//		SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByDecadeGenerator(), 1);

        // Initialize Matching Engine
        MatchingEngine<Club, Attribute> engine = new MatchingEngine<>();

        // Execute the matching
        Processable<Correspondence<Club, Attribute>> correspondences = engine.runIdentityResolution(
                dataDbpedia, dataJokecamp, null, matchingRule,
                blocker);

        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/base_dbpedia_2_jokecamp_correspondences_clubs.csv"), correspondences);

        // load the gold standard (test set)
        MatchingGoldStandard gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File(
                "data/goldstandard/completeGoldstandard/gs_dbpedia_2_jokecamp_clubs.csv"));

        // evaluate your result
        MatchingEvaluator<Club, Attribute> evaluator = new MatchingEvaluator<Club, Attribute>(true);
        Performance perfTest = evaluator.evaluateMatching(correspondences.get(),
                gsTest);
        new ErrorAnalysisClubs().printFalsePositives(correspondences, gsTest);
        new ErrorAnalysisClubs().printFalseNegatives(dataDbpedia, dataJokecamp, correspondences, gsTest);
        // print the evaluation result
        System.out.println("Dbpedia ↔ Jokecamp");
        System.out
                .println(String.format(
                        "Precision: %.4f\nRecall: %.4f\nF1: %.4f",
                        perfTest.getPrecision(), perfTest.getRecall(),
                        perfTest.getF1()));
    }
}
