package com.assesment.usermanager.service;

import com.assesment.usermanager.dto.UserProfile;
import com.assesment.usermanager.entity.User;
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
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void testIsValidCredentialsAShouldReturnFalseForInvalidCredentials() {
        userService.setRestTemplate(restTemplate);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        when(restTemplate.getForEntity(anyString(), anyObject(), anyString()))
                .thenReturn(responseEntity);

        assertFalse(userService.isValidCredentials("test", "fdsfd"));
    }

    @Test
    void testIsValidCredentialsAShouldReturnTrueForValidCredentials() {
        userService.setRestTemplate(restTemplate);
        User usr = new User();
        usr.setPassword("password");
        usr.setUsername("test");
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(usr, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), anyObject(), anyString()))
                .thenReturn(responseEntity);

        assertTrue(userService.isValidCredentials("test", "password"));
    }

    @Test
    void testGetUserProfileShouldReturnFullNameAndPhoneNumber() {
        userService.setRestTemplate(restTemplate);
        User usr = new User();
        usr.setUsername("test");
        usr.setFirstName("FirstName");
        usr.setLastName("LastName");
        usr.setPhone("+94771188");
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(usr, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), anyObject(), anyString()))
                .thenReturn(responseEntity);

        UserProfile profile = userService.getUserProfile("test");
        assertNotNull(profile);
        assertEquals("FirstName LastName", profile.getFullName());
        assertEquals("+94771188", profile.getPhoneNumber());
    }

}
