public class VertexDonatedMessage extends AbstractMessage
{
    public String donorVertexId;
    public String receivingVertexId;

    public VertexDonatedMessage(AbstractVertex donor, AbstractVertex receiver)
    {
        donorVertexId = donor.vertexId();
        receivingVertexId = receiver.vertexId();
    }

    public String toString()
    {
        return "VertexDonated " + donorVertexId + " " + receivingVertexId;
    }
}
