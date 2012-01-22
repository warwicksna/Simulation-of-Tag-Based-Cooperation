import java.util.ArrayList;

public class DirectedGraph extends AbstractGraph
{	
	public void addEdge(AbstractVertex fromVertex, AbstractVertex toVertex)
	{
		String fromVertexId = fromVertex.vertexId();
		String toVertexId = toVertex.vertexId();
		
		addEdge(fromVertexId, toVertexId, null);
	}
	
	public void addEdge(String fromVertexId, String toVertexId)
	{
		addEdge(fromVertexId, toVertexId, null);
	}
	
	public void addEdge(AbstractVertex fromVertex, AbstractVertex toVertex, String edgeId)
	{
		String fromVertexId = fromVertex.vertexId();
		String toVertexId = toVertex.vertexId();
		
		addEdge(fromVertexId, toVertexId, edgeId);
	}
	
	public void addEdge(String fromVertexId, String toVertexId, String edgeId)
	{
		DirectedEdge edge = new DirectedEdge(this, fromVertexId, toVertexId, edgeId);
		edges.put(edge.edgeId(), edge);
		
		ArrayList<String> incidenceListForVertex;
		
		incidenceListForVertex = incidenceList.get(fromVertexId);
		incidenceListForVertex.add(edge.edgeId());
		
		incidenceListForVertex = incidenceList.get(toVertexId);
		incidenceListForVertex.add(edge.edgeId());
	}
	
	public void removeEdge(String fromVertexId, String toVertexId)
	{
		// remove edge From -> To
		ArrayList<String> incidenceListForVertex = new ArrayList<String>(incidenceList.get(fromVertexId));
		
		for (String edgeId : incidenceListForVertex)
		{
			AbstractEdge edge = edges.get(edgeId);
			if (!edge.secondVertexId().equals(toVertexId))
			{
				continue;
			}
			
			// remove edge
			ArrayList<String> edgeIdsFromVertex = incidenceList.get(fromVertexId);
			edgeIdsFromVertex.remove(edgeId);
		}
	}
}