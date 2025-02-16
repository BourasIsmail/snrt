package ma.snrt.snrt.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.snrt.snrt.Entities.Roles;
import ma.snrt.snrt.Services.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "Roles",
        description = "This API provides the capability to manipulate Unite from a Role Repository")
public class RolesController {
    @Autowired
    private RolesService rolesService;

    public ResponseEntity<List<Roles>> getRoles() {
        return ResponseEntity.ok(rolesService.getRoles());
    }

    public ResponseEntity<Roles> getRole(Long id) {
        return ResponseEntity.ok(rolesService.getRole(id));
    }

    public ResponseEntity<Roles> addRole(Roles role) {
        return ResponseEntity.ok(rolesService.addRole(role));
    }

    public ResponseEntity<String> deleteRole(Long id) {
        return ResponseEntity.ok(rolesService.deleteRole(id));
    }

    public ResponseEntity<Roles> updateRole(Long id, Roles role) {
        return ResponseEntity.ok(rolesService.updateRole(id, role));
    }
}
