package catfish.transport.http;

import catfish.transport.exception.TransportException;

public class HTTPTransportException extends TransportException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2387964239357730857L;

	public HTTPTransportException(String message) {
		super(message);
	}
}
