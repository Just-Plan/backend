package com.jyp.justplan.domain.mbti;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Sql({"classpath:mbti.sql", "classpath:mbti-question.sql","classpath:mbti-answer.sql"})
@Transactional
@AutoConfigureMockMvc
public class MbtiTestConfig {

}