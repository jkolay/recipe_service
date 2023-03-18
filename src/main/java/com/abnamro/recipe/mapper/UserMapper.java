package com.abnamro.recipe.mapper;


import com.abnamro.recipe.model.persistence.UserDao;
import com.abnamro.recipe.model.request.UserRequestModel;
import com.abnamro.recipe.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {

    UserDao mapUserRequestModelToCustomer(UserRequestModel userRequestModel);

    UserResponse mapUserDetailsToUserResponse(UserDao customerDao);
}
