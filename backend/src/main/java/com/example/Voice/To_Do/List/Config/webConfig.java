package com.example.Voice.To_Do.List.Config;

import com.example.Voice.To_Do.List.Constants.CommonConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class webConfig implements WebMvcConfigurer {

//    public static final String API = "/api/**";
//    public static final String GET = "GET";
//    public static final String POST = "POST";
//    public static final String PUT = "PUT";
//    public static final String DELETE = "DELETE";
//    public static final String OPTIONS = "OPTIONS";

//    @Value("${allowed.origin}")
//    private String origin;
    @Override
    public void addCorsMappings(CorsRegistry registry){
//        String[] allowedOrigins = origin.split(CommonConstants.COMMA);
//        List<String> allowedOriginsParsed = Arrays.stream(allowedOrigins).map(String::trim).toList();
        registry.addMapping("/api/**")
                .allowedOrigins("http://127.0.0.1:5500", "http://localhost:5500")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
