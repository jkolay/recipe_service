package com.abnamro.recipe.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse {
    private String name;
    private String email;
    private String mobileNumber;
}
