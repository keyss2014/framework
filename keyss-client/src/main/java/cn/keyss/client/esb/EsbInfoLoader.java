package cn.keyss.client.esb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import cn.keyss.client.esb.contract.EsbService;
import cn.keyss.client.esb.contract.datacontract.Event;
import cn.keyss.client.esb.contract.datacontract.QueryApplicationEventsRequest;
import cn.keyss.client.esb.contract.datacontract.QueryApplicationEventsResponse;
import cn.keyss.client.esb.contract.datacontract.QueryApplicationServicesRequest;
import cn.keyss.client.esb.contract.datacontract.QueryApplicationServicesResponse;
import cn.keyss.client.esb.contract.datacontract.Service;
import cn.keyss.common.rpc.RpcProxy;

/**
 * 服务总线信息加载器
 */
public class EsbInfoLoader {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(EsbInfoLoader.class);

    /***
     * 应用
     */
    private int application;

    /**
     * esb服务代理
     */
    private RpcProxy<EsbService> esbService;

    /***
     * 构造器
     * @param application 应用
     * @param esbServerUrl 企业服务总线服务
     */
    public EsbInfoLoader(int application, String esbServerUrl) {
        this.application = application;
        if (StringUtils.isEmpty(application) || StringUtils.isEmpty(esbServerUrl)) {
            logger.warn("应用或ESB服务器地址未设置，ESB信息加载器未启动");
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
        //第一次同步启动加载
        loadEsbService();
        loadEsbEvent();
        if (isRunning == false) {
            Runnable runnable = new Runnable() {
                public void run() {
                    loadEsbService();
                    loadEsbEvent();
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
            //之后定时刷新
            executorService.scheduleAtFixedRate(runnable, 10, 10, TimeUnit.MINUTES);
            isRunning = true;
        }
    }

    /**
     * 加载服务信息
     */
    private void loadEsbService() {
        QueryApplicationServicesRequest request = new QueryApplicationServicesRequest();
        request.setApplication(application);
        request.setCallSource("keyss.client.esb");

        try {
            QueryApplicationServicesResponse response = this.esbService.getTransparentProxy()
                    .queryApplicationServices(request);
            if (response.getResultCode() != 0)
                throw new EsbException(String.format("%s", response.getResultCode(), response.getResultMessage()));
            if (response.getServices() == null)
                response.setServices(new ArrayList<>());

            try {
                syncLock.writeLock().lock();
                serviceInfos = response.getServices();
            } finally {
                syncLock.writeLock().unlock();
            }

            for (Service service : response.getServices()) {
                logger.info("加载ESB服务：{}", service.getContract());
            }
        } catch (Throwable e) {
            logger.error("访问企业总线异常！" + e.getMessage());
        }
    }

    /**
     * 加载事件信息
     */
    private void loadEsbEvent() {
        QueryApplicationEventsRequest request = new QueryApplicationEventsRequest();
        request.setApplication(application);
        request.setCallSource("keyss.client.esb");

        try {
            QueryApplicationEventsResponse response = this.esbService.getTransparentProxy()
                    .queryApplicationEvents(request);
            if (response.getResultCode() != 0)
                throw new EsbException(String.format("%s", response.getResultCode(), response.getResultMessage()));
            if (response.getEvents() == null)
                response.setEvents(new ArrayList<>());
            try {
                syncLock.writeLock().lock();
                eventInfos = response.getEvents();
            } finally {
                syncLock.writeLock().unlock();
            }

            for (Event event : response.getEvents()) {
                logger.info("加载ESB事件：{}", event.getContract());
            }

        } catch (Exception e) {
            logger.error("访问企业服务总线异常：" + e.getMessage());
        }
    }

    /***
     * 查找服务
     * @param clazz 契约
     * @param tags 源标签
     * @return 服务信息
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
                if (service.getContract().equals(clazz.getName()) && TagsHelper.contains(service.getTags(), tags)) {
                    result = service;
                    return result;
                }
            }
        } finally {
            syncLock.readLock().unlock();
        }
        return result;
    }


    /***
     * 查找事件
     * @param clazz 契约
     * @return 事件信息
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
