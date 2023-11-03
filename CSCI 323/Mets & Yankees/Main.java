import java.lang.Math;

public class Main {
	public static double MemChance[] = new double[100];
	public static double ChancesToWin(int c) {
		int half = c/2;
		if (c%2 == 1) {
			half++;
		}
		return ChancesToWinR(c, half);
	}
	public static double ChancesToWinR(int c, int n) {
		double chance = Math.pow(.463, n);
		if(c == n) {
			return chance;
		}
		if(MemChance[c] != 0) {
			return MemChance[c];
		}
		else {
			chance = chance * Math.pow(.537, c-n) *((factorial(c-1)/(factorial(n-1)*factorial(c-n))));
			MemChance[c] = chance;
			return MemChance[c] + ChancesToWinR(c-1 , n);
		}
	}
	public static int factorial(int n) {
		int f = 1;
		for(int i = 1 ; i <= n ; i++) {
			f = f * i;
		}
		return f;
	}
	public static void main(String args[]) {
		System.out.println(ChancesToWin(7) * 100);
	}
}
