package utility;


public class DaoAcessException extends RuntimeException {
    public DaoAcessException() {
    }

    public DaoAcessException(String message) {
        super(message);
    }

    public DaoAcessException(String message, Throwable cause) {
        super(message,cause);
    }

    public DaoAcessException(Throwable cause) {
        super(cause);
    }
}
