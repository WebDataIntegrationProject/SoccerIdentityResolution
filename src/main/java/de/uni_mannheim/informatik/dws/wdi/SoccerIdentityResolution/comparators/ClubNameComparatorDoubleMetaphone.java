package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import org.apache.commons.codec.language.DoubleMetaphone;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class ClubNameComparatorDoubleMetaphone implements Comparator<Club, Attribute> {

    private static final long serialVersionUID = 1L;
    private DoubleMetaphone sim = new DoubleMetaphone();
    boolean useStringSimplifier = false;

    public ClubNameComparatorDoubleMetaphone(boolean lowerCase) {
        super();
        useStringSimplifier = true;
    }

    public ClubNameComparatorDoubleMetaphone() { super(); }


    @Override
    public double compare(Club record1, Club record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {

    	double similarity = 0.0;
    	
        if(record1.getName() == null || record2.getName() == null){
            return 0.0;
        }

        String string1 = record1.getName();
        String string2 = record2.getName();

        if(useStringSimplifier){
            string1 = StringSimplifier.simplifyStringClubOptimized(string1);
            string2 = StringSimplifier.simplifyStringClubOptimized(string2);
        }

        string1 = sim.doubleMetaphone(string1);
        string2 = sim.doubleMetaphone(string2);
        
        if(string1.equals(string2)){
        	similarity = 1.0;
        }
        
        return similarity;

    }
}
