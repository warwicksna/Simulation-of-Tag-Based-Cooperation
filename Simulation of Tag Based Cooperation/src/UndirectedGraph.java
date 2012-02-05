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
		
		// notify vertices that their neighbourhoods have expanded
		AbstractVertex vertexA = vertices.get(vertexAId);
		vertexA.neighbourWasAdded(vertexBId);
		
		AbstractVertex vertexB = vertices.get(vertexBId);
		vertexB.neighbourWasAdded(vertexAId);

        if (job == null)
        {
            return;
        }

        AbstractMessage message = new EdgeAddedMessage(edge);
        job.registerMessage(message);
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

            break;
		}
		
		// notify vertexA that its neighbourhood has been reduced
		AbstractVertex vertexA = vertices.get(vertexAId);
		vertexA.neighbourWasRemoved(vertexBId);
		
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
		
		// notify vertexB that its neighbourhood has been reduced
		AbstractVertex vertexB = vertices.get(vertexBId);
		vertexB.neighbourWasRemoved(vertexAId);
	}
}
