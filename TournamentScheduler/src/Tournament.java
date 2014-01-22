import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Tournament
{
	protected ArrayList<String> teamNames;
	protected ArrayList<VenueDistance> venueDistances;
	protected Schedule schedule;
	
	private ArrayList<TeamPairing> teamPairings;
	private double maxDistance = 0;
	
	public Tournament(String teams, String times, String distances)
	{
		schedule = new Schedule();
		try {
			teamNames = Configuration.readTeamsFromFile(teams);
			schedule.addTimeSlots(Configuration.readTimesFromFile(times));
			venueDistances = Configuration.readDistancesFromFile(distances);
		} catch (IOException e) {
			System.err.println("Error reading file.");
			System.exit(0);
		}
		for(int i=0;i<venueDistances.size();i++)
		{
			if(venueDistances.get(i).getDistance()> maxDistance)
				maxDistance = venueDistances.get(i).getDistance();
		}
	}
	/**
	 * Creates a schedule for a tournament through the use of a genetic algorithm
	 * @param initialPopulationSize
	 * @param survivalPercentage
	 * @param mutatePercentage
	 * @param numGenerations
	 */
	public void createSchedule(int initialPopulationSize, int survivalPercentage, int mutatePercentage, int numGenerations)
	{
		int iterations = 0;
		//get a list of all games that must be played in the tournament
		teamPairings = pairTeams(teamNames);
		
		if(schedule.getNumTimeSlots()< teamPairings.size())
		{
			System.out.println("Not enough time slots to play the required amount of games");
			return;
		}
		ArrayList<Sequence> generation = new ArrayList<Sequence>();
		for(int i=0;i<initialPopulationSize;i++)
		{
			generation.add(new Sequence(getRandomSequence(teamPairings.size())));
		}
		while(iterations<numGenerations && generation.size()>1)
		{

			double maxFitness = 0;
			double avgFitness = 0;
			
			double totalFitness = 0;
			double minFitness = -1;
			//set the fitness for each sequence
			for(int i=0;i<generation.size();i++)
			{
				assignFitness(generation.get(i));
				double fitness = generation.get(i).getFitness();
				avgFitness+= fitness;
				if(fitness>maxFitness)
					maxFitness = fitness;
				if(minFitness == -1 || fitness <minFitness)
					minFitness = fitness;
			}
			//adjust fitness of each as relative
			for(int i=0;i<generation.size();i++)
			{
				double fitness = generation.get(i).getFitness() - minFitness;
				totalFitness += fitness;
				generation.get(i).setFitness(fitness);
			}
			
			//choose successors
			ArrayList<Sequence> survivors = new ArrayList<Sequence>();
			int numSurvivors = (int)((double)survivalPercentage*0.01 *generation.size());
			System.out.println("Current generation size: "+generation.size()+" average fitness: "+ avgFitness/generation.size()+" maximum fitness: "+ maxFitness);
			Random r = new Random();
			for(int i=0;i<numSurvivors;i++)
			{
				double survivorValue = r.nextDouble();
				double count = 0;
				for(int j=0;j<generation.size();j++)
				{
					count+= generation.get(j).getFitness()/totalFitness;
					if(survivorValue <= count)
					{
						survivors.add(generation.get(j));
						break;
					}
				}
			}
			//generate second population
			ArrayList<Sequence> nextGeneration = new ArrayList<Sequence>();
			//add to second population from mutation
			int numMutations = (int)((double)mutatePercentage*0.01 *survivors.size());
			for(int i=0; i<numMutations;i++)
			{
				nextGeneration.add(mutation(survivors.get(r.nextInt(survivors.size()))));
			}
			for(int i=0;i<generation.size()-numMutations;i+=2)
			{
				Sequence[] children= orderCrossover(survivors.get(r.nextInt(survivors.size())), survivors.get(r.nextInt(survivors.size())));
				nextGeneration.add(children[0]);
				nextGeneration.add(children[1]);
			}
			
			//determine end state
			iterations++;
			//breed another generation
			generation = nextGeneration;
		}
		
		// find the best sequence in the final generation
		Sequence bestSequence = null;
		
		double maxFitness = 0;
		for(int i=0;i<generation.size();i++)
		{
			Sequence curr = generation.get(i);
			assignFitness(curr);
			double fitness = curr.getFitness();
			if(fitness>maxFitness && isValidSequence(curr))
			{
				bestSequence = curr;
				maxFitness = fitness;
			}
		}
		if(bestSequence == null)
		{
			System.out.println("No valid solution found.");
		}
		else
		{
			assignFitness(bestSequence);
			System.out.println("Fitness: "+bestSequence.getFitness()+ "\nDistance Traveled: " +bestSequence.getDistance()+ "\nSequence: "+bestSequence.toString() );
			setTeams(bestSequence);
			System.out.println(schedule.toString());
		}
		
	}
	private Sequence[] orderCrossover(Sequence p1 ,Sequence p2)
	{
		//create cutoffs for both parents at the same place
		p1.createCutoffs();
		p2.setCutoffs(p1);
		
		//segment p1 sequence into its 3 parts
		ArrayList<Integer> leftSegment1 = p1.getLeftSegment();
		ArrayList<Integer> middleSegment1 = p1.getMiddleSegment();
		ArrayList<Integer> rightSegment1 = p1.getRightSegment();
		//segment p2 sequence into its 3 parts
		ArrayList<Integer> leftSegment2 = p2.getLeftSegment();
		ArrayList<Integer> middleSegment2 = p2.getMiddleSegment();
		ArrayList<Integer> rightSegment2 = p2.getRightSegment();
		
		//concatenate p1 in the order of RLM
		ArrayList<Integer> concatSegment1 = new ArrayList<Integer>();
		concatSegment1.addAll(rightSegment1);
		concatSegment1.addAll(leftSegment1);
		concatSegment1.addAll(middleSegment1);
		//concatenate p2 in the order of RLM
		ArrayList<Integer> concatSegment2 = new ArrayList<Integer>();
		concatSegment2.addAll(rightSegment2);
		concatSegment2.addAll(leftSegment2);
		concatSegment2.addAll(middleSegment2);
		
		//remove existing numbers in c1 that match the middle part of segment2
		concatSegment1.removeAll(middleSegment2);
		//remove existing numbers in c2 that match the middle part of segment1
		concatSegment2.removeAll(middleSegment1);
		
		//add the middle segments of p2 back into the sequence c1
		concatSegment1.addAll(p1.getLeftCutoff(), middleSegment2);
		//add the middle segments of p1 back into the sequence c2
		concatSegment2.addAll(p1.getLeftCutoff(), middleSegment1);
		
		Sequence[] children = new Sequence[2];
		children[0] = new Sequence(concatSegment1);
		children[1] = new Sequence(concatSegment2);
		
		return children;
	}
	private Sequence mutation(Sequence p)
	{
		p.createCutoffs();
		
		ArrayList<Integer> leftSegment = p.getLeftSegment();
		ArrayList<Integer> middleSegment = reverse(p.getMiddleSegment());
		ArrayList<Integer> rightSegment = p.getRightSegment();
		
		ArrayList<Integer> segments = new ArrayList<Integer>();
		
		//add non mutated left segment to the child
		segments.addAll(leftSegment);
		//add mutated middle segment to the child, we mutate it by inverting order
		segments.addAll(middleSegment);
		//add non mutated right segment to the child
		segments.addAll(rightSegment);
		
		return new Sequence(segments);
	}
	private void assignFitness(Sequence p)
	{
		double maxTotalDistance = (maxDistance * (teamNames.size()-1)* teamNames.size()) ;
		double distanceTraveled = this.findTotalDistanceTraveled(p);
		p.setDistance(distanceTraveled);
		if(isValidSequence(p))
		{
			p.setFitness(maxTotalDistance- distanceTraveled);
		}
		p.setFitness((maxTotalDistance- distanceTraveled)/2);
	}
	
	private ArrayList<Integer> reverse(ArrayList<Integer> p)
	{
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		for(int i =0;i<p.size();i++)
		{
			ret.add(p.get(p.size()-1-i));
		}
		return ret;
	}
	private ArrayList<TeamPairing> pairTeams(ArrayList<String> teams)
	{
		ArrayList<TeamPairing> pairs = new ArrayList<TeamPairing>();
		
		for(int i=0;i<teams.size();i++)
		{
			for(int j=i+1;j<teams.size();j++)
			{
				pairs.add(new TeamPairing(teams.get(i), teams.get(j)));
			}
		}
		return pairs;
	}
	private ArrayList<Integer> getRandomSequence(int size)
	{
		ArrayList<Integer> pool = new ArrayList<Integer>();
		for(int i=0;i<size;i++)
		{
			pool.add(new Integer(i));
		}
		ArrayList<Integer> ret = new ArrayList<Integer>();
		Random r = new Random();
		//selects and removes a random integer from pool and adds it to the return list
		for(int i=0;i<size;i++)
		{
			int index = r.nextInt(pool.size());
			ret.add(pool.get(index));
			pool.remove(index);
		}
		return ret;
	}
	private double calculateDistance(String venue1, String venue2)
	{
		for(int i=0;i<venueDistances.size();i++)
		{
			if(venueDistances.get(i).contains(venue1, venue2))
				return venueDistances.get(i).getDistance();
		}
		return 0;
	}
	private boolean isValidSequence(Sequence s)
	{
		setTeams(s);
		if(schedule.isValid())
		{
			clearTeams();
			return true;
		}
		clearTeams();
		return false;
	}
	private void setTeams(Sequence s)
	{
		for(int i=0; i<s.getSize();i++)
		{
			schedule.setTeams(i, teamPairings.get(s.get(i)));
		}
	}
	private void clearTeams()
	{
		for(int i=0; i<schedule.getNumTimeSlots();i++)
		{
			schedule.setTeams(i, null);
		}
	}
	private double findTotalDistanceTraveled(Sequence s)
	{
		double totalDistance = 0;
		for(int i=0;i<teamNames.size();i++)
		{
			String teamName = teamNames.get(i);
			String lastVenue = null;
			for(int j=0;j<s.getSize();j++)
			{
				int curr = s.get(j);
				if(teamPairings.get(curr).contains(teamName))
				{
					String currentVenue = schedule.getTimeSlot(j).getVenueName();
					if(lastVenue ==null)
					{
						lastVenue = currentVenue;
					}
					else if(!lastVenue.equals(currentVenue))
					{
						totalDistance += calculateDistance(lastVenue, currentVenue);
					}
				}
			}
		}
		return totalDistance;
	}

}
