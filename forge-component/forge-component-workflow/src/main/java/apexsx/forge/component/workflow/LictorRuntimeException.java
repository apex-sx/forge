package apexsx.forge.component.workflow;

/**
 * @author Terraqo
 */
public class LictorRuntimeException extends RuntimeException {
    public LictorRuntimeException() {
    }

    public LictorRuntimeException(String message) {
        super(message);
    }

    public LictorRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public LictorRuntimeException(Throwable cause) {
        super(cause);
    }
}
