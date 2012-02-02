import java.util.List;

public class CheatingVertex extends TagScoreVertex
{

    public CheatingVertex(AbstractGraph graph, double tag, double tolerance)
    {
        super(graph, tag, tolerance);
    }

    public CheatingVertex(AbstractGraph graph, String vertexId, double tag, double tolerance)
    {
        super(graph, vertexId, tag, tolerance);
    }

    protected void donate()
    {
        List<AbstractVertex> observers = graph.get().observersForVertex(vertexId);

        for (AbstractVertex observer : observers)
        {
            ((TagScoreVertex) observer).neighbourDidDonate(vertexId, false);
        }

        graph.get().agentDidDonate(vertexId, false);
    }

}
