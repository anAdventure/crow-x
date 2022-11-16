package activiti.demo;

import com.crowx.RuoYiApplication;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RuoYiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class ActivitiBasicDemo2 {

    @Autowired
    private ProcessEngine processEngine;

    String reProcKey ="evection";


    /**
     * 1、部署文件，定义流程
     */
    @Test
    public void testResp() {

//      得到RepositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();
//      使用RepositoryService进行部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("demo/evection.bpmn") // 添加bpmn资源
                .name("出差流程申请")
                .deploy();

//        4、输出部署信息
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
    }

    /**
     * 2.创建流程实例
     */
    @Test
    public void createProcessInstance() {

        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 流程定义的key
        ProcessInstance exection = runtimeService.startProcessInstanceByKey(reProcKey);


        System.out.println("流程定义id: " + exection.getProcessDefinitionId());
        System.out.println("流程实例id: "+ exection.getId());
        System.out.println("当前活动id"+exection.getActivityId());

    }
    public void createProcessInstance(String businessKey) {

        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 流程定义的key
        ProcessInstance exection = runtimeService.startProcessInstanceByKey(reProcKey,businessKey);


        System.out.println("流程定义id: " + exection.getProcessDefinitionId());
        System.out.println("流程实例id: "+ exection.getId());
        System.out.println("当前活动id"+exection.getActivityId());

    }


    @Test
    public void selectAndFinishMyTask(){
        Arrays.asList("张三","李四","王五").forEach(name->{

            TaskService taskService = processEngine.getTaskService();
            // 根据流程定义的key和任务负责人查询任务
            List<Task> taskList = taskService.createTaskQuery()
                    .processDefinitionKey(reProcKey)
                    .taskAssignee(Optional.ofNullable(name).orElse("张三"))
                    .list();

            taskList.forEach(task->{
                System.out.println("流程实例Id："+task.getProcessInstanceId());
                System.out.println("任务id："+task.getId());
                System.out.println("任务负责人："+task.getAssignee());
                System.out.println("任务名称："+task.getName());

                taskService.complete(task.getId());
                System.out.println(task.getAssignee()+"的任务已完成\n\n");

            });

        });
    }


    public void selectMyTask(String name){

            TaskService taskService = processEngine.getTaskService();
            // 根据流程定义的key和任务负责人查询任务
            List<Task> taskList = taskService.createTaskQuery()
                    .processDefinitionKey(reProcKey)
                    .processInstanceBusinessKey("10001")
                    .taskAssignee(Optional.ofNullable(name).orElse("张三"))
                    .list();

            taskList.forEach(task->{
                System.out.println("流程实例Id："+task.getProcessInstanceId());
                System.out.println("任务id："+task.getId());
                System.out.println("任务负责人："+task.getAssignee());
                System.out.println("任务名称："+task.getName());
                System.out.println();
                System.out.println();
//                taskService.complete(task.getId());
//                System.out.println(task.getAssignee()+"的任务已完成\n\n");

            });
    }

    @Test
    public void finishMyTask() {
        TaskService taskService = processEngine.getTaskService();
        // 参数： 「查询个人代办任务」返回的任务id
        taskService.complete("123123123");
    }


    @Test
    public void main(){
        // 2. 用户A 创建流程实例 RuntimeService
        createProcessInstance("10001");
        // 2. 用户B 创建流程实例 RuntimeService
        createProcessInstance("10002");
        System.out.println("==创建流程实例成功\n\n");

        // 3. 审批 TaskService
        // a.查询个人代办任务 b.办理任务
        System.out.println("==查询个人代办任务");
        selectMyTask("张三");

        System.out.println("流程结束");

    }
}
