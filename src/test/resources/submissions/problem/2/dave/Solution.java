import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Solution {
	public static void main(String[] args) throws IOException {
		Scanner input = new Scanner(System.in);
		String[] params = input.nextLine().split(",");
		File file = new File("test.txt");
		file.createNewFile();
		System.out.println(params[0]);
	}
}