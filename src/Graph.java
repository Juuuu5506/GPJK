import java.util.ArrayList;

public class Graph {
	private ArrayList<Node> nodes;
	private ArrayList<ArrayList<Node>> criticalPaths = new ArrayList<>();
	
	public Graph( ArrayList<Node> nodes) {
		for(Node n : nodes) {
			System.out.println(n.toString());
		}
		this.nodes = nodes;
	}
	
	public void addCritical(ArrayList<Node> critical) {
		criticalPaths.add(critical);
	}
	
	public Node getNodeByID(int id) {
		for(Node n : nodes) {
			if(n.getId() == id) {
				return n;
				
			}
		}
		return null;
	}
	
	public ArrayList<Node> getStartNodes() {
		ArrayList <Node> starters = new ArrayList<>();
		for(Node n: nodes) {
			if(n.isStartingNode()) {
				starters.add(n);
			}
		}
		return starters;
	}
	
	public ArrayList<Node> getEndNodes() {
		ArrayList <Node> enders = new ArrayList<>();
		for(Node n: nodes) {
			if(n.isEndingNode()) {
				enders.add(n);
			}
		}
		return enders;
	}
	
	public ArrayList<Node> unvisitedNodes() {
		ArrayList<Node> unvisited = new ArrayList<>();
		
		for(Node n: nodes) {
			if(n.getFez() == -1) {
				unvisited.add(n);
			}
		}
		
		return unvisited;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Node n : nodes) {
			sb.append(n.toString()+"\n");
		}
		return sb.toString();
	}
}
