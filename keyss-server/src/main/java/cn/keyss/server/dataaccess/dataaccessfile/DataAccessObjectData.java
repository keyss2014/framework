package cn.keyss.server.dataaccess.dataaccessfile;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 数据访问对象
 */
@XmlRootElement(name = "dataAccessObject")
public class DataAccessObjectData {
    //region private fields
    private StatementData[] statements;
    private QueryData[] queries;
    //endregion

    /**
     * 指令
     *
     * @return
     */
    @XmlElementWrapper(name = "statements")
    @XmlElement(name = "statement")
    public StatementData[] getStatements() {
        return statements;
    }

    public void setStatements(StatementData[] statements) {
        this.statements = statements;
    }

    /**
     * 查询
     *
     * @return
     */
    @XmlElementWrapper(name = "queries")
    @XmlElement(name = "query")
    public QueryData[] getQueries() {
        return this.queries;
    }

    public void setQueries(QueryData[] queries) {
        this.queries = queries;
    }
}
