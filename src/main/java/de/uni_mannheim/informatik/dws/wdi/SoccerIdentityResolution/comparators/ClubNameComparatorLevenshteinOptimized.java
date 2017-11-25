package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;

public class ClubNameComparatorLevenshteinOptimized implements Comparator<Club, Attribute> {

    private static final long serialVersionUID = 1L;
    LevenshteinSimilarity sim = new LevenshteinSimilarity();
    boolean useStringSimplifier = false;

    public ClubNameComparatorLevenshteinOptimized(boolean useStringSimplifier) {
        super();
        this.useStringSimplifier = useStringSimplifier;
    }

    public ClubNameComparatorLevenshteinOptimized() { super(); }


    @Override
    public double compare(Club record1, Club record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {

        if(record1.getName() == null || record2.getName() == null){
            return 0.0;
        }

        String string1 = record1.getName();
        String string2 = record2.getName();

        if(useStringSimplifier){
            string1 = StringSimplifier.simplifyStringClubOptimized(string1);
            string2 = StringSimplifier.simplifyStringClubOptimized(string2);
        }

        return sim.calculate(string1, string2);

    }
}
