package com.dat.productorder.dto.response.user;

import com.dat.productorder.dto.response.role.RoleSummaryResponse;
import com.dat.productorder.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String username;

    private String fullName;

    private String email;

    private String phone;

    private String avatar;

    private UserStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private RoleSummaryResponse role;

    private boolean deleted;

}
