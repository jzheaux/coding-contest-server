import java.util.Scanner;

public class BowlingScore {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String lineOfScores = input.nextLine();
		String[] scoresAsString = lineOfScores.split(" ");
		Integer[] scores = new Integer[scoresAsString.length];
		for ( int i = 0; i < scores.length; i++ ) {
			scores[i] = Integer.parseInt(scoresAsString[i]);
		}
		Integer total = 0;
		int currentFrame = 1;
		for ( int i = 0; i < scores.length && currentFrame <= 10; i++ ) {
			total += scores[i];
			if ( scores[i] == 10 ) {
				total += i + 1 < scores.length ? scores[i+1] : 0;
				total += i + 2 < scores.length ? scores[i+2] : 0;
			} else if ( i + 1 < scores.length ) {
				if ( scores[i] + scores[i+1] == 10 ) {
					total += scores[i+1];
					total += i + 2 < scores.length ? scores[i+2] : 0;
					i++;
				} else {
					total += scores[i+1];
					i++;
				}
			}
			currentFrame++;
		}
		System.out.println(total);
	}
}
