package cn.keyss.server.dataaccess.dataaccessfile;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * 查询
 */
public class QueryData {

    //region private fields
    private String name;
    private OrderData[] orders;
    private FromTableData fromTable;
    private JoinTableData[] joinTables;
    private ColumnData[] columns;
    private ParameterData[] parameters;
    private String shardType;
    //endregion

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElementWrapper(name = "orders")
    @XmlElement(name = "order")
    public OrderData[] getOrders() {
        return this.orders;
    }

    public void setOrders(OrderData[] orders) {
        this.orders = orders;
    }

    @XmlElement(name = "fromTable")
    public FromTableData getFromTable() {
        return this.fromTable;
    }

    public void setFromTable(FromTableData fromTable) {
        this.fromTable = fromTable;
    }

    @XmlElementWrapper(name = "joinTables")
    @XmlElement(name = "joinTable")
    public JoinTableData[] getJoinTables() {
        return this.joinTables;
    }

    public void setJoinTables(JoinTableData[] joinTables) {
        this.joinTables = joinTables;
    }

    @XmlElementWrapper(name = "columns")
    @XmlElement(name = "column")
    public ColumnData[] getColumns() {
        return this.columns;
    }

    public void setColumns(ColumnData[] columns) {
        this.columns = columns;
    }

    @XmlElementWrapper(name = "parameters")
    @XmlElement(name = "parameter")
    public ParameterData[] getParameters() {
        return this.parameters;
    }

    public void setParameters(ParameterData[] parameters) {
        this.parameters = parameters;
    }

    @XmlAttribute(name = "shardType")
    public String getShardType() {
        return this.shardType;
    }

    public void setShardType(String shardType) {
        this.shardType = shardType;
    }
}
