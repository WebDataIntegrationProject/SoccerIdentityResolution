package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// DEPRECATED - DOES NOT WORK
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

public class StableMarriage {

	public static void removeDuplicateMatches(String correspondenceFile){
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(correspondenceFile)));
			String line;
			 File fout = new File("data/output/club_correspondences_CLEANED_ADVANCED.csv");
			 FileOutputStream fos = new FileOutputStream(fout);
			 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			 ArrayList<String> alreadyAddedCorrespondences = new ArrayList<String>();
			 
			 while((line = br.readLine()) != null){
				 // only check correspondence if it hasn't already been added
				 if(!alreadyAddedCorrespondences.contains(line)){
					 String[] parts = line.split(",");
					 // check if there is a best correspondence for the first id and if yes add (and write) it if it isn't already added
					 String bestMatch_firstId = getBestMatch(parts[0], 0, correspondenceFile);
			         if(bestMatch_firstId != null){
			        	 if(!alreadyAddedCorrespondences.contains(bestMatch_firstId)){
			        		bw.write(line);
			             	bw.newLine();
			        	 }
			         }
			         // check if there is a best correspondence for the second id and if yes add (and write) it if it isn't already added
					 String bestMatch_secondId = getBestMatch(parts[1], 1, correspondenceFile);
			         if(bestMatch_secondId != null){
			        	 if(!alreadyAddedCorrespondences.contains(bestMatch_secondId)){
			        		bw.write(line);
			             	bw.newLine();
			        	 }
			         }
				 }

			 }
			 
			 br.close();
			 bw.close();
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		
		 
	}
	
	private static ArrayList<String> getMatches(String id, int partNumber, String correspondenceFile){
		ArrayList<String> matches = new ArrayList<String>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(correspondenceFile)));
			String line; 
			 
			 while((line = br.readLine()) != null){
		         String[] parts = line.split(",");
		         if(id.equals(parts[partNumber])){
		        	 matches.add(line);
		         }
			 }

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return matches;
	}
	
	private static int getIndexOfBestCurrentMatch(ArrayList<String> matches){
		int bestLineNumber;
		double max = 0;
		ArrayList<Integer> lineNumbers_max = new ArrayList<Integer>();
		for(int i=0; i<matches.size(); i++){
			double score = Double.valueOf(matches.get(i).split(",")[2].replaceAll("\"", ""));
			if(score > max){
				lineNumbers_max = new ArrayList<Integer>();
				lineNumbers_max.add(i);
			}
			if(score == max){
				lineNumbers_max.add(i);
			}
		}
		
		/*if(lineNumbers_max.size() > 1){
			Random random = new Random();
			bestLineNumber = lineNumbers_max.get(random.nextInt(lineNumbers_max.size()));
		}
		else{
			bestLineNumber = lineNumbers_max.get(0);
		}*/
		
		bestLineNumber = lineNumbers_max.get(0);
		
		return bestLineNumber;
	}
	
	private static String getBestMatch(String id, int partNumber, String correspondenceFile){
		String result = null; 
		
		int partNumberCounterpart;
		if(partNumber == 0){
			partNumberCounterpart = 1;
		}
		else{
			partNumberCounterpart = 0;
		}
		
		ArrayList<String> possibleMatches = getMatches(id, partNumber, correspondenceFile);
		while(!possibleMatches.isEmpty()){
			int index_currentBestMatch = getIndexOfBestCurrentMatch(possibleMatches);
			String currentBestMatch = possibleMatches.get(index_currentBestMatch);
			String id_BestMatchCounterpart = currentBestMatch.split(",")[partNumberCounterpart];
			String bestMatchForCounterpart = getBestMatch(id_BestMatchCounterpart, partNumberCounterpart, correspondenceFile);
			
			// if current best match is the same for the counterpart then an agreement is found and this is the best match for both --> keep it
			if(currentBestMatch.equals(bestMatchForCounterpart)){
				result = currentBestMatch;
			}
			
			else{
				// if current best match is not the same for the counterpart then the match is removed and the search will be continued
				possibleMatches.remove(index_currentBestMatch);
			}
		}
		
		return result;
	}
	
	public static void main (String[] args){
		StableMarriage.removeDuplicateMatches("data/output/club_correspondences_CLEANED.csv");
	}
	
}
