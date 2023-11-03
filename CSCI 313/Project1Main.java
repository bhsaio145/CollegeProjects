import java.io.File;
import java.util.Scanner;

public class Project1Main {
	public static void main(String[] args) throws Exception {
		File inputFile = new File("Project1.txt");
		Scanner inFile = new Scanner(inputFile);
		Scanner input = new Scanner(System.in);
		String num = " " , num1 = " ";
		String tfirst, tlast, taccNo, tbal;
		double tdbal;
		myBST BSTtree = new myBST();
		//Addition array of Customers
		Customer[] AccDB = new Customer[10000];
		//while loop to check all lines of input text file
		while(inFile.hasNext()) {
			//Formated so each variable is a word with each Customer being the line
			tfirst = inFile.next();
			tlast = inFile.next();
			taccNo = inFile.next();
			tbal = inFile.next();
			//converting String to double for Balance
			tdbal = Double.parseDouble(tbal);
			//creating the Customer and inserting it into the BST
			Customer c = new Customer(tfirst, tlast, taccNo, tdbal);
			BSTtree.insert(c);
			//saving customer to the corresponding element in AccDB
			AccDB[Integer.parseInt(c.getacctNo())] = c;
			//if statement for next line to ensure NoSuchElementException does not occur
			if(inFile.hasNext()) {
				inFile.nextLine();
			}
		}
		//loops as long as user does not input 6 to exit
		while(!num.equals("6")) {
			PrintMenu();
			//User input
			num = input.nextLine();
			//Switch case depending on user input
			switch(num) {
				case "1": 
					System.out.println("============================");
					//new ask if Account number is available
					System.out.println("Do you have the Account Number?\nInput 1 if Yes: ");
					//using num1 as input instead of num, incase any input is 6
					num1 = input.nextLine();
					//if account number is available then searches through the Array AccDB (hashing)
					//getting the customer through the array is faster then searching through the BST for the same customer
					//But if there is no Account number then there would be no way to efficiently search the array, thus BST still needed
					if(num1.equals("1")) {
						Customer tempCD = askAInfo();
						System.out.print("Input Account's Balance Change Amount: ");
						num1 = input.nextLine();
						//adds to balance
						AccDB[Integer.parseInt(tempCD.getacctNo())].setBalance(AccDB[Integer.parseInt(tempCD.getacctNo())].getBalance() + Double.parseDouble(num1));
					}
					else {
						//declaring temporary Customer Object for easier BST access and comparison
						Customer tempCD = askFLInfo(num);
						BSTtree.accBalance(tempCD, num, tempCD.getBalance());
					}
					break;
				case "2":
					System.out.println("============================");
					System.out.println("Do you have the Account Number?\nInput 1 if Yes: ");
					num1 = input.nextLine();
					if(num1.equals("1")) {
						Customer tempCW = askAInfo();
						System.out.print("Input Account's Balance Change Amount: ");
						num1 = input.nextLine();
						//checks if balance is enough to withdraw
						if(AccDB[Integer.parseInt(tempCW.getacctNo())].getBalance() >= Double.parseDouble(num1)) {
							//subtracts from balance
							AccDB[Integer.parseInt(tempCW.getacctNo())].setBalance(AccDB[Integer.parseInt(tempCW.getacctNo())].getBalance() - Double.parseDouble(num1));
						}
						else {
							System.out.println("Account Balance is insufficient");
						}
					}
					else {
						Customer tempCW = askFLInfo(num);
						BSTtree.accBalance(tempCW, num, tempCW.getBalance());
					}
					break;
				case "3":
					System.out.println("============================");
					System.out.println("Do you have the Account Number?\nInput 1 if Yes: ");
					num1 = input.nextLine();
					if(num1.equals("1")) {
						Customer tempC = askAInfo();
						System.out.println("Balance is: " + AccDB[Integer.parseInt(tempC.getacctNo())].getBalance());
					}
					else {
						Customer tempC = askFLInfo(num);
						BSTtree.accBalance(tempC, num, 0);
					}
					break;
				case "4":
					System.out.println("============================");
					Customer newCInsert = askWholeInfo();
					BSTtree.insert(newCInsert);
					//placing customer into array
					AccDB[Integer.parseInt(newCInsert.getacctNo())] = newCInsert;
					break;
				case "5":
					System.out.println("============================");
					Customer newCDelete = askWholeInfo();
					BSTtree.delete(newCDelete);
					//deleting customer in array
					AccDB[Integer.parseInt(newCDelete.getacctNo())] = null;
					break;
			}
			System.out.println("---------------------------------\n");
		}
		//output txt file
		BSTtree.preOrderPrint();
		//closing Scanners
		input.close();
		inFile.close();
	}
	//method for printing the entire Menu for Organization
	private static void PrintMenu() {
		System.out.println("1. Make a deposit to existing customer account");
		System.out.println("2. Make a withdrawal from existing customer account");
		System.out.println("3. Check balance of an existing customer account");
		System.out.println("4. Open a new customer account");
		System.out.println("5. Close a customer account");
		System.out.println("6. Exit the program");
		System.out.print("User Input : ");
	}
	//method to ask Customer information (for delete and insert)
	private static Customer askWholeInfo() {
		String tfirst, tlast, taccNo, tbal;
		double tdbal;
		Scanner Accinput = new Scanner(System.in);
		System.out.print("Input Account's First Name: ");
		tfirst = Accinput.nextLine();
		System.out.print("Input Account's Last Name: ");
		tlast = Accinput.nextLine();
		System.out.print("Input Account's Number: ");
		taccNo = Accinput.nextLine();
		System.out.print("Input Account's Balance: ");
		tbal = Accinput.nextLine();
		tdbal = Double.parseDouble(tbal);
		Customer ct = new Customer(tfirst, tlast, taccNo, tdbal);
		return ct;
	}
	//Method to ask Customer First and Last name ONLY (for withdraw, deposit, and view Balance)
	private static Customer askFLInfo(String n) {
		String tfirst, tlast, taccNo, tbal;
		double tdbal;
		Scanner Accinput = new Scanner(System.in);
		System.out.print("Input Account's First Name: ");
		tfirst = Accinput.nextLine();
		System.out.print("Input Account's Last Name: ");
		tlast = Accinput.nextLine();
		taccNo = "0";
		//Sets Customer Balance if this is for deposit or withdraw (will be used later in BST)
		if(n.equals("1") || n.equals("2")) {
			System.out.print("Input Account's Balance Change Amount: ");
			tbal = Accinput.nextLine();
			tdbal = Double.parseDouble(tbal);
		}
		//if just view balance then set Balance to 0
		else {
			tdbal = 0;
		}
		Customer ct = new Customer(tfirst, tlast, taccNo, tdbal);
		return ct;
	}
	//Method to ask Customer Account Number Only (for Hashing)
	private static Customer askAInfo() {
		String tfirst = "", tlast ="", taccNo;
		double tdbal = 0.0;
		Scanner Accinput = new Scanner(System.in);
		System.out.print("Input Account Number: ");
		taccNo = Accinput.nextLine();
		Customer ct = new Customer(tfirst, tlast, taccNo, tdbal);
		return ct;
	}
}
