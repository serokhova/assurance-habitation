package com.example.assurancehabitation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;

@Configuration
@EnableJms
public class JmsConfig {

    /**
     * Utilise des TextMessage (JSON String) bruts pour maximiser la compatibilité
     * avec tous les brokers JMS et faciliter le débogage dans la console Artemis.
     * Le producteur sérialise lui-même en JSON via ObjectMapper.
     */
    @Bean
    public MessageConverter messageConverter() {
        return new SimpleMessageConverter();
    }
}
