package cn.keyss.server.mapping.datasource;


import cn.keyss.server.mapping.MappingException;
import cn.keyss.server.mapping.anotation.MapHelper;

import java.util.HashMap;

/**
 * 对象数据源
 */
public class ObjectDataSource implements DataSource {

    private static MapHelper mapHelper = new MapHelper(false);

    /**
     * 数据源对象
     */
    private Object object;

    /**
     * 数据源对象类型
     */
    private Class<?> objectClazz;

    /**
     * 构造方法
     *
     * @param object
     */
    public ObjectDataSource(Object object) {
        if (object == null)
            throw new IllegalArgumentException("object不能为Null！");
        this.object = object;
        this.objectClazz = object.getClass();
    }

    @Override
    public boolean contains(String key) {
        if (key == null || key.length()==0)
            throw new IllegalArgumentException("key不能为Null或空字符串！");
        HashMap<String, MapHelper.MapInfo> objectInfos = mapHelper.getEntityMapInfos(objectClazz);
        return objectInfos.containsKey(key);
    }

    @Override
    public Object getValue(String key, Class<?> targetClazz) {
        if (key == null || key.length() == 0)
            throw new IllegalArgumentException("key不能为Null或空字符串！");

        if (targetClazz == null)
            throw new IllegalArgumentException("targetClazz不能为Null！");

        try {
            HashMap<String, MapHelper.MapInfo> objectInfos = mapHelper.getEntityMapInfos(objectClazz);
            MapHelper.MapInfo propertyInfo = objectInfos.get(key);
            if (propertyInfo == null || propertyInfo.Prperty.getReadMethod() == null) {
                throw new MappingException("属性不可读！");
            }
            return propertyInfo.Prperty.getReadMethod().invoke(object);
        } catch (Throwable throwable) {
            throw new MappingException("判断数据源中是否包含指定键时发生异常！", throwable);
        }
    }

    @Override
    public void setValue(String key, Class<?> valueClazz, Object value) {
        if (key == null || key.length() == 0)
            throw new IllegalArgumentException("key不能为Null或空字符串！");

        if (valueClazz == null)
            throw new IllegalArgumentException("targetClazz不能为Null！");

        try {
            HashMap<String, MapHelper.MapInfo> objectInfos = mapHelper.getEntityMapInfos(objectClazz);
            MapHelper.MapInfo propertyInfo = objectInfos.get(key);
            if (propertyInfo == null || propertyInfo.Prperty.getWriteMethod() == null) {
                throw new MappingException("属性不可写！");
            }

            if (!propertyInfo.Prperty.getPropertyType().isAssignableFrom(valueClazz)) {
                throw new MappingException("属性和所提供的值类型不匹配！");
            }
            propertyInfo.Prperty.getWriteMethod().invoke(object, value);
        } catch (Throwable throwable) {
            throw new MappingException("判断数据源中是否包含指定键时发生异常！", throwable);
        }
    }
}
