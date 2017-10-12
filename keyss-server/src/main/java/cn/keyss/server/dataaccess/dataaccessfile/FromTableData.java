package cn.keyss.server.dataaccess.dataaccessfile;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * 选择表
 */
public class FromTableData {

    //region private fields
    private String schema;
    private String name;
    private String tableType;
    private String alias;
    //endregion

    /**
     * 架构
     *
     * @return
     */
    @XmlAttribute(name = "schema")
    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * 名称
     *
     * @return
     */
    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 表类型
     *
     * @return
     */
    @XmlAttribute(name = "tableType")
    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    /**
     * 别名
     *
     * @return
     */
    @XmlAttribute(name = "alias")
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
