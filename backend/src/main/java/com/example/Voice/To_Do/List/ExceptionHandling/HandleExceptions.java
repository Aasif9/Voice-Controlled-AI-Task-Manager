package com.example.Voice.To_Do.List.ExceptionHandling;

import org.springframework.mail.MailSendException;

public class HandleExceptions {

    public static void throwRuntimeException(String customeMessage, Exception e) {
        if(customeMessage == null || customeMessage.isEmpty() ) {
            throw new RuntimeException(e.getMessage(), e);
        }else {

            throw new RuntimeException(customeMessage, e);
        }
    }

    public static void throwMailException(String customeMessage, Exception e) {
        if(customeMessage == null || customeMessage.isEmpty() ) {
            throw new MailSendException(e.getMessage(), e);
        }else {

            throw new MailSendException(customeMessage, e);
        }
    }

}
