import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public abstract class AbstractGraph
{
	
	// Vertex and Edge arrays
	protected Map<String, AbstractVertex> vertices = new HashMap<String, AbstractVertex>();
	protected Map<String, AbstractEdge>   edges    = new HashMap<String, AbstractEdge>();
	
	// Vertex array (for accessing random vertices)
	protected ArrayList<String> vertexList = new ArrayList<String>();
	
	// Incidence List
	protected Map<String, ArrayList<String>> incidenceList = new HashMap<String, ArrayList<String>>();
	
	public AbstractGraph()
	{
	}
	
	public void addVertex(AbstractVertex vertex)
	{
		String vertexId = vertex.vertexId();
		vertices.put(vertexId, vertex);
		vertexList.add(vertexId);
		
		incidenceList.put(vertex.vertexId(), new ArrayList<String>());		
        System.out.println("Adding vertex " + vertex.vertexId());
	}
	
	public abstract void addEdge(AbstractVertex vertexA, AbstractVertex vertexB);
	public abstract void addEdge(String vertexAId, String vertexBId);
	public abstract void addEdge(AbstractVertex vertexA, AbstractVertex vertexB, String edgeId);
	public abstract void addEdge(String vertexAId, String vertexBId, String edgeId);
	
	public void removeVertex(AbstractVertex vertex)
	{
		removeVertex(vertex.vertexId());
	}
	
	public void removeVertex(String vertexId)
	{	
		ArrayList<String> edgeIdsForVertex = new ArrayList<String>(incidenceList.get(vertexId));

		for (String edgeId : edgeIdsForVertex)
		{
			AbstractEdge edge = edges.get(edgeId);
			String firstVertexId = edge.firstVertexId();
			String secondVertexId = edge.secondVertexId();
			
			String otherVertexId = (vertexId.equals(firstVertexId)) ? secondVertexId : firstVertexId;
						
			// remove edge from other vertex id
			ArrayList<String> edgeIdsForOtherVertex = incidenceList.get(otherVertexId);
			edgeIdsForOtherVertex.remove(edgeId);
			
			// remove edge
			edges.remove(edgeId);
		}
		
		// remove vertex
		vertices.remove(vertexId);
		vertexList.remove(vertexId);
		
		// remove vertex from incidence list
		incidenceList.remove(vertexId);
	}
	
	public void removeEdge(AbstractVertex vertexA, AbstractVertex vertexB)
	{
		String vertexAId = vertexA.vertexId();
		String vertexBId = vertexB.vertexId();
		
		removeEdge(vertexAId, vertexBId);
	}
	
	public abstract void removeEdge(String vertexAId, String vertexBId);
	
	public void removeEdge(AbstractEdge edge)
	{
		String edgeId = edge.edgeId();
		removeEdge(edgeId);
	}
	
	public void removeEdge(String edgeId)
	{
		AbstractEdge edge = edges.get(edgeId);
		String firstVertexId = edge.firstVertexId();
		String secondVertexId = edge.secondVertexId();
		
		ArrayList<String> edgeIdsForFirstVertex = incidenceList.get(firstVertexId);
		edgeIdsForFirstVertex.remove(edgeId);
		
		ArrayList<String> edgeIdsForSecondVertex = incidenceList.get(secondVertexId);
		edgeIdsForSecondVertex.remove(edgeId);
		
		edges.remove(edgeId);
	}
	
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
	
	public void listScores()
	{
		for (Map.Entry<String, AbstractVertex> vertex : vertices.entrySet())
		{
			System.out.format("%s: %f\n", vertex.getKey(), ((TagScoreVertex)vertex.getValue()).score());
		}
	}
	
	public AbstractVertex randomVertex()
	{
		int vertexCount = vertexList.size();
		int randomVertexIndex = RandomGenerator.getInstance().nextIntegerLessThan(vertexCount);
		String vertexId = vertexList.get(randomVertexIndex);
		AbstractVertex vertex = vertices.get(vertexId);
		return vertex;
	}
	
	public ArrayList<String> neighbourIdsForVertex(String vertexId)
	{
		ArrayList<String> neighbours = new ArrayList<String>();
		
		ArrayList<String> edgeIdsForVertex = incidenceList.get(vertexId);
		
		for (String edgeId : edgeIdsForVertex)
		{
			AbstractEdge edge = edges.get(edgeId);
			if (edge.firstVertexId().equals(vertexId))
			{
				neighbours.add(edge.secondVertexId());
			}
		}
		
		return neighbours;
	}
	
	public ArrayList<AbstractVertex> neighboursForVertex(String vertexId)
	{
		ArrayList<String> neighbourIds = neighbourIdsForVertex(vertexId);
		ArrayList<AbstractVertex> neighbours = new ArrayList<AbstractVertex>();
		
		for (String neighbourId : neighbourIds)
		{
			AbstractVertex neighbour = vertices.get(neighbourId);
			neighbours.add(neighbour);
		}
		
		return neighbours;
	}
}
