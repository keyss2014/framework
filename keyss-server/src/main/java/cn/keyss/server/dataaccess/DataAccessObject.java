package cn.keyss.server.dataaccess;

import cn.keyss.server.data.Database;
import cn.keyss.server.data.command.*;
import cn.keyss.server.mapping.Mapper;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Map;

/**
 * 数据访问对象
 */
public abstract class DataAccessObject {

    //region private fields
    private static final String TableShardToken = "{shard}";

    private Database database;
    private DbCommandLoader commandLoader;

    /**
     * 获取实体映射器
     *
     * @return
     */
    protected Mapper getEntityMapper() {
        return this.database.getMapper();
    }

    /**
     * 数据访问层
     *
     * @param database 数据库
     */
    public DataAccessObject(Database database) {
        this.database = database;
        this.commandLoader = new DbCommandLoader(this.getClass());
    }
    //endregion

    //region executeStatementForEntities

    /**
     * 执行查询指令
     *
     * @param entityClazz   实体类型
     * @param statementName 指令
     * @param params        参数
     * @param databaseShard 数据库分区
     * @param tableShard    数据表分区
     * @param <TEntity>     实体类型
     * @return 实体集
     */
    protected <TEntity> List<TEntity> executeStatementForEntities(Class<TEntity> entityClazz, String statementName, Map<String, Object> params, String databaseShard, String tableShard) {
        DbCommandWrapper command = this.commandLoader.getCommand(statementName);

        if (command == null || command.getCommand() == null || !"statement".equals(command.getCommandType()))
            throw new DataAccessException(String.format("类型'%s'的指令'%s'获取无效！", this.getClass(), statementName));

        DbStatement statement = (DbStatement) command.getCommand();

        //region process tableShard
        if (tableShard != null
                && !tableShard.trim().equals("")
                && statement.getStatementText() != null
                && !statement.getStatementText().trim().equals("")
                && statement.getStatementText().contains(TableShardToken)
                ) {
            statement.setStatementText(statement.getStatementText().replace(TableShardToken, tableShard));
        }
        //endregion

        //region process paras
        if (params != null
                && params.size() > 0
                && statement.getParameters() != null
                && statement.getParameters().size() > 0) {
            for (String paraName : statement.getParameters().keySet()) {
                if (params.containsKey(paraName)) {
                    statement.getParameters().put(paraName, params.get(paraName));
                }
            }
        }
        //endregion

        return this.database.executeStatementForEntities(entityClazz, command.getShardType(), databaseShard, statement);
    }

    protected <TEntity> List<TEntity> executeStatementForEntities(Class<TEntity> entityClazz, String statementName, Map<String, Object> params, String databaseShard) {
        return executeStatementForEntities(entityClazz, statementName, params, databaseShard, "");
    }

    protected <TEntity> List<TEntity> executeStatementForEntities(Class<TEntity> entityClazz, String statementName, Map<String, Object> params) {
        return executeStatementForEntities(entityClazz, statementName, params, "", "");
    }
    //endregion

    //region executeStatementForEntity

    /**
     * 执行获取指令
     *
     * @param entityClazz   实体类型
     * @param statementName 指令
     * @param params        参数
     * @param databaseShard 数据库分区
     * @param tableShard    数据表分区
     * @param <TEntity>     实体类型
     * @return
     */
    protected <TEntity> TEntity executeStatementForEntity(Class<TEntity> entityClazz, String statementName, Map<String, Object> params, String databaseShard, String tableShard) {
        DbCommandWrapper command = this.commandLoader.getCommand(statementName);

        if (command == null || command.getCommand() == null || !"statement".equals(command.getCommandType()))
            throw new DataAccessException(String.format("类型'%s'的指令'%s'获取无效！", this.getClass(), statementName));

        DbStatement statement = (DbStatement) command.getCommand();

        //region process tableShard
        if (tableShard != null
                && !tableShard.trim().equals("")
                && statement.getStatementText() != null
                && !statement.getStatementText().trim().equals("")
                && statement.getStatementText().contains(TableShardToken)
                ) {
            statement.setStatementText(statement.getStatementText().replace(TableShardToken, tableShard));
        }
        //endregion

        //region process paras
        if (params != null
                && params.size() > 0
                && statement.getParameters() != null
                && statement.getParameters().size() > 0) {
            //statement.getStatement().getParameters().setParaValues(params);
            for (String paraName : statement.getParameters().keySet()) {
                if (params.containsKey(paraName)) {
                    statement.getParameters().put(paraName, params.get(paraName));
                }
            }
        }
        //endregion

        return this.database.executeStatementForEntity(entityClazz, command.getShardType(), databaseShard, statement);
    }

    protected <TEntity> TEntity executeStatementForEntity(Class<TEntity> entityClazz, String statementName, Map<String, Object> params, String databaseShard) {
        return executeStatementForEntity(entityClazz, statementName, params, databaseShard, "");
    }

    protected <TEntity> TEntity executeStatementForEntity(Class<TEntity> entityClazz, String statementName, Map<String, Object> params) {
        return executeStatementForEntity(entityClazz, statementName, params, "", "");
    }
    //endregion

    //region executeStatementForMap

    /**
     * 执行获取Map指令
     *
     * @param statementName 指令
     * @param params        参数
     * @param databaseShard 数据库分区
     * @param tableShard    数据表分区
     * @return
     */
    protected Map<String, Object> executeStatementForMap(String statementName, Map<String, Object> params, String databaseShard, String tableShard) {
        DbCommandWrapper command = this.commandLoader.getCommand(statementName);

        if (command == null || command.getCommand() == null || !"statement".equals(command.getCommandType()))
            throw new DataAccessException(String.format("类型'%s'的指令'%s'获取无效！", this.getClass(), statementName));

        DbStatement statement = (DbStatement) command.getCommand();

        //region process tableShard
        if (tableShard != null
                && !tableShard.trim().equals("")
                && statement.getStatementText() != null
                && !statement.getStatementText().trim().equals("")
                && statement.getStatementText().contains(TableShardToken)
                ) {
            statement.setStatementText(statement.getStatementText().replace(TableShardToken, tableShard));
        }
        //endregion

        //region process paras
        if (params != null
                && params.size() > 0
                && statement.getParameters() != null
                && statement.getParameters().size() > 0) {
            //statement.getStatement().getParameters().setParaValues(params);
            for (String paraName : statement.getParameters().keySet()) {
                if (params.containsKey(paraName)) {
                    statement.getParameters().put(paraName, params.get(paraName));
                }
            }
        }
        //endregion

        return this.database.executeStatementForMap(command.getShardType(), databaseShard, statement);
    }

    protected Map<String, Object> executeStatementForMap(String statementName, Map<String, Object> params, String databaseShard) {
        return executeStatementForMap(statementName, params, databaseShard, "");
    }

    protected Map<String, Object> executeStatementForMap(String statementName, Map<String, Object> params) {
        return executeStatementForMap(statementName, params, "", "");
    }
    //endregion

    //region executeNoQueryStatement

    /**
     * 执行非查询指令
     *
     * @param statementName 指令
     * @param params        参数
     * @param databaseShard 数据库分区
     * @param tableShard    数据表分区
     * @return 影响行数
     */
    protected int executeNoQueryStatement(String statementName, Map<String, Object> params, KeyHolder keyHolder, String databaseShard, String tableShard) {
        DbCommandWrapper command = this.commandLoader.getCommand(statementName);

        if (command == null || command.getCommand() == null || !"statement".equals(command.getCommandType()))
            throw new DataAccessException(String.format("类型'%s'的指令'%s'获取无效！", this.getClass(), statementName));

        DbStatement statement = (DbStatement) command.getCommand();

        //region process tableShard
        if (tableShard != null
                && !tableShard.trim().equals("")
                && statement.getStatementText() != null
                && !statement.getStatementText().trim().equals("")
                && statement.getStatementText().contains(TableShardToken)
                ) {
            statement.setStatementText(statement.getStatementText().replace(TableShardToken, tableShard));
        }
        //endregion

        //region process paras
        if (params != null
                && params.size() > 0
                && statement.getParameters() != null
                && statement.getParameters().size() > 0) {
            //statement.getStatement().getParameters().setParaValues(params);
            for (String paraName : statement.getParameters().keySet()) {
                if (params.containsKey(paraName)) {
                    statement.getParameters().put(paraName, params.get(paraName));
                }
            }
        }
        //endregion

        return this.database.executeNoQueryStatement(command.getShardType(), databaseShard, statement, keyHolder);
    }

    protected int executeNoQueryStatement(String statementName, Map<String, Object> params, KeyHolder keyHolder, String databaseShard) {
        return executeNoQueryStatement(statementName, params, keyHolder, databaseShard, "");
    }

    protected int executeNoQueryStatement(String statementName, Map<String, Object> params, KeyHolder keyHolder) {
        return executeNoQueryStatement(statementName, params, keyHolder, "", "");
    }
    //endregion


    //region executeNoQueryStatement

    /**
     * 执行非查询指令
     *
     * @param statementName 指令
     * @param params        参数
     * @param databaseShard 数据库分区
     * @param tableShard    数据表分区
     * @return 影响行数
     */
    protected int executeNoQueryStatement(String statementName, Map<String, Object> params, String databaseShard, String tableShard) {
        DbCommandWrapper command = this.commandLoader.getCommand(statementName);

        if (command == null || command.getCommand() == null || !"statement".equals(command.getCommandType()))
            throw new DataAccessException(String.format("类型'%s'的指令'%s'获取无效！", this.getClass(), statementName));

        DbStatement statement = (DbStatement) command.getCommand();

        //region process tableShard
        if (tableShard != null
                && !tableShard.trim().equals("")
                && statement.getStatementText() != null
                && !statement.getStatementText().trim().equals("")
                && statement.getStatementText().contains(TableShardToken)
                ) {
            statement.setStatementText(statement.getStatementText().replace(TableShardToken, tableShard));
        }
        //endregion

        //region process paras
        if (params != null
                && params.size() > 0
                && statement.getParameters() != null
                && statement.getParameters().size() > 0) {
            //statement.getStatement().getParameters().setParaValues(params);
            for (String paraName : statement.getParameters().keySet()) {
                if (params.containsKey(paraName)) {
                    statement.getParameters().put(paraName, params.get(paraName));
                }
            }
        }
        //endregion

        return this.database.executeNoQueryStatement(command.getShardType(), databaseShard, statement);
    }

    protected int executeNoQueryStatement(String statementName, Map<String, Object> params, String databaseShard) {
        return executeNoQueryStatement(statementName, params, databaseShard, "");
    }

    protected int executeNoQueryStatement(String statementName, Map<String, Object> params) {
        return executeNoQueryStatement(statementName, params, "", "");
    }
    //endregion

    //region executeQuery

    /**
     * 执行查询
     *
     * @param entityClazz   实体类型
     * @param queryName
     * @param criteria
     * @param databaseShard
     * @param tableShard
     * @param <TEntity>
     * @return
     */
    protected <TEntity> DbQueryResult<TEntity> executeQuery(Class<TEntity> entityClazz, String queryName, DbQueryCriteria criteria, String databaseShard, String tableShard) {
        DbCommandWrapper command = this.commandLoader.getCommand(queryName);
        if (command == null || command.getCommand() == null || !"query".equals(command.getCommandType()))
            throw new DataAccessException(String.format("类型'%s'的查询'%s'获取无效！", this.getClass(), queryName));

        DbQuery dbQuery = (DbQuery) command.getCommand();

        //region process tableShard
        if (tableShard != null && !tableShard.trim().equals("")) {
            if (dbQuery.getFromTable() != null
                    && !dbQuery.getFromTable().getName().trim().equals("")
                    && dbQuery.getFromTable().getName().contains(TableShardToken))
                dbQuery.getFromTable().setName(dbQuery.getFromTable().getName().replace(TableShardToken, tableShard));
            if (dbQuery.getJoinTables() != null && dbQuery.getJoinTables().size() > 0) {
                for (DbJoinTable joinTable : dbQuery.getJoinTables()) {
                    if (joinTable.getName() != null
                            && !joinTable.getName().trim().equals("")
                            && joinTable.getName().contains(TableShardToken))
                        joinTable.setName(joinTable.getName().replace(TableShardToken, tableShard));
                }
            }
        }
        //endregion

        //region process criteria
        dbQuery.mergeCriteria(criteria);
        //endregion

        //执行查询
        return this.database.excuteQuery(entityClazz, command.getShardType(), databaseShard, dbQuery);
    }

    protected <TEntity> DbQueryResult<TEntity> executeQuery(Class<TEntity> entityClazz, String queryName, DbQueryCriteria criteria, String databaseShard) {
        return executeQuery(entityClazz, queryName, criteria, databaseShard, "");
    }

    protected <TEntity> DbQueryResult<TEntity> executeQuery(Class<TEntity> entityClazz, String queryName, DbQueryCriteria criteria) {
        return executeQuery(entityClazz, queryName, criteria, "", "");
    }
    //endregion
}
