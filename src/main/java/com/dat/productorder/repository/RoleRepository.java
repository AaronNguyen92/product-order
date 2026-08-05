package com.dat.productorder.repository;
import com.dat.productorder.entity.Role;
import com.dat.productorder.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(RoleName name);
    Optional<Role> findByName(RoleName name);
}
