import java.lang.ref.WeakReference;

public abstract class AbstractVertex
{
	static int nextVertexId = 0;
	String vertexId;
	WeakReference<AbstractGraph> graph;
	
	public AbstractVertex(AbstractGraph graph)
	{
		this.graph = new WeakReference<AbstractGraph>(graph);
		vertexId = String.format("%5d", nextVertexId);
		nextVertexId++;
	}
	
	public AbstractVertex(AbstractGraph graph, String vertexId)
	{
		this.graph = new WeakReference<AbstractGraph>(graph);
		this.vertexId = vertexId;
	}
	
	public String vertexId()
	{
		return vertexId;
	}
}