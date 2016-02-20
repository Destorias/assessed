import java.util.*;
import java.io.*;
import networkFlow.*;

/**
 * The Class FordFulk.
 * Contains main part of the Ford-Fulkerson implementation and code for file input
 */
public class FordFulk {

	/** The name of the file that encodes the given network. */
	private String filename;
	
	/** The network on which the Ford-Fulkerson algorithm is to be run. */
	private Network net; 

	/**
	 * Instantiates a new FordFulk object.
	 * @param s the name of the input file
	 */
	public FordFulk(String s) {
		filename = s; // store name of input file
	}

	/**
	 * Read in network from file.
	 * See assessed exercise specification for the file format.
	 */
	public void readNetworkFromFile() {
		FileReader fr = null;
		// open file with name given by filename
		try {
			try {
				fr = new FileReader (filename);
				Scanner in = new Scanner (fr);

				// get number of vertices
				String line = in.nextLine();
				int numVertices = Integer.parseInt(line);

				// create new network with desired number of vertices
				net = new Network (numVertices);

				// now add the edges
				while (in.hasNextLine()) {
					line = in.nextLine();
					String [] tokens = line.split("[( )]+");
					// this line corresponds to add vertices adjacent to vertex u
					int u = Integer.parseInt(tokens[0]);
					// get corresponding Vertex object
					Vertex uu = net.getVertexByIndex(u);
					int i=1;
					while (i<tokens.length) {
						// get label of vertex v adjacent to u
						int v = Integer.parseInt(tokens[i++]);
						// get corresponding Vertex object
						Vertex vv = net.getVertexByIndex(v);
						// get capacity c of (uu,vv)
						int c = Integer.parseInt(tokens[i++]);
						// add edge (uu,vv) with capacity c to network 
						net.addEdge(uu, vv, c);
					}
				}
			}
			finally { 
				if (fr!=null) fr.close();
			}
		}
		catch (IOException e) {
			System.err.println("IO error:");
			System.err.println(e);
			System.exit(1);
		}
	}

	/**
	 * Executes Ford-Fulkerson algorithm on the constructed network net.
	 */
	public void fordFulkerson() {


		while(true) {
			ResidualGraph resGraph = new ResidualGraph(net);
			LinkedList<Edge> augmentingPath = resGraph.findAugmentingPath();
			if(augmentingPath.isEmpty()) {
				System.out.println("Found the max flow");
				break;  // found the max flow
			} else {
				// Find the minimum capacity
				int minCap = Integer.MAX_VALUE;
				for (Edge e : augmentingPath) {
					if (e.getCap() < minCap) minCap = e.getCap();
				}
				StringBuilder testout = new StringBuilder();
				testout.append(Integer.toString(augmentingPath.peek().getSourceVertex().getLabel()));
				// Edit the path according to what we've found in the augmenting path
				for (Edge e : augmentingPath) {
					// if e is a forward edge:
					Vertex source = e.getSourceVertex();
					Vertex target = e.getTargetVertex();
					Edge originalEdge = net.getAdjMatrixEntry(source, target);
					//System.out.println(originalEdge != null);
					//testout.append(" -> " + Integer.toString(originalEdge.getTargetVertex().getLabel()));
					if (net.getAdjList(source).contains(target) && originalEdge.getFlow() + minCap <= originalEdge.getCap()) {
						originalEdge.setFlow(originalEdge.getFlow() + minCap);
					} else {
						System.out.println(net.getAdjList(target).contains(source));
						net.addEdge(source, target, e.getCap());
						originalEdge = net.getAdjMatrixEntry(target, source);
						System.out.println(e.getCap());
						originalEdge.setFlow(net.getAdjMatrixEntry(target, source).getFlow() - minCap);
						
					}
				}
				System.out.println(testout.toString());
			}
			
			net.printFlow();
		}
	}

	/**
	 * Print the results of the execution of the Ford-Fulkerson algorithm.
	 */
	public void printResults() {
		if (net.isFlow()) {
			System.out.println("The assignment is a valid flow");
			System.out.println("A maximum flow has value: "+net.getValue());
			System.out.println("The flows along the edges are as follows:");
			net.printFlow();
		}
		else
			System.out.println("The assignment is not a valid flow");
	}
}