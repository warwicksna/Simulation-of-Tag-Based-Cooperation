public class TimeIntervalBegunMessage extends AbstractMessage
{
    protected static int nextTimeInterval = 0;
    protected int timeInterval = 0;

    public TimeIntervalBegunMessage()
    {
        timeInterval = nextTimeInterval++;
    }

    public String toString()
    {
        return "TimeIntervalBegun " + timeInterval;
    }
}
