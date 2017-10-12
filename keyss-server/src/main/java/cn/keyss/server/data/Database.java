package cn.keyss.server.data;

import cn.keyss.server.data.command.*;
import cn.keyss.server.mapping.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 数据库
 * <p>
 * 实现数据库分区，读写分离数据访问层
 * 仅支持最简单的数据库使用场景
 * 仅返回一个数据集
 * 仅支持普通Sql指令，不支持存贮过程
 * 仅支持传入参数
 * 返回的结果集直接进行实体映射，不提供底层API
 * 如果有特殊的应用场景，请直接获取底层的相关对象自行操作
 */
public class Database {
    //region init

    /**
     * 数据库实体映射器
     */
    private Mapper mapper;

    /**
     * 分区包装器
     */
    public static class ShardWrapper {
        /**
         * 分区
         */
        public String Shard;

        /**
         * 数据源
         */
        public DataSource DataSource;

        /**
         * 模板
         */
        public NamedParameterJdbcTemplate JdbcTemplate;

        /**
         * 事务管理器
         */
        public DataSourceTransactionManager TransactionManager;

        /**
         * 比率限制
         */
        public int RatioLimit;
    }

    /**
     * 数据库引擎
     */
    private DataEngine engine;

    /**
     * 主分区
     */
    private Map<String, ShardWrapper> masterShards;

    /**
     * 从分区
     */
    private Map<String, List<ShardWrapper>> slaveShards;

    /**
     * 随机种子
     */
    private static Random random = new Random(System.currentTimeMillis());

    /**
     * 获取分区
     *
     * @param shardType
     * @param shard
     * @return
     */
    private ShardWrapper getShard(ShardType shardType, String shard) {
        if (shardType == ShardType.SLAVE
                && this.slaveShards.containsKey(shard)) {
            List<ShardWrapper> shards = this.slaveShards.get(shard);
            if (shards.size() > 0) {
                int random = this.random.nextInt(shards.get(shards.size() - 1).RatioLimit);
                for (int i = 0; i < shards.size() - 1; i++) {
                    if (random < shards.get(i).RatioLimit) {
                        return shards.get(i);
                    }
                }
                return shards.get(shards.size() - 1);
            }
        }
        if (!this.masterShards.containsKey(shard))
            throw new DataException(String.format("分区'%s'的主分区不存在！", shard));
        return this.masterShards.get(shard);
    }

    /**
     * 构造方法
     *
     * @param shards 分区
     * @param engine 数据库引擎
     */
    public Database(Shard[] shards, DataEngine engine) {
        //region precheck
        if (shards == null || shards.length == 0)
            throw new IllegalArgumentException("shards不能为null或空!");

        if (engine == null)
            throw new IllegalArgumentException("engine不能为null!");
        //endregion

        mapper = new Mapper(true);

        //region init
        for (Shard shard : shards) {
            if (shard.getShard() == null)
                shard.setShard("");
            if (shard.getRatio() <= 0)
                shard.setRatio(1);
            if (shard.getQueryTimeout() < 0)
                shard.setQueryTimeout(-1);
            if (shard.getDataSource() == null)
                throw new IllegalArgumentException(String.format("分区'%s'数据源不能为null!", shard.getShard()));
        }
        //endregion

        //region assembly database
        this.engine = engine;
        this.masterShards = new HashMap<>();
        this.slaveShards = new HashMap<>();

        for (Shard shard : shards) {
            ShardWrapper shardWrapper = new ShardWrapper();
            shardWrapper.Shard = shard.getShard();
            shardWrapper.DataSource = shard.getDataSource();
            JdbcTemplate innerTemplate = new JdbcTemplate(shard.getDataSource());
            innerTemplate.setQueryTimeout(shard.getQueryTimeout());
            shardWrapper.JdbcTemplate = new NamedParameterJdbcTemplate(innerTemplate);
            shardWrapper.TransactionManager = new DataSourceTransactionManager(shard.getDataSource());
            shardWrapper.RatioLimit = shard.getRatio();

            if (shard.getShardType() == ShardType.MASTER) {
                if (this.masterShards.containsKey(shard.getShard())) {
                    throw new DataException(String.format("分区'%s'的主分区存在多个，应该只有一个！", shard.getShard()));
                } else {
                    this.masterShards.put(shard.getShard(), shardWrapper);
                }
            } else {
                List<ShardWrapper> slaves;
                if (this.slaveShards.containsKey(shard.getShard())) {
                    slaves = this.slaveShards.get(shard.getShard());
                } else {
                    slaves = new ArrayList<>();
                    this.slaveShards.put(shard.getShard(), slaves);
                }
                slaves.add(shardWrapper);
            }
        }
        //endregion

        //region set limit
        for (String key : this.slaveShards.keySet()) {
            List<ShardWrapper> shardWrappers = this.slaveShards.get(key);
            int limitRate = 0;
            for (int i = 0; i < shardWrappers.size(); i++) {
                ShardWrapper wrapper = shardWrappers.get(i);
                limitRate = limitRate + wrapper.RatioLimit;
                wrapper.RatioLimit = limitRate;
            }
        }
        //endregion
    }


    //endregion

    //region debug
    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(Database.class);

    /**
     * 日志执行的Sql语句
     * 主要用来排障
     *
     * @param sqlStr
     * @param paramss
     */
    private void logSqlText(String sqlStr, Map<String, Object>... paramss) {
        StringBuilder parmsStr = new StringBuilder();
        parmsStr.append("[SQL]:");
        parmsStr.append(sqlStr);
        parmsStr.append("[Params]:");
        if (paramss != null && paramss.length > 0) {
            for (Map<String, Object> params : paramss) {
                if (params != null && params.size() > 0) {
                    for (String paraName : params.keySet()) {
                        parmsStr.append(String.format("[%s]:%s;", paraName, params.get(paraName)));
                    }
                }
            }
        }
        this.logger.info(parmsStr.toString());
    }
    //endregion

    //region execute command

    //region parameter helpers

    /**
     * 准备指令输入参数及值
     *
     * @param parass
     * @return
     */
    private static Map<String, Object> prepareCommandParas(Map<String, Object>... parass) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (parass == null || parass.length == 0)
            return result;

        for (Map<String, Object> paras : parass) {
            if (paras != null && paras.size() > 0) {
                for (String paraName : paras.keySet()) {
                    result.put(paraName, paras.get(paraName));
                }
            }
        }
        return result;
    }
    //endregion

    /**
     * 执行查询实体集指令
     *
     * @param entityClass 实体类型
     * @param shardType   分区类型
     * @param shard       分区
     * @param statement   指令
     * @param <TEntity>   实体类型
     * @return 实体集
     */
    public <TEntity> List<TEntity> executeStatementForEntities(Class<TEntity> entityClass, ShardType shardType, String shard, DbStatement statement) {
        if (entityClass == null)
            throw new IllegalArgumentException("entityClass不能为null!");
        if (shardType == null)
            shardType = ShardType.MASTER;
        if (shard == null)
            shard = "";
        if (statement == null)
            throw new IllegalArgumentException("statement不能为null!");

        statement.validateAndFormat();

        this.logSqlText(statement.getStatementText(), statement.getParameters());
        ShardWrapper shardWrapper = this.getShard(shardType, shard);
        Map<String, Object> paras = this.prepareCommandParas(statement.getParameters());
        return shardWrapper.JdbcTemplate.query(statement.getStatementText(), paras, new RowMapper<TEntity>() {
            @Override
            public TEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                return mapper.buildEntity(entityClass, rs);
            }
        });
    }

    /**
     * 执行查询实体指令
     *
     * @param entityClass 实体类型
     * @param shardType   分区类型
     * @param shard       分区
     * @param statement   指令
     * @param <TEntity>   实体类型
     * @return 实体
     */
    public <TEntity> TEntity executeStatementForEntity(Class<TEntity> entityClass, ShardType shardType, String shard, DbStatement statement) {
        if (entityClass == null)
            throw new IllegalArgumentException("entityClass不能为null!");
        if (shardType == null)
            shardType = ShardType.MASTER;
        if (shard == null)
            shard = "";
        if (statement == null)
            throw new IllegalArgumentException("statement不能为null!");

        statement.validateAndFormat();
        this.logSqlText(statement.getStatementText(), statement.getParameters());
        ShardWrapper shardWrapper = this.getShard(shardType, shard);
        Map<String, Object> paras = this.prepareCommandParas(statement.getParameters());

        List<TEntity> resultList = shardWrapper.JdbcTemplate.query(statement.getStatementText(), paras, new RowMapper<TEntity>() {
            @Override
            public TEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                return mapper.buildEntity(entityClass, rs);
            }
        });
        if (resultList.size() > 0)
            return resultList.get(0);
        else
            return null;
    }

    /**
     * 执行指令并返回Map
     *
     * @param shardType
     * @param shard
     * @param statement
     * @return
     */
    public Map<String, Object> executeStatementForMap(ShardType shardType, String shard, DbStatement statement) {
        if (shardType == null)
            shardType = ShardType.MASTER;
        if (shard == null)
            shard = "";
        if (statement == null)
            throw new IllegalArgumentException("statement不能为null!");

        statement.validateAndFormat();
        this.logSqlText(statement.getStatementText(), statement.getParameters());
        ShardWrapper shardWrapper = this.getShard(shardType, shard);
        Map<String, Object> paras = this.prepareCommandParas(statement.getParameters());
        return shardWrapper.JdbcTemplate.queryForMap(statement.getStatementText(), paras);
    }

    /**
     * 执行指令
     *
     * @param shardType 分区类型
     * @param shard     分区
     * @param statement 指令
     * @return 影响行数
     */
    public int executeNoQueryStatement(ShardType shardType, String shard, DbStatement statement) {
        if (shardType == null)
            shardType = ShardType.MASTER;
        if (shard == null)
            shard = "";
        if (statement == null)
            throw new IllegalArgumentException("statement不能为null!");

        statement.validateAndFormat();
        this.logSqlText(statement.getStatementText(), statement.getParameters());
        ShardWrapper shardWrapper = this.getShard(shardType, shard);
        Map<String, Object> paras = this.prepareCommandParas(statement.getParameters());
        return shardWrapper.JdbcTemplate.update(statement.getStatementText(), paras);
    }

    /**
     * 执行指令
     *
     * @param shardType 分区类型
     * @param shard     分区
     * @param statement 指令
     * @param keyHolder 生成的Key
     * @return 影响行数
     */
    public int executeNoQueryStatement(ShardType shardType, String shard, DbStatement statement, KeyHolder keyHolder) {
        if (shardType == null)
            shardType = ShardType.MASTER;
        if (shard == null)
            shard = "";
        if (statement == null)
            throw new IllegalArgumentException("statement不能为null!");

        statement.validateAndFormat();
        this.logSqlText(statement.getStatementText(), statement.getParameters());
        ShardWrapper shardWrapper = this.getShard(shardType, shard);
        Map<String, Object> paras = this.prepareCommandParas(statement.getParameters());
        return shardWrapper.JdbcTemplate.update(statement.getStatementText(), new MapSqlParameterSource(paras), keyHolder);
    }

    /**
     * 执行查询
     *
     * @param entityClass 实体类型
     * @param shardType   分区类型
     * @param shard       分区
     * @param query       查询
     * @param <TEntity>   实体类型
     * @return 查询结果
     */
    public <TEntity> DbQueryResult<TEntity> excuteQuery(Class<TEntity> entityClass, ShardType shardType, String shard, DbQuery query) {
        if (entityClass == null)
            throw new IllegalArgumentException("entityClass不能为null!");
        if (shardType == null)
            shardType = ShardType.MASTER;
        if (shard == null)
            shard = "";
        if (query == null)
            throw new IllegalArgumentException("statement不能为null!");

        query.validateAndFormat();

        ShardWrapper shardWrapper = this.getShard(shardType, shard);
        DbQueryResult<TEntity> result = new DbQueryResult();

        DataEngine.ParseResult parseSelectResult = this.engine.parseDbQuerySelectSql(query);
        this.logSqlText(parseSelectResult.getSqlText(), query.getParameters(), parseSelectResult.getDynamicParameters());
        Map<String, Object> selectParas = prepareCommandParas(query.getParameters(), parseSelectResult.getDynamicParameters());

        List<TEntity> entities = shardWrapper.JdbcTemplate.query(parseSelectResult.getSqlText(), selectParas, new RowMapper<TEntity>() {
            @Override
            public TEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                return mapper.buildEntity(entityClass, rs);
            }
        });

        result.setRecords(entities);
        if (query.getStartIndex() != 0 || query.getMaxRecordReturn() !=0) {
            DataEngine.ParseResult parseCountResult = this.engine.parseDbQuerySelectCountSql(query);
            this.logSqlText(parseCountResult.getSqlText(), query.getParameters(), parseCountResult.getDynamicParameters());
            Map<String, Object> selectCountParas = prepareCommandParas(query.getParameters(), parseCountResult.getDynamicParameters());
            Long count = shardWrapper.JdbcTemplate.queryForObject(parseCountResult.getSqlText(), selectCountParas, new RowMapper<Long>() {
                @Override
                public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getLong(1);
                }
            });
            result.setTotal(count);
        } else {
            result.setTotal(result.getRecords().size());
        }

        return result;
    }
    //endregion

    //region get shards info

    //region getdatasource

    /**
     * 根据分区类型及分区名称返回DataSource
     *
     * @param shardType 分区类型
     * @param shard     分区名
     * @return 数据源
     */
    public DataSource getDataSource(ShardType shardType, String shard) {
        if (shardType == null)
            shardType = ShardType.MASTER;
        if (shard == null)
            shard = "";
        ShardWrapper wrapper = this.getShard(shardType, shard);
        return wrapper.DataSource;
    }

    public DataSource getDataSource(ShardType shardType) {
        return getDataSource(shardType, "");
    }

    public DataSource getDataSource() {
        return getDataSource(ShardType.MASTER, "");
    }
    //endregion

    /**
     * 根据分区类型及分区名称返回Jdbc模板
     *
     * @param shardType 分区类型
     * @param shard     分区名
     * @return Jdbc模板
     */
    public NamedParameterJdbcTemplate getJdbcTemplate(ShardType shardType, String shard) {
        if (shardType == null)
            shardType = ShardType.MASTER;
        if (shard == null)
            shard = "";
        ShardWrapper wrapper = this.getShard(shardType, shard);
        return wrapper.JdbcTemplate;
    }

    public NamedParameterJdbcTemplate getJdbcTemplate(ShardType shardType) {
        return getJdbcTemplate(shardType, "");
    }

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return getJdbcTemplate(ShardType.MASTER, "");
    }
    //region transaction manager

    /**
     * 根据分区类型及分区名称返回事务管理器
     *
     * @param shardType 分区类型
     * @param shard     分区名
     * @return 事务管理器
     */
    public DataSourceTransactionManager getTransactionManager(ShardType shardType, String shard) {
        if (shardType == null)
            shardType = ShardType.MASTER;
        if (shard == null)
            shard = "";
        ShardWrapper wrapper = this.getShard(shardType, shard);
        return wrapper.TransactionManager;
    }

    public DataSourceTransactionManager getTransactionManager(ShardType shardType) {
        return getTransactionManager(shardType, "");
    }

    public DataSourceTransactionManager getTransactionManager() {
        return getTransactionManager(ShardType.MASTER, "");
    }
    //endregion

    public Mapper getMapper() {
        return this.mapper;
    }
    //endregion
}
