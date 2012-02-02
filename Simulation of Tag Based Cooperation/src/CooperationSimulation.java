import java.io.FileReader;

import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;

public class CooperationSimulation
{
	protected AbstractGraph graph;
	
	public static void main(String args[])
	{		
        // create a new job
        Job job = new Job();

        // setup the jobs parameters
        job.setRewireStrategy(RewireStrategy.None);
        job.setNumberOfPairings(NumberOfPairings.Pairings10);
        job.setPopulationSize(PopulationSize.Population100);
        job.setIterationCount(1000);
        job.setContextInfluence(0.5);
        job.setProbabilityOfLearning(0.1);
        job.setRewireProportion(0.6);

        // run the job, repeat n-times
        job.run(10);

        System.out.println("Donation rate: " + job.averageDonationRate());

        // String graphmlFilename = "test_graph.xml";

        // if (args.length > 0)
        // {
            // graphmlFilename = args[0];
        // }

		// new CooperationSimulation(graphmlFilename);
	}

	public CooperationSimulation(String graphMLFilename)
	{
		loadGraphFromFile(graphMLFilename);
		outputGraphToConsole();
		play(100);
		graph.listScores();

        outputGraphToConsole();
        System.out.println("Donation rate: " + graph.donationRate());
	}
	
	protected void loadGraphFromFile(String graphMLFilename)
	{
		GraphMLParser handler = new GraphMLParser();
		
		try
		{
			XMLReader xr = XMLReaderFactory.createXMLReader();
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
			xr.parse(new InputSource(new FileReader(graphMLFilename)));
		}
		catch (Exception e)
		{
			System.err.format("Could not read graph %s\n", graphMLFilename);
			e.printStackTrace();
			return;
		}
		
		graph = handler.getGraph();
	}
	
	protected void outputGraphToConsole()
	{
		System.out.println("List of vertices");
		graph.listVertices();
		System.out.println("List of edges for each vertex");
		graph.listEdges();
	}
	
	protected void play(int stepCount)
	{
		for (int i = 0; i < stepCount; i++)
		{
			AbstractVertex randomVertex = graph.randomVertex();
			randomVertex.step();
		}
	}
}
