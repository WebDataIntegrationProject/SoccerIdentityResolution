package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import java.lang.*;

public class PlayerNationalityComparator implements Comparator<Player, Attribute> {

    private static final long serialVersionUID = 1L;

    @Override
    public double compare(Player record1, Player record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {
    	
        if(record1.getNationality() == null || record2.getNationality() == null){
            return 0.0;
        } 
        
        if(record1.getNationality().equals(record2.getNationality())) {
        	return 1;
        } 
        
        else {
        	return 0; 
        }
       
    }

}
