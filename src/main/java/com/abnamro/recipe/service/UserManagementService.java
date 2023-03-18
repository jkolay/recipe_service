package com.abnamro.recipe.service;


import com.abnamro.recipe.model.request.UserRequestModel;
import com.abnamro.recipe.model.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface UserManagementService {
  ResponseEntity<String> registerUser(UserRequestModel userManagementService);

  UserResponse getUserDetailsAfterLogin(Authentication authentication);
}
