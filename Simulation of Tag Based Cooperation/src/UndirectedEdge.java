public class UndirectedEdge extends AbstractEdge
{
	String vertexAId;
	String vertexBId;
	
	public UndirectedEdge(AbstractGraph graph, AbstractVertex vertexA, AbstractVertex vertexB)
	{
		this(graph, vertexA.vertexId(), vertexB.vertexId(), null);
	}
	
	public UndirectedEdge(AbstractGraph graph, String vertexAId, String vertexBId)
	{
		this(graph, vertexAId, vertexBId, null);
	}
	
	public UndirectedEdge(AbstractGraph graph, AbstractVertex vertexA, AbstractVertex vertexB, String edgeId)
	{
		this(graph, vertexA.vertexId(), vertexB.vertexId(), edgeId);
	}
	
	public UndirectedEdge(AbstractGraph graph, String vertexAId, String vertexBId, String edgeId)
	{
		super(graph, edgeId);
		this.vertexAId = vertexAId;
		this.vertexBId = vertexBId;
	}
	
	public String toString()
	{
		return String.format("%s <--> %s", vertexAId, vertexBId);
	}
	
	public String firstVertexId()
	{
		return vertexAId;
	}
	
	public String secondVertexId()
	{
		return vertexBId;
	}
}