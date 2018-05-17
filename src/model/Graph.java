import java.util.ArrayList;

public class Graph {
	private ArrayList<Node> nodes;
	private ArrayList<ArrayList<Node>> criticalPaths = new ArrayList<>();
	private int maxAusf;
	
	public Graph( ArrayList<Node> nodes) throws SameIDException {
		ArrayList<Integer> ids = new ArrayList<>();
		for(Node n : nodes) {
			if(ids.contains(n.getId())) {
				throw new SameIDException(""+n.getId());
			}
			ids.add(n.getId());
		}
		this.nodes = nodes;
	}
	
	public void addCritical(ArrayList<Node> critical) {
		ArrayList<Node> cr = new ArrayList<>(critical);
		criticalPaths.add(cr);
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
	
	public ArrayList<Node> getNodes() {
		return nodes;
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
	
	public ArrayList<Node> missedSecPhase() {
		ArrayList<Node> missed = new ArrayList<>();
		
		for(Node n: nodes) {
			if(n.getSaz() == -1) {
				missed.add(n);
			}
		}
		return missed;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Node n : nodes) {
			sb.append(n.toString()+"\n");
		}
		return sb.toString();
	}

	public ArrayList<ArrayList<Node>> getCriticalPaths() {
		return criticalPaths;
	}

	public int getMaxAusf() {
		return maxAusf;
	}

	public void setMaxAusf(int maxAusf) {
		this.maxAusf = maxAusf;
	}

}
