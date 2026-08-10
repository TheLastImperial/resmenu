package com.thelastimperial.resmail.services;

import java.util.HashMap;

public interface SendMailService {
    public void sendMail(String to, String subject, String content);
    public void sendHtmlMail(
        String to, String subject, String template, HashMap<String, Object> params
    );
}
