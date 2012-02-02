import java.util.LinkedList;

public class ObservationQueue
{
	protected LinkedList<Boolean> observations = new LinkedList<Boolean>();
	protected int maxSize;
	protected int rank;
	
	public ObservationQueue(int size)
	{
		maxSize = size;
		rank = 0;
	}
	
	public ObservationQueue() {
		// TODO Auto-generated constructor stub
		maxSize = 10;
		rank = 0;
        observe(true);
        observe(false);
        observe(true);
        observe(false);
        observe(true);
        observe(false);
        observe(true);
        observe(false);
        observe(true);
        observe(false);
	}

	public void observe(boolean didDonate)
	{
		if (observations.size() >= maxSize)
		{
			Boolean value = observations.poll();
			rank = (value) ? rank - 1 : rank + 1;
		}
		
		observations.offer(new Boolean(didDonate));
		rank = (didDonate) ? rank + 1 : rank - 1;

        // if (didDonate)
        // {
        //     System.out.println("observing +");
        // }
        // else
        // {
        //     System.out.println("observing -");
        // }
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
		return rank;
	}
	
	public int getObservationCount()
	{
		return observations.size();
	}
	
	public double contextAssessment()
	{
		double contextAssessment = 0;
		
		if (observations.size() == 0)
		{
			return contextAssessment;
		}
		
		for (Boolean observation : observations)
		{
			if (observation.booleanValue())
			{
				contextAssessment++;
			}
		}
		
		contextAssessment /= observations.size();
		
		return contextAssessment;
	}
}
