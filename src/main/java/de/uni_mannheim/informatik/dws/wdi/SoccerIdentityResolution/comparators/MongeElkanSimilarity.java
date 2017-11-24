package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.apache.commons.text.similarity.JaccardSimilarity;
import org.apache.commons.text.similarity.JaroWinklerDistance;

import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;

public class MongeElkanSimilarity {
    private JaccardSimilarity simJaccard = new JaccardSimilarity();
    private JaroWinklerDistance simJaroWinkler = new JaroWinklerDistance();
	private LevenshteinSimilarity simLevenshtein = new LevenshteinSimilarity();
	private SoundexSimilarity simSoundex = new SoundexSimilarity();
	
	public double calculate(String name1, String name2, String similarityMeasure){
		String[] partsOfName1 = name1.split(" ");
		String[] partsOfName2 = name2.split(" ");
		Hashtable<String, Double> table = new Hashtable<String, Double>();

		// compute similarity for each possible pair and store it in the hashtable
		for(int i=0; i<partsOfName1.length; i++){
			for(int j=0; j<partsOfName2.length; j++){
				switch(similarityMeasure){
					case "jaccard": table.put(i + "-" + j, simJaccard.apply(partsOfName1[i], partsOfName2[j])); break;
					case "jaroWinkler": table.put(i + "-" + j, simJaroWinkler.apply(partsOfName1[i], partsOfName2[j])); break;
					case "levenshtein": table.put(i + "-" + j, simLevenshtein.calculate(partsOfName1[i], partsOfName2[j])); break;
					case "soundex": table.put(i + "-" + j, simSoundex.calculate(partsOfName1[i], partsOfName2[j])); 
				}				
			}
		}
		
		// start searching for all max values
		LinkedList<Double> allMaxValues= new LinkedList<Double>();
		while(!table.isEmpty()){
			double maxValue = 0.0;
			String maxKey = "";
			
			// search for current max value
			for(Entry<String, Double> entry : table.entrySet()) {
			     if(entry.getValue() >= maxValue) {
			         maxValue = entry.getValue();
			         maxKey = entry.getKey();
			     }
			}
			allMaxValues.add(maxValue);
			
			String[] currentKeyParts = maxKey.split("-");
			LinkedList<String> removals = new LinkedList<String>();
			
			// collect keys of pairs containing one of the word of the current pair from the hashtable
			for(Entry<String, Double> entry : table.entrySet()) {
			     if(entry.getKey().contains(currentKeyParts[0] + "-") || entry.getKey().contains("-" + currentKeyParts[1])) {  
			         removals.add(entry.getKey());
			     }
			}
			
			// remove the collected keys of pairs containing one of the word of the current pair from the hashtable
			for(int i=0; i<removals.size(); i++){
		        System.out.println(removals.get(i));
				table.remove(removals.get(i));
				if(i== removals.size() -1 ){
					System.out.println("---------");
				}
			}

			
		}
		
		double similarity = 0.0;
		
		for(int i=0; i<allMaxValues.size(); i++){
			System.out.println(i + ": " + allMaxValues.get(i));
			similarity = similarity + allMaxValues.get(i);
		}
		
		similarity = similarity/allMaxValues.size();
		
		return similarity;
	}
}
