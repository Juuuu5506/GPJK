import java.io.File;

public class OwnController implements Controller {
	
	private Graph graph;
	
	private OwnInout inout;

	@Override
	public void startProgram(File file) {
		graph = new Graph(inout.parseFile(file));
		startAlgo();
	}

	@Override
	public void startAlgo() {
		// TODO Auto-generated method stub
		
	}

}
