package com.dat.productorder.service.impl;

import com.dat.productorder.dto.request.role.RoleRequest;
import com.dat.productorder.dto.response.role.RoleResponse;
import com.dat.productorder.entity.Role;
import com.dat.productorder.enums.RoleName;
import com.dat.productorder.exception.DuplicateResourceException;
import com.dat.productorder.exception.ResourceNotFoundException;
import com.dat.productorder.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void shouldReturnAllRoles(){

        // Arrange
        Role admin = new Role();
        admin.setId(1L);
        admin.setName(RoleName.ROLE_ADMIN);
        admin.setDescription("Administrator");

        Role user = new Role();
        user.setId(2L);
        user.setName(RoleName.ROLE_USER);
        user.setDescription("User");

        List<Role> roles = List.of(admin, user);

        when(roleRepository.findAll())
                .thenReturn(roles);

        // Act
        List<RoleResponse> result = roleService.getAllRoles();

        // Assert
        assertEquals(2, result.size());//check number of elements
        assertEquals(RoleName.ROLE_ADMIN.name(), result.get(0).getName());
        assertEquals(RoleName.ROLE_USER.name(), result.get(1).getName());

    }

    @Test
    void shouldReturnRoleById(){

        // Arrange
        final Long roleId = 1L;
        Role admin = new Role();
        admin.setId(roleId);
        admin.setName(RoleName.ROLE_ADMIN);
        admin.setDescription("Administrator");

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.of(admin));

        // Act
        RoleResponse result = roleService.getRoleById(roleId);

        // Assert
        assertEquals(admin.getId(), result.getId());
        assertEquals(RoleName.ROLE_ADMIN.name(), result.getName());
        assertEquals(admin.getDescription(), result.getDescription());

    }

    @Test
    void shouldThrowRoleNotFoundException() {

        // Arrange
        final Long roleId = 1L;

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.empty());

        // Act and Assert
        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class,
                () -> roleService.getRoleById(roleId)
        );

        assertEquals("Role not found", exception.getMessage());

    }

    @Test
    void shouldCreateRole() {

        // Arrange
        final RoleRequest request = new RoleRequest();
        request.setName(RoleName.ROLE_ADMIN);
        request.setDescription("Administrator");

        Role savedRole = new Role();
        savedRole.setId(1L);
        savedRole.setName(RoleName.ROLE_ADMIN);
        savedRole.setDescription("Administrator");

        when(roleRepository.existsByName(request.getName()))
                .thenReturn(false);

        when(roleRepository.save(any(Role.class)))
                .thenReturn(savedRole);

        // Act
        RoleResponse result = roleService.createRole(request);

        // Assert
        assertEquals(savedRole.getId(), result.getId());
        assertEquals(RoleName.ROLE_ADMIN.name(), result.getName());
        assertEquals(savedRole.getDescription(), result.getDescription());

        // Verify
        verify(roleRepository).save(any(Role.class));

    }

    @Test
    void shouldThrowDuplicateRoleException() {

        // Arrange
        RoleRequest request = new RoleRequest();
        request.setName(RoleName.ROLE_ADMIN);
        request.setDescription("Administrator");

        when(roleRepository.existsByName(request.getName()))
                .thenReturn(true);

        // Act and Assert
        DuplicateResourceException exception =
                assertThrows(
                        DuplicateResourceException.class,
                        () -> roleService.createRole(request)
                );

        assertEquals("Role name already exists", exception.getMessage());

        // Verify
        verify(roleRepository, never())
                .save(any(Role.class));

    }

    @Test
    void shouldDeleteRole() {

        // Arrange
        final Long roleId = 1L;

        Role role = new Role();
        role.setId(roleId);
        role.setName(RoleName.ROLE_ADMIN);
        role.setDescription("Administrator");

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.of(role));

        // Act
        roleService.deleteRole(roleId);

        // Assert
        verify(roleRepository).findById(roleId);
        verify(roleRepository).delete(role);

    }


    @Test
    void shouldThrowRoleNotFoundExceptionWhenDelete() {

        // Arrange
        final Long roleId = 1L;

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.empty());

        // Act and Assert
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> roleService.deleteRole(roleId)
                );

        assertEquals("Role not found", exception.getMessage());

        // Verify
        verify(roleRepository).findById(roleId);
        verify(roleRepository, never()).delete(any(Role.class));

    }

    @Test
    void shouldUpdateRoleWhenNameUnchanged(){

        // Arrange
        final Long roleId = 1L;

        RoleRequest request = new RoleRequest();
        request.setName(RoleName.ROLE_ADMIN);
        request.setDescription("New Description");

        Role oldRole = new Role();
        oldRole.setId(roleId);
        oldRole.setName(RoleName.ROLE_ADMIN);
        oldRole.setDescription("Old Description");

        Role updatedRole = new Role();
        updatedRole.setId(roleId);
        updatedRole.setName(RoleName.ROLE_ADMIN);
        updatedRole.setDescription("New Description");

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.of(oldRole));

        when(roleRepository.save(any(Role.class)))
                .thenReturn(updatedRole);

        // Act
        RoleResponse result = roleService.updateRole(roleId, request);

        //Assert
        assertEquals(updatedRole.getId(), result.getId());
        assertEquals(updatedRole.getName().name(), result.getName());
        assertEquals(updatedRole.getDescription(), result.getDescription());

        // Verify
        verify(roleRepository).findById(roleId);

        verify(roleRepository, never())
                .existsByName(any(RoleName.class));

        verify(roleRepository)
                .save(any(Role.class));

    }

    @Test
    void shouldUpdateRoleWhenNameChanged(){

        // Arrange
        final Long roleId = 1L;

        RoleRequest request = new RoleRequest();
        request.setName(RoleName.ROLE_USER);
        request.setDescription("New Description");

        Role oldRole = new Role();

        oldRole.setId(roleId);
        oldRole.setName(RoleName.ROLE_ADMIN);
        oldRole.setDescription("Old Description");

        Role updatedRole = new Role();

        updatedRole.setId(roleId);
        updatedRole.setName(RoleName.ROLE_USER);
        updatedRole.setDescription("New Description");

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.of(oldRole));

        when(roleRepository.existsByName(request.getName()))
                .thenReturn(false);

        when(roleRepository.save(any(Role.class)))
                .thenReturn(updatedRole);

        // Act
        RoleResponse result = roleService.updateRole(roleId, request);

        // Assert
        assertEquals(updatedRole.getId(), result.getId());
        assertEquals(updatedRole.getName().name(), result.getName());
        assertEquals(updatedRole.getDescription(), result.getDescription());

        // Verify
        verify(roleRepository).findById(roleId);

        verify(roleRepository)
                .existsByName(request.getName());

        verify(roleRepository)
                .save(any(Role.class));

    }

    @Test
    void shouldThrowRoleNotFoundExceptionWhenUpdate(){

        // Arrange
        final Long roleId = 1L;

        RoleRequest request = new RoleRequest();
        request.setName(RoleName.ROLE_ADMIN);
        request.setDescription("Administrator");

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.empty());

        // Act and Assert
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> roleService.updateRole(roleId, request)
                );

        assertEquals("Role not found", exception.getMessage());

        // Verify
        verify(roleRepository).findById(roleId);

        verify(roleRepository, never())
                .existsByName(any(RoleName.class));

        verify(roleRepository, never())
                .save(any(Role.class));

    }

    @Test
    void shouldThrowDuplicateRoleExceptionWhenUpdate(){

        // Arrange
        final Long roleId = 1L;

        RoleRequest request = new RoleRequest();
        request.setName(RoleName.ROLE_USER);
        request.setDescription("User");

        Role oldRole = new Role();

        oldRole.setId(roleId);
        oldRole.setName(RoleName.ROLE_ADMIN);
        oldRole.setDescription("Administrator");

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.of(oldRole));

        when(roleRepository.existsByName(request.getName()))
                .thenReturn(true);

        // Act and Assert
        DuplicateResourceException exception =
                assertThrows(
                        DuplicateResourceException.class,
                        () -> roleService.updateRole(roleId, request)
                );

        assertEquals("Role name already exists", exception.getMessage());

        // Verify
        verify(roleRepository).findById(roleId);

        verify(roleRepository)
                .existsByName(request.getName());

        verify(roleRepository, never())
                .save(any(Role.class));

    }

}
