package cn.keyss.common.metrics.reporter;

import cn.keyss.common.metrics.MetricInfo;
import cn.keyss.common.metrics.Reporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class KairosdbReporter implements Reporter {


    private static final Logger logger = LoggerFactory.getLogger(KairosdbReporter.class);
    private static final int pkgMaxSize = 150;//单包可承受最大大小,每条Entity会拆分为两条metrics
    private String serverUrl;

    public KairosdbReporter(){}
    public KairosdbReporter(String serverUrl){
        this.serverUrl = serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public void report(List<MetricInfo> metricInfos) {
        logger.trace("收到数据提交请求:Size:{}",metricInfos.size());
        //如果单包过大，进行切分
        if (metricInfos.size() > pkgMaxSize) {
            int p_cnt = (int) Math.ceil((float) metricInfos.size() / pkgMaxSize);
            for (int p_index = p_cnt - 1; p_index >= 0; p_index--) {
                int index_from = p_index * pkgMaxSize;
                int index_to = (p_index + 1) * pkgMaxSize;
                if (index_to > metricInfos.size())
                    index_to = metricInfos.size();
                reportToKairosdb(metricInfos.subList(index_from, index_to));
                metricInfos.subList(index_from, index_to).clear();
            }
        } else {
            reportToKairosdb(metricInfos);
            metricInfos.clear();
        }
    }

    void reportToKairosdb(List<MetricInfo> sourceMetricEntities){
        if (sourceMetricEntities==null || sourceMetricEntities.size() == 0)
            return;
        final ArrayList<MetricInfo> metricEntities = new ArrayList<>();
        for (MetricInfo metricEntity : sourceMetricEntities){
            metricEntities.addAll(metricEntity.splitEntityToValue());
        }
        try {
//            HttpRequestEntity httpRequestEntity = HttpRequestEntity.json(this.serverUrl, metricEntities);
//            asyncHttpClient.post(httpRequestEntity, new CallBack(metricEntities.size()));
        } catch (Exception e) {
            LoggerFactory.getLogger(KairosdbReporter.class).error("report失败",e);
        }finally {
//
        }
    }
//    private class CallBack extends STFutureCallBack<String> {
//        private int metricEntities;
//        public CallBack(int metricEntities) {
//            this.metricEntities = metricEntities;
//        }
//
//        @Override
//        public void completed(String result) {
//            logger.trace("Sync 提交数据:{}条，StatusCode：{}", metricEntities, result);
//        }
//
//        @Override
//        public void failed(Exception ex) {
//            LoggerFactory.getLogger(KairosdbReporter.class).error("report失败",ex);
//        }
//    }
}

