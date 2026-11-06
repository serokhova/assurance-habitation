package com.example.assurancehabitation;

import com.example.assurancehabitation.config.TestJmsConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestJmsConfig.class)
class AssuranceHabitationApplicationTests {

    @Test
    void contextLoads() {
    }

}
