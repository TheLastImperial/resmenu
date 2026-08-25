package com.thelastimperial.resmail.services;

import com.thelastimperial.resmail.rq.MailRq;

public interface MailRequestService {
    public void mailRequest(MailRq rq);
}
