import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class CooperationSimulation
{
	public static void appendToCSV(String graphType, RewireStrategy rewireStrategy, int vertexCount, int edgeDegree, double donationRate)
	{
		try
		{
			FileWriter fileWriter = new FileWriter("results/master.csv", true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(String.format("%s, %s, %d, %d, %f\n", graphType, Job.rewireStrategyAsString(rewireStrategy), vertexCount, edgeDegree, donationRate));
			bufferedWriter.close();
		} catch (IOException ex)
		{
			System.err.println("Could not write to results file: " + ex.getMessage());
		}
	}
	
	public static void appendToCSV(String name, String graphType, RewireStrategy rewireStrategy, int vertexCount, int edgeDegree, int iterationCount, double donationRate)
	{
		try
		{
			FileWriter fileWriter = new FileWriter("results/" + name + ".csv", true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(String.format("%s, %s, %d, %d, %d, %f\n",
				graphType,
				rewireStrategy,
				vertexCount,
				edgeDegree,
				iterationCount,
				donationRate));
			bufferedWriter.close();
		}
		catch (IOException ex)
		{
			System.err.println("Could not write to csv file " + name + ".csv: " + ex.getMessage());
		}
	}
	
	public static void main(String args[])
	{
		String graphTypes[] = { "Random", "Scale-Free", "Small-World" };
		int edgeDegrees[] = { 1, 2, 3, 4, 5, 10, 15, 20, 25 };
		int vertexCounts[] = { 100, 200, 300, 400, 500 };
		RewireStrategy rewireStrategies[] = { RewireStrategy.Random, RewireStrategy.RandomReplaceWorst, RewireStrategy.IndividualReplaceWorst, RewireStrategy.GroupReplaceWorst };
		
		int iterationCount = 100000;
		
		for (int graphTypeIndex = 0; graphTypeIndex < graphTypes.length; graphTypeIndex++)
		{
			String graphType = graphTypes[graphTypeIndex];
			for (int edgeDegreeIndex = 0; edgeDegreeIndex < edgeDegrees.length; edgeDegreeIndex++)
			{
				int edgeDegree = edgeDegrees[edgeDegreeIndex];
				for (int vertexCountIndex = 0; vertexCountIndex < vertexCounts.length; vertexCountIndex++)
				{
					int vertexCount = vertexCounts[vertexCountIndex];

					for (int rewireStrategyIndex = 0; rewireStrategyIndex < rewireStrategies.length; rewireStrategyIndex++)
					{
						RewireStrategy rewireStrategy = rewireStrategies[rewireStrategyIndex];
						
						Job job = new Job();

				        job.setRewireStrategy(rewireStrategy);
						job.setGraphType(graphType);
						job.setVertexCount(vertexCount);
						job.setEdgeDegree(edgeDegree);
				        job.setIterationCount(iterationCount);
				        job.setContextInfluence(0.5);
				        job.setProbabilityOfLearning(0.001);
				        job.setRewireProportion(0.6);
				        job.setCheatingPercentage(0.1);

						job.run(10);
						
						// appendToCSV(graphType, rewireStrategy, vertexCount, edgeDegree, job.medianDonationRate());
						
						appendToCSV("master", graphType, rewireStrategy, vertexCount, edgeDegree, iterationCount, job.medianDonationRate());
						appendToCSV(graphType + " graph", graphType, rewireStrategy, vertexCount, edgeDegree, iterationCount, job.medianDonationRate());
						appendToCSV(job.rewireStrategyAsString(rewireStrategy) + " rewire strategy", graphType, rewireStrategy, vertexCount, edgeDegree, iterationCount, job.medianDonationRate());
					}
				}
			}
		}
		
       // System.out.println("========== CONTEXT INFLUENCE ================");
       //  for (double contextInfluence = 0; contextInfluence <= 1; contextInfluence += 0.1)
       //  {
       //      Job job = new Job();
       // 
       //      job.setRewireStrategy(RewireStrategy.None);
       //      job.setNumberOfPairings(NumberOfPairings.Pairings10);
       //      job.setPopulationSize(PopulationSize.Population100);
       //      job.setIterationCount(100000);
       //      job.setContextInfluence(contextInfluence);
       //      job.setProbabilityOfLearning(0.1);
       //      job.setRewireProportion(0.6);
       //      job.setCheatingPercentage(0.1);
       // 
       //      job.run(10);
       // 
       //      job.writeMessagesToFile("context_influence_" + String.format("%.1f", contextInfluence) + ".log");
       //  }

//        System.out.println("========== CHEATERS = 10% ================");

//         for (double rewireProportion = 0; rewireProportion <= 1; rewireProportion += 0.1)
//         {
//             Job job = new Job();

//             job.setRewireStrategy(RewireStrategy.Random);
//             job.setNumberOfPairings(NumberOfPairings.Pairings10);
//             job.setPopulationSize(PopulationSize.Population100);
//             job.setIterationCount(100000);
//             job.setContextInfluence(0.5);
//             job.setProbabilityOfLearning(0.1);
//             job.setRewireProportion(rewireProportion);
//             job.setCheatingPercentage(0.1);

//             job.run(10);


//             job = new Job();

//             job.setRewireStrategy(RewireStrategy.RandomReplaceWorst);
//             job.setNumberOfPairings(NumberOfPairings.Pairings10);
//             job.setPopulationSize(PopulationSize.Population100);
//             job.setIterationCount(100000);
//             job.setContextInfluence(0.5);
//             job.setProbabilityOfLearning(0.1);
//             job.setRewireProportion(rewireProportion);
//             job.setCheatingPercentage(0.1);

//             job.run(10);


//             job = new Job();

//             job.setRewireStrategy(RewireStrategy.IndividualReplaceWorst);
//             job.setNumberOfPairings(NumberOfPairings.Pairings10);
//             job.setPopulationSize(PopulationSize.Population100);
//             job.setIterationCount(100000);
//             job.setContextInfluence(0.5);
//             job.setProbabilityOfLearning(0.1);
//             job.setRewireProportion(rewireProportion);
//             job.setCheatingPercentage(0.1);

//             job.run(10);

//             job = new Job();

//             job.setRewireStrategy(RewireStrategy.GroupReplaceWorst);
//             job.setNumberOfPairings(NumberOfPairings.Pairings10);
//             job.setPopulationSize(PopulationSize.Population100);
//             job.setIterationCount(100000);
//             job.setContextInfluence(0.5);
//             job.setProbabilityOfLearning(0.1);
//             job.setRewireProportion(rewireProportion);
//             job.setCheatingPercentage(0.1);

//             job.run(10);

//         }

//        System.out.println("========== CHEATERS = 20% ================");

//        for (double rewireProportion = 0; rewireProportion <= 1; rewireProportion += 0.1)
//         {
//             Job job = new Job();

//             job.setRewireStrategy(RewireStrategy.Random);
//             job.setNumberOfPairings(NumberOfPairings.Pairings10);
//             job.setPopulationSize(PopulationSize.Population100);
//             job.setIterationCount(100000);
//             job.setContextInfluence(0.5);
//             job.setProbabilityOfLearning(0.1);
//             job.setRewireProportion(rewireProportion);
//             job.setCheatingPercentage(0.2);

//             job.run(10);


//             job = new Job();

//             job.setRewireStrategy(RewireStrategy.RandomReplaceWorst);
//             job.setNumberOfPairings(NumberOfPairings.Pairings10);
//             job.setPopulationSize(PopulationSize.Population100);
//             job.setIterationCount(100000);
//             job.setContextInfluence(0.5);
//             job.setProbabilityOfLearning(0.1);
//             job.setRewireProportion(rewireProportion);
//             job.setCheatingPercentage(0.2);

//             job.run(10);


//             job = new Job();

//             job.setRewireStrategy(RewireStrategy.IndividualReplaceWorst);
//             job.setNumberOfPairings(NumberOfPairings.Pairings10);
//             job.setPopulationSize(PopulationSize.Population100);
//             job.setIterationCount(100000);
//             job.setContextInfluence(0.5);
//             job.setProbabilityOfLearning(0.1);
//             job.setRewireProportion(rewireProportion);
//             job.setCheatingPercentage(0.2);

//             job.run(10);

//             job = new Job();

//             job.setRewireStrategy(RewireStrategy.GroupReplaceWorst);
//             job.setNumberOfPairings(NumberOfPairings.Pairings10);
//             job.setPopulationSize(PopulationSize.Population100);
//             job.setIterationCount(100000);
//             job.setContextInfluence(0.5);
//             job.setProbabilityOfLearning(0.1);
//             job.setRewireProportion(rewireProportion);
//             job.setCheatingPercentage(0.2);

//             job.run(10);

//         }

//        System.out.println("========== CHEATERS = 30% ================");

//        for (double rewireProportion = 0; rewireProportion <= 1; rewireProportion += 0.1)
//         {
//             Job job = new Job();

//             job.setRewireStrategy(RewireStrategy.Random);
//             job.setNumberOfPairings(NumberOfPairings.Pairings10);
//             job.setPopulationSize(PopulationSize.Population100);
//             job.setIterationCount(100000);
//             job.setContextInfluence(0.5);
//             job.setProbabilityOfLearning(0.1);
//             job.setRewireProportion(rewireProportion);
//             job.setCheatingPercentage(0.3);

//             job.run(10);


//             job = new Job();

//             job.setRewireStrategy(RewireStrategy.RandomReplaceWorst);
//             job.setNumberOfPairings(NumberOfPairings.Pairings10);
//             job.setPopulationSize(PopulationSize.Population100);
//             job.setIterationCount(100000);
//             job.setContextInfluence(0.5);
//             job.setProbabilityOfLearning(0.1);
//             job.setRewireProportion(rewireProportion);
//             job.setCheatingPercentage(0.3);

//             job.run(10);


//             job = new Job();

//             job.setRewireStrategy(RewireStrategy.IndividualReplaceWorst);
//             job.setNumberOfPairings(NumberOfPairings.Pairings10);
//             job.setPopulationSize(PopulationSize.Population100);
//             job.setIterationCount(100000);
//             job.setContextInfluence(0.5);
//             job.setProbabilityOfLearning(0.1);
//             job.setRewireProportion(rewireProportion);
//             job.setCheatingPercentage(0.3);

//             job.run(10);

//             job = new Job();

//             job.setRewireStrategy(RewireStrategy.GroupReplaceWorst);
//             job.setNumberOfPairings(NumberOfPairings.Pairings10);
//             job.setPopulationSize(PopulationSize.Population100);
//             job.setIterationCount(100000);
//             job.setContextInfluence(0.5);
//             job.setProbabilityOfLearning(0.1);
//             job.setRewireProportion(rewireProportion);
//             job.setCheatingPercentage(0.3);

//             job.run(10);

//         }

//         System.out.println("========== POPULATION SIZE ================");

//         PopulationSize[] populations = new PopulationSize[5];
//         populations[0] = PopulationSize.Population100;
//         populations[1] = PopulationSize.Population200;
//         populations[2] = PopulationSize.Population300;
//         populations[3] = PopulationSize.Population400;
//         populations[4] = PopulationSize.Population500;

//         for (int index = 0; index < populations.length; index++)
//         {
//             PopulationSize population = populations[index];

//             Job job = new Job();

//             job.setRewireStrategy(RewireStrategy.IndividualReplaceWorst);
//             job.setNumberOfPairings(NumberOfPairings.Pairings10);
//             job.setPopulationSize(population);
//             job.setIterationCount(100000);
//             job.setContextInfluence(0.5);
//             job.setProbabilityOfLearning(0.1);
//             job.setRewireProportion(0.6);
//             job.setCheatingPercentage(0.1);

//             job.run(10);

//             job = new Job();

//             job.setRewireStrategy(RewireStrategy.GroupReplaceWorst);
//             job.setNumberOfPairings(NumberOfPairings.Pairings10);
//             job.setPopulationSize(population);
//             job.setIterationCount(100000);
//             job.setContextInfluence(0.5);
//             job.setProbabilityOfLearning(0.1);
//             job.setRewireProportion(0.6);
//             job.setCheatingPercentage(0.1);

//             job.run(10);
//         }


//         System.out.println("========== PAIRINGS ================");


//         int[] px = new int[8];
//         px[0] = 5;
//         px[1] = 10;
//         px[2] = 15;
//         px[3] = 20;
//         px[4] = 25;
//         px[5] = 30;
//         px[6] = 50;
//         px[7] = 100;

//         for (int index = 0; index < px.length; index++)
//         {
//             int iterations = 100 * 100 * px[index];

//             Job job = new Job();

//             job.setRewireStrategy(RewireStrategy.IndividualReplaceWorst);
//             job.setNumberOfPairings(NumberOfPairings.Pairings10);
//             job.setPopulationSize(PopulationSize.Population100);
//             job.setIterationCount(iterations);
//             job.setContextInfluence(0.5);
//             job.setProbabilityOfLearning(0.1);
//             job.setRewireProportion(0.6);
//             job.setCheatingPercentage(0.1);

//             job.run(10);

//             job = new Job();

//             job.setRewireStrategy(RewireStrategy.GroupReplaceWorst);
//             job.setNumberOfPairings(NumberOfPairings.Pairings10);
//             job.setPopulationSize(PopulationSize.Population100);
//             job.setIterationCount(iterations);
//             job.setContextInfluence(0.5);
//             job.setProbabilityOfLearning(0.1);
//             job.setRewireProportion(0.6);
//             job.setCheatingPercentage(0.1);

//             job.run(10);
//         }

//         System.out.println("========== NEIGHBOURHOOD SIZE ================");



//         NumberOfPairings[] pairings = new NumberOfPairings[7];
//         pairings[0] = NumberOfPairings.Pairings5;
//         pairings[1] = NumberOfPairings.Pairings10;
//         pairings[2] = NumberOfPairings.Pairings15;
//         pairings[3] = NumberOfPairings.Pairings20;
//         pairings[4] = NumberOfPairings.Pairings25;
//         pairings[5] = NumberOfPairings.Pairings50;
//         pairings[6] = NumberOfPairings.Pairings100;

//         for (int index = 0; index < pairings.length; index++)
//         {
//             NumberOfPairings pairing = pairings[index];

//             Job job = new Job();

//             job.setRewireStrategy(RewireStrategy.IndividualReplaceWorst);
//             job.setNumberOfPairings(pairing);
//             job.setPopulationSize(PopulationSize.Population100);
//             job.setIterationCount(100000);
//             job.setContextInfluence(0.5);
//             job.setProbabilityOfLearning(0.1);
//             job.setRewireProportion(0.6);
//             job.setCheatingPercentage(0.1);

//             job.run(10);

//             job = new Job();

//             job.setRewireStrategy(RewireStrategy.GroupReplaceWorst);
//             job.setNumberOfPairings(pairing);
//             job.setPopulationSize(PopulationSize.Population100);
//             job.setIterationCount(100000);
//             job.setContextInfluence(0.5);
//             job.setProbabilityOfLearning(0.1);
//             job.setRewireProportion(0.6);
//             job.setCheatingPercentage(0.1);

//             job.run(10);
//         }
    }
}
