package com.dat.productorder.config;

import com.dat.productorder.entity.Role;
import com.dat.productorder.entity.User;
import com.dat.productorder.enums.RoleName;
import com.dat.productorder.enums.UserStatus;
import com.dat.productorder.repository.RoleRepository;
import com.dat.productorder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        initializeRoles();
        initializeAdmin();

    }

    private void initializeRoles() {

        createRoleIfNotExists(RoleName.ROLE_ADMIN);
        createRoleIfNotExists(RoleName.ROLE_USER);

    }

    private Role createRoleIfNotExists(RoleName roleName) {

        return roleRepository.findByName(roleName).orElseGet(
                () -> {
                    Role role = Role.builder()
                            .name(roleName)
                            .description(getRoleDescription(roleName))
                            .build();

                    return roleRepository.save(role);
                });

    }

    private void initializeAdmin() {

        if (userRepository.existsByUsername("admin")) {
            return;
        }

        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN not found"));

        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("123456"))
                .fullName("Administrator")
                .email("admin@example.com")
                .status(UserStatus.ACTIVE)
                .role(adminRole)
                .build();

        userRepository.save(admin);

    }

    private String getRoleDescription(RoleName roleName) {

        return switch (roleName) {
            case ROLE_ADMIN -> "System administrator";
            case ROLE_USER -> "Normal user";
        };

    }

}