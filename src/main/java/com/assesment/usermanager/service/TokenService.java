package com.assesment.usermanager.service;

import com.assesment.usermanager.entity.Token;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Configuration
@Log
@Setter
public class TokenService {
    private RestTemplate restTemplate = new RestTemplate();
    private String userStoreUrl = "http://localhost:8282/users";

    public boolean isValidToken(String username, String tokenStr) {
        log.info("Token Validate request received. Requesting token from user store in " + userStoreUrl);
        ResponseEntity<Token> token
                = restTemplate.getForEntity(userStoreUrl + "/{username}/tokens",
                Token.class,
                username);

        if (token.getStatusCode() == HttpStatus.OK) {
            return token.getBody().getToken().equals(tokenStr);
        }

        return false;
    }

    public String createToken(String username, String tokenStr) {
        log.info("Token Validate request received. Requesting token from user store in " + userStoreUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Token tokenObj = new Token();
        tokenObj.setToken(tokenStr);

        HttpEntity<Token> request = new HttpEntity<>(tokenObj, headers);
        ResponseEntity<Token> token
                = restTemplate.postForEntity(userStoreUrl + "/{username}/tokens",
                request,
                Token.class,
                username);

        if (token.getStatusCode() == HttpStatus.CREATED) {
            return tokenStr;
        } else {
            return null;
        }
    }
}
