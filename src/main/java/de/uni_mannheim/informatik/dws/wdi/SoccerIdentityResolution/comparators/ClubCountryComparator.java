package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class ClubCountryComparator implements Comparator<Club, Attribute> {

    private static final long serialVersionUID = 1L;
    
    public ClubCountryComparator() { super(); }


    @Override
    public double compare(Club record1, Club record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {

        if(record1.getCountry() == null || record2.getCountry() == null){
            return 0.0;
        }

        else if(record1.getCountry().equals(record2.getCountry())){
        	return 1.0;
        }

        else{
        	return 0.0;
        }
        

    }
}
