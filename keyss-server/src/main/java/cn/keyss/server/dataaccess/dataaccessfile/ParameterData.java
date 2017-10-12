package cn.keyss.server.dataaccess.dataaccessfile;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * 参数
 */
public class ParameterData {
    //region private fields
    private String name;
    //endregion

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
