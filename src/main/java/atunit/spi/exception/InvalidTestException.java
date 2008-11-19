package atunit.spi.exception;

public abstract class InvalidTestException extends Exception {
	
	public InvalidTestException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidTestException(Throwable cause) {
		super(cause);
	}

	public InvalidTestException(String msg) {
		super(msg);
	}
}
