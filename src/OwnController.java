import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class OwnController implements Controller {

	private Graph graph;

	private Inout inout = new OwnInout();

	String fileName;
	String fileWithoutEnding;

	@Override
	public void startProgram(String fileName) {
		this.fileName = fileName;
		fileWithoutEnding = fileName.split("\\.")[0];
		try {
			graph = new Graph(inout.parseFile(new File(fileName)));
			startAlgo();
		} catch (FileNotFoundException e) {
			try {
				inout.printFileNotFound(fileName, fileWithoutEnding + ".err");
			} catch (FileNotFoundException | UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		} catch (SyntaxException e) {

			try {
				inout.printException("Es besteht eine Fehler in Zeile " + e.getMessage() + " der Eingabedatei.",
						fileWithoutEnding + ".err");
			} catch (FileNotFoundException | UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SameIDException e) {
			try {
				inout.printException(
						"Die Zahl " + e.getMessage() + " wurde mindestens " + "zwei mal als Vorgangsnummer angegeben.",
						fileWithoutEnding + ".err");
			} catch (FileNotFoundException | UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

		}

	}

	@Override
	public void startAlgo() {
		ArrayList<Node> starters = graph.getStartNodes();
		ArrayList<Node> enders = graph.getEndNodes();
		ArrayList<Node> next = new ArrayList<>();
		Node tmp;
		Node n;
		System.out.println(starters.size());
		System.out.println(enders.size());
		boolean alreadyLooked = false;
		int counter = 0;
		
		// Phase 1
		next.addAll(starters);
		while (next.size() > 0 && counter < 10) {
			n = next.get(0);
			if (n.cycleDanger() && !alreadyLooked) {
				lookForCycle(n);
				alreadyLooked = true;
			}
			n.setFez(n.getFaz() + n.getD());
			for (int i : n.getNachfID()) {
				tmp = graph.getNodeByID(i);
				tmp.setFaz(n.getFez());
				System.out.println(tmp.getFaz());
				next.trimToSize();
				next.add(tmp);
				next.remove(n);
			}
			counter++;
		}
		System.out.println(graph.toString());
		if(counter == 400) {
			//Erneute Suche nach Zyklus
		}
		
		if(graph.unvisitedNodes().size() > 0) {
			//Unverbundnener Graph
		}

	}

	public void lookForCycle(Node n) {
		ArrayList<Node> nodes = new ArrayList<>();
		ArrayList<Node> next = new ArrayList<>();
		ArrayList<Node> cycle = new ArrayList<>();
		ArrayList<Integer> ids = new ArrayList<>();
		Node tmp;
		int counter = 0;
		next.add(n);
		while (next.size() > 0 || counter <= 400) {
			tmp = next.get(0);
			if (nodes.contains(tmp)) {
				if (cycle.contains(tmp)) {
					cycle.add(tmp);
					next.clear();
				} else {
					cycle.add(tmp);
				}
			} else {
				nodes.add(tmp);
				ids.addAll(tmp.getNachfID());
				for (int i : ids) {
					next.add(graph.getNodeByID(i));
				}
			}
			next.remove(tmp);
			next.trimToSize();
			counter++;
		}
		if (counter < 400) {
			try {

				inout.printCycleException(cycle, fileWithoutEnding + ".err");

			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

}
