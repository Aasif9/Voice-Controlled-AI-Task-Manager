package com.example.Voice.To_Do.List.Controller;

import com.example.Voice.To_Do.List.Model.EmailRequest;
import com.example.Voice.To_Do.List.Model.Notify;
import com.example.Voice.To_Do.List.Service.EmailService;
import com.example.Voice.To_Do.List.Service.ServiceInterface.INotifyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "http://localhost:5500")
public class EmailController {

    private EmailService emailService;

    public  EmailController(){
        emailService = new EmailService();
    }
    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest request){

        try {
            emailService.sendTaskNotification(
                    request.getTo(),
                    request.getSubject(),
                    request.getMessage()
            );
            return ResponseEntity.ok().body("Email sent successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send email: " + e.getMessage());
        }
//
//
//        try {
//            if(request.getTo().isEmpty() || request.getMessage().isEmpty() || request.getSubject().isEmpty()){
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//            }
//
//            Notify notify = Notify.builder().email(request.getTo())
//                    .message(request.getMessage())
//                    .metaData(request.getSubject())
//                    .build();
//            emailService.notifyUser(notify);
//            return ResponseEntity.ok().body("Email sent successfully");
//        }
    }
}


