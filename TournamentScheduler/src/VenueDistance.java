
public class VenueDistance
{
	String firstVenue;
	String secondVenue;
	double distance;
	
	public VenueDistance(String first, String second, double dist)
	{
		firstVenue = first;
		secondVenue  = second;
		distance = dist;
	}
	public String getFirstVenue()
	{
		return firstVenue;
	}
	public String getSecondVenue()
	{
		return secondVenue;
	}
	public double getDistance()
	{
		return distance;
	}
	public String toString()
	{
		return distance + " km from " + firstVenue + " to " + secondVenue;
	}
	public boolean contains(String first, String second)
	{
		return (firstVenue.equals(first)||secondVenue.equals(first)) && (firstVenue.equals(second)||secondVenue.equals(second));
	}
}
