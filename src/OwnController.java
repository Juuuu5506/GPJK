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
			inout.printFinal(graph, fileWithoutEnding+".out");
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
		int maxAusf = 0;

		// Phase 1
		next.addAll(starters);
		while (next.size() > 0 && counter < 400) {
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
			}
			next.remove(n);
			counter++;
		}
		System.out.println(graph.toString());
		if (counter == 400) {
			// Erneute Suche nach Zyklus
		}

		if (graph.unvisitedNodes().size() > 0) {
			// Unverbundnener Graph
			System.out.println("Es exisiert ein unzusammenhängender Graph. Unbesucht sind:");
			for (Node m : graph.unvisitedNodes()) {
				System.out.println(m.toString());
			}
			System.out.println("Unbesucht vorbei.");
		}

		next.addAll(enders);
		for (Node m : enders) {
			if(m.getFez() > maxAusf) {
				maxAusf = m.getFez();
			}
			m.setSez(m.getFez());
		}
		
		graph.setMaxAusf(maxAusf);
		
		while (next.size() > 0 && counter < 400) {
			n = next.get(0);
			n.setSaz(n.getSez() - n.getD());
			for (int i : n.getVorgID()) {
				tmp = graph.getNodeByID(i);
				tmp.setSez(n.getSaz());
				next.trimToSize();
				next.add(tmp);
			}
			next.remove(n);
			counter++;
		}

		if (graph.missedSecPhase().size() > 0 || counter == 400) {
			System.out.println("Es existiert ein Fehler in den Vorgängerangaben.");
		}

		System.out.println(graph.toString());

		// Phase 3
		int faz;
		int tmpz;
		for (Node s : graph.getNodes()) {
			next.clear();
			s.setGp(s.getSaz() - s.getFaz());
			if (s.getNachfID().size() == 0) {
				s.setFp(0);
			} else {
				faz = Integer.MAX_VALUE;
				for (int i : s.getNachfID()) {
					tmpz = graph.getNodeByID(i).getFaz();
					if (tmpz < faz) {
						faz = tmpz;
					}
				}

				s.setFp(faz - s.getFez());
			}
			if (s.getGp() == 0 && s.getFp() == 0) {
				s.setCritical(true);
			}
		}
		System.out.println("Phase 3");
		System.out.println(graph.toString());

		// Suche nach Kritischen Pfaden

		ArrayList<Node> criticals = new ArrayList<>();
		ArrayList<Node> crPath = new ArrayList<>();
		boolean allFound = false;
		boolean ending = false;
		boolean beginning = false;
		Node tn;
		Node tmpn;
		for (Node u : graph.getNodes()) {
			if (u.isCritical()) {
				criticals.add(u);
			}
		}

		while (!allFound) {
			tn = criticals.get(0);
			crPath.add(tn);
			if(tn.getNachfID().size() == 0) {
				ending = true;
			} else if (tn.getVorgID().size() == 0) {
				beginning = true;
			}

			endless: while (true) {
				criticals.remove(tn);
				if (!ending) {
					if (tn.getNachfID().size() == 0) {
						ending = true;
					} else {
						for (int i : tn.getNachfID()) {
							tmpn = graph.getNodeByID(i);
							if (tmpn.isCritical()) {
								tn = tmpn;
								System.out.println(tn.toString());
								crPath.add(tn);
								continue endless;
							}
						}
					}
				}

				if (!beginning) {

					if (tn.getVorgID().size() == 0) {
						beginning = true;
					} else {
						if (tn.getVorgID().size() != 0) {
							for (int i : tn.getVorgID()) {
								tmpn = graph.getNodeByID(i);
								if (tmpn.isCritical()) {
									tn = tmpn;
									System.out.println(tn.toString());
									crPath.add(0, tn);
									continue endless;
								}
							}
						}
					}
				}

				graph.addCritical(crPath);
				System.out.println();
				for (Node sy : crPath) {
					System.out.print(sy.toString() + "->");
				}
				break endless;

			}

			if (criticals.isEmpty()) {
				allFound = true;
			}
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
