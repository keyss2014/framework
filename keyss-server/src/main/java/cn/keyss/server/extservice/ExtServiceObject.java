package cn.keyss.server.extservice;

import ch.ralscha.extdirectspring.bean.ExtDirectStoreReadRequest;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import ch.ralscha.extdirectspring.bean.SortDirection;
import ch.ralscha.extdirectspring.bean.SortInfo;
import ch.ralscha.extdirectspring.filter.*;
import ch.ralscha.extdirectspring.filter.Filter;
import cn.keyss.common.query.*;
import cn.keyss.common.query.Comparison;
import cn.keyss.server.mapping.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ext服务对象
 */
public abstract class ExtServiceObject {

    private static Mapper mapper = new Mapper(false);

    public Mapper getMapper() {
        return mapper;
    }

    /**
     * 解析比较符
     *
     * @param extComparison
     * @return
     */
    private Comparison parseExtComparison(ch.ralscha.extdirectspring.filter.Comparison extComparison) {
        if (extComparison == ch.ralscha.extdirectspring.filter.Comparison.GREATER_THAN) {
            return Comparison.GREATER_THAN;
        } else if (extComparison == ch.ralscha.extdirectspring.filter.Comparison.GREATER_THAN_OR_EQUAL) {
            return Comparison.GREATER_OR_EQUALS;
        } else if (extComparison == ch.ralscha.extdirectspring.filter.Comparison.LESS_THAN) {
            return Comparison.LESS_THAN;
        } else if (extComparison == ch.ralscha.extdirectspring.filter.Comparison.LESS_THAN_OR_EQUAL) {
            return Comparison.LESS_OR_EQUALS;
        } else if (extComparison == ch.ralscha.extdirectspring.filter.Comparison.IN) {
            return Comparison.IN;
        } else if (extComparison == ch.ralscha.extdirectspring.filter.Comparison.LIKE) {
            return Comparison.LIKE;
        } else if (extComparison == ch.ralscha.extdirectspring.filter.Comparison.NOT_EQUAL) {
            return Comparison.NOT_EQUALS;
        } else
            return Comparison.EQUALS;
    }

    /**
     * 通过请求对象解析出查询条件
     *
     * @param request
     * @return
     */
    protected QueryCriteria retriveQueryCriteria(ExtDirectStoreReadRequest request) {
        return retriveQueryCriteria(request, new HashMap<String, Object>());
    }

    protected QueryCriteria retriveQueryCriteria(ExtDirectStoreReadRequest request, Map<String, Object> extraFilters) {
        if (request == null)
            throw new IllegalArgumentException("request");
        if (extraFilters == null)
            throw new IllegalArgumentException("extraFilters");

        QueryCriteria queryCriteria = new QueryCriteria();
        if (request.getStart() != null)
            queryCriteria.setStartIndex(request.getStart());
        if (request.getLimit() != null)
            queryCriteria.setMaxRecordReturn(request.getLimit());

        if (request.getSorters() != null && request.getSorters().size() > 0) {
            List<Order> orders = new ArrayList<>();
            for (SortInfo sortInfo : request.getSorters()) {
                Order order = new Order();
                order.setProperty(sortInfo.getProperty());
                if (sortInfo.getDirection() == SortDirection.DESCENDING) {
                    order.setSorting(Sorting.DESCEDING);
                } else {
                    order.setSorting(Sorting.ASCEDING);
                }
                orders.add(order);
            }
            queryCriteria.setOrders(orders);
        }

        if ((request.getFilters() != null && request.getFilters().size() > 0) || extraFilters.size() > 0) {
            List<List<cn.keyss.common.query.Filter>> filterss = new ArrayList<>();
            filterss.add(new ArrayList<>());
            if (extraFilters.size() > 0) {
                for (String key : extraFilters.keySet()) {
                    cn.keyss.common.query.Filter dbFilter = new cn.keyss.common.query.Filter();
                    dbFilter.setProperty(key);
                    dbFilter.setComparison(Comparison.EQUALS);
                    dbFilter.setValue(extraFilters.get(key));
                    filterss.get(0).add(dbFilter);
                }
            }
            if (request.getFilters() != null && request.getFilters().size() > 0) {
                for (Filter filter : request.getFilters()) {
                    cn.keyss.common.query.Filter dbFilter = new cn.keyss.common.query.Filter();
                    dbFilter.setProperty(filter.getField());
                    dbFilter.setComparison(this.parseExtComparison(filter.getComparison()));

                    if (filter instanceof NumericFilter) {
                        NumericFilter numericFilter = (NumericFilter) filter;
                        dbFilter.setValue(numericFilter.getValue());
                    } else if (filter instanceof DateFilter) {
                        DateFilter dateFilter = (DateFilter) filter;
                        dbFilter.setValue(dateFilter.getValue());
                    } else if (filter instanceof ListFilter) {
                        ListFilter listFilter = (ListFilter) filter;
                        dbFilter.setValue(listFilter.getValue());
                    } else if (filter instanceof BooleanFilter) {
                        BooleanFilter booleanFilter = (BooleanFilter) filter;
                        dbFilter.setValue(booleanFilter.getValue());
                    } else if (filter instanceof StringFilter) {
                        StringFilter stringFilter = (StringFilter) filter;
                        dbFilter.setValue(stringFilter.getValue());
                    } else {
                        //不认识的过滤器
                    }
                    filterss.get(0).add(dbFilter);
                }
            }
            queryCriteria.setFilters(filterss);
        }
        return queryCriteria;
    }


    /**
     * 准备正确的查询结果
     *
     * @param queryResult
     * @return
     */
    protected ExtDirectStoreResult prepareExtQueryNormalResult(QueryResult queryResult) {
        ExtDirectStoreResult result = new ExtDirectStoreResult(true);
        if (queryResult == null)
            return result;
        result.setTotal(queryResult.getTotal());
        result.setRecords(queryResult.getRecords());
        return result;
    }

    /**
     * 准备异常的查询结果
     *
     * @param message
     * @return
     */
    protected ExtDirectStoreResult prepareExtQueryErrorResult(String message) {
        ExtDirectStoreResult result = new ExtDirectStoreResult(false);
        result.setMessage(message);
        return result;
    }
}
