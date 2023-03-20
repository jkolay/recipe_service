package com.abnamro.recipe.service.impl;


import com.abnamro.recipe.exception.UserException;
import com.abnamro.recipe.mapper.UserMapper;
import com.abnamro.recipe.model.persistence.AuthorityDao;
import com.abnamro.recipe.model.persistence.UserDao;
import com.abnamro.recipe.model.request.UserRequestModel;
import com.abnamro.recipe.model.response.UserResponse;
import com.abnamro.recipe.repositories.AuthorityRepository;
import com.abnamro.recipe.repositories.LoginRepository;
import com.abnamro.recipe.service.UserManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class UserManagementServiceImpl implements UserManagementService {

  private final AuthorityRepository authorityRepository;
  private final LoginRepository loginRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  public UserManagementServiceImpl(AuthorityRepository authorityRepository, LoginRepository loginRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
    this.authorityRepository = authorityRepository;
    this.loginRepository = loginRepository;
    this.passwordEncoder = passwordEncoder;
    this.userMapper = userMapper;
  }

  /**
   * add a new user to the app
   * @param userRequestModel
   * @return
   */
  @Override
  public ResponseEntity<String> registerUser(UserRequestModel userRequestModel) {
    UserDao savedCustomer = null;
    ResponseEntity response = null;
    try {
      UserDao user = userMapper.mapUserRequestModelToCustomer(userRequestModel);
      if (!userRequestModel.getRole().equalsIgnoreCase("ADMIN")
          && !userRequestModel.getRole().equalsIgnoreCase("CUSTOMER")) {
        throw new UserException("User role needs to be either admin or customer");
      }
      String hashPwd = passwordEncoder.encode(user.getPwd());
      user.setPwd(hashPwd);
      user.setCreateDt(String.valueOf(new Date(System.currentTimeMillis())));
      savedCustomer = loginRepository.save(user);
      String authorityName = "ROLE_" + user.getRole().toUpperCase();

      AuthorityDao authority = new AuthorityDao();
      authority.setName(authorityName);
      authority.setUserDao(savedCustomer);
      authorityRepository.save(authority);
      if (savedCustomer.getId() > 0) {
        response =
            ResponseEntity.status(HttpStatus.CREATED)
                .body("Given user details are successfully registered");
      }
    } catch (Exception ex) {
      response =
          ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("An exception occurred due to " + ex.getMessage());
    }
    return response;
  }

  /**
   * Authenticate user and generate auth token on successful authorization
   * @param authentication
   * @return
   */
  @Override
  public UserResponse getUserDetailsAfterLogin(Authentication authentication) {
    List<UserDao> customers = loginRepository.findByEmail(authentication.getName());
    if (customers.size() > 0) {
      return userMapper.mapUserDetailsToUserResponse(customers.get(0));
    } else {
      return null;
    }
  }
}
