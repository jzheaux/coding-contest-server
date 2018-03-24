import java.util.Scanner;

public class StepIt {
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    String[] numbers = input.nextLine().split(" ");
    int x = Integer.parseInt(numbers[0]);
    int y = Integer.parseInt(numbers[1]);
    double z = Double.parseDouble(numbers[2]);
    long iterations = (long)(( y - x ) / z) + 1;
    System.out.println(iterations < 0 ? -1 : iterations);
  }
}