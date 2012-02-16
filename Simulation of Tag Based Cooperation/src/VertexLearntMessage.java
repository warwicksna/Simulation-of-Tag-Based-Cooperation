public class VertexLearntMessage extends AbstractMessage
{
    protected String vertexId;
    protected String donorId;
    protected double newTag;
    protected double newTolerance;

    public VertexLearntMessage(TagScoreVertex learner, AbstractVertex donorVertex)
    {
        vertexId = learner.vertexId();
        donorId = donorVertex.vertexId();
        newTag = learner.tag();
        newTolerance = learner.tolerance();
    }

    public String toString()
    {
        return "VertexLearnt " + vertexId + " " + donorId + " " + newTag + " " + newTolerance;
    }

    public String toSqlString()
    {
        return "INSERT INTO MessageProperties (Data, TimeStamp) VALUES ('\"message\":{\"type\": learnt, \"vertex\": vertexId, \"donor\": donorId, \"tag\": newTag, \"tolerance\": newTolerance}', 0);";
    }
}
