package com.example.auditconsumer;

import com.example.auditconsumer.config.TestJmsConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestJmsConfig.class)
class AuditConsumerApplicationTests {

    @Test
    void contextLoads() {
    }
}
