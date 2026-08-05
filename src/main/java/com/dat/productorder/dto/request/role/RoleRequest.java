package com.dat.productorder.dto.request.role;

import com.dat.productorder.enums.RoleName;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {

    @NotNull(message = "Role name is required")
    @Size(max = 50, message = "Role name must not exceed 50 characters")
    private RoleName name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

}
