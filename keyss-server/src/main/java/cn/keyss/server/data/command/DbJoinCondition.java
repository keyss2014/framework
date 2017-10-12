package cn.keyss.server.data.command;

import java.io.Serializable;

/**
 * 连接条件.
 */
public class DbJoinCondition implements Serializable {
    //region private fields
    private static final long serialVersionUID = -8758800200414999583L;
    private DbConditionColumn leftColumn;
    private DbConditionColumn rightColumn;
    //endregion

    /**
     * 左列
     *
     * @return
     */
    public DbConditionColumn getLeftColumn() {
        return leftColumn;
    }

    public void setLeftColumn(DbConditionColumn leftColumn) {
        this.leftColumn = leftColumn;
    }

    /**
     * 右列
     *
     * @return
     */
    public DbConditionColumn getRightColumn() {
        return rightColumn;
    }

    public void setRightColumn(DbConditionColumn rightColumn) {
        this.rightColumn = rightColumn;
    }
}
