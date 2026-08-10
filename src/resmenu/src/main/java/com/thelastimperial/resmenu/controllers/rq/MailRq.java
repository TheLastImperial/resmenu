package com.thelastimperial.resmenu.controllers.rq;

import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class MailRq {
    private String to;
    private String subject;
    private boolean html;
    private String content;
    private String contentName;

    private HashMap<String, Object> params;
}
