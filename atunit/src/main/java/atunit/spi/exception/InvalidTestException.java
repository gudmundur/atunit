package atunit.spi.exception;

public abstract class InvalidTestException extends Exception {
	private static final long serialVersionUID = 1374867901552474720L;

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
