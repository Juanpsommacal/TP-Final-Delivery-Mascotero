package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.RoleType;
import SSVG.TPFinalDeliveryMascotero.Model.Usuarios.RoleEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleEntity findByRoleName(RoleType roleName) {
        //og.info("Buscando Role por RoleName: {}", roleName);

        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role no encontrado"));
    }
}

//    public List<RoleResponseDto> findAll() {
//        return roleRepository.findAll()
//                .stream()
//                .map(this::toResponse)
//                .toList();
//    }
//
//    public RoleResponseDto findById(Long id) {
//        RoleEntity roleEntity = roleRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));
//
//        return toResponse(roleEntity);
//    }

//    public RoleResponseDto create(RoleRequestDto request) {
//
//        if (roleRepository.existsByRoleName(request.name())) {
//            throw new DuplicatedEntityException("El rol ya existe");
//        }
//
//        RoleEntity roleEntity = RoleEntity.builder()
//                .roleName(request.name())
//                .build();
//
//        RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
//
//        return toResponse(savedRoleEntity);
//    }
//
//    private RoleResponseDto toResponse(RoleEntity roleEntity) {
//        return new RoleResponseDto(roleEntity.getRoleName());
//    }
//}
