package com.dat.productorder.dto.response.user;

import com.dat.productorder.dto.response.role.RoleSummaryResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyProfileResponse {
    private Long id;

    private String username;

    private String fullName;

    private String email;

    private String phone;

    private String avatar;

    private LocalDateTime createdAt;

    private RoleSummaryResponse role;
}
