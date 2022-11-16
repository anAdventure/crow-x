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
import java.util.Map;
import java.util.Optional;

/**
 * 设置参数
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RuoYiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class ActivitiBasicDemo3 {

    @Autowired
    private ProcessEngine processEngine;




    /**
     * 1、部署文件，定义流程
     */
    public void deploy(String classpathResource,String name) {

//      得到RepositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();
//      使用RepositoryService进行部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(classpathResource) // 添加bpmn资源
                .name(name)
                .deploy();

//        4、输出部署信息
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
    }

    /**
     * 2.创建流程实例
     */
    public void createProcessInstance(String reProcKey, Map<String,String> params) {

//        RuntimeService runtimeService = processEngine.getRuntimeService();
//        // 流程定义的key
//        ProcessInstance exection = runtimeService.
//                startProcessInstanceByKey(reProcKey,params);
//
//
//        System.out.println("流程定义id: " + exection.getProcessDefinitionId());
//        System.out.println("流程实例id: "+ exection.getId());
//        System.out.println("当前活动id"+exection.getActivityId());


    }


    public void selectAndFinishMyTask(String reProcKey,String name){

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

    }

    public void finishMyTask() {
        TaskService taskService = processEngine.getTaskService();
        // 参数： 「查询个人代办任务」返回的任务id
        taskService.complete("123123123");
    }


    @Test
    public void main(){
        String key ="evection";

        // 1. 流程部署 RepositoryService
        deploy("demo/evection3.bpmn","自定义流程传参测试");

        System.out.println("==流程部署成功\n\n");

        // 2. 创建流程实例 RuntimeService
        createProcessInstance(key,null);

        System.out.println("==创建流程实例成功\n\n");

        // 3. 审批 TaskService
        // a.查询个人代办任务 b.办理任务
        System.out.println("==查询并且完成个人代办任务");
        Arrays.asList("张三","李四","王五").forEach(name->{
            selectAndFinishMyTask(key,name);
        });

        System.out.println("流程结束");

    }

    @Test
    public void test2(){
        String key ="evection";

        Arrays.asList("张三","李四","王五").forEach(name->{
            selectAndFinishMyTask(key,name);
        });

    }
}
