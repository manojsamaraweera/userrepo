package com.assesment.usermanager.service;

import com.assesment.usermanager.dto.UserProfile;
import com.assesment.usermanager.entity.User;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Configuration
@Log
@Setter
public class UserService {

    private RestTemplate restTemplate = new RestTemplate();

    private String userStoreUrl = "http://localhost:8282/users";

    public UserProfile getUserProfile(String username) {
        log.info("User Profile request received. Requesting user store in " + userStoreUrl);
        try {
            ResponseEntity<User> user = getUser(username);
            if (user.getStatusCode() == HttpStatus.OK) {
                return toUserProfile(user.getBody());
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public boolean isValidCredentials(String username, String password) {
        try {
            ResponseEntity<User> user = getUser(username);
            if (user.getStatusCode() == HttpStatus.OK) {
                return password.equals(user.getBody().getPassword());
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private static UserProfile toUserProfile(User user) {
        final UserProfile profile = new UserProfile();
        profile.setUsername(user.getUsername());
        profile.setPhoneNumber(user.getPhone());
        profile.setFullName(user.getFirstName() + " " + user.getLastName());

        return profile;
    }


    private ResponseEntity<User> getUser(String username) {
        return restTemplate.getForEntity(userStoreUrl + "/{username}",
                User.class,
                username);
    }
}
