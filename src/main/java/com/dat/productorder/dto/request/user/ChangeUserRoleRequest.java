package com.dat.productorder.dto.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeUserRoleRequest {

    @NotNull(message = "Role id is required")
    private Long roleId;
}
