package com.dat.productorder.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeMyPasswordRequest {

    @NotBlank(message = "Current password is required")
    @Size(min = 6, max = 50, message = "Current password must be between 6 and 50 characters")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 6, max = 50, message = "New password must be between 6 and 50 characters")
    private String newPassword;

    @NotBlank(message = "Confirm password is required")
    @Size(min = 6, max = 50, message = "Confirm password must be between 6 and 50 characters")
    private String confirmPassword;
}
