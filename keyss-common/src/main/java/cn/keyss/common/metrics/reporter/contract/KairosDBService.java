package cn.keyss.common.metrics.reporter.contract;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by wangkun on 2016/1/23.
 */
@WebService(serviceName = "ppdai.kairosdb.KairosDBService")
public interface KairosDBService {

    @WebMethod(operationName = "datapoints")
    public void postDataPoints(DataPoint[] dataPoints);
}
