package com.dat.productorder.dto.request.user;

import com.dat.productorder.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeUserStatusRequest {

    @NotNull(message = "Status is required")
    private UserStatus status;
}
