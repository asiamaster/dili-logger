package com.dili.logger;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.dili.logger.domain.BusinessLog;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.logger.service.BusinessLogService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
/**  指定当前生效的配置文件( active profile)，如果是 appplication-dev.yml 则 dev   **/
@ActiveProfiles("dev")
@SpringBootTest(classes = LoggerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoggerApplicationTests {

	@Autowired
	private BusinessLogService operationLogService;

	@Test
	void testSaveOperationLog() {
		BusinessLog temp = new BusinessLog();
		temp.setBusinessId(System.currentTimeMillis());
		temp.setBusinessType("ia");
		temp.setContent("这是junit测试内容内容");
		temp.setOperatorId(Long.valueOf(1));
		temp.setCreateTime(LocalDateTime.now());
		temp.setMarketId(1L);
		operationLogService.save(temp);
	}

	@Test
	void testBatchSaveOperationLog() {
		List<BusinessLog> logList = Lists.newArrayList();
		for (int i=0;i<=10;i++){
			BusinessLog temp = new BusinessLog();
			temp.setBusinessId(System.currentTimeMillis());
			temp.setBusinessType("ia");
			JSONObject object = new JSONObject();
			object.put("num",i);
			object.put("content","这是第" + i + "个内容");
			temp.setContent(object.toJSONString());
			temp.setOperatorId(Long.valueOf(i));
			temp.setCreateTime(LocalDateTime.now().plusMinutes(i));
			temp.setMarketId(1L);
			logList.add(temp);
		}
		operationLogService.batchSave(logList);
	}

	@Test
	void testListPageOperationLog() {
		BusinessLogQueryInput temp = new BusinessLogQueryInput();
//		temp.setBusinessId(System.currentTimeMillis());
//		temp.setBusinessType("ia");
//		temp.setOperatorId(Long.valueOf(1));
//		temp.setCreateTime(LocalDateTime.now());
//		temp.setMarketId(1L);
		PageOutput<List<BusinessLog>> listPage = operationLogService.searchPage(temp);
		System.out.println("总记录数："+listPage.getTotal());
		System.out.println("总页数："+listPage.getPages());
		System.out.println("数据长度："+listPage.getData().size());
	}

	@Test
	void testListOperationLog() {
		BusinessLogQueryInput temp = new BusinessLogQueryInput();
//		temp.setBusinessId(System.currentTimeMillis());
//		temp.setBusinessType("ia");
//		temp.setCreateTime(LocalDateTime.now());
//		temp.setMarketId(1L);
//		org.elasticsearch.common.time.DateFormatter
//		temp.setCreateTimeStart(LocalDateTime.now().minusHours(3));
//		temp.setCreateTimeEnd(LocalDateTime.now().plusHours(2));

		BaseOutput<List<BusinessLog>> list = operationLogService.list(temp);
		List<BusinessLog> data = list.getData();
		if (CollectionUtil.isNotEmpty(data)) {
			System.out.println("数据长度：" + data.size());
			data.forEach(o -> {
				System.out.println(o.toString());
			});
		} else {
			System.out.println("没有数据");
		}
	}

	@Test
	void testDeleteAllOperationLog() {
		operationLogService.deleteAll();

	}

}
