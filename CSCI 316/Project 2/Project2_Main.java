import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.HashMap;

/*
	args[0] = "Reserved_Words.txt"
	args[1] = "Operators.txt"
	args[2] = "Character_Set.txt"
	args[3] = "Pascal_Code.txt"
 */

public class Project2_Main {
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
		if((s.equalsIgnoreCase("'") || s.equalsIgnoreCase("\"")) && !c) {
			System.out.print("String: ");
			return true;
		}
		else if(c && (s.equalsIgnoreCase("'") || s.equalsIgnoreCase("\""))) {
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
	//checkDeclareLine method which checks if the given line is a valid declaration line using ":"
	public static boolean checkDeclareLine(String line, String ary[]) {
		Scanner checkLine = new Scanner(line);
		if(!checkLine.hasNext()) {
			return false;
		}
		String nextLex = checkLine.next();
		if(!validIdentifier(ary, nextLex)) {
			return false;
		}
		if(!checkLine.hasNext()) {
			return false;
		}
		nextLex = checkLine.next();
		if(!nextLex.equalsIgnoreCase(":")) {
			return false;
		}
		if(!checkLine.hasNext()) {
			return false;
		}
		nextLex = checkLine.next();
		/*this would be a check for variable types, 
		but since INTEGER is only used and is stored in Operator's array, 
		this is done to simplify*/
		if(!nextLex.equalsIgnoreCase("INTEGER")) {
			return false;
		}
		if(!checkLine.hasNext()) {
			return false;
		}
		nextLex = checkLine.next();
		if(nextLex.equalsIgnoreCase(";") || nextLex.equalsIgnoreCase("=")) {
			return true;
		}
		return false;
	}
	//checkDeclareEqualLine method which checks if the given line is a valid declaration line using "="
	public static boolean checkDeclareEqualLine(String line, String ary[]) {
		Scanner checkLine = new Scanner(line);
		if(!checkLine.hasNext()) {
			return false;
		}
		String nextLex = checkLine.next();
		if(!validIdentifier(ary, nextLex)) {
			return false;
		}
		if(!checkLine.hasNext()) {
			return false;
		}
		nextLex = checkLine.next();
		if(!nextLex.equalsIgnoreCase("=")) {
			return false;
		}
		while(checkLine.hasNext()) {
			nextLex = checkLine.next();
			if(!(checkInt(nextLex) || validIdentifier(ary, nextLex))) {
				return false;
			}
			if(!checkLine.hasNext()) {
				return false;
			}
			nextLex = checkLine.next();
			if(nextLex.equalsIgnoreCase(";")) {
				return true;
			}
			else if(!(nextLex.equalsIgnoreCase("-") || nextLex.equalsIgnoreCase("+") || nextLex.equalsIgnoreCase("div") || nextLex.equalsIgnoreCase("mod") || nextLex.equalsIgnoreCase("*") || nextLex.equalsIgnoreCase("%"))) {
				return false;
			}
		}
		return false;
	}
	//checkDeclareLine method which checks if the given line is a valid assignment line
	public static boolean checkAssignLine(String line, String ary[]) {
		Scanner checkLine = new Scanner(line);
		if(!checkLine.hasNext()) {
			return false;
		}
		String nextLex = checkLine.next();
		if(!validIdentifier(ary, nextLex)) {
			return false;
		}
		if(!checkLine.hasNext()) {
			return false;
		}
		nextLex = checkLine.next();
		if(!nextLex.equalsIgnoreCase(":=")) {
			return false;
		}
		//arithmetic Checking
		while(checkLine.hasNext()) {
			nextLex = checkLine.next();
			if(!(checkInt(nextLex) || validIdentifier(ary, nextLex))) {
				return false;
			}
			if(!checkLine.hasNext()) {
				return false;
			}
			nextLex = checkLine.next();
			if(nextLex.equalsIgnoreCase(";")) {
				return true;
			}
			else if(!(nextLex.equalsIgnoreCase("-") || nextLex.equalsIgnoreCase("+") || nextLex.equalsIgnoreCase("div") || nextLex.equalsIgnoreCase("mod") || nextLex.equalsIgnoreCase("*") || nextLex.equalsIgnoreCase("%"))) {
				return false;
			}
		}
		return false;
	}
	//checkIfStatement method which checks if the given line is a valid if statement line
	public static boolean checkIfStatement(String line, String ary[]) {
		Scanner checkLine = new Scanner(line);
		String nextLex = checkLine.next();
		if(!checkLine.hasNext()) {
			return false;
		}
		nextLex = checkLine.next();
		if(!nextLex.equals("(")) {
			return false;
		}
		//boolean expression checking
		while(checkLine.hasNext() && !nextLex.equalsIgnoreCase(")")) {
			nextLex = checkLine.next();
			if(!(checkInt(nextLex) || validIdentifier(ary, nextLex))) {
				return false;
			}
			if(!checkLine.hasNext()) {
				return false;
			}
			nextLex = checkLine.next();
			if(nextLex.equals(")")) {
				return true;
			}
			else if(!(nextLex.equalsIgnoreCase("==") || nextLex.equalsIgnoreCase(">=") || nextLex.equalsIgnoreCase("<=") || nextLex.equalsIgnoreCase(">") || nextLex.equalsIgnoreCase("<") || nextLex.equalsIgnoreCase("!="))) {
				return false;
			}
		}
		return false;
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
		String validChar[] = new String[58];
		while(charFile.hasNext()) {
			validChar[index] = charFile.next();
			index++;
		}
		Arrays.sort(validChar);
		//performing lexical analysis
		String nextLex, nextLine;
		Scanner pascalLine;
		boolean isComment = false; 
		boolean isString = false;
		boolean validLine;
		boolean isStartVar;
		int prevIf = 0;
		int parenCount;
		int beginEnd = 0;
		//get next Lexical as long as there exist another
		while(pascalFile.hasNext()) {
			nextLine = pascalFile.nextLine();
			pascalLine = new Scanner(nextLine);
			validLine = true;
			isStartVar = false;
			isComment = false;
			parenCount = 0;
			while(pascalLine.hasNext()) {
				nextLex = pascalLine.next();
				//checks if the read nextLex is within a comment or string
				isComment = checkComment(nextLex, isComment);
				if(!isComment) {
					isString = checkString(nextLex, isString);
				}
				if((!isComment && !nextLex.equals("*)"))&& (!isString && !(nextLex.equals("'") || nextLex.equals("\"")))) {
					if(!validIdentifier(validChar,nextLex)) {
						System.out.print("UNKNOWN: "+ nextLex + " | ");
					}
					else if(nextLex.equalsIgnoreCase(";")){
						System.out.print(nextLex);
					}
					else if(binarySearch(reservedWords,nextLex,0,34)) {
						System.out.print("Reserved Word: " + nextLex + " | ");
						//matching on BEGIN and END
						if(nextLex.equalsIgnoreCase("begin")) {
							beginEnd++;
						}
						else if(nextLex.equalsIgnoreCase("end")) {
							beginEnd--;
						}
						//if check
						if(nextLine.startsWith("  IF")) {
							validLine = checkIfStatement(nextLine,validChar);
							prevIf++;
						}
						//then check
						if(nextLine.startsWith("  THEN") & prevIf != 1) {
							validLine = false;
						}
						else if(nextLine.startsWith("  THEN") & prevIf == 1) {
							prevIf++;
						}
						//else check
						if(nextLine.startsWith("  ELSE") & prevIf == 0) {
							validLine = false;
						}
						else if(nextLine.startsWith("  ELSE") & prevIf > 0) {
							prevIf = 0;
						}
					}
					else if(checkOperator(opMap, nextLex)) {
						System.out.print(opMap.get(nextLex) + ": " + nextLex + " | ");
						if(isStartVar) {
							if(nextLex.equalsIgnoreCase(":")){
								validLine = checkDeclareLine(nextLine,validChar);
							}
							else if(nextLex.equalsIgnoreCase(":=")){
								validLine = checkAssignLine(nextLine,validChar);
							}
							else if(nextLex.equalsIgnoreCase("=")) {
								validLine = checkDeclareEqualLine(nextLine,validChar);
							}
							else if(nextLex.equalsIgnoreCase("==")) {
								validLine = false;
							}
						}
					}
					else if(checkInt(nextLex)) {
						System.out.print("Num_Const: " + nextLex + " | ");
					}
					else {
						//matching parenthesis
						if(nextLex.equals("(")) {
							parenCount++;
							System.out.print("Parenthesis: " + nextLex + " | ");
						}
						else if(nextLex.equals(")")) {
							parenCount--;
							System.out.print("Parenthesis: " + nextLex + " | ");
						}
						else {
							System.out.print("Identifier: " + nextLex + " | ");
							if(nextLine.startsWith("  " + nextLex)) {
								isStartVar = true;
							}
						}
					}
				}
			}
			System.out.println();
			if(isComment) {
				System.out.println("--Missing closing comment for the above line");
			}
			if(isString) {
				System.out.println("--Missing closing string for the above line");
			}
			if(parenCount != 0) {
				System.out.println("--Missing closing parenthesis for the above line");
			}
			if(!validLine) {
				System.out.println("--Above line is invalid");
			}
		}
		if(beginEnd != 0) {
			System.out.println("--Above program missing END");
		}
		//closing all files
		rWFile.close();
		pascalFile.close();
		opFile.close();
		charFile.close();
	}
}