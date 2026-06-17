package SSVG.TPFinalDeliveryMascotero.Mapper;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Usuario.UsuarioCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.UsuarioResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Usuarios.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioEntity toEntity(UsuarioCreateRequestDTO dto);

    @Mapping(target = "role",source = "usuario.role.roleName")
    UsuarioResponseDTO toResponse(UsuarioEntity usuario);
}
