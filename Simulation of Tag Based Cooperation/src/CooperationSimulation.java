public class CooperationSimulation
{
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

            job.run(10);
        }
	}
}
