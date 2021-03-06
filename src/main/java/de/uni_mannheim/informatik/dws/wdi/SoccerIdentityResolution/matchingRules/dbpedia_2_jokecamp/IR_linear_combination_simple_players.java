package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.matchingRules.dbpedia_2_jokecamp;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.ErrorAnalysisPlayers;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.FeaturesToCSV;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.blockers.PlayerBlockerByFirstLettersOfName;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.PlayerBirthDateComparatorExactDateComparison;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.PlayerNameComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.PlayerNameComparatorMongeElkan;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.PlayerXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
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
 * Learning Combination Rules for Players
 */
public class IR_linear_combination_simple_players
{

    public static void main( String[] args ) throws Exception
    {

        // loading data
        HashedDataSet<Player, Attribute> dataDbpedia = new HashedDataSet<>();
        new PlayerXMLReader().loadFromXML(new File("data/input/dbpedia.xml"), "/clubs/club/players/player", dataDbpedia);
        HashedDataSet<Player, Attribute> dataJokecamp = new HashedDataSet<>();
        new PlayerXMLReader().loadFromXML(new File("data/input/jokecamp.xml"), "/clubs/club/players/player", dataJokecamp);

        System.out.println("Sample from dbpedia: " + dataDbpedia.getRandomRecord());
        System.out.println("Sample from jokecamp: " + dataJokecamp.getRandomRecord());

        // create a matching rule
        LinearCombinationMatchingRule<Player, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
                0.9);
        
        // F1: 0.9057 [P:0.8421, R:0.9796]

        // add comparators
        // matchingRule.addComparator(new MovieDateComparator10Years(), 0.5);
        //matchingRule.addComparator(new PlayerNameComparatorLevenshtein(false), 1);
        //matchingRule.addComparator(new PlayerBirthDateComparatorLevenshtein(), 0.5);
        //matchingRule.addComparator(new PlayerBirthDateComparatorExactDateComparison(), 0.2);
        matchingRule.addComparator(new PlayerNameComparatorMongeElkan(true, "doubleMetaphone", true), 1.0);
        
        
        // create a blocker (blocking strategy)
        StandardRecordBlocker<Player, Attribute> blocker = new StandardRecordBlocker<Player, Attribute>(new PlayerBlockerByFirstLettersOfName(4));
//        StandardRecordBlocker<Player, Attribute> blocker = new StandardRecordBlocker<Player, Attribute>(new PlayerBlockerByBirthYear());

//        NoBlocker<Player, Attribute> blocker = new NoBlocker<>();
//		SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByDecadeGenerator(), 1);


        // Initialize Matching Engine
        MatchingEngine<Player, Attribute> engine = new MatchingEngine<>();

        // Execute the matching
        Processable<Correspondence<Player, Attribute>> correspondences = engine.runIdentityResolution(
                dataDbpedia, dataJokecamp, null, matchingRule,
                blocker);

        // write the correspondences to the output file
        //new CSVCorrespondenceFormatter().writeCSV(new File("data/output/basic_dbpedia_2_jokecamp_correspondences_players.csv"), correspondences);

        // load the gold standard (test set)
        MatchingGoldStandard gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File(
                "data/goldstandard/completeGoldstandard/gs_dbpedia_2_jokecamp_players.csv"));

        // evaluate your result
        MatchingEvaluator<Player, Attribute> evaluator = new MatchingEvaluator<Player, Attribute>(true);
        Performance perfTest = evaluator.evaluateMatching(correspondences.get(),
                gsTest);
        new ErrorAnalysisPlayers().printFalsePositives(correspondences, gsTest);
        new ErrorAnalysisPlayers().printFalseNegatives(dataDbpedia, dataJokecamp, correspondences, gsTest);
        // print the evaluation result
        System.out.println("Dbpedia ↔ Jokecamp");
        System.out
                .println(String.format(
                        "Precision: %.4f\nRecall: %.4f\nF1: %.4f",
                        perfTest.getPrecision(), perfTest.getRecall(),
                        perfTest.getF1()));

    }


}
