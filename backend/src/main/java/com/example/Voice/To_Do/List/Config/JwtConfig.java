package com.example.Voice.To_Do.List.Config;

import com.example.Voice.To_Do.List.Constants.CommonConstants;
import com.example.Voice.To_Do.List.Constants.JWTConstants;
import com.example.Voice.To_Do.List.ExceptionHandling.HandleExceptions;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

//class 12 : 52min
@Configuration
public class JwtConfig {

    private static final String PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvcljcDWwTyHhwjRzVRlo\n" +
            "Se4tL+DcN3Tjz/cwjRS9Y3353PBBqQtwWfQeDd0niuXqYVoQ4ZiSMD/wo5yDVwRB\n" +
            "lfW+XNI5ePXeYlHSH8r7uELaZoGMJNxXiwVvyY6lvYxkPo9go79AoXXQXebCzPV/\n" +
            "S6CbeF2DE590h4+XSQqHleHAP25UhpwuJdwaDFcWs5o+BQTT6sDClJVWx/EbgoR+\n" +
            "nYVXVBShR1xZzZA9mDWztl9iRa0VENKSP5l0tx6oe98MRp8bmH+SVQPHBhX4kjcn\n" +
            "qn1DjBpVnNLaHo6lgDAsHrm0+ThfDFYCQ8UIvGKFoRPo8+fHz96XEzbq5wAZ+vVc\n" +
            "MQIDAQAB\n" +
            "-----END PUBLIC KEY-----";

    @Bean
    public JwtParser jwtParser(){
        return Jwts.parserBuilder().setSigningKey(getRSAPublicKey()).build();
    }

    private RSAPublicKey getRSAPublicKey() {
        try {
            String publicKeyPEM = JWTConstants.PUBLIC_KEY
                    .replace(JWTConstants.BEGIN_PUBLIC_KEY, "")
                    .replace(JWTConstants.END_PUBLIC_KEY, "")
                    .replaceAll("\\s", "");

            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory kf = KeyFactory.getInstance(JWTConstants.RSA);
            return (RSAPublicKey) kf.generatePublic(spec);
        } catch (Exception e) {
            HandleExceptions.throwRuntimeException("Failed to load RSA public key",e);
        }
        return null;
    }



    public Claims parseToken(String token) {
        try {
            return jwtParser().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public String getUserRole(String token) {
        Claims claims = parseToken(token);
        return claims.get(CommonConstants.ROLE, String.class);
    }

    public String getUserEmail(String token) {
        Claims claims = parseToken(token);
        return claims.get(CommonConstants.EMAIL, String.class);
    }

    public String getUserPassword(String token) {
        Claims claims = parseToken(token);
        return claims.get(CommonConstants.PASSWORD, String.class);
    }

    public boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().before(new Date());
    }


//    private RSAPrivateKey getRSAPrivateKey(){
//        try{
//            byte[] privateKeyBytes = Base64.getDecoder().decode(
//                PRIVATE_KEY.replace("-----BEGIN PUBLIC KEY ---- \n", "")
//                        .replace("-----END PUBLIC KEY----", "")
//                        .replaceAll("\\s", "")
//            );
//            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyBytes);
//            KeyFactory kf = KeyFactory.getInstance("RSA");
//            return (RSAPrivateKey) kf.generatePrivate(spec);
//        }catch (Exception exception){
//            throw new RuntimeException("Failed to load RSA key", exception);
//        }
//    }
}








