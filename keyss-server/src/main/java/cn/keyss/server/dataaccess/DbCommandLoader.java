package cn.keyss.server.dataaccess;

import cn.keyss.common.utilities.XmlConfigHelper;
import cn.keyss.server.data.ShardType;
import cn.keyss.server.data.command.*;
import cn.keyss.server.dataaccess.dataaccessfile.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 数据库指令加载器
 */
class DbCommandLoader {
    //region private fields
    /**
     * 数据访问对象类型
     */
    private Class<? extends DataAccessObject> dataAccessObjectClazz;
    /**
     * 指令缓存
     */
    private HashMap<String, DbCommandWrapper> commands = new HashMap<>();
    /**
     * 指令锁
     */
    private ReentrantReadWriteLock syncLocker = new ReentrantReadWriteLock();

    /**
     * 构造方法
     *
     * @param dataAccessObjectClazz
     */
    public DbCommandLoader(Class<? extends DataAccessObject> dataAccessObjectClazz) {
        this.dataAccessObjectClazz = dataAccessObjectClazz;
    }
    //endregion

    /**
     * 加载数据访问指令
     *
     * @param commandName
     * @return
     */
    public DbCommandWrapper getCommand(String commandName) {
        if (commandName == null || "".equals(commandName.trim()))
            throw new IllegalArgumentException("commandName不有为null或空字符串!");

        try {
            syncLocker.readLock().lock();
            if (commands.containsKey(commandName) && commands.get(commandName) != null) {
                return commands.get(commandName).clone();
            }
        } finally {
            syncLocker.readLock().unlock();
        }
        try {
            syncLocker.writeLock().lock();
            if (commands.containsKey(commandName) && commands.get(commandName) != null) {
                return commands.get(commandName).clone();
            }
            DbCommandWrapper result = loadCommand(commandName);
            commands.put(commandName, result);
            return result.clone();
        } finally {
            syncLocker.writeLock().unlock();
        }
    }

    /**
     * 加载指令
     *
     * @param commandName
     * @return
     */
    private DbCommandWrapper loadCommand(String commandName) {
        List<DataAccessFile> dataAccessFiles = this.getDataAccessObjectAnnotation();
        if (dataAccessFiles != null && dataAccessFiles.size() > 0) {
            for (DataAccessFile dataAccessFile : dataAccessFiles) {
                //加载配置文件
                DataAccessObjectData setting = loadDataAccessObjectConfig(dataAccessFile);
                if (setting.getStatements() != null && setting.getStatements().length > 0) {
                    for (StatementData statementData : setting.getStatements()) {
                        if (statementData == null || statementData.getName() == null)
                            continue;
                        if (!statementData.getName().trim().toLowerCase().equals(commandName.trim().toLowerCase()))
                            continue;
                        return resoveStatementCommand(statementData);
                    }
                }
                if (setting.getQueries() != null && setting.getQueries().length > 0) {
                    for (QueryData queryData : setting.getQueries()) {
                        if (queryData == null || queryData.getName() == null)
                            continue;
                        if (!queryData.getName().trim().toLowerCase().equals(commandName.trim().toLowerCase()))
                            continue;
                        return resoveQueryCommand(queryData);
                    }
                }
            }
        }
        throw new DataAccessException(String.format("%s未找到指定的数据访问指令:%s", this.dataAccessObjectClazz.getName(), commandName));
    }

    /**
     * 解析Statement
     *
     * @param statementData
     * @return
     */
    private static DbCommandWrapper resoveStatementCommand(StatementData statementData) {
        //解析Statement
        DbStatement dbStatement = new DbStatement();
        dbStatement.setStatementText(statementData.getStatementText());
        if (statementData.getParameters() != null && statementData.getParameters().length > 0) {
            HashMap<String, Object> paras = new HashMap<>();
            for (ParameterData parameterData : statementData.getParameters()) {
                paras.put(parameterData.getName(), null);
            }
            dbStatement.setParameters(paras);
        }

        DbCommandWrapper result = new DbCommandWrapper();
        if ("slave".equals(statementData.getShardType().toLowerCase())) {
            result.setShardType(ShardType.SLAVE);
        } else {
            result.setShardType(ShardType.MASTER);
        }
        result.setCommandType("statement");
        result.setCommand(dbStatement);
        return result;
    }

    /**
     * 解析Query
     *
     * @param queryData
     * @return
     */
    private static DbCommandWrapper resoveQueryCommand(QueryData queryData) {
        DbQuery dbQuery = new DbQuery();

        //region from
        if (queryData.getFromTable() != null) {
            DbFromTable table = new DbFromTable();
            table.setName(queryData.getFromTable().getName());

            if ("sql".equals(queryData.getFromTable().getTableType())) {
                table.setTableType(DbTableType.SQL);
            } else if ("variable".equals(queryData.getFromTable().getTableType())) {
                table.setTableType(DbTableType.VARIABLE);
            } else {
                table.setTableType(DbTableType.DEFAULT);
            }

            table.setAlias(queryData.getFromTable().getAlias());
            table.setSchema(queryData.getFromTable().getSchema());
            dbQuery.setFromTable(table);
        }
        //endregion

        //region joins
        if (queryData.getJoinTables() != null && queryData.getJoinTables().length > 0) {
            List<DbJoinTable> dbJoinTables = new ArrayList<>();
            for (JoinTableData joinTableData : queryData.getJoinTables()) {
                DbJoinTable join = new DbJoinTable();

                //region table
                if ("sql".equals(join.getTableType())) {
                    join.setTableType(DbTableType.SQL);
                } else if ("variable".equals(joinTableData.getTableType())) {
                    join.setTableType(DbTableType.VARIABLE);
                } else {
                    join.setTableType(DbTableType.DEFAULT);
                }
                join.setSchema(joinTableData.getSchema());
                join.setAlias(joinTableData.getAlias());
                join.setName(joinTableData.getName());
                //endregion

                //region jointype
                if ("outter".equals(joinTableData.getJoinType())) {
                    join.setType(DbJoinType.OUTTER_JOIN);
                } else if ("left".equals(joinTableData.getJoinType())) {
                    join.setType(DbJoinType.LEFT_JOIN);
                } else if ("right".equals(joinTableData.getJoinType())) {
                    join.setType(DbJoinType.RIGHT_JOIN);
                } else {
                    join.setType(DbJoinType.INNER_JOIN);
                }
                //endregion

                //region condition
                if (joinTableData.getConditions() != null && joinTableData.getConditions().length > 0) {
                    List<DbJoinCondition> conditions = new ArrayList<>();
                    for (JoinConditionData conditionData : joinTableData.getConditions()) {
                        DbJoinCondition condition = new DbJoinCondition();
                        DbConditionColumn leftColumn = new DbConditionColumn();
                        leftColumn.setName(conditionData.getLeftColumn());
                        leftColumn.setTable(conditionData.getLeftTable());
                        DbConditionColumn rightColumn = new DbConditionColumn();
                        rightColumn.setName(conditionData.getRightColumn());
                        rightColumn.setTable(conditionData.getRightTable());
                        condition.setLeftColumn(leftColumn);
                        condition.setRightColumn(rightColumn);
                        conditions.add(condition);
                    }
                    join.setConditions(conditions.toArray(new DbJoinCondition[0]));
                }
                //endregion

                dbJoinTables.add(join);
            }
            dbQuery.setJoinTables(dbJoinTables);
        }
        //endregion

        //region orders
        if (queryData.getOrders() != null && queryData.getOrders().length > 0) {
            List<DbOrder> orders = new ArrayList<>();
            for (OrderData orderData : queryData.getOrders()) {
                DbOrder order = new DbOrder();
                if ("desceding".equals(orderData.getSorting())) {
                    order.setSorting(DbSorting.DESCEDING);
                } else {
                    order.setSorting(DbSorting.ASCEDING);
                }
                order.setName(orderData.getName());
                order.setTable(orderData.getTable());
                orders.add(order);
            }
            dbQuery.setOrders(orders);
        }
        //endregion

        //region paras
        if (queryData.getParameters() != null && queryData.getParameters().length > 0) {
            HashMap<String, Object> paras = new HashMap<>();
            for (ParameterData parameterData : queryData.getParameters()) {
                paras.put(parameterData.getName(), null);
            }
            dbQuery.setParameters(paras);
        }
        //endregion

        //region columns
        if (queryData.getColumns() != null && queryData.getColumns().length > 0) {
            List<DbQualifiedColumn> columns = new ArrayList<>();
            for (ColumnData columnData : queryData.getColumns()) {
                DbQualifiedColumn column = new DbQualifiedColumn();
                column.setName(columnData.getName());
                column.setTable(columnData.getTable());
                column.setProperty(columnData.getProperty());
                columns.add(column);
            }
            dbQuery.setQualifiedColumns(columns);
        }
        //endregion

        DbCommandWrapper result = new DbCommandWrapper();
        if ("slave".equals(queryData.getShardType())) {
            result.setShardType(ShardType.SLAVE);
        } else {
            result.setShardType(ShardType.MASTER);
        }
        result.setCommandType("query");
        result.setCommand(dbQuery);
        return result;
    }

    private List<DataAccessFile> getDataAccessObjectAnnotation() {
        List<DataAccessFile> files = new ArrayList<>();
        Class<?> type = this.dataAccessObjectClazz;
        while (type != DataAccessObject.class) {
            DataAccessFile file = type.getAnnotation(DataAccessFile.class);
            if (file != null)
                files.add(file);
            type = type.getSuperclass();
        }
        return files;
    }

    private DataAccessObjectData loadDataAccessObjectConfig(DataAccessFile dataAccessFile) {
        if (dataAccessFile == null || dataAccessFile.resource() == null || "".equals(dataAccessFile.resource()))
            throw new DataAccessException(String.format("%s未加载到对应的数据资源文件", this.dataAccessObjectClazz.getName()));
        try {
            return XmlConfigHelper.loadConfig(DataAccessObjectData.class, dataAccessFile.resource());
        } catch (Throwable t) {
            throw new DataAccessException("加载数据访问对象资源文件时发生异常！", t);
        }
    }
}
