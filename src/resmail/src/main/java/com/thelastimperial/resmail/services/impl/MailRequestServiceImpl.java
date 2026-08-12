package com.thelastimperial.resmail.services.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.thelastimperial.resdomain.entities.MailAuditEntity;
import com.thelastimperial.resdomain.repositories.MailAuditRepository;
import com.thelastimperial.resmail.rq.MailRq;
import com.thelastimperial.resmail.services.MailRequestService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class MailRequestServiceImpl implements MailRequestService {
    private final SendEmailServiceImpl sendEmailService;
    private final MailAuditRepository mailAuditRepository;

    @Override
    @RabbitListener(queues = "${com.thelastimperial.resmail.queue}")
    public void mailRequest(MailRq rq) {
        if(rq.isHtml())
            sendEmailService.sendHtmlMail(
                rq.getTo(), rq.getSubject(), rq.getContent(), rq.getParams()
            );
        else
            sendEmailService.sendMail(rq.getTo(), rq.getSubject(), rq.getContent());

        log.info("Email send to: {}", rq.getTo());
        mailAuditRepository.save(
            MailAuditEntity.builder()
            .email(rq.getTo())
            .subject(rq.getSubject())
            .contentName(rq.getContentName())
            .build()
        );
    }
    
}
