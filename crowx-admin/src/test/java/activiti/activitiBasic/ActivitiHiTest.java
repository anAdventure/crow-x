package activiti.activitiBasic;

import com.crowx.RuoYiApplication;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
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
public class ActivitiHiTest {


    @Autowired
    private ProcessEngine processEngine;

    String reProcKey = "evection";

    /**
     * 查询历史实例id
     */
    @Test
    public void selectHistoryList (){
        HistoryService historyService = processEngine.getHistoryService();

        HistoricActivityInstanceQuery instanceQuery = historyService
                .createHistoricActivityInstanceQuery();

        List<HistoricActivityInstance> list = instanceQuery
                .processInstanceId("c38ceafd-d73a-11ec-849f-fe3eafa9b927")
                .orderByHistoricActivityInstanceStartTime().asc()
                .list();

        list.forEach(item->{
            System.out.println("实例id: " + item.getProcessInstanceId());
            System.out.println("定义id: " + item.getProcessDefinitionId());
            System.out.println("活动name: " + item.getActivityName());
            System.out.println("活动id: " + item.getActivityId());
            System.out.println("开始时间: " + item.getStartTime());
            System.out.println("结束数据: " + item.getEndTime());
            System.out.println();
            System.out.println(">=====================<");
        });
    }
}
