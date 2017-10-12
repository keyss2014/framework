package cn.keyss.server.data.command;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询条件
 */
public class DbQueryCriteria implements Serializable {
    //region private fields
    private static final long serialVersionUID = -3650406156732186630L;
    private int startIndex;
    private int maxRecordReturn;
    private Map<String, Object> parameters;
    private List<DbSelect> selects;
    private List<DbOrder> orders;
    private List<List<DbFilter>> filters;
    //endregion

    /**
     * 起始索引
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
     * 最大返回记录数
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
     * 查询参数
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
     * 过滤条件
     *
     * @return
     */
    public List<List<DbFilter>> getFilters() {
        return filters;
    }

    public void setFilters(List<List<DbFilter>> filters) {
        this.filters = filters;
    }

    public void merge(DbQueryCriteria criteria){
        if(criteria == null)
            return;
        this.setStartIndex(criteria.getStartIndex());
        this.setMaxRecordReturn(criteria.getMaxRecordReturn());
        this.setParameters(criteria.getParameters());
        this.setSelects(criteria.getSelects());
        this.setOrders(criteria.getOrders());
        this.setFilters(criteria.getFilters());
    }
    public void orFilters(List<DbFilter> dbFilters){
        if(this.getFilters() == null){
            this.setFilters(new ArrayList<List<DbFilter>>());
        }
        this.getFilters().add(dbFilters);
    }
    public void andFilter(DbFilter dbFilter){
        if(this.getFilters() == null){
            this.setFilters(new ArrayList<List<DbFilter>>());
        }
        if(this.getFilters().size() == 0){
            this.getFilters().add(new ArrayList<DbFilter>());
        }
        for(List<DbFilter> filters: this.getFilters()){
            filters.add(dbFilter);
        }
    }
}
