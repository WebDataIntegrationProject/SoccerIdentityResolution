package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;

public class PlayerClubNameComparatorLevenshtein implements Comparator<Player, Attribute> {

    private static final long serialVersionUID = 1L;
    LevenshteinSimilarity sim = new LevenshteinSimilarity();
    boolean useStringSimplifier = false;

    public PlayerClubNameComparatorLevenshtein(boolean useStringSimplifier) {
        super();
        this.useStringSimplifier = useStringSimplifier;
    }

    public PlayerClubNameComparatorLevenshtein() { super(); }


    @Override
    public double compare(Player record1, Player record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {

        if(record1.getClubName() == null || record2.getClubName() == null){
            return 0.0;
        }

        String string1 = record1.getClubName();
        String string2 = record2.getClubName();

        if(useStringSimplifier){
            string1 = StringSimplifier.simplifyString(string1);
            string2 = StringSimplifier.simplifyString(string2);
        }

        return sim.calculate(string1, string2);

    }
}
