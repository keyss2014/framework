package cn.keyss.common.metrics;

import java.util.List;

/**
 * 指标汇报器
 */
public interface Reporter {
    /**
     * 汇报指标
     * @param metricInfos
     */
    void report(List<MetricInfo> metricInfos);
}
