import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class OwnInout implements Inout {

	private String heading = "";

	@Override
	public ArrayList<Node> parseFile(File file) throws FileNotFoundException, SyntaxException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		ArrayList<Node> nodes = new ArrayList<>();
		String[] splited;
		String[] hilfsString;
		ArrayList<Integer> hilfsInteger = new ArrayList<>();
		int id;
		String bez;
		int d;
		ArrayList<Integer> vorg = new ArrayList<>();
		ArrayList<Integer> nachf = new ArrayList<>();
		int row = 0;
		try {
			String line = br.readLine();
			row+=1;
			while (line != null) {
				
				line.trim();
				if (line.isEmpty()) {

				} else if (line.startsWith("//+")) {
					// Es ist die Überschrift
					heading += " "+line.substring(3).trim();
				} else if (line.startsWith("//")) {
					// Es ist ein Kommentar
				} else {
					// Ein Knoten
					splited = line.split(";");
					if (splited.length < 5) {
						throw new SyntaxException(row+"");
						// Schwerwiegende fehlerhafte Eingabe der Semikoli.
						// Wäre es mehr als fünf würde der Rest ignoriert werden.
					} else {
						id = Integer.parseInt(splited[0].trim());
						bez = splited[1].trim();
						d = Integer.parseInt(splited[2].trim());

						if (splited[3].trim().equals("-")) {
							//Kein Vorgängerknoten
						} else {
							hilfsString = splited[3].split(",");
							for (String s : hilfsString) {
								vorg.add(Integer.parseInt(s.trim()));
							}

						}

						if (splited[4].trim().equals("-")) {
							//Kein Nachfolgerknoten
						} else {
							hilfsString = splited[4].split(",");
							for (String s : hilfsString) {
								nachf.add(Integer.parseInt(s.trim()));
							}
						}
						// nodes.add(new Node(id, bez, d, vorg, nachf));
						Node n = new Node(id, bez, d, vorg, nachf);
						nodes.add(n);
						nachf.clear();
						vorg.clear();
					}
				}
				line = br.readLine();
			}
			br.close();
			return nodes;
		} catch (IOException e) {
			throw e;
		} catch (NumberFormatException nfe) {
			throw new SyntaxException(row+"");
		}		
	}

	@Override
	/**
	 * Erstellt eine Datei mit dem übergebenen @fileName.
	 * Dort wird der Netzplan hineingeschrieben.
	 */
	public void printFinal(Graph graph, String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		ArrayList<Node> starters = graph.getStartNodes();
		ArrayList<Node> enders = graph.getEndNodes();
		PrintWriter pw = new PrintWriter(fileName, "UTF-8");
		pw.println(heading);
		pw.println();
		pw.println("Vorgangsnummer; Vorgangsbezeichnung; D; FAZ; FEZ; SAZ; SEZ; GP; FP");
		pw.println(graph.toString());
		
		pw.println();
		pw.print("Anfangsvorgaenge: ");
		for(int i = 0; i < starters.size(); i++) {
			pw.print(starters.get(i).getId());
			if(i < starters.size()-1) {
				pw.print(", ");
			}
		}
		
		pw.println();
		pw.print("Endvorgaenge: ");
		for(int i = 0; i < enders.size(); i++) {
			pw.print(enders.get(i).getId());
			if(i < enders.size()-1) {
				pw.print(", ");
			}
		}
		
		pw.println();
		pw.print("Gesamtdauer: ");
		pw.print(graph.getMaxAusf());
		
		pw.println();
		pw.println();
		pw.println("Kritische Pfade: ");
		for(ArrayList<Node> aln: graph.getCriticalPaths()) {
			for(int i = 0; i < aln.size(); i++) {
				pw.print(aln.get(i).getId());
				if(i < aln.size()-1) {
					pw.print("->");
				}
			}
			pw.println();
		}
		pw.close();
	}
	
	public void printFileNotFound(String notFoundFile, String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter pw = new PrintWriter(fileName, "UTF-8");
		pw.println("Die Datei "+ notFoundFile+" wurde nicht gefunden.");
		pw.close();
	}
	
	public void printException(String exception, String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter pw = new PrintWriter(fileName, "UTF-8");
		pw.println(exception);
		pw.close();
	}

	@Override
	public void printCycleException(ArrayList<Node> path, String fileName)
			throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
	}

}
