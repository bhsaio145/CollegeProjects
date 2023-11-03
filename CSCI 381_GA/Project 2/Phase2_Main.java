import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Phase2_Main {
	//value of Xi for BBS, initialized as X0
	static float BBS_x;
	//BBS method which generates a random number using the BBS algorithm
	public static float BBS() {
		int N = 67*79;
		BBS_x = (BBS_x*BBS_x) % N;
		return (BBS_x / (N-1));
	}
	//importGraph method to read the graph from the input file and place it into the 2d array
	public static void importGraph(int graph[][], int v, Scanner infile) {
		for(int i = 0 ; i < v ; i++) {
			for(int j = 0 ; j < v ; j++) {
				graph[i][j] = infile.nextInt();
			}
		}
	}
	//fitnessFunction method that evaluates the given answer
	//returns an int of the number of "correct" edges, with an int of vertex+1 being perfect fitness
	public static int fitnessFunction(int ans[], int graph[][]) {
		int fitness = 0;
		for(int i = 0 ; i < ans.length-1 ; i++) {
			//check if current vertex has been used already
			for(int j = 0 ; j < i ; j++) {
				if(ans[j] == ans[i]) {
					return fitness;
				}
			}
			//check if each vertex has an edge to the following vertex 
			if(graph[ans[i]][ans[i+1]] == 1) {
				fitness++;
			}
			else {
				return fitness;
			}
		}
		//check if last vertex has been used already previously
		for(int i = 0 ; i < ans.length-1 ; i++) {
			if(ans[ans.length-1] == ans[i]) {
				return fitness;
			}
		}
		//check if last vertex has an edge to first vertex to finish circuit
		if(graph[ans[ans.length-1]][ans[0]] == 1) {
			fitness++;
		}
		return fitness;
	}
	//phase2Random which generates all 10000 random solutions using Math.random()
	public static float phase2Random(int v, int currBest[], int currBestAll[][], int graph[][]) {
		double rand;
		int currAns[] = new int[v];
		int randV;
		float avgFitness = 0;
		int fitness = 0;
		//for loop representing 100 generations
		for(int i = 0 ; i < 100 ; i++) {
			//generating population of 1000 per generation
			for(int j = 0; j < 1000 ; j++) {
				//generating random solution
				for(int x = 0 ; x < v ; x++) {
					rand = Math.random();
					//bounding random number to 0 - v
					randV = (int) Math.round(rand*(v-1));
					currAns[x] = randV;
				}
				fitness = fitnessFunction(currAns, graph);
				//comparing best solution, and saving better one
				if(fitnessFunction(currBest, graph) <= fitness) {
					for(int x = 0 ; x < currBest.length ; x++) {
						currBest[x] = currAns[x];
					}
				}
				avgFitness += fitness;
			}
			//saving best solution from current generation
			for(int x = 0 ; x < currBest.length ; x++) {
				currBestAll[i][x] = currBest[x];
				//After saving it, reset current best for next generation
				currBest[x] = 0;
			}
		}
		return avgFitness/100000;
	}
	//phase2Random which generates all 10000 random solutions using BBS
	public static float phase2BBS(int v, int currBest[], int currBestAll[][], int graph[][]) {
		double rand;
		int currAns[] = new int[v];
		int randV;
		float avgFitness = 0;
		int fitness = 0;
		//for loop representing 100 generations
		for(int i = 0 ; i < 10 ; i++) {
			//generating population of 1000 per generation
			for(int j = 0; j < 10 ; j++) {
				//generating random solution
				for(int x = 0 ; x < v ; x++) {
					rand = BBS();
					//bounding random number to 0 - v
					randV = (int) Math.round(rand*(v-1));
					currAns[x] = randV;
				}
				fitness = fitnessFunction(currAns, graph);
				//comparing best solution, and saving better one
				if(fitnessFunction(currBest, graph) <= fitness) {
				for(int x = 0 ; x < currBest.length ; x++) {
						currBest[x] = currAns[x];
					}
				}
				avgFitness += fitness;
			}
			//saving best solution from current generation
			for(int x = 0 ; x < currBest.length ; x++) {
				currBestAll[i][x] = currBest[x];
				//After saving it, reset current best for next generation
				currBest[x] = 0;
			}
		}
		return avgFitness/100000;
	}
	//output Results method which outputs all 100 "best" solutions with their fitness values to the output file
	public static void outputResults(int currBestAll[][], int graph[][], BufferedWriter outfile) throws IOException {
		int[] tempCurr = new int[currBestAll[0].length];
		for(int i = 0; i < currBestAll.length ; i++) {
			for(int j = 0 ; j < currBestAll[0].length ; j++) {
				outfile.write(Integer.toString(currBestAll[i][j]) + " ");
				tempCurr[j] = currBestAll[i][j];
			}
			outfile.write("fitness: " + Integer.toString(fitnessFunction(tempCurr,graph)) + "\n");
		}
	}
	public static void main(String[] args) throws IOException{
		//opening args[0] which contains the input file
		String inputName = args[0];
		FileReader fReader = new FileReader(inputName);
		BufferedReader bfReader = new BufferedReader(fReader);
		Scanner inFile = new Scanner(bfReader);
		//opening args[1] which contains output file name for random
		String outputName = args[1]; 
		FileWriter outputWriter = new FileWriter(outputName);
		BufferedWriter outFile = new BufferedWriter(outputWriter);
		//opening args[2] which contains output file name for BBS
		outputName = args[2]; 
		outputWriter = new FileWriter(outputName);
		BufferedWriter outFile2 = new BufferedWriter(outputWriter);
		//declaring variables and dynamically allocating needed arrays
		int numVertex = inFile.nextInt();
		int graph[][] = new int[numVertex][numVertex];
		int currBest[] = new int[numVertex];
		int currBestAll[][] = new int[100][numVertex];
		//seeding BBS X0
		BBS_x = 127;
		//importing graph
		importGraph(graph, numVertex, inFile);
		//running method to determine best solutions of 100 generations of 1000 population each using Math.Random()
		//and outputting the best per generation with its fitness
		float avgFitness = phase2Random(numVertex, currBest, currBestAll, graph);
		outputResults(currBestAll, graph, outFile);
		outFile.write("Total Average Fitness: " + Float.toString(avgFitness));
		//running method to determine best solutions of 100 generations of 1000 population each using BBS
		//and outputting the best per generation with its fitness
		avgFitness = phase2BBS(numVertex, currBest, currBestAll, graph);
		outputResults(currBestAll, graph, outFile2);
		outFile2.write("Total Average Fitness: " + Float.toString(avgFitness));
		//closing all files
		inFile.close();
		outFile.close();
		outFile2.close();
	}
}
