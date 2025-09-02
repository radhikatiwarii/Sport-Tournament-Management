package main;
public class Test {
    public static void main(String[] args) {
        int a=010;
        int b=07;
        System.out.println(a);

System.out.println(b);    }
}
//Data Flare
import java.util.Random;

int teamAScore = 0;
int teamBScore = 0;

Random rand = new Random();

for (int round = 1; round <= 10; round++) {
    int teamAStrength = rand.nextInt(100); // 0 to 99
    int teamBStrength = rand.nextInt(100);

    System.out.println("Round " + round + ":");
    System.out.println("Team A Strength: " + teamAStrength);
    System.out.println("Team B Strength: " + teamBStrength);

    if (teamAStrength > teamBStrength) {
        teamAScore++;
        System.out.println("Team A wins this round!");
    } else if (teamBStrength > teamAStrength) {
        teamBScore++;
        System.out.println("Team B wins this round!");
    } else {
        System.out.println("It's a tie!");
    }
    System.out.println();
}

System.out.println("Final Score:");
System.out.println("Team A: " + teamAScore);
System.out.println("Team B: " + teamBScore);
