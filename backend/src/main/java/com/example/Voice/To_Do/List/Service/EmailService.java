package com.example.Voice.To_Do.List.Service;

import com.example.Voice.To_Do.List.ExceptionHandling.HandleExceptions;
import com.example.Voice.To_Do.List.Model.Notify;
import com.example.Voice.To_Do.List.Service.ServiceInterface.INotifyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("uzer.infinitecoder@gmail.com")
    private String fromEmail;

    public void sendTaskNotification(String to, String subject, String message) {
//        try{
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom(fromEmail);
            email.setTo(to);
            email.setSubject(subject);
            email.setText(message);
            mailSender.send(email);
//        }catch (MailException e){
//            HandleExceptions.throwMailException("Unable to send Mail", e);
//            return false;
//        }

    }

//    @Override
//    public void notifyUser(Notify notify) {
//        sendTaskNotification(notify.getEmail(), notify.getMetaData(), notify.getMessage());
//    }
}
