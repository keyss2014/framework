package cn.keyss.server.data.dataengine;

import cn.keyss.server.data.DataEngine;
import cn.keyss.server.data.command.*;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 信息部 on 2016/10/27.
 */
public class H2DataEngine implements DataEngine {
    //region append tables

    /**
     * 添加列
     *
     * @param sb
     * @param column
     */
    protected void appendColumn(StringBuilder sb, DbColumn column) {
        sb.append("`");
        sb.append(column.getTable());
        sb.append("`");
        sb.append(".");
        sb.append("`");
        sb.append(column.getName());
        sb.append("`");
    }

    /**
     * 添加表
     *
     * @param sb
     * @param table
     */
    private void appendTable(StringBuilder sb, DbTable table) {
        switch (table.getTableType()) {
            case DEFAULT:
                sb.append("`");
                sb.append(table.getName());
                sb.append("`");
                break;
            case SQL:
                sb.append("(");
                sb.append(table.getName());
                sb.append(")");
                break;
            case VARIABLE:
                sb.append(table.getName());
                break;
        }
        sb.append(" ");
        sb.append("`");
        sb.append(table.getAlias());
        sb.append("`");
    }

    /**
     * 添加关联表
     *
     * @param sb
     * @param query
     */
    protected void appendTables(StringBuilder sb, DbQuery query) {
        //拼接查询主表-由各个provider自己实现
        appendTable(sb, query.getFromTable());
        //拼接Join表
        if (query.getJoinTables() == null || query.getJoinTables().size() == 0)
            return;
        for (DbJoinTable join : query.getJoinTables()) {
            sb.append(" ");
            DbJoinType joinType = join.getType();
            switch (joinType) {
                case INNER_JOIN:
                    sb.append("INNER");
                    break;
                case LEFT_JOIN:
                    sb.append("LEFT");
                    break;
                case OUTTER_JOIN:
                    sb.append("OUTER");
                    break;
                case RIGHT_JOIN:
                    sb.append("RIGHT");
                    break;
            }
            sb.append(" JOIN ");
            this.appendTable(sb, join);

            //拼接Join条件
            if (join.getConditions() == null || join.getConditions().length == 0)
                continue;
            for (int j = 0; j < join.getConditions().length; j++) {
                sb.append(" ");
                DbJoinCondition condition = join.getConditions()[j];
                if (j == 0) {
                    sb.append("ON");
                } else {
                    sb.append("AND");
                }
                this.appendColumn(sb, condition.getLeftColumn());
                sb.append("=");
                this.appendColumn(sb, condition.getRightColumn());
            }
        }
    }
    //endregion

    //region append filters
    //拼接过滤条件
    protected int appendFilter(StringBuilder sb, Map<String, Object> dynamicParas,
                               int parameterIndex, DbColumn column, DbComparison comparison, Object value) {

        this.appendColumn(sb, column);
        sb.append(" ");

        if (value == null) {
            if (comparison == DbComparison.NOT_EQUALS) {
                sb.append("NOT ");
            }
            sb.append("IS NULL");
            return parameterIndex;
        }
        switch (comparison) {
            case EQUALS:
                sb.append("= :P");
                sb.append(parameterIndex);
                dynamicParas.put(String.format("P%s", parameterIndex++), value);
                break;
            case NOT_EQUALS:
                sb.append("<> :P");
                sb.append(parameterIndex);
                dynamicParas.put(String.format("P%s", parameterIndex++), value);
                break;
            case NOT_LIKE:
                sb.append("NOT ");
            case LIKE:
                sb.append("LIKE :P");
                sb.append(parameterIndex);
                if(value.toString().contains("%")){
                    dynamicParas.put(String.format("P%s", parameterIndex++), value);
                }else{
                    dynamicParas.put(String.format("P%s", parameterIndex++), value.toString()+"%");
                }
                break;
            case GREATER_THAN:
                sb.append("> :P");
                sb.append(parameterIndex);
                dynamicParas.put(String.format("P%s", parameterIndex++), value);
                break;
            case GREATER_OR_EQUALS:
                sb.append(">= :P");
                sb.append(parameterIndex);
                dynamicParas.put(String.format("P%s", parameterIndex++), value);
                break;
            case LESS_THAN:
                sb.append("< :P");
                sb.append(parameterIndex);
                dynamicParas.put(String.format("P%s", parameterIndex++), value);
                break;
            case LESS_OR_EQUALS:
                sb.append("<= :P");
                sb.append(parameterIndex);
                dynamicParas.put(String.format("P%s", parameterIndex++), value);
                break;
            case NOT_IN:
                sb.append("NOT ");
            case IN:
                sb.append("IN (");

                for (int i = 0; i < Array.getLength(value); i++) {
                    Object item = Array.get(value, i);
                    sb.append(":P");
                    sb.append(parameterIndex);
                    dynamicParas.put(String.format("P%s", parameterIndex++), item);
                    if (i != Array.getLength(value) - 1)
                        sb.append(",");
                }
                sb.append(")");
                break;
            case NOT_BETWEEN:
                sb.append("NOT ");
            case BETWEEN:
                sb.append("BETWEEN :P");
                sb.append(parameterIndex);
                dynamicParas.put(String.format("P%s", parameterIndex++), Array.get(value, 0));
                sb.append(" AND :P");
                sb.append(parameterIndex);
                dynamicParas.put(String.format("P%s", parameterIndex++), Array.get(value, 1));
                break;
        }
        return parameterIndex;
    }

    protected int appendFilters(StringBuilder sb, DbQuery query, Map<String, Object> dynamicParas) {
        int parameterIndex = 1;
        if (query == null || query.getFilters() == null || query.getFilters().size() == 0)
            return parameterIndex;
        sb.append(" WHERE");

        int i = 0;
        for (List<DbFilter> filters : query.getFilters()) {
            if (filters == null || filters.size() == 0)
                continue;
            sb.append(" ");
            //多Filters拼接处理使用OR
            if (i++ > 0)
                sb.append("OR ");
            if (query.getFilters().size() > 1)
                sb.append("(");
            int j = 0;
            for (DbFilter filter : filters) {
                //每个Filters中各个Filter使用AND
                sb.append(" ");
                if (j++ != 0)
                    sb.append("AND ");
                parameterIndex = appendFilter(sb, dynamicParas, parameterIndex, filter, filter.getComparison(), filter.getValue());
                if (filter.getSubFilters() == null || filter.getSubFilters().size() == 0)
                    continue;
                //嵌套条件拼接
                int subFilterIndex = 0;
                for (DbSubFilter subFilter : filter.getSubFilters()) {
                    sb.append(" ");
                    if (subFilter.getLogic() == DbLogic.AND) {
                        sb.append("AND");
                    } else {
                        sb.append("OR");
                    }
                    sb.append(" ");
                    if (subFilterIndex++ == 0)
                        sb.append("( ");
                    parameterIndex = appendFilter(sb, dynamicParas, parameterIndex, filter, subFilter.getComparison(), subFilter.getValue());
                }
                sb.append(" )");
            }

            if (query.getFilters().size() > 1)
                sb.append(" )");
        }
        return parameterIndex;
    }
    //endregion

    @Override
    public ParseResult parseDbQuerySelectCountSql(DbQuery query) {
        if (query == null)
            throw new IllegalArgumentException("query对象不能为null！");

        HashMap<String, Object> paras = new HashMap<>();
        StringBuilder sqlStr = new StringBuilder();

        //Count查询语句
        sqlStr.append("SELECT COUNT(1) FROM ");
        this.appendTables(sqlStr, query);//拼接查询表
        this.appendFilters(sqlStr, query, paras);//拼接过滤条件

        ParseResult result = new ParseResult();
        result.setDynamicParameters(paras);
        result.setSqlText(sqlStr.toString());
        return result;
    }

    @Override
    public ParseResult parseDbQuerySelectSql(DbQuery query) {
        if (query == null)
            throw new IllegalArgumentException("query对象不能为null！");

        HashMap<String, Object> paras = new HashMap<>();
        StringBuilder sqlStr = new StringBuilder();

        sqlStr.append("SELECT ");
        this.appendSelects(sqlStr, query);
        sqlStr.append(" FROM ");

        this.appendTables(sqlStr, query);
        this.appendFilters(sqlStr, query, paras);
        this.appendOrders(sqlStr, query);

        if (query.getStartIndex() != 0 || query.getMaxRecordReturn() != 0) {
            sqlStr.append(" LIMIT ");
            sqlStr.append(query.getStartIndex());
            sqlStr.append(",");
            sqlStr.append(query.getMaxRecordReturn());
        }

        ParseResult result = new ParseResult();
        result.setDynamicParameters(paras);
        result.setSqlText(sqlStr.toString());
        return result;
    }

    private void appendSelect(StringBuilder sb, DbSelect select) {
        if ("*".equals( select.getName())) {
            if (select.getTable() == null || "".equals(select.getTable().trim()))
                sb.append("*");
            else {
                sb.append("`");
                sb.append(select.getTable());
                sb.append("`");
                sb.append(".*");
            }
        } else {
            this.appendColumn(sb, select);
            if (select.getAlias() != null && !"".equals(select.getAlias().trim())) {
                sb.append(" ");
                sb.append("`");
                sb.append(select.getAlias());
                sb.append("`");
            }
        }
    }

    private void appendSelects(StringBuilder sb, DbQuery query) {
        if (query.getSelects() == null || query.getSelects().size() == 0) {
            DbSelect select = new DbSelect();
            select.setTable(query.getFromTable().getAlias());
            select.setName("*");
            appendSelect(sb, select);
        } else {
            for (int i = 0; i < query.getSelects().size(); i++) {
                DbSelect select = query.getSelects().get(i);
                appendSelect(sb, select);
                if (i != query.getSelects().size() - 1) {
                    sb.append(",");
                }
            }
        }
    }

    private void appendOrder(StringBuilder sb, DbOrder order) {
        this.appendColumn(sb, order);
        if (order.getSorting() == DbSorting.DESCEDING)
            sb.append(" DESC");
    }

    private void appendOrders(StringBuilder sb, DbQuery query) {
        if (query == null || query.getOrders() == null || query.getOrders().size() == 0)
            return;
        for (int i = 0; i < query.getOrders().size(); i++) {
            DbOrder order = query.getOrders().get(i);
            if (i == 0) {
                sb.append(" ORDER BY ");
            }
            appendOrder(sb, order);
            if (i != query.getOrders().size() - 1) {
                sb.append(",");
            }
        }
    }
}
