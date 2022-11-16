package activiti.activitiGroup;

import com.crowx.RuoYiApplication;
import org.activiti.engine.*;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RuoYiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class GroupTest {


    String reProcKey = "evectionClaim";

    @Autowired
    private ProcessEngine processEngine;


    /**
     * 1. 查询任务
     * 2. 拾取任务、归还任务
     * 3. 查询当前任务、办理任务
     */
    @Test
    public void claimTask(){

        RuntimeService runtimeService = processEngine.getRuntimeService();
        String s = "c0b1e385-d77d-11ec-88d0-debd590ac538";
        HistoryService historyService = processEngine.getHistoryService();
    runtimeService.deleteProcessInstance(s,"");
    historyService.deleteHistoricProcessInstance(s);//(顺序不能换)
    }
    @Test
    public void findGroupTaskList() {

        // 创建TaskService
        TaskService taskService = processEngine.getTaskService();
        //查询组任务
        List<Task> list = taskService.createTaskQuery()
//                .processDefinitionKey(reProcKey)
//                .processInstanceBusinessKey("10005")
                .taskCandidateOrAssigned("guanyu")//根据候选人查询
                .list();
        for (Task task : list) {
            System.out.println("----------------------------");
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }



}
