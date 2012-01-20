import java.io.FileReader;

import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;

public class CooperationSimulation
{	
	public static void main(String args[])
	{		
<<<<<<< HEAD
		//new CooperationSimulation(true);
		//new CooperationSimulation(false);
=======
		new CooperationSimulation();
		new CooperationSimulation(true);
		new CooperationSimulation(false);
>>>>>>> Added ability to remove vertices
		new CooperationSimulation("test_graph.xml");
	}
	
	public CooperationSimulation()
	{
		System.out.println("Testing removing vertices");
		AbstractGraph graph = new UndirectedGraph();
		
		AbstractVertex n0 = new TagScoreVertex(graph, "n0", 0.5, 0.4);
		graph.addVertex(n0);
		
		AbstractVertex n1 = new TagScoreVertex(graph, "n1", 0.1, 0.1);
		graph.addVertex(n1);
		
		graph.addEdge("n0", "n1", "e1");
		
		graph.listVertices();
		graph.listEdges();
		
		graph.removeVertex("n0");
		
		AbstractVertex n2 = new TagScoreVertex(graph, "n2", 0.9, 0.9);
		graph.addVertex(n2);
		
		graph.addEdge("n1", "n2", "e2");
		
		graph.listVertices();
		graph.listEdges();
		
		graph.removeEdge("e2");
		
		graph.listVertices();
		graph.listEdges();
	}
	
	public CooperationSimulation(boolean shouldUseDirectedGraph)
	{
		AbstractGraph graph;
		
		if (shouldUseDirectedGraph)
		{
			graph = new DirectedGraph();
			System.out.println("Testing Directed Graphs");
		}
		else
		{
			graph = new UndirectedGraph();
			System.out.println("Testing Undirected Graphs");
		}
		
		AbstractVertex newVertex1 = new TagScoreVertex(graph, "Hello", 0.5, 0.4);
		graph.addVertex(newVertex1);
		
		AbstractVertex newVertex2 = new TagScoreVertex(graph, "World", 0.7, 0.2);
		graph.addVertex(newVertex2);
		
		graph.addEdge(newVertex1, newVertex2, "Goodbye");
		
		System.out.println("List of vertices");
		graph.listVertices();
		System.out.println("List of edges for each vertex");
		graph.listEdges();
	}
	
	public CooperationSimulation(String graphMLFilename)
	{
                try{

                XMLReader xr = XMLReaderFactory.createXMLReader();
                GraphMLParser handler = new GraphMLParser();
                xr.setContentHandler(handler);
                xr.setErrorHandler(handler);

                xr.parse(new InputSource(new FileReader(graphMLFilename)));
                AbstractGraph graph = handler.getGraph();
                
                System.out.println("List of vertices");
                graph.listVertices();
                System.out.println("List of edges for each vertex");
                graph.listEdges();
                }catch(Exception e){e.printStackTrace();}

        }
		// read graphml
		// create directed graph / undirected graph based on xml
		// construct graph from xml
		
		// if read vertex:
		// vertex = new TagScoreVertex(graph, (vertex id), tag, tolerance) (vertex id is optional, recommended)
		// graph.addVertex(vertex)
		// if read edge:
		// graph.addEdge(vertex1 id, vertex2 id, (edge id)) (edge id is optional, recommended)
	
}
