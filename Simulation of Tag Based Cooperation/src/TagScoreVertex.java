import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class TagScoreVertex extends AbstractVertex
{
	protected static double global_DonationCount = 0;
	protected static double global_PossibleDonationCount = 0;
	
	protected double tag;
	protected double tolerance;
	protected double score;
	protected Map<String, ObservationQueue> observations = new HashMap<String, ObservationQueue>();

    protected boolean isCheater = false;
	
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

    public boolean isCheater()
    {
        return isCheater;
    }

    public void setIsCheater(boolean isCheater)
    {
        this.isCheater = isCheater;
    }
	
	public void step()
	{
        // System.out.println(vertexId + " stepping");
		donate();
		learn();
	}
	
	protected void donate()
	{
        // get list of neighbours
		ArrayList<AbstractVertex> neighbours = graph.get().neighboursForVertex(vertexId);
        ArrayList<AbstractVertex> observers = graph.get().observersForVertex(vertexId);

        // if this agent is a cheater, then we do not donate
        if (isCheater)
        {
            for (AbstractVertex observer : observers)
            {
                for (int i = 0; i < neighbours.size(); i++)
                {
                    ((TagScoreVertex) observer).neighbourDidDonate(vertexId, false);
                    graph.get().agentDidDonate(vertexId, false);
                }
            }
            return;
        }

		// keep only the neighbours within tolerance value
		ArrayList<TagScoreVertex> neighboursWithinTolerance = neighboursWithinTolerance(neighbours);
		
		// donate to neighbours
		donateToNeighbours(neighboursWithinTolerance);
		
		// update all neighbours with the donation responses
		// boolean didDonate = neighboursWithinTolerance.size() > 0;
        // updateObservers(didDonate);

        int donationsMade = neighboursWithinTolerance.size();
        int donationsNotMade = neighbours.size() - donationsMade;

        for (AbstractVertex observer : observers)
        {
            for (int i = 0; i < donationsMade; i++)
            {
                ((TagScoreVertex) observer).neighbourDidDonate(vertexId, true);
                graph.get().agentDidDonate(vertexId, true);
            }

            for (int i = 0; i < donationsNotMade; i++)
            {
                ((TagScoreVertex) observer).neighbourDidDonate(vertexId, false);
                graph.get().agentDidDonate(vertexId, false);
            }
        }
	}

    protected void updateObservers(boolean didDonate)
    {
        ArrayList<AbstractVertex> observers = graph.get().observersForVertex(vertexId);

        for (AbstractVertex observer : observers)
        {
            ((TagScoreVertex) observer).neighbourDidDonate(vertexId, didDonate);
        }

        graph.get().agentDidDonate(vertexId, didDonate);
    }
	
	public void neighbourDidDonate(String neighbourId, boolean didDonate)
	{
		// get observation queue based on neighbour id
		ObservationQueue neighbourObservations = observations.get(neighbourId);
		
		if (neighbourObservations == null)
		{
            System.err.println("Error in: " + vertexId + ".neighbourDidDonate(" + neighbourId + ", " + didDonate + ")");
            for (AbstractVertex neighbour : graph.get().neighboursForVertex(vertexId))
            {
                System.out.println("neighbour = " + neighbour.vertexId());
            }
            
            for (String vid : observations.keySet())
            {
                System.out.println("observation = " + vid);
            }
            
            for (AbstractVertex neighbour : graph.get().neighboursForVertex(neighbourId))
            {
                System.out.println("am i a neighbour? = " + neighbour.vertexId());
            }
		}
		
		// add to ((Boolean) didDonate) to observation queue
		neighbourObservations.observe(didDonate);
	}
	
	protected ArrayList<TagScoreVertex> neighboursWithinTolerance(ArrayList<AbstractVertex> neighbours)
	{
		ArrayList<TagScoreVertex> neighboursWithinTolerance = new ArrayList<TagScoreVertex>();
		
		double contextInfluence = graph.get().job().contextInfluence();
		double contextAssessment = neighbourhoodAssessment();
		
		for (AbstractVertex neighbour : neighbours)
		{
			TagScoreVertex neighbourAsTSV = (TagScoreVertex) neighbour;
			
			global_PossibleDonationCount++;
			if (shouldDonateToNeighbour(neighbourAsTSV, contextInfluence, contextAssessment))
			{
				neighboursWithinTolerance.add(neighbourAsTSV);
				global_DonationCount++;
			}
			
//			if (neighbourAsTSV.isWithinTolerance(tag, tolerance))
//			{
//				neighboursWithinTolerance.add(neighbourAsTSV);
//			}
		}
		
		return neighboursWithinTolerance;
	}
	
	// | \tau_A - \tau_B | <= (1 - \gamma).T_A + \gamma.C_A
	// \tau_A = A.tag
	// \tau_B = B.tag
	// \gamma = context influence
	// T_A = A.tolerance
	// C_A = context assessment
	protected boolean shouldDonateToNeighbour(TagScoreVertex neighbour, double contextInfluence, double contextAssessment)
	{
		double difference = Math.abs(tag - neighbour.tag());
		double minDifference = ((1 - contextInfluence) * tolerance) + (contextInfluence * contextAssessment);
		
		return difference < minDifference;
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
		double probabilityOfLearning = graph.get().job().probabilityOfLearning();
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
		//double[] newTagAndTolerance = tagAndToleranceWithChanceOfMutation(0.1);
        double[] newTagAndTolerance = tagAndToleranceWithChanceOfMutation(0.001);

		tag = newTagAndTolerance[0];
		tolerance = newTagAndTolerance[1];

        AbstractMessage message = new VertexLearntMessage(this, randomAgent);
        graph.get().job().registerMessage(message);
		
		// rewire the agent's neighbourhood
        rewire();
	}
	
	protected void rewire()
	{
        int numberOfNeighbours = graph.get().neighboursForVertex(vertexId).size();
        double rewireProportion = graph.get().job().rewireProportion();
        int lambda = (int)Math.round(((double) numberOfNeighbours) * rewireProportion);

        switch (graph.get().job().rewireStrategy())
        {
            case Random:
                randomRewire(lambda);
                break;
            case RandomReplaceWorst:
                randomReplaceWorstRewire(lambda);
                break;
            case IndividualReplaceWorst:
                individualReplaceWorstRewire(lambda);
                break;
            case GroupReplaceWorst:
                groupReplaceWorseRewire(lambda);
                break;
        }
	}
	
	protected void randomRewire(int lambda)
	{
		// remove \lambda neighbours at random
		removeNeighboursAtRandom(lambda);
		
		// add \lambda neighbours at random
		addNeighboursAtRandom(lambda);
	}
	
	protected void removeNeighboursAtRandom(int count)
	{
		// get all neighbours
		ArrayList<AbstractVertex> neighbours = new ArrayList<AbstractVertex>(graph.get().neighboursForVertex(vertexId));
		
		// remove neighbours from list so that number of size <= count
		while (neighbours.size() > count)
		{
			int indexOfNeighbourToRemove = RandomGenerator.getInstance().nextIntegerLessThan(neighbours.size());
			neighbours.remove(indexOfNeighbourToRemove);
		}
		
		// remove edges to neighbours
		for (AbstractVertex neighbour : neighbours)
		{
			graph.get().removeEdge(this, neighbour);
		}
	}
	
	protected void addNeighboursAtRandom(int count)
	{
		// get all neighbours (to prevent duplicate neighbours)
		ArrayList<AbstractVertex> neighbours = graph.get().neighboursForVertex(vertexId);
		
        // check that there are enough vertices to add
        if (count > (graph.get().vertexCount() - 1) - neighbours.size())
        {
            count = (graph.get().vertexCount() - 1) - neighbours.size();
        }

		int neighboursAdded = 0;
		
		// while neighbours added < count
		// get a random vertex and attempt to add it as a neighbour
		while (neighboursAdded < count)
		{
			AbstractVertex randomAgent = graph.get().randomVertex();

			// we can't have an edge to ourselves
			if (vertexId.equals(randomAgent.vertexId()))
			{
				continue;
			}

            // we can't have duplicate edges
            if (graph.get().containsEdge(vertexId, randomAgent.vertexId()))
            {
                continue;
            }
			
			// add neighbour
			graph.get().addEdge(this, randomAgent);
			neighboursAdded++;
		}
	}
	
	protected void randomReplaceWorstRewire(int lambda)
	{
		// remove \lambda worst neighbours
		removeWorstNeighbours(lambda);
		
		// add \lambda neighbours at random
		addNeighboursAtRandom(lambda);
	}
	
	protected void removeWorstNeighbours(int count)
	{
		ArrayList<AbstractVertex> rankedNeighbours = rankNeighbours();
		
        List<AbstractVertex> worstNeighbours;
        if (count > rankedNeighbours.size())
        {
            worstNeighbours = rankedNeighbours;
        }
        else
        {
		    worstNeighbours = rankedNeighbours.subList(0, count);
        }
		
		for (AbstractVertex neighbour : worstNeighbours)
		{
			graph.get().removeEdge(this, neighbour);
		}
	}
	
	protected ArrayList<AbstractVertex> rankNeighbours()
	{
		// get neighbours
		ArrayList<AbstractVertex> neighbours = new ArrayList<AbstractVertex>(graph.get().neighboursForVertex(vertexId));
		
		// order neighbours by rank
		Collections.sort(neighbours, new RankComparator());
		
		return neighbours;
	}
	
	protected class RankComparator implements Comparator<AbstractVertex>
	{
		public int compare(AbstractVertex a, AbstractVertex b)
		{
			int objectARank = observations.get(a.vertexId()).getRank();
			int objectBRank = observations.get(b.vertexId()).getRank();
			
			int delta = objectARank - objectBRank;
			return delta;
		}
	}
	
	protected void individualReplaceWorstRewire(int lambda)
	{
		// remove \lambda worst neighbours
		removeWorstNeighbours(lambda);
		
		// add the best \lambda neighbours from the best neighbour
		TagScoreVertex bestNeighbour = bestNeighbour();
        int randomNeighboursToAdd = 0;

        List<AbstractVertex> neighboursToAdd;
        if (bestNeighbour != null)
        {
            neighboursToAdd = bestNeighbour.bestNeighbours(lambda);
        }
        else
        {
            neighboursToAdd = new ArrayList<AbstractVertex>();
            randomNeighboursToAdd = lambda;
        }

        for (AbstractVertex newNeighbour : neighboursToAdd)
        {
            // we can't have duplicate edges
            if (graph.get().containsEdge(vertexId, newNeighbour.vertexId()))
            {
                randomNeighboursToAdd++;
                continue;
            }

            // we can't have an edge to ourselves
            if (vertexId.equals(newNeighbour.vertexId()))
            {
                randomNeighboursToAdd++;
                continue;
            }

            graph.get().addEdge(this, newNeighbour);
        }

        while (randomNeighboursToAdd > 0)
        {
            AbstractVertex newNeighbour = graph.get().randomVertex();

            // we can't have duplicate edges
            if (graph.get().containsEdge(vertexId, newNeighbour.vertexId()))
            {
                continue;
            }

            // we can't have an edge to ourselves
            if (vertexId.equals(newNeighbour.vertexId()))
            {
                continue;
            }

            graph.get().addEdge(this, newNeighbour);
            randomNeighboursToAdd--;
        }
	}
	
	protected void groupReplaceWorseRewire(int lambda)
	{
		// remove \lambda worst neighbours
		removeWorstNeighbours(lambda);
		
		// add the best neighbour of each of the best \lambda neighbours
        List<AbstractVertex> bestNeighbours = bestNeighbours(lambda);
        List<AbstractVertex> chosenNeighbours = new ArrayList<AbstractVertex>();

        List<AbstractVertex> currentNeighbours = graph.get().neighboursForVertex(vertexId);
        int randomNeighboursToAdd = 0;

        for (AbstractVertex bestNeighbour : bestNeighbours)
        {
            // we can't have duplicate edges
            if (graph.get().containsEdge(vertexId, bestNeighbour.vertexId()))
            {
                randomNeighboursToAdd++;
                continue;
            }

            // we can't have an edge to ourselves
            if (vertexId.equals(bestNeighbour.vertexId()))
            {
                randomNeighboursToAdd++;
                continue;
            }

            chosenNeighbours.add(((TagScoreVertex) bestNeighbour).bestNeighbour());
        }

        for (AbstractVertex newNeighbour : chosenNeighbours)
        {
            graph.get().addEdge(this, newNeighbour);
        }

        currentNeighbours = graph.get().neighboursForVertex(vertexId);
        while (randomNeighboursToAdd > 0)
        {
            AbstractVertex newNeighbour = graph.get().randomVertex();

            // we can't have duplicate edges
            if (graph.get().containsEdge(vertexId, newNeighbour.vertexId()))
            {
                continue;
            }

            // we can't have an edge to ourselved
            if (vertexId.equals(newNeighbour.vertexId()))
            {
                continue;
            }

            graph.get().addEdge(this, newNeighbour);
            randomNeighboursToAdd--;
        }

	}
	
	public TagScoreVertex bestNeighbour()
	{		
		List<AbstractVertex> bestNeighbourList = bestNeighbours(1);
        
        if (bestNeighbourList.size() == 0)
        {
            return null;
        }
		
		return (TagScoreVertex)bestNeighbourList.get(0);
	}
	
	public List<AbstractVertex> bestNeighbours(int count)
	{
		ArrayList<AbstractVertex> rankedNeighbours = rankNeighbours();

        if (count > rankedNeighbours.size())
        {
            count = rankedNeighbours.size();
        }
		
		List<AbstractVertex> bestNeighbours = rankedNeighbours.subList(rankedNeighbours.size() - count, rankedNeighbours.size());
		
		return bestNeighbours;
	}
	
	public List<AbstractVertex> bestNeighboursExcluding(int count, ArrayList<AbstractVertex> exclude)
	{
		return null;
	}
	
	protected double neighbourhoodAssessment()
	{
		double neighbourhoodAssessment = 0;
		
		if (observations.size() == 0)
		{
			return neighbourhoodAssessment;
		}
		
		for (Map.Entry<String, ObservationQueue> neighbourObservation : observations.entrySet())
		{
            if (neighbourObservation.getValue().getObservationCount() == 0)
            {
                //System.out.println("Cheating");
                neighbourhoodAssessment += 0.5;
            }

			neighbourhoodAssessment += neighbourObservation.getValue().contextAssessment();
		}

        // System.out.println("Neighbourhood assessment: " + neighbourhoodAssessment);
		neighbourhoodAssessment /= observations.size();
		
        // System.out.println("Neighbourhood assessment: " + neighbourhoodAssessment);
		return neighbourhoodAssessment;
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
//			System.out.println("mutated tag from " + tag + " to " + tagAndTolerance[0]);
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
			
//			System.out.println("mutated tolerance from " + tolerance + " to " + tagAndTolerance[1]);
		}
		
		return tagAndTolerance;
	}
	
	public void neighbourWasAdded(String neighbourId)
	{
        // System.out.println(vertexId + " adding neighbour " + neighbourId);
		// add an observation queue for neighbour
		observations.put(neighbourId, new ObservationQueue());
	}
	
	public void neighbourWasRemoved(String neighbourId)
	{
        // System.out.println(vertexId + " removing neighbour " + neighbourId);
		// remove the observation queue for neighbour
		observations.remove(neighbourId);
	}
}
