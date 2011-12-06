import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Agent
{
	// Node details
	protected ArrayList<Agent> neighbours = new ArrayList<Agent>();
	protected static int lastAgentId = 0;
	protected int agentId = 0;
	
	// Edge limited nodes
	protected int maxEdgeCount = 10;
	
	// Agent details
	protected double tag = 0.0;
	protected double tolerance = 0.0;
	protected double score = 0.0;
	protected double learningRate = 0.0;
	
	// observations
	protected HashMap<Agent, ObservationQueue> neighbourhoodObservations = new HashMap<Agent, ObservationQueue>();
	
	// Constants
	protected static final Random randomNumberGenerator = new Random();
	protected static final double kDonationCost = 0.1;
	protected static final double kReceipientBenefit = 1;
	protected static final double kContextInfluence = 0.2;

	public Agent()
	{
		initialise();
	}
	
	public Agent(double tag, double tolerance, double score, double learningRate)
	{
		initialise(tag, tolerance, score, learningRate);
	}
	
	protected void initialise()
	{	
		double randomTag = randomNumberGenerator.nextDouble();
		double randomTolerance = randomNumberGenerator.nextDouble();
		double randomScore = randomNumberGenerator.nextDouble() * 100;
		double randomLearningRate = randomNumberGenerator.nextDouble();
		
		initialise(randomTag, randomTolerance, randomScore, randomLearningRate);
	}
	
	protected void initialise(double tag, double tolerance, double score, double learningRate)
	{
		agentId = lastAgentId++;
		maxEdgeCount = 5;
		
		this.tag = tag;
		this.tolerance = tolerance;
		this.score = score;
		this.learningRate = learningRate;
	}
	
	public int getAgentId()
	{
		return agentId;
	}
	
	// Node methods
	public int getNeighbourCount()
	{
		return neighbours.size();
	}
	
	public boolean canAddEdgeTo(Agent neighbour)
	{
		if (getNeighbourCount() >= maxEdgeCount)
		{
			return false;
		}
		
		if (this == neighbour)
		{
			return false;
		}
		
		return !neighbours.contains(neighbour);
	}
	
	public void addEdgeTo(Agent neighbour)
	{
		if (!canAddEdgeTo(neighbour))
		{
			System.err.println("Cannot add edge to " + neighbour + " from " + this);
		}
		
		neighbours.add(neighbour);
	}
	
	public void removeEdgeTo(Agent neighbour)
	{
		if (!neighbours.contains(neighbour))
		{
			System.err.println("Trying to remove a non-existant edge");
			return;
		}
		
		neighbours.remove(neighbour);
	}
	
	// Edge limited methods
	
	public boolean hasAvailableEdges()
	{
		return getNeighbourCount() < maxEdgeCount;
	}
	
	// Agent methods
	
	public double getTag()
	{
		return tag;
	}
	
	public double getTagWithProbabilityOfMutation(double probabilityOfMutation)
	{
		double chance = randomNumberGenerator.nextDouble();
		
		if (chance > probabilityOfMutation)
		{
			return tag;
		}
		
		return mutatedTag();
	}
	
	protected double mutatedTag()
	{
		return randomNumberGenerator.nextDouble();
	}
	
	public double getTolerance()
	{
		return tolerance;
	}
	
	public double getToleranceWithProbabilityOfMutation(double probabilityOfMutation)
	{
		double chance = randomNumberGenerator.nextDouble();
		
		if (chance > probabilityOfMutation)
		{
			return tolerance;
		}
		
		return mutatedTolerance();
	}
	
	public double mutatedTolerance()
	{
		double change = 0;
		double mutatedTolerance = 0;
		
		do
		{
			change = randomNumberGenerator.nextGaussian();
			change *= 0.4; // adjust the stddev
			mutatedTolerance = tolerance + change;	
		} while (mutatedTolerance < 0 || mutatedTolerance > 0);
		
		return mutatedTolerance;
	}
	
	public double getScore()
	{
		return score;
	}
	
	public boolean canToDonate()
	{
		return score >= kDonationCost;
	}
	
	public void donate(Agent neighbour)
	{
		if (score < kDonationCost)
		{
			System.err.println("Unable to decrement score, too low");
			return;
		}
		
		System.out.println("\tAgent " + getAgentId() + " is donating to Agent " + neighbour.getAgentId());
		
		score -= kDonationCost;
		
		neighbour.receive();
	}
	
	private void receive()
	{
		score += kReceipientBenefit;
	}

	public String toString()
	{
		String stringValue;
		stringValue = String.format("Agent #%3d (%.2f %.2f %3d) neighbours = (", agentId, getTag(), getTolerance(), Math.round(getScore()));
		
		for (Agent neighbour : neighbours)
		{
			stringValue += String.format(" Agent (%.2f %.2f)",
					((Agent) neighbour).getTag(),
					((Agent) neighbour).getTolerance());
		}
		
		stringValue += String.format(" )");
		return stringValue;
	}
	
	public void step(Graph graph)
	{
		System.out.format("Agent %d is stepping\n", getAgentId());
		donateStep(2);
		learnStep(graph);
	}
	
	protected void donateStep(int donationCount)
	{
		ArrayList<Agent> neighboursToDonateTo = (ArrayList<Agent>) neighbours.clone();
		
		for (int i = neighboursToDonateTo.size(); i > donationCount; i--)
		{
			int neighbourToRemove = randomNumberGenerator.nextInt(neighboursToDonateTo.size());
			neighboursToDonateTo.remove(neighbourToRemove);
		}
		
		if (neighboursToDonateTo.size() != donationCount)
		{
			System.err.println("wrong number of neighbours to donate to");
		}
		
		for (Agent neighbour : neighboursToDonateTo)
		{
			if (shouldDonateToNeighbour(neighbour))
			{
				donate(neighbour);
			}
		}
	}
	
	protected boolean shouldDonateToNeighbour(Agent neighbour)
	{
		double neighboursTag = neighbour.getTag();
		double neighbourhoodAssessment = getNeighbourhoodAssessment();
		
		double tagDifference = Math.abs(getTag() - neighboursTag);
		double newTolerance = (1.0 - kContextInfluence) * getTolerance();
		double neighbourhoodTolerance = kContextInfluence * neighbourhoodAssessment;
		
		return tagDifference < (newTolerance + neighbourhoodTolerance);
	}
	
	protected double getNeighbourhoodAssessment()
	{
		if (neighbours.size() == 0)
		{
			return 0;
		}
		
		double neighbourhoodAssessment = 0.0;
		
		for (Agent neighbour : neighbours)
		{
			neighbourhoodAssessment += getContextAssessmentForNeighbour(neighbour);
		}
		
		neighbourhoodAssessment /= neighbours.size();
		
		return neighbourhoodAssessment;
	}
	
	protected double getContextAssessmentForNeighbour(Agent neighbour)
	{
		ObservationQueue observations = neighbourhoodObservations.get(neighbour);
		if (observations == null)
		{
			return 0.0;
		}
		
		int observationCount = observations.getObservationCount();
		if (observationCount == 0)
		{
			return 0.0;
		}
		
		double changeComponent = observations.getRank();
		changeComponent /= observationCount;
		changeComponent = (changeComponent < 0) ? 0 : changeComponent;

		return changeComponent;
	}
	
	protected void learnStep(Graph graph)
	{
		double learnChance = randomNumberGenerator.nextDouble();
		
		if (learnChance > learningRate)
		{
			return;
		}
		
		Agent randomAgent = null;
		
		do
		{
			randomAgent = graph.getRandomAgent();
		} while (randomAgent == this);
		
		if (randomAgent.getScore() > getScore())
		{
			double unmutatedTag = randomAgent.getTag();
			double unmutatedTolerance = randomAgent.getTolerance();
			
			double newTag = randomAgent.getTagWithProbabilityOfMutation(0.001);
			double newTolerance = randomAgent.getToleranceWithProbabilityOfMutation(0.001);
			
			long currentScore = Math.round(getScore());
			long otherScore = Math.round(randomAgent.getScore());
			
			System.out.format("\tAgent %d (%d) is learning from Agent %d (%d)\n", getAgentId(), currentScore, randomAgent.getAgentId(), otherScore);
			if (unmutatedTag == newTag)
			{
				System.out.format("\t  - tag:       %.2f => %.2f\n", tag, newTag);
			}
			else
			{
				System.out.format("\t  - tag:       %.2f => %.2f (mutated from %.2f)\n", tag, newTag, unmutatedTag);
			}
			
			if (unmutatedTolerance == newTolerance)
			{
				System.out.format("\t  - tolerance: %.2f => %.2f\n", tolerance, newTolerance);
			}
			else
			{
				System.out.format("\t  - tolerance: %.2f => %.2f (mutated from %.2f)\n", tolerance, newTolerance, unmutatedTolerance);
			}
			
			tag = newTag;
			tolerance = newTolerance;
			
			// rewire
		}
	}
	
}
