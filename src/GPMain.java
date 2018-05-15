import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GPMain {
	public static void main(String [] args) throws FileNotFoundException {
		System.out.println(args[0]);
		
		OwnController con = new OwnController();
		con.startProgram(args[0]);
	}
}
