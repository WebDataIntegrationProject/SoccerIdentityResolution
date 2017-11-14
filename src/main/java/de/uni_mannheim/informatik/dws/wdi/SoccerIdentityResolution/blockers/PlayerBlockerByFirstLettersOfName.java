package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.blockers;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;


/**
 * This blocker uses the first x letters of the name field for the player.
 */
public class PlayerBlockerByFirstLettersOfName extends
        RecordBlockingKeyGenerator<Player, Attribute> {

    private int numberOfLetters;

    public PlayerBlockerByFirstLettersOfName(int numberOfLetters){
        this.numberOfLetters = numberOfLetters;
    }

    public PlayerBlockerByFirstLettersOfName(){
        // use the standard value of 3
        this.numberOfLetters = 3;
    }

    @Override
    public void generateBlockingKeys(Player record, Processable<Correspondence<Attribute, Matchable>> correspondences, DataIterator<Pair<String, Player>> resultCollector) {


        if(record.getFullName() != null){
            if(record.getFullName().length() >= numberOfLetters){
                resultCollector.next(new Pair<>(record.getFullName().toLowerCase().substring(0,numberOfLetters), record));
            }
        }

    }

}
