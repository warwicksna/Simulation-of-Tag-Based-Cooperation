import java.io.FileReader;

import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;

public class CooperationSimulation
{
	protected AbstractGraph graph;
	
	public static void main(String args[])
	{		
        for (double contextInfluence = 0; contextInfluence <= 1; contextInfluence += 0.1)
        {
            Job job = new Job();

            job.setRewireStrategy(RewireStrategy.None);
            job.setNumberOfPairings(NumberOfPairings.Pairings10);
            job.setPopulationSize(PopulationSize.Population100);
            job.setIterationCount(1000);
            job.setContextInfluence(contextInfluence);
            job.setProbabilityOfLearning(0.1);
            job.setRewireProportion(0.6);

            System.out.print("Running job: " + job);
            
            job.run(10);

            System.out.println("Donation rate: " + job.averageDonationRate() + "\n");
        }
	}

	protected void outputGraphToConsole()
	{
		System.out.println("List of vertices");
		graph.listVertices();
		System.out.println("List of edges for each vertex");
		graph.listEdges();
	}
}
