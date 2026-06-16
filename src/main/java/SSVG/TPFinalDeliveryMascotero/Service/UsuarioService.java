package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.ResourceAlreadyExistsException;
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

    private void validateUsername(String email){
        if(usuarioRepository.existsByEmail(email)){
            throw new ResourceAlreadyExistsException("Ya existe un usuario registrado con el EMAIL:  " + email);
        }
    }
}
