import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.tools.DiagnosticListener;

public class OwnController implements Controller {

	private Graph graph;

	private Inout inout = new OwnInout();

	private String fileName;
	private String fileWithoutEnding;
	private ArrayList<Integer> warnings = new ArrayList<>();

	@Override
	public void startProgram(String fileName) {
		this.fileName = fileName;
		String[] tmp = fileName.split("\\.");
		fileWithoutEnding = tmp[tmp.length - 2];
		tmp = fileWithoutEnding.split("\\\\");
		fileWithoutEnding = tmp[tmp.length - 1];
		try {
			graph = new Graph(inout.parseFile(new File(fileName)));
			startAlgo();
			inout.printFinal(graph, fileWithoutEnding + ".out");
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
						"Die Zahl " + e.getMessage() + " wurde mindestens zwei mal als Vorgangsnummer angegeben.",
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
		int counter = 0;
		int maxAusf = 0;
		try {
			if (starters.size() == 0) {
				inout.printException("Es gibt keine Startknoten.", fileWithoutEnding + ".err");
				System.exit(0);
			}
			if (enders.size() == 0) {
				inout.printException("Es gibt keine Endknoten.", fileWithoutEnding + ".err");
				System.exit(0);
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// Phase 1
		next.addAll(starters);
		while (next.size() > 0 && counter < Integer.MAX_VALUE) {
			n = next.get(0);
			if (n.cycleDanger()) {
				lookForCycle(n);
			}
			n.setFez(n.getFaz() + n.getD());
			for (int i : n.getNachfID()) {
				if (graph.getNodeByID(i) == null) {
					try {
						inout.printException("Knoten " + n.getId() + " verweist auf einen nicht existenten Knoten.",
								fileWithoutEnding + ".err");
						System.exit(0);
					} catch (FileNotFoundException | UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				tmp = graph.getNodeByID(i);
				tmp.setFaz(n.getFez());
				next.trimToSize();
				next.add(tmp);
			}
			next.remove(n);
			counter++;
		}
		System.out.println(graph.toString());
		if (counter == Integer.MAX_VALUE) {
			try {
				inout.printException("Es gab einen Fehler mit einem unentdeckten Zyklus", fileWithoutEnding + ".err");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			System.exit(0);
		}

		if (graph.unvisitedNodes().size() > 0) {
			// Unverbundnener Graph
			StringBuilder sb = new StringBuilder();
			sb.append("Es exisiert ein unzusammenhaengender Graph. Unbesucht sind: \n");
			for (Node m : graph.unvisitedNodes()) {
				sb.append(m.toString() + "\n");
			}

			try {
				inout.printException(sb.toString(), fileWithoutEnding + ".err");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			System.exit(0);
		}

		// Phase 2
		next.addAll(enders);
		for (Node m : enders) {
			if (m.getFez() > maxAusf) {
				maxAusf = m.getFez();
			}
		}

		for (Node m : enders) {
			m.setSez(maxAusf);
		}

		graph.setMaxAusf(maxAusf);

		while (next.size() > 0 && counter < 400) {
			n = next.get(0);
			n.setSaz(n.getSez() - n.getD());
			for (int i : n.getVorgID()) {
				if (graph.getNodeByID(i) == null) {
					try {
						inout.printException("Knoten " + n.getId() + " verweist auf einen nicht existenten Knoten.",
								fileWithoutEnding + ".err");
						System.exit(0);
					} catch (FileNotFoundException | UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
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
			try {
				inout.printException("Es existiert ein Fehler in den Vorgängerangaben der Knoten.",
						fileWithoutEnding + ".err");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
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

		ArrayList<Node> criticalsLeft = new ArrayList<>();
		ArrayList<Node> crPath = new ArrayList<>();
		boolean allFound = false;
		boolean ending = false;
		boolean beginning = false;
		Node tn;
		Node tmpn;
		Node mid;
		for (Node u : graph.getNodes()) {
			if (u.isCritical()) {
				criticalsLeft.add(u);
			}
		}
		mid = criticalsLeft.get(0);
		while (!allFound) {
			tn = criticalsLeft.get(0);
			crPath.add(tn);
			if (tn.isEndingNode()) { // Wenn man zufällig einen Endknoten hat.
				ending = true;
			} else if (tn.isStartingNode()) { // Wenn man zufällig einen Anfangsknoten hat.
				beginning = true;
			}

			endless: while (true) {
				criticalsLeft.remove(tn);
				if (!ending) {
					if (tn.isEndingNode()) {
						ending = true;
						tn = mid;
					} else {
						for (int i : tn.getNachfID()) {
							tmpn = graph.getNodeByID(i);
							if (tmpn.isCritical()) {
								tn = tmpn;
								crPath.add(tn);
								continue endless;
							}
						}
					}
				}

				if (!beginning) {

					if (tn.isStartingNode()) {
						beginning = true;
					} else {
						for (int i : tn.getVorgID()) {
							tmpn = graph.getNodeByID(i);
							if (tmpn.isCritical()) {
								tn = tmpn;
								crPath.add(0, tn);
								continue endless;

							}
						}
					}
				}

				graph.addCritical(crPath);
				System.out.println();
				for (Node sy : crPath) {
					System.out.print(sy.getId() + "->");
				}
				crPath.clear();
				beginning = false;
				ending = false;
				break endless;

			}

			if (criticalsLeft.isEmpty()) {
				allFound = true;
			}
		}

	}

	public void lookForCycle(Node n) {
		ArrayList<Node> already = new ArrayList<>();
		ArrayList<Node> next = new ArrayList<>();
		ArrayList<Node> cycle = new ArrayList<>();
		ArrayList<Integer> ids = new ArrayList<>();
		Node tmp;
		int counter = 0;
		next.add(n);
		while (next.size() > 0 && counter <= 400) {
			tmp = next.get(0);
			if (already.contains(tmp)) { // Wenn der Knoten bereits einmal bearbeitet wurde, ist er Teil des Zykluses.
				if (cycle.contains(tmp)) { // Falls der Knoten bereits in der Liste des Zykluses vorhanden ist, ist man
											// einmal
					cycle.add(tmp); // durch. Somit wurde ein Zyklus komplett gefunden und die Suche kann beendet
									// werden.
					ids.clear();
					next.clear();

				} else { // Fuer den Fall, dass der Knoten noch nicht im Zyklus aufgenommen wurde, jedoch
							// schon zwei mal
					cycle.add(tmp); // ueberrueft wurde, wird er nun dem Zyklus zugefuegt.
					ids.addAll(tmp.getNachfID()); // Alle Folgeknoten werden hinzugefügt.
					for (int i : ids) {
						next.add(graph.getNodeByID(i));
					}
					ids.clear();
					next.remove(tmp);
				}
			} else {
				already.add(tmp); // Wird der Knote das erste Mal ueberlaufe, muss er already hinzugefuegt werden.
				ids.addAll(tmp.getNachfID()); // Alle Folgeknoten werden
				for (int i : ids) {
					next.add(graph.getNodeByID(i));
				}
				ids.clear();
			}
			next.remove(tmp);
			next.trimToSize();
			counter++;
		}
		if (counter < 400) {
			try {
				System.out.println();
				inout.printCycleException(cycle, fileWithoutEnding + ".err");
				System.exit(0);

			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

}
