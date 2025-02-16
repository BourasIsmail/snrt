package com.unity.erm.unity_erm.Services;

import com.unity.erm.unity_erm.Entities.Roles;
import com.unity.erm.unity_erm.Repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RolesService {
    @Autowired
    private RolesRepository rolesRepository;

    public Roles getRole(Long id) {
        Optional<Roles> role = rolesRepository.findById(id);
        if(role.isPresent()) {
            return role.get();
        }
        else {
            throw new RuntimeException("Role not found");
        }
    }

    public Roles addRole(Roles role) {
        return rolesRepository.save(role);
    }

    public String deleteRole(Long id) {
        Roles role = getRole(id);
        rolesRepository.delete(role);
        return "Role deleted";
    }

    public Roles updateRole(Long id, Roles role) {
        Roles existingRole = getRole(role.getId());
        existingRole.setRole(role.getRole());
        return rolesRepository.save(existingRole);
    }
}
