package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.matchingRules.dbpedia_euro16;

import java.io.File;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.ErrorAnalysisClubs;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.ClubXMLReader;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.ClubCountryComparator;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.ClubLeagueComparatorMongeElkan;
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
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

/**
 * Data Set DBPedia ↔ Euro2016
 * Learning Combination Rules for Clubs
 */
public class IR_linear_combination_simple_clubs
{
    public static void main( String[] args ) throws Exception
    {
        // loading data
    	HashedDataSet<Club, Attribute> datadbpedia = new HashedDataSet<>();
        new ClubXMLReader().loadFromXML(new File("data/input/dbpedia.xml"), "/clubs/club", datadbpedia);
        HashedDataSet<Club, Attribute> dataeuro2016 = new HashedDataSet<>();
        new ClubXMLReader().loadFromXML(new File("data/input/euro2016.xml"), "/clubs/club", dataeuro2016);
        
        System.out.println("Sample from dbpedia: " + datadbpedia.getRandomRecord());
        System.out.println("Sample from euro2016: " + dataeuro2016.getRandomRecord());

        // create a matching rule
        LinearCombinationMatchingRule<Club, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
                0.9);
        // add comparators

        //matchingRule.addComparator(new ClubNameComparatorLevenshteinOptimized(true), 1); // 
        // matchingRule.addComparator(new ClubNameComparatorJaccard(true), 1); // 
        //matchingRule.addComparator(new ClubNameComparatorJaroWinkler(true), 1); // 
        // matchingRule.addComparator(new ClubNameComparatorSoundex(true), 1); // 
        matchingRule.addComparator(new ClubNameComparatorMongeElkan(true, "levenshtein", true), 1); // 0.7692 (MongeElkan(levenshtein) > 0.85)
        																							// 0.7778 (MongeElkan(levenshtein) > 0.9)
        //matchingRule.addComparator(new ClubNameComparatorCosine(true), 1.0);  //  (0.8*CosineName + 0.2*country > 0.8)
        //matchingRule.addComparator(new ClubNameComparatorMongeElkan(true, "cosine", true), 0.7);
       // matchingRule.addComparator(new ClubNameComparatorDoubleMetaphone(true), 1); //
        
        
        //matchingRule.addComparator(new ClubCountryComparator(), 0.15); 
        //matchingRule.addComparator(new ClubLeagueComparatorMongeElkan(true, "cosine"), 0.3); 
        //matchingRule.addComparator(new ClubNameComparatorDoubleMetaphone(true), 1); 
        
        

        // create a blocker (blocking strategy)
//		StandardRecordBlocker<Club, Attribute> blocker = new StandardRecordBlocker<Club, Attribute>(new MovieBlockingKeyByDecadeGenerator());
        NoBlocker<Club, Attribute> blocker = new NoBlocker<>();
//		SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByDecadeGenerator(), 1);

        // Initialize Matching Engine
        MatchingEngine<Club, Attribute> engine = new MatchingEngine<>();

        // Execute the matching
        Processable<Correspondence<Club, Attribute>> correspondences = engine.runIdentityResolution(
                datadbpedia, dataeuro2016, null, matchingRule,
                blocker);

        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/dbpedia_euro2016_correspondences.csv"), correspondences);

        // load the gold standard (test set)
        MatchingGoldStandard gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File(
                "data/goldstandard/completeGoldstandard/gs_dbpedia_2_euro2016_clubs.csv"));

        // evaluate your result
        MatchingEvaluator<Club, Attribute> evaluator = new MatchingEvaluator<Club, Attribute>(true);
        Performance perfTest = evaluator.evaluateMatching(correspondences.get(),
                gsTest);
        new ErrorAnalysisClubs().printFalsePositives(correspondences, gsTest);
        new ErrorAnalysisClubs().printFalseNegatives(datadbpedia, dataeuro2016, correspondences, gsTest);
        // print the evaluation result
        System.out.println("DBPedia ↔ Euro2016");
        System.out
                .println(String.format(
                        "Precision: %.4f\nRecall: %.4f\nF1: %.4f",
                        perfTest.getPrecision(), perfTest.getRecall(),
                        perfTest.getF1()));
    }
}
