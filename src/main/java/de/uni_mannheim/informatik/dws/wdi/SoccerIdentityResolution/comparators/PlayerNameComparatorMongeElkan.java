package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class PlayerNameComparatorMongeElkan implements Comparator<Player, Attribute> {

    private static final long serialVersionUID = 1L;
    private MongeElkanSimilarity sim = new MongeElkanSimilarity();
    private boolean simplifyString;
    private String similarityMeasure;
    private boolean punishment = false;

    public PlayerNameComparatorMongeElkan(boolean simplifyString, String similarityMeasure){
        this.simplifyString = simplifyString;
        this.similarityMeasure = similarityMeasure;
    }
    
    public PlayerNameComparatorMongeElkan(boolean simplifyString, String similarityMeasure, boolean punishment){
        this.simplifyString = simplifyString;
        this.similarityMeasure = similarityMeasure;
        this.punishment = punishment;
    }
    

    public PlayerNameComparatorMongeElkan(){
        this.simplifyString = false;
    }

    @Override
    public double compare(Player record1, Player record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {

        if(record1.getFullName() == null || record2.getFullName() == null){
            return 0.0;
        }

        String name1simplified, name2simplified;
        if(simplifyString){
            name1simplified = StringSimplifier.simplifyString(record1.getFullName());
            name2simplified = StringSimplifier.simplifyString(record2.getFullName());
            if(punishment){
            	return sim.calculateWithPunishment(name1simplified, name2simplified, similarityMeasure);
            }
            else{
            	return sim.calculate(name1simplified, name2simplified, similarityMeasure);
            }
        } 
        else {
        	if (punishment){
        		return sim.calculateWithPunishment(record1.getFullName(), record2.getFullName(), similarityMeasure);
        	}
        	else{
        		return sim.calculate(record1.getFullName(), record2.getFullName(), similarityMeasure);
        	}
        }

    }
    
}
