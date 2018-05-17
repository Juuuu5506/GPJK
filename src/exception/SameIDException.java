
public class SameIDException extends Exception {
	
	/*
	 * Bekommt die Nr der doppelt vergebenen ID als Message übergeben.
	 */
	public SameIDException(String message) {
		super(message);
	}
}
