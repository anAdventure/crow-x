package activiti.activitiBasic;

import com.crowx.RuoYiApplication;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
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
public class ActivitiReTest {


    @Autowired
    private ProcessEngine processEngine;
    @Test
    public void testCreateDbTable() {
        //使用classpath下的activiti.cfg.xml中的配置创建processEngine
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		System.out.println(processEngine);

        RepositoryService repositoryService = processEngine.getRepositoryService();
        TaskService taskService = processEngine.getTaskService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        HistoryService historyService = processEngine.getHistoryService();
    }


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
                .addClasspathResource("bpmn/evection.bpmn") // 添加bpmn资源
                .addClasspathResource("bpmn/evection.png")  // 添加png资源
                .name("出差申请流程")
                .deploy();

//        4、输出部署信息
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
        System.out.println("流程部署名称：" + deployment.getKey());
    }

    /**
     * 1.2、部署zip文件，定义流程
     */
    @Test
    public void testRespZip() {
        //使用classpath下的activiti.cfg.xml中的配置创建processEngine
//		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		System.out.println(processEngine);

//        2、得到RepositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        3、使用RepositoryService进行部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/countersign.bpmn20.xml") // 添加bpmn资源
//                .addClasspathResource("bpmn/evection.png")  // 添加png资源
                .name("会签流程部署")
                .deploy();

//        4、输出部署信息
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
    }
    /**
     * 查询流程定义信息
     */
    @Test
    public void selectProcessDefinition() {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("evection")
                .list();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
//                .processDefinitionKey()
//                .processDefinitionVersion(1)
                .orderByProcessDefinitionVersion()
                .desc()
                .list();



        TaskService taskService = processEngine.getTaskService();
// 根据流程定义的key和任务负责人查询任务
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("evection")
                .processInstanceId("流程实例Id")
                .taskAssignee("张三")

                .list();

        list.forEach(processDefinition -> {
            System.out.println("流程定义的id：" + processDefinition.getId());
            System.out.println("流程定义的版本：" + processDefinition.getVersion());
            System.out.println("流程定义的名称：" + processDefinition.getName());
            System.out.println("流程定义的key：" + processDefinition.getKey());
            System.out.println("流程部署id：" + processDefinition.getDeploymentId());
            System.out.println("-----------------------------------");
/*
            流程定义id：evection:1:a6484f75-d713-11ec-ba0f-5ece98684d55
            流程版本：1
            流程名称：出差申请单
            流程名称key：evection
*/
        });
    }

    /**
     * 删除流程定义信息
     */
    @Test
    public void deleteProcessDefinition() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.deleteDeployment("bea43c0b-d737-11ec-bbed-fe3eafa9b927",true);
        repositoryService.deleteDeployment("f3238953-d73a-11ec-9017-fe3eafa9b927",true);

        //设置true 级联删除流程定义，即使该流程有流程实例启动也可以删除，设置为false非级别删除方式，如果流程
        //repositoryService.deleteDeployment(deploymentId, true);
    }


}
