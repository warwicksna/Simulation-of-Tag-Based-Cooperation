public class Job
{
    protected double           contextInfluence;
    protected RewireStrategy   rewireStrategy;
    protected NumberOfPairings numberOfParings;
    protected PopulationSize   populationSize;
    protected int              repitionCount;

    protected double donationRates[];
    protected Object logs[];

    public Job()
    {
    
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
        return numberOfParings;
    }

    public void setNumberOfPairings(NumberOfPairings pairings)
    {
        numberOfParings = pairings;
    }

    public PopulationSize populationSize()
    {
        return populationSize;
    }

    public void setPopulationSize(PopulationSize size)
    {
        populationSize = size;
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
}
