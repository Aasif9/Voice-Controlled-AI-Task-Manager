package com.example.Voice.To_Do.List.Service;

import com.example.Voice.To_Do.List.Model.Task;
import com.example.Voice.To_Do.List.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private EmailService emailService;

//    @Autowired
//   private SMSService smsService;
    public void sendTaskNotification(Task task, User user, boolean isUpdated){
        if(user != null){
            String action = isUpdated ? "updated" : "created";
            String emailMessage = String.format("Task %s: %s\nUrgency %s\nDue: %s", action, task.getTask(), task.getUrgency(), task.getDatetime());
//            String subject = "Task" + action.substring(0, 1).toUpperCase() +  action.substring(1);
            emailService.sendTaskNotification(user.getEmail(), "Task " + action.substring(0, 1).toUpperCase() + action.substring(1), emailMessage);
            //If user has phone number

            // Send SMS if phone number is available
//            if (user.getPhoneNumber() != null) {
//                smsService.sendSMS(
//                    user.getPhoneNumber(),
//                    "Task " + action + ": " + task.getTask() + " (Urgency: " + task.getUrgency() + ")"
//                );
//            }
        }
    }
}

//class 9 started



