package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import java.lang.*;

public class PlayerWeightComparator implements Comparator<Player, Attribute> {

    private static final long serialVersionUID = 1L;
    private int dmax;
    
    public PlayerWeightComparator (){
    	this.dmax = 0;
    }
    
    public PlayerWeightComparator (int threshold){
    	this.dmax = threshold;
    }
    
    @Override
    public double compare(Player record1, Player record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {

    	int sim = 0;
    	
        if(record1.getWeight() == null || record2.getWeight() == null){
            return 0.0;
        }
        
        
        if(Math.abs(record1.getWeight() - record2.getWeight()) <= dmax){
        	sim = 1 - (Math.abs(record1.getWeight() - record2.getWeight())/dmax);
        }
       
        return sim;

    }


}
