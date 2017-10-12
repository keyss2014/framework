package cn.keyss.common.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 指标
 */
public class Metric implements AutoCloseable {

    private Logger logger= LoggerFactory.getLogger(Metric.class);
    private String name;
    RingBuffer data;
    boolean isClose;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Metric(RingBuffer data) {
        this.data = data;
        logger.info("{}-启动Metrics", Thread.currentThread().getName());
    }

    /**
     * 添加metric点
     *
     * @param value
     */
    public void add(double value) throws MetricException {
        add(value, null);
    }

    /**
     * 添加metric点
     *
     * @param value
     * @param tags
     */
    public void add(double value, SortedMap<String, String> tags) throws MetricException {
        if (isClose)
            return;
        if (tags == null)
            tags = new TreeMap<>();
        // tags.put("HOST_NAME", HostHelper.getLocalHostName());
        data.add(new MetricInfo(name, tags, value));
    }

    @Override
    public void close() {
        isClose = true;
    }
}
