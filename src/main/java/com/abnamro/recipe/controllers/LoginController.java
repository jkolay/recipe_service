package com.abnamro.recipe.controllers;


import com.abnamro.recipe.model.request.UserRequestModel;
import com.abnamro.recipe.model.response.UserResponse;
import com.abnamro.recipe.service.UserManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for new user and log in
 */
@RestController
public class LoginController {


    private final UserManagementService userManagementService;

    @Autowired
    public LoginController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    /**
     * register new user
     * @param userRequestModel the user details of the user
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRequestModel userRequestModel) {
        return userManagementService.registerUser(userRequestModel);

    }

    /**
     * the method to get details of authenticated user
     * @param authentication
     * @return
     */
    @RequestMapping("/user")
    public UserResponse getUserDetailsAfterLogin(Authentication authentication) {
        return userManagementService.getUserDetailsAfterLogin(authentication);

    }
}
