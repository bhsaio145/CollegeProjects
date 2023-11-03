import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class myBST {
	//declaring head node;
	protected myBSTNode head;
	//declaring String variable which will be used to transfer tree to output txt file
	private static String returnString = "";
	//insert method
	public void insert(Customer c) {
		//calls recursive version of the method
		head = insertR(head, c);
	}
	private static myBSTNode insertR(myBSTNode n, Customer c) {
		//if node is null, insert node
		if(n == null) {
			n = new myBSTNode(c);
			return n;
		}
		//if comparable is less, goes to left of current node
		if(n.info.compareTo(c) > 0) {
			n.left = insertR(n.left, c);
		}
		//if comparable is less, goes to right of current node
		else if(n.info.compareTo(c) < 0) {
			n.right = insertR(n.right, c);
		}
		return n;
	}
	//inOrder method to print BST in order
	public void inOrder() {
		//calls recursive version
		inOrderR(head);
	}
	public static void inOrderR(myBSTNode n) {
		//returns if node is null
		if(n == null) {
			return;
		}
		//prints elements in BST in order of left -> current -> right
		inOrderR(n.left);
		System.out.println(n.info);
		inOrderR(n.right);
	}
	//preOrder method to add BST into a new txt file in preOrder
	public void preOrderPrint() throws FileNotFoundException{
		//moving all elements of BST in preorder into a single string
		preOrderPrintR(head);
		PrintWriter out = new PrintWriter("Project1Out.txt");
		out.print(returnString);
		out.close();
	}
	private static void preOrderPrintR(myBSTNode n) throws FileNotFoundException {
		if(n == null) {
			return;
		}
		//adds BST element to string
		returnString += n.info + " \n";
		preOrderPrintR(n.left);
		preOrderPrintR(n.right);
	}
	//delete method for node with comparable data of c
	public void delete(Customer c) {
		//calls recursive version
		head = deleteR(head, c);
	}
	public static myBSTNode deleteR(myBSTNode n , Customer c) {
		//if current node is empty return n;
		if(n == null) {
			return n;
		}
		//if comparable data is less then current node check left of current node
		if(n.info.compareTo(c) > 0) {
			n.left = deleteR(n.left , c);
		}
		//if comparable data is greater then current node check right of current node
		else if(n.info.compareTo(c) < 0) {
			n.right = deleteR(n.right , c);
		}
		//else current node is to be deleted
		else {
			//conditions of delete if there are none or one child
			if(n.left == null) {
				return n.right;
			}
			if(n.right == null) {
				return n.left;
			}
			//if there are 2 children then replace current node with min of right
			Customer newroot = FindMin(n.right);
			n.info = newroot;
			n.right = deleteR(n.right, newroot);
		}
		return n;
	}
	//finds the mininmum node in bst by traveling to left most child
	private static Customer FindMin(myBSTNode n) {
		while(n.left != null) {
			n = n.left;
		}
		return n.info;
	}
	//accBalance Method to find the given customer
	//takes addition parameter of String and double for the method to be able to handle deposit, withdraw, and view Balance
	public void accBalance(Customer c , String type, double d) {
		accBalanceR(head, c, type, d);
	}
	private static void accBalanceR(myBSTNode n ,Customer c , String type, double d) {
		if(n == null) {
			return;
		}
		//Customer is less so check node's left child
		if(n.info.compareTo(c) > 0) {
			accBalanceR(n.left, c, type, d);
		}
		//Customer is greater so check node's right child
		else if(n.info.compareTo(c) < 0) {
			accBalanceR(n.right, c, type, d);
		}
		//Customer is found
		else if(n.info.compareTo(c) == 0) {
			//deposit
			if(type.equals("1")) {
				n.info.deposit(d);
			}
			//withdraw
			else if(type.equals("2")) {
				n.info.withdraw(d);
			}
			//else view Balance
			else {
				System.out.println("Balance is: " + n.info.getBalance());
			}
		}
	}
}