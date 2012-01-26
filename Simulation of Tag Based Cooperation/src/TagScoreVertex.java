import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class TagScoreVertex extends AbstractVertex
{
	protected double tag;
	protected double tolerance;
	protected double score;
	protected Map<String, ObservationQueue> observations = new HashMap<String, ObservationQueue>();
	
	public TagScoreVertex(AbstractGraph graph, double tag, double tolerance)
	{
		super(graph);
		this.tag = tag;
		this.tolerance = tolerance;
	}
	
	public TagScoreVertex(AbstractGraph graph, String vertexId, double tag, double tolerance)
	{
		super(graph, vertexId);
		this.tag = tag;
		this.tolerance = tolerance;
	}
	
	public double tag()
	{
		return tag;
	}
	
	public double tolerance()
	{
		return tolerance;
	}
	
	public double score()
	{
		return score;
	}
	
	public String toString()
	{
		return String.format("tag = %1.5f; tolerance = %1.5f", tag, tolerance);
	}
	
	public void step()
	{
		System.out.format("Agent %s stepping\n", vertexId);
		donate();
		learn();
	}
	
	protected void donate()
	{
		// get list of neighbours
		ArrayList<AbstractVertex> neighbours = graph.get().neighboursForVertex(vertexId);
		
		// keep only the neighbours within tolerance value
		ArrayList<TagScoreVertex> neighboursWithinTolerance = neighboursWithinTolerance(neighbours);
		
		// donate to neighbours
		donateToNeighbours(neighboursWithinTolerance);
		
		// update all neighbours with the donation responses
		boolean didDonate = neighboursWithinTolerance.size() > 0;
		
		for (AbstractVertex neighbour : neighbours)
		{
			((TagScoreVertex) neighbour).neighbourDidDonate(vertexId, didDonate);
		}
	}
	
	public void neighbourDidDonate(String neighbourId, boolean didDonate)
	{
		// get observation queue based on neighbour id
		ObservationQueue neighbourObservations = observations.get(neighbourId);
		
		// add to ((Boolean) didDonate) to observation queue
		neighbourObservations.observe(didDonate);
	}
	
	protected ArrayList<TagScoreVertex> neighboursWithinTolerance(ArrayList<AbstractVertex> neighbours)
	{
		ArrayList<TagScoreVertex> neighboursWithinTolerance = new ArrayList<TagScoreVertex>();
		
		for (AbstractVertex neighbour : neighbours)
		{
			TagScoreVertex neighbourAsTSV = (TagScoreVertex) neighbour;
			
			if (neighbourAsTSV.isWithinTolerance(tag, tolerance))
			{
				neighboursWithinTolerance.add(neighbourAsTSV);
			}
		}
		
		return neighboursWithinTolerance;
	}
	
	public boolean isWithinTolerance(double otherTag, double otherTolerance)
	{
		return Math.abs(tag - otherTag) < otherTolerance;
	}
	
	protected void donateToNeighbours(ArrayList<TagScoreVertex> neighbours)
	{
		for (TagScoreVertex neighbour : neighbours)
		{
			neighbour.receiveDonation(this);
			score = score - 0.1;
		}
	}
	
	public void receiveDonation(TagScoreVertex donator)
	{
		// increase success
		score = score + 1;
	}
	
	protected void learn()
	{
		// determine if we should learn or not
		double probabilityOfLearning = 0.1;
		boolean shouldLearn = RandomGenerator.getInstance().booleanWithProbability(probabilityOfLearning);
		
		if (!shouldLearn)
		{
			return;
		}
		
		// pick another agent at random
		TagScoreVertex randomAgent = (TagScoreVertex) graph.get().randomVertex();
		
		// if current agent is more successful than the random agent, return
		if (randomAgent.score() <= score)
		{
			return;
		}
		
		// learn from the more successful agent (with chance of mutation)
		double[] newTagAndTolerance = tagAndToleranceWithChanceOfMutation(0.1);
		tag = newTagAndTolerance[0];
		tolerance = newTagAndTolerance[1];
		
		// rewire the agent's neighbourhood
		//     rewire()
	}
	
	public double[] tagAndToleranceWithChanceOfMutation(double mutationProbability)
	{
		double[] tagAndTolerance = new double[2];
		tagAndTolerance[0] = tag;
		tagAndTolerance[1] = tolerance;
		
		// should we mutate the tag?
		if (RandomGenerator.getInstance().booleanWithProbability(mutationProbability))
		{
			// mutate tag by generating a new tag
			tagAndTolerance[0] = RandomGenerator.getInstance().nextDouble();
			System.out.println("mutated tag from " + tag + " to " + tagAndTolerance[0]);
		}
		
		// should we mutate the tolerance
		if (RandomGenerator.getInstance().booleanWithProbability(mutationProbability))
		{
			// mutate tolerance by adding gaussian noise with 0 mean, and small stddev
			double standardDeviation = 0.05;
			double toleranceDelta = RandomGenerator.getInstance().nextGaussian();
			toleranceDelta *= standardDeviation;
			
			tagAndTolerance[1] += toleranceDelta;
			
			// clamp tolerance value to range [0, 1]
			if (tagAndTolerance[1] < 0)
			{
				tagAndTolerance[1] = 0;
			}
			
			if (tagAndTolerance[1] > 1)
			{
				tagAndTolerance[1] = 1;
			}
			
			System.out.println("mutated tolerance from " + tolerance + " to " + tagAndTolerance[1]);
		}
		
		return tagAndTolerance;
	}
	
	public void neighbourWasAdded(String neighbourId)
	{
		// add an observation queue for neighbour
		observations.put(neighbourId, new ObservationQueue());
	}
	
	public void neighbourWasRemoved(String neighbourId)
	{
		// remove the observation queue for neighbour
		observations.remove(neighbourId);
	}
}