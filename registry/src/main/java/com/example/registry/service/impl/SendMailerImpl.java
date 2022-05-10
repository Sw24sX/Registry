package com.example.registry.service.impl;

import com.example.registry.service.SendMailer;
import com.example.registry.service.persistance.email.EmailAddress;
import com.example.registry.service.persistance.email.EmailContent;
import com.example.registry.service.persistance.exception.RegistryException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class SendMailerImpl implements SendMailer {
    private static final Logger LOG = LoggerFactory.getLogger(SendMailerImpl.class);

    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    public SendMailerImpl(TemplateEngine templateEngine, JavaMailSender mailSender) {
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
    }

    @Override
    public void sendMail(EmailAddress toAddress, EmailContent messageBody) throws TimeoutException {
        if(shouldThrowTimeout()) {
            LOG.info("Mal service throw timeout");
            sleep();

            throw new TimeoutException("Timeout!");
        }

        if(shouldSleep()) {
            sleep();
        }

        String content = build(messageBody);
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setSubject("Registry mail");
            messageHelper.setTo(toAddress.getRecipientAddress());
            messageHelper.setText(content, true);
        };

        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            throw new RegistryException(e);
        }

        LOG.info("Message sent to {}, body {}.", toAddress.getRecipientAddress(), content);
    }

    private String build(EmailContent messageBody) {
        Context context = new Context();
        context.setVariables(messageBody.getVariables());
        return templateEngine.process(messageBody.getTemplate(), context);
    }

    @SneakyThrows
    private static void sleep() {
        LOG.info("Mail service sleep by 60 sec");
        Thread.sleep(TimeUnit.MINUTES.toMillis(1));
    }

    private static boolean shouldSleep() {
        return new Random().nextInt(10) == 1;
    }

    private static boolean shouldThrowTimeout() {
        return new Random().nextInt(10) == 1;
    }
}
