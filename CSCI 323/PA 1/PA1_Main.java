/**
 * Brian Hsiao
 * CSCI 323
 * 3/12/22
 * Programming assignment 1: assessing and comparing the runtime of 4 commonly used sorting algorithms
 */
import java.util.Random;
import java.util.stream.IntStream;

public class PA1_Main {
	/*Declaring all counters for each algorithm
	 * ta is the time accumulators
	 * ca is the comparison accumulator
	 * ca_S is the swap accumulator
	 */
	public static long taI=0, taH=0, taM=0, taQ=0;
	public static long caI=0, caH=0, caM=0, caIS=0, caQ=0 ,caMA =0, caHS=0, caMS, caQS;
	public static void main(String args[]) {
		long t = 0;
		//for loop to run each algorithm 100 times
		for(int i = 0 ; i < 100 ; i++) {
			/*
			 * test arrays used for various parts of the assignment
			 * int[] arr = IntStream.generate(() -> new Random().nextInt(9)+1).limit(10).toArray();
			 * int[] arr = IntStream.generate(() -> new Random().nextInt(999)+1).limit(16).toArray();
			 */
			//declaring a random array of size 10000, containing numbers from 1 to 1,000,000
			int[] arr = IntStream.generate(() -> new Random().nextInt(999999)+1).limit(100000).toArray();
			//Insertion sort
			t = System.currentTimeMillis();
			InsertionSort(arr);
			t = System.currentTimeMillis() - t;
			taI += t;
			//Heap Sort
			t = System.currentTimeMillis();
			HeapSort(arr);
			t = System.currentTimeMillis() - t;
			taH += t;
			//Merge Sort
			t = System.currentTimeMillis();
			MergeSort(arr);
			t = System.currentTimeMillis() - t;
			taM += t;
			//Quick Sort
			t = System.currentTimeMillis();
			QuickSort(arr);
			t = System.currentTimeMillis() - t;
			taQ += t;
		}
		//printing all the information, and getting averages by dividing each counter by 100 (to get average per array ran)
		System.out.println("Insertion: " + taI/100 + "ms | " + caI/100 + " comparison | " + caIS/100 + " swaps | ");
		System.out.println("Heap Sort: " + taH/100 + "ms | " + caH/100 + " comparison | " + caHS/100 + " swaps | ");
		System.out.println("Merge    : " + taM/100 + "ms | " + caM/100 + " comparison | " + caMS/100 + " swaps | " + caMA/100 + "arrays");
		System.out.println("Quick    : " + taQ/100 + "ms | " + caQ/100 + " comparison | " + caQS/100 + " swaps | ");
	}
	//method for insertion sort
	public static void InsertionSort(int[] a) {
		//creating a new temp array to not change the inputted array
		int[] arrt = new int[a.length];
		for(int i = 0 ; i< a.length ; i++) {
			arrt[i] = a[i];
		}
		//go through all elements of the array and swap it with the element before it if it is less then it
		for(int i = 1 ; i < arrt.length; i++) {
			int k = arrt[i];
			int j = i;
			while (j > 0 && arrt[j-1] > k) {
				caI++;
				caIS++;
				arrt[j] = arrt[j-1];
				j--;
			}
			caI++;
		}
	}
	//method for heap sort (Max heap)
	public static void HeapSort(int[] a) {
		//build heap method creates the new and temp array
		int[] arrt = BuildHeap(a);
		//for every element in the heap array, delete it to create the sorted min array
		for(int i = 0 ; i < arrt.length-1 ; i++) {
			DeleteMax(arrt, arrt.length-i);
		}
	}
	//Build heap method
	public static int[] BuildHeap(int[] a) {
		//creating a new array to build heap, this array will ignore the 0th element to make the code easier
		int[] arrt = new int[a.length+1];
		//for loop to insert all elements of input array into heap
		for(int i = 1 ; i < a.length+1 ; i++) {
			arrt[i] = a[i-1];
			int ti = i;
			//comparing the inserted element, to see if it is in place in the Max heap
			//if the parent of the element is smaller, swap it
			while((arrt[ti] > arrt[ti/2]) && (ti != 1)) {
				caHS++;
				Swap(arrt, ti, ti/2);
				ti = ti/2;
				caH++;
			}
			caH++;
		}
		//return max heap
		return arrt;
	}
	//Delete max method 
	public static void DeleteMax(int[] a, int l) {
		caHS++;
		//places the first element to the back
		//since the root of the heap is always the greatest
		Swap(a, 1, l-1);
		int i = 1;
		//for the new head of the heap, pushes it down the heap to keep the max heap ordered
		//only should a child of the element be greater, swap them
		while(i<l && (2*i+1 != l-1) && (2*i+1 < l)) {
			caH++;
			if((Math.max(a[2*i], a[2*i+1]) > a[i])) {
				//swap with left child
				if(a[2*i] > a[2*i+1]) {
					caHS++;
					Swap(a, i, 2*i);
					i = 2*i;
				}
				//swaps with right child
				else {
					caHS++;
					Swap(a, i, 2*i+1);
					i = 2*i+1;
				}
			}
			else{
				return;
			}
		}
	}
	//Merge Sort Method
	public static void MergeSort(int[] a) {
		//creating a new temp array to not change the inputted array
		int[] arrt = new int[a.length];
		for(int i = 0 ; i< a.length ; i++) {
			arrt[i] = a[i];
		}
		//calls on the recursive version of Mergesort
		MergesortR(arrt, 0, arrt.length-1);
	}
	//Recursive method of merge sort
	public static void MergesortR(int[] a, int first, int last) {
		//returns if inputted "segment" is only 1 element
		if(first == last) {
			return;
		}
		//post order traversal of merge sort
		int mid = (first+last)/2;
		MergesortR(a, first, mid);
		MergesortR(a, mid+1, last);
		Merge(a, first, last, mid);
	}
	//Merge method to combine two array "segments"
	public static void Merge(int[] a, int f, int l, int m) {
		int left = f, right = m+1;
		//creating temporary array for the combined elements
		caMA++;
		int[] b = new int[l-f+1];
		int i=0;
		//increment through left and right adding the lesser element to the combined array
		while((left <= m )&&(right <= l)) {
			caM++;
			if(a[left]< a[right]) {
				b[i++] = a[left++];
			}
			else {
				b[i++] = a[right++];
			}
		}
		//should one array end early, adds remainder of other array's element to the combined array
		caM++;
		if(left > m) {
			//adding remaining right elements
			for(int j = right ; j <= l ; j++) {
				b[i++] = a[j];
			}
		}
		else {
			//adding remaining left elements
			for(int j = left; j <= m ; j++) {
				b[i++] = a[j];
			}
		}
		//place combined array elements into the "main" array
		int j = 0;
		for(int o = f ; o <= l ; o++) {
			a[o] = b[j++];
			caMS++;
		}
	}
	//Quick Sort Method
	public static void QuickSort(int[] a) {
		//Temporary array
		int[] arrt = new int[a.length];
		for(int i = 0 ; i< a.length ; i++) {
			arrt[i] = a[i];
		}
		//Quick Sort Recursive
		QuicksortR(arrt, 0, arrt.length-1);
	}
	//Quick Sort Recursive Method
	public static void QuicksortR(int[] a, int first, int last) {
		if(first < last) {
			//quick sort inorder traversal
			int pivot = Partition(a, first, last);
			QuicksortR(a,first, pivot-1);
			QuicksortR(a,pivot+1,last);
		}
	}
	//Parition method for Quicksort
	public static int Partition(int[] a , int first, int last) {
		//set pivot as the last element 
		int pivot = a[last];
		int left = first, right = last-1;
		while(left <= right) {
			//keep incrementing left pointer and stopping when pointed element is greater then pivot
			while((left <= right) && (a[left] <= pivot)) {
				left++;
				caQ++;
			}
			caQ++;
			//keep incrementing right pointer and stopping when pointed element is less then pivot
			while((left <= right) && (a[right] >= pivot)) {
				right--;
				caQ++;
			}
			caQ++;
			if(left < right) {
				//swapping the elements pointed at to organize array
				caQS++;
				Swap(a, left++, right--);
			}
		}
		//swap the pivot and where last pointer ended to place pivot in correct place
		//after this swap all elements left of pivot will be less then pivot
		//and all elements right of pivot will be greater then the pivot
		caQS++;
		Swap(a, left, last);
		return left;
	}
	//Simple Swap Method to interchange 2 elements
	public static void Swap(int[] a, int num1, int num2) {
		int temp = a[num1];
		a[num1] = a[num2];
		a[num2] = temp; 
	}
	//print arr method used when testing the code
	public static void printarr(int[] a) {
		for(int i = 0 ; i< a.length ; i++) {
			System.out.print(a[i] + ",");
		}
		System.out.println();
	}
}
