import com.crowx.common.core.processDataFlow.build.impl.AnnotationExecDataBuilder;
import com.crowx.common.core.processDataFlow.core.ExecDataContext;
import com.crowx.common.core.processDataFlow.domain.FileUploadDataDTO;
import com.crowx.common.core.processDataFlow.exception.ExcelImportException;
import com.crowx.common.core.processDataFlow.handler.impl.DictConvertHandler;
import com.google.common.collect.Lists;
import com.crowx.RuoYiApplication;
import org.activiti.engine.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import testDomain.TestUser;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RuoYiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class TestDataBuilder {

    @Test
    public void test() throws ExcelImportException {
        FileUploadDataDTO fileUploadDataDTO = new FileUploadDataDTO();
        fileUploadDataDTO.setFilePath("/Users/xiezihao/Downloads/1650525046048用户数据.xlsx");
        fileUploadDataDTO.setFileName("谢子豪——打车明细");

        ArrayList<TestUser> testUsers = Lists.newArrayList();

        List<TestUser> userList = ExecDataContext.builder(TestUser.class)
                // 数据加载器 如何获取数据？ 通过文件导入、或者自定义list
                // 字段加载器 如何加载字段信息？
                .fieldDefinitionBuilder(new AnnotationExecDataBuilder<>(TestUser.class))
                // 数据处理器 字典，异步，
                // 数据校验器
                .addListHandler(new DictConvertHandler<>())
                .addListHandler(list -> {
                    System.out.println("执行处理器222");
                    return list;
                })

                // 添加监听器，删除文件、保存数据
                .defaultExecFlow(fileUploadDataDTO).executeFlow();

        System.out.println(userList);
    }


    @Autowired
    private ProcessEngine processEngine;
    @Test
    public void testCreateDbTable() {
        //使用classpath下的activiti.cfg.xml中的配置创建processEngine
//		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		System.out.println(processEngine);

        RepositoryService repositoryService = processEngine.getRepositoryService();
        TaskService taskService = processEngine.getTaskService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        HistoryService historyService = processEngine.getHistoryService();
    }
}
