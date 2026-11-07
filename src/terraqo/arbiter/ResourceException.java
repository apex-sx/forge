package terraqo.arbiter;

import java.io.IOException;

/**
 * @author Terraqo
 */
public class ResourceException extends IOException {
    public ResourceException() {
    }

    public ResourceException(String message) {
        super(message);
    }

    public ResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceException(Throwable cause) {
        super(cause);
    }
}
