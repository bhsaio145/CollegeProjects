public class Customer {
	//declaring attribute variables
	private String last, first;
	private String acctNo;
	private double balance;
	//overriding default constructor
	public Customer(){
		balance = 0;
	}
	//setting constructor with 4 variables
	public Customer(String f, String l, String a, double b){
		first = f;
		last = l;
		acctNo = a;
		balance = b;
	}
	//compareTo method returning comparison of last names, and first if last names are equal
	public int compareTo(Customer c) {
		if(last.compareToIgnoreCase(c.last) != 0) {
			return last.compareToIgnoreCase(c.last);
		}
		else {
			return first.compareToIgnoreCase(c.first);
		}
	}
	//equals method returning true if acctNo are the same
	public boolean equals(Customer c) {
		if(acctNo.equals(c.getacctNo())) {
			return true;
		}
		return false;
	}
	//overriding toString method
	public String toString() {
		return first + " " + last + " " + acctNo + " " + balance;
	}	
	//setter and getter methods for each attributes
	public void setFirst(String f) {
		first = f;
	}
	public void setLast(String l){
		last = l;
	}
	public void setacctNo(String a){
		acctNo = a;
	}
	public void setBalance(double b) {
		balance = b;
	}
	public String getFirst(){
		return first;
	}
	public String getLast(){
		return last;
	}
	public String getacctNo() {
		return acctNo;
	}
	public double getBalance() {
		return balance;
	}
	//withdraw and deposit methods which change balance
	public void deposit(double b) {
		balance += b;
	}
	public void withdraw(double b) {
		//only withdraws if there is enough money
		if(balance - b >= 0) {
			balance -= b;
		}
		else {
			//else prints message of cannot withdraw
			System.out.println("Account Balance is insufficient");
		}
	}
}
