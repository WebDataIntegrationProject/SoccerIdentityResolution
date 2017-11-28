package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.matchingRules.dbpedia_2_jokecamp;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.ErrorAnalysisPlayers;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.blockers.PlayerBlockerByBirthYear;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.blockers.PlayerBlockerByFirstLettersOfName;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators.*;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.PlayerXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
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
 * Learning Combination Rules for Players
 */
public class IR_weka_players_dbpedia_jokecamp
{

    static boolean WRITE_FEATURE_SET_FOR_EXTERNAL_TOOL = false;

    public static void main( String[] args ) throws Exception
    {

        // loading data
        HashedDataSet<Player, Attribute> dataDbpedia = new HashedDataSet<>();
        new PlayerXMLReader().loadFromXML(new File("data/input/dbpedia.xml"), "/clubs/club/players/player", dataDbpedia);
        HashedDataSet<Player, Attribute> dataJokecamp = new HashedDataSet<>();
        new PlayerXMLReader().loadFromXML(new File("data/input/jokecamp.xml"), "/clubs/club/players/player", dataJokecamp);

        System.out.println("Sample from dbpedia: " + dataDbpedia.getRandomRecord());
        System.out.println("Sample from jokecamp: " + dataJokecamp.getRandomRecord());

        String options[] = new String[] {""};
        String modelType = "SimpleLogistic"; // using a logistic regression
        WekaMatchingRule<Player, Attribute> matchingRule = new WekaMatchingRule<>(0.9, modelType, options);

        // add comparators
        matchingRule.addComparator(new PlayerClubNameComparatorLevenshtein(true));
        matchingRule.addComparator(new PlayerNameComparatorLevenshtein(true));
        matchingRule.addComparator(new PlayerNameComparatorJaroWinkler(true));
        matchingRule.addComparator(new PlayerNameComparatorMongeElkan(true, "doubleMetaphone", true));
        matchingRule.addComparator(new PlayerHeightComparator());
        matchingRule.addComparator(new PlayerPositionComparator());


        // create a blocker (blocking strategy)
        StandardRecordBlocker<Player, Attribute> blocker = new StandardRecordBlocker<Player, Attribute>(new PlayerBlockerByFirstLettersOfName(2));


        // load the gold standard (test set)
        MatchingGoldStandard goldStandardForTraining = new MatchingGoldStandard();
        goldStandardForTraining.loadFromCSVFile(new File("data/goldstandard/gs_dbpedia_2_jokecamp_players_97.csv"));

        // train the matching rule's model
        RuleLearner<Player, Attribute> learner = new RuleLearner<>();
        learner.learnMatchingRule(dataDbpedia, dataJokecamp, null, matchingRule, goldStandardForTraining);

        // Initialize Matching Engine
        MatchingEngine<Player, Attribute> engine = new MatchingEngine<>();

        // Execute the matching
        Processable<Correspondence<Player, Attribute>> correspondences = engine.runIdentityResolution(
                dataDbpedia, dataJokecamp, null, matchingRule,
                blocker);

        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/dbpedia_2_jokecamp_correspondences_players.csv"), correspondences);


        // gold standard for evaluation
        MatchingGoldStandard goldStandardForEvaluation = new MatchingGoldStandard();
        goldStandardForEvaluation.loadFromCSVFile(new File("data/goldstandard/gs_dbpedia_2_jokecamp_players_53.csv"));



        // evaluate your result
        MatchingEvaluator<Player, Attribute> evaluator = new MatchingEvaluator<Player, Attribute>(true);
        Performance perfTest = evaluator.evaluateMatching(correspondences.get(),
                goldStandardForEvaluation);
        new ErrorAnalysisPlayers().printFalsePositives(correspondences, goldStandardForEvaluation);
        new ErrorAnalysisPlayers().printFalseNegatives(dataDbpedia, dataJokecamp, correspondences, goldStandardForEvaluation);
        // print the evaluation result
        System.out.println("Dbpedia ↔ Kaggle");
        System.out
                .println(String.format(
                        "Precision: %.4f\nRecall: %.4f\nF1: %.4f",
                        perfTest.getPrecision(), perfTest.getRecall(),
                        perfTest.getF1()));


    }

}
