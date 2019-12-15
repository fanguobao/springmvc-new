package utility;

/**
 * 功能说明：业务类异常
 */
public class BusinessException extends  RuntimeException {

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message,cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

}
