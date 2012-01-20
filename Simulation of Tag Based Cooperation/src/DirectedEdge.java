public class DirectedEdge extends AbstractEdge
{
	String fromVertexId;
	String toVertexId;
	
	public DirectedEdge(AbstractGraph graph, AbstractVertex fromVertex, AbstractVertex toVertex)
	{
		this(graph, fromVertex.vertexId(), toVertex.vertexId(), null);
	}
	
	public DirectedEdge(AbstractGraph graph, String fromVertexId, String toVertexId)
	{
		this(graph, fromVertexId, toVertexId, null);
	}
	
	public DirectedEdge(AbstractGraph graph, AbstractVertex fromVertex, AbstractVertex toVertex, String edgeId)
	{
		this(graph, fromVertex.vertexId(), toVertex.vertexId(), edgeId);
	}
	
	public DirectedEdge(AbstractGraph graph, String fromVertexId, String toVertexId, String edgeId)
	{
		super(graph, edgeId);
		this.fromVertexId = fromVertexId;
		this.toVertexId = toVertexId;
	}
	
	public String toString()
	{
		return String.format("(%s) %s ---> %s", edgeId, fromVertexId, toVertexId);
	}
	
	public String firstVertexId()
	{
		return fromVertexId;
	}
	
	public String secondVertexId()
	{
		return toVertexId;
	}
}