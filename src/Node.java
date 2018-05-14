
public class Node {

	private int id;
	private String name;
	private int d;
	private int faz;
	private int fez;
	private int saz;
	private int sez;
	private int gp;
	private int fp;
	private int[] vorgID;
	private int[] nachfID;
	private boolean critical = false;
	private boolean startingNode;
	private boolean endingNode;
	
	public Node(int id, String name, int d, int[] vorg, int[] nachf) {
		this.id = id;
		this.name = name;
		this.d = d;
		this.vorgID = vorg;
		this.nachfID = nachf;
		
		if(this.vorgID.length == 0) {
			startingNode = true;
		}else {
			startingNode = false;
		}
		
		if(this.nachfID.length == 0) {
			endingNode = true;
		} else {
			endingNode = false;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getD() {
		return d;
	}

	public void setD(int d) {
		this.d = d;
	}

	public int getFaz() {
		return faz;
	}

	public void setFaz(int faz) {
		this.faz = faz;
	}

	public int getFez() {
		return fez;
	}

	public void setFez(int fez) {
		this.fez = fez;
	}

	public int getSaz() {
		return saz;
	}

	public void setSaz(int saz) {
		this.saz = saz;
	}

	public int getSez() {
		return sez;
	}

	public void setSez(int sez) {
		this.sez = sez;
	}

	public int getGp() {
		return gp;
	}

	public void setGp(int gp) {
		this.gp = gp;
	}

	public int getFp() {
		return fp;
	}

	public void setFp(int fp) {
		this.fp = fp;
	}

	public int[] getVorgID() {
		return vorgID;
	}

	public void setVorgID(int[] vorgID) {
		this.vorgID = vorgID;
	}

	public int[] getNachfID() {
		return nachfID;
	}

	public void setNachfID(int[] nachfID) {
		this.nachfID = nachfID;
	}

	public boolean isCritical() {
		return critical;
	}

	public void setCritical(boolean critical) {
		this.critical = critical;
	}

	public boolean isStartingNode() {
		return startingNode;
	}

	public void setStartingNode(boolean startingNode) {
		this.startingNode = startingNode;
	}

	public boolean isEndingNode() {
		return endingNode;
	}

	public void setEndingNode(boolean endingNode) {
		this.endingNode = endingNode;
	}
}
