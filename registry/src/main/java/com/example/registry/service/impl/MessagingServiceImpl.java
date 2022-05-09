package com.example.registry.service.impl;

import com.example.registry.service.MessagingService;
import com.example.registry.service.persistance.message.Message;
import com.example.registry.service.persistance.message.MessageReceiver;
import com.example.registry.service.persistance.exception.RegistryException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpReplyTimeoutException;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class MessagingServiceImpl implements MessagingService {
    private static final Logger log = LoggerFactory.getLogger(MessagingServiceImpl.class);

    private final AsyncRabbitTemplate asyncRabbitTemplate;
    private final DirectExchange directExchange;

    public MessagingServiceImpl(AsyncRabbitTemplate asyncRabbitTemplate, DirectExchange directExchange) {
        this.asyncRabbitTemplate = asyncRabbitTemplate;
        this.directExchange = directExchange;
    }

    @Override
    public <T> MessageReceiver send(Message<T> msg) {
        //TODO
        ListenableFuture<Object> listenableFuture =
                asyncRabbitTemplate.convertSendAndReceiveAsType(
                        directExchange.getName(),
                        msg.getRouting().getValue(),
                        msg.getBody(),
                        new ParameterizedTypeReference<>() {
                        });

        log.info("Message was send");
        UUID correlationId = UUID.randomUUID();
        return new MessageReceiver(correlationId.toString(), msg.getRouting(), listenableFuture);
    }

    @Override
    public <T> Message<T> receive(MessageReceiver messageReceiver) throws TimeoutException {
        if (shouldSleep()) {
            sleep();
        }

        try {
            Object response = messageReceiver.getListenableFuture().get();
            log.info("Message was received");
            return new Message<>((T)response, messageReceiver.getRoutingKey());
        } catch (InterruptedException | ExecutionException e) {
            if (e.getCause() instanceof AmqpReplyTimeoutException) {
                throw new TimeoutException();
            }

            throw new RegistryException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public <R, A> Message<A> doRequest(Message<R> request) throws TimeoutException {
        final MessageReceiver messageReceiver = send(request);

        return receive(messageReceiver);
    }

    @SneakyThrows
    private static void sleep() {
        Thread.sleep(TimeUnit.MINUTES.toMillis(1));
    }


    private static boolean shouldSleep() {
        return new Random().nextInt(10) == 1;
    }
}
