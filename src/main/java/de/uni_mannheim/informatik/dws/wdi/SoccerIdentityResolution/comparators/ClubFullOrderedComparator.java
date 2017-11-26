package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import org.apache.commons.text.similarity.CosineDistance;
import org.apache.commons.text.similarity.JaccardSimilarity;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class ClubFullOrderedComparator implements Comparator<Club, Attribute> {

    private static final long serialVersionUID = 1L;
    private MongeElkanSimilarity simMongeElkan = new MongeElkanSimilarity();
    boolean useStringSimplifier = false;

    public ClubFullOrderedComparator(boolean lowerCase) {
        super();
        useStringSimplifier = true;
    }

    public ClubFullOrderedComparator() { super(); }


    @Override
    public double compare(Club record1, Club record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {

    	double similarity = 0.0;
    	
        if(record1.getName() == null || record2.getName() == null){
            return 0.0;
        }

        String name1 = record1.getName();
        String name2 = record2.getName();
        
        String league1 = record1.getLeague();
        String league2 = record2.getLeague();


        if(useStringSimplifier){
            name1 = StringSimplifier.simplifyStringClubOptimized(name1);
            name2 = StringSimplifier.simplifyStringClubOptimized(name2);
            league1 = StringSimplifier.simplifyString(league1);
            league2 = StringSimplifier.simplifyString(league2);
        }

        similarity = simMongeElkan.calculate(name1, name2, "levenshtein");
        double similarity_leagues = simMongeElkan.calculate(league1, league2, "levenshtein");
        
        // Assumption: we only have a match if all 3 conditions are fulfilled
        if(similarity > 0.8){
        	if(similarity_leagues > 0.8){
        		String[] name1Parts = name1.split(" ");
                String[] name2Parts = name2.split(" ");
        		if(name1Parts[name1Parts.length - 1].equals(name2Parts[name2Parts.length - 1])){
        			similarity = 1.0;
        		}
        	}
        }
        return similarity;

    }
}
