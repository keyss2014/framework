package cn.keyss.server.mapping;

import cn.keyss.server.mapping.anotation.MapHelper;
import cn.keyss.server.mapping.datasource.DataSource;
import cn.keyss.server.mapping.datasource.MapDataSource;
import cn.keyss.server.mapping.datasource.ObjectDataSource;
import cn.keyss.server.mapping.datasource.ResultSetDataSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.*;

/**
 * 对像映射器
 */
public class Mapper {
    /**
     * 缺省的映射器
     */
    public static final Mapper DefaultMapper = new Mapper(false);

    /**
     * 基于Map注解的映射器
     */
    public static final Mapper MapMapper = new Mapper(true);

    //region private methods

    /**
     * 映身帮助器
     */
    private MapHelper mapHelper;

    /**
     * 是否激活Map注解
     *
     * @param enableMap
     */
    public Mapper(boolean enableMap) {
        this.mapHelper = new MapHelper(enableMap);
    }

    /**
     * 根据数据构建数据源
     *
     * @param source 数据
     * @return 构建后的数据源
     */
    @SuppressWarnings("unchecked")
    private DataSource buildDataSource(Object source) {
        if (source == null)
            throw new IllegalArgumentException("source不能为Null！");

        if (source instanceof DataSource)
            return (DataSource) source;
        else if (source instanceof ResultSet)
            return new ResultSetDataSource((ResultSet) source);
        else if (source instanceof Map)
            return new MapDataSource((Map<String, Object>) source);
        else
            return new ObjectDataSource(source);
    }

    /**
     * @param objectClazz
     * @param <TObject>
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private <TObject> TObject buildObjectWithDefaultConstruct(Class<TObject> objectClazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (objectClazz == null)
            throw new IllegalArgumentException("objectClazz不能为Null！");
        Constructor<TObject> constructor = objectClazz.getConstructor();
        return constructor.newInstance();
    }

    /**
     * 更新实体
     *
     * @param entityClass 实体类型
     * @param dataSource  数据源
     * @param entity      实体
     * @param <TEntity>   实体类型
     */
    private <TEntity> void updateEntity(Class<TEntity> entityClass, DataSource dataSource, TEntity entity) {
        if (entityClass == null)
            throw new IllegalArgumentException("entityClass不能为Null！");
        if (dataSource == null)
            throw new IllegalArgumentException("dataSource不能为Null！");
        if (entity == null)
            throw new IllegalArgumentException("entity不能为Null！");
        try {
            HashMap<String, MapHelper.MapInfo> mapinfos = this.mapHelper.getEntityMapInfos(entityClass);
            for (MapHelper.MapInfo mapInfo : mapinfos.values()) {
//                if (mapInfo.IsArray) {
//                    //tobefinished
//
//                } else {
                Method writeMethod = mapInfo.Prperty.getWriteMethod();
                if (writeMethod != null && dataSource.contains(mapInfo.Name)) {
                    writeMethod.invoke(entity, dataSource.getValue(mapInfo.Name, mapInfo.Prperty.getPropertyType()));
                }
//                }
            }
        } catch (Throwable throwable) {
            throw new MappingException("更新实体异常！", throwable);
        }
    }

    /**
     * 更新数据
     *
     * @param dataSource 数据源
     * @param entity     实体
     * @throws MappingException
     */
    private void updateDataSource(DataSource dataSource, Object entity) {
        if (dataSource == null)
            throw new IllegalArgumentException("dataSource不能为Null！");
        if (entity == null)
            throw new IllegalArgumentException("entity不能为Null！");
        try {
            HashMap<String, MapHelper.MapInfo> mapinfos = this.mapHelper.getEntityMapInfos(entity.getClass());
            for (MapHelper.MapInfo mapInfo : mapinfos.values()) {
//                if (mapInfo.IsArray) {
//
//                } else {
                Method readMethod = mapInfo.Prperty.getReadMethod();
                if (readMethod != null && dataSource.contains(mapInfo.Name)) {
                    dataSource.setValue(mapInfo.Name, mapInfo.Prperty.getPropertyType(), readMethod.invoke(entity));
                }
//                }
            }
        } catch (Throwable throwable) {
            throw new MappingException("更新数据源异常！", throwable);
        }
    }
    //endregion

    /**
     * 构造实体
     *
     * @param entityClazz 实体类型
     * @param source      源数据
     * @param <TEntity>   实体类型
     * @return 构建的实体对象
     * @throws MappingException 映射异常
     */
    public <TEntity> TEntity buildEntity(Class<TEntity> entityClazz, Object source) {
        if (entityClazz == null)
            throw new IllegalArgumentException("entityClazz不能为Null！");

        if (source == null)
            throw new IllegalArgumentException("source不能为Null！");

        try {
            DataSource dataSource = buildDataSource(source);
            TEntity entity = buildObjectWithDefaultConstruct(entityClazz);
            updateEntity(entityClazz, dataSource, entity);
            return entity;
        } catch (Throwable throwable) {
            throw new MappingException("构建实体异常！", throwable);
        }
    }

    /**
     * 构造实体数组
     *
     * @param entityClazz 实体类型
     * @param sources     源数据集
     * @param <TEntity>   实体类型
     * @return 构建的实体对象数组
     * @throws MappingException 映射异常
     */
    @SuppressWarnings("unchecked")
    public <TEntity> List<TEntity> buildEntities(Class<TEntity> entityClazz, Iterator sources) {
        if (entityClazz == null)
            throw new IllegalArgumentException("entityClazz不能为Null！");
        if (sources == null)
            throw new IllegalArgumentException("sources不能为Null！");
        ArrayList<TEntity> results = new ArrayList<TEntity>();
        while (sources.hasNext()) {
            results.add(buildEntity(entityClazz, sources.next()));
        }
        return results;
    }

    /**
     * 更新实体
     *
     * @param entityClazz 实体类型
     * @param entity      实体
     * @param source      数据源
     * @param <TEntity>   实体类型
     */
    @SuppressWarnings("unchecked")
    public <TEntity> void updateEntity(Class<TEntity> entityClazz, TEntity entity, Object source) {
        if (entityClazz == null)
            throw new IllegalArgumentException("entityClazz不能为Null！");

        if (source == null)
            throw new IllegalArgumentException("source不能为Null！");

        try {
            DataSource dataSource = buildDataSource(source);
            updateEntity(entityClazz, dataSource, entity);
        } catch (Throwable throwable) {
            throw new MappingException("构建实体时发生异常！", throwable);
        }
    }

    /**
     * 更新数据集
     *
     * @param dataClazz
     * @param entity
     * @param data
     * @param <TData>
     */
    public <TData> void updateData(Class<TData> dataClazz, Object entity, TData data) {
        if (dataClazz == null)
            throw new IllegalArgumentException("dataClazz不能为Null！");

        if (entity == null)
            throw new IllegalArgumentException("source不能为Null！");

        if (data == null)
            throw new IllegalArgumentException("data不能为Null！");

        try {
            DataSource dataSource = buildDataSource(data);
            updateDataSource(dataSource, entity);
        } catch (Throwable throwable) {
            throw new MappingException("构建实体时发生异常！", throwable);
        }
    }

    /**
     * 构建数据对象
     *
     * @param dataClazz
     * @param entity
     * @param <TData>
     * @return
     */
    public <TData> TData buildData(Class<TData> dataClazz, Object entity) {
        if (dataClazz == null)
            throw new IllegalArgumentException("dataClazz不能为Null！");

        if (entity == null)
            throw new IllegalArgumentException("entity不能为Null！");

        try {
            TData data = buildObjectWithDefaultConstruct(dataClazz);
            DataSource dataSource = buildDataSource(data);
            updateDataSource(dataSource, entity);
            return data;
        } catch (Throwable throwable) {
            throw new MappingException("构建实体时发生异常！", throwable);
        }
    }

    /**
     * 构建数据对象数组
     *
     * @param dataClazz
     * @param entities
     * @param <TData>
     * @return
     * @throws MappingException
     */
    @SuppressWarnings("unchecked")
    public <TData> List<TData> buildDatas(Class<TData> dataClazz, Iterator entities) {
        if (dataClazz == null)
            throw new IllegalArgumentException("entityClazz不能为Null！");
        if (entities == null)
            throw new IllegalArgumentException("entities不能为Null！");
        ArrayList<TData> results = new ArrayList<TData>();
        while (entities.hasNext()) {
            results.add(buildData(dataClazz, entities.next()));
        }
        return results;
    }

    /**
     * 从映射构建实体
     *
     * @param entityClazz
     * @param data
     * @param <TEntity>
     * @return
     * @throws MappingException
     */
    public <TEntity> TEntity buildEntityFromMap(Class<TEntity> entityClazz, Map<String, Object> data) {
        if (entityClazz == null)
            throw new IllegalArgumentException("entityClazz不能为Null！");

        if (data == null)
            throw new IllegalArgumentException("data不能为Null！");

        try {
            DataSource dataSource = buildDataSource(data);
            TEntity entity = buildObjectWithDefaultConstruct(entityClazz);
            updateEntity(entityClazz, dataSource, entity);
            return entity;
        } catch (Throwable throwable) {
            throw new MappingException("构建实体时发生异常！", throwable);
        }
    }

    /**
     * 从实体构建映射
     *
     * @param entity
     * @return
     * @throws MappingException
     */
    public Map<String, Object> buildMapFromEntity(Object entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity不能为Null！");

        try {
            Map<String, Object> results = new HashMap<String, Object>();
            DataSource dataSource = buildDataSource(results);
            updateDataSource(dataSource, entity);
            return results;
        } catch (Throwable throwable) {
            throw new MappingException("构建实体时发生异常！", throwable);
        }
    }
}
