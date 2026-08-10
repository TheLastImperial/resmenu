package com.thelastimperial.resmenu.services;

import com.thelastimperial.resmenu.controllers.rq.MailRq;

public interface MailRequestService {
    public void sendMail(MailRq rq);
}
