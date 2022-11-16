package activiti.activitiBasic;

import com.crowx.RuoYiApplication;
import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RuoYiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class ActivitiRunTest {


    @Autowired
    private ProcessEngine processEngine;

    String reProcKey = "evection";


    /**
     * 2。 启动流程（如发起请假，发起调休）
     */
    @Test
    public void testResp() {

        RuntimeService runtimeService = processEngine.getRuntimeService();

        /**
         * 参数1： 流程定义的key
         * 参数2： 业务数据
         */
        ProcessInstance exection = runtimeService.startProcessInstanceByKey("evection","业务key");
/*
        runtimeService.startProcessInstanceByKey(
                "myEvectuibUel",
                "10002",
                ImmutableBiMap.of("jingli", "jack,xiezihao")
        );
        */
        System.out.println("流程定义id: " + exection.getProcessDefinitionId());
        // 新增任务关联流程实例id
        System.out.println("流程实例id: "+ exection.getId());
        System.out.println("当前活动id"+exection.getActivityId());
/*

        1
        流程定义id: evection:1:a6484f75-d713-11ec-ba0f-5ece98684d55
        流程实例id: de3b95f9-d713-11ec-ac02-5ece98684d55
*/

    }
    /**
     * 3。 查询个人待办任务
     */
    @Test
    public void selectMyTast() {
        TaskService taskService = processEngine.getTaskService();
        // 根据流程待key和任务负责人查询任务

        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey(reProcKey)
                .taskAssignee("张三")
                .list();

        taskList.forEach(task->{
            System.out.println("流程实例Id："+task.getProcessInstanceId());
            System.out.println("任务id："+task.getId());
            System.out.println("任务负责人："+task.getAssignee());
            System.out.println("任务名称："+task.getName());
        });
        System.out.println(taskList);

        RuntimeService runtimeService = processEngine.getRuntimeService();
//        ProcessInstance exection = runtimeService.startProcessInstanceByKey(reProcKey);
        System.out.println("查询关联业务id");
        runtimeService.createProcessInstanceQuery()
                .processDefinitionIds(
                        taskList.stream().map(s->s.getProcessDefinitionId()).collect(Collectors.toSet())
                ).list().forEach(s->{
            ProcessInstance s1 = s;
            // todo 可以关联业务表 输出业务类型？请假、调休...?
            System.out.println(s.getProcessInstanceId()+"__"+s.getBusinessKey());
        });
/*

        流程实例Id：de3b95f9-d713-11ec-ac02-5ece98684d55
        任务id：de6516fd-d713-11ec-ac02-5ece98684d55
        任务负责人：张三
        任务名称：创建出差申请
        流程实例Id：fa534cad-d719-11ec-b1b6-5ece98684d55
        任务id：fa7e7b61-d719-11ec-b1b6-5ece98684d55
        任务负责人：张三
        任务名称：创建出差申请
        [Task[id=de6516fd-d713-11ec-ac02-5ece98684d55, name=创建出差申请], Task[id=fa7e7b61-d719-11ec-b1b6-5ece98684d55, name=创建出差申请]]
*/

    }
    /**
     * 4。 完成个人惹怒
     */
    @Test
    public void finishMyTask() {

        TaskService taskService = processEngine.getTaskService();
        // 参数： 任务id
        taskService.complete(
                taskService.createTaskQuery()
                        .processDefinitionKey(reProcKey)
                        .taskAssignee("张三")
                        .singleResult()
                        .getId()
        );

        this.selectMyTast();
// 张三 jerry jack rols


/*

        流程实例Id：de3b95f9-d713-11ec-ac02-5ece98684d55
        任务id：de6516fd-d713-11ec-ac02-5ece98684d55
        任务负责人：张三
        任务名称：创建出差申请
        流程实例Id：fa534cad-d719-11ec-b1b6-5ece98684d55
        任务id：fa7e7b61-d719-11ec-b1b6-5ece98684d55
        任务负责人：张三
        任务名称：创建出差申请
        [Task[id=de6516fd-d713-11ec-ac02-5ece98684d55, name=创建出差申请], Task[id=fa7e7b61-d719-11ec-b1b6-5ece98684d55, name=创建出差申请]]
*/


    }


    @Test
    public void addTaskComment(){
        TaskService taskService = processEngine.getTaskService();

        // 根据流程待key和任务负责人查询任务
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey(reProcKey)
                .taskAssignee("张三")
                .list();

        taskList.forEach(task->{
            System.out.println("流程实例Id："+task.getProcessInstanceId());
            System.out.println("任务id："+task.getId());
            System.out.println("任务负责人："+task.getAssignee());
            System.out.println("任务名称："+task.getName());
            // 添加任务审批意见
            taskService.addComment(task.getId(),task.getProcessInstanceId(),"同意q");
            System.out.println("0000");
        });

        taskList.forEach(task->{
            // 查询任务审批意见
            List<Comment> taskComments = taskService.getTaskComments(task.getId());
            for (Comment taskComment : taskComments) {
                System.out.println(taskComment);
                System.out.println(taskComment.getUserId());
                System.out.println(taskComment.getFullMessage());

            }
            System.out.println("--------");

        });
        System.out.println(taskList);
    }
}
