package com.newconomy.newconomy;

import com.newconomy.member.domain.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class NewconomyApplicationTests {

	@Autowired
	EntityManager em;

	@Test
	void contextLoads() {
		return;
	}

}
