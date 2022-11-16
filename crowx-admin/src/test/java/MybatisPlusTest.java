
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.crowx.RuoYiApplication;
import com.ruoyi.test.domain.TUser;
import com.ruoyi.test.mapper.TUserMapper;
import io.vavr.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 类 名: StoreSalesTest
 * 描 述:
 * 作 者: 谢子豪
 * 创 建：2021年10月22日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RuoYiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class MybatisPlusTest {
    @Autowired
    private TUserMapper tUserMapper;

    @Test
    public void test(){
        tUserMapper.selectList(
                Wrappers.<TUser>lambdaQuery()
                .eq(TUser::getUsername,"zhangsan")
        ).forEach(System.out::println);
   }

   @Test
    public void test2(){
       List<TUser> users = tUserMapper.selectList(null);

//      获取8点-10点，10点-12点的用户创建数量
       Map<String, Long> result = IntStream.rangeClosed(8, 18).filter(s -> s % 2 == 0)
               .mapToObj(number -> {
                   LocalTime startTime = LocalTime.of(number, 0);
                   LocalTime endTime = startTime.plusHours(2L);
                   return Tuple.of(startTime, endTime);
               }).collect(Collectors.toMap(s -> s._1() + "-" + s._2(), s -> {
                   return users.stream().filter(item -> {
                       LocalTime time = item.getCreateTime().toLocalTime();
                       return time.isBefore(s._2()) && time.isAfter(s._1());
                   }).count();
               }));

       System.out.println(result);
   }


}
