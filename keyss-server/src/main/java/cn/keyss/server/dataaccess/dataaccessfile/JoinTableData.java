package cn.keyss.server.dataaccess.dataaccessfile;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * 连接表
 */
public class JoinTableData {

    //region private fields
    private String joinType;
    private String schema;
    private String name;
    private String tableType;
    private String alias;
    private JoinConditionData[] conditions;
    //endregion

    @XmlElement(name = "joinType")
    public String getJoinType() {
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }

    @XmlAttribute(name = "schema")
    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "tableType")
    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    @XmlAttribute(name = "alias")
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @XmlElementWrapper(name = "conditions")
    @XmlElement(name = "condition")
    public JoinConditionData[] getConditions() {
        return conditions;
    }

    public void setConditions(JoinConditionData[] conditions) {
        this.conditions = conditions;
    }
}
