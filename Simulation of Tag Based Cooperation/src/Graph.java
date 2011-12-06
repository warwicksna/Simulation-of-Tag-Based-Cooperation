import java.util.ArrayList;
import java.util.Random;

public class Graph {

	protected ArrayList<Agent> nodes = new ArrayList<Agent>();
	protected static Random randomNumberGenerator = new Random();
	
	public Graph()
	{
	}
	
	public void createAgents(int agentCount)
	{
		for (int i = 0; i < agentCount; i++)
		{
			Agent agent = new Agent();
			nodes.add(agent);
		}
	}
	
	public void createNeighbours()
	{
		int neighbourIndex = 0;
		Agent agent = null;
		Agent neighbour = null;
		
		int iterationCount = 0;
		
		for (int i = 0; i < nodes.size(); i++)
		{
			agent = nodes.get(i);
			
			while(agent.hasAvailableEdges())
			{
				do
				{
					neighbourIndex = randomNumberGenerator.nextInt(nodes.size());
					neighbour = nodes.get(neighbourIndex);
					iterationCount++;
					if (iterationCount > 10000000)
					{
						neighbour = null;
						break;
					}
				} while ((neighbourIndex == i) ||
					(!agent.canAddEdgeTo(neighbour)) ||
					(!neighbour.canAddEdgeTo(agent)));
				
				if (neighbour == null)
				{
					break;
				}
				
				agent.addEdgeTo(neighbour);
				neighbour.addEdgeTo(agent);
			}
		}
	}

	
	public void listAgents()
	{
		for (Agent agent : nodes)
		{
			System.out.println(agent);
		}
	}
	
	public void play(int steps)
	{
		for (int i = 0; i < steps; i++)
		{
			int randomAgentIndex = randomNumberGenerator.nextInt(nodes.size());
			Agent randomAgent = nodes.get(randomAgentIndex);
			randomAgent.step(this);
		}
	}
	
	public Agent getRandomAgent()
	{
		int neighbourIndex = randomNumberGenerator.nextInt(nodes.size());
		return nodes.get(neighbourIndex);
	}
	
	public void printObservations()
	{
		for (Agent agent : nodes)
		{
			agent.printObservations();
		}
	}
	
}
