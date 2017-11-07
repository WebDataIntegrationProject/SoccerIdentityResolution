package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.blockers;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class ClubBlockerByLeague extends
        RecordBlockingKeyGenerator<Club, Attribute> {

    @Override
    public void generateBlockingKeys(Club record, Processable<Correspondence<Attribute, Matchable>> correspondences, DataIterator<Pair<String, Club>> resultCollector) {

        if(record.getLeague() != null) {
            resultCollector.next(new Pair<>(record.getLeague(), record));
        }


    }
}
