package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.blockers;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class PlayerBlockerByBirthYearAndMonth extends
        RecordBlockingKeyGenerator<Player, Attribute> {



    @Override
    public void generateBlockingKeys(Player record, Processable<Correspondence<Attribute, Matchable>> correspondences, DataIterator<Pair<String, Player>> resultCollector) {

        if(record.getBirthDate() != null) {
            resultCollector.next(new Pair<>(record.getBirthDate().toString().substring(0, 7), record));
        }


    }
}
