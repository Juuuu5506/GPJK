package view;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import exception.SameIDException;
import exception.SyntaxException;
import model.Graph;
import model.Node;

public interface Inout {

	public ArrayList <Node> parseFile(File file) throws FileNotFoundException, SyntaxException, IOException, SameIDException;
	
	public void printFinal(Graph graph, String fileName) throws IOException;
	
	public void printFileNotFound(String notFoundFile, String fileName) throws FileNotFoundException, UnsupportedEncodingException;
	
	public void printException(String exception, String fileName) throws FileNotFoundException, UnsupportedEncodingException;
	
	public void printCycleException(ArrayList<Node> path, String fileName) throws FileNotFoundException, UnsupportedEncodingException;
}
