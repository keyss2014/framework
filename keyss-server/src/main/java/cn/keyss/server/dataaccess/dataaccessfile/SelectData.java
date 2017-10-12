package cn.keyss.server.dataaccess.dataaccessfile;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * 选择列
 */
public class SelectData {
    //region private fields
    private String name;
    private String table;
    private String alias;
    //endregion

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "table")
    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    @XmlAttribute(name = "alias")
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
