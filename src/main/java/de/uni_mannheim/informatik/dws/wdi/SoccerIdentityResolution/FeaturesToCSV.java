package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution;

import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.FeatureVectorDataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Record;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A helper class which allows to save features of comparators to a CSV file.
 */
public class FeaturesToCSV {

    /**
     * Prints the features to a file in CSV format.
     * Be aware, there are also irrelevant features in there.
     * A sample entry can be found in the second line of the file.
     * @param features The features.
     * @param file The file where the content should be written in.
     * @return The content written.
     */
    public static String writeFeaturesInCSV(FeatureVectorDataSet features, File file){

        StringBuffer internalResult = new StringBuffer();
        StringBuffer result = new StringBuffer();

        //Iterator it = features.records();
        Collection<Record> records = features.get();
        Iterator it = records.iterator();

        // create pattern: (?<==)[a-zA-Z0-9.]*(?=[,}]{1,1})
        Pattern pattern = Pattern.compile("(?<==)[a-zA-Z0-9.]*(?=[,}]{1,1})");

        Matcher matcher;
        int index = 0;
        while(it.hasNext()){
            Record recordEntry = (Record) it.next();
            matcher = pattern.matcher(recordEntry.toString());

            if(index == 0){
                result.append("Sample Entry:\n");
                result.append(recordEntry.toString() + "\n\n");
            }

            internalResult = new StringBuffer();

            // print the features as CSV
            while(matcher.find()){
                internalResult.append(matcher.group() + ",");
            }

            result.append(internalResult.toString().substring(0,internalResult.toString().length()-1) + "\n");
            index++;
        }

        writeContent(file, result.toString());
        return result.toString();
    }


    /**
     * Method overloaded.
     * @param features
     * @param pathName
     * @return
     */
    public static String writeFeaturesInCSV(FeatureVectorDataSet features, String pathName){
        return writeFeaturesInCSV(features, new File(pathName));
    }



        public static void writeContent(File f, String content){

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(content);
            bw.flush();
            bw.close();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

}
