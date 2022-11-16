package activiti.springbootdemo;

import com.crowx.RuoYiApplication;
import com.google.common.collect.ImmutableBiMap;
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
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RuoYiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class ActivitiCountersignTest {
    @Autowired
    private ProcessEngine processEngine;

    String reProcKey = "countersign";


    /**
     * 1、部署文件，定义流程
     */
    @Test
    public void testResp() {
        //使用classpath下的activiti.cfg.xml中的配置创建processEngine
//		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(processEngine);

//        2、得到RepositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        3、使用RepositoryService进行部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("demo/countersign.bpmn") // 添加bpmn资源
                .name("会签流程")
                .deploy();

//        4、输出部署信息
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
        System.out.println("流程部署名称：" + deployment.getKey());
    }




    /**
     * 2。 启动流程（如发起请假，发起调休）
     */
    @Test
    public void runProcessInstance() {
        testResp();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        /**
         *
         * 发起流程实例
         * 参数1： 流程定义的key
         * 参数2： 业务数据
         */
        ProcessInstance exection =  runtimeService.startProcessInstanceByKey(
                reProcKey,
                "10003",
//               参数
                ImmutableBiMap.of("userNameList",
                        Arrays.asList("zhangsan","lisi","wangwu","zhaoliu","tianqi"))
        );



        // 完成zhangsan zhaoliu tianqi到任务后，该流程节点结束
        TaskService taskService = processEngine.getTaskService();
        // 参数： 任务id
        Arrays.asList("zhangsan","zhaoliu","tianqi")
                .forEach(username->{
                    taskService.complete(
                            taskService.createTaskQuery()
                                    .processDefinitionKey(reProcKey)
                                    .taskAssignee(username)
                                    .processInstanceBusinessKey("10003")
                                    .singleResult()
                                    .getId()
                    );
                });

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
                .taskAssignee("zhangsan")
                .list();

        taskList.forEach(task->{
            System.out.println("流程实例Id："+task.getProcessInstanceId());
            System.out.println("任务id："+task.getId());
            System.out.println("任务负责人："+task.getAssignee());
            System.out.println("任务名称："+task.getName());
            System.out.println("流程变量:"+taskService.getVariables(task.getId()));

        });
        System.out.println("====================");



    }
    /**
     * 4。 完成个人惹怒
     */
    @Test
    public void finishMyTask() {
//        runProcessInstance();
        TaskService taskService = processEngine.getTaskService();
        // 参数： 任务id
        Arrays.asList("zhangsan","zhaoliu","tianqi")
                .forEach(username->{
                    taskService.complete(
                            taskService.createTaskQuery()
                                    .processDefinitionKey(reProcKey)
                                    .taskAssignee(username)
                                    .processInstanceBusinessKey("10003")
                                    .singleResult()
                                    .getId()
                    );
                });

/*
        Arrays.asList("zhaoliu")
                .forEach(username->{
                    taskService.complete(
                            taskService.createTaskQuery()
                                    .processDefinitionKey(reProcKey)
                                    .taskAssignee(username)
                                    .singleResult()
                                    .getId()
                    );
                });

        this.selectMyTast();
*/



    }
}
