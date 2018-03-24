import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    String[] numbers = input.nextLine().split(",");
    int min = Math.min(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]));
    int max = Math.max(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]));
    int step = Math.abs(Integer.parseInt(numbers[2]));
    int sum = 0;
    for ( int i = min; i <= max; i += step ) {
    	sum += i;
    }
    System.out.println(sum);
  }
}