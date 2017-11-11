package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;


import org.apache.commons.lang.StringUtils;

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
public class StringSimplifier {

    public static String simplifyString(String inputString){

        // lowercasing
        String returnString = inputString.toLowerCase();

        // removing some characters that are not helpful
        returnString = returnString.replace(".", "");
        returnString = returnString.replace(",", "");
        returnString = returnString.replace(";", "");
        returnString = returnString.replace(":", "");

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

        // trimming
        returnString = returnString.trim();

        return returnString;
    }

}
