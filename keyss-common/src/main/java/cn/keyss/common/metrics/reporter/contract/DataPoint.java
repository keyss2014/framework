package cn.keyss.common.metrics.reporter.contract;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.Map;

/**
 * Created by wangkun on 2016/1/23.
 */
public class DataPoint {
    private String name;
    //到毫秒的时间戳
    private long timestamp;
    private double value;
    private Map<String,String> tags;

    public DataPoint() {
    }

    public DataPoint(String name, long timestamp, double value) {
        this.name = name;
        this.timestamp = timestamp;
        this.value = value;
    }

    public DataPoint(String name, long timestamp, double value, Map<String, String> tags) {
        this.name = name;
        this.timestamp = timestamp;
        this.value = value;
        this.tags = tags;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @XmlAttribute
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @XmlAttribute
    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }
}
