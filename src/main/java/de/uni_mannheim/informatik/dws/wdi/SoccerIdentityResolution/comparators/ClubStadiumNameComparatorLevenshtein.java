package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;

public class ClubStadiumNameComparatorLevenshtein implements Comparator<Club, Attribute> {

    private static final long serialVersionUID = 1L;
    private LevenshteinSimilarity sim = new LevenshteinSimilarity();
    private boolean useStringSimplifier = false;

    public ClubStadiumNameComparatorLevenshtein(boolean useStringSimplifier){
        this.useStringSimplifier = useStringSimplifier;
    }

    @Override
    public double compare(Club record1, Club record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {

        if(record1.getNameOfStadium() == null || record2.getNameOfStadium() == null){
            return 0.0;
        }

        String stadium1 = record1.getNameOfStadium();
        String stadium2 = record2.getNameOfStadium();

        if(useStringSimplifier){
            stadium1 = StringSimplifier.simplifyString(stadium1);
            stadium2 = StringSimplifier.simplifyString(stadium2);
        }

        return sim.calculate(stadium1, stadium2);

    }

}
