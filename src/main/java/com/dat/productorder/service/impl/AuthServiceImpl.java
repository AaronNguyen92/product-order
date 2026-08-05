package com.dat.productorder.service.impl;

import com.dat.productorder.dto.request.user.CreateUserRequest;
import com.dat.productorder.dto.request.auth.LoginRequest;
import com.dat.productorder.dto.request.auth.RefreshRequest;
import com.dat.productorder.dto.response.auth.AuthResponse;
import com.dat.productorder.dto.response.user.MyProfileResponse;
import com.dat.productorder.entity.RefreshToken;
import com.dat.productorder.entity.Role;
import com.dat.productorder.entity.User;
import com.dat.productorder.enums.RoleName;
import com.dat.productorder.enums.UserStatus;
import com.dat.productorder.exception.DuplicateResourceException;
import com.dat.productorder.exception.ResourceNotFoundException;
import com.dat.productorder.mapper.UserMapper;
import com.dat.productorder.repository.RoleRepository;
import com.dat.productorder.repository.UserRepository;
import com.dat.productorder.security.CustomUserDetails;
import com.dat.productorder.security.CustomUserDetailsService;
import com.dat.productorder.security.jwt.JwtService;
import com.dat.productorder.service.AuthService;
import com.dat.productorder.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public MyProfileResponse register(CreateUserRequest request) {

        //check username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        //check email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        //check phone
        String phone = request.getPhone();

        if (phone != null && userRepository.existsByPhone(phone)) {
            throw new DuplicateResourceException("Phone already exists");
        }

        //mapper
        User user = userMapper.toEntity(request);

        //encode password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        //find role
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        //set default
        user.setRole(userRole);
        user.setStatus(UserStatus.ACTIVE);
        user.setDeleted(false);

        //save
        User savedUser = userRepository.save(user);

        log.info("User '{}' registered successfully.", user.getUsername());

        return userMapper.toMyProfileResponse(savedUser);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        //authenticate
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken( request.getUsername(), request.getPassword() )
        );

        //principal
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        //generate access token
        User user = userDetails.getUser();

        String accessToken = jwtService.generateAccessToken(userDetails);

        //create refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails);

        log.info("User '{}' logged in successfully.", user.getUsername());

        return AuthResponse.builder().accessToken(accessToken)
                .refreshToken(refreshToken.getToken()).build();
    }

    @Override
    public AuthResponse refresh(RefreshRequest request) {

        //verify refresh
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());

        //load user
        User user = refreshToken.getUser();

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());

        //generate token
        String accessToken = jwtService.generateAccessToken(userDetails);

        log.info("Refresh token issued for user '{}'.", user.getUsername());

        return AuthResponse.builder().accessToken(accessToken)
                .refreshToken(refreshToken.getToken()).build();
    }


    @Override
    public void logout() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        refreshTokenService.deleteRefreshToken(user);
    }

}
