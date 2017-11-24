package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import org.apache.commons.codec.language.DoubleMetaphone;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;

public class PlayerNameComparatorDoubleMetaphone implements Comparator<Player, Attribute> {

    private static final long serialVersionUID = 1L;
    private DoubleMetaphone sim = new DoubleMetaphone();
    private boolean simplifyString;

    public PlayerNameComparatorDoubleMetaphone(boolean simplifyString){
        this.simplifyString = simplifyString;
    }

    public PlayerNameComparatorDoubleMetaphone(){
        this.simplifyString = false;
    }

    @Override
    public double compare(Player record1, Player record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {

    	double similarity = 0.0;
    	
        if(record1.getFullName() == null || record2.getFullName() == null){
            return 0.0;
        }

        String name1 = record1.getFullName();
        String name2 = record2.getFullName();
        if(simplifyString){
        	name1 = StringSimplifier.simplifyString(name1);
        	name2 = StringSimplifier.simplifyString(name2);
        } 
        
        if(sim.doubleMetaphone(name1).equals(sim.doubleMetaphone(name2))){
        	similarity = 1.0;
        }
        
        return similarity;
    }
}
