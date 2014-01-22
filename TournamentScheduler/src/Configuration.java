import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;


public class Configuration
{
	public static ArrayList<String> readTeamsFromFile(String fileName) throws IOException
	{
		ArrayList<String> teams = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		
		String line = null;
		while((line = in.readLine()) != null)
		{
			teams.add(line.trim());
		}
		in.close();
		
		return teams;
	}
	public static ArrayList<TimeSlot> readTimesFromFile(String fileName) throws IOException
	{
		ArrayList<TimeSlot> times = new ArrayList<TimeSlot>();
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		
		String line = null;
		while((line = in.readLine()) != null)
		{
			String[] parseLine = line.split("[-]");
			String venue = parseLine[0].trim();
			
			String[] parseDate = parseLine[1].trim().split("[/]");
			int month = (int)Double.parseDouble(parseDate[0].trim())-1;
			int day = (int)Double.parseDouble(parseDate[1].trim());
			int year = (int)Double.parseDouble(parseDate[2].trim());
			
			String[] parseTime = parseLine[2].trim().split("[:]");
			int hour = (int)Double.parseDouble(parseTime[0].trim());
			int minute = (int)Double.parseDouble(parseTime[1].trim());
			
			GregorianCalendar cal = new GregorianCalendar();
			cal.set(year, month, day, hour, minute, 0);
			Date date = cal.getTime();
			
			TimeSlot slot = new TimeSlot(date, venue);
			times.add(slot);
		}
		in.close();
		
		return times;
	}
	public static ArrayList<VenueDistance> readDistancesFromFile(String fileName) throws IOException
	{
		ArrayList<VenueDistance> distances = new ArrayList<VenueDistance>();
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		
		String line = null;
		while((line = in.readLine()) != null)
		{
			String[] parseLine = line.split("[-]");
			String venueOne = parseLine[0].trim();
			String venueTwo = parseLine[1].trim();
			double dist = Double.parseDouble(parseLine[2].trim());
			VenueDistance distance = new VenueDistance(venueOne, venueTwo, dist);
			distances.add(distance);
		}
		in.close();
		
		return distances;
	}
}
