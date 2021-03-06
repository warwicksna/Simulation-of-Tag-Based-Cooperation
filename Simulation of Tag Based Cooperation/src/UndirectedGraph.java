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
	    System.out.println("removing " + vertexAId + " <--> " + vertexBId);
		// remove edge A -> B
		
		System.out.println("Edges before\n============");
		for (String incidence : incidenceList.get(vertexAId))
		{
		    System.out.println(incidence);
		}
		
		ArrayList<String> incidenceListForVertexA = new ArrayList<String>(incidenceList.get(vertexAId));
				
		for (String edgeId : incidenceListForVertexA)
		{
			AbstractEdge edge = edges.get(edgeId);
			
			if (edge == null)
			{
			    System.out.println("Edge is null");
			    continue;
			}
			
			if (edge instanceof DirectedEdge && !edge.secondVertexId().equals(vertexBId))
			{
				continue;
			}
			else if (!edge.firstVertexId().equals(vertexBId) && !edge.secondVertexId().equals(vertexBId))
			{
			    continue;
			}
			
            System.out.println("Found: " + edgeId + " " + edge.firstVertexId() + " --> " + edge.secondVertexId());
					
			// remove edge
			ArrayList<String> edgeIdsFromVertexA = incidenceList.get(vertexAId);
			edgeIdsFromVertexA.remove(edgeId);
			
			incidenceList.get(vertexAId).remove(edgeId);
            // edges.remove(edgeId);
			
            if (job != null)
            {
                AbstractMessage message = new EdgeRemovedMessage(edge);
                job.registerMessage(message);
            }

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
			
			if (edge == null)
			{
			    System.out.println("Edge is null");
			    continue;
			}
			
			if (edge instanceof DirectedEdge && !edge.firstVertexId().equals(vertexAId))
			{
				continue;
			}
			else if (!edge.firstVertexId().equals(vertexAId) && !edge.secondVertexId().equals(vertexAId))
			{
			    continue;
			}
			
			
            System.out.println("Found: " + edge.firstVertexId() + " <-- " + edge.secondVertexId());
						
			// remove edge
			ArrayList<String> edgeIdsFromVertexB = incidenceList.get(vertexBId);
			edgeIdsFromVertexB.remove(edgeId);
			edges.remove(edgeId);
			
            if (job != null)
            {
                AbstractMessage message = new EdgeRemovedMessage(edge);
                job.registerMessage(message);
            }
		}
		
		System.out.println("Edges after\n============");
		for (String incidence : incidenceList.get(vertexAId))
		{
		    System.out.println(incidence);
		}
				
		// notify vertexB that its neighbourhood has been reduced
		AbstractVertex vertexB = vertices.get(vertexBId);
		vertexB.neighbourWasRemoved(vertexAId);
	}
}
