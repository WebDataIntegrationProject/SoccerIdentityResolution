package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import org.apache.commons.text.similarity.JaccardSimilarity;
import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;


// used to check for clubs like "Bayern Munich II" or "Bayern Munich Jugend" compared to "Bayern Munich"
public class ClubNameComparatorAdditionalClubVersions implements Comparator<Club, Attribute> {

    private static final long serialVersionUID = 1L;
    boolean useStringSimplifier = false;

    public ClubNameComparatorAdditionalClubVersions(boolean lowerCase) {
        super();
        useStringSimplifier = true;
    }

    public ClubNameComparatorAdditionalClubVersions() { super(); }


    @Override
    public double compare(Club record1, Club record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {

    	double similarity = 0.0;
    	
        if(record1.getName() == null || record2.getName() == null){
            return 0.0;
        }

        String string1 = record1.getName();
        String string2 = record2.getName();

        if(useStringSimplifier){
            string1 = StringSimplifier.simplifyStringClubOptimized(string1);
            string2 = StringSimplifier.simplifyStringClubOptimized(string2);
        }
        
        String[] name1Parts = string1.split(" ");
        String[] name2Parts = string2.split(" ");
        
        // if the last word in the first name equals the last word in the second name then the clubs are assumed to be same
        if(name1Parts.length != name2Parts.length){
        	if(name1Parts[name1Parts.length - 1].equals(name2Parts[name2Parts.length - 1])){
        		similarity =  1.0;
        	}
        }
        return similarity;
    }
}
