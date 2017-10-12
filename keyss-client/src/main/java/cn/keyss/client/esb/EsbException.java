package cn.keyss.client.esb;

/**
 * Esb异常
 */
public class EsbException extends RuntimeException {

    private static final long serialVersionUID = 7940039672855751820L;

    public EsbException() {
        super();
    }

    public EsbException(String message) {
        super(message);
    }

    public EsbException(String message, Throwable cause) {
        super(message, cause);
    }

    public EsbException(Throwable cause) {
        super(cause);
    }

    protected EsbException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
