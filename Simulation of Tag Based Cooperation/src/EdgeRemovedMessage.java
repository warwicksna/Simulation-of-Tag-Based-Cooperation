public class EdgeRemovedMessage extends AbstractMessage
{
    protected String edgeId;
    protected String vertexAId;
    protected String vertexBId;

    public EdgeRemovedMessage(AbstractEdge edge)
    {
        edgeId = edge.edgeId();
        vertexAId = edge.firstVertexId();
        vertexBId = edge.secondVertexId();
    }

    public String toString()
    {
        return "EdgeRemoved " + edgeId;
    }

    public String toSqlString()
    {
        return "DELETE FROM Connected WHERE FromEntityId = '" + vertexAId + "' AND ToEntityId = '" + vertexBId + ";";
    }
}
