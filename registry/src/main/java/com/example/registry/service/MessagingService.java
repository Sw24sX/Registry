package com.example.registry.service;

import com.example.registry.service.persistance.message.Message;
import com.example.registry.service.persistance.message.MessageReceiver;

import java.util.concurrent.TimeoutException;

/**
 * Ориентировочный интерфейс нашего messaging решения.
 */
public interface MessagingService {
    /**
     * Отправка сообщения в шину.
     *
     * @param msg сообщение для отправки.
     *
     * @return идентификатор отправленного сообщения (correlationId)
     */
    <T> MessageReceiver send(Message<T> msg);

    /**
     * Встает на ожидание ответа по сообщению с messageId.
     *
     * Редко, но может кинуть исключение по таймауту.
     *
     * @param messageReceiver идентификатор сообщения, на которое ждем ответ.
     * @return Тело ответа.
     */
    <T> Message<T> receive(MessageReceiver messageReceiver) throws TimeoutException;

    /**
     * Отправляем сообщение и ждем на него ответ.
     *
     * @param request тело запроса.
     * @return тело ответа.
     */
    <R, A> Message<A> doRequest(Message<R> request) throws TimeoutException;
}
