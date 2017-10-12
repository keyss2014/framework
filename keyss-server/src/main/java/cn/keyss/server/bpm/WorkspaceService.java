package cn.keyss.server.bpm;

import cn.keyss.common.query.QueryCriteria;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class WorkspaceService {

    @Autowired
    IdentityService identityService;

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;

    public List<TaskInfo> queryTask(QueryCriteria queryCriteria) {
        return null;
    }
    public void completeTask(String taskId, Map<String,Object> datas){
        //taskService.deleteTask();
    }
}
