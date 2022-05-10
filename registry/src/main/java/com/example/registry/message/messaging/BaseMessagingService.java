package com.example.registry.message.messaging;

import com.example.registry.service.persistance.exception.RegistryException;
import com.example.registry.service.persistance.model.MessageState;
import com.example.registry.service.persistance.repository.MessageStateRepository;
import com.example.registry.message.dto.Message;
import com.example.registry.message.dto.MessageId;
import lombok.SneakyThrows;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Базовый сервис отправки сообщений.
 * Реализует подключение, отправку и ожидание сообщения.
 */
public abstract class BaseMessagingService implements MessagingService {
    private static final int TIMEOUT_RECEIVE = 8000;
    private static final Logger log = LoggerFactory.getLogger(BaseMessagingService.class);

    private final JmsTemplate jmsTemplate;
    private final Connection connection;
    private final MessageStateRepository messageStateRepository;

    public BaseMessagingService(JmsTemplate jmsTemplate, ConnectionFactory connectionFactory,
                                MessageStateRepository messageStateRepository) throws JMSException {
        this.jmsTemplate = jmsTemplate;
        this.connection = connectionFactory.createConnection();
        this.messageStateRepository = messageStateRepository;
        this.connection.start();
    }

    @Override
    public <T> MessageId send(Message<T> msg) {
        String messageBody = convertRequest(msg.getBody());
        try(Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            Destination replyDestination = session.createQueue(getReplyDestination(msg.getDestination()));

            TextMessage message = session.createTextMessage();
            message.setStringProperty(msg.getPropertyBodyName(), messageBody);
            message.setJMSReplyTo(replyDestination);
            String correlationId = UUID.randomUUID().toString();
            message.setJMSCorrelationID(correlationId);

            Destination destination = new ActiveMQQueue(msg.getDestination());
            jmsTemplate.convertAndSend(destination, message);
            MessageState messageState = messageStateRepository.save(new MessageState(correlationId, msg.getDestination(),
                    msg.getPropertyBodyName()));
            return new MessageId(messageState.getId());
        } catch (JMSException e) {
            throw new RegistryException(e);
        }
    }

    @Override
    public <T> Message<T> receive(MessageId messageId) throws TimeoutException {
        if (shouldSleep()) {
            sleep();
        }

        try (Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            MessageState state = messageStateRepository.getById(messageId.getId());
            Destination replyDestination = session.createQueue(getReplyDestination(state.getQueueName()));

            String correlationId = String.format("JMSCorrelationID = '%s'", state.getCorrelationId());
            try (MessageConsumer consumer = session.createConsumer(replyDestination, correlationId)) {
                TextMessage reply = (TextMessage)consumer.receive(TIMEOUT_RECEIVE);
                Optional.ofNullable(reply).orElseThrow(() -> new TimeoutException("Timeout!"));

                return new Message<>(convertResponse(reply.getText()), null);
            }
        } catch (JMSException e) {
            throw new RegistryException(e);
        }
    }

    private String getReplyDestination(String requestDestination) {
        return String.format("%s.reply", requestDestination);
    }

    @Override
    public <R, A> Message<A> doRequest(Message<R> request) throws TimeoutException {
        final MessageId messageId = send(request);

        return receive(messageId);
    }

    protected abstract <T> String convertRequest(T requestBody);

    protected abstract <T> T convertResponse(String message);

    @SneakyThrows
    private static void sleep() {
        log.info("Messaging service sleep by 60 sec");
        Thread.sleep(TimeUnit.MINUTES.toMillis(1));
    }

    private static boolean shouldSleep() {
        return new Random().nextInt(10) == 1;
    }
}
