package cn.keyss.server.dataaccess.dataaccessfile;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * 指令数据
 */
public class StatementData {

    //region private fields
    private String name;
    private String statementText;
    private String shardType;
    private ParameterData[] parameters;
    //endregion

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

    /**
     * 指令文本
     *
     * @return
     */
    @XmlElement(name = "statementText")
    public String getStatementText() {
        return statementText;
    }

    public void setStatementText(String statementText) {
        this.statementText = statementText;
    }

    /**
     * 参数
     *
     * @return
     */
    @XmlElementWrapper(name = "parameters")
    @XmlElement(name = "parameter")
    public ParameterData[] getParameters() {
        return parameters;
    }

    public void setParameters(ParameterData[] parameters) {
        this.parameters = parameters;
    }

    /**
     * 分区类型
     * <p>
     * 一般来说需要同步锁定或者更新的语句走主分区，只读可延时语句走从分区
     *
     * @return
     */
    @XmlAttribute(name = "shardType")
    public String getShardType() {
        return shardType;
    }

    public void setShardType(String shardType) {
        this.shardType = shardType;
    }
}
