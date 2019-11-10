package com.assesment.usermanager.controller;

import com.assesment.usermanager.dto.UserProfile;
import com.assesment.usermanager.dto.UserToken;
import com.assesment.usermanager.service.TokenService;
import com.assesment.usermanager.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@Log
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/users/{username}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfile> getUserProfile(@RequestHeader("authorization") String authToken,
                                                      @PathVariable String username) {
        log.info("get users called for username " + username + "and with toke: " + authToken);

        final UserProfile userProfile = userService.getUserProfile(username);

        if (userProfile == null) {
            log.info("User Profile not found");
            Map<String, String> message = new HashMap();
            message.put("message", "No users found");
            return new ResponseEntity(message, HttpStatus.NOT_FOUND);
        }

        if (!isTokenValid(username, authToken)) {
            log.info("Invalid token");
            Map<String, String> message = new HashMap();
            message.put("message", "Invalid token");
            return new ResponseEntity(message, HttpStatus.UNAUTHORIZED);
        }

        log.info("User found for username: " + username);
        return new ResponseEntity(userProfile, HttpStatus.OK);
    }

    private boolean isTokenValid(String username, String authToken) {
        return tokenService.isValidToken(username, authToken);
    }

    @RequestMapping(value = "/users",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserToken> login(@RequestHeader("authorization") String authString) {
        String username = getUserForValidCredentials(authString);
        if(username == null || username.isEmpty()) {
            log.info("Invalid Basic Auth Headers: " + authString);
            Map<String, String> message = new HashMap();
            message.put("error", "Invalid username/password");
            return new ResponseEntity(message, HttpStatus.UNAUTHORIZED);
        }

        String tokenStr = UUID.randomUUID().toString();
        String createdToken = tokenService.createToken(username, tokenStr);

        if (createdToken != null || !createdToken.isEmpty()) {
            return new ResponseEntity(tokenStr, HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getUserForValidCredentials(String authString) {
        if (authString != null && authString.toLowerCase().startsWith("basic")) {
            String base64Credentials = authString.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            final String[] values = credentials.split(":", 2);
            if (userService.isValidCredentials(values[0], values[1])) {
                return values[0];
            }
        }
        return null;
    }
}
