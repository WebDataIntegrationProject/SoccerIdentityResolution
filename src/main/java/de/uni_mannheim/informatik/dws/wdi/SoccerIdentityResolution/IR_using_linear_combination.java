package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution;

import java.io.File;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.ClubXMLReader;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.ClubNameComparatorLevenshtein;
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

public class IR_using_linear_combination 
{
    public static void main( String[] args ) throws Exception
    {
    	// loading data
		HashedDataSet<Club, Attribute> dataDbpedia = new HashedDataSet<>();
		new ClubXMLReader().loadFromXML(new File("data/input/dbpedia.xml"), "/clubs/club", dataDbpedia);
		HashedDataSet<Club, Attribute> dataJokecampOthers = new HashedDataSet<>();
		new ClubXMLReader().loadFromXML(new File("data/input/jokecamp-others.xml"), "/clubs/club", dataJokecampOthers);
		
		System.out.println("Sample from dbpedia: " + dataDbpedia.getRandomRecord());
		System.out.println("Sample from jokecamp others: " + dataJokecampOthers.getRandomRecord());
		
		// create a matching rule
		LinearCombinationMatchingRule<Club, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
				0.7);
		// add comparators
		// matchingRule.addComparator(new MovieDateComparator10Years(), 0.5);
		matchingRule.addComparator(new ClubNameComparatorLevenshtein(), 1);
		
		// create a blocker (blocking strategy)
//		StandardRecordBlocker<Club, Attribute> blocker = new StandardRecordBlocker<Club, Attribute>(new MovieBlockingKeyByDecadeGenerator());
		NoBlocker<Club, Attribute> blocker = new NoBlocker<>();
//		SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByDecadeGenerator(), 1);
		
		// Initialize Matching Engine
		MatchingEngine<Club, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		Processable<Correspondence<Club, Attribute>> correspondences = engine.runIdentityResolution(
				dataDbpedia, dataJokecampOthers, null, matchingRule,
				blocker);

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/dbpedia_2_jokecamp_others_correspondences.csv"), correspondences);

		// load the gold standard (test set)
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File(
				"data/goldstandard/gs_dbpedia_2_jokecamp_others.csv"));

		// evaluate your result
		MatchingEvaluator<Club, Attribute> evaluator = new MatchingEvaluator<Club, Attribute>(true);
		Performance perfTest = evaluator.evaluateMatching(correspondences.get(),
				gsTest);
		new ErrorAnalysisClubs().printFalsePositives(correspondences, gsTest);
		new ErrorAnalysisClubs().printFalseNegatives(dataDbpedia, dataJokecampOthers, correspondences, gsTest);
		// print the evaluation result
		System.out.println("Dbpedia <-> Jokecamp others");
		System.out
				.println(String.format(
						"Precision: %.4f\nRecall: %.4f\nF1: %.4f",
						perfTest.getPrecision(), perfTest.getRecall(),
						perfTest.getF1()));
    }
}
