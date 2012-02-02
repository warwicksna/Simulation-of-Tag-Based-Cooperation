import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.SAXException;

public class GraphMLParser extends DefaultHandler {

    AbstractGraph graph;

    public GraphMLParser (){
        super();
    }

    public AbstractGraph generateGraphFromFile(String graphMLFilename)
    {
        XMLReader saxReader = null;

        try
        {
            saxReader = XMLReaderFactory.createXMLReader();
        }
        catch (SAXException ex)
        {
            System.err.println("Could not create an xml reader to read " + graphMLFilename);
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        saxReader.setContentHandler(this);
        saxReader.setErrorHandler(this);

        FileReader fileReader = null;

        try
        {
            fileReader = new FileReader(graphMLFilename);
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("Could not find file " + graphMLFilename);
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        try
        {
            saxReader.parse(new InputSource(fileReader));
        }
        catch (SAXException ex)
        {
            System.err.println("Error when parsing " + graphMLFilename);
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        catch (IOException ex)
        {
            System.err.println("IO Error when parsing " + graphMLFilename);
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        return graph;
    }



 public void startElement (String uri, String name, String qName, Attributes atts)
    {
        //System.out.println("Start element: " + name );
        if("node".equals(name)){
            double initialTag = RandomGenerator.getInstance().nextDouble();
            double initialTolerance = RandomGenerator.getInstance().nextDouble();
            AbstractVertex newVertex = new TagScoreVertex(graph, atts.getValue("id"), initialTag, initialTolerance);
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
