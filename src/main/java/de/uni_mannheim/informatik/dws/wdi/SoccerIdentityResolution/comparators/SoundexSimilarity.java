package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import java.util.ArrayList;

public class SoundexSimilarity {

	ArrayList<String> code_1 = new ArrayList<String>();
	ArrayList<String> code_2 = new ArrayList<String>();
	ArrayList<String> code_3 = new ArrayList<String>();
	ArrayList<String> code_4 = new ArrayList<String>();
	ArrayList<String> code_5 = new ArrayList<String>();
	ArrayList<String> code_6 = new ArrayList<String>();

	public SoundexSimilarity(){
		code_1.add("b");
		code_1.add("f");
		code_1.add("p");
		code_1.add("v");
		
		code_2.add("c");
		code_2.add("g");
		code_2.add("j");
		code_2.add("k");
		code_2.add("q");
		code_2.add("s");
		code_2.add("x");
		code_2.add("z");
		
		code_3.add("d");
		code_3.add("t");
		
		code_4.add("l");
		
		code_5.add("m");
		code_5.add("n");
		
		code_6.add("r");
	}
	
	public String createSoundex(String word){
		
		String result = word.substring(0,1);
		String input = word;
		
		input = input.replace("a", "");
		input = input.replace("e", "");
		input = input.replace("h", "");
		input = input.replace("i", "");
		input = input.replace("o", "");
		input = input.replace("u", "");
		input = input.replace("w", "");
		input = input.replace("y", "");
				
		int i = 1;	// because the first letter is already covered
		
		while(i<input.length() && result.length() < 4){
			String currentChar = input.substring(i, i+1);
			
			if(code_1.contains(currentChar)){
				result = result + "1";
			}
			else if(code_2.contains(currentChar)){
				result = result + "2";
			}
			else if(code_3.contains(currentChar)){
				result = result + "3";
			}
			else if(code_4.contains(currentChar)){
				result = result + "4";
			}
			else if(code_5.contains(currentChar)){
				result = result + "5";
			}
			else if(code_6.contains(currentChar)){
				result = result + "6";
			}

			
			if(result.substring(result.length()-2, result.length()-1).equals(result.substring(result.length()-1, result.length()))){
				result = result.substring(0, result.length()-1);
			}
						
			i++;
		}
		
		while(result.length() < 4){
			result = result + "0";
		}
		
		return result;
	}
	
	public double calculate(String name1, String name2){
		String soundex1 = createSoundex(name1);
		String soundex2 = createSoundex(name2);

		if(soundex1.equals(soundex2)){
			return 1.0;
		}
		else{
			return 0.0;
		}
	}
	
	
}
