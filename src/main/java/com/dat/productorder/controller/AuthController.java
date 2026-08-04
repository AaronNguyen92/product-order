package com.dat.productorder.controller;

import com.dat.productorder.dto.request.user.CreateUserRequest;
import com.dat.productorder.dto.request.auth.LoginRequest;
import com.dat.productorder.dto.request.auth.RefreshRequest;
import com.dat.productorder.dto.response.auth.AuthResponse;
import com.dat.productorder.dto.response.user.MyProfileResponse;
import com.dat.productorder.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@Tag(
        name = "Authentication",
        description = "APIs for Authentication"
)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Register",
            description = "Register a new user account."
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public MyProfileResponse register(@Valid @RequestBody CreateUserRequest request) {
        return authService.register(request);
    }


    @Operation(
            summary = "Login",
            description = "Authenticate user and return access token."
    )
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }


    @Operation(
            summary = "Refresh Access Token",
            description = "Generate a new access token using refresh token."
    )
    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshRequest request) {
        return authService.refresh(request);
    }


    @Operation(
            summary = "Logout",
            description = "Logout user and invalidate refresh token."
    )
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
        authService.logout();
    }
}
