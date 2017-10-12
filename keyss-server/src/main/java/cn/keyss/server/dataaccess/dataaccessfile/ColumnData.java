package cn.keyss.server.dataaccess.dataaccessfile;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * 列
 */
public class ColumnData {
    //region private fields
    private String property;
    private String table;
    private String name;
    //endregion

    /**
     * 属性
     *
     * @return
     */
    @XmlAttribute(name = "property")
    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * 表
     * 通常为表别名
     *
     * @return
     */
    @XmlAttribute(name = "table")
    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    /**
     * 名称
     *
     * @return
     */
    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
