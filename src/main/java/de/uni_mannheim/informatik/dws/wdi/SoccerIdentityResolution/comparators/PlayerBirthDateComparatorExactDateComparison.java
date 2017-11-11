package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import java.lang.*;

public class PlayerBirthDateComparatorExactDateComparison implements Comparator<Player, Attribute> {

    private static final long serialVersionUID = 1L;

    @Override
    public double compare(Player record1, Player record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {

    	double sim = 0.0;
    	
        if(record1.getBirthDate() == null || record2.getBirthDate() == null){
            return 0.0;
        }

        if(record1.getBirthDate().equals(record2.getBirthDate())){
        	sim = 1.0;
        }

        return sim;

    }


}
