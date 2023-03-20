package com.abnamro.recipe.model.request;


import com.abnamro.recipe.config.RecipeValidationMessageConfig;
import com.abnamro.recipe.model.constant.RecipeType;
import com.abnamro.recipe.model.constant.UserRole;
import com.abnamro.recipe.validator.EnumValidator;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User request model
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestModel {
  @NotBlank(message = RecipeValidationMessageConfig.USER_NAME_NOT_NULL)
  private String name;

  @NotBlank(message = RecipeValidationMessageConfig.EMAIL_NOT_NULL)
  private String email;

  @NotBlank(message = RecipeValidationMessageConfig.MOBILE_NOT_NULL)
  private String mobileNumber;

  @NotBlank(message = RecipeValidationMessageConfig.PASSWORD_NOT_NULL)
  private String pwd;

  @NotBlank(message = RecipeValidationMessageConfig.ROLE_NOT_NULL)
  @EnumValidator(enumClass = UserRole.class, message = RecipeValidationMessageConfig.INVALID_USER_ROLE)
  private String role;
}
