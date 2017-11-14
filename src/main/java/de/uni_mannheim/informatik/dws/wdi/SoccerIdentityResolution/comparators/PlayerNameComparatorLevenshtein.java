package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;

public class PlayerNameComparatorLevenshtein implements Comparator<Player, Attribute> {

    private static final long serialVersionUID = 1L;
    private LevenshteinSimilarity sim = new LevenshteinSimilarity();
    private boolean simplifyString;

    public PlayerNameComparatorLevenshtein(boolean simplifyString){
        this.simplifyString = simplifyString;
    }

    public PlayerNameComparatorLevenshtein(){
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
        } else {
            return sim.calculate(record1.getFullName(), record2.getFullName());
        }

        return sim.calculate(name1simplified, name2simplified);


    }
}
