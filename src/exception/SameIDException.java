
public class SameIDException extends Exception {
	
	/*
	 * Bekommt die Nr der doppelt vergebenen ID als Message �bergeben.
	 */
	public SameIDException(String message) {
		super(message);
	}
}
