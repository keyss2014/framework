package cn.keyss.server.bpm;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

public class WorkflowService {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    IdentityService identityService;

    public ProcessInstance startProcess(String process, String businessKey, Map<String, Object> paras) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        identityService.setAuthenticatedUserId(userName);
        return runtimeService.startProcessInstanceByKey(process, businessKey, paras);
    }

    public void submitTask(String taskId, Map<String, Object> paras) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        identityService.setAuthenticatedUserId(userName);
        taskService.complete(taskId, paras);
    }
}
