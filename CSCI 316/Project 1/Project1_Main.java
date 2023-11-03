//Brian Hsiao CSCI316 Project1
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.HashMap;

public class Project1_Main {
	//binarySearch method which performs binary search to find if the given string is a reserved word stored in the sorted array
	public static boolean binarySearch(String ary[], String s, int start, int end) {
		if(start > end) {
			return false;
		}
		else {
			int mid = (start+end)/2;
			if(ary[mid].equalsIgnoreCase(s)) {
				return true;
			}
			//if not found search right half
			else if(ary[mid].compareToIgnoreCase(s) < 0) {
				return binarySearch(ary,s,mid+1,end);
			}
			//if not found search left half
			else {
				return binarySearch(ary,s,start,mid-1);
			}
		}
	}
	//checkInt method which uses pattern to check if the given string is a number constant
	public static boolean checkInt(String s) {
		Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
		return pattern.matcher(s).matches();
	}
	//validIndentifier which checks if the given string contains only valid characters
	public static boolean validIdentifier(String ary[], String s) {
		String s_Upper = s.toUpperCase();
		String subString;
		for(int i = 0 ; i < s_Upper.length() ; i++) {
			subString = s_Upper.substring(i, i+1);
			if(!binarySearch(ary,subString,0,56)) {
				return false;
			}
		}
		return true;
	}
	//checkComment method which checks if current String is the start, end, or in a comment block
	public static boolean checkComment(String s, boolean c) {
		//checks if s is (* meaning the start of a comment
		if(s.equalsIgnoreCase("(*") && !c) {
			System.out.print("Comment: ");
			return true;
		}
		//checks if s is *) and a (* was found previously meaning the end of a comment
		else if(c && s.equalsIgnoreCase("*)")) {
			System.out.println();
			return false;
		}
		//checks if currently in a comment block
		else if(c){
			System.out.print(s+" ");
			return true;
		}
		else {
			return false;
		}
	}
	//checkString method which checks if current String is the start, end, or in a string
	public static boolean checkString(String s, boolean c) {
		if(s.equalsIgnoreCase("'") && !c) {
			System.out.print("String: ");
			return true;
		}
		else if(c && s.equalsIgnoreCase("'")) {
			System.out.print(" | ");
			return false;
		}
		else if(c){
			System.out.print(s+" ");
			return true;
		}
		else {
			return false;
		}
	}
	//checkOperator method which checks if string s is in the hashmap
	public static boolean checkOperator(HashMap m, String s) {
		if(m.get(s) == null) {
			return false;
		}
		return true;
	}
	public static void main(String[] args) throws FileNotFoundException {
		//opening args[0] which contains the reserved words
		String inputName = args[0]; 
		FileReader fReader = new FileReader(inputName);
		BufferedReader bfReader = new BufferedReader(fReader);	
		Scanner rWFile = new Scanner(bfReader);
		//opening args[1] which contains the Operators
		inputName = args[1];
		fReader = new FileReader(inputName);
		bfReader = new BufferedReader(fReader);
		Scanner opFile = new Scanner(bfReader);
		//opening args[2] which contains Valid Characters
		inputName = args[2];
		fReader = new FileReader(inputName);
		bfReader = new BufferedReader(fReader);
		Scanner charFile = new Scanner(bfReader);
		//opening args[3] which contains Pascal Code
		inputName = args[3];
		fReader = new FileReader(inputName);
		bfReader = new BufferedReader(fReader);
		Scanner pascalFile = new Scanner(bfReader);
		//reading reserved words and storing into an array
		int index = 0;
		String reservedWords[] = new String[35];
		while(rWFile.hasNext()) {
			reservedWords[index] = rWFile.next();
			index++;
		}
		Arrays.sort(reservedWords);
		//reading operators and storing into a hashmap
		HashMap<String, String> opMap = new HashMap<>();
		String operator, operator_type;
		while(opFile.hasNext()) {
			operator = opFile.next();
			operator_type = opFile.next();
			opMap.put(operator, operator_type);
		}
		//reading valid characters and storing into an array
		index = 0;
		String validChar[] = new String[56];
		while(charFile.hasNext()) {
			validChar[index] = charFile.next();
			index++;
		}
		Arrays.sort(validChar);
		//performing lexical analysis
		String nextLex;
		boolean isComment = false; 
		boolean isString = false;
		//get next Lexical as long as there exist another
		while(pascalFile.hasNext()) {
			nextLex = pascalFile.next();
			//checks if the read nextLex is within a comment or string
			isComment = checkComment(nextLex, isComment);
			if(!isComment) {
				isString = checkString(nextLex, isString);
			}
			if((!isComment && !nextLex.equals("*)"))&& (!isString && !nextLex.equals("'"))) {
				if(!validIdentifier(validChar,nextLex)) {
					System.out.print("UNKNOWN: "+ nextLex + " | ");
				}
				else if(nextLex.equalsIgnoreCase(";")){
					System.out.println(nextLex);
				}
				else if(binarySearch(reservedWords,nextLex,0,34)) {
					System.out.print("Reserved Word: " + nextLex + " | ");
				}
				else if(checkOperator(opMap, nextLex)) {
					System.out.print(opMap.get(nextLex) + ": " + nextLex + " | ");
				}
				else if(checkInt(nextLex)) {
					System.out.print("Num_Const: " + nextLex + " | ");
				}
				else {
					System.out.print("Identifier: " + nextLex + " | ");
				}
			}
		}
		//closing all files
		rWFile.close();
		pascalFile.close();
		opFile.close();
		charFile.close();
	}
}
