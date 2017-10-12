package cn.keyss.server.dataaccess.dataaccessfile;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * 排序列
 */
public class OrderData {
    //region private fields
    private String name;
    private String table;
    private String sorting;
    //endregion

    @XmlAttribute(name = "table")
    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "sorting")
    public String getSorting() {
        return sorting;
    }

    public void setSorting(String sorting) {
        this.sorting = sorting;
    }
}
