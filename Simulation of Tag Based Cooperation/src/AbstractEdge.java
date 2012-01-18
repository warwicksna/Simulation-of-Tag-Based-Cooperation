import java.lang.ref.WeakReference;

public abstract class AbstractEdge
{
	static int nextEdgeId = 0;
	String edgeId;
	WeakReference<AbstractGraph> graph;
	
	public AbstractEdge(AbstractGraph graph)
	{
		this.graph = new WeakReference<AbstractGraph>(graph);
		edgeId = String.format("%5d", nextEdgeId);
		nextEdgeId++;
	}
	
	public AbstractEdge(AbstractGraph graph, String edgeId)
	{
		this.graph = new WeakReference<AbstractGraph>(graph);
		
		if (edgeId != null)
		{
			this.edgeId = edgeId;
			return;
		}
		
		edgeId = String.format("%5d", nextEdgeId);
		nextEdgeId++;
	}
	
	public String edgeId()
	{
		return edgeId;
	}
}