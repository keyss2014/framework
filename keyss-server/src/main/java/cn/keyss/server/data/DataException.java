package cn.keyss.server.data;

/**
 * 数据异常
 */
public class DataException extends RuntimeException {

    private static final long serialVersionUID = 3304106779394265520L;

    public DataException() {
        super();
    }

    public DataException(String message) {
        super(message);
    }

    public DataException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataException(Throwable cause) {
        super(cause);
    }

    protected DataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
