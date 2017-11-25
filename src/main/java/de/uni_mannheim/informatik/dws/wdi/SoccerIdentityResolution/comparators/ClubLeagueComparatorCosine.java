package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import org.apache.commons.text.similarity.CosineDistance;
import org.apache.commons.text.similarity.JaccardSimilarity;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class ClubLeagueComparatorCosine implements Comparator<Club, Attribute> {

    private static final long serialVersionUID = 1L;
    private CosineDistance sim = new CosineDistance();
    boolean useStringSimplifier = false;

    public ClubLeagueComparatorCosine(boolean lowerCase) {
        super();
        useStringSimplifier = true;
    }

    public ClubLeagueComparatorCosine() { super(); }


    @Override
    public double compare(Club record1, Club record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {

        if(record1.getLeague() == null || record2.getLeague() == null){
            return 0.0;
        }

        String string1 = record1.getLeague();
        String string2 = record2.getLeague();

        if(useStringSimplifier){
            string1 = StringSimplifier.simplifyString(string1);
            string2 = StringSimplifier.simplifyString(string2);
        }

        return 1 - sim.apply(string1, string2);

    }
}
