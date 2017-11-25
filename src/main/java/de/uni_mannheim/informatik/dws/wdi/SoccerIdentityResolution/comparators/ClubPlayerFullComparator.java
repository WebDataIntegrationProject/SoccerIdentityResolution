package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This comparator counts the number of players which two clubs have in common and normalizes that with the
 * number of players of the smaller club.
 *
 * The player correspondences outcome is used here.
 */
public class ClubPlayerFullComparator implements Comparator<Club, Attribute> {

    private static final long serialVersionUID = 1L;
    private boolean isVerbose = false;

    static HashMap<String, String> playerMapping;

    public ClubPlayerFullComparator(String playerCorrespondenceFile){

        playerMapping =new HashMap<>(1000);

        try {
            System.out.println("Loading the players correspondences.");
            BufferedReader br = new BufferedReader(new FileReader(new File(playerCorrespondenceFile)));

            String line, key, value;
            while((line = br.readLine()) != null){

                Pattern pattern = Pattern.compile("\"(.*?)\"");
                Matcher matcher = pattern.matcher(line);
                matcher.find();
                key = matcher.group(0);
                key = key.replace("\"", "");
                matcher.find();
                value = matcher.group(0);
                value = value.replace("\"", "");
                playerMapping.put(key, value);
                System.out.println(key + "," + value);

            }

        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public ClubPlayerFullComparator(String playerCorrespondenceFile, boolean isVerbose){
        this(playerCorrespondenceFile);
        this.isVerbose = isVerbose;
    }


    @Override
    public double compare(Club record1, Club record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {

        List<Player> record1playerList, record2playerList;

        if (record1.getPlayers() != null && record2.getPlayers() != null) {

            record1playerList = record1.getPlayers();
            record2playerList = record2.getPlayers();


            if(record1playerList.isEmpty() || record2playerList.isEmpty()){
                return 0.0;
            }

        } else {
            return 0.0;
        }

        Iterator iterator1 = record1playerList.iterator();
        Iterator iterator2;

        Player player1, player2;
        int numberOfMatches = 0;

        while(iterator1.hasNext()){

            player1 = (Player) iterator1.next();

            // re-initialize iterator 2
            iterator2 = record2playerList.iterator();

            club2loop: while(iterator2.hasNext()){
                player2 = (Player) iterator2.next();

                if(
                        playerMapping.containsKey(player1.getIdentifier())
                        && ((String) playerMapping.get(player1.getIdentifier())).equals(player2.getIdentifier())
                        || playerMapping.containsKey(player2.getIdentifier())
                        && ((String) playerMapping.get(player2.getIdentifier())).equals(player1.getIdentifier())
                        )
                {

                    numberOfMatches++;
                    if (isVerbose){ System.out.println("Player match! " + player1.getFullName() + " and " + player2.getFullName()); }
                    break club2loop;
                }
            }
        }

        if(isVerbose) {
            if (numberOfMatches > 0) {
                System.out.println(record1.getName() + " and " + record2.getName() + " have " + numberOfMatches + " players in common.");
            }
        }

        // return the match ratio normalized with the smaller team size
        return (double) numberOfMatches / (double) Math.min(record1playerList.size(), record2playerList.size());

    }
}