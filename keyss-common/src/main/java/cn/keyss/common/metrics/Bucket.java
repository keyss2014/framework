package cn.keyss.common.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 桶
 */
public class Bucket {

    //启始时间
    private long startTickets;
    //结束时间
    private long stopTickets;

    private Bucket(){}

    //简单聚合后的数据
    HashMap<String, MetricInfo> datas = new HashMap<>();
    private Object writeLocker = new Object();
    private static Logger logger = LoggerFactory.getLogger(Bucket.class);

    public long getStartTickets() {
        return startTickets;
    }

    public long getStopTickets() {
        return stopTickets;
    }

    /**
     * 重设桶，重置后的时间区间[startTickets,stopTickets)
     * @param startTickets
     * @param stopTickets
     */
    public void reset(long startTickets, long stopTickets) {
        this.startTickets = startTickets;
        this.stopTickets = stopTickets;
        this.datas.clear();
    }

    /**
     * 桶是否可写
     *
     * @param currentTickets
     * @return
     */
    public boolean canWrite(long currentTickets) {
        return currentTickets >= startTickets && currentTickets < stopTickets;
    }

    /**
     * 推入数据
     *
     * @param info
     */
    public void writeData(MetricInfo info) {
        synchronized (writeLocker){
            info.mergeMetrics(datas.get(info.getKey()));
            datas.put(info.getKey(),info);
        }
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return String.format("[Bucket:startTickets:{},stopTickets:{},dataSize:{}]",format.format(new Date(startTickets)),format.format(new Date(stopTickets)),datas.size());
    }

    public static Bucket createBucket(){
        return new Bucket();
    }

    public HashMap<String,MetricInfo> getDatas() {
        return datas;
    }
}
