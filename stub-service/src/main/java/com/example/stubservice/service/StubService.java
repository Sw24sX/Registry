package com.example.stubservice.service;

import com.example.stubservice.dto.UserData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class StubService {
    private static final Logger log = LoggerFactory.getLogger(StubService.class);
    private static final Set<String> INCORRECT_EMAILS = new HashSet<>(Arrays.asList("string", "test@test.ru"));

    private final ObjectMapper mapper;
    private final Connection connection;

    public StubService(ConnectionFactory connectionFactory) throws JMSException {
        this.mapper = new ObjectMapper();
        connection = connectionFactory.createConnection();
        connection.start();
    }

    @JmsListener(destination = "add-user-queue")
    public void registryConsumer(Message message) throws JsonProcessingException, JMSException {
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        String userAsString = message.getStringProperty("userObject");
//        UserObject user = mapper.readValue(userAsString, UserObject.class);
//        user.setName("Not" + user.getName());

        String replyPayload = mapper.writeValueAsString(true);

        TextMessage replyMessage = session.createTextMessage(replyPayload);
        replyMessage.setJMSDestination(message.getJMSReplyTo());
        replyMessage.setJMSCorrelationID(message.getJMSCorrelationID());

        MessageProducer producer = session.createProducer(message.getJMSReplyTo());
        producer.send(replyMessage);

        session.close();

//        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        String userAsString = message.getStringProperty("userData");
//        UserData user = mapper.readValue(userAsString, UserData.class);
//        user.setApproval(isApproval(user));
//        String replyPayload = mapper.writeValueAsString(user.isApproval());
//
//        TextMessage replyMessage = session.createTextMessage(replyPayload);
//        replyMessage.setJMSDestination(message.getJMSReplyTo());
//        replyMessage.setJMSCorrelationID(message.getJMSCorrelationID());
//
//        MessageProducer producer = session.createProducer(message.getJMSReplyTo());
//        producer.send(replyMessage);
//
//        producer.close();
//        session.close();
    }

    private boolean isApproval(UserData userData) {
        sleep();
        return !INCORRECT_EMAILS.contains(userData.getEmail());
    }

    @SneakyThrows
    private static void sleep() {
        int secondsSleep = new Random().nextInt(10);
        log.info("Sleep by {}", secondsSleep);
        Thread.sleep(TimeUnit.SECONDS.toMillis(secondsSleep));
    }
}
