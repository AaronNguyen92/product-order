package com.dat.productorder.service.impl;

import com.dat.productorder.dto.request.user.ChangeMyPasswordRequest;
import com.dat.productorder.dto.request.user.ChangeUserRoleRequest;
import com.dat.productorder.dto.request.user.ChangeUserStatusRequest;
import com.dat.productorder.dto.request.user.UpdateMyProfileRequest;
import com.dat.productorder.dto.response.user.MyProfileResponse;
import com.dat.productorder.dto.response.user.UserResponse;
import com.dat.productorder.entity.Role;
import com.dat.productorder.entity.User;
import com.dat.productorder.exception.BadRequestException;
import com.dat.productorder.exception.DuplicateResourceException;
import com.dat.productorder.exception.ResourceNotFoundException;
import com.dat.productorder.mapper.UserMapper;
import com.dat.productorder.repository.RoleRepository;
import com.dat.productorder.repository.UserRepository;
import com.dat.productorder.service.FileStorageService;
import com.dat.productorder.service.RefreshTokenService;
import com.dat.productorder.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;
    private final RefreshTokenService refreshTokenService;
    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png");
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png");

    @Override
    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();

        List<UserResponse> responses = new ArrayList<>();

        for (User user : users){
            UserResponse response = userMapper.toResponse(user);
            responses.add(response);
        }

        return responses;

    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found"));

        return userMapper.toResponse(user);

    }

    @Override
    public UserResponse changeUserStatus(Long id, ChangeUserStatusRequest request) {

        User user = getActiveUser(id);

        user.setStatus(request.getStatus());

        User updatedUser = userRepository.save(user);

        log.info("Admin changed status of user '{}' to '{}'.", user.getUsername(), user.getStatus());

        return userMapper.toResponse(updatedUser);

    }

    @Override
    public UserResponse changeUserRole(Long id, ChangeUserRoleRequest request) {

        User user = getActiveUser(id);

        Role role = roleRepository.findById(request.getRoleId()).orElseThrow(
                () -> new ResourceNotFoundException("Role not found"));

        user.setRole(role);

        User updatedUser = userRepository.save(user);

        log.info("Admin changed role of user '{}' to '{}'.", user.getUsername(), role.getName());

        return userMapper.toResponse(updatedUser);

    }

    @Override
    public void deleteUser(Long id) {

        User user = getActiveUser(id);

        user.setDeleted(true);

        userRepository.save(user);

        refreshTokenService.deleteRefreshToken(user);

        log.info("Admin deleted user '{}'.", user.getUsername());

    }

    @Override
    public MyProfileResponse getMyProfile() {

        User user = getCurrentUser();

        return userMapper.toMyProfileResponse(user);

    }

    @Override
    public MyProfileResponse updateMyProfile(UpdateMyProfileRequest request) {

        User user = getCurrentUser();

        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new DuplicateResourceException("Email already exists");
        }

        String phone = request.getPhone();

        if (phone != null
                && !phone.equals(user.getPhone())
                && userRepository.existsByPhone(phone)) {

            throw new DuplicateResourceException("Phone already exists");
        }

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(phone);

        User updatedUser = userRepository.save(user);

        log.info("User '{}' updated profile successfully.", user.getUsername());

        return userMapper.toMyProfileResponse(updatedUser);

    }

    @Override
    public void changeMyPassword(ChangeMyPasswordRequest request) {

        User user = getCurrentUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("New password must be different from current password");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Confirm password does not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        log.info("User '{}' changed password successfully.", user.getUsername());

    }

    @Override
    public void deleteMyAccount() {

        User user = getCurrentUser();

        user.setDeleted(true);

        userRepository.save(user);

        refreshTokenService.deleteRefreshToken(user);

        log.info("User '{}' deleted their account.", user.getUsername());

    }

    @Override
    public MyProfileResponse uploadAvatar(MultipartFile file) {

        User user = getCurrentUser();

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Image file is required");
        }

        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new BadRequestException("Image size must not exceed 10 MB");
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank()) {
            throw new BadRequestException("Invalid image file");
        }

        int lastDot = originalFilename.lastIndexOf('.');

        if (lastDot == -1) {
            throw new BadRequestException("Image must have a valid extension");
        }

        String extension = originalFilename.substring(lastDot + 1).toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BadRequestException("Only JPG, JPEG and PNG images are allowed");
        }

        String contentType = file.getContentType();

        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new BadRequestException("Only JPG, JPEG and PNG images are allowed");
        }

        String fileName = "user-" + user.getId() + "." + extension;

        String avatarUrl = fileStorageService.store(file, "avatars", fileName);

        user.setAvatar(avatarUrl);

        User updatedUser = userRepository.save(user);

        log.info("User '{}' updated avatar successfully.", user.getUsername());

        return userMapper.toMyProfileResponse(updatedUser);

    }

    private User getCurrentUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );

        if (user.isDeleted()) {
            throw new ResourceNotFoundException("User not found");
        }

        return user;

    }

    private User getActiveUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.isDeleted()) {
            throw new ResourceNotFoundException("User not found");
        }

        return user;
    }

}
