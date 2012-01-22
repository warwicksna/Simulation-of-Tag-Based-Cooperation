import java.util.ArrayList;

public class UndirectedGraph extends AbstractGraph
{	
	public void addEdge(AbstractVertex vertexA, AbstractVertex vertexB)
	{
		String vertexAId = vertexA.vertexId();
		String vertexBId = vertexB.vertexId();
		
		addEdge(vertexAId, vertexBId, null);
	}
	
	public void addEdge(String vertexAId, String vertexBId)
	{
		addEdge(vertexAId, vertexBId, null);
	}
	
	public void addEdge(AbstractVertex vertexA, AbstractVertex vertexB, String edgeId)
	{
		String vertexAId = vertexA.vertexId();
		String vertexBId = vertexB.vertexId();
		
		addEdge(vertexAId, vertexBId, edgeId);
	}
	       
	public void addEdge(String vertexAId, String vertexBId, String edgeId)
	{
		UndirectedEdge edge = new UndirectedEdge(this, vertexAId, vertexBId, edgeId);
		edges.put(edge.edgeId(), edge);
		
		ArrayList<String> incidenceListForVertex;
		
		incidenceListForVertex = incidenceList.get(vertexAId);
		incidenceListForVertex.add(edge.edgeId());
		
		incidenceListForVertex = incidenceList.get(vertexBId);
		incidenceListForVertex.add(edge.edgeId());
	}
	
	public void removeEdge(String vertexAId, String vertexBId)
	{
		// remove edge A -> B
		// remove edge B -> A
	}
}