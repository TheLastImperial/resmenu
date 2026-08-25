package com.thelastimperial.resmenu.services.impl;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.thelastimperial.resmenu.controllers.rq.MailRq;
import com.thelastimperial.resmenu.services.MailRequestService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MailRequestServiceImpl implements MailRequestService {
    public final RabbitTemplate template;
    public final Queue queue;
    @Override
    public void sendMail(MailRq rq) {
        template.convertAndSend(queue.getName(), rq);
    }
    
}
