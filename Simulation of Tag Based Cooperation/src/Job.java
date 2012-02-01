public enum RewireStrategy
{
    None,
    Random,
    RandomReplaceWorst,
    IndividualReplaceWorst,
    GroupReplaceWorst
}

public enum NumberOfPairings
{
    Parings2,
    Parings5,
    Parings10,
    Parings15,
    Parings20,
    Parings25,
    Parings50,
    Parings100
}

public enum PopulationSize
{
    Population100,
    Population200,
    Population300,
    Population400,
    Population500
}

public class Job
{
    protected double           contextInfluence;
    protected RewireStrategy   rewireStrategy;
    protected NumberOfPairings numberOfParings;
    protected PopulationSize   populationSize;
    protected int              repitionCount;

    protected double donationRates[];
    protected Object logs[];

}
