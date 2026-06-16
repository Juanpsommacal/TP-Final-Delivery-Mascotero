package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.RoleType;
import SSVG.TPFinalDeliveryMascotero.Model.Usuarios.RoleEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleEntity findByRoleName(RoleType roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role no encontrado"));
    }
}

