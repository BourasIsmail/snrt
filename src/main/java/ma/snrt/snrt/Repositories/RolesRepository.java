package com.unity.erm.unity_erm.Repositories;

import com.unity.erm.unity_erm.Entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
    Roles findByRole(String role);
}
