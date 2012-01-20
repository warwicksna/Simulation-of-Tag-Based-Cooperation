import java.io.FileReader;

import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;

public class CooperationSimulation
{	
	public static void main(String args[])
	{		
		//new CooperationSimulation(true);
		//new CooperationSimulation(false);
		new CooperationSimulation("test_graph.xml");
	}
	
	public CooperationSimulation(boolean shouldUseDirectedGraph)
	{
		AbstractGraph graph;
		
		if (shouldUseDirectedGraph)
		{
			graph = new DirectedGraph();
			System.out.println("Created Directed Graph");
		}
		else
		{
			graph = new UndirectedGraph();
			System.out.println("Created Undirected Graph");
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
