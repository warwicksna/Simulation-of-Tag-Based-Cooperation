
public class STBC {

	public static void main(String[] args)
	{
		System.out.println("Creating graph");
		Graph graph = new Graph();
		graph.createAgents(100);
		graph.createNeighbours();
		graph.listAgents();
		System.out.println("Beginning simulation");
		graph.play(10);
		System.out.println("Simulation ended");
		graph.printObservations();
	}
	
}
