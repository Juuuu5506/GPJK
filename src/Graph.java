import java.util.ArrayList;

public class Graph {
	private ArrayList<Node> nodes;
	private ArrayList<ArrayList<Node>> criticalPaths = new ArrayList<>();
	
	public Graph( ArrayList<Node> nodes) {
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
}
