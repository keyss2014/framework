package cn.keyss.server.mapping;

/**
 * 映射异常
 */
public class MappingException extends RuntimeException  {

    /**
     * 序列化UID
     */
    private static final long serialVersionUID = -8199794000286499527L;

    /**
     * 默认构造
     */
    public MappingException() {
        super();
    }

    /**
     * 构造方法
     * @param message 异常描述
     */
    public MappingException(String message) {
        super(message);
    }

    /**
     * 构造方法
     * @param message 异常描述
     * @param cause 原始异常
     */
    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造方法
     * @param cause 原始异常
     */
    public MappingException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造方法
     * @param message 异常描述
     * @param cause 原始异常
     * @param enableSuppression  是否屏蔽
     * @param writableStackTrace 是否写堆栈信息
     */
    protected MappingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
