
public class TeamPairing
{
	protected String firstTeam;
	protected String secondTeam;
	
	public TeamPairing()
	{
		
	}
	public TeamPairing(String first, String second)
	{
		setTeams(first, second);
	}
	public boolean setTeams(String first, String second)
	{
		if(first.equals(second))
		{
			return false;
		}
		firstTeam = first;
		secondTeam = second;
		return true;
	}
	public String getFirstTeam()
	{
		return firstTeam;
	}
	public String getSecondTeam()
	{
		return secondTeam;
	}
	public String toString()
	{
		return firstTeam +" vs "+ secondTeam;
	}
	public boolean contains(String name)
	{
		return firstTeam.equals(name)|| secondTeam.equals(name);
	}
	public boolean containsDuplicates(TeamPairing pairing)
	{
		return pairing.contains(firstTeam) || pairing.contains(secondTeam);
	}
}
