package com.assesment.usermanager.service;

import com.assesment.usermanager.entity.Token;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TokenServiceTest {

    @Autowired
    TokenService tokenService;

    @Mock
    RestTemplate restTemplate;

    @Test
    void testIsValidTokenShouldReturnFalseForInvalidToken() {
        tokenService.setRestTemplate(restTemplate);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        when(restTemplate.getForEntity(anyString(), anyObject(), anyString()))
                .thenReturn(responseEntity);

        assertFalse(tokenService.isValidToken("test", "testToken"));
    }

    @Test
    void testIsValidTokenShouldReturnTrueForValidToken() {
        tokenService.setRestTemplate(restTemplate);
        Token t = new Token();
        t.setToken("testToken11");
        ResponseEntity<Object> responseEntity
                = new ResponseEntity<>(t, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), anyObject(), anyString()))
                .thenReturn(responseEntity);

        assertTrue(tokenService.isValidToken("test", "testToken11"));
    }

    @Test
    void testCreateTokeShouldReturnTokenOnSuccess() {
        tokenService.setRestTemplate(restTemplate);
        Token t = new Token();
        t.setToken("testToken22");
        ResponseEntity<Object> responseEntity
                = new ResponseEntity<>(t, HttpStatus.CREATED);
        when(restTemplate.postForEntity(anyString(), anyObject(), anyObject(), anyString()))
                .thenReturn(responseEntity);

        String tokenStr = tokenService.createToken("test", "testToken22");

        assertNotNull(tokenStr);
        assertEquals("testToken22", tokenStr);
    }

}
