package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.com.bytecode.opencsv.CSVWriter;

public class ClubCorrespondencesCleaner {
	

	public static void cleanClubCorrespondences(String clubCorrespondenceFile){
        try {
			BufferedReader br = new BufferedReader(new FileReader(new File(clubCorrespondenceFile)));
			 String line;
			 boolean isAlternativeClub_first;
			 boolean isAlternativeClub_second;
			 File fout = new File("data/output/kaggle_2_transfermarket_correspondences_clubs.csv");
			 FileOutputStream fos = new FileOutputStream(fout);
			 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			 Pattern pattern = Pattern.compile(".*u[0-9][0-9]");
			 
			 
			 	// write only valid matches into the new file
	            while((line = br.readLine()) != null){
	            	
	            	String lineModified = line.replace("_", " ");
	                String[] parts = lineModified.split(",");
	                for(int i=0; i<parts.length; i++){
	                	parts[i] = StringSimplifier.simplifyString(parts[i]);
	                	if(parts[i].matches(".* u[0-9]*$")){
	                		Matcher matcher = pattern.matcher(parts[i]);
	                		if(matcher.find()){
	                			parts[i] = parts[i].replaceAll(".*u[0-9]*$", matcher.group(0));
	                		}
	                		
	                	}
	                	else{
	                		parts[i] = parts[i].replaceAll("[0-9]*$", "");
	                	}
	                	
	                	
	                }
	                isAlternativeClub_first = AlternativeClubChecker.checkForAlternativeClub(parts[0]);
	                isAlternativeClub_second = AlternativeClubChecker.checkForAlternativeClub(parts[1]);
	                
	                if(isAlternativeClub_first == isAlternativeClub_second){
	                	
	                	
	                	
	                	// if both are false, then write the line
	                	if(isAlternativeClub_first == false){
	                		bw.write(line);
	                		bw.newLine();
	                		
	                	}
	                	// if both are true and both are of the same type (e.g. U19 and U19)
	                	else if(isAlternativeClub_first == true){
	                		String[] part_first_splits = parts[0].split(" ");
		                	String[] part_second_splits = parts[1].split(" ");
		                	
		                	if(part_first_splits[part_first_splits.length-1].equals(part_second_splits[part_second_splits.length-1])){
		                		bw.write(line);
		                		bw.newLine();
		                		
		                	}
	                	}
	                	
	                }
	            }
	            
	            bw.close();
	            br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public static void main(String[] args){
		ClubCorrespondencesCleaner.cleanClubCorrespondences("data/correspondences/kaggle_2_transfermarket_correspondences_clubs.csv");
	}
}
