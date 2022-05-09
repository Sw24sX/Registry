package com.example.registry.service.persistance.message.impl;

import com.example.registry.model.MessageState;
import com.example.registry.repository.MessageStateRepository;
import com.example.registry.service.persistance.message.MessagingService;
import com.example.registry.service.persistance.exception.RegistryException;
import com.example.registry.service.persistance.message.dto.Message;
import com.example.registry.service.persistance.message.dto.MessageId;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class CommonMessagingService implements MessagingService {
    private static final int timout = 1000;
    private static final Logger log = LoggerFactory.getLogger(CommonMessagingService.class);

    private final JmsTemplate jmsTemplate;
    private final Destination addDestination;
    private final Connection connection;
    private final MessageStateRepository messageStateRepository;

    public CommonMessagingService(JmsTemplate jmsTemplate, Destination addDestination, ConnectionFactory connectionFactory,
                                  MessageStateRepository messageStateRepository) throws JMSException {
        this.jmsTemplate = jmsTemplate;
        this.addDestination = addDestination;
        this.connection = connectionFactory.createConnection();
        this.messageStateRepository = messageStateRepository;
        this.connection.start();
    }


    @Override
    public <T> MessageId send(Message<T> msg) throws JMSException {
        String messageBody = convertRequest(msg.getBody());
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

        Destination replyDestination = session.createQueue("test");

        javax.jms.Message message = session.createTextMessage();
        message.setStringProperty("userObject", messageBody);
        message.setJMSReplyTo(replyDestination);
        String correlationId = UUID.randomUUID().toString();
        message.setJMSCorrelationID(correlationId);

        jmsTemplate.convertAndSend(addDestination, message);

        session.close();
        MessageState messageState = messageStateRepository.save(new MessageState(correlationId, msg.getQueueName(),
        msg.getPropertyBodyName()));
        return new MessageId(messageState.getId());


//        Session session = null;
//        try {
//            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
////            Destination replyDestination = session.createQueue(msg.getQueueName());
//            Destination replyDestination = session.createQueue("test");
//
//            javax.jms.Message message = session.createMessage();
//            String messageBody = convertRequest(msg.getBody());
//            message.setStringProperty(msg.getPropertyBodyName(), messageBody);
//            message.setJMSReplyTo(replyDestination);
//
//            String correlationId = UUID.randomUUID().toString();
//            message.setJMSCorrelationID(correlationId);
//
//            jmsTemplate.convertAndSend(destination, message);
//            log.info("Message was send");
//            MessageState messageState = messageStateRepository.save(new MessageState(correlationId, msg.getQueueName(),
//                    msg.getPropertyBodyName()));
//            return new MessageId(messageState.getId());
//        } catch (JMSException e) {
//            throw new RegistryException(e.getMessage(), e.getCause());
//        } finally {
//            //todo
//            if (session != null) {
//                session.close();
//            }
//        }
    }

    @Override
    public <T> Message<T> receive(MessageId messageId) throws TimeoutException, JMSException {
        // TODO: 09.05.2022
//        if(shouldThrowTimeout()) {
//            sleep();
//
//            throw new TimeoutException("Timeout!");
//        }
//
//        if (shouldSleep()) {
//            sleep();
//        }

        MessageState state = messageStateRepository.getById(messageId.getId());


        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination replyDestination = session.createQueue("test");

        MessageConsumer consumer = session.createConsumer(replyDestination, "JMSCorrelationID = '" + state.getCorrelationId() + "'");
        TextMessage reply = (TextMessage)consumer.receive();
        String userResponce = reply.getText();

        consumer.close();
        session.close();
        return new Message<>(convertResponse(reply.getText()), state.getQueueName(), state.getPropertyBodyName());

//        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        Destination replyDestination = session.createQueue(state.getQueueName());
//        MessageConsumer consumer = session.createConsumer(replyDestination, "JMSCorrelationID = '" + state.getCorrelationId() + "'");
//        TextMessage reply = (TextMessage) consumer.receive();
//
//        consumer.close();
//        session.close();
//
//        return new Message<>(convertResponse(reply.getText()), state.getQueueName(), state.getPropertyBodyName());
    }

    @Override
    public <R, A> Message<A> doRequest(Message<R> request) throws TimeoutException, JMSException {
        final MessageId messageId = send(request);

        return receive(messageId);
    }

    protected abstract <T> String convertRequest(T requestBody);

    protected abstract <T> T convertResponse(String message);

    @SneakyThrows
    private static void sleep() {
        log.info("Service sleep by 60 sec");
        Thread.sleep(TimeUnit.MINUTES.toMillis(1));
    }


    private static boolean shouldSleep() {
        return new Random().nextInt(10) == 1;
    }

    private static boolean shouldThrowTimeout() {
        return new Random().nextInt(10) == 1;
    }
}
