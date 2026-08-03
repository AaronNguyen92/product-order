package com.dat.productorder.service;

import com.dat.productorder.dto.request.role.RoleRequest;
import com.dat.productorder.dto.response.role.RoleResponse;

import java.util.List;

public interface RoleService {
    List<RoleResponse> getAllRoles();
    RoleResponse getRoleById(Long id);
    RoleResponse updateRole(Long id, RoleRequest request);
    RoleResponse createRole(RoleRequest request);
    void deleteRole(Long id);
}
