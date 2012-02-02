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
            job.setCheatingPercentage(0.1);

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

        NumberOfPairings[] pairings = new NumberOfPairings[7];
        pairings[0] = NumberOfPairings.Pairings5;
        pairings[1] = NumberOfPairings.Pairings10;
        pairings[2] = NumberOfPairings.Pairings15;
        pairings[3] = NumberOfPairings.Pairings20;
        pairings[4] = NumberOfPairings.Pairings25;
        pairings[5] = NumberOfPairings.Pairings50;
        pairings[6] = NumberOfPairings.Pairings100;

        for (int index = 0; index < pairings.length; index++)
        {
            NumberOfPairings pairing = pairings[index];

            Job job = new Job();

            job.setRewireStrategy(RewireStrategy.IndividualReplaceWorst);
            job.setNumberOfPairings(pairing);
            job.setPopulationSize(PopulationSize.Population100);
            job.setIterationCount(1000);
            job.setContextInfluence(0.5);
            job.setProbabilityOfLearning(0.1);
            job.setRewireProportion(0.6);
            job.setCheatingPercentage(0.1);

            job.run(10);

            job = new Job();

            job.setRewireStrategy(RewireStrategy.GroupReplaceWorst);
            job.setNumberOfPairings(pairing);
            job.setPopulationSize(PopulationSize.Population100);
            job.setIterationCount(1000);
            job.setContextInfluence(0.5);
            job.setProbabilityOfLearning(0.1);
            job.setRewireProportion(0.6);
            job.setCheatingPercentage(0.1);

            job.run(10);
        }

        PopulationSize[] populations = new PopulationSize[5];
        populations[0] = PopulationSize.Population100;
        populations[1] = PopulationSize.Population200;
        populations[2] = PopulationSize.Population300;
        populations[3] = PopulationSize.Population400;
        populations[4] = PopulationSize.Population500;

        for (int index = 0; index < populations.length; index++)
        {
            PopulationSize population = populations[index];

            Job job = new Job();

            job.setRewireStrategy(RewireStrategy.IndividualReplaceWorst);
            job.setNumberOfPairings(NumberOfPairings.Pairings10);
            job.setPopulationSize(population);
            job.setIterationCount(1000);
            job.setContextInfluence(0.5);
            job.setProbabilityOfLearning(0.1);
            job.setRewireProportion(0.6);
            job.setCheatingPercentage(0.1);

            job.run(10);

            job = new Job();

            job.setRewireStrategy(RewireStrategy.GroupReplaceWorst);
            job.setNumberOfPairings(NumberOfPairings.Pairings10);
            job.setPopulationSize(population);
            job.setIterationCount(1000);
            job.setContextInfluence(0.5);
            job.setProbabilityOfLearning(0.1);
            job.setRewireProportion(0.6);
            job.setCheatingPercentage(0.1);

            job.run(10);
        }

	}
}
