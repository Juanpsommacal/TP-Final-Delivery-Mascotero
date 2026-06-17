package SSVG.TPFinalDeliveryMascotero.Controller;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.UsuarioResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDTO>> getAll()
    {
        return ResponseEntity.ok().body(usuarioService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> getAll(@PathVariable long id){
        return ResponseEntity.ok().body(usuarioService.getById(id));
    }

    @PatchMapping("elevate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> elevatePrivilage(@PathVariable long id) {
        return ResponseEntity.ok().body(usuarioService.elevatePrivilage(id));
    }
    @PatchMapping("revoke/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> downgradePrivilege(@PathVariable long id) {
        return ResponseEntity.ok().body(usuarioService.revokePrivilege(id));
    }
    @PatchMapping("activate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> activatePrivilage(@PathVariable long id) {
        return ResponseEntity.ok().body(usuarioService.activateUser(id));
    }
    @PatchMapping("deactivate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> deactivatePrivilege(@PathVariable long id) {
        return ResponseEntity.ok().body(usuarioService.desactivateUser(id));
    }


}
