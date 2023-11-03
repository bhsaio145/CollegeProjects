public class Main {
	public static void main(String args[]) {
		int a[][] = new int[5][5];
		int values[] = {0, 1, 100, 1, 5,9,0,3,2,100,100,100,0,4,100,100,100,2,0,3,3,100,100,100,0};
		int x = 0;
		for(int i = 0 ; i < a.length ; i++) {
			for(int l = 0 ; l < a.length ; l++) {
				a[i][l] = values[x++];
			}
		}
		int d[][] = new int[5][5];
		int path[][] = new int[5][5];
		allPairs(a,d,path);
	}
	public static void allPairs(int[][] a, int[][] d, int[][] path) {
		int n = a.length;
		
		// Initialize d and path
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				d[i][j] = a[i][j];
				path[i][j] = -1;
			}
		}
		for (int k = 0; k < n; k++) {
			// Consider each vertex as an intermediate
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (d[i][k] + d[k][j] < d[i][j]) {
						// Update shortest path
						d[i][j] = d[i][k] + d[k][j];
						path[i][j] = k;
					}
				}
			}
		}
		PrintPath(path);
	}
	public static void PrintPath(int[][] path) {
		for(int i = 0 ; i < path.length ; i++) {
			for(int l = 0 ; l < path.length ; l++) {
				System.out.print(path[i][l]+1 + " ");
			}
			System.out.println();
		}
	}

}
