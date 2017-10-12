package cn.keyss.common.metrics;

/**
 * 埋点异常
 */
public class MetricException extends Exception {

    public MetricException() {
        super();
    }

    public MetricException(String message) {
        super(message);
    }

    public MetricException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetricException(Throwable cause) {
        super(cause);
    }

    protected MetricException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
