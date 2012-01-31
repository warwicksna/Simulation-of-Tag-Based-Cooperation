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
		
		// notify vertex that a neighbour was added
		AbstractVertex fromVertex = vertices.get(fromVertexId);
		fromVertex.neighbourWasAdded(toVertexId);
	}
	
	public void removeEdge(String fromVertexId, String toVertexId)
	{
		// remove edge From -> To
		ArrayList<String> incidenceListForVertex = new ArrayList<String>(incidenceList.get(fromVertexId));
		AbstractEdge edge = null;
		
		for (String edgeId : incidenceListForVertex)
		{
			edge = edges.get(edgeId);
			if (!edge.secondVertexId().equals(toVertexId))
			{
				continue;
			}
			
			// remove edge
			ArrayList<String> edgeIdsFromVertex = incidenceList.get(fromVertexId);
			edgeIdsFromVertex.remove(edgeId);
			edges.remove(edgeId);
		}
		
		// Also remove edge From -> To in toVertex's incidence list
		if (edge != null)
		{
			incidenceList.get(toVertexId).remove(edge.edgeId());
		}
		
		// notify fromVertex that its neighbourhood has been reduced
		AbstractVertex fromVertex = vertices.get(fromVertexId);
		fromVertex.neighbourWasRemoved(toVertexId);
	}
}
