import java.util.LinkedList;

public class ObservationQueue
{
	protected LinkedList<Boolean> observations = new LinkedList<Boolean>();
	protected int maxSize;
	
	public ObservationQueue(int size)
	{
		maxSize = size;
	}
	
	public void observe(boolean didDonate)
	{
		if (observations.size() >= maxSize)
		{
			observations.remove();
		}
		
		observations.offer(new Boolean(didDonate));
	}
	
	public String toString()
	{
		String observationString = "";
		
		for (Boolean observation : observations)
		{
			if (observation.booleanValue())
			{
				observationString += " +";
			}
			else
			{
				observationString += " -";
			}
		}
		
		return observationString;
	}
	
	public int getRank()
	{
		int rank = 0;
		
		for (Boolean observation : observations)
		{
			if (observation.booleanValue())
			{
				rank++;
			}
			else
			{
				rank--;
			}
		}
		
		return rank;
	}
	
	public int getObservationCount()
	{
		return observations.size();
	}
}