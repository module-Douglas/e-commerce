package io.github.douglas.ms_accounts.resource;

import io.github.douglas.ms_accounts.dto.RoleDTO;
import io.github.douglas.ms_accounts.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<?> registerRole(@RequestBody RoleDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roleService.registerRole(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable("id") UUID id) {
        roleService.deleteRole(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
