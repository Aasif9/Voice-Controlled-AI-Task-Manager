package com.example.Voice.To_Do.List.Utils;

import com.example.Voice.To_Do.List.Analyzers.MyAnalyzer;
import com.example.Voice.To_Do.List.Model.User;
import org.springframework.beans.factory.annotation.Autowired;

public class UserHelper {

    @Autowired
    MyAnalyzer myAnalyzer;

    public boolean isValiduser(String email, String password, User user) {
        String processedEmail = myAnalyzer.stem(email);
        if (processedEmail.equals(email) && user.getPassword().equals(password)){
            return true;
        }
        return false;
    }
}

// delete

