package com.thelastimperial.resmail.services.impl;

import java.util.HashMap;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.thelastimperial.resmail.services.SendMailService;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class SendEmailServiceImpl implements SendMailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    @Override
    public void sendHtmlMail(
        String to, String subject, String template, HashMap<String, Object> params
    ){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            Context ctx = new Context();
            ctx.setVariables(params);
            String htmlContent = templateEngine.process(template, ctx);
            MimeMessageHelper helper = new MimeMessageHelper(
                message, true, "UTF-8"
            );
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch( Exception me ) {
            log.error("There is a error trying to set email");
            log.error(me.getMessage());
            return;
        }
    }
}
