package com.dat.productorder.controller;

import com.dat.productorder.dto.request.user.ChangeMyPasswordRequest;
import com.dat.productorder.dto.request.user.ChangeUserRoleRequest;
import com.dat.productorder.dto.request.user.ChangeUserStatusRequest;
import com.dat.productorder.dto.request.user.UpdateMyProfileRequest;
import com.dat.productorder.dto.response.user.MyProfileResponse;
import com.dat.productorder.dto.response.user.UserResponse;
import com.dat.productorder.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/status")
    public UserResponse changeUserStatus(@PathVariable Long id
            ,@Valid @RequestBody ChangeUserStatusRequest request){
        return userService.changeUserStatus(id, request);
    }

    @PutMapping("/{id}/role")
    public UserResponse changeUserRole( @PathVariable Long id
            ,@Valid @RequestBody ChangeUserRoleRequest request){
        return userService.changeUserRole(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public MyProfileResponse getMyProfile() {
        return userService.getMyProfile();
    }

    @PutMapping("/me")
    public MyProfileResponse updateMyProfile(@Valid @RequestBody UpdateMyProfileRequest request) {
        return userService.updateMyProfile(request);
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changeMyPassword(@Valid @RequestBody ChangeMyPasswordRequest request) {
        userService.changeMyPassword(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount() {

        userService.deleteMyAccount();

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/avatar")
    public MyProfileResponse uploadAvatar(@RequestParam("file") MultipartFile file) {
        return userService.uploadAvatar(file);
    }
}
