package com.my.column;

import com.my.column.service.ArticleService;
import com.my.column.service.SearchService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

class MyColumnApplicationTests {

	@Test
	void contextLoads() {
		SearchService.searchActor("朱",0,20);
		SearchService.searchFilm("如果",0,20);
	}

}
