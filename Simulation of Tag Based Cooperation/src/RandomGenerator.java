import java.util.Random;

public class RandomGenerator {
	
	private static final RandomGenerator singletonInstance = new RandomGenerator();
	private final Random rng;
	
	private RandomGenerator()
	{
		rng = new Random();
	}
	
	public static RandomGenerator getInstance()
	{
		return singletonInstance;
	}
	
	public void setSeed(long seed)
	{
		rng.setSeed(seed);
	}
	
	public int nextIntegerLessThan(int maxValue)
	{
		return rng.nextInt(maxValue);
	}
	
}