package com.dat.productorder.mapper;

import com.dat.productorder.dto.request.user.CreateUserRequest;
import com.dat.productorder.dto.response.user.MyProfileResponse;
import com.dat.productorder.dto.response.role.RoleSummaryResponse;
import com.dat.productorder.dto.response.user.UserResponse;
import com.dat.productorder.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserRequest request){

        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAvatar(request.getAvatar());

        return user;

    }

    public UserResponse toResponse(User user){

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setAvatar(user.getAvatar());
        response.setStatus(user.getStatus());
        response.setDeleted(user.isDeleted());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        RoleSummaryResponse roleResponse = new RoleSummaryResponse();
        roleResponse.setId(user.getRole().getId());
        roleResponse.setName(user.getRole().getName());

        response.setRole(roleResponse);

        return response;

    }


    public MyProfileResponse toMyProfileResponse(User user) {

        MyProfileResponse response = new MyProfileResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setAvatar(user.getAvatar());
        response.setCreatedAt(user.getCreatedAt());

        RoleSummaryResponse roleResponse = new RoleSummaryResponse();
        roleResponse.setId(user.getRole().getId());
        roleResponse.setName(user.getRole().getName());

        response.setRole(roleResponse);

        return response;

    }

}
