package com.example.assurancehabitation.config;

import jakarta.jms.ConnectionFactory;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.SimpleMessageConverter;

@TestConfiguration
public class TestJmsConfig {

    @Bean
    @Primary
    public ConnectionFactory connectionFactory() {
        return Mockito.mock(ConnectionFactory.class);
    }

    @Bean
    @Primary
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setMessageConverter(new SimpleMessageConverter());
        return template;
    }
}
