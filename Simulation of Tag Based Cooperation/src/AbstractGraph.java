import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.BufferedWriter;;
import java.io.FileWriter;
import java.io.IOException;

public abstract class AbstractGraph
{
	
	// Vertex and Edge arrays
	protected Map<String, AbstractVertex> vertices = new HashMap<String, AbstractVertex>();
	protected Map<String, AbstractEdge>   edges    = new HashMap<String, AbstractEdge>();
	
	// Vertex array (for accessing random vertices)
	protected ArrayList<String> vertexList = new ArrayList<String>();
	
	// Incidence List
	protected Map<String, ArrayList<String>> incidenceList = new HashMap<String, ArrayList<String>>();

    // Metrics for calculating donation rate
    protected int donationCount = 0;
    protected int possibleDonationCount = 0;

    protected Job job;
	
	public AbstractGraph()
	{
	}

    public void setJob(Job job)
    {
        this.job = job;
    }

    public Job job()
    {
        return job;
    }
	
	public void addVertex(AbstractVertex vertex)
	{
		String vertexId = vertex.vertexId();
		vertices.put(vertexId, vertex);
		vertexList.add(vertexId);
		
		incidenceList.put(vertex.vertexId(), new ArrayList<String>());		

        // job is not null for vertices being added which were not in the graphml
        if (job != null)
        {
            AbstractMessage message = new VertexAddedMessage((TagScoreVertex) vertex);
            job.registerMessage(message);
        }
	}
	
	public abstract void addEdge(AbstractVertex vertexA, AbstractVertex vertexB);
	public abstract void addEdge(String vertexAId, String vertexBId);
	public abstract void addEdge(AbstractVertex vertexA, AbstractVertex vertexB, String edgeId);
	public abstract void addEdge(String vertexAId, String vertexBId, String edgeId);

    public boolean containsEdge(AbstractVertex vertexA, AbstractVertex vertexB)
    {
        return containsEdge(vertexA.vertexId(), vertexB.vertexId());
    }

    public boolean containsEdge(String vertexAId, String vertexBId)
    {
        List<String> edgeIds = incidenceList.get(vertexAId);

        for (String edgeId : edgeIds)
        {
            AbstractEdge edge = edges.get(edgeId);

            if (edge.secondVertexId().equals(vertexBId))
            {
                return true;
            }
        }

        return false;
    }
	
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
			
            // if (edge instanceof UndirectedEdge)
            // {
            //     if (edge.firstVertexId().equals(vertexId))
            //     {
            //         neighbours.add(edge.secondVertexId());
            //     }
            //     else
            //     {
            //         neighbours.add(edge.firstVertexId());
            //     }
            // }
            // else
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

    public ArrayList<String> observerIdsForVertex(String vertexId)
    {
        ArrayList<String> observers = new ArrayList<String>();

        ArrayList<String> edgeIdsForVertex = incidenceList.get(vertexId);

        for (String edgeId : edgeIdsForVertex)
        {
            AbstractEdge edge = edges.get(edgeId);
            if (edge.secondVertexId().equals(vertexId))
            {
                observers.add(edge.firstVertexId());
            }
        }

        return observers;
    }

    public ArrayList<AbstractVertex> observersForVertex(String vertexId)
    {
        ArrayList<String> observerIds = observerIdsForVertex(vertexId);
        ArrayList<AbstractVertex> observers = new ArrayList<AbstractVertex>();

        for (String observerId : observerIds)
        {
            AbstractVertex observer = vertices.get(observerId);
            observers.add(observer);
        }

        return observers;
    }

    public void agentDidDonate(String agentId, boolean didDonate)
    {
        possibleDonationCount++;

        if (didDonate)
        {
            donationCount++;
        }
    }

    public double donationRate()
    {
        if (possibleDonationCount == 0)
        {
            return 0;
        }
        
        return (double) donationCount / (double) possibleDonationCount;
    }

    public int vertexCount()
    {
        return vertexList.size();
    }

    public void setPercentageOfVerticesAsCheaters(double percentage)
    {
        int numNodesToConvert = (int)Math.round(((double)vertexCount()) * percentage);

        while (numNodesToConvert > 0)
        {
            // pick a random vertex
            AbstractVertex randomVertex = randomVertex();

            // if this node is already a cheater, continue
            if (((TagScoreVertex)randomVertex).isCheater())
            {
                continue;
            }

            // turn node into a cheater
            ((TagScoreVertex) randomVertex).setIsCheater(true);

            // decrement number left to convert
            numNodesToConvert--;
        }
    }
    
    public void writeToGraphML(String filename)
    {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        
        try
        {
            fileWriter = new FileWriter(filename);
            bufferedWriter = new BufferedWriter(fileWriter);
        }
        catch (IOException ex)
        {
            System.err.print("Could not open " + filename + " for writing: " + ex.getMessage());
            return;
        }
        
        try
        {
            bufferedWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            bufferedWriter.write("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns/graphml \nxmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance \nxsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns/graphml\">\n");
            
            if (this instanceof DirectedGraph)
            {
                bufferedWriter.write("<graph edgedefault=\"directed\">\n");
            }
            else
            {
                bufferedWriter.write("<graph edgedefault=\"undirected\">\n");
            }
            
            // write nodes
            for (String nodeId : vertices.keySet())
            {
                bufferedWriter.write("<node id \"" + nodeId + "\" />\n");
            }
            
            // write edges
            for (Map.Entry<String, AbstractEdge> edgeEntry : edges.entrySet())
            {
                AbstractEdge edge = edgeEntry.getValue();
                bufferedWriter.write("<edge source=\"" + edge.firstVertexId() + "\" target=\"" + edge.secondVertexId() + "\" />\n");
            }
            
            bufferedWriter.write("</graph>");
            bufferedWriter.write("</graphml>");
            
            bufferedWriter.close();
        }
        catch (IOException ex)
        {
            System.err.println("Could not write to " + filename + ": " + ex.getMessage());
            return;
        }
    }
}
