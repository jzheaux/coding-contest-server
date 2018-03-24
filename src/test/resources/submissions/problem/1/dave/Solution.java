import java.util.Scanner;

public class Solution {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String[] params = input.nextLine().split(",");
		int start = Math.min(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
		int end = Math.max(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
		int step = Math.abs(Integer.parseInt(params[2]));
		int sum = 0 ;
		for ( int x = start; x <= end; x+= step ) {
			sum += x;
		}
		System.out.println(sum);
	}
}