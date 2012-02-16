public class VertexAddedMessage extends AbstractMessage
{
    protected String vertexId;
    protected double tag;
    protected double tolerance;

    public VertexAddedMessage(TagScoreVertex vertex)
    {
        vertexId = vertex.vertexId();
        tag = vertex.tag();
        tolerance = vertex.tolerance();
    }

    public String toString()
    {
        return "VertexAdded " + vertexId + " " + tag + " " + tolerance;
    }

    public String toSqlString()
    {
        return "INSERT INTO Entity (EntityName) VALUES (" + vertexId + ");";
    }
}
