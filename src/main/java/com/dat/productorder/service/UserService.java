package com.dat.productorder.service;

import com.dat.productorder.dto.request.user.ChangeMyPasswordRequest;
import com.dat.productorder.dto.request.user.ChangeUserRoleRequest;
import com.dat.productorder.dto.request.user.ChangeUserStatusRequest;
import com.dat.productorder.dto.request.user.UpdateMyProfileRequest;
import com.dat.productorder.dto.response.user.MyProfileResponse;
import com.dat.productorder.dto.response.user.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    UserResponse changeUserStatus(Long id, ChangeUserStatusRequest request);
    UserResponse changeUserRole(Long id, ChangeUserRoleRequest request);
    void deleteUser(Long id);
    MyProfileResponse getMyProfile();
    MyProfileResponse updateMyProfile(UpdateMyProfileRequest request);
    void changeMyPassword(ChangeMyPasswordRequest request);
    void deleteMyAccount();
    MyProfileResponse uploadAvatar(MultipartFile file);
}
