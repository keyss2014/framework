package cn.keyss.server.entity;

/**
 * 实体异常
 */
public class EntityException extends RuntimeException {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 3658137712546718256L;

    /**
     * 默认构造
     */
    public EntityException() {
        super();
    }

    /**
     * 构造方法
     *
     * @param message 异常描述
     */
    public EntityException(String message) {
        super(message);
    }

    /**
     * 构造方法
     *
     * @param message 异常描述
     * @param cause   原始异常
     */
    public EntityException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造方法
     *
     * @param cause 原始异常
     */
    public EntityException(Throwable cause) {
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
    protected EntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
