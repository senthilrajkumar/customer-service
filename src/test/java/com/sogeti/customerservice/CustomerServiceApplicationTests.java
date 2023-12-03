package com.sogeti.customerservice;

import com.sogeti.customerservice.controller.CustomerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class CustomerServiceApplicationTests {

	@Autowired
	private CustomerController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
