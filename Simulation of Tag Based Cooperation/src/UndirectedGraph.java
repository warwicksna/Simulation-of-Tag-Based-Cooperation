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
		ArrayList<String> incidenceListForVertexA = new ArrayList<String>(incidenceList.get(vertexAId));
		
		for (String edgeId : incidenceListForVertexA)
		{
			AbstractEdge edge = edges.get(edgeId);
			if (!edge.secondVertexId().equals(vertexBId))
			{
				continue;
			}
			
			// remove edge
			ArrayList<String> edgeIdsFromVertexA = incidenceList.get(vertexAId);
			edgeIdsFromVertexA.remove(edgeId);
			edges.remove(edgeId);
		}
		
		// remove edge B -> A
		ArrayList<String> incidenceListForVertexB = new ArrayList<String>(incidenceList.get(vertexBId));
		
		for (String edgeId : incidenceListForVertexB)
		{
			AbstractEdge edge = edges.get(edgeId);
			if (!edge.firstVertexId().equals(vertexAId))
			{
				continue;
			}
			
			// remove edge
			ArrayList<String> edgeIdsFromVertexB = incidenceList.get(vertexBId);
			edgeIdsFromVertexB.remove(edgeId);
			edges.remove(edgeId);
		}
	}
}