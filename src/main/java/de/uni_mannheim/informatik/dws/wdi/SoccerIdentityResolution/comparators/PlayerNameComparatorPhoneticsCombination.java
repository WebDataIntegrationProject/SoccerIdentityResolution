package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class PlayerNameComparatorPhoneticsCombination implements Comparator<Player, Attribute> {

    private static final long serialVersionUID = 1L;
    private SoundexSimilarity sim_Soundex = new SoundexSimilarity();
    private ColognePhoneticsSimilarity sim_ColognePhonetic = new ColognePhoneticsSimilarity();

    
    private boolean simplifyString;

    public PlayerNameComparatorPhoneticsCombination(boolean simplifyString){
        this.simplifyString = simplifyString;
    }

    public PlayerNameComparatorPhoneticsCombination(){
        this.simplifyString = false;
    }

    @Override
    public double compare(Player record1, Player record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {

    	double result;
    	
        if(record1.getFullName() == null || record2.getFullName() == null){
            return 0.0;
        }

        String name1, name2;
        if(simplifyString){
            name1 = StringSimplifier.simplifyString(record1.getFullName());
            name2 = StringSimplifier.simplifyString(record2.getFullName());
        } else {
            name1 = record1.getFullName();
            name2 = record2.getFullName();
        }
        
        if((record1.getNationality() != null && record1.getNationality().equals("DEU")) || (record2.getNationality() != null && record2.getNationality().equals("DEU"))){
        	result = sim_ColognePhonetic.calculate(name1, name2);
        }
        else{
        	result = sim_Soundex.calculate(name1, name2);
        }
        
        return result;

    }
}
