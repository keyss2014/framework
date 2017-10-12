package cn.keyss.common.metrics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * 指标信息
 * 在同一个桶中，同样的Key则只会有一个指标信息
 */
public class MetricInfo {
    String name;
    SortedMap<String, String> tags;
    double value;
    long timestamp;
    int count;
    double sum;

    public MetricInfo(String name, SortedMap<String, String> tags, double value) {
        this.name = name;
        this.tags = tags;
        this.value = value;
        this.count = 1;
        this.sum = value;
    }

    public MetricInfo(String name, double value, SortedMap<String, String> tags, long timestamp) {
        this.name = name;
        this.value = value;
        this.tags = tags;
        this.timestamp = timestamp;
        this.count = 1;
        this.sum = value;
    }

    @JsonIgnore
    public String getKey() {
        return computeKey(name, tags, timestamp);
    }

    public void mergeMetrics(MetricInfo info) {
        if (info == null)
            return;
        count = count + info.count;
        sum = sum + info.sum;
    }

    public static String computeKey(String name, SortedMap<String, String> tags, long timestamp) {
        StringBuilder keySb = new StringBuilder(name);
        if (tags != null && tags.size() > 0) {
            for (Map.Entry<String, String> tagEntry : tags.entrySet()) {
                keySb.append("|").append(tagEntry.getKey()).append("=").append(tagEntry.getValue());
            }
        }
        keySb.append("|").append(timestamp);
        return keySb.toString();
    }

    @JsonProperty(value = "timestamp")
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty(value = "name")
    public String getName() {
        return name;
    }

    @JsonProperty(value = "tags")
    public SortedMap<String, String> getTags() {
        return tags;
    }

    @JsonProperty(value = "value")
    public double getValue() {
        return value;
    }

    @JsonIgnore
    public int getCount() {
        return count;
    }

    @JsonIgnore
    public double getSum() {
        return sum;
    }

    public List<MetricInfo> splitEntityToValue() {
        ArrayList<MetricInfo> result = new ArrayList<>();
        result.add(new MetricInfo(String.format("%s.sum", this.getName()), this.getSum(), this.getTags(), this.getTimestamp()));
        result.add(new MetricInfo(String.format("%s.cnt", this.getName()), this.getCount(), this.getTags(), this.getTimestamp()));
        result.add(new MetricInfo(String.format("%s.avg", this.getName()), (this.getCount() > 0) ? (this.getSum() / this.getCount()) : 0, this.getTags(), this.getTimestamp()));
        return result;
    }
}
