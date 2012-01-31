import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

public class GraphMLParser extends DefaultHandler {

    AbstractGraph graph;
    
    public GraphMLParser (){
        super();
    }

 public void startElement (String uri, String name, String qName, Attributes atts)
    {
        //System.out.println("Start element: " + name );
        if("node".equals(name)){
            AbstractVertex newVertex = new TagScoreVertex(graph, atts.getValue("id"), 0.5, 0.4);
            graph.addVertex(newVertex);
        }
        else if("edge".equals(name)){
            if (atts.getValue("id") != null)
            {
                graph.addEdge(atts.getValue("source"), atts.getValue("target"), atts.getValue("id"));
            }
            else
            {
                graph.addEdge(atts.getValue("source"), atts.getValue("target"));
            }
        }
        else if("graph".equals(name)){
            if("directed".equals(atts.getValue("edgedefault"))){
                graph = new DirectedGraph();
            }
            else{
                graph = new UndirectedGraph();
            }
        }
        return;
    }

    public AbstractGraph getGraph() { return graph;}
}
