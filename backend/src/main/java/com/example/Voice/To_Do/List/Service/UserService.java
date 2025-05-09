package com.example.Voice.To_Do.List.Service;

import com.example.Voice.To_Do.List.Analyzers.MyAnalyzer;
import com.example.Voice.To_Do.List.DAO.AuthRepository;
import com.example.Voice.To_Do.List.Model.User;
import com.example.Voice.To_Do.List.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
// delete this
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    MyAnalyzer myAnalyzer;

    @Autowired
    private AuthRepository authRepository;
    public User findUserByEmail(String email){
        if(userRepository.existsByEmail(email)){
            Optional<User> user = userRepository.findByEmail(email);
            return user.orElse(null);
//            OR
//            if(user.isPresent()){
//                return user.get();
//            }else {
//                return null;
//            }
        }

        return null;
    }

    public User saveToDB(String email, String password){
        String processedEmail = myAnalyzer.stem(email);
        User user = User.builder().email(processedEmail).password(password).build();
        if(userRepository.existsByEmail(email)){
            return null;
        }
        else {
            return userRepository.save(user);
        }
    }

//    public User validateUser(String token){
//        if (jwtService.validateToken((token))) {
//            Long id = 1L;
//            Optional<User> user = authRepository.findById(id);
//            if(user.isPresent()){
//                return user.get();
//            }
//            else {
//                log.error("not found in DB");
//            }
//        }
//        return null;
//
//    }
}

