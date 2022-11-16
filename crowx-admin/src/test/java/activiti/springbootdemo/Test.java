package activiti.springbootdemo;

import com.crowx.RuoYiApplication;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RuoYiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class Test {

    @Autowired
    private ProcessRuntime processRuntime;
    @org.junit.Test
    public void addWorkFlow(){

    }
}
