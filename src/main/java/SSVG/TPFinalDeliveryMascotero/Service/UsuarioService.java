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

    public UsuarioResponseDTO createUsuario(UsuarioCreateRequestDTO dto){
        validateUsername(dto.getEmail());
        RoleEntity roleEntity = roleService.findByRoleName(RoleType.ADMIN);
        UsuarioEntity usuario = usuarioMapper.toEntity(dto);
        usuario.setRole(roleEntity);
        usuario.setActive(true);
        usuario.setPassword(passwordEncoded.encode(dto.getPassword()));
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }
    public UsuarioResponseDTO elevatePrivilage(long idUsuario)
    {
       UsuarioEntity user = usuarioRepository.findById(idUsuario).orElseThrow(()-> new ResourceNotFoundException("Usuario con id: " + idUsuario +" no existe"));
        if(user.getRole().getRoleName().equals(RoleType.ADMIN)) throw  new InvalidResourceStateException("EL usuario ya posee el rol de admin");
        user.setRole(roleService.findByRoleName(RoleType.ADMIN));
        return usuarioMapper.toResponse(usuarioRepository.save(user));
    }

    public  UsuarioResponseDTO revokePrivilege(long idUsuario)
    {
        UsuarioEntity user = usuarioRepository.findById(idUsuario).orElseThrow(()-> new ResourceNotFoundException("Usuario con id: " + idUsuario +" no existe"));
        if(user.getRole().getRoleName().equals(RoleType.USER)) throw  new InvalidResourceStateException("EL usuario ya posee el rol mas bajo");
        RoleEntity role = roleService.findByRoleName(RoleType.USER);
        minimoDeAdmin(role.getId());
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
        if(!usuario.isActive()) throw new InvalidResourceStateException("Acción redundante: El usuario seleccionado ya tiene la baja en el sistema.");
        RoleEntity role = roleService.findByRoleName(usuario.getRole().getRoleName());
        minimoDeAdmin(role.getId());
        usuario.setActive(false);
        return  usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }
    private void minimoDeAdmin(long idRole)
    {
        int cantidadRole = usuarioRepository.countByRole_id(idRole);
        if(cantidadRole == 1) throw new InvalidRevokePrivilege("No se puede modificar al unico Admin disponible");
    }

    private void validateUsername(String email){
        if(usuarioRepository.existsByEmail(email)){
            throw new ResourceAlreadyExistsException("Ya existe un usuario registrado con el EMAIL:  " + email);
        }
    }
}
