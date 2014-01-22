import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class TimeSlot
{
	protected Date startTime;
	protected String venueName;
	TeamPairing teams;
	
	public TimeSlot(Date time, String venue)
	{
		startTime = time;
		venueName = venue;
	}
	public TimeSlot(Date time, String venue, String first, String second)
	{
		startTime = time;
		venueName = venue;
		teams = new TeamPairing(first, second);
	}
	public void setTeams(TeamPairing pair)
	{
		teams = pair;
	}
	public void setTeams(String first, String second)
	{
		teams = new TeamPairing(first, second);
	}
	public String getFirstTeam()
	{
		return teams.getFirstTeam();
	}
	public String getSecondTeam()
	{
		return teams.getSecondTeam();
	}
	public boolean equals(TimeSlot slot)
	{
		return this.startTime.equals(slot.startTime) && this.venueName.equals(slot.venueName);
	}
	public String getVenueName()
	{
		return venueName;
	}

	@SuppressWarnings("deprecation")
	public boolean overlaps(TimeSlot slot)
	{
		GregorianCalendar first = new GregorianCalendar();
		first.setTime(startTime);
		GregorianCalendar second = new GregorianCalendar();
		second.setTime(slot.startTime);
		if(first.get(Calendar.YEAR)== second.get(Calendar.YEAR) && first.get(Calendar.MONTH)== second.get(Calendar.MONTH) && first.get(Calendar.DAY_OF_MONTH) == second.get(Calendar.DAY_OF_MONTH))
		{
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(startTime);
			int firstStart = calendar.get(Calendar.HOUR_OF_DAY)*100 + calendar.get(Calendar.MINUTE);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)+1);
			int firstEnd = calendar.get(Calendar.HOUR_OF_DAY)*100 + calendar.get(Calendar.MINUTE);
			
			calendar.setTime(slot.startTime);
			int secondStart = calendar.get(Calendar.HOUR_OF_DAY)*100 + calendar.get(Calendar.MINUTE);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)+1);
			int secondEnd = calendar.get(Calendar.HOUR_OF_DAY)*100 + calendar.get(Calendar.MINUTE);
			
			if((firstStart>secondStart && firstStart<secondEnd) || 
			   (secondStart>firstStart && secondStart<firstEnd) ||
			   (firstStart == secondStart))
			{
				return true;
			}
			
		}
		return false;
	}
	
	public String toString()
	{
		return startTime.toString() + ": " + teams.toString()+ " at "+venueName;
	}
	public TeamPairing getTeams()
	{
		return teams;
	}
}
