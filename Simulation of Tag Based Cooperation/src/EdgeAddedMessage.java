public class EdgeAddedMessage extends AbstractMessage
{
    protected String edgeId;
    protected boolean isDirected;
    protected String vertexAId;
    protected String vertexBId;

    public EdgeAddedMessage(AbstractEdge edge)
    {
        edgeId = edge.edgeId();
        isDirected = (edge instanceof DirectedEdge);
        vertexAId = edge.firstVertexId();
        vertexBId = edge.secondVertexId();
    }

    public String toString()
    {
        return "EdgeAdded " + edgeId + " " + isDirected + " " + vertexAId + " " + vertexBId;
    }

    public String toSqlString()
    {
        return "INSERT INTO Connected (FromEntityId, ToEntityId, Strength, Initial) VALUES (" + vertexAId + ", " + vertexBId + ", 1, 0);";
    }
}
