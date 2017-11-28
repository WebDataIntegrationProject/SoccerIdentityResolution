package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class ClubNameComparatorMongeElkan implements Comparator<Club, Attribute> {

    private static final long serialVersionUID = 1L;
    private MongeElkanSimilarity sim = new MongeElkanSimilarity();
    boolean useStringSimplifier = false;
    private String similarityMeasure;
    private boolean punishment = false;

    public ClubNameComparatorMongeElkan(boolean lowerCase, String similarityMeasure, boolean punishment) {
        super();
        useStringSimplifier = true;
        this.similarityMeasure = similarityMeasure;
        this.punishment = punishment;
    }
    
    public ClubNameComparatorMongeElkan(boolean lowerCase, String similarityMeasure) {
        super();
        useStringSimplifier = true;
        this.similarityMeasure = similarityMeasure;
    }

    public ClubNameComparatorMongeElkan() { super(); }


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

        if(AlternativeClubChecker.checkForAlternativeClub(string1) != AlternativeClubChecker.checkForAlternativeClub(string2)){
        	return 0.0;
        }
        
        else if(punishment){
        	return sim.calculateWithPunishment(string1, string2, similarityMeasure);
        }
        else{
        	return sim.calculate(string1, string2, similarityMeasure);
        }
        

    }
    
    public static void main (String[] args){
        MongeElkanSimilarity sim = new MongeElkanSimilarity();
        String similarityMeasure = "levenshtein";

    	double similarity;
    	
    	String string1 = "KAA Gent";
    	String string2 = "KAA Gent I";
    	
    	
    	string1 = StringSimplifier.simplifyStringClubOptimized(string1);
        string2 = StringSimplifier.simplifyStringClubOptimized(string2);
        
        System.out.println(string1);
        System.out.println(string2);
        
        System.out.println(AlternativeClubChecker.checkForAlternativeClub(string1));
        System.out.println(AlternativeClubChecker.checkForAlternativeClub(string2));
        System.out.println(AlternativeClubChecker.checkForAlternativeClub(string1) != AlternativeClubChecker.checkForAlternativeClub(string2));
        
        if(AlternativeClubChecker.checkForAlternativeClub(string1) != AlternativeClubChecker.checkForAlternativeClub(string2)){
        	similarity = 0.0;
        }
        
        else if(true){
        	similarity =  sim.calculateWithPunishment(string1, string2, similarityMeasure);
        }
        else{
        	similarity =  sim.calculate(string1, string2, similarityMeasure);
        }
        
        System.out.println(similarity);
    }
}
