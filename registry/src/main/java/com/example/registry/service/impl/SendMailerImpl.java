package com.example.registry.service.impl;

import com.example.registry.service.SendMailer;
import com.example.registry.service.persistance.dto.EmailAddress;
import com.example.registry.service.persistance.dto.EmailContent;
import com.example.registry.service.persistance.exception.RegistryException;
import lombok.SneakyThrows;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class SendMailerImpl implements SendMailer {
    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    public SendMailerImpl(TemplateEngine templateEngine, JavaMailSender mailSender) {
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
    }

    @Override
    public void sendMail(EmailAddress toAddress, EmailContent messageBody) throws TimeoutException {
        if(shouldThrowTimeout()) {
            sleep();

            throw new TimeoutException("Timeout!");
        }

        if(shouldSleep()) {
            sleep();
        }

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setSubject("Registry mail");
            messageHelper.setTo(toAddress.getRecipientAddress());
            String content = build(messageBody);
            messageHelper.setText(content, true);
        };

        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            throw new RegistryException(e.getMessage(), e.getCause());
        }
//        log.info("Message sent to {}, body {}.", toAddress, messageBody);
    }

    private String build(EmailContent messageBody) {
        Context context = new Context();
        context.setVariables(messageBody.getVariables());
        return templateEngine.process(messageBody.getTemplate(), context);
    }

    @SneakyThrows
    private static void sleep() {
        Thread.sleep(TimeUnit.MINUTES.toMillis(1));
    }

    private static boolean shouldSleep() {
        return new Random().nextInt(10) == 1;
    }

    private static boolean shouldThrowTimeout() {
        return new Random().nextInt(10) == 1;
    }
}
