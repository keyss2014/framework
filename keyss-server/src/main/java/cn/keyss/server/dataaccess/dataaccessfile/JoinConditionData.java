package cn.keyss.server.dataaccess.dataaccessfile;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * 连接条件
 */
public class JoinConditionData {
    //region private fields
    private String leftTable;
    private String leftColumn;
    private String rightTable;
    private String rightColumn;
    //endregion

    @XmlAttribute(name = "leftTable")
    public String getLeftTable() {
        return leftTable;
    }

    public void setLeftTable(String leftTable) {
        this.leftTable = leftTable;
    }

    @XmlAttribute(name = "leftColumn")
    public String getLeftColumn() {
        return leftColumn;
    }

    public void setLeftColumn(String leftColumn) {
        this.leftColumn = leftColumn;
    }

    @XmlAttribute(name = "rightTable")
    public String getRightTable() {
        return rightTable;
    }

    public void setRightTable(String rightTable) {
        this.rightTable = rightTable;
    }

    @XmlAttribute(name = "rightColumn")
    public String getRightColumn() {
        return rightColumn;
    }

    public void setRightColumn(String rightColumn) {
        this.rightColumn = rightColumn;
    }
}
