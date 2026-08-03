package com.dat.productorder.service;

import com.dat.productorder.dto.request.user.CreateUserRequest;
import com.dat.productorder.dto.request.auth.LoginRequest;
import com.dat.productorder.dto.request.auth.RefreshRequest;
import com.dat.productorder.dto.response.auth.AuthResponse;
import com.dat.productorder.dto.response.user.MyProfileResponse;

public interface AuthService {
    MyProfileResponse register(CreateUserRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refresh(RefreshRequest request);
    void logout();
}
