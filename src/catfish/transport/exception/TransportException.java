package catfish.transport.exception;

public class TransportException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2568492927761608876L;

	public TransportException(String message, Throwable t) {
        super(message, t);
    }
    
    public TransportException(String message) {
        super(message);
    }
}
