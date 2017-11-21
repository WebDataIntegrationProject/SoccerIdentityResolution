package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import java.lang.*;

public class PlayerHeightComparator implements Comparator<Player, Attribute> {

    private static final long serialVersionUID = 1L;
    private int dmax;
    
    public PlayerHeightComparator(){
    	this.dmax = 0;
    }

    public PlayerHeightComparator(int threshold){
    	this.dmax = threshold;
    }
    
    @Override
    public double compare(Player record1, Player record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {

    	int sim = 0;
    	
        if(record1.getHeight() == null || record2.getHeight() == null){
            return 0.0;
        }
        
        
        if(Math.abs(record1.getHeight() - record2.getHeight()) <= dmax){
        	sim = 1 - (Math.abs(record1.getHeight() - record2.getHeight())/dmax);
        }
       
        return sim;

    }


}

