package com.catcher.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest
@ComponentScan(basePackages = {"com.catcher.resource"})
@EnableJpaRepositories(basePackages = {"com.catcher.datasource"})
@EntityScan(basePackages = {"com.catcher.core.domain.entity"})
class AppApplicationTests {

	@Test
	void contextLoads() {
	}

}
