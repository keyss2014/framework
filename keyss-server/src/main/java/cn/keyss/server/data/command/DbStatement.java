package cn.keyss.server.data.command;


import cn.keyss.server.data.DataException;

import java.io.Serializable;
import java.util.Map;

/**
 * 指令
 */
public class DbStatement implements Serializable {
    //region private fields
    private static final long serialVersionUID = -1028065180630114889L;
    private String statementText;
    private Map<String, Object> parameters;
    //endregion

    /**
     * 执行指令
     *
     * @return
     */
    public String getStatementText() {
        return statementText;
    }

    public void setStatementText(String statementText) {
        this.statementText = statementText;
    }

    /**
     * 输入参数
     *
     * @return
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * 校验及格式化
     */
    public void validateAndFormat() {
        if (this.getStatementText() == null || "".equals(this.getStatementText().trim()))
            throw new DataException("未提供数据访问指令！");
    }
}
