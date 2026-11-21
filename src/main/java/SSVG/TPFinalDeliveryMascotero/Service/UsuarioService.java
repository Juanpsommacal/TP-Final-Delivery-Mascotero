package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.*;
import SSVG.TPFinalDeliveryMascotero.Mapper.UsuarioMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Usuario.UsuarioCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.UsuarioResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.RoleType;
import SSVG.TPFinalDeliveryMascotero.Model.Usuarios.RoleEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Usuarios.UsuarioEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoded;

    // Todo usuario nuevo es USER. Un admin (o el superadmin) lo puede pasar a ADMIN despues.
    public UsuarioResponseDTO createUsuario(UsuarioCreateRequestDTO dto){
        validateUsername(dto.getEmail());
        RoleEntity roleEntity = roleService.findByRoleName(RoleType.USER);
        UsuarioEntity usuario = usuarioMapper.toEntity(dto);
        usuario.setRole(roleEntity);
        usuario.setActive(true);
        usuario.setPassword(passwordEncoded.encode(dto.getPassword()));
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    // USER -> ADMIN. Solo el superadmin (lo controla el @PreAuthorize del controller)
    public UsuarioResponseDTO elevatePrivilage(long idUsuario)
    {
        UsuarioEntity user = findById(idUsuario);
        validarNoEsSuperadmin(user, "No se puede modificar el rol del superadmin");
        if(user.getRole().getRoleName().equals(RoleType.ADMIN)) throw  new InvalidResourceStateException("EL usuario ya posee el rol de admin");
        user.setRole(roleService.findByRoleName(RoleType.ADMIN));
        return usuarioMapper.toResponse(usuarioRepository.save(user));
    }

    // ADMIN -> USER. Solo el superadmin (lo controla el @PreAuthorize del controller)
    public  UsuarioResponseDTO revokePrivilege(long idUsuario)
    {
        UsuarioEntity user = findById(idUsuario);
        validarNoEsSuperadmin(user, "No se puede modificar el rol del superadmin");
        if(user.getRole().getRoleName().equals(RoleType.USER)) throw  new InvalidResourceStateException("EL usuario ya posee el rol mas bajo");
        user.setRole(roleService.findByRoleName(RoleType.USER));
        return usuarioMapper.toResponse(usuarioRepository.save(user));
    }
    public List<UsuarioResponseDTO> getAll(){
        return usuarioRepository.findAll().stream().map(usuarioMapper::toResponse).toList();
    }
    public UsuarioResponseDTO getById(long idUsuario){
        return usuarioMapper.toResponse(usuarioRepository.findById(idUsuario).orElseThrow(()-> new ResourceNotFoundException("Usuario con id: " + idUsuario +" no existe")));
    }

    public UsuarioResponseDTO activateUser(Long idUsuario ){
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario).orElseThrow(()-> new ResourceNotFoundException("Usuario con id: " + idUsuario +" no existe"));
        if(usuario.isActive()) throw new InvalidResourceStateException("Acción redundante: El usuario seleccionado ya tiene el alta en el sistema.");
        usuario.setActive(true);
        return  usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }
    public UsuarioResponseDTO desactivateUser(Long idUsuario ){
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario).orElseThrow(()-> new ResourceNotFoundException("Usuario con id: " + idUsuario +" no existe"));
        validarNoEsSuperadmin(usuario, "No se puede dar de baja al superadmin");
        if(!usuario.isActive()) throw new InvalidResourceStateException("Acción redundante: El usuario seleccionado ya tiene la baja en el sistema.");
        usuario.setActive(false);
        return  usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }
    // El superadmin siempre existe y esta activo, asi que el sistema nunca se queda sin administrador
    private void validarNoEsSuperadmin(UsuarioEntity usuario, String mensaje) {
        if (usuario.getRole().getRoleName() == RoleType.SUPERADMIN) {
            throw new InvalidRevokePrivilege(mensaje);
        }
    }

    private UsuarioEntity findById(long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new ResourceNotFoundException("Usuario con id: " + idUsuario +" no existe"));
    }

    private void validateUsername(String email){
        if(usuarioRepository.existsByEmail(email)){
            throw new ResourceAlreadyExistsException("Ya existe un usuario registrado con el EMAIL:  " + email);
        }
    }
}
