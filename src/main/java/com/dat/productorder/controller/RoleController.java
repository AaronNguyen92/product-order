package com.dat.productorder.controller;

import com.dat.productorder.dto.request.role.RoleRequest;
import com.dat.productorder.dto.response.role.RoleResponse;
import com.dat.productorder.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Role Management",
        description = "APIs for managing Role users"
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "Admin - Get Role List")
    @GetMapping
    public List<RoleResponse> getAllRoles() {

        return roleService.getAllRoles();

    }


    @Operation(summary = "Admin -  Get Role Detail")
    @GetMapping("/{id}")
    public RoleResponse getRoleById(@PathVariable  Long id){

        return roleService.getRoleById(id);

    }


    @Operation(summary = "Admin -  Update Role")
    @PutMapping("/{id}")
    public RoleResponse updateRole(@PathVariable Long id, @Valid @RequestBody RoleRequest request){

        return roleService.updateRole(id, request);

    }


    @Operation(summary = "Admin - Create Role")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleResponse createRole(@Valid @RequestBody RoleRequest request){

        return roleService.createRole(request);

    }


    @Operation(summary = "Admin - Delete Role")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable Long id){

        roleService.deleteRole(id);

    }
}
