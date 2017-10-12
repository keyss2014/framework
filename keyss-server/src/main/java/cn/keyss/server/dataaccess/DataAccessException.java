package cn.keyss.server.dataaccess;

/**
 * 数据访问层异常
 */
public class DataAccessException extends RuntimeException {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -6641356103762269432L;

    /**
     * 默认构造
     */
    public DataAccessException() {
        super();
    }

    /**
     * 构造方法
     *
     * @param message 异常描述
     */
    public DataAccessException(String message) {
        super(message);
    }

    /**
     * 构造方法
     *
     * @param message 异常描述
     * @param cause   原始异常
     */
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造方法
     *
     * @param cause 原始异常
     */
    public DataAccessException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造方法
     *
     * @param message            异常描述
     * @param cause              原始异常
     * @param enableSuppression  是否屏蔽
     * @param writableStackTrace 是否写堆栈信息
     */
    protected DataAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
