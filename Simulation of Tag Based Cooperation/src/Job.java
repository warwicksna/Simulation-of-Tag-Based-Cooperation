public class Job
{
    protected double           contextInfluence = 0.5;
    protected RewireStrategy   rewireStrategy   = RewireStrategy.Random;
    protected NumberOfPairings numberOfPairings = NumberOfPairings.Pairings10;
    protected PopulationSize   populationSize   = PopulationSize.Population100;
    protected int              iterationCount   = 100;
    protected double           probabilityOfLearning = 0.1;
    protected int              repitionCount;
    protected double           rewireProportion = 0.6;

    protected int              currentRun = 0;

    protected String           filename;

    protected double donationRates[];
    protected Object logs[];

    public Job()
    {

    }

    public double[] run(int runs)
    {
        repitionCount = runs;

        donationRates = new double[repitionCount];

        filename = graphMLFilename(populationSize, numberOfPairings);

        for (currentRun = 0; currentRun < repitionCount; currentRun++)
        {
            // Generate a new copy of the graph from file
            AbstractGraph graph = new GraphMLParser().generateGraphFromFile(filename);
            graph.setJob(this);

            // Step n iterations
            for (int iteration = 0; iteration < iterationCount; iteration++)
            {
                AbstractVertex randomVertex = graph.randomVertex();
                randomVertex.step();
            }

            donationRates[currentRun] = graph.donationRate();
        }

        return donationRates;
    }

    public double averageDonationRate()
    {
        int sampleCount = donationRates.length;

        if (sampleCount == 0)
        {
            return 0;
        }

        double donationRateSum = 0;

        for (int i = 0; i < sampleCount; i++)
        {
            donationRateSum += donationRates[i];
        }

        return donationRateSum / sampleCount;
    }

    public double contextInfluence()
    {
        return contextInfluence;
    }

    public void setContextInfluence(double contextInfluence)
    {
        this.contextInfluence = contextInfluence;
    }

    public RewireStrategy rewireStrategy()
    {
        return rewireStrategy;
    }

    public void setRewireStrategy(RewireStrategy strategy)
    {
        rewireStrategy = strategy;
    }

    public NumberOfPairings numberOfParings()
    {
        return numberOfPairings;
    }

    public void setNumberOfPairings(NumberOfPairings pairings)
    {
        numberOfPairings = pairings;
    }

    public PopulationSize populationSize()
    {
        return populationSize;
    }

    public void setPopulationSize(PopulationSize size)
    {
        populationSize = size;
    }

    public int iterationCount()
    {
        return iterationCount;
    }

    public void setIterationCount(int iterations)
    {
        iterationCount = iterations;
    }

    public double probabilityOfLearning()
    {
        return probabilityOfLearning;
    }

    public void setProbabilityOfLearning(double probability)
    {
        probabilityOfLearning = probability;
    }

    public double rewireProportion()
    {
        return rewireProportion;
    }

    public void setRewireProportion(double proportion)
    {
        rewireProportion = proportion;
    }

    public static String rewireStrategyAsString(RewireStrategy strategy)
    {
        switch (strategy)
        {
            case None:
                return "None";
            case Random:
                return "Random";
            case RandomReplaceWorst:
                return "Random Replace Worst";
            case IndividualReplaceWorst:
                return "Individual Replace Worst";
            case GroupReplaceWorst:
                return "Group Replace Worst";
        }

        return "Unknown";
    }

    public static int numberOfPairingsAsInteger(NumberOfPairings pairings)
    {
        switch (pairings)
        {
            case Pairings2:
                return 2;
            case Pairings5:
                return 5;
            case Pairings10:
                return 10;
            case Pairings15:
                return 15;
            case Pairings20:
                return 20;
            case Pairings25:
                return 25;
            case Pairings50:
                return 50;
            case Pairings100:
                return 100;
        }

        return -1;
    }

    public static int populationSizeAsInteger(PopulationSize size)
    {
        switch (size)
        {
            case Population100:
                return 100;
            case Population200:
                return 200;
            case Population300:
                return 300;
            case Population400:
                return 400;
            case Population500:
                return 500;
        }

        return -1;
    }

    public static String graphMLFilename(PopulationSize size, NumberOfPairings pairings)
    {
        return String.format("pop_size_%d.pairing_count_%d.graphml", populationSizeAsInteger(size), numberOfPairingsAsInteger(pairings));
    }

    public String toString()
    {
        return String.format("Population Size: %d, Number of Pairings: %d, Rewire strategy: %s, Context Influence: %f\n",
                populationSizeAsInteger(populationSize), numberOfPairingsAsInteger(numberOfPairings), rewireStrategyAsString(rewireStrategy), contextInfluence);
    }
}
