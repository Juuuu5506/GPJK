import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class OwnInout implements Inout{
	
	private String heading;

	@Override
	public ArrayList<Node> parseFile(File file) throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		try {
			String line = br.readLine();
			
			while(line != null) {
				line.trim();
				if(line.startsWith("//+")) {
					//Es ist die Überschrift
					heading = line.substring(3).trim();
				}else if(line.startsWith("//")) {
					//Es ist ein Kommentar
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void printFinal(Graph graph) {
		// TODO Auto-generated method stub
		
	}

}
