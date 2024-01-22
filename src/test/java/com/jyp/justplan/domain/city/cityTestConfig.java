package com.jyp.justplan.domain.city;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Sql({"classpath:country-test.sql", "classpath:city-test.sql"})
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class cityTestConfig {

}