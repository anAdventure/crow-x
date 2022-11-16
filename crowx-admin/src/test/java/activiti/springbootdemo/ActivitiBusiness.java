package activiti.springbootdemo;

import com.crowx.RuoYiApplication;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;
import io.netty.util.concurrent.FastThreadLocal;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
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
public class ActivitiBusiness {

    String reProcKey = "evection";

    @Autowired
    private ProcessEngine processEngine;

    /**
     * 启动流程实例并且绑定业务key
     */
    @Test
    public void runAndBindBusinessKey() {

/*        ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                .start()
                .withProcessDefinitionKey("leave")
                .withName(workflowLeave.getTitle())
                .withBusinessKey(id)
//                .withVariable("deptLeader",join)
                .build());*/

        RuntimeService runtimeService = processEngine.getRuntimeService();

        ProcessInstance exection = runtimeService.startProcessInstanceByKey(reProcKey,"10001");
        System.out.println("流程定义id: " + exection.getProcessDefinitionId());
        // 新增任务关联流程实例id
        System.out.println("流程实例id: "+ exection.getId());
        System.out.println("当前活动id:"+exection.getActivityId());
        System.out.println("关联业务的key："+exection.getBusinessKey());
    }


    /**
     * 单个流程实例挂起与激活
     */
    @Test
    public void SuspendSingleProcessInstance(){
//        RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        查询流程定义的对象
        ProcessInstance processInstance = runtimeService.
                createProcessInstanceQuery().
                processInstanceId("15001").
                singleResult();
//        得到当前流程定义的实例是否都为暂停状态
        boolean suspended = processInstance.isSuspended();
//        流程定义id
        String processDefinitionId = processInstance.getId();
//        判断是否为暂停
        if(suspended){
//         如果是暂停，可以执行激活操作 ,参数：流程定义id
            runtimeService.activateProcessInstanceById(processDefinitionId);
            System.out.println("流程定义："+processDefinitionId+",已激活");
        }else{
//          如果是激活状态，可以暂停，参数：流程定义id
            runtimeService.suspendProcessInstanceById( processDefinitionId);
            System.out.println("流程定义："+processDefinitionId+",已挂起");
        }

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
                .addClasspathResource("bpmn/diagram2.bpmn") // 添加bpmn资源
                .name("出差申请流程UEL222")


                .deploy();

//        4、输出部署信息
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
    }

    @Autowired
    private ProcessRuntime processRuntime;

    @Test
    public void runTask(){
        this.testResp();
/*
        t
        org.activiti.api.process.model.ProcessInstance instance = processRuntime.start(ProcessPayloadBuilder
                .start()
                .withProcessDefinitionKey("myEvectuibUel")
                // todo 业务name？
                .withName("张三的请假流程")
                // 指定业务的key
                .withBusinessKey("10001")
                // todo 自定义变量？
                .withVariable("jingli", "jack,xiezihao")
                .build());*/

        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey(
                    "myEvectuibUel2",
                    "10002"
                );

    }


    @Test
    public void runTaskBindBusinesskey(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey(
                "evection",
                "10002"
        );



    }
}
