package SSVG.TPFinalDeliveryMascotero.Config;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.RoleType;
import SSVG.TPFinalDeliveryMascotero.Model.Usuarios.RoleEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    @Override
    public void run(String... args) throws Exception {
        for (RoleType nombreRol : RoleType.values()) {
            if (!roleRepository.existsByRoleName(nombreRol)) {
                RoleEntity nuevoRol = new RoleEntity();
                nuevoRol.setRoleName(nombreRol);
                roleRepository.save(nuevoRol);
                System.out.println("Rol creado automáticamente: " + nombreRol);
            }
        }
    }
}
