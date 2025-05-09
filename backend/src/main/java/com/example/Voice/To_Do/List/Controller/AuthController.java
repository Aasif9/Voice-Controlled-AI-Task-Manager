package com.example.Voice.To_Do.List.Controller;

import com.example.Voice.To_Do.List.Config.JwtConfig;
import com.example.Voice.To_Do.List.Constants.ApiPaths;
import com.example.Voice.To_Do.List.Model.User;
import com.example.Voice.To_Do.List.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(ApiPaths.AUTH_BASE_PATH)
@CrossOrigin(origins = "http://localhost:5500")
public class AuthController {


    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private UserHelper userHelper;

    @PostMapping(ApiPaths.LOGIN)
    public ResponseEntity<?> login(@RequestHeader("Authorization") String token){
        if(token != null && token.startsWith(("Bearer "))){
            token = token.substring(7);
            if(!jwtConfig.isTokenExpired(token)){
                String email = jwtConfig.getUserEmail(token);
                String password = jwtConfig.getUserPassword(token);
//                User user = userService.findUserByEmail(email);
                return userRepository.findByEmail(email)
                        .filter(user -> user.getPassword().equals(password))
                        .map(user -> {
                            Map<String, Object> response = new HashMap<>();
                            response.put("id", user.getId());
                            response.put("email", user.getEmail());
                            return ResponseEntity.ok(response);
                        })
                        .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping(ApiPaths.SIGNUP)
    public ResponseEntity<?> signup(@RequestHeader("Authorization") String token){
        if(token != null && token.startsWith(("Bearer "))){
            token= token.substring(7);
            if (!jwtConfig.isTokenExpired(token)) {
                String email = jwtConfig.getUserEmail(token);
                String password = jwtConfig.getUserPassword(token);

                if (userRepository.existsByEmail(email)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(Map.of("message", "Email already exists"));
                }

                User user = new User();
                user.setEmail(email);
                user.setPassword(password);
                userRepository.save(user);

                Map<String, Object> response = new HashMap<>();
                response.put("id", user.getId());
                response.put("email", user.getEmail());
                response.put("password", user.getPassword());
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}



