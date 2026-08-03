package com.dat.productorder.service.impl;

import com.dat.productorder.dto.request.role.RoleRequest;
import com.dat.productorder.dto.response.role.RoleResponse;
import com.dat.productorder.entity.Role;
import com.dat.productorder.exception.DuplicateResourceException;
import com.dat.productorder.exception.ResourceNotFoundException;
import com.dat.productorder.repository.RoleRepository;
import com.dat.productorder.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<RoleResponse> getAllRoles() {

        List<Role> roles = roleRepository.findAll();

        List<RoleResponse> responses = new ArrayList<>();

        for(Role role : roles){
            responses.add(mapToResponse(role));
        }

        return responses;

    }

    @Override
    public RoleResponse getRoleById(Long id) {

        Role role = roleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Role not found"));

        return mapToResponse(role);

    }

    @Override
    public RoleResponse updateRole(Long id, RoleRequest request) {

        Role oldRole = roleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Role not found") );

        if (!oldRole.getName().equals(request.getName())
                && roleRepository.existsByName(request.getName())) {

            throw new DuplicateResourceException("Role name already exists");
        }

        oldRole.setName(request.getName());
        oldRole.setDescription(request.getDescription());
        Role updatedRole = roleRepository.save(oldRole);

        return mapToResponse(updatedRole);
    }

    @Override
    public RoleResponse createRole(RoleRequest request) {

        if(roleRepository.existsByName(request.getName())){
            throw new DuplicateResourceException("Role name already exists");
        }

        Role role = new Role();

        role.setName(request.getName());
        role.setDescription(request.getDescription());

        Role savedRole = roleRepository.save(role);

        return mapToResponse(savedRole);

    }

    @Override
    public void deleteRole(Long id) {

        Role oldRole = roleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Role not found"));

        roleRepository.delete(oldRole);

    }

    private RoleResponse mapToResponse(Role role){

        RoleResponse response = new RoleResponse();

        response.setId(role.getId());
        response.setName(role.getName());
        response.setDescription(role.getDescription());

        return response;

    }
}
