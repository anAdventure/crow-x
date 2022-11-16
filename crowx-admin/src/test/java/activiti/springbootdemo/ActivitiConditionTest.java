package activiti.springbootdemo;

import com.crowx.RuoYiApplication;
import com.google.common.collect.ImmutableMap;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RuoYiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class ActivitiConditionTest {

    String reProcKey = "evectionClaim";

    @Autowired
    private ProcessEngine processEngine;



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
                .addClasspathResource("bpmn/evectionClaim.bpmn") // 添加bpmn资源

                .name("出差申请流程-"+reProcKey)
                .deploy();

//        4、输出部署信息
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
    }

    @Autowired
    private ProcessRuntime processRuntime;

    @Test
    public void runTask(){

        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey(
                reProcKey,
                "10005",
                ImmutableMap.of("days",4)
        );

    }

    @Test
    public void finishTask(){
//        this.testResp();
//        runTask();

        TaskService taskService = processEngine.getTaskService();
        Arrays.asList("zhangsan")
                .forEach(username->{
                    taskService.complete(
                    taskService.createTaskQuery()
                            .processDefinitionKey(reProcKey)
                            .processInstanceBusinessKey("10005")
                            .taskAssignee(username)
                            .singleResult()
                            .getId());
                    System.out.println(username+"审批通过");
                });

    }

}
