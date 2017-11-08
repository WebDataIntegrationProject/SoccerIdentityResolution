package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.blockers;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;


public class PlayerBlockerByFirstLettersOfName extends
        RecordBlockingKeyGenerator<Player, Attribute> {
    @Override
    public void generateBlockingKeys(Player record, Processable<Correspondence<Attribute, Matchable>> correspondences, DataIterator<Pair<String, Player>> resultCollector) {

        // change the offset only here
        int numberOfCharactersForOffset = 3;

        if(record.getFullName() != null){
            if(record.getFullName().length() >= numberOfCharactersForOffset){
                resultCollector.next(new Pair<>(record.getFullName().toLowerCase().substring(0,numberOfCharactersForOffset), record));
            }
        }

    }

}
