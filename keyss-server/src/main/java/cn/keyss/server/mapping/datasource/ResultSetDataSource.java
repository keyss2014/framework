package cn.keyss.server.mapping.datasource;

import cn.keyss.server.mapping.MappingException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ResultSet数据源
 */
public class ResultSetDataSource implements DataSource {

    private ResultSet resultSet;

    /**
     * 构造方法
     *
     * @param resultSet 数据集
     */
    public ResultSetDataSource(ResultSet resultSet) {
        if (resultSet == null)
            throw new IllegalArgumentException("resultSet不能为Null！");
        this.resultSet = resultSet;
    }

    /**
     * 是否包含键
     *
     * @param key 键
     * @return 是否包含
     * @throws MappingException 映射异常
     */
    @Override
    public boolean contains(String key) {
        if (key == null || key == "")
            throw new IllegalArgumentException("key不能为Null或空字符串！");
        try {
            return this.resultSet.findColumn(key) >= 0;
        } catch (SQLException throwable) {
            return false;
        }
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

        try {
            //region lang
            if (targetClazz == java.lang.String.class) {
                return resultSet.getString(key);
            }

            if (targetClazz == int.class) {
                return resultSet.getInt(key);
            }

            if (targetClazz == java.lang.Integer.class) {
                Object innerResult = resultSet.getInt(key);
                if (resultSet.wasNull())
                    return null;
                else
                    return innerResult;
            }

            if (targetClazz == long.class) {
                return resultSet.getLong(key);
            }

            if (targetClazz == java.lang.Long.class) {
                Object innerResult = resultSet.getLong(key);
                if (resultSet.wasNull())
                    return null;
                else
                    return innerResult;
            }

            if (targetClazz == boolean.class) {
                return resultSet.getBoolean(key);
            }

            if (targetClazz == java.lang.Boolean.class) {
                Object innerResult = resultSet.getBoolean(key);
                if (resultSet.wasNull())
                    return null;
                else
                    return innerResult;
            }

            if (targetClazz == float.class) {
                return resultSet.getFloat(key);
            }

            if (targetClazz == java.lang.Float.class) {
                Object innerResult = resultSet.getFloat(key);
                if (resultSet.wasNull())
                    return null;
                else
                    return innerResult;
            }

            if (targetClazz == double.class) {
                return resultSet.getDouble(key);
            }

            if (targetClazz == java.lang.Double.class) {
                Object innerResult = resultSet.getDouble(key);
                if (resultSet.wasNull())
                    return null;
                else
                    return innerResult;
            }

            if (targetClazz == byte.class) {
                return resultSet.getByte(key);
            }

            if (targetClazz == java.lang.Byte.class) {
                Object innerResult = resultSet.getByte(key);
                if (resultSet.wasNull())
                    return null;
                else
                    return innerResult;
            }

            if (targetClazz == short.class) {
                return resultSet.getShort(key);
            }

            if (targetClazz == java.lang.Short.class) {
                Object innerResult = resultSet.getShort(key);
                if (resultSet.wasNull())
                    return null;
                else
                    return innerResult;
            }

            if (targetClazz == Byte[].class) {
                return convertBytes(resultSet.getBytes(key));
            }

            if (targetClazz == byte[].class) {
                return resultSet.getBytes(key);
            }
            //endregion

            //region sql
            if (targetClazz == java.sql.Timestamp.class) {
                return resultSet.getTimestamp(key);
            }

            if (targetClazz == java.sql.Time.class)
                return resultSet.getTime(key);

            if (targetClazz == java.sql.Date.class)
                return resultSet.getDate(key);
            //endregion

            //region math
            if (targetClazz == java.math.BigDecimal.class)
                return resultSet.getBigDecimal(key);

            if (targetClazz == java.math.BigInteger.class)
                return resultSet.getLong(key);
            //endregion

            Object result = resultSet.getObject(key, targetClazz);
            if (resultSet.wasNull())
                return null;
            else
                return result;
        } catch (Throwable throwable) {
            throw new MappingException("获取数据源中键值时发生异常！", throwable);
        }
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
        throw new MappingException("不能更新ResultSet类型的数据源！");
    }

    public static Byte[] convertBytes(byte[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return new Byte[0];
        } else {
            Byte[] result = new Byte[array.length];

            for (int i = 0; i < array.length; ++i) {
                result[i] = Byte.valueOf(array[i]);
            }

            return result;
        }
    }
}
