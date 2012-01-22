public class TagScoreVertex extends AbstractVertex
{
	protected double tag;
	protected double tolerance;
	
	public TagScoreVertex(AbstractGraph graph, double tag, double tolerance)
	{
		super(graph);
		this.tag = tag;
		this.tolerance = tolerance;
	}
	
	public TagScoreVertex(AbstractGraph graph, String vertexId, double tag, double tolerance)
	{
		super(graph, vertexId);
		this.tag = tag;
		this.tolerance = tolerance;
	}
	
	public double tag()
	{
		return tag;
	}
	
	public double tolerance()
	{
		return tolerance;
	}
	
	public String toString()
	{
		return String.format("tag = %1.5f; tolerance = %1.5f", tag, tolerance);
	}
	
	public void step()
	{
		System.out.format("Agent %s stepping\n", vertexId);
		donate();
		learn();
	}
	
	protected void donate()
	{
		// get list of neighbours
		// keep only the neighbours within tolerance value
		// donate to neighbours
		// update all neighbours with the donation responses
	}
	
	protected void learn()
	{
		// get random number
		// if random number <= threshold, return
		// pick another agent at random
		// compare if it is more successful than the current agent
		// if the current agent is less successful, return
		// learn from the more successful agent
		// rewire the agent's neighbourhood
	}
}