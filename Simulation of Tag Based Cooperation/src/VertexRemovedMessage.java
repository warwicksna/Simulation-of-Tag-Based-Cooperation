public class VertexRemovedMessage extends AbstractMessage
{
    protected String vertexId;

    public VertexRemovedMessage(AbstractVertex vertex)
    {
        vertexId = vertex.vertexId();
    }

    public String toString()
    {
        return "VertexRemoved " + vertexId;
    }

    public String toSqlString()
    {
        return "DELETE FROM Entity WHERE EntityName = '" + vertexId + "';";
    }
}
