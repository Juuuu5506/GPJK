import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GPMain {
	public static void main(String [] args) {
		System.out.println("A");
		Scanner sc;
		try {
			sc = new Scanner(new File(args[0]));
			System.out.println(sc.nextLine());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
