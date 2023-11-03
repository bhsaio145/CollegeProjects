//importing needed libraries
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Phase1_Main {
	static int solutionNum = 0;
	//importGraph method to read the graph from the input file and place it into the 2d array
	public static void importGraph(int graph[][], int v, Scanner infile) {
		for(int i = 0 ; i < v ; i++) {
			for(int j = 0 ; j < v ; j++) {
				graph[i][j] = infile.nextInt();
			}
		}
	}
	//fitnessFunction method which serves as a final check for Hamiltonian circuit too
	public static void fitnessFunction(int graph[][], ArrayList<Integer> path, int v, BufferedWriter outfile) throws IOException {
		//checks if the proposed answer has all verticies included
		//this can be adjusted to accept paths that dont contain all verticies too depending on the fitness function
		if(path.size() == v) {
			//checks if the final vertex in the path has a connection to the first vertex to complete the circuit
			if(graph[path.get(v-1)][path.get(0)] == 1) {
				//outputs the found correct answer to output file
				for(int i = 0 ; i < v ; i++) {
					outfile.write(path.get(i) + " ");
				}
				outfile.write("\n");
			}
		}
	}
	//inputToOutput function which outputs the problem title and input file information to the output file
	public static void inputToOutput(int v, int graph[][], Scanner infile, BufferedWriter outfile) throws IOException {
		outfile.write("Undirected Hamilton circuit	Karp#10\n");
		outfile.write(v + "\n");
		for(int i = 0 ; i < v ; i++) {
			for(int j = 0 ; j < v ; j++) {
				outfile.write(graph[i][j] + " ");
			}
			outfile.write("\n");
		}
		//Divider to seperate input with solutions
		outfile.write("----------------------\n");
	}
	public static void printPath(ArrayList<Integer> path) {
		for(int i= 0; i < path.size() ; i++) {
			System.out.print(path.get(i) + " ");
		}
		System.out.println();
	}
	//hamiltonCircuit method which is a recursive method that iterates all possible combination of paths and checks if each "completed" path is a circuit
	public static void hamiltonCircuit(int graph[][], int v, ArrayList<Integer> path, int p, BufferedWriter outfile) throws IOException {
		if(p == v) {
			fitnessFunction(graph, path, v, outfile);
		}
		else {
			for(int i = 1 ; i < v ; i++) {
				if(graph[path.get(p-1)][i] == 1 && (!path.contains(i))) {
					path.add(i);
					hamiltonCircuit(graph, v, path, p+1, outfile);
				}
			}
		}
		//incrementing number of possible solution whenever theres a full path
		solutionNum++;
		path.remove(p-1);
	}
	public static void main(String[] args) throws IOException{
		//opening args[0] which contains the input file
		String inputName = args[0]; 
		FileReader fReader = new FileReader(inputName);
		BufferedReader bfReader = new BufferedReader(fReader);
		Scanner inFile = new Scanner(bfReader);
		//opening args[1] which contains output file name
		String outputName = args[1]; 
		FileWriter outputWriter = new FileWriter(outputName);
		BufferedWriter outFile = new BufferedWriter(outputWriter);
		//declaring variables and dynamically allocating needed arrays
		int numVertex = inFile.nextInt();
		int graph[][] = new int[numVertex][numVertex];
		ArrayList<Integer> path = new ArrayList<>();
		//importing graph
		importGraph(graph, numVertex, inFile);
		//copying input file information to output file
		inputToOutput(numVertex, graph, inFile, outFile);
		//all circuits will contain all verticies, so to prevent repeating circuits all answers will start with the 0th vertex
		path.add(0);
		//finding all circuits
		hamiltonCircuit(graph, numVertex, path, 1, outFile);
		//ouputting number of solution generated
		outFile.write(Integer.toString(solutionNum));
		//closing all files
		inFile.close();
		outFile.close();
	}
}
