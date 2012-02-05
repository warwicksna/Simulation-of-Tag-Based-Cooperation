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
}
