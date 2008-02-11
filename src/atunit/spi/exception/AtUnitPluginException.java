package atunit.spi.exception;

/**
 * Indicates a problem within the plugin, not related to the test itself.
 * 
 * @author Logan Johnson &lt;logan.johnson@gmail.com&gt;
 */
public class AtUnitPluginException extends Exception {
	private static final long serialVersionUID = 1L;

	public AtUnitPluginException(String message, Throwable cause) {
		super(message, cause);
	}

	public AtUnitPluginException(String message) {
		super(message);
	}

	public AtUnitPluginException(Throwable cause) {
		super(cause);
	}

}
