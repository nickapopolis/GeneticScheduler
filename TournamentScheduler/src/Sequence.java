import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Sequence
{
	protected ArrayList<Integer> seq;
	protected int leftCutoff = -1;
	protected int rightCutoff = -1;
	protected double fitness;
	protected double distance;
	
	public Sequence(ArrayList<Integer> s)
	{
		seq = s;
	}
	public void createCutoffs()
	{
		Random r = new Random();
		
		int firstCutoff = r.nextInt(seq.size()+1);
		int secondCutoff = r.nextInt(seq.size()+1);
		
		//in case the cutoffs are at the same point, no mutation occurs
		while(secondCutoff == firstCutoff)
		{
			secondCutoff = r.nextInt(seq.size()+1);
		}
		
		leftCutoff = (int)Math.min(firstCutoff, secondCutoff);
		rightCutoff = (int)Math.max(firstCutoff, secondCutoff);
	}
	public void setCutoffs(Sequence s)
	{
		leftCutoff = s.leftCutoff;
		rightCutoff = s.rightCutoff;
	}
	public void setCutoffs(int left, int right)
	{
		leftCutoff = left;
		rightCutoff = right;
	}
	public ArrayList<Integer> getLeftSegment()
	{
		List lst = seq.subList(0, leftCutoff);
		
		ArrayList<Integer> ret = new ArrayList<Integer>();
		for(int i=0; i< lst.size() ; i++)
		{
			Integer num = (Integer) lst.get(i);
			ret.add(new Integer(num.intValue()));
		}
		return ret;
	}
	public ArrayList<Integer> getMiddleSegment()
	{
		List lst = seq.subList(leftCutoff, rightCutoff);
		
		ArrayList<Integer> ret = new ArrayList<Integer>();
		for(int i=0; i< lst.size() ; i++)
		{
			Integer num = (Integer) lst.get(i);
			ret.add(new Integer(num.intValue()));
		}
		return ret;
	}
	public ArrayList<Integer> getRightSegment()
	{
		List lst = seq.subList(rightCutoff, seq.size());
		
		ArrayList<Integer> ret = new ArrayList<Integer>();
		for(int i=0; i< lst.size() ; i++)
		{
			Integer num = (Integer) lst.get(i);
			ret.add(new Integer(num.intValue()));
		}
		return ret;
	}
	public String toString()
	{
		String ret = "";
		for(int i=0;i<seq.size();i++)
		{
			ret+=seq.get(i);
			if(i!=seq.size()-1)
			{
				ret+=",";
			}
		}
		return ret;
	}
	public int getLeftCutoff()
	{
		return leftCutoff;
	}
	public double getFitness()
	{
		return fitness;
	}
	public void setFitness(double f)
	{
		fitness = f;
	}
	public double getDistance()
	{
		return distance;
	}
	public void setDistance(double d)
	{
		distance = d;
	}
	public int getSize()
	{
		return seq.size();
	}
	public Integer get(int i)
	{
		return seq.get(i);
	}
}
