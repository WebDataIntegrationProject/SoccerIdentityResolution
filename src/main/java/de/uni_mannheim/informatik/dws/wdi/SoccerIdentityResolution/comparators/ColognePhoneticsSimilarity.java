package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import org.apache.commons.codec.language.ColognePhonetic;

public class ColognePhoneticsSimilarity {

    private ColognePhonetic colognePhoneticCreator = new ColognePhonetic();

	
	public String createCode(String word){
		
		return colognePhoneticCreator.encode(word);
	}
	
	public double calculate(String name1, String name2){
		String code1 = createCode(name1);
		String code2 = createCode(name2);

		if(code1.equals(code2)){
			return 1.0;
		}
		else{
			return 0.0;
		}
	}
	
}
