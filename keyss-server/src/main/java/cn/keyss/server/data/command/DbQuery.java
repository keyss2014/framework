package cn.keyss.server.data.command;

import cn.keyss.common.utilities.StringHelper;
import cn.keyss.server.data.DataException;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据库查询
 */
public class DbQuery implements Serializable {
    //region private fields
    private static final long serialVersionUID = -2477463364971212320L;
    private int startIndex = 0;
    private int maxRecordReturn = 0;
    private List<DbSelect> selects;
    private DbFromTable fromTable;
    private List<DbJoinTable> joinTables;
    private List<DbOrder> orders;
    private List<List<DbFilter>> filters;
    private Map<String, Object> parameters;
    private List<DbQualifiedColumn> qualifiedColumns;
    //endregion

    //region public methods

    /**
     * 开始索引
     *
     * @return
     */
    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    /**
     * 最大返回值
     *
     * @return
     */
    public int getMaxRecordReturn() {
        return maxRecordReturn;
    }

    public void setMaxRecordReturn(int maxRecordReturn) {
        this.maxRecordReturn = maxRecordReturn;
    }

    /**
     * 选择列
     *
     * @return
     */
    public List<DbSelect> getSelects() {
        return selects;
    }

    public void setSelects(List<DbSelect> selects) {
        this.selects = selects;
    }

    /**
     * 主表
     *
     * @return
     */
    public DbFromTable getFromTable() {
        return fromTable;
    }

    public void setFromTable(DbFromTable fromTable) {
        this.fromTable = fromTable;
    }

    /**
     * 连接表
     *
     * @return
     */
    public List<DbJoinTable> getJoinTables() {
        return joinTables;
    }

    public void setJoinTables(List<DbJoinTable> joinTables) {
        this.joinTables = joinTables;
    }

    /**
     * 排序列
     *
     * @return
     */
    public List<DbOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<DbOrder> orders) {
        this.orders = orders;
    }

    /**
     * 过滤器
     * 最外层过滤器组之间为或关系
     * 在同一个过滤器组之内是与关系
     * 子过滤器根据子过滤器的逻辑类型决定与或关系
     *
     * @return
     */
    public List<List<DbFilter>> getFilters() {
        return filters;
    }

    public void setFilters(List<List<DbFilter>> filters) {
        this.filters = filters;
    }

    /**
     * 参数
     *
     * @return
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * 列信息
     *
     * @return
     */
    public List<DbQualifiedColumn> getQualifiedColumns() {
        return qualifiedColumns;
    }

    public void setQualifiedColumns(List<DbQualifiedColumn> qualifiedColumns) {
        this.qualifiedColumns = qualifiedColumns;
    }

    /**
     * 合并查询条件
     *
     * @param criteria
     */
    public void mergeCriteria(DbQueryCriteria criteria) {
        if (criteria == null)
            return;
        this.startIndex = criteria.getStartIndex();
        this.maxRecordReturn = criteria.getMaxRecordReturn();

        if (criteria.getSelects() != null)
            this.setSelects(criteria.getSelects());
        if (criteria.getOrders() != null)
            this.setOrders(criteria.getOrders());
        if (criteria.getFilters() != null)
            this.setFilters(criteria.getFilters());
        if (criteria.getParameters() != null)
            this.setParameters(criteria.getParameters());
    }
    //endregion

    //region validate query
    private DbQualifiedColumn findQualifiedColumn(String table, String name, String property) {
        if (this.getQualifiedColumns() != null && this.getQualifiedColumns().size() > 0) {
            for (DbQualifiedColumn col : this.getQualifiedColumns()) {
                if ((StringHelper.areEqual(col.getTable(), table) && StringHelper.areEqual(col.getName(), name)) || StringHelper.areEqual(col.getProperty(), property))
                    return col;
            }
        }
        throw new DataException(String.format("表名%s，列名%s，属性名%s的列不存在！", table, name, property));
    }

    /**
     * 校验及格式化
     */
    public void validateAndFormat() {
        //region fromTable
        if (this.getFromTable() == null || this.getFromTable().getName() == null || "".equals(this.getFromTable().getName().trim()))
            throw new DataException("数据查询未指定有效的查询表！");

        if (this.getFromTable().getTableType() == DbTableType.DEFAULT && (this.getFromTable().getAlias() == null || "".equals(this.getFromTable().getAlias().trim())))
            this.getFromTable().setAlias(this.getFromTable().getName());

        if (this.getFromTable().getAlias() == null || "".equals(this.getFromTable().getAlias().trim()))
            throw new DataException("语义表或变量表未指定表别名！");
        //endregion

        //region qualified columns
        if (this.getQualifiedColumns() != null && this.getQualifiedColumns().size() > 0) {
            for (DbQualifiedColumn col : this.getQualifiedColumns())
                if (col.getTable() == null || "".equals(col.getTable().trim()))
                    col.setTable(this.getFromTable().getAlias());
        }
        //endregion

        //region join table
//        ArrayList<String> tables = new ArrayList<>();
//        tables.add(this.getFromTable().getAlias());
        if (this.getJoinTables() != null && this.getJoinTables().size() > 0) {
            for (DbJoinTable join : this.getJoinTables()) {
                if (join.getName() == null)
                    throw new DataException("查询未指定数据库连接表！");

                if (join.getTableType() == DbTableType.DEFAULT && (join.getAlias() == null || "".equals(join.getAlias().trim())))
                    join.setAlias(join.getName());

                if (join.getAlias() == null || "".equals(join.getAlias().trim()))
                    throw new DataException("语义表或变量表未指定表别名！");

                if (join.getConditions() == null || join.getConditions().length == 0)
                    throw new DataException("未指定数据库连接表连接条件！");
//                tables.add(join.getAlias());

                for (DbJoinCondition condition : join.getConditions()) {
                    if (condition.getLeftColumn() == null || condition.getLeftColumn().getName() == null || "".equals(condition.getLeftColumn().getName().trim()))
                        throw new DataException("未指定连接条件列的名称！");
                    if (condition.getRightColumn() == null || condition.getRightColumn().getName() == null || "".equals(condition.getRightColumn().getName().trim()))
                        throw new DataException("未指定连接条件列的名称！");

                    if (condition.getLeftColumn().getTable() == null || "".equals(condition.getLeftColumn().getTable()))
                        condition.getLeftColumn().setTable(this.getFromTable().getAlias());
                    if (condition.getRightColumn().getTable() == null || "".equals(condition.getRightColumn().getTable()))
                        condition.getRightColumn().setTable(this.getFromTable().getAlias());
                }
            }
        }

        //endregion

        //region page
        if ((this.getStartIndex() != 0 || this.getMaxRecordReturn() != 0) && (this.getOrders() == null || this.getOrders().size() == 0))
            throw new DataException("分页查询时未提供排序列！");
        //endregion

        //region selects
        //如果没有设置选择列，则全选
        if (this.getSelects() == null || this.getSelects().size() == 0) {
            DbSelect select = new DbSelect();
            select.setTable(this.getFromTable().getAlias());
            select.setName("*");
            ArrayList<DbSelect> selects = new ArrayList<>();
            selects.add(select);
            this.setSelects(selects);
        } else {
            for (DbSelect select : this.getSelects()) {
                if (select.getName() != null && select.getName().trim().equals("*")) {
                    if (select.getTable() == null || select.getTable().trim().equals(""))
                        select.setTable(this.getFromTable().getAlias());
                } else {
                    DbQualifiedColumn qualifiedColumn = this.findQualifiedColumn(select.getTable(), select.getName(), select.getProperty());
                    if (select.getTable() == null || select.getTable().trim().equals("")) {
                        select.setTable(qualifiedColumn.getTable());
                    }
                    if (select.getName() == null || select.getName().trim().equals("")) {
                        select.setName(qualifiedColumn.getName());
                    }
                }
            }
        }
        //endregion

        //region orders
        if (this.getOrders() != null && this.getOrders().size() > 0) {
            for (DbOrder order : this.getOrders()) {
                DbQualifiedColumn column = this.findQualifiedColumn(order.getTable(), order.getName(), order.getProperty());
                if (order.getTable() == null || order.getTable().trim().equals("")) {
                    order.setTable(column.getTable());
                }
                if (order.getName() == null || order.getName().trim().equals("")) {
                    order.setName(column.getName());
                }
            }
        }
        //endregion

        //region filters
        if (this.getFilters() != null && this.getFilters().size() != 0) {
            for (List<DbFilter> filters : this.getFilters()) {
                if (filters == null || filters.size() == 0)
                    continue;
                for (DbFilter filter : filters) {
                    if(filter.getComparison() == null) {
                        filter.setComparison(DbComparison.EQUALS);
                    }
                    DbQualifiedColumn qualifiedColumn = this.findQualifiedColumn(filter.getTable(), filter.getName(), filter.getProperty());

                    if (filter.getTable() == null || "".equals(filter.getTable().trim())) {
                        filter.setTable(qualifiedColumn.getTable());
                    }
                    if (filter.getName() == null || "".equals(filter.getName().trim())) {
                        filter.setName(qualifiedColumn.getName());
                    }

                    if (filter.getValue() == null && filter.getComparison() != DbComparison.EQUALS && filter.getComparison() != DbComparison.NOT_EQUALS)
                        throw new DataException("当筛选值为Null时仅支持等于或不等于！");

                    if ((filter.getComparison() == DbComparison.IN || filter.getComparison() == DbComparison.NOT_IN) && !filter.getValue().getClass().isArray())
                        throw new DataException("当筛选条件为in或者not in时,筛选值应为数组！");

                    if ((filter.getComparison() == DbComparison.BETWEEN || filter.getComparison() == DbComparison.NOT_BETWEEN)
                            && (!filter.getValue().getClass().isArray() || Array.getLength(filter.getValue()) != 2))
                        throw new DataException("当筛选条件为between或者not between时,筛选值应为长度为2的数组！");

                    if (filter.getSubFilters() == null || filter.getSubFilters().size() == 0)
                        continue;
                    for (DbSubFilter subFilter : filter.getSubFilters()) {
                        if (subFilter.getValue() == null && subFilter.getComparison() != DbComparison.EQUALS && subFilter.getComparison() != DbComparison.NOT_EQUALS)
                            throw new DataException("当筛选值为Null时仅支持等于或不等于！");

                        if ((subFilter.getComparison() == DbComparison.IN || subFilter.getComparison() == DbComparison.NOT_IN) && !subFilter.getValue().getClass().isArray())
                            throw new DataException("当筛选条件为in或者not in时,筛选值应为数组！");

                        if ((subFilter.getComparison() == DbComparison.BETWEEN || subFilter.getComparison() == DbComparison.NOT_BETWEEN)
                                && (!subFilter.getValue().getClass().isArray() || Array.getLength(subFilter.getValue()) != 2))
                            throw new DataException("当筛选条件为between或者not between时,筛选值应为长度为2的数组！");
                    }
                }
            }
        }
        //endregion
    }

    //endregion
}
