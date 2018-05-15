import java.util.ArrayList;

public class Node {

	private int id;
	private String name;
	private int d;
	private int faz;
	private int fez = -1;
	private int saz = -1;
	private int sez;
	private int gp = -1;
	private int fp;
	private ArrayList<Integer> vorgID = new ArrayList<>();
	private ArrayList<Integer> nachfID = new ArrayList<>();
	private boolean critical = false;
	private boolean startingNode;
	private boolean endingNode;
	private int lastFazIncrease = 0; // Um Zyklen aufzuspüren
	private int secLastFazIncrease = 0;
	private int thrLastFazIncrease = 0;
	private int increased = 0;
	
	public Node(int id, String name, int d, ArrayList<Integer> vorg, ArrayList<Integer> nachf) {
		this.id = id;
		this.name = name;
		this.d = d;
		this.vorgID.addAll(vorg);
		this.nachfID.addAll(nachf);
		
		if(this.vorgID.size() == 0) {
			this.startingNode = true;
			this.faz = 0;
		}else {
			this.startingNode = false;
		}
		
		if(this.nachfID.size() == 0) {
			endingNode = true;
		} else {
			endingNode = false;
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if(fez == -1) { //Es wurden noch keine Berechnungen vorgenommen.
			sb.append(id+"; ");
			sb.append(name+"; ");
			sb.append(d+"; ");
			if(vorgID.size() == 0) {
				sb.append("-");
			}
			for(int i = 0; i < vorgID.size(); i++) {
				sb.append(vorgID.get(i));
				if(i < (vorgID.size()-1)) {
					sb.append(", ");
				}
			}
			sb.append("; ");
			if(nachfID.size() == 0) {
				sb.append("-");
			}
			for(int i = 0; i < nachfID.size(); i++) {
				sb.append(nachfID.get(i));
				if(i < (nachfID.size()-1)) {
					sb.append(", ");
				}
			}
			return sb.toString();
			
			
		} else if(saz == -1) { //Es wurde vorwärts aber noch nicht rückwärts gerechnet.
			sb.append(id+"; ");
			sb.append(name+"; ");
			sb.append(d+"; ");
			if(vorgID.size() == 0) {
				sb.append("-");
			}
			for(int i = 0; i < vorgID.size(); i++) {
				sb.append(vorgID.get(i));
				if(i < (vorgID.size()-1)) {
					sb.append(", ");
				}
			}
			sb.append("; ");
			if(nachfID.size() == 0) {
				sb.append("-");
			}
			for(int i = 0; i < nachfID.size(); i++) {
				sb.append(nachfID.get(i));
				if(i < (nachfID.size()-1)) {
					sb.append(", ");
				}
			}
			
			sb.append("; "+ faz);
			sb.append("; "+ fez);
			return sb.toString();
			
			
		} else if(gp == -1) {  //Es wurde rückwärts gerechnet aber die Puffer fehlen noch. 
			sb.append(id+"; ");
			sb.append(name+"; ");
			sb.append(d+"; ");
			if(vorgID.size() == 0) {
				sb.append("-");
			}
			for(int i = 0; i < vorgID.size(); i++) {
				sb.append(vorgID.get(i));
				if(i < (vorgID.size()-1)) {
					sb.append(", ");
				}
			}
			sb.append("; ");
			if(nachfID.size() == 0) {
				sb.append("-");
			}
			for(int i = 0; i < nachfID.size(); i++) {
				sb.append(nachfID.get(i));
				if(i < (nachfID.size()-1)) {
					sb.append(", ");
				}
			}
			return sb.toString();
		}
		return null;
	}
	
	/**
	 * Gibt an, ob die Gefahr eines Zykluses besteht.
	 * @return true oder false
	 */
	public boolean cycleDanger() {
		
		/* Wenn sich drei mal nacheinander der FAZ um mindestens seine Bearbeitungsdauer erhöht, ist
		 * die Wahrscheinlichkeit sehr hoch, dass es sich um einen Zyklus handelt. Falls d jedoch klein ist,
		 * kann es schnell passieren, dass d drei mal nacheinander überboten wurde. Für diesen Fall ist eine Mindestgrenze 
		 * von 10 miteingebaut.
		 */
		if(increased < 3) {
			return false;
		}
		if(((thrLastFazIncrease >= d) && (thrLastFazIncrease >= 10)) &&
			((secLastFazIncrease >= d) && (secLastFazIncrease >= 10)) &&
			((lastFazIncrease >= d) && (lastFazIncrease >= 10)) ||
								increased > 50) {
			return true;			
		} else {
			return false;
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
		thrLastFazIncrease = secLastFazIncrease;
		secLastFazIncrease = lastFazIncrease;
		lastFazIncrease = this.faz-faz;
		if(faz > this.faz) {
			this.faz = faz;
		}
		increased++;
		
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

	public ArrayList<Integer> getVorgID() {
		return vorgID;
	}

	public void setVorgID(ArrayList<Integer> vorgID) {
		this.vorgID = vorgID;
	}

	public ArrayList<Integer> getNachfID() {
		return nachfID;
	}

	public void setNachfID(ArrayList<Integer> nachfID) {
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

	public int getLastFazIncrease() {
		return lastFazIncrease;
	}

	public void setLastFazIncrease(int lastFazIncrease) {
		this.lastFazIncrease = lastFazIncrease;
	}

	public int getSecLastFazIncrease() {
		return secLastFazIncrease;
	}

	public void setSecLastFazIncrease(int secLastFazIncrease) {
		this.secLastFazIncrease = secLastFazIncrease;
	}
}
