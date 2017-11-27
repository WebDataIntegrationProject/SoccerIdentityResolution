package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;


import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simplifier which allows to better compare input strings.
 * The following operations are implemented:
 * - lowercasing
 * - replacing accents with the original letter e.g. á → a
 * - handling of German umlauts and "ß"
 * - removing leading, trailing multiple white spaces
 * - all kinds of punctuation, special characters and numbers
 * - removing club name specific stop words
 * - removing all parentheses along with their content (with subsequent space)
 * - treat "-" connected names as 2 separate names
 *
 * Please adapt the unit test if you change something!
 */
public class StringSimplifier {

	 static String path_ClubNameStopWordsDictionary = "data/ClubNameStopWordsDictionary.csv";
	 static String[] stopWordsDictionary = {
			 "fc",
			 "fcm",
			 "sc",
			 "fv",
			 "sc",
			 "tsv",
			 "vf",
			 "united",
			 "real",
			 "ff",
			 "asd",
			 "ifk",
			 "as",
			 "ac",
			 "msk",
			 "fk",
		 	 "acf",
		 	 "afc",
		 	 "cf",
		 	 "club de",
		 	 "club",
		 	 "vfl",
		 	 "cd",
		 	 "kaa",
		 	 "city",
		 	 "cp",
		 	 "sportverein",
		 	 "sporting",
		 	 "sportfreunde",
			 "alemannia",
			 "germania",
			 "teutonia",
			 "bavaria",
			 "borussia",
			 "eintracht",
			 "concordia",
			 "union",
			 "fortuna",
			 "victoria",
			 "viktoria",
			 "kickers",
			 "werder",
			 "hertha",
			 "arminia",
			 "amicitia",
			 "olympia",
			 "wacker",
			 "rapid",
			 "rapide",
			 "vorwaerts",
			 "dynamo",
			 "dinamo",
			 "athletic",
			 "athletico",
			 "atletico",
			 "usd",
			 "aik",
			 "kf",
			 "wanderers",
			 "fotball"
	 };

	
    public static String simplifyString(String inputString){

        // lowercasing
        String returnString = inputString.toLowerCase();

        // removing some characters that are not helpful
       // returnString = returnString.replace(".", "");
        //returnString = returnString.replace(",", "");
       // returnString = returnString.replace(";", "");
       // returnString = returnString.replace(":", "");

        // German umlauts
        returnString = returnString.replace("Ä", "Ae");
        returnString = returnString.replace("ä", "ae");
        returnString = returnString.replace("Ö", "Oe");
        returnString = returnString.replace("ö", "oe");
        returnString = returnString.replace("Ü", "Ue");
        returnString = returnString.replace("ü", "ue");
        returnString = returnString.replace("ß", "ss");

        // remove all parentheses along with their content (with subsequent space)
        returnString = returnString.replaceAll("\\(.*\\) ","");
        returnString = returnString.replaceAll("\\(.*\\)","");

        // strip accents
        returnString = org.apache.commons.lang3.StringUtils.stripAccents(returnString);

        // treat "-" connected names as 2 separate names
        returnString = returnString.replace("-", " ");
        
        // removing all kinds of punctuation, special characters and numbers
        returnString = returnString.replaceAll("[^a-z0-9 ]", "");
        
        // trimming & removal of multiple whitespaces
        returnString = returnString.trim().replaceAll(" +", " ");
        
        return returnString;
    }
    
    
   public static String simplifyStringClubOptimized(String inputString){
    	
    	String resultString = simplifyString(inputString);
    	
    	for(int i=0; i<stopWordsDictionary.length; i++){
    		
    		String before = resultString;
    		resultString = resultString.replaceAll("^" + stopWordsDictionary[i] + " ", "");
	        resultString = resultString.replaceAll(" " + stopWordsDictionary[i] + "$", "");
	        resultString = resultString.replace(" " + stopWordsDictionary[i] + " ", " ");
	        
	        // assumption: a name will only contain one stop word -> to make the computation faster!
	        if(!before.equals(resultString)){
	        	break;
	        }
    	}
			
        return resultString;
    }
    
    public static String simplifyStringClubName(String inputString){
        String resultString = simplifyString(inputString);

        resultString = resultString.replaceAll("^fc ", "");
        resultString = resultString.replaceAll(" fc$", "");
        resultString = resultString.replace(" fc ", " ");

        return resultString;

    }
    
    
 // NOTE: this is the CSV based version but this seems to be too slow! 
    /*public static String simplifyStringClubOptimized(String inputString){
    	
    	String resultString = simplifyString(inputString);
    	
    	BufferedReader br = null;
		try {
		
			
			br = new BufferedReader(new FileReader(path_ClubNameStopWordsDictionary));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
        String stopWord = "";
        

        try {
			while ((stopWord = br.readLine()) != null) {

				resultString = resultString.replaceAll("^" + stopWord + " ", "");
		        resultString = resultString.replaceAll(" " + stopWord + "$", "");
		        resultString = resultString.replace(" " + stopWord + " ", " ");

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return resultString;
    }*/

    public static void main (String[] args){
    	System.out.println(StringSimplifier.simplifyStringClubOptimized("ac fc Milan Jugend U19 FC"));
    }
}
