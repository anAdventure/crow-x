
import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.crowx.common.utils.StringUtils;
import com.google.common.collect.HashMultimap;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import io.vavr.Tuple4;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZjsyBootDemoApplicationTests {

	String excelPath="/Users/xiezihao/Downloads/江宁新能源汽车经销商人员名单.xlsx";

	public static void main(String[] args) {
		new ZjsyBootDemoApplicationTests().contextLoads();
	}

	public void contextLoads() {
		try {

			ExcelReader excelReader = ExcelUtil.getReader(FileUtil.file(excelPath),0);
			int columnCount = excelReader.getColumnCount();
			int rowCount = excelReader.getRowCount();

			// key 经销商 value 人员信息
			HashMap<Tuple4<String, String, String,String>, List<Tuple3<String, String, String>>> map =new HashMap<>();

			for (int i = 3; i < rowCount; i++) {
				Row row = excelReader.getOrCreateRow(i);
				Tuple4<String, String, String,String> parentItem=null;
				for (int j = 0; j < columnCount; j++) {
					int parentEndIndex = 5;
					if(j== parentEndIndex){
						parentItem= Tuple.of(
								row.getCell(1).getStringCellValue()+"-"+row.getCell(2).getStringCellValue(),
								row.getCell(3).getStringCellValue(),
								row.getCell(4).getStringCellValue(),
								row.getCell(0).getStringCellValue()
						);
					}
					if(parentItem!=null  && (j- parentEndIndex)%3==0 &&j> parentEndIndex){
						int phoneIndex = j - 1;
						int idCardIndex = j - 2;
						int nameIndex = j - 3;

						String name = Optional.ofNullable(row.getCell(nameIndex)).map(Cell::getStringCellValue).orElse(null);
						String idCard = Optional.ofNullable(row.getCell(idCardIndex)).map(Cell::getStringCellValue).orElse(null);
						String phone = Optional.ofNullable(row.getCell(phoneIndex)).map(Cell::getStringCellValue).orElse(null);
						if(StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(idCard)){
							if(CollectionUtils.isEmpty(map.get(parentItem))){
								map.put(parentItem,new ArrayList<>());
							}
							map.get(parentItem).add(
									Tuple.of(
											name,
											idCard,
											phone
									)
							);
						}
					}
				}
			}
			excelReader.close();

//		AtomicReference<Long> id = new AtomicReference<>(63L);
			AtomicLong childrenId = new AtomicLong(7L);
			map.forEach((parentItem,childrenList)->{
				System.out.println(StringUtils.format(" insert into N_ENERGY_CAR_DEALER values ({},'{}','{}','{}','{}','{}');",
						parentItem._4,parentItem._1,parentItem._2,parentItem._3,1,0));
				childrenList.forEach(childrenItem->{
					System.out.println(StringUtils.format(" insert into N_ENERGY_CAR_DEALER_PERSONNEL(ID, NAME, IDCARD, TELEPHONE, DEALER_ID, ROLE) values({},'{}','{}','{}','{}','{}');",
							childrenId.getAndIncrement(),childrenItem._1,childrenItem._2,childrenItem._3, parentItem._4,2
					));
				});
//			id.getAndSet(id.get() + 1);
			});

		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
