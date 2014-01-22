import java.util.Scanner;


/**
 * @author Nick
 *
 */
public class TournamentScheduler
{
	/**
	 * Main method for the program. Arguments taken are formatted text files with data.
	 * The -t argument is for the team list file.
	 * The -s argument is for the time slot list file.
	 * The -d argument is for the distance between venues list file.
	 * Ex: java TournamentScheduler -t teams.txt -s times.txt -d distances.txt
	 * @param args
	 */
	public static void main(String[] args)
	{
		int i = 0;
		String teamsFile = "src/Teams.txt";
		String timesFile = "src/Times.txt";
		String distancesFile = "src/VenueDistances.txt";
		boolean running = true;

		while(i<args.length)
		{
			if(args[i].equals("-t"))
			{
				teamsFile = args[i+1];
			}
			else if(args[i].equals("-s"))
			{
				timesFile = args[i+1];
			}
			else if(args[i].equals("-d"))
			{
				distancesFile = args[i+1];
			}
			i+=1;
		}
		Tournament t = new Tournament(teamsFile, timesFile, distancesFile);

		while(running)
		{
			Scanner scanner = new Scanner(System.in);
			int size, percent,mutations, iterations;
			String next;
			System.out.print("Default settings?(y/n): ");
			next = scanner.next().trim();
			if(next.equals("y")||next.equals("Y")||next.equals("Yes")||next.equals("yes"))
			{
				t.createSchedule(100, 30, 2, 100);
			}
			else
			{
				System.out.print("Enter initial population size: ");
				size = scanner.nextInt();
				System.out.print("Enter average percentage of survival: ");
				percent = scanner.nextInt();
				System.out.print("Enter average percentage of mutation: ");
				mutations = scanner.nextInt();
				System.out.print("Enter number of generations to iterate: ");
				iterations = scanner.nextInt();
				t.createSchedule(size, percent, mutations, iterations);
			}

			System.out.print("Again?(y/n): ");
			next = scanner.next().trim();
			if(next.equals("y")||next.equals("Y")||next.equals("Yes")||next.equals("yes"))
				running = true;
			else
				running = false;
		}
	}
}
