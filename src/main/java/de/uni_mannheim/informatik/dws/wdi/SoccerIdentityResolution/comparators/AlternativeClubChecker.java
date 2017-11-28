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
 * - removing leading and trailing white spaces
 * - removing ".", ";", ":" and ","
 * - replacing accents with the original letter e.g. á → a
 * - handling of German umlauts and "ß"
 *
 * Please adapt the unit test if you change something!
 */
public class AlternativeClubChecker {

	 static String path_ClubNameStopWordsDictionary = "data/ClubNameStopWordsDictionary.csv";
	 static String[] alternativeClubIndicatorsDictionary = {
			 "u[1-9][1-9]",
			 "i*",
			 "[a-d]",
			 "iv",
			 "v",
			 "jugend",
			 "youth"
	 };

	
    public static boolean checkForAlternativeClub(String inputString){
    	
    	String[] stringParts = inputString.split(" ");
    	
    	boolean result = false;
    	
    	for(int i=0; !result & i<alternativeClubIndicatorsDictionary.length; i++){
    		String before = stringParts[stringParts.length-1];
    		String newString = stringParts[stringParts.length-1].replaceAll(alternativeClubIndicatorsDictionary[i] + "$", "");
    		result = !before.equals(newString);
    	}
    	
    	return result;
    	
    }
    
    public static void main (String[] args){
    	System.out.println(AlternativeClubChecker.checkForAlternativeClub("Polonia Bytom"));
		System.out.println(AlternativeClubChecker.checkForAlternativeClub("Polonia Bytom Jugend"));
		System.out.println(true == false);
	}

}
