package cn.keyss.server.mapping.datasource;

import cn.keyss.server.mapping.MappingException;

import java.util.Map;

/**
 * 映射数据源
 */
public class MapDataSource implements DataSource {
    private Map<String, Object> map;

    /**
     * 构造方法
     *
     * @param map 映射
     */
    public MapDataSource(Map<String, Object> map) {
        this.map = map;
    }

    /**
     * 是否包含键
     *
     * @param key 键
     * @return 是否包含
     */
    @Override
    public boolean contains(String key) {
        if (key == null || key == "")
            throw new IllegalArgumentException("key不能为Null或空字符串！");

        return map == null ? false : true;
    }

    /**
     * 获取键值
     *
     * @param key 键
     * @return 值
     * @throws MappingException 映射异常
     */
    @Override
    public Object getValue(String key, Class<?> targetClazz) {
        if (key == null || key == "")
            throw new IllegalArgumentException("key不能为Null或空字符串！");

        if (targetClazz == null)
            throw new IllegalArgumentException("targetClazz不能为Null！");
        if (map == null)
            return null;
        Object value = map.get(key);
        if (value == null)
            return null;
        if (!targetClazz.isAssignableFrom(value.getClass()))
            throw new MappingException("数据源中的值类型和目标类型不匹配！");
        return value;
    }

    /**
     * 设置键值
     *
     * @param key        键
     * @param valueClazz
     * @param value      值
     * @throws MappingException 映射异常
     */
    @Override
    public void setValue(String key, Class<?> valueClazz, Object value) {
        if (key == null || key == "")
            throw new IllegalArgumentException("key不能为Null或空字符串！");

        if (valueClazz == null)
            throw new IllegalArgumentException("valueClazz不能为Null！");

        if (map == null)
            throw new MappingException("映射对象为空，无法更新键值！");

        map.put(key, value);
    }
}
