import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public interface Inout {

	public ArrayList <Node> parseFile(File file) throws FileNotFoundException;
	
	public void printFinal(Graph graph);
}
