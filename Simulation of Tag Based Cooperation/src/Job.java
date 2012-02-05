import java.util.Arrays;
import java.util.ArrayList;
import java.io.BufferedWriter;;
import java.io.FileWriter;
import java.io.IOException;

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
    protected double           cheatingPercentage = 0.1;

    protected int              currentRun = 0;

    protected String           filename;

    protected double donationRates[];
    protected ArrayList<AbstractMessage> messages = new ArrayList<AbstractMessage>();

    protected static int nextJobId = 0;
    protected int jobId;

    public Job()
    {
        jobId = nextJobId++;
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
            graph.setPercentageOfVerticesAsCheaters(cheatingPercentage);

            // Step n iterations
            for (int iteration = 0; iteration < iterationCount; iteration++)
            {
                TimeIntervalBegunMessage message = new TimeIntervalBegunMessage();
                registerMessage(message);
                AbstractVertex randomVertex = graph.randomVertex();
                randomVertex.step();
            }

            donationRates[currentRun] = graph.donationRate();
        }

        printDescription();

        return donationRates;
    }

    public void printDescription()
    {
        System.out.format("Job %d\n==================================\n", jobId);
        System.out.println("       Population size: " + populationSizeAsInteger(populationSize));
        System.out.println("    Number of pairings: " + numberOfPairingsAsInteger(numberOfPairings));
        System.out.println("     Rewiring strategy: " + rewireStrategyAsString(rewireStrategy));
        System.out.format("    Rewire percentage: %.3f\n", rewireProportion);
        System.out.format("     Context Influence: %.3f\n", contextInfluence);
        System.out.format("   Cheating Percentage: %.3f\n", cheatingPercentage);
        System.out.println("----------------------------------");
        System.out.format(" Average donation rate: %.3f\n", averageDonationRate());
        System.out.format(" Minimum donation rate: %.3f\n", minimumDonationRate());
        System.out.format(" Maximum donation rate: %.3f\n", maximumDonationRate());
        System.out.format("  Median donation rate: %.3f\n", medianDonationRate());
        System.out.println("----------------------------------\n");
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

    public double maximumDonationRate()
    {
        int sampleCount = donationRates.length;

        double maximum = 0;

        for (int i = 0; i < sampleCount; i++)
        {
            if (donationRates[i] > maximum)
            {
                maximum = donationRates[i];
            }
        }

        return maximum;
    }

    public double minimumDonationRate()
    {
        int sampleCount = donationRates.length;

        double minimum = 1;

        for (int i = 0; i < sampleCount; i++)
        {
            if (donationRates[i] < minimum)
            {
                minimum = donationRates[i];
            }
        }

        return minimum;
    }

    public double medianDonationRate()
    {
        if (donationRates.length == 0)
        {
            return 0;
        }

        double[] sortedDonationRates = new double[donationRates.length];

        System.arraycopy(donationRates, 0, sortedDonationRates, 0, donationRates.length);

        Arrays.sort(sortedDonationRates);

        double median = 0;

        if (sortedDonationRates.length % 2 == 0)
        {
            double lowerBound = sortedDonationRates[(sortedDonationRates.length / 2) - 1];
            double upperBound = sortedDonationRates[(sortedDonationRates.length / 2)];

            median = (lowerBound + upperBound) / 2.0;
        }
        else
        {
            median = sortedDonationRates[((sortedDonationRates.length + 1) / 2) - 1];
        }

        return median;
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

    public double cheatingPercentage()
    {
        return cheatingPercentage;
    }

    public void setCheatingPercentage(double percentage)
    {
        cheatingPercentage = percentage;
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
        return String.format("graphs/pop_size_%d.pairing_count_%d.directed.graphml", populationSizeAsInteger(size), numberOfPairingsAsInteger(pairings));
    }

    public String toString()
    {
        return String.format("Population Size: %d, Number of Pairings: %d, Rewire strategy: %s, Context Influence: %f\n",
                populationSizeAsInteger(populationSize), numberOfPairingsAsInteger(numberOfPairings), rewireStrategyAsString(rewireStrategy), contextInfluence);
    }

    public void registerMessage(AbstractMessage message)
    {
        messages.add(message);
    }

    public void writeMessagesToFile(String filename)
    {
        FileWriter fw = null;
        BufferedWriter bw;

        try
        {
            fw = fw = new FileWriter(filename);
        }
        catch (IOException ex)
        {
            System.err.println("Error: could not open file " + filename + " to write job messages to");
            System.err.println(ex.getMessage());
            return;
        }

        bw = new BufferedWriter(fw);

        try
        {
            for (AbstractMessage message : messages)
            {
                bw.write(message.toString());
                bw.newLine();
            }
        }
        catch (IOException ex)
        {
            System.err.println("Error: could not write message to file " + filename);
            System.err.println(ex.getMessage());
        }

        try
        {
            bw.close();
        }
        catch (IOException ex)
        {
            System.err.println("Could not close file " + filename);
            System.err.println(ex.getMessage());
        }
    }
}
