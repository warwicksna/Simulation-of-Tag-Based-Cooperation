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
            job.setCheatingPercentage(0.3);

            job.run(10);
        }

        for (double rewireProportion = 0; rewireProportion <= 1; rewireProportion += 0.1)
        {
            Job job = new Job();

            job.setRewireStrategy(RewireStrategy.Random);
            job.setNumberOfPairings(NumberOfPairings.Pairings10);
            job.setPopulationSize(PopulationSize.Population100);
            job.setIterationCount(1000);
            job.setContextInfluence(0.5);
            job.setProbabilityOfLearning(0.1);
            job.setRewireProportion(rewireProportion);
            job.setCheatingPercentage(0.1);

            job.run(10);


            job = new Job();

            job.setRewireStrategy(RewireStrategy.RandomReplaceWorst);
            job.setNumberOfPairings(NumberOfPairings.Pairings10);
            job.setPopulationSize(PopulationSize.Population100);
            job.setIterationCount(1000);
            job.setContextInfluence(0.5);
            job.setProbabilityOfLearning(0.1);
            job.setRewireProportion(rewireProportion);
            job.setCheatingPercentage(0.1);

            job.run(10);


            job = new Job();

            job.setRewireStrategy(RewireStrategy.IndividualReplaceWorst);
            job.setNumberOfPairings(NumberOfPairings.Pairings10);
            job.setPopulationSize(PopulationSize.Population100);
            job.setIterationCount(1000);
            job.setContextInfluence(0.5);
            job.setProbabilityOfLearning(0.1);
            job.setRewireProportion(rewireProportion);
            job.setCheatingPercentage(0.1);

            job.run(10);

            job = new Job();

            job.setRewireStrategy(RewireStrategy.GroupReplaceWorst);
            job.setNumberOfPairings(NumberOfPairings.Pairings10);
            job.setPopulationSize(PopulationSize.Population100);
            job.setIterationCount(1000);
            job.setContextInfluence(0.5);
            job.setProbabilityOfLearning(0.1);
            job.setRewireProportion(rewireProportion);
            job.setCheatingPercentage(0.1);

            job.run(10);

        }
	}
}
