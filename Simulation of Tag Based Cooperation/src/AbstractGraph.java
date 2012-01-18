import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public abstract class AbstractGraph
{
	
	// Vertex and Edge arrays
	protected Map<String, AbstractVertex> vertices = new HashMap<String, AbstractVertex>();
	protected Map<String, AbstractEdge>   edges    = new HashMap<String, AbstractEdge>();
	
	// Incidence List
	protected Map<String, ArrayList<String>> incidenceList = new HashMap<String, ArrayList<String>>();
	
	public AbstractGraph()
	{
	}
	
	public void addVertex(AbstractVertex vertex)
	{
		String vertexId = vertex.vertexId();
		vertices.put(vertexId, vertex);
		
		incidenceList.put(vertex.vertexId(), new ArrayList<String>());
	}
	
	public abstract void addEdge(AbstractVertex vertexA, AbstractVertex vertexB);
	public abstract void addEdge(String vertexAId, String vertexBId);
	public abstract void addEdge(AbstractVertex vertexA, AbstractVertex vertexB, String edgeId);
	public abstract void addEdge(String vertexAId, String vertexBId, String edgeId);
	
	public void listVertices()
	{
		for (Map.Entry<String, AbstractVertex> vertex : vertices.entrySet())
		{
			System.out.format("%s: %s\n", vertex.getKey(), vertex.getValue());
		}
	}
	
	public void listEdges()
	{
		for (Map.Entry<String, ArrayList<String>> edge : incidenceList.entrySet())
		{
			System.out.format("%s: ", edge.getKey());
			for (String edgeId : edge.getValue())
			{
				System.out.print(edges.get(edgeId) + " ");
			}
			System.out.println("");
		}
	}
}