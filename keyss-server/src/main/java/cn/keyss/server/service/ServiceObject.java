package cn.keyss.server.service;

import cn.keyss.common.query.*;
import cn.keyss.server.data.command.*;
import cn.keyss.server.mapping.Mapper;

import java.util.ArrayList;
import java.util.List;

public abstract class ServiceObject {
    private static Mapper mapper = new Mapper(false);

    public Mapper getMapper() {
        return mapper;
    }


    private DbComparison parseComparison(Comparison comparison) {
        if (comparison == Comparison.GREATER_THAN) {
            return DbComparison.GREATER_THAN;
        } else if (comparison == Comparison.GREATER_OR_EQUALS) {
            return DbComparison.GREATER_OR_EQUALS;
        } else if (comparison == Comparison.LESS_THAN) {
            return DbComparison.LESS_THAN;
        } else if (comparison == Comparison.LESS_OR_EQUALS) {
            return DbComparison.LESS_OR_EQUALS;
        } else if (comparison == Comparison.EQUALS) {
            return DbComparison.EQUALS;
        } else if (comparison == Comparison.NOT_EQUALS) {
            return DbComparison.NOT_EQUALS;
        } else if (comparison == Comparison.BETWEEN) {
            return DbComparison.BETWEEN;
        } else if (comparison == Comparison.NOT_BETWEEN) {
            return DbComparison.NOT_BETWEEN;
        } else if (comparison == Comparison.IN) {
            return DbComparison.IN;
        } else if (comparison == Comparison.NOT_IN) {
            return DbComparison.NOT_IN;
        } else if (comparison == Comparison.LIKE) {
            return DbComparison.LIKE;
        } else {
            return DbComparison.NOT_LIKE;
        }
    }

    public DbQueryCriteria buildDbQueryCriteria(QueryCriteria criteria) {
        DbQueryCriteria dbQueryCriteria = new DbQueryCriteria();
        if (criteria == null)
            return dbQueryCriteria;

        dbQueryCriteria.setStartIndex(criteria.getStartIndex());
        dbQueryCriteria.setMaxRecordReturn(criteria.getMaxRecordReturn());

        if (criteria.getSelects() != null && criteria.getSelects().size() > 0) {
            List<DbSelect> dbSelects = new ArrayList<>();
            for (Select select : criteria.getSelects()) {
                DbSelect dbSelect = new DbSelect();
                dbSelect.setProperty(select.getProperty());
                dbSelects.add(dbSelect);
            }
            dbQueryCriteria.setSelects(dbSelects);
        }

        if (criteria.getOrders() != null && criteria.getOrders().size() > 0) {
            List<DbOrder> dbOrders = new ArrayList<>();
            for (Order order : criteria.getOrders()) {
                DbOrder dbOrder = new DbOrder();
                if (order.getSorting() == Sorting.DESCEDING) {
                    dbOrder.setSorting(DbSorting.DESCEDING);
                } else {
                    dbOrder.setSorting(DbSorting.ASCEDING);
                }
                dbOrder.setProperty(order.getProperty());
                dbOrders.add(dbOrder);
            }
            dbQueryCriteria.setOrders(dbOrders);
        }

        if (criteria.getFilters() != null && criteria.getFilters().size() > 0) {
            List<List<DbFilter>> dbFilterss = new ArrayList<>();
            for (List<Filter> filters : criteria.getFilters()) {
                if (filters != null && filters.size() > 0) {
                    List<DbFilter> dbFilters = new ArrayList<>();
                    for (Filter filter : filters) {
                        DbFilter dbFilter = new DbFilter();
                        dbFilter.setProperty(filter.getProperty());
                        dbFilter.setComparison(this.parseComparison(filter.getComparison()));
                        dbFilter.setValue(filter.getValue());
                        if (filter.getSubFilters() != null && filter.getSubFilters().size() > 0) {
                            List<DbSubFilter> dbSubFilters = new ArrayList<>();
                            for (SubFilter subFilter : filter.getSubFilters()) {
                                DbSubFilter dbSubFilter = new DbSubFilter();
                                if (subFilter.getLogic() == Logic.OR) {
                                    dbSubFilter.setLogic(DbLogic.OR);
                                } else {
                                    dbSubFilter.setLogic(DbLogic.AND);
                                }
                                dbSubFilter.setValue(subFilter.getValue());
                                dbSubFilter.setComparison(this.parseComparison(subFilter.getComparison()));
                                dbSubFilters.add(dbSubFilter);
                            }
                            dbFilter.setSubFilters(dbSubFilters);
                        }
                        dbFilters.add(dbFilter);
                    }
                    dbFilterss.add(dbFilters);
                }
            }
            dbQueryCriteria.setFilters(dbFilterss);
        }
        return dbQueryCriteria;
    }

    public <TDataClass, TEntityClass> QueryResult<TDataClass> buildQueryResult(DbQueryResult<TEntityClass> result, Class<TDataClass> dataClazz, Class<TEntityClass> entityClass) {
        QueryResult<TDataClass> queryResult = new QueryResult<TDataClass>();
        queryResult.setTotal(result.getTotal());
        queryResult.setRecords(this.getMapper().buildDatas(dataClazz, result.getRecords().iterator()));
        return queryResult;
    }
}
