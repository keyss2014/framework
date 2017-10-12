package cn.keyss.common.metrics;

import cn.keyss.common.utilities.DaemonExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 计数器工厂
 */
public class MetricFactory {
    private Map<String, Metric> metricsMap = new HashMap<>();
    private Object instanceLocker = new Object();
    private Object finishNotify = new Object();
    private List<Reporter> reporters;
    private static Logger logger = LoggerFactory.getLogger(MetricFactory.class);
    //埋点数据
    private RingBuffer data = new RingBuffer(60 * 1000, 12);
    private DaemonExecutor daemonExecutor = new DaemonExecutor(2, 4, 30, 1);

    private boolean isStop = false;

    public  MetricFactory(){
        daemonExecutor.registShutdownTask(10, new Runnable() {
            @Override
            public void run() {
                logger.trace("启动清理线程");
                for (Map.Entry<String, Metric> metricsEntry : metricsMap.entrySet()) {
                    if (metricsEntry.getValue() == null)
                        continue;
                    metricsEntry.getValue().close();
                }
                data.close();
                isStop = true;
                synchronized (finishNotify) {
                    try {
                        finishNotify.wait(1000 * 60);
                    } catch (InterruptedException e) {
                        logger.error("清理数据时发生异常", e);
                    }
                }
            }
        });

        daemonExecutor.registTask(new Runnable() {
            @Override
            public void run() {
                logger.trace("启动Reporter上报线程");
                processReport();
            }
        });
    }



    /**
     * 创建Metric
     *
     * @param metricName
     * @return
     */
    public Metric buildMetric(String metricName) {
        String cacheKey = metricName;
        if (metricsMap.containsKey(cacheKey))
            return metricsMap.get(cacheKey);

        Metric metrics = new Metric(data);
        metrics.setName(metricName);
        synchronized (instanceLocker) {
            if (metricsMap.containsKey(cacheKey))
                return metricsMap.get(cacheKey);
            metricsMap.put(cacheKey, metrics);
            return metrics;
        }
    }

    boolean isStop() {
        return isStop;
    }

    /**
     * 处理上报
     */
    void processReport() {
        while (!isStop()) {
            try {
                Bucket bucket = data.nextInBlock();
                sendToReporter(bucket);
            } catch (Exception ex) {
                logger.error("数据Report发生异常", ex);
            }
        }
        //清理逻辑
        try {
            sendToReporter(data.next());
            sendToReporter(data.readCurrent());
        } catch (Exception ex) {
            logger.error("清理数据时发生异常", ex);
        }
        logger.info("数据清理完毕");
        synchronized (finishNotify) {
            finishNotify.notifyAll();
        }
    }

    void sendToReporter(Bucket bucket) throws ExecutionException, InterruptedException {
        if (bucket == null) {
            return;
        }
        if (reporters == null) {
            return;
        }
        for (Reporter reporter : reporters) {
            reporter.report(getBucketMetricInfo(bucket));
        }
    }

    //获取桶中的Metrics
    List<MetricInfo> getBucketMetricInfo(Bucket bucket) {
        ArrayList<MetricInfo> result = new ArrayList<>();
        if (bucket == null || bucket.getDatas() == null || bucket.getDatas().size() == 0)
            return result;
        for (Map.Entry<String, MetricInfo> entry : bucket.getDatas().entrySet()) {
            entry.getValue().setTimestamp(bucket.getStartTickets());
            result.add(entry.getValue());
        }
        return result;
    }
}
