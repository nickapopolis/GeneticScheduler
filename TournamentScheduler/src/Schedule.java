import java.util.ArrayList;
import java.util.Date;


public class Schedule
{
	ArrayList<TimeSlot> times;
	
	public Schedule()
	{
		times = new ArrayList<TimeSlot>();
	}
	public void addTimeSlot(Date startTime, String venueName)
	{
		TimeSlot slot = new TimeSlot(startTime, venueName);
		addTimeSlot(slot);
	}
	public void addTimeSlot(TimeSlot slot)
	{
		if(!timeSlotExists(slot))
			times.add(slot);
	}
	public void addTimeSlots(ArrayList<TimeSlot> slots)
	{
		for(TimeSlot slot:slots)
		{
			addTimeSlot(slot);
		}
	}
	public boolean timeSlotExists(TimeSlot slot)
	{
		for(int i=0; i<times.size();i++)
		{
			TimeSlot curr = times.get(i);
			if(curr.equals(slot))
				return true;
		}
		return false;
	}
	public int getNumTimeSlots()
	{
		return times.size();
	}
	public TimeSlot getTimeSlot(int i)
	{
		return times.get(i);
	}
	public void setTeams(int i, TeamPairing pair)
	{
		times.get(i).setTeams(pair);
	}
	public boolean isValid()
	{
		for(int i=0; i<times.size();i++)
		{
			TimeSlot firstTime = times.get(i);
			for(int j=0; j<times.size();j++)
			{
				if(i!=j)
				{
					TimeSlot secondTime = times.get(j);
					if(firstTime.overlaps(secondTime)&& firstTime.getTeams().containsDuplicates(secondTime.getTeams()))
					{
						return false;
					}
				}
			}
		}
		return true;
	}
	public String toString()
	{
		String ret ="";
		ret+= "--------------------\n";
		ret+= "Tournament Schedule\n";
		ret+= "--------------------\n";
		for( int i=0;i<times.size();i++)
		{
			ret += times.get(i).toString() + "\n";
		}
		return ret;
	}
}
