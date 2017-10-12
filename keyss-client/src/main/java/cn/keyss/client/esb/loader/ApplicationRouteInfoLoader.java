package cn.keyss.client.esb.loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import cn.keyss.client.esb.EsbException;
import cn.keyss.client.esb.contract.EsbService;
import cn.keyss.client.esb.contract.datacontract.Event;
import cn.keyss.client.esb.contract.datacontract.QueryApplicationEventsRequest;
import cn.keyss.client.esb.contract.datacontract.QueryApplicationEventsResponse;
import cn.keyss.client.esb.contract.datacontract.QueryApplicationServicesRequest;
import cn.keyss.client.esb.contract.datacontract.QueryApplicationServicesResponse;
import cn.keyss.client.esb.contract.datacontract.Service;
import cn.keyss.common.rpc.RpcProxy;
/**
 * 应用信息加载器
 */
public class ApplicationRouteInfoLoader {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ApplicationRouteInfoLoader.class);

    private int application;

    /**
     * esb服务
     */
    private RpcProxy<EsbService> esbService;

    public ApplicationRouteInfoLoader(int application, String esbServerUrl) {
        this.application = application;
        if (StringUtils.isEmpty(application) || StringUtils.isEmpty(esbServerUrl)) {
            logger.warn("ESB CLIENT未启动");
            return;
        }
        this.esbService = new RpcProxy<EsbService>(EsbService.class, esbServerUrl, new RestTemplate());
        this.startSchedule();
        isRunning = true;
    }

    /**
     * 缓存的服务信息
     */
    private List<Service> serviceInfos;

    /**
     * 缓存的事件信息
     */
    private List<Event> eventInfos;

    /**
     * 正在运行
     */
    private boolean isRunning = false;

    /**
     * 读写锁
     */
    private ReentrantReadWriteLock syncLock = new ReentrantReadWriteLock();

    /**
     * 刷新处理器
     */
    private ScheduledExecutorService executorService;

    /**
     * 开始调度
     */
    private void startSchedule() {
        loadEsbService();
        if (serviceInfos != null) {
            for (Service service : serviceInfos) {
                logger.info("ESB LOAD:{}", service.toString());
            }
        }
        // loadEsbEvent();
        if (isRunning == false) {
            Runnable runnable = new Runnable() {
                public void run() {
                    loadEsbService();
                    // loadEsbEvent();
                }
            };
            executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = Executors.defaultThreadFactory().newThread(r);
                    t.setDaemon(true);
                    return t;
                }
            });
            executorService.scheduleAtFixedRate(runnable, 10, 10, TimeUnit.MINUTES);
            isRunning = true;
        }
    }

    public List<Service>  getAllServices() {
        return serviceInfos;
    }

    /**
     * 加载服务信息
     */
    private void loadEsbService() {
        logger.trace(String.format("Load Esb Service Start Thread:%s [%s]", Thread.currentThread().getName(),
                Thread.currentThread().getId()));

        QueryApplicationServicesRequest request = new QueryApplicationServicesRequest();
        request.setApplication(application);
        request.setCallSource("shitou.esb.client");

        try {

            QueryApplicationServicesResponse response = this.esbService.getTransparentProxy()
                    .queryApplicationServices(request);
            if (  response.getResultCode()!= 0)
                throw new EsbException(String.format("%s", response.getResultCode(), response.getResultMessage()));
            if (response.getServices() == null)
                response.setServices(new ArrayList<>());
            try {
                syncLock.writeLock().lock();
                serviceInfos = response.getServices();
            } finally {
                syncLock.writeLock().unlock();
            }

        } catch (Throwable e) {
            logger.error("访问企业总线异常！" + e.getMessage());
        }

        try {
            syncLock.readLock().lock();
            logger.trace(String.format("Load Esb Services - DownLoad List Finish,TotalCnt:%s",
                    this.serviceInfos == null ? 0 : this.serviceInfos.size()));
        } finally {
            syncLock.readLock().unlock();
        }
    }

    /**
     * 加载事件信息
     */
    private void loadEsbEvent() {
        logger.trace(String.format("Load Esb Event Start Thread:%s [%s]", Thread.currentThread().getName(),
                Thread.currentThread().getId()));

        QueryApplicationEventsRequest request = new QueryApplicationEventsRequest();
        request.setApplication(application);
        request.setCallSource("shitou.client.esb");

        try {
            QueryApplicationEventsResponse response = this.esbService.getTransparentProxy()
                    .queryApplicationEvents(request);
            if (  response.getResultCode()!= 0)
                throw new EsbException(String.format("%s", response.getResultCode(), response.getResultMessage()));
            if (response.getEvents() == null)
                response.setEvents(new ArrayList<>());
            try {
                syncLock.writeLock().lock();
                eventInfos = response.getEvents();
            } finally {
                syncLock.writeLock().unlock();
            }

        } catch (Exception e) {
            logger.error("访问企业总线异常！" + e.getMessage());
        }

        try {
            syncLock.readLock().lock();
            logger.trace(String.format("Load Esb Event - DownLoad List Finish,TotalCnt:%s",
                    this.serviceInfos == null ? 0 : this.serviceInfos.size()));
        } finally {
            syncLock.readLock().unlock();
        }
    }

    /**
     * Contains Function：标签比对.
     *
     * @param srcTags
     *            源标签
     * @param dstTags
     *            比对标签
     * @return 源标签是否包含目标标签
     * @author Alex Lee
     */
    private boolean contains(List<String> srcTags, List<String> dstTags) {
        if (dstTags == null || dstTags.size() == 0)
            return true;

        if (srcTags == null || srcTags.size() == 0)
            return false;

        for (String t : dstTags) {
            if (!srcTags.contains(t)) {
                return false;
            }
        }
        return true;
    }

    /**
     * FindService Function：查找服务.
     *
     * @param tags
     */
    public Service findService(Class clazz, List<String> tags) {
        Service result = null;
        try {
            syncLock.readLock().lock();
            if (serviceInfos == null) {
                throw new EsbException("企业服务总线服务未就绪");
            }
            // 根据全类名找到对应的服务 可能需要用到注解
            for (Service service : serviceInfos) {
                // 如果contract 和 注解匹配且...
                if (service.getContract().equals(clazz.getName()) && contains(service.getTags(), tags)) {
                    result = service;
                    return result;
                }
            }
        } finally {
            syncLock.readLock().unlock();
        }
        return result;
    }

    /**
     * 查找Event
     *
     * @param clazz
     * @return Event
     */
    public Event findEvent(Class clazz) {
        Event result = null;
        try {
            syncLock.readLock().lock();
            if (eventInfos == null) {
                throw new EsbException("企业服务总线服务未就绪");
            }
            for (Event event : eventInfos) {
                if (event.getContract().equals(clazz.getName())) {
                    result = event;
                    return result;
                }
            }
        } finally {
            syncLock.readLock().unlock();
        }
        return result;
    }
}
