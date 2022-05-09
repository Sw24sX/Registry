package com.example.stubservice.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

@Configuration
@EnableJms
public class StubServiceConfig {
    @Value("${activemq.broker-url}")
    private String brokerUrl;

    @Value("${activemq.user}")
    private String username;

    @Value("${activemq.password}")
    private String password;

    @Bean
    public Destination addDestination() {
        return new ActiveMQQueue("add-user-queue");
    }

    @Bean
    public Destination getRequestDestination() {
        return new ActiveMQQueue("get-user-queue");
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(brokerUrl);
        factory.setUserName(username);
        factory.setPassword(password);
        return factory;
    }

//    @Bean
//    public JmsTemplate jmsTemplate(){
//        return new JmsTemplate(connectionFactory());
//    }
}
