package cn.keyss.server.mapping.anotation;

import cn.keyss.server.mapping.MappingException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 映射帮助器
 */
public class MapHelper {
    //region private fields
    /**
     * 是否激活Map映射
     */
    private boolean enableMapAnotation;

    /**
     * 构造方法
     *
     * @param enableMapAnotation 是否激活Map映射
     */
    public MapHelper(boolean enableMapAnotation) {
        this.enableMapAnotation = enableMapAnotation;
    }

    /**
     * 实体映射信息
     */
    public static class MapInfo {
        /**
         * 名称
         */
        public String Name;
        /**
         * 属性
         */
        public PropertyDescriptor Prperty;
        /**
         * 是否是数组
         */
        public boolean IsArray;
        /**
         * 数组元素类型
         */
        public Class<?> CommponentType;
    }

    /**
     * 读写锁
     */
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * 映射信息缓存
     */
    private HashMap<String, HashMap<String, MapInfo>> mapInfos = new HashMap<String, HashMap<String, MapInfo>>();

    //endregion

    //region private methods
    /**
     * 解析实体类映射信息
     *
     * @param entityClazz 实体类型
     * @return 类映射信息
     */
    private HashMap<String, MapInfo> resolveEntityMapInfos(Class<?> entityClazz) {
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(entityClazz, Object.class);
        } catch (IntrospectionException e) {
            throw new MappingException("获取Bean属性时发生异常！", e);
        }

        HashMap<String, MapInfo> entityMapInfos = new HashMap<String, MapInfo>();

        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            Map resolveMap = null;
            if (this.enableMapAnotation) {
                if (propertyDescriptor.getReadMethod() != null) {
                    resolveMap = propertyDescriptor.getReadMethod().getAnnotation(Map.class);
                }
                if (resolveMap == null && propertyDescriptor.getWriteMethod() != null) {
                    resolveMap = propertyDescriptor.getWriteMethod().getAnnotation(Map.class);
                }
                if (resolveMap == null)
                    continue;
            }

            MapInfo mapInfo = new MapInfo();
            mapInfo.Prperty = propertyDescriptor;
            if (resolveMap == null) {
                mapInfo.Name = propertyDescriptor.getName();
            } else {
                String name = resolveMap.name();
                if (name == null || "".equals(name.trim()))
                    name = propertyDescriptor.getName();
                mapInfo.Name = name;
            }

            mapInfo.IsArray = propertyDescriptor.getPropertyType().isArray();
            if (mapInfo.IsArray) {
                mapInfo.CommponentType = propertyDescriptor.getPropertyType().getComponentType();
            }
            entityMapInfos.put(mapInfo.Name, mapInfo);
        }
        return entityMapInfos;
    }

    //endregion

    //region public methods

    /**
     * 获取实体类映射信息
     *
     * @param entityClazz 实体类型
     * @return 实体映射信息
     * @throws IntrospectionException
     */
    public HashMap<String, MapInfo> getEntityMapInfos(Class<?> entityClazz) {
        if (entityClazz == null)
            throw new IllegalArgumentException("entityClazz不能为null!");

        //region private try read
        //读缓存
        try {
            readWriteLock.readLock().lock();
            if (mapInfos.containsKey(entityClazz.getName()))
                return mapInfos.get(entityClazz.getName());
        } finally {
            readWriteLock.readLock().unlock();
        }
        //endregion

        //缓存未命中-实时解析
        HashMap<String, MapInfo> resultMap = resolveEntityMapInfos(entityClazz);

        //region try write
        //更新缓存
        try {
            readWriteLock.writeLock().lock();
            if (!mapInfos.containsKey(entityClazz.getName()))
                mapInfos.put(entityClazz.getName(), resultMap);
        } finally {
            readWriteLock.writeLock().unlock();
        }
        //endregion
        return resultMap;
    }
    //endregion
}
