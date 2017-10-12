package cn.keyss.server.mapping.datasource;


/**
 * 数据源
 */
public interface DataSource {

    /**
     * 是否包含键
     *
     * @param key 键
     * @return 是否包含
     */
    boolean contains(String key);

    /**
     * 获取键值
     *
     * @param key 键
     * @return 值
     */
    Object getValue(String key, Class<?> targetClazz);

    /**
     * 设置键值
     *
     * @param key        键
     * @param valueClazz
     * @param value      值
     */
    void setValue(String key, Class<?> valueClazz, Object value);
}
