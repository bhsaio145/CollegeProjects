public class myBSTNode {
	//declaring variables
	protected Customer info;
	protected myBSTNode left;
	protected myBSTNode right;
	//overloading default constructor
	public myBSTNode() {
	}
	//one variable constructor
	public myBSTNode(Customer c){
		//setting info equal to inputted customer
		info = c;
	}
}
