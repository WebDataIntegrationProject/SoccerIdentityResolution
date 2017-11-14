package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;

import java.util.Iterator;
import java.util.List;

public class ClubPlayerNameComparator implements Comparator<Club, Attribute> {

    private static final long serialVersionUID = 1L;
    LevenshteinSimilarity sim = new LevenshteinSimilarity();


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
        String player1name, player2name;
        int numberOfMatches = 0;

        while(iterator1.hasNext()){

            player1 = (Player) iterator1.next();
            player1name = StringSimplifier.simplifyString(player1.getFullName());

            // re-initialize iterator 2
            iterator2 = record2playerList.iterator();

            club2loop: while(iterator2.hasNext()){
                player2 = (Player) iterator2.next();
                player2name = StringSimplifier.simplifyString(player2.getFullName());

                if(player1name.equals(player2name)){
                    numberOfMatches++;
                    System.out.println("Player match! " + player1.getFullName() + " and " + player2.getFullName());
                    break club2loop;
                }
            }
        }

        if(numberOfMatches > 0){
            System.out.println(record1.getName() + " and " + record2.getName() + " have " + numberOfMatches + " players in common.");
        }

        // return the match ratio normalized with the smaller team size
        return (double) numberOfMatches / (double) Math.min(record1playerList.size(), record2playerList.size());

    }
}