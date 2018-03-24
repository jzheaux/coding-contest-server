using System;
 
public class Solution {
	public static void Main(string[] args) {
		string input = Console.ReadLine();
		string[] numbers = input.Split(',');
		int sum = 0;
		foreach (string t in numbers) {
			sum += int.Parse(t);
		}
		Console.WriteLine(sum / numbers.Length); 
	}
}

