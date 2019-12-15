package utility;

/**
 * 功能说明：业务类异常
 */
public class ControllerException extends  RuntimeException {
    public ControllerException() {
    }

    public ControllerException(String message) {
        super(message);
    }

    public ControllerException(String message, Throwable cause) {
        super(message,cause);
    }

    public ControllerException(Throwable cause) {
        super(cause);
    }
}
