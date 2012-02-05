public class EdgeRemovedMessage extends AbstractMessage
{
    protected String edgeId;

    public EdgeRemovedMessage(AbstractEdge edge)
    {
        edgeId = edge.edgeId();
    }

    public String toString()
    {
        return "EdgeRemoved " + edgeId;
    }
}
