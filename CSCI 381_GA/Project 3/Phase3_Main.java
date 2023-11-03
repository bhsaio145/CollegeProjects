import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Phase3_Main {
	//importGraph method to read the graph from the input file and place it into the 2d array
	public static void importGraph(int graph[][], int v, Scanner infile) {
		for(int i = 0 ; i < v ; i++) {
			for(int j = 0 ; j < v ; j++) {
				graph[i][j] = infile.nextInt();
			}
		}
	}
	//fitnessFunction method that evaluates the given answer
	//returns an int of the number of "correct" edges, with an int of vertex being perfect fitness
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
	//generateInitialPopulation method to randomly generate all members of the initial population
	public static void generateInitialPopulation(int p_count, int numVertex, int population[][]) {
		double rand;
		int randV;
		for(int i = 0; i < p_count ; i++) {
			for(int j = 0; j < numVertex ; j++) {
				rand = Math.random();
				randV = (int) Math.round(rand*(numVertex-1));
				population[i][j] = randV;
			}
		}
	}
	//findBestFit method which finds and returns the answer in the given population with the best fitness
	public static void findBestFit(int v, int[] best, int[][] graph, int[][] population, int p_count) {
		int indexBest = 0;
		int fitnessBest = -1;
		for(int i = 0 ; i < p_count ; i++) {
			if(fitnessFunction(population[i],graph) > fitnessBest) {
				indexBest = i;
				fitnessBest = fitnessFunction(population[i],graph);
			}
		}
		for(int i = 0 ; i < v ; i++) {
			best[i] = population[indexBest][i];
		}
	}
	//uniformCrossover method which performs uniform crossover on the given child chromosome with the given crossover rate
	public static void uniformCrossover(int v, double crossover, int[] child, int[] parent1, int[] parent2) {
		double crossoverChance[] = new double[v];
		for(int i = 0 ; i < v ; i++) {
			crossoverChance[i] = Math.random();
		}
		for(int i = 0 ; i < v ; i++) {
			if(crossoverChance[i] <= crossover) {
				child[i] = parent2[i];
			}
			else {
				child[i] = parent1[i];
			}
		}
	}
	//uniformMutation method which performs uniform mutation on the given child chromosome with the given mutation rate
	public static void uniformMutation(int v, double mutation, int[] child) {
		double rand;
		double mutationChance[] = new double[v];
		for(int i = 0 ; i < v ; i++) {
			mutationChance[i] = Math.random();
		}
		for(int i = 0 ; i < v ; i++) {
			if(mutationChance[i] <= mutation) {
				rand = Math.random();
				child[i] = (int) Math.round(rand*(v-1));
			}
		}
	}
	//nextGereration method which performs step 8 of GA algorithms, computing the next generation
	public static void nextGeneration(int v, int[][] currPop, int[][] nextPop, int p_count, double crossover, double mutation) {
		double rand;
		double randCross, randMutate;
		int indexP1, indexP2;
		int child[] = new int[v];
		for(int i = 1 ; i < p_count ; i++) {
			randCross = Math.random();
			randMutate = Math.random();
			rand = Math.random();
			indexP1 = (int) Math.round(rand*(p_count-1));
			rand = Math.random();
			indexP2 = (int) Math.round(rand*(p_count-1));
			//default child chromosome to be parent 1's chromosome
			for(int j = 0 ; j < v ; j++) {
				child[j] = currPop[indexP1][j];
			}
			//Step 11 Crossover Check
			if(crossover != 0) {
				uniformCrossover(v,crossover,child,currPop[indexP1], currPop[indexP2]);
			}
			//Step 12 Mutation Check
			if(mutation != 0) {
				uniformMutation(v,mutation,child);
			}
			//Step 13 adding resulting child to the next generation
			for(int j = 0 ; j < v ; j++) {
				nextPop[i][j] = child[j];
			}
		}
	}
	public static void outputAnalysis(int v, int[][]  graph, int[][] currPop, int[] best, BufferedWriter outfile, int gen, int popCount) throws IOException{
		double avgFit = 0;
		int minFit = 999;
		int tempFit;
		for(int i = 0; i < popCount ; i++) {
			tempFit = fitnessFunction(currPop[i], graph);
			if(minFit > tempFit) {
				minFit = tempFit;
			}
			avgFit += tempFit;
		}
		avgFit = avgFit/popCount;
		outfile.write("Generation: " + Integer.toString(gen));
		outfile.write("\nBest Fitness: " + Integer.toString(fitnessFunction(best, graph)));
		outfile.write("\nWorst Fitness: " + Integer.toString(minFit));
		outfile.write("\nAverage Fitness: " + Double.toString(avgFit) + "\n----------------------\n");
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
		int bestFit[] = new int[numVertex];
		//declaring GA Parameter variables
		int maxGeneration = 100;
		int populationCount = 1000;
		double crossoverRate = 0.03;
		double mutationRate = 0.01;
		int FitnessThreshold = numVertex;
		//declaring variables and dynamically allocating needed arrays to store populations
		int generationPopulation[][] = new int[populationCount][numVertex];
		int next_generationPopulation[][] = new int[populationCount][numVertex];
		//importing graph
		importGraph(graph, numVertex, inFile);
		//randomly generating initial population
		generateInitialPopulation(populationCount, numVertex, generationPopulation);
		//determining best fit member of the initial population
		findBestFit(numVertex,bestFit,graph,generationPopulation,populationCount);
		for(int currGen = 0 ; currGen < maxGeneration ; currGen++) {
			//Output Analysis
			if(currGen == 0 || currGen == maxGeneration/2 || currGen == maxGeneration-1) {
				outputAnalysis(numVertex, graph, generationPopulation, bestFit, outFile, currGen, populationCount);
			}
			//Step 4 : checking stopping criteria of fitness_checking
			if(fitnessFunction(bestFit, graph) >= FitnessThreshold) {
				for(int i = 0 ; i < numVertex ; i++) {
					outFile.write(Integer.toString(bestFit[i]) + " ");
				}
				outFile.write("\nFitness: " + Integer.toString(fitnessFunction(bestFit, graph)));
				outFile.write("\nGeneration: " + Integer.toString(currGen));
				inFile.close();
				outFile.close();
				return;
			}
			//Step 6: Memetic Step
			next_generationPopulation[0] = bestFit.clone();
			//Step 7: Let parent pool be the current population
			//Step 8: computing next generation
			nextGeneration(numVertex,generationPopulation,next_generationPopulation,populationCount,crossoverRate,mutationRate);
			//Step 15: resetting current population
			generationPopulation = next_generationPopulation.clone();
			//Step 3 of next generation: determining best fit member
			findBestFit(numVertex,bestFit,graph,generationPopulation,populationCount);
		}
		//Reach here if maxGeneration has been reached, outputting the current best fit member
		for(int i = 0 ; i < numVertex ; i++) {
			outFile.write(Integer.toString(bestFit[i]) + " ");
		}
		outFile.write("\nFitness: " + Integer.toString(fitnessFunction(bestFit, graph)));
		outFile.write("\nGeneration: 100");
		//closing all files
		inFile.close();
		outFile.close();
	}
}
